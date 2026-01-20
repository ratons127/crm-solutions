'use client';
import StatusField from '@/components/common/StatusField';
import { LeaveGroupAssign } from '@/lib/types/leave';
import {
  useDeleteLeaveGroupAssignMutation,
  useGetPaginatedLeaveGroupAssignsQuery,
} from '@/services/api/leave/leaveGroupAssignAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import Breadcrumbs from '../../../components/common/Breadcrumbs';
import { DataTable } from '../../../components/common/table/DataTable';
import RowActionDropdown from '../../../components/common/table/RowActionDropdown';
import LeaveGroupAssignForm from './LeaveGroupAssignForm';

export default function LeaveGroupAssignPage() {
  const resourceName = 'Leave Group Assign';
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<LeaveGroupAssign | null>(null);
  const [deleteItem] = useDeleteLeaveGroupAssignMutation();

  // Permission checks
  // const { canCreate, canEdit, canDelete } =
  //   useResourcePermissions('leave-group-assign');

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedLeaveGroupAssignsQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<LeaveGroupAssign, LeaveGroupAssign>[] = useMemo(
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
        id: 'leaveGroup',
        header: 'Leave Group',
        accessorKey: 'leaveGroup.name',
        accessorFn: row => row.leaveGroup?.name ?? '',
        cell: ({ getValue }) => <span>{String(getValue() ?? '')}</span>,
      },
      {
        id: 'leaveType',
        header: 'Leave Type',
        accessorKey: 'leaveType.name',
        accessorFn: row => row.leaveType?.name ?? '',
        cell: ({ getValue }) => <span>{String(getValue() ?? '')}</span>,
      },
      {
        id: 'company',
        header: 'Company',
        accessorKey: 'company.name',
        accessorFn: row => row.company?.name ?? '',
        cell: ({ getValue }) => <span>{String(getValue() ?? '')}</span>,
      },
      // {
      //   accessorKey: 'businessUnit',
      //   header: 'Business Unit',
      //   cell: ({ row }) => <span>{row.original.businessUnit.name}</span>,
      // },
      // {
      //   accessorKey: 'workplaceGroup',
      //   header: 'WorkplaceGroup',
      //   cell: ({ row }) => <span>{row.original.workplaceGroup.name}</span>,
      // },
      // {
      //   accessorKey: 'workplace',
      //   header: 'Workplace',
      //   cell: ({ row }) => <span>{row.original.workplace.name}</span>,
      // },
      // {
      //   accessorKey: 'department',
      //   header: 'Department',
      //   cell: ({ row }) => <span>{row.original.department.name}</span>,
      // },
      // {
      //   accessorKey: 'team',
      //   header: 'Team',
      //   cell: ({ row }) => <span>{row.original.team.name}</span>,
      // },
      {
        id: 'description',
        header: 'Description',
        accessorKey: 'description',
        accessorFn: row => row.description ?? '',
        cell: ({ getValue }) => <span>{String(getValue() || '-')}</span>,
      },
      {
        id: 'status',
        header: 'Status',
        accessorKey: 'status',
        accessorFn: row => (row.status ? 'Active' : 'Inactive'),
        cell: ({ row }) => <StatusField status={row.original.status} />,
      },

      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          const actions = [];

          // Only show edit if user has permission

          actions.push({
            label: 'Edit',
            icon: <PencilSquareIcon height={16} />,
            action: () => {
              setAction('update');
              setItem(row.original);
            },
          });

          // Only show delete if user has permission

          actions.push({
            label: 'Delete',
            icon: <TrashIcon height={16} />,
            action: () => {
              modals.openConfirmModal({
                title: 'Delete confirmation',
                centered: true,
                children: (
                  <Text size="sm">
                    Are you sure you want to delete ? This action is destructive
                    and you will have to contact support to restore your data.
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
                    const response: any = await deleteItem({
                      id: row.original.id,
                    });

                    //  handle both success and backend-reported error
                    if (response?.error.status === 409) {
                      notifications.show({
                        title: 'Failed to delete',
                        message: response?.error?.data.message,
                        color: 'red',
                      });
                      return;
                    }

                    // success case (backend didn’t return 409)
                    notifications.show({
                      title: 'Leave Group Assign deleted successfully',
                      message: '',
                      color: 'green',
                    });
                  } catch (error: any) {
                    // network or thrown errors
                    const message = error?.response?.data?.message;
                    notifications.show({
                      title: 'Failed to delete',
                      message,
                      color: 'red',
                    });
                  }
                },
              });
            },
          });

          return (
            <RowActionDropdown data={actions}>
              <BsThreeDots />
            </RowActionDropdown>
          );
        },
      },
    ],

    [deleteItem]
  );

  return (
    <>
      <Paper p={20} radius={5} shadow="sm">
        <Breadcrumbs />
        <DataTable<LeaveGroupAssign>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="employees"
          enableRowSelection
          onPressAdd={() => setAction('create')}
          manualPagination
          pageCount={data?.meta?.lastPage ?? 0}
          pagination={{ pageIndex, pageSize }}
          onPaginationChange={({ pageIndex, pageSize }) => {
            setPageIndex(pageIndex);
            setPageSize(pageSize);
          }}
          totalRows={data?.meta?.total}
          loading={isLoading}
          hideExport
        />
      </Paper>

      {/* Modal */}
      <Modal
        title={`${resourceName} ${action === 'create' ? 'Create' : 'Update'}`}
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        <LeaveGroupAssignForm
          action={action}
          onClose={() => setAction(null)}
          data={item}
        />
      </Modal>
    </>
  );
}
