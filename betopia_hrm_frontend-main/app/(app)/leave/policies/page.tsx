'use client';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import Breadcrumbs from '../../../components/common/Breadcrumbs';
import {
  DataTable,
  SelectFilterDef,
} from '../../../components/common/table/DataTable';

import StatusField from '@/components/common/StatusField';
import CellValue from '@/components/common/table/CellValue';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteLeavePolicyMutation,
  useGetPaginatedLeavePoliciesQuery,
} from '@/services/api/leave/leavePolicyAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { Modal, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { BsThreeDots } from 'react-icons/bs';
import Paper from '../../../components/layout/Paper';
import type { LeavePolicy } from '../../../lib/types/leave';
import LeavePolicyForm from './LeavePolicyForm';

export default function LeavePolicy() {
  const [action, setAction] = useState<null | 'create' | 'edit'>(null);
  const [item, setItem] = useState<LeavePolicy | null>(null);
  const [deleteItem] = useDeleteLeavePolicyMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedLeavePoliciesQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<LeavePolicy, any>[] = useMemo(
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
        accessorKey: 'leaveType.name',
        id: 'leaveType',
        header: 'Leave Type',
        accessorFn: row => row?.leaveType?.name ?? '',
        cell: ({ getValue }) => <span>{String(getValue() ?? '')}</span>,
      },
      // {
      //   accessorKey: 'leaveType',
      //   header: 'Leave Type Code',
      //   cell: ({ row }) => {
      //     return <span>{row?.original?.leaveType?.code}</span>;
      //   },
      // },
      {
        accessorKey: 'defaultQuota',
        header: 'Default Entitlement (Days)',
      },
      {
        accessorKey: 'minDays',
        header: 'Min Days per Request',
      },
      {
        accessorKey: 'maxDays',
        header: 'Max Days per Request',
      },
      {
        accessorKey: 'tenureRequiredDays',
        header: 'Minimum Tenure (Days)',
      },
      {
        accessorKey: 'carryForwardCap',
        header: 'Carry Forward Cap (Days)',
      },
      {
        accessorKey: 'allowHalfDay',
        header: 'Half-Day Allowed',
        cell: ({ row }) => <CellValue>{row.original.allowHalfDay}</CellValue>,
      },
      {
        accessorKey: 'requiresApproval',
        header: 'Requires Approval',
        cell: ({ row }) => (
          <CellValue>{row.original.requiresApproval}</CellValue>
        ),
      },
      {
        accessorKey: 'status',
        id: 'status',
        header: 'Policy Status',
        accessorFn: row => (row.status ? 'Active' : 'Inactive'),
        cell: ({ row }) => <StatusField status={row.original.status} />,
      },
      // {
      //   accessorKey: 'proofThreshold',
      //   header: 'Proof Required',
      // },
      // {
      //   accessorKey: 'genderRestricted',
      //   header: 'Gender Required',
      // },
      // {
      //   accessorKey: 'eligibleGender',
      //   header: 'Eligible Gender',
      // },
      // {
      //   accessorKey: 'tenureRequired',
      //   header: 'Tenure Required',
      // },
      // {
      //   accessorKey: 'maxOccurrences',
      //   header: 'Max Occurrences',
      // },
      // {
      //   accessorKey: 'requiredApproval',
      //   header: 'Required Approval',
      // },
      // {
      //   accessorKey: 'expeditedApproval',
      //   header: 'Expedited Approval',
      // },
      // {
      //   accessorKey: 'linkedToOvertime',
      //   header: 'Linked to overtime',
      // },
      // {
      //   accessorKey: 'accrualRatePerMonth',
      //   header: 'Accrual Rate Per Month',
      // },
      // {
      //   accessorKey: 'extraDaysAfterYears',
      //   header: 'Extra Days After Years',
      // },
      // {
      //   accessorKey: 'restrictBridgeLeave',
      //   header: 'Restrict Bridge Leave',
      // },
      // {
      //   accessorKey: 'maxBridgeDays',
      //   header: 'Max Bridge Days',
      // },
      // {
      //   accessorKey: 'allowNegativeBalance',
      //   header: 'Allow Negative Balance',
      // },
      // {
      //   accessorKey: 'maxAdvanceDays',
      //   header: 'Max Advance days',
      // },

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
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setItem(row.original);
                  setAction('edit');
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
                    },
                    onConfirm: async () => {
                      try {
                        await deleteItem({ id: row.original.id });
                        notifications.show({
                          title: 'Leave Policy Deleted successfully',
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
                disabled: true,
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

  const selectFilters: SelectFilterDef<LeavePolicy>[] = [];

  return (
    <Paper>
      <Breadcrumbs />
      <div>
        {/* Render form */}
        <Modal
          size="xl"
          opened={action !== null}
          onClose={() => {
            setAction(null);
          }}
        >
          <LeavePolicyForm
            action={action}
            data={item}
            onClose={() => {
              setAction(null);
            }}
          />
        </Modal>
      </div>
      <DataTable<LeavePolicy>
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
      />
    </Paper>
  );
}
