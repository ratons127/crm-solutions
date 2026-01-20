'use client';
import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteShiftDefinitionMutation,
  useGetPaginatedShiftDefinitionsQuery,
} from '@/lib/features/shiftDefinition/shiftDefinitionAPI';
import { ShiftDefinition } from '@/lib/types/shiftDefinition';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { FiCoffee } from 'react-icons/fi';
import { GoClock } from 'react-icons/go';
import ShiftDefinitionForm from './ShiftDefinitionForm';

const ShiftDefinitionPage = () => {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<ShiftDefinition | null>(null);
  const [deleteItem] = useDeleteShiftDefinitionMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedShiftDefinitionsQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  // Calculate working hours from start and end time
  const calculateWorkingHours = (
    startTime: string,
    endTime: string,
    breakMinutes: number = 0
  ) => {
    try {
      const [startHour, startMin] = startTime.split(':').map(Number);
      const [endHour, endMin] = endTime.split(':').map(Number);

      let totalMinutes = endHour * 60 + endMin - (startHour * 60 + startMin);

      // Handle overnight shifts
      if (totalMinutes < 0) {
        totalMinutes += 24 * 60;
      }

      // Subtract break time
      totalMinutes -= breakMinutes;

      const hours = Math.floor(totalMinutes / 60);
      const minutes = totalMinutes % 60;

      return `${hours}h ${minutes}m`;
    } catch {
      return 'N/A';
    }
  };

  const columns: ColumnDef<ShiftDefinition, ShiftDefinition>[] = useMemo(
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
        header: 'Shift ID',
      },
      // {
      //   accessorKey: 'shiftName',
      //   header: 'Shift Details',
      //   cell: ({ row }) => (
      //     <span>
      //       {row.original.shiftName} - {row.original.shiftCode}
      //     </span>
      //   ),
      // },
      {
        id: 'shiftName',
        accessorKey: 'shiftName',
        header: 'Shift Category',
      },
      {
        accessorKey: 'startTime',
        header: 'Timing',
        cell: ({ row }) => (
          <div className="flex flex-col gap-y-2">
            <span className="inline-flex items-center gap-x-2">
              <GoClock /> {row.original.startTime} - {row.original.endTime}
            </span>
            <span className="inline-flex items-center gap-x-2">
              <FiCoffee /> {row.original.breakMinutes} Minute break
            </span>
          </div>
        ),
      },
      {
        accessorKey: 'workingHours',
        header: 'Working Hours',
        cell: ({ row }) => (
          <span className="font-medium">
            {calculateWorkingHours(
              row.original.startTime,
              row.original.endTime,
              row.original.breakMinutes
            )}
          </span>
        ),
      },
      {
        accessorKey: 'graceInMinutes',
        header: 'Grace Period',
        cell: ({ row }) => (
          <div className="flex flex-col gap-y-2">
            <span>In: {row.original.graceInMinutes} Minutes</span>
            <span>Out: {row.original.graceOutMinutes} Minutes</span>
          </div>
        ),
      },
      {
        accessorKey: 'department.name',
        header: 'Department',
      },
      {
        accessorKey: 'weeklyOffs',
        header: 'Weekly Offs',
        cell: ({ row }) => (
          <div className="flex flex-wrap gap-1">
            {row.original.weeklyOffs && row.original.weeklyOffs.length > 0 ? (
              row.original.weeklyOffs.map((off, index) => (
                <span
                  key={index}
                  className="px-2 py-1 text-xs border border-gray-300 rounded-lg"
                >
                  {off.dayOfWeek.charAt(0) +
                    off.dayOfWeek.slice(1).toLowerCase()}
                </span>
              ))
            ) : (
              <span className="text-gray-400 text-sm">No weekly offs</span>
            )}
          </div>
        ),
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
                          title: `Shift Definition Deleted successfully`,
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
    [deleteItem, calculateWorkingHours]
  );

  // Generate dynamic filter options from data
  const uniqueShiftCategories = Array.from(
    new Set(
      data?.data
        ?.map((shift: ShiftDefinition) => shift.shiftName)
        .filter(Boolean)
    )
  ).map(shiftName => ({
    label: String(shiftName),
    value: String(shiftName),
  }));

  // Using any type for selectFilters to support mixed value types (string for category, boolean for status)
  const selectFilters: any[] = [
    {
      id: 'shiftName',
      label: 'Shift Category',
      options: uniqueShiftCategories,
    },
    {
      id: 'status',
      label: 'Status',
      options: [
        { label: 'Enabled', value: true },
        { label: 'Disabled', value: false },
      ],
    },
  ];

  return (
    <div>
      {/* <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
        <WorkCard
          icon={<PiStack className="h-5 w-5" />}
          label="Total Shift"
          value="4"
          bgGradient="bg-gradient-to-r from-[#EFF6FF] to-[#DBEAFE]"
          labelClass="text-[#155DFC]"
          valueClass="text-[#193CB8]"
          iconBg="bg-[#2B7FFF]"
          borderClass="border border-[#BEDBFF]"
        />
        <WorkCard
          icon={<SiTicktick className="h-5 w-5" />}
          label="Active Shift"
          value="3"
          bgGradient="bg-gradient-to-r from-[#F0FDF4] to-[#DCFCE7]"
          labelClass="text-[#00A63E]"
          valueClass="text-[#016630]"
          iconBg="bg-[#00C950]"
          borderClass="border border-[#B9F8CF]"
        />
        <WorkCard
          icon={<MdErrorOutline className="h-5 w-5" />}
          label="Night Shift"
          value="1"
          bgGradient="bg-gradient-to-r from-[#FAF5FF] to-[#F3E8FF]"
          labelClass="text-[#9810FA]"
          valueClass="text-[#6E11B0]"
          titleClass="text-white/70"
          iconBg="bg-[#AD46FF]"
          borderClass="border border-[#E9D4FF]"
        />
        <WorkCard
          icon={<FiCoffee className="h-5 w-5" />}
          label="Avg. Break Time"
          value="55 Min"
          bgGradient="bg-gradient-to-r from-[#FFF7ED] to-[#FFEDD4]"
          labelClass="text-[#F54900]"
          valueClass="text-[#9F2D00]"
          iconBg="bg-[#FF6900]"
          borderClass="border border-[#FFD6A7]"
        />
      </div> */}
      <Paper p={20} radius={5} shadow="sm">
        <DataTable<ShiftDefinition>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={selectFilters}
          searchPlaceholder="Search shift definitions..."
          csvFileName="Shift Definitions"
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
          headerLeft={<Breadcrumbs />}
        />
      </Paper>
      {/* Modal */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        <ShiftDefinitionForm
          action={action}
          onClose={() => setAction(null)}
          data={item}
        />
      </Modal>
    </div>
  );
};

export default ShiftDefinitionPage;
