'use client';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import Breadcrumbs from '../../../components/common/Breadcrumbs';
import {
  DataTable,
  SelectFilterDef,
} from '../../../components/common/table/DataTable';
import { LeaveType } from '../../../lib/types/leave';

import StatusField from '@/components/common/StatusField';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import Paper from '@/components/layout/Paper';
import {
  useDeleteLeaveTypeMutation,
  useGetPaginatedLeaveTypesQuery,
} from '@/services/api/leave/leaveTypeAPI';
import { Modal, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { BsThreeDots } from 'react-icons/bs';
import { GoTrash } from 'react-icons/go';
import { TbEdit } from 'react-icons/tb';
import LeaveTypeForm from './LeaveTypeForm';

export default function LeaveTypeList() {
  const [action, setAction] = useState<null | 'edit' | 'create'>(null);
  const [item, setItem] = useState<null | LeaveType>(null);
  const [deleteItem] = useDeleteLeaveTypeMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedLeaveTypesQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<LeaveType, any>[] = useMemo(
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
      { accessorKey: 'code', header: 'Code Name', filterFn: 'equals' },
      {
        accessorKey: 'name',
        header: 'Leave Name',
      },
      { accessorKey: 'description', header: 'Description', filterFn: 'equals' },
      {
        accessorKey: 'status',
        header: 'Status',
        filterFn: 'equals',
        cell: ({ row }) => <StatusField status={row.original.status} />,
      },
      // {
      //   accessorKey: 'createdDate',
      //   header: 'Created At',
      //   cell: ({ getValue }) => {
      //     return <>{formatDate(getValue())}</>;
      //   },
      // },
      // {
      //   accessorKey: 'lastModifiedDate',
      //   header: 'Updated At',
      //   cell: ({ getValue }) => {
      //     return <>{formatDate(getValue())}</>;
      //   },
      // },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <TbEdit />,
                action: () => {
                  setAction('edit');
                  setItem(row.original);
                },
              },
              {
                label: 'Delete',
                icon: <GoTrash />,
                action: () => {
                  modals.openConfirmModal({
                    title: 'Delete confirmation',
                    centered: true,
                    children: (
                      <Text size="sm">
                        Are you sure you want to delete? This action is
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
                          title: 'Leave Type deleted successfully',
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

  const selectFilters: SelectFilterDef<LeaveType>[] = [
    // {
    //     id: "",
    //     label: "Role",
    //     options: [
    //         { label: "Administrator", value: "Administrator" },
    //         { label: "Analyst", value: "Analyst" },
    //         { label: "Developer", value: "Developer" },
    //     ],
    // },
    // {
    //   id: 'active',
    //   label: 'Status',
    //   options: [
    //     { label: 'Enabled', value: 'Enabled' },
    //     { label: 'Disabled', value: 'Disabled' },
    //   ],
    // },
  ];

  return (
    <Paper>
      <Breadcrumbs />
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        {action === null ? null : action === 'create' ? (
          <LeaveTypeForm
            action="create"
            onClose={() => {
              setAction(null);
            }}
          />
        ) : (
          !!item && (
            <LeaveTypeForm
              action="edit"
              data={item}
              onClose={() => {
                setAction(null);
              }}
            />
          )
        )}
      </Modal>
      <DataTable<LeaveType>
        data={data?.data ?? []}
        columns={columns}
        selectFilters={selectFilters}
        initialColumnVisibility={{}}
        searchPlaceholder="Search..."
        csvFileName="employees"
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
        hideExport
        hideColumnVisibility
      />
    </Paper>
  );
}
