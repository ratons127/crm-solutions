'use client';
import {
  useDeleteLeaveEligibilityMutation,
  useGetLeaveEligibilityListQuery,
} from '@/services/api/leave/leaveEligibilityAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import Breadcrumbs from '../../../components/common/Breadcrumbs';
import { DataTable } from '../../../components/common/table/DataTable';
import RowActionDropdown from '../../../components/common/table/RowActionDropdown';
import {
  LeaveEligibility,
  LeaveGroupType,
  LeaveType,
} from '../../../lib/types/leave';
import LeaveEligibilityForm from './LeaveEligibilityForm';

export default function LeaveEligibilityPage() {
  const { data } = useGetLeaveEligibilityListQuery(undefined);
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<LeaveEligibility | null>(null);
  const [deleteItem] = useDeleteLeaveEligibilityMutation();

  const columns: ColumnDef<LeaveEligibility, LeaveEligibility>[] = useMemo(
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
        accessorKey: 'id',
        header: 'Id',
      },
      {
        accessorKey: 'leaveType',
        header: 'Leave Type',
        cell: ({ row }) => {
          return <span>{(row?.original?.leaveType as LeaveType).name}</span>;
        },
      },
      {
        accessorKey: 'leaveGroup',
        header: 'Leave Group',
        cell: ({ row }) => {
          return (
            <span>{(row?.original?.leaveGroup as LeaveGroupType).name}</span>
          );
        },
      },
      {
        accessorKey: 'gender',
        header: 'Gender',
      },
      {
        accessorKey: 'employmentStatus',
        header: 'Employment Status',
      },

      {
        accessorKey: 'isActive',
        header: 'active',
      },
      {
        accessorKey: 'minTenureMonths',
        header: 'Min Tenure Months',
      },
      {
        accessorKey: 'maxTenureMonths',
        header: 'Max Tenure Months',
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
                      } catch (error) {
                        console.error('Error message:', error);
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
        <DataTable<LeaveEligibility>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="employees"
          enableRowSelection
          onPressAdd={() => {
            setAction('create');
          }}
        />
      </Paper>

      {/* Modal */}
      <Modal
        title="Create Leave Type Rule"
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        {action === 'update' && item ? (
          <LeaveEligibilityForm
            data={item}
            action={action}
            onClose={() => {
              setAction(null);
            }}
          />
        ) : action === 'create' ? (
          <LeaveEligibilityForm
            action={action}
            onClose={() => {
              setAction(null);
            }}
          />
        ) : null}
      </Modal>
    </div>
  );
}
