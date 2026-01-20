'use client';

import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteEmployeeMutation,
  useGetEmployeeListPaginatedQuery,
} from '@/services/api/employee/employeeProfileAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import EmployeeForm from './EmployeeForm';

const EmployeeListPage = () => {
  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<any | null>(null);
  const [keyword, setKeyword] = useState('');
  const [deleteItem] = useDeleteEmployeeMutation();
  const { data, isLoading } = useGetEmployeeListPaginatedQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
    keyword,
  });

  const columns: ColumnDef<any, any>[] = useMemo(
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
      {
        accessorKey: 'firstName',
        header: 'Full Name',
        cell: ({ row }) => (
          <span>
            {row.original.firstName} - {row.original.lastName}
          </span>
        ),
      },
      // {
      //   accessorKey: 'designation?.name',
      //   header: 'Designation',
      //   cell: ({ row }) => (
      //     <span>{row.original?.designation?.name || '--'}</span>
      //   ),
      // },
      {
        accessorKey: 'designation.name',
        header: 'Designation',
        enableSorting: true,
        sortingFn: (rowA, rowB) =>
          (rowA.original?.designation?.name || '').localeCompare(
            rowB.original?.designation?.name || ''
          ),
        cell: ({ row }) => <span>{row.original?.designation?.name || '--'}</span>,
      },
      {
        accessorKey: 'employeeSerialId',
        header: 'Serial Id',
      },
      // {
      //   accessorKey: 'Email',
      //   cell: ({ row }) => <span>{row.original.email || '--'}</span>,
      //   header: 'Email',
      // },
      {
        accessorKey: 'email',
        header: 'Email',
        enableSorting: true,
        sortingFn: (rowA, rowB) =>
          (rowA.original?.email || '').localeCompare(
            rowB.original?.email || ''
          ),
        cell: ({ row }) => <span>{row.original.email || '--'}</span>,
      },
      {
        accessorKey: 'phone',
        cell: ({ row }) => <span>{row.original.phone || '--'}</span>,
        header: 'Phone',
      },
      {
        accessorKey: 'gender',
        header: 'Gender',
        cell: ({ row }) => {
          const gender = row.original.gender?.toLowerCase();
          const color =
            gender === 'male'
              ? '#007bff' // blue
              : gender === 'female'
                ? '#e83e8c' // pink
                : '#6c757d'; // gray for others or null

          return (
            <span
              style={{ color, fontWeight: 500, textTransform: 'capitalize' }}
            >
              {gender || '--'}
            </span>
          );
        },
      },
      {
        cell: ({ row }) => <span>{row.original.dateOfJoining || '--'}</span>,
        header: 'Joined',
      },
      {
        accessorKey: 'businessUnit.name',
        cell: ({ row }) => (
          <span>{row.original?.businessUnit?.name || '--'}</span>
        ),
        header: 'BU',
      },
      {
        accessorKey: 'workplaceGroup.name',
        cell: ({ row }) => (
          <span>{row.original?.workplaceGroup?.name || '--'}</span>
        ),
        header: 'WPG',
      },
      {
        accessorKey: 'workplace.name',
        cell: ({ row }) => <span>{row.original?.workplace?.name || '--'}</span>,
        header: 'WP',
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
                          title: `Employee Deleted successfully`,
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
  return (
    <div>
      <Paper p={20} radius={5} shadow="sm">
        <DataTable<any>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          onSearchChange={setKeyword}
          csvFileName="Employee List"
          enableRowSelection
          onPressAdd={() => {
            setItem(null);
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
        size="xl"
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        {action && (
          <EmployeeForm
            action={action}
            data={item}
            onCancel={() => setAction(null)}
            onSuccess={() => setAction(null)}
          />
        )}
      </Modal>
    </div>
  );
};

export default EmployeeListPage;
