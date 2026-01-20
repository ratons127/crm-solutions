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
  createGrade,
  deleteGradeById,
  fetchGrades,
  updateGradeById,
} from '../../../../lib/features/employees/workstructure/gradeApi';
import { GradeRow } from '../../../../lib/features/employees/workstructure/gradeSlice';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import SolidButton from '@/components/common/button/SolidButton';
import Modal from '@/components/common/Modal';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { BsThreeDots } from 'react-icons/bs';

/* ---------- schema ---------- */
const GradeSchema = Yup.object({
  code: Yup.string().min(1).max(30).required('Code is required'),
  name: Yup.string().min(1).max(100).required('Name is required'),
  description: Yup.string().max(200).nullable(),
  status: Yup.boolean().required(), // boolean now
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

function GradeForm({
  initial,
  onSubmit,
  onCancel,
}: {
  initial: {
    code: string;
    name: string;
    description: string | null;
    status: boolean;
  };
  onSubmit: (v: {
    code: string;
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
      validationSchema={GradeSchema}
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
            label="Code"
            required
            value={values.code}
            onChange={e => setFieldValue('code', e.currentTarget.value)}
            error={
              touched.code && errors.code ? String(errors.code) : undefined
            }
          />
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

export default function GradeSection() {
  const dispatch = useAppDispatch();
  const state = useSelector((s: RootState) => s.grade);
  const list = state.list as GradeRow[];

  const [openCreate, setOpenCreate] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [current, setCurrent] = useState<GradeRow | null>(null);

  // Pagination state
  const [pageIndex, setPageIndex] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  useEffect(() => {
    dispatch<any>(fetchGrades(pageIndex + 1, pageSize));
  }, [dispatch, pageIndex, pageSize]);

  const columns: ColumnDef<GradeRow, any>[] = useMemo(
    () => [
      // { accessorKey: 'id', header: 'ID' },
      { accessorKey: 'code', header: 'Code' },
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
      <DataTable<GradeRow>
        data={list}
        columns={columns}
        headerLeft={<Breadcrumbs />}
        searchPlaceholder="Search grades..."
        csvFileName="grades"
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
        title="Create Grade"
        size="lg"
      >
        <GradeForm
          initial={{ code: '', name: '', description: '', status: true }}
          submitText="Save"
          onCancel={() => setOpenCreate(false)}
          onSubmit={async v => {
            try {
              await dispatch<any>(createGrade(v)); // v.status is boolean
              notifications.show({
                color: 'green',
                title: 'Grade created',
                message: v.name,
              });
              setOpenCreate(false);
              dispatch<any>(fetchGrades(pageIndex + 1, pageSize));
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
        title="Edit Grade"
        size="lg"
      >
        {current && (
          <GradeForm
            initial={{
              code: current.code ?? '',
              name: current.name ?? '',
              description: current.description ?? '',
              status: !!current.status, // boolean
            }}
            submitText="Save changes"
            onCancel={() => setOpenEdit(false)}
            onSubmit={async v => {
              try {
                await dispatch<any>(updateGradeById(current.id, v)); // v.status is boolean
                notifications.show({
                  color: 'green',
                  title: 'Grade updated',
                  message: v.name,
                });
                setOpenEdit(false);
                dispatch<any>(fetchGrades(pageIndex + 1, pageSize));
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
        title="Delete grade"
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
                await dispatch<any>(deleteGradeById(current!.id));
                notifications.show({
                  color: 'green',
                  title: 'Grade deleted',
                  message: current?.name,
                });
                setOpenDelete(false);
                dispatch<any>(fetchGrades(pageIndex + 1, pageSize));
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
