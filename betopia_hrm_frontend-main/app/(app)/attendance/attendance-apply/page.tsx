'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { useAppState } from '@/lib/features/app/appSlice';
import { useDeleteLeaveRequestMutation } from '@/lib/features/leaveRequest/leaveRequestAPI';
import { useGetPaginatedManualAttendanceQuery } from '@/services/api/attendance/manualAttendanceAPI';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Badge, Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { FaRegClock } from 'react-icons/fa';
import AttendanceApplyForm from './AttendanceApplyForm';

const formatTime = (dateTime?: string) => {
  if (!dateTime) return '-';
  const d = new Date(dateTime);
  return d.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: 'numeric',
    hour12: true,
  });
};

export default function AttendanceApplyListPage() {
  const { auth } = useAppState();
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<any | null>(null);
  const [deleteItem] = useDeleteLeaveRequestMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);
  const [keyword, setKeyword] = useState('');

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedManualAttendanceQuery({
    page: pageIndex + 1,
    perPage: pageSize,
    sortDirection: 'DESC',
    keyword,
    userId: Number(auth?.user?.id),
  });

  const columns: ColumnDef<any, any>[] = useMemo(
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
      // { accessorKey: 'id', header: 'ID' },
      {
        accessorKey: 'employeeId',
        header: 'Employee ID',
        cell: () => <span>{auth.user?.employeeSerialId}</span>,
      },
      { accessorKey: 'attendanceDate', header: 'Attendance Date' },
      {
        accessorKey: 'inTime',
        header: 'In Time',
        cell: ({ row }: any) => (
          <div className="flex items-center gap-2 text-green-600">
            <FaRegClock size={14} />
            <Text size="sm">{formatTime(row.original.inTime) || '-'}</Text>
          </div>
        ),
      },
      {
        accessorKey: 'outTime',
        header: 'Out Time',
        cell: ({ row }: any) => (
          <div className="flex items-center gap-2 text-red-600">
            <FaRegClock size={14} />
            <Text size="sm">{formatTime(row.original.outTime) || '-'}</Text>
          </div>
        ),
      },
      {
        accessorKey: 'adjustmentType',
        header: 'Adjustment Type',
        cell: ({ row }: any) => {
          const status = row.original.adjustmentType;
          const color =
            status === 'Missing Biometric'
              ? 'blue'
              : status === 'Forgot to Punch'
                ? 'yellow'
                : status === 'Device Malfunction'
                  ? 'red'
                  : 'gray';
          return (
            <Badge color={color} variant="light">
              {status}
            </Badge>
          );
        },
      },
      { accessorKey: 'reason', header: 'Reason' },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ row }) => {
          const s = String(row.original.manualAttendanceStatus);
          const cls =
            s === 'APPROVED'
              ? 'text-green-700 bg-green-50'
              : s === 'REJECTED'
                ? 'text-red-700 bg-red-50'
                : 'text-amber-700 bg-amber-50';
          return (
            <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>
              {s}
            </span>
          );
        },
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          const isPending = row.original.manualAttendanceStatus === 'PENDING';

          // Only show actions if status is PENDING
          if (!isPending) {
            return <span className="text-gray-400 text-sm">No actions</span>;
          }

          // Build actions array for PENDING status
          const actions = [
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
                      Are you sure you want to delete? This action is
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
                        title: `Leave Request Deleted successfully`,
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
          ];

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
    <div>
      <Paper p={20} radius={5} shadow="sm">
        <Breadcrumbs />
        <DataTable<any>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="Leave Requests"
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
          onSearchChange={setKeyword}
        />
      </Paper>
      {/* Modal */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
          setItem(null);
        }}
        size="xl"
      >
        {action === 'create' && (
          <AttendanceApplyForm
            key="create"
            action="create"
            onClose={() => setAction(null)}
          />
        )}
        {action === 'update' && item && (
          <AttendanceApplyForm
            key={`edit-${item.id}`}
            action="edit"
            data={item}
            onClose={() => setAction(null)}
          />
        )}
      </Modal>
    </div>
  );
}
