'use client';

import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import Paper from '@/components/layout/Paper';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import React, { useMemo, useState } from 'react';
import Breadcrumbs from '../../../components/common/Breadcrumbs';

import { BsThreeDots } from 'react-icons/bs';
import {
  DataTable,
  SelectFilterDef,
} from '../../../components/common/table/DataTable';
import {
  useDeleteLeaveYearMutation,
  useGetPaginatedLeaveYearsQuery,
} from '../../../lib/features/leave/leaveYear/leaveYearRTKAPI';
import { LeaveYearType } from '../../../lib/types/leave';
import LeaveYearForm from './LeaveYearForm';

export default function YearPage() {
  const [item, setItem] = useState<LeaveYearType | null>(null);
  const [action, setAction] = React.useState<null | 'create' | 'edit'>(null);
  const [deleteItem] = useDeleteLeaveYearMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedLeaveYearsQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<LeaveYearType, LeaveYearType>[] = useMemo(
    () => [
      {
        id: 'select',
        enableSorting: false,
        enableHiding: false,
        header: ({ table }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={table.getIsAllPageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        ),
        cell: ({ row }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={row.getIsSelected()}
            disabled={!row.getCanSelect()}
            onChange={row.getToggleSelectedHandler()}
          />
        ),
      },
      { accessorKey: 'id', header: 'ID', filterFn: 'equals' },
      {
        accessorKey: 'startDate',
        header: 'From Date',
      },
      {
        accessorKey: 'endDate',
        header: 'End Date',
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
                  setAction('edit');
                  setItem(row.original);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  modals.openConfirmModal({
                    title: 'Delete confirmation',
                    centered: true,
                    children: (
                      <Text size="sm">
                        Are you sure you want to delete ? This action is
                        destructive and you will have to contact support to
                        restore your data.
                      </Text>
                    ),
                    labels: {
                      confirm: 'Delete',
                      cancel: 'Cancel',
                    },
                    size: 'lg',
                    confirmProps: { color: 'red' },
                    onCancel: () => {
                      setAction(null);
                      setItem(null);
                    },
                    onConfirm: async () => {
                      try {
                        await deleteItem({ id: row.original.id });
                        notifications.show({
                          title: `Leave Year Deleted successfully`,
                          message: '',
                          color: 'red',
                        });
                      } catch (error) {
                        notifications.show({
                          title: 'Failed to delete',
                          message: 'Something went wrong',
                        });
                      }
                    },
                  });
                },
              },
            ]}
          >
            <BsThreeDots />
          </RowActionDropdown>
        ),
      },
    ],
    [deleteItem]
  );

  const selectFilters: SelectFilterDef<LeaveYearType>[] = [];

  return (
    <Paper>
      <Breadcrumbs />

      {/* Modal */}
      <Modal
        // size={'md'}
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        <div>
          <LeaveYearForm
            action={action as 'edit'}
            onClose={() => {
              setAction(null);
            }}
            data={action === 'edit' && (item as any)}
          />
        </div>
      </Modal>

      {/* Table */}
      <DataTable<LeaveYearType>
        data={data?.data ?? []}
        columns={columns}
        selectFilters={selectFilters}
        initialColumnVisibility={{}}
        searchPlaceholder="Search..."
        csvFileName="leave"
        enableRowSelection
        onPressAdd={() => {
          setAction('create');
        }}
        manualPagination
        pageCount={data?.meta?.lastPage ?? 0}
        pagination={{ pageIndex, pageSize }}
        onPaginationChange={({ pageIndex, pageSize }) => {
          setPageIndex(pageIndex);
          setPageSize(pageSize);
        }}
        totalRows={data?.meta?.total}
        loading={isLoading}
      />
    </Paper>
  );
}
