'use client';

import { Badge, Button, Switch, TextInput, Textarea } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { Form, Formik } from 'formik';
import { useEffect, useMemo, useState } from 'react';
import { useSelector } from 'react-redux';
import * as Yup from 'yup';

import { useAppDispatch } from '@/lib/hooks';
import { RootState } from '@/lib/store';
import {
  createDesignation,
  deleteDesignationById,
  fetchDesignations,
  updateDesignationById,
} from '../../../../lib/features/employees/workstructure/designationApi';
import { DesignationRow } from '../../../../lib/features/employees/workstructure/designationSlice';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import SolidButton from '@/components/common/button/SolidButton';
import Modal from '@/components/common/Modal';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { BsThreeDots } from 'react-icons/bs';

/* ---------- schema (boolean now) ---------- */
const DesigSchema = Yup.object({
  name: Yup.string().min(1).max(100).required('Name is required'),
  description: Yup.string().max(200).nullable(),
  status: Yup.boolean().required(),
});

function StatusBadge({ value }: { value: boolean }) {
  return value ? (
    <Badge color="green" variant="light" radius="sm">
      Active
    </Badge>
  ) : (
    <Badge color="gray" variant="light" radius="sm">
      Inactive
    </Badge>
  );
}

function DesigForm({
  initial,
  onSubmit,
  onCancel,
}: {
  initial: { name: string; description: string | null; status: boolean };
  onSubmit: (v: {
    name: string;
    description?: string | null;
    status: boolean;
  }) => Promise<void>;
  onCancel: () => void;
  submitText: string;
}) {
  return (
    <Formik
      initialValues={initial}
      validationSchema={DesigSchema}
      enableReinitialize
      onSubmit={async (v, { setSubmitting }) => {
        try {
          await onSubmit(v);
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ values, setFieldValue, isSubmitting, errors, touched }) => (
        <Form className="space-y-4">
          <TextInput
            label="Name"
            required
            value={values.name}
            onChange={e => setFieldValue('name', e.currentTarget.value)}
            error={
              touched.name && errors.name ? String(errors.name) : undefined
            }
          />
          <Textarea
            label="Description"
            minRows={2}
            value={values.description ?? ''}
            onChange={e => setFieldValue('description', e.currentTarget.value)}
          />

          {/* Boolean status switch */}
          <Switch
            checked={!!values.status}
            onChange={e => setFieldValue('status', e.currentTarget.checked)}
            label="Active"
          />
          {touched.status && errors.status ? (
            <div className="text-xs text-red-600">{String(errors.status)}</div>
          ) : null}

          {/* <div className="w-full flex justify-end gap-2 pt-1">
            <SolidButton type="button" variant="solid" onClick={onCancel} disabled={isSubmitting}>
              Cancel
            </SolidButton>
            <SolidButton type="submit" variant="primary" disabled={isSubmitting}>
              {isSubmitting ? 'Saving…' : submitText}
            </SolidButton>
          </div> */}
          <div className="flex items-center gap-5 justify-end mt-8">
            <Button
              onClick={onCancel}
              disabled={isSubmitting}
              color="red"
              type="button"
            >
              Cancel
            </Button>
            <Button type="submit">{isSubmitting ? 'Saving…' : 'Save'}</Button>
          </div>
        </Form>
      )}
    </Formik>
  );
}

