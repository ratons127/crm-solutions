import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { Country } from '@/lib/types/admin/location';
import {
  useDeleteCountryMutation,
  useGetPaginatedCountriesQuery,
} from '@/services/api/admin/location/countryAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import CountryForm from './CountryForm';

export default function CountryTable() {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<Country | null>(null);
  const [deleteItem] = useDeleteCountryMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedCountriesQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  // Permission checks (aligned with group-assign)
  // const { canCreate, canEdit, canDelete } = useResourcePermissions('country');

  const columns: ColumnDef<Country, Country>[] = useMemo(
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
        header: 'Name',
      },
      {
        accessorKey: 'code',
        header: 'Code Name',
      },

      {
        accessorKey: 'region',
        header: 'Region',
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
      <>
        <DataTable<Country>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="countries"
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
      </>

      {/* Modal */}
      <Modal
        title="Create country"
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        {action === 'update' && item ? (
          <CountryForm
            data={item}
            action={action}
            onClose={() => {
              setAction(null);
            }}
          />
        ) : action === 'create' ? (
          <CountryForm
            action={action}
            onClose={() => {
              setAction(null);
            }}
          />
        ) : null}
      </Modal>
    </>
  );
}
