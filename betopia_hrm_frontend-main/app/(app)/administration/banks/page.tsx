'use client';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteBankMutation,
  useGetPaginatedBanksQuery,
} from '@/lib/features/banks/banksAPI';
import { Bank } from '@/lib/types/banks';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import BankForm from './BankForm';

const BanksPage = () => {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<Bank | null>(null);
  const [deleteItem] = useDeleteBankMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedBanksQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<Bank, Bank>[] = useMemo(
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
      // {
      //   accessorKey: 'id',
      //   header: 'Id',
      // },
      {
        accessorKey: 'name',
        header: 'Bank',
      },
      {
        cell: ({ row }) => <span>{row.original.shortName || 'N/A'}</span>,
        header: 'Short Name',
      },
      {
        cell: ({ row }) => <span>{row.original.bankCode || 'N/A'}</span>,
        header: 'Bank Code',
      },
      {
        cell: ({ row }) => <span>{row.original.swiftCode || 'N/A'}</span>,
        header: 'Swift Code',
      },
      {
        cell: ({ row }) => <span>{row.original.country.name || 'N/A'}</span>,
        header: 'Country',
      },
      {
        header: 'Website',
        cell: ({ row }) => <span>{row.original.website || 'N/A'}</span>,
      },
      {
        cell: ({ row }) => <span>{row.original.supportEmail || 'N/A'}</span>,
        header: 'Support Email',
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
                          title: `Bank Deleted successfully`,
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
        <DataTable<Bank>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="Bank List"
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
      {/* Modal */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        <BankForm action={action} onClose={() => setAction(null)} data={item} />
      </Modal>
    </div>
  );
};

export default BanksPage;
