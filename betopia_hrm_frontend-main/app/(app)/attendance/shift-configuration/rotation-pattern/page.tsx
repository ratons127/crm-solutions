'use client';

import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { FaRegClock } from 'react-icons/fa6';
import { GiPathDistance } from 'react-icons/gi';
import { MdOutlineAutorenew } from 'react-icons/md';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import WorkCard from '@/components/common/WorkCard';

import {
  useDeleteShiftRotationPatternMutation,
  useGetPaginatedShiftRotationPatternsQuery,
} from '@/lib/features/shiftRotationPattern/shiftRotationPatternAPI';
import { ShiftRotationPattern } from '@/lib/types/shiftRotationPattern';
import ShiftRotationPatternForm from './ShiftRotationPatternForm';

const ShiftRotationPatternPage = () => {
  const [action, setAction] = useState<'create' | 'update' | null>(null);
  const [item, setItem] = useState<ShiftRotationPattern | null>(null);

  // Pagination state
  const [pageIndex, setPageIndex] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  // API hooks
  const { data, isLoading } = useGetPaginatedShiftRotationPatternsQuery({
    page: pageIndex + 1,
    perPage: pageSize,
    sortDirection: 'ASC',
  });
  const [deletePattern] = useDeleteShiftRotationPatternMutation();

  /* ================= Columns ================= */
  const columns: ColumnDef<ShiftRotationPattern, ShiftRotationPattern>[] =
    useMemo(
      () => [
        {
          accessorKey: 'patternName',
          header: 'Pattern Name',
        },
        {
          accessorKey: 'rotationDays',
          header: 'Rotation Days',
        },
        {
          accessorKey: 'description',
          header: 'Description',
          cell: ({ row }) => (
            <span className="text-gray-600">
              {row.original.description || '-'}
            </span>
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
                  action: () =>
                    modals.openConfirmModal({
                      title: 'Delete confirmation',
                      centered: true,
                      children: (
                        <Text size="sm">
                          Are you sure you want to delete this shift rotation
                          pattern? This action cannot be undone.
                        </Text>
                      ),
                      labels: { confirm: 'Delete', cancel: 'Cancel' },
                      confirmProps: { color: 'red' },
                      onConfirm: async () => {
                        try {
                          await deletePattern({ id: row.original.id });
                          //   notifications.show({
                          //     title: 'Deleted successfully',
                          //     color: 'red',
                          //   });
                        } catch {
                          //   notifications.show({
                          //     title: 'Failed to delete',
                          //     color: 'orange',
                          //   });
                        }
                      },
                    }),
                },
              ]}
            >
              <BsThreeDots />
            </RowActionDropdown>
          ),
        },
      ],
      [deletePattern]
    );

  const selectFilters: any[] = [
    {
      id: 'status',
      label: 'Status',
      options: [
        { label: 'Active', value: true },
        { label: 'Inactive', value: false },
      ],
    },
  ];

  return (
    <div>
      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
        <WorkCard
          icon={<MdOutlineAutorenew className="h-5 w-5" />}
          label="Total Patterns"
          value={data?.meta?.total ?? 0}
          bgGradient="bg-gradient-to-r from-[#EFF6FF] to-[#DBEAFE]"
          labelClass="text-[#155DFC]"
          valueClass="text-[#193CB8]"
          iconBg="bg-[#2B7FFF]"
        />
        <WorkCard
          icon={<GiPathDistance className="h-5 w-5" />}
          label="Active Patterns"
          value={(data?.data ?? []).filter(p => p.status === true).length ?? 0}
          bgGradient="bg-gradient-to-r from-[#F0FDF4] to-[#DCFCE7]"
          labelClass="text-[#00A63E]"
          valueClass="text-[#016630]"
          iconBg="bg-[#00C950]"
        />
        <WorkCard
          icon={<FaRegClock className="h-5 w-5" />}
          label="Average Rotation Days"
          value={
            Math.round(
              ((data?.data ?? [])
                .map(p => p.rotationDays)
                .reduce((a, b) => a + b, 0) /
                ((data?.data ?? []).length || 1)) *
                10
            ) / 10
          }
          bgGradient="bg-gradient-to-r from-[#FFF7ED] to-[#FFEDD4]"
          labelClass="text-[#F54900]"
          valueClass="text-[#9F2D00]"
          iconBg="bg-[#FF6900]"
        />
      </div>

      {/* Table */}
      <Paper p={20} radius={5} shadow="sm">
        <DataTable<ShiftRotationPattern>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={selectFilters}
          searchPlaceholder="Search rotation patterns..."
          csvFileName="ShiftRotationPatterns"
          enableRowSelection={false}
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
          onPressAdd={() => setAction('create')}
        />
      </Paper>

      {/* Modal */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
          setItem(null);
        }}
        size="lg"
      >
        <ShiftRotationPatternForm
          action={action}
          data={item}
          onClose={() => {
            setAction(null);
            setItem(null);
          }}
        />
      </Modal>
    </div>
  );
};

export default ShiftRotationPatternPage;