export default function DesignationSection() {
  const dispatch = useAppDispatch();
  const state = useSelector((s: RootState) => s.designation);
  const list = state.list as DesignationRow[];

  const [openCreate, setOpenCreate] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [current, setCurrent] = useState<DesignationRow | null>(null);

  // Pagination state
  const [pageIndex, setPageIndex] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  useEffect(() => {
    dispatch<any>(fetchDesignations(pageIndex + 1, pageSize));
  }, [dispatch, pageIndex, pageSize]);

  const columns: ColumnDef<DesignationRow, any>[] = useMemo(
    () => [
      // { accessorKey: 'id', header: 'ID' },
      { accessorKey: 'name', header: 'Name' },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ row }) => <StatusBadge value={!!row.original.status} />,
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setCurrent(row.original);
                  setOpenEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  setCurrent(row.original);
                  setOpenDelete(true);
                },
              },
            ]}
          >
            <BsThreeDots />
          </RowActionDropdown>
        ),
      },
    ],
    []
  );

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <DataTable<DesignationRow>
        data={list}
        columns={columns}
        headerLeft={<Breadcrumbs />}
        searchPlaceholder="Search designations..."
        csvFileName="designations"
        onPressAdd={() => setOpenCreate(true)}
        hideExport
        manualPagination
        pageCount={state.page?.lastPage ?? 0}
        pagination={{ pageIndex, pageSize }}
        onPaginationChange={({ pageIndex, pageSize }) => {
          setPageIndex(pageIndex);
          setPageSize(pageSize);
        }}
        totalRows={state.page?.total}
        loading={state.loading}
      />
      {state.error && (
        <p className="mt-3 text-sm text-red-600">{state.error}</p>
      )}

      {/* Create */}
      <Modal
        isOpen={openCreate}
        onClose={() => setOpenCreate(false)}
        title="Create Designation"
        size="lg"
      >
        <DesigForm
          initial={{ name: '', description: '', status: true }}
          submitText="Save"
          onCancel={() => setOpenCreate(false)}
          onSubmit={async v => {
            try {
              await dispatch<any>(createDesignation(v)); // v.status is boolean
              notifications.show({
                color: 'green',
                title: 'Designation created',
                message: v.name,
              });
              setOpenCreate(false);
              dispatch<any>(fetchDesignations(pageIndex + 1, pageSize));
            } catch (e: any) {
              notifications.show({
                color: 'red',
                title: 'Create failed',
                message: e?.response?.data?.message ?? e?.message ?? 'Failed',
              });
            }
          }}
        />
      </Modal>

      {/* Edit */}
      <Modal
        isOpen={openEdit}
        onClose={() => setOpenEdit(false)}
        title="Edit Designation"
        size="lg"
      >
        {current && (
          <DesigForm
            initial={{
              name: current.name ?? '',
              description: current.description ?? '',
              status: !!current.status, // boolean
            }}
            submitText="Save changes"
            onCancel={() => setOpenEdit(false)}
            onSubmit={async v => {
              try {
                await dispatch<any>(updateDesignationById(current.id, v)); // v.status is boolean
                notifications.show({
                  color: 'green',
                  title: 'Designation updated',
                  message: v.name,
                });
                setOpenEdit(false);
                dispatch<any>(fetchDesignations(pageIndex + 1, pageSize));
              } catch (e: any) {
                notifications.show({
                  color: 'red',
                  title: 'Update failed',
                  message: e?.response?.data?.message ?? e?.message ?? 'Failed',
                });
              }
            }}
          />
        )}
      </Modal>

      {/* Delete */}
      <Modal
        isOpen={openDelete}
        onClose={() => setOpenDelete(false)}
        title="Delete designation"
      >
        <p className="text-sm text-gray-700">
          Delete <span className="font-medium">{current?.name}</span>?
        </p>
        <div className="mt-4 flex items-center justify-end gap-2">
          <SolidButton onClick={() => setOpenDelete(false)} variant="solid">
            Cancel
          </SolidButton>
          <SolidButton
            variant="primary"
            onClick={async () => {
              try {
                await dispatch<any>(deleteDesignationById(current!.id));
                notifications.show({
                  color: 'green',
                  title: 'Designation deleted',
                  message: current?.name,
                });
                setOpenDelete(false);
                dispatch<any>(fetchDesignations(pageIndex + 1, pageSize));
              } catch (e: any) {
                notifications.show({
                  color: 'red',
                  title: 'Delete failed',
                  message: e?.response?.data?.message ?? e?.message ?? 'Failed',
                });
              }
            }}
          >
            Delete
          </SolidButton>
        </div>
      </Modal>
    </div>
  );
}
