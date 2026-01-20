'use client';
import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteAttendanceDeviceMutation,
  useGetPaginatedAttendanceDevicesQuery,
} from '@/lib/features/attendanceDevice/attendanceDeviceAPI';
import { AttendanceDevice } from '@/lib/types/attendanceDevice';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import AttendanceDeviceForm from './AttendanceDeviceForm';

const AttendanceDevicePage = () => {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<AttendanceDevice | null>(null);
  const [deleteItem] = useDeleteAttendanceDeviceMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedAttendanceDevicesQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<AttendanceDevice, AttendanceDevice>[] = useMemo(
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
        accessorKey: 'deviceName',
        header: 'Device Name',
      },
      {
        accessorKey: 'category.name',
        cell: ({ row }) => <span>{row.original.category?.name || 'N/A'}</span>,
        header: 'Category',
      },
      {
        accessorKey: 'location?.name',
        cell: ({ row }) => <span>{row.original.location?.name || 'N/A'}</span>,
        header: 'Location',
      },
      {
        accessorKey: 'deviceType',
        cell: ({ row }) => <span>{row.original.deviceType || 'N/A'}</span>,
        header: 'Device Type',
      },
      {
        accessorKey: 'serialNumber',
        cell: ({ row }) => <span>{row.original.serialNumber || 'N/A'}</span>,
        header: 'Serial Number',
      },
      {
        accessorKey: 'ipAddress',
        cell: ({ row }) => <span>{row.original.ipAddress || 'N/A'}</span>,
        header: 'IP Address',
      },
      {
        accessorKey: 'macAddress',
        cell: ({ row }) => <span>{row.original.macAddress || 'N/A'}</span>,
        header: 'MAC Address',
      },
      {
        accessorKey: 'firmwareVersion',
        cell: ({ row }) => <span>{row.original.firmwareVersion || 'N/A'}</span>,
        header: 'Firmware Version',
      },
      {
        accessorKey: 'deviceCount',
        cell: ({ row }) => <span>{row.original.deviceCount || 0}</span>,
        header: 'Device Count',
      },
      {
        accessorKey: 'status',
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
                          title: `Attendance Device Deleted successfully`,
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
        <Breadcrumbs />
        <DataTable<AttendanceDevice>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="Attendance Device List"
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
        size="xl"
      >
        <AttendanceDeviceForm
          action={action}
          onClose={() => setAction(null)}
          data={item}
        />
      </Modal>
    </div>
  );
};

export default AttendanceDevicePage;
