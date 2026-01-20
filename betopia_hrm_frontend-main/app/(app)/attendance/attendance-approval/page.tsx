'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import { AttendanceApproval } from '@/lib/types/attendanceApproval';
import { formatDateLocale } from '@/lib/utils/helpers';
import {
  useBulkUpdateManualAttendanceApprovalStatusMutation,
  useGetPaginatedAttendanceApprovalListQuery,
} from '@/services/api/attendance/attendanceApprovalAPI';
import {
  Avatar,
  Badge,
  Button,
  Checkbox,
  Group,
  Stack,
  Text,
  Textarea,
} from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { useMemo, useState } from 'react';
import { CgCalendarDates } from 'react-icons/cg';
import { FaRegClock } from 'react-icons/fa6';
import { IoCheckmarkCircle, IoTimeOutline } from 'react-icons/io5';

export type ApprovalStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';

const formatTime = (dateTime?: string) => {
  if (!dateTime) return '-';
  const d = new Date(dateTime);
  return d.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: 'numeric',
    hour12: true,
  });
};

export default function AttendanceApprovalPage() {
  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);
  const [keyword, setKeyword] = useState('');

  const { data, isLoading } = useGetPaginatedAttendanceApprovalListQuery({
    page: pageIndex + 1,
    perPage: pageSize,
    sortDirection: 'DESC',
    keyword,
  });

  const [bulkUpdateStatus, { isLoading: isBulkUpdating }] =
    useBulkUpdateManualAttendanceApprovalStatusMutation();

  // Track selected rows (current page)
  const [selectedRows, setSelectedRows] = useState<AttendanceApproval[]>([]);

  /* --------------------------- COLUMN DESIGN --------------------------- */
  const columns = useMemo(
    () =>
      [
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
        {
          accessorKey: 'employeeSerialId',
          header: 'Employee ID',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => {
            const emp = row.original;
            return (
              <div className="flex items-start gap-3">
                <Avatar radius="xl" size="sm" />
                <div>
                  <Text size="sm" fw={500}>
                    {emp.employeeName}-{emp.employeeSerialId}
                  </Text>
                </div>
              </div>
            );
          },
        },
        {
          accessorKey: 'date',
          header: 'Date',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => (
            <div className="flex items-center gap-2">
              <CgCalendarDates size={14} className="text-gray-500" />
              <Text size="sm">{row.original.date}</Text>
            </div>
          ),
        },
        {
          accessorKey: 'inTime',
          header: 'In Time',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => (
            <div className="flex items-center gap-2 text-green-600">
              <FaRegClock size={14} />
              <Text size="sm">{formatTime(row.original.inTime) || '-'}</Text>
            </div>
          ),
        },
        {
          accessorKey: 'outTime',
          header: 'Out Time',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => (
            <div className="flex items-center gap-2 text-red-600">
              <FaRegClock size={14} />
              <Text size="sm">{formatTime(row.original.outTime) || '-'}</Text>
            </div>
          ),
        },
        {
          accessorKey: 'adjustmentType',
          header: 'Adjustment Type',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => {
            const type = row.original.adjustmentType;
            const color =
              type === 'Missing Biometric'
                ? 'blue'
                : type === 'Forgot to Punch'
                  ? 'yellow'
                  : type === 'Device Malfunction'
                    ? 'red'
                    : 'gray';
            return (
              <Badge color={color} variant="light">
                {type}
              </Badge>
            );
          },
        },
        {
          header: 'Reason',
          accessorKey: 'reason',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => (
            <Text size="sm" c="dimmed">
              {row.original.reason || '-'}
            </Text>
          ),
        },
        {
          accessorKey: 'submittedAt',
          header: 'Requested At',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => (
            <span>{formatDateLocale(row.original.submittedAt)}</span>
          ),
        },
        {
          accessorKey: 'manualApprovalStatus',
          header: 'Status',
          filterFn: 'equals',
          cell: ({ row }: { row: { original: AttendanceApproval } }) => {
            const status = row.original.manualApprovalStatus;
            const color =
              status === 'APPROVED'
                ? 'green'
                : status === 'REJECTED'
                  ? 'red'
                  : status === 'CANCELLED'
                    ? 'gray'
                    : 'yellow';
            const icon =
              status === 'APPROVED' ? IoCheckmarkCircle : IoTimeOutline;
            const Icon = icon;
            return (
              <Badge
                color={color}
                leftSection={<Icon size={14} />}
                variant="light"
              >
                {status}
              </Badge>
            );
          },
        },
      ] as const,
    []
  );

  const openBulkActionModal = () => {
    if (!selectedRows.length) {
      notifications.show({
        title: 'No rows selected',
        message: 'Please select at least one record.',
        color: 'red',
      });
      return;
    }

    function BulkUpdateModalContent({ rows }: { rows: AttendanceApproval[] }) {
      const [status, setStatus] = useState<ApprovalStatus>('APPROVED');
      const [commonReason, setCommonReason] = useState('');

      const needReason = status === 'REJECTED';

      const Row = ({ r }: { r: AttendanceApproval }) => (
        <tr key={r.id} className="border-b border-gray-100">
          <td className="px-2 py-1 text-sm text-gray-700">
            {r.employeeSerialId}
          </td>
          <td className="px-2 py-1 text-sm text-gray-700">{r.employeeName}</td>
          <td className="px-2 py-1 text-sm text-gray-700">{r.date}</td>
          <td className="px-2 py-1 text-xs">
            <span className="px-2 py-0.5 rounded bg-gray-50 text-gray-700">
              {r.manualApprovalStatus}
            </span>
          </td>
        </tr>
      );

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
            withAsterisk={needReason}
            value={commonReason}
            onChange={e => setCommonReason(e.currentTarget.value)}
            error={
              needReason && !commonReason.trim()
                ? 'Reason is required for rejection'
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
                    Attendance Date
                  </th>
                  <th className="px-2 py-1.5 text-left text-gray-600">
                    Current
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
              loading={isBulkUpdating}
              onClick={async () => {
                if (status === 'REJECTED' && !commonReason.trim()) {
                  notifications.show({
                    title: 'Reason required',
                    message: 'Please provide a reason when rejecting.',
                    color: 'red',
                  });
                  return;
                }
                const payload = rows.map(r => ({
                  id: r.id,
                  manualApprovalStatus: status,
                  reason: (commonReason || '').trim(),
                }));
                try {
                  const res = await bulkUpdateStatus(payload as any).unwrap();
                  modals.closeAll();
                  notifications.show({
                    title: 'Status updated',
                    message: res?.message,
                    color: 'teal',
                  });
                } catch (err) {
                  notifications.show({
                    title: 'Update failed',
                    message: 'Could not update statuses. Try again later.',
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
      title: <Text fw={600}>Are you sure to update attendance status?</Text>,
      children: <BulkUpdateModalContent rows={selectedRows} />,
    });
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow max-w-full">
      <div className="flex items-center justify-between mb-4">
        <Breadcrumbs />
        <Button color="#01922C" onClick={openBulkActionModal}>
          Take Action
        </Button>
      </div>
      <DataTable<AttendanceApproval>
        data={(data?.data ?? []).filter(
          item => item.manualApprovalStatus === 'PENDING'
        )}
        columns={columns as any}
        searchPlaceholder="Search Manual Attendance Approval..."
        csvFileName="Manual Attendance Approval"
        loading={isLoading}
        manualPagination
        pageCount={data?.meta?.lastPage ?? 0}
        pagination={{ pageIndex, pageSize }}
        onPaginationChange={({ pageIndex, pageSize }) => {
          setPageIndex(pageIndex);
          setPageSize(pageSize);
        }}
        totalRows={data?.meta?.total}
        hideExport
        onSearchChange={setKeyword}
        onSelectedRowsChange={rows =>
          setSelectedRows(rows as AttendanceApproval[])
        }
        // selectFilters={[
        //   {
        //     id: 'manualApprovalStatus',
        //     label: 'Status',
        //     options: [
        //       { label: 'Pending', value: 'PENDING' },
        //       { label: 'Approved', value: 'APPROVED' },
        //       { label: 'Rejected', value: 'REJECTED' },
        //       { label: 'Cancelled', value: 'CANCELLED' },
        //     ],
        //   },
        // ]}
        // initialColumnFilters={[
        //   { id: 'manualApprovalStatus', value: 'PENDING' },
        // ]}
      />
    </div>
  );
}
