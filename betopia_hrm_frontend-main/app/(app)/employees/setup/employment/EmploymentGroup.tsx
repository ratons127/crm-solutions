'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteEmploymentGroupMutation,
  useGetPaginatedEmploymentGroupsQuery,
} from '@/lib/features/employment/employmentGroupAPI';
import { EmploymentGroupData } from '@/lib/types/employment';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import EmploymentGroupForm from './EmploymentGroupForm';

export default function EmploymentGroup() {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<EmploymentGroupData | null>(null);
  const [deleteItem] = useDeleteEmploymentGroupMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedEmploymentGroupsQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<EmploymentGroupData, EmploymentGroupData>[] =
    useMemo(
      () => [
        // {
        //   id: 'select',
        //   enableSorting: false,
        //   enableHiding: false,
        //   header: ({ table }) => (
        //     <input
        //       type="checkbox"
        //       className="h-4 w-4 accent-blue-600 cursor-pointer"
        //       checked={table.getIsAllPageRowsSelected()}
        //       onChange={table.getToggleAllPageRowsSelectedHandler()}
        //     />
        //   ),
        //   cell: ({ row }) => (
        //     <input
        //       type="checkbox"
        //       className="h-4 w-4 accent-blue-600 cursor-pointer"
        //       checked={row.getIsSelected()}
        //       disabled={!row.getCanSelect()}
        //       onChange={row.getToggleSelectedHandler()}
        //     />
        //   ),
        // },
        // {
        //   accessorKey: 'id',
        //   header: 'Id',
        // },
        {
          accessorKey: 'name',
          header: 'Employment Group name',
        },
        {
          accessorKey: 'company.name',
          header: 'Company Name',
        },
        {
          accessorKey: 'workplace.name',
          header: 'Workplace Name',
        },
        {
          accessorKey: 'code',
          header: 'Code',
        },
        {
          accessorKey: 'description',
          header: 'Description',
        },
        {
          header: 'Status',
          cell: ({ row }) => <StatusField status={row.original.status} />,
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
                    setAction('update');
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
                            title: `Qualification Type Deleted successfully`,
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
              ...
            </RowActionDropdown>
          ),
        },
      ],
      [deleteItem]
    );

  return (
    <div>
      <Paper p={20} radius={5} shadow="sm">
        <Breadcrumbs />
        <DataTable<EmploymentGroupData>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="Employment Group"
          enableRowSelection
          hideExport
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
      {/* Modal */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        <EmploymentGroupForm
          action={action}
          onClose={() => setAction(null)}
          data={item}
        />
      </Modal>
    </div>
  );
}
