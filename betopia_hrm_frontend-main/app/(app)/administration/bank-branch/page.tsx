'use client';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteBankBranchMutation,
  useGetPaginatedBankBranchesQuery,
} from '@/lib/features/banks/bankBranchAPI';
import { BankBranch } from '@/lib/types/banks';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import BankBranchForm from './BankBranchForm';

const BanksBranchPage = () => {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<BankBranch | null>(null);
  const [deleteItem] = useDeleteBankBranchMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedBankBranchesQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<BankBranch, BankBranch>[] = useMemo(
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
            className="h-4 w-4 cursor-pointer bg-white"
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
        accessorKey: 'bank.name',
        header: 'Bank Name',
      },
      {
        accessorKey: 'branchName',
        header: 'Branch Name',
      },
      {
        accessorKey: 'location.name',
        header: 'Location',
      },
      {
        cell: ({ row }) => <span>{row.original.branchCode || 'N/A'}</span>,
        header: 'Branch Code',
      },
      {
        cell: ({ row }) => <span>{row.original.routingNo || 'N/A'}</span>,
        header: 'Routing No.',
      },
      {
        cell: ({ row }) => <span>{row.original.swiftCode || 'N/A'}</span>,
        header: 'Swift Code',
      },
      {
        cell: ({ row }) => <span>{row.original.email || 'N/A'}</span>,
        header: 'Email',
      },
      {
        cell: ({ row }) => <span>{row.original.addressLine1 || 'N/A'}</span>,
        header: 'Address 1',
      },
      {
        cell: ({ row }) => <span>{row.original.addressLine2 || 'N/A'}</span>,
        header: 'Address 2',
      },
      {
        cell: ({ row }) => <span>{row.original.district || 'N/A'}</span>,
        header: 'District',
      },
      {
        header: 'status',
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
                          title: `Bank Branch Deleted successfully`,
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
        <DataTable<BankBranch>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="Branch List"
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
        <BankBranchForm
          action={action}
          onClose={() => setAction(null)}
          data={item}
        />
      </Modal>
    </div>
  );
};

export default BanksBranchPage;
