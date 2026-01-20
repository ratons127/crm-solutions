'use client';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import Breadcrumbs from '../../../components/common/Breadcrumbs';
import { DataTable } from '../../../components/common/table/DataTable';
import RowActionDropdown from '../../../components/common/table/RowActionDropdown';
import { LeaveType, LeaveTypeRule } from '../../../lib/types/leave';
import {
  useDeleteLeaveTypeRuleMutation,
  useGetLeaveTypeRuleListQuery,
} from '../../../services/api/leave/leaveTypeRuleAPI';
import LeaveTypeRuleForm from './LeaveTypeRuleForm';

export default function LeaveTypeRulesPage() {
  const { data } = useGetLeaveTypeRuleListQuery(undefined);
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<LeaveTypeRule | null>(null);
  const [deleteItem] = useDeleteLeaveTypeRuleMutation();

  const columns: ColumnDef<LeaveTypeRule, LeaveTypeRule>[] = useMemo(
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
        accessorKey: 'ruleKey',
        header: 'Rule Key',
      },
      {
        accessorKey: 'ruleValue',
        header: 'Rule Value',
      },
      {
        accessorKey: 'description',
        header: 'Description',
      },
      {
        accessorKey: 'isActive',
        header: 'active',
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
                    confirmProps: { color: 'red' },
                    onCancel: () => {
                      setAction(null);
                      setItem(null);
                    },
                    onConfirm: async () => {
                      try {
                        await deleteItem({ id: row.original.id });
                        notifications.show({
                          title: 'Delete',
                          message: 'Leave type rule deleted successfully',
                        });
                      } catch (error) {
                        console.error('Error message:', error);
                        notifications.show({
                          title: 'Failed to delete',
                          message: 'Something went wrong',
                          color: 'red',
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
        <DataTable<LeaveTypeRule>
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
          <LeaveTypeRuleForm
            data={item}
            action={action}
            onClose={() => {
              setAction(null);
            }}
          />
        ) : action === 'create' ? (
          <LeaveTypeRuleForm
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
