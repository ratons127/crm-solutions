'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import {
  useBulkUpdateLeaveApprovalStatusMutation,
  useGetPaginatedLeaveApprovalsQuery,
} from '@/lib/features/leave/leaveApproval/leaveApprovalAPI';
import { LeaveStatus } from '@/lib/types/leaveApproval';
import { Button, Checkbox, Group, Stack, Text, Textarea } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { useMemo, useState } from 'react';

export default function LeaveApprovalPage() {
  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);
  const [keyword, setKeyword] = useState('');
  const { data, isLoading } = useGetPaginatedLeaveApprovalsQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
    keyword,
  });

  const [bulkUpdateStatus, { isLoading: isUpdating }] =
    useBulkUpdateLeaveApprovalStatusMutation();

  // Track current page's selected rows from DataTable
  const [selectedRows, setSelectedRows] = useState<any[]>([]);

  const openBulkActionModal = () => {
    if (!selectedRows.length) {
      notifications.show({
        title: 'No rows selected',
        message: 'Please select at least one record.',
        color: 'red',
      });
      return;
    }

    function BulkUpdateModalContent({ rows }: { rows: any[] }) {
      const [status, setStatus] = useState<LeaveStatus>('APPROVED');
      const [commonReason, setCommonReason] = useState('');
      const requireRemarks = status === 'REJECTED';

      const Row = ({ r }: { r: any }) => {
        const eid = r?.employee?.employeeSerialId;
        const name =
          `${r?.employee?.firstName ?? ''} ${r?.employee?.lastName ?? ''}`.trim();
        const leaveType = r?.leaveRequest?.leaveType?.name ?? '-';
        const dateStr = `${r?.leaveRequest?.startDate ?? ''}`;
        const current = r?.leaveStatus ?? '';
        return (
          <tr key={r.id} className="border-b border-gray-100">
            <td className="px-2 py-1 text-sm text-gray-700">{eid}</td>
            <td className="px-2 py-1 text-sm text-gray-700">{name}</td>
            <td className="px-2 py-1 text-sm text-gray-700">{leaveType}</td>
            <td className="px-2 py-1 text-sm text-gray-700">{dateStr}</td>
            <td className="px-2 py-1 text-xs">
              <span className="px-2 py-0.5 rounded bg-gray-50 text-gray-700">
                {current}
              </span>
            </td>
          </tr>
        );
      };

      return (
        <Stack gap="sm">
          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-700">Request Status</span>
            <Group gap={8} className="flex items-center">
              <Checkbox
                checked={status === 'REJECTED'}
                onChange={() => setStatus('REJECTED')}
                label={
                  <span className="px-2 py-0.5 rounded bg-red-100 text-red-700 text-xs">
                    Reject
                  </span>
                }
              />
              <Checkbox
                checked={status === 'APPROVED'}
                onChange={() => setStatus('APPROVED')}
                label={
                  <span className="px-2 py-0.5 rounded bg-green-100 text-green-700 text-xs">
                    Approve
                  </span>
                }
              />
            </Group>
          </div>

          <Textarea
            placeholder="Reason"
            minRows={2}
            withAsterisk={requireRemarks}
            value={commonReason}
            onChange={e => setCommonReason(e.currentTarget.value)}
            error={
              requireRemarks && !commonReason.trim()
                ? 'Remarks are required for rejection'
                : undefined
            }
          />

          <div className="w-full overflow-auto border border-gray-200 rounded">
            <table className="min-w-full text-sm">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-2 py-1.5 text-left text-gray-600">
                    Employee ID
                  </th>
                  <th className="px-2 py-1.5 text-left text-gray-600">
                    Employee Name
                  </th>
                  <th className="px-2 py-1.5 text-left text-gray-600">
                    Leave Type
                  </th>
                  <th className="px-2 py-1.5 text-left text-gray-600">
                    Start Date
                  </th>
                  <th className="px-2 py-1.5 text-left text-gray-600">
                    Status
                  </th>
                </tr>
              </thead>
              <tbody>
                {rows.map(r => (
                  <Row key={r.id} r={r} />
                ))}
              </tbody>
            </table>
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <Button variant="default" onClick={() => modals.closeAll()}>
              Cancel
            </Button>
            <Button
              loading={isUpdating}
              onClick={async () => {
                if (requireRemarks && !commonReason.trim()) {
                  notifications.show({
                    title: 'Remarks required',
                    message: 'Please provide remarks when rejecting.',
                    color: 'red',
                  });
                  return;
                }
                const payload = rows.map((r: any) => ({
                  id: r.id,
                  leaveStatus: status,
                  remarks: (commonReason || '').trim(),
                }));
                try {
                  await bulkUpdateStatus(payload as any).unwrap();
                  modals.closeAll();
                  notifications.show({
                    title: 'Status updated',
                    message: 'Selected leave approvals have been updated.',
                    color: 'teal',
                  });
                } catch (err) {
                  console.error(err);
                  notifications.show({
                    title: 'Update failed',
                    message:
                      'Could not update leave statuses. Please try again.',
                    color: 'red',
                  });
                }
              }}
            >
              Update
            </Button>
          </div>
        </Stack>
      );
    }

    modals.open({
      size: 'xl',
      centered: true,
      padding: 'md',
      title: <Text fw={600}>Are you sure to update leave status?</Text>,
      children: <BulkUpdateModalContent rows={selectedRows} />,
    });
  };

  const columns = useMemo(
    () => [
      {
        id: 'select',
        enableSorting: false,
        enableHiding: false,
        header: ({ table }: any) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={table.getIsAllPageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        ),
        cell: ({ row }: any) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={row.getIsSelected()}
            disabled={!row.getCanSelect()}
            onChange={row.getToggleSelectedHandler()}
          />
        ),
      },
      // { accessorKey: 'id', header: 'ID' },
      {
        accessorKey: 'employee.firstName',
        header: 'Employee Info',
        cell: ({ row }: any) => (
          <span>
            {row.original.employee.employeeSerialId} -{' '}
            {row.original?.employee?.firstName}{' '}
            {row.original?.employee?.lastName} -{' '}
            {row.original?.employee?.designationName}
          </span>
        ),
      },
      { accessorKey: 'leaveRequest.leaveType.name', header: 'Leave Type' },
      { accessorKey: 'leaveRequest.startDate', header: 'Start Date' },
      { accessorKey: 'leaveRequest.endDate', header: 'End Date' },
      { accessorKey: 'leaveRequest.daysRequested', header: 'Days Request' },
      { accessorKey: 'approverId', header: 'Approver' },
      { accessorKey: 'level', header: 'Level' },
      {
        accessorKey: 'leaveStatus',
        header: 'Status',
        filterFn: 'equals',
        cell: ({ row }: any) => {
          const s = row.original.leaveStatus;
          const cls =
            s === 'APPROVED'
              ? 'text-green-700 bg-green-50'
              : s === 'REJECTED'
                ? 'text-red-700 bg-red-50'
                : s === 'CANCELLED'
                  ? 'text-gray-700 bg-gray-50'
                  : 'text-amber-700 bg-amber-50';
          return (
            <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>
              {s}
            </span>
          );
        },
      },
      { accessorKey: 'leaveRequest.reason', header: 'Reason' },
      {
        accessorKey: 'remarks',
        header: 'Remarks',
        cell: ({ row }) => <span>{row?.original?.remarks || '--'}</span>,
      },
    ],
    []
  );

  return (
    <div className="p-6 bg-white rounded-lg shadow max-w-full">
      <div className="flex items-center justify-between mb-4">
        <Breadcrumbs />
        <Button color="#01922C" onClick={openBulkActionModal}>
          Take Action
        </Button>
      </div>
      <DataTable<any>
        data={(data?.data ?? []).filter(item => item.leaveStatus === 'PENDING')}
        columns={columns as any}
        searchPlaceholder="Search leave approvals..."
        csvFileName="Leave Approvals"
        hideExport
        manualPagination
        pageCount={data?.meta?.lastPage ?? 0}
        pagination={{ pageIndex, pageSize }}
        onPaginationChange={({ pageIndex, pageSize }) => {
          setPageIndex(pageIndex);
          setPageSize(pageSize);
        }}
        totalRows={data?.meta?.total}
        loading={isLoading}
        onSearchChange={setKeyword}
        onSelectedRowsChange={setSelectedRows}
        // selectFilters={[
        //   {
        //     id: 'leaveStatus',
        //     label: 'Status',
        //     options: [
        //       { label: 'Pending', value: 'PENDING' },
        //       { label: 'Approved', value: 'APPROVED' },
        //       { label: 'Rejected', value: 'REJECTED' },
        //       { label: 'Cancelled', value: 'CANCELLED' },
        //     ],
        //   },
        // ]}
        // initialColumnFilters={[{ id: 'leaveStatus', value: 'PENDING' }]}
      />
    </div>
  );
}
