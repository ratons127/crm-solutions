'use client';
import {
  useDeleteLookupSetupEntryMutation,
  useGetLookupSetupEntryListQuery,
} from '@/services/api/employee/lookupSetupEntryAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import Breadcrumbs from '../../../../components/common/Breadcrumbs';
import { DataTable } from '../../../../components/common/table/DataTable';
import RowActionDropdown from '../../../../components/common/table/RowActionDropdown';
import { LookupSetupEntry } from '../../../../lib/types/employee/lookup';
import LookupSetupEntryForm from './LookupSetupEntryForm';

export default function LookupSetupEntryPage() {
  const { data } = useGetLookupSetupEntryListQuery(undefined);
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<LookupSetupEntry | null>(null);
  const [deleteItem] = useDeleteLookupSetupEntryMutation();

  const columns: ColumnDef<LookupSetupEntry, LookupSetupEntry>[] = useMemo(
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
        header: 'Name',
      },
      {
        accessorKey: 'status',
        header: 'Status',
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
        <DataTable<LookupSetupEntry>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="employees"
          enableRowSelection
          hideExport
          onPressAdd={() => {
            setAction('create');
          }}
        />
      </Paper>

      {/* Modal */}
      <Modal
        title={`${action === 'create' ? 'Create' : 'Update'} Lookup Setup Entry`}
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        {action === 'update' && item ? (
          <LookupSetupEntryForm
            data={item}
            action={action}
            onClose={() => {
              setAction(null);
            }}
          />
        ) : action === 'create' ? (
          <LookupSetupEntryForm
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
