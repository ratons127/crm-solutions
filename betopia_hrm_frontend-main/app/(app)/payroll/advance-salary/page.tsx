'use client';

import { useState, useMemo } from 'react';
import {
  Button,
  Badge,
  Title,
  Text,
} from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { BsThreeDots } from 'react-icons/bs';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { ColumnDef } from '@tanstack/react-table';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useGetCashAdvanceSlabConfigListQuery,
  useDeleteCashAdvanceSlabConfigMutation,
} from '@/lib/features/cashAdvanceSlabConfig/cashAdvanceSlabConfigAPI';
import dayjs from 'dayjs';
import { EditCashAdvanceSlabConfigModal } from './EditCashAdvanceSlabConfigModal';
import Link from 'next/link';

/* -------------------------------------------------------------------------- */
/*                              📄 Main Page                                  */
/* -------------------------------------------------------------------------- */
export default function AdvanceCashSlabConfigListPage() {
  const [page, setPage] = useState(1);
  const limit = 10;
  const { data, isFetching, error, refetch } =
    useGetCashAdvanceSlabConfigListQuery({ page, limit });
  const [deleteConfig] = useDeleteCashAdvanceSlabConfigMutation();

  const configs = data?.data || [];
  const meta = data?.meta;

  if (error) {
    notifications.show({
      title: 'Error',
      message: 'Failed to fetch cash advance configurations',
      color: 'red',
    });
  }

  /* ----------------------------- Delete Handler ----------------------------- */
  const handleDelete = (id: number, name: string) => {
    modals.openConfirmModal({
      title: 'Delete Confirmation',
      centered: true,
      children: (
        <Text size="sm">
          Are you sure you want to delete <strong>{name}</strong>? This action
          cannot be undone.
        </Text>
      ),
      labels: { confirm: 'Delete', cancel: 'Cancel' },
      confirmProps: { color: 'red' },
      onConfirm: async () => {
        try {
          const res = await deleteConfig(id).unwrap();
          notifications.show({
            title: 'Deleted',
            message: res?.message || 'Record deleted successfully.',
            color: 'green',
          });
          refetch();
        } catch (err: any) {
          notifications.show({
            title: 'Error',
            message: err?.data?.message || 'Failed to delete record.',
            color: 'red',
          });
        }
      },
    });
  };

  /* ----------------------------- Edit Handler ----------------------------- */
  const handleEdit = (row: any) => {
    modals.open({
      title: (
        <Text fw={600} size="lg" c="orange">
          Edit Configuration — {row.setupName}
        </Text>
      ),
      centered: true,
      size: 'xl',
      children: (
        <EditCashAdvanceSlabConfigModal
          row={row}
          onClose={() => modals.closeAll()}
          onSuccess={refetch}
        />
      ),
    });
  };

  /* ----------------------------- Table Columns ----------------------------- */
  const columns: ColumnDef<any>[] = useMemo(
    () => [
      {
        id: 'sl',
        header: 'SL',
        cell: ({ row }) => (page - 1) * limit + row.index + 1,
      },
      { accessorKey: 'setupName', header: 'Setup Name' },
      { accessorKey: 'companyName', header: 'Company Name' },
      { accessorKey: 'employeeTypeName', header: 'Employee Type Name' },
      { accessorKey: 'employeeTypeId', header: 'Employee Type ID' },
      // { accessorKey: 'serviceChargeType', header: 'Service Charge Type' },
      {
        accessorKey: 'serviceChargeType',
        header: 'Service Charge Type',
        cell: ({ row }) => {
          const type = row.original.serviceChargeType;
          const amount = row.original.serviceChargeAmount;

          switch (type) {
            case 'PERCENTAGE':
              return (
                <Badge
                  variant="light"
                  color="violet"
                  radius="md"
                  size="lg"
                  styles={{
                    root: {
                      fontWeight: 600,
                      textTransform: 'none',
                      backgroundColor: '#F3E8FF',
                      color: '#7C3AED',
                    },
                  }}
                >
                  % Percent
                </Badge>
              );

            case 'FIXED':
              return (
                <Badge
                  variant="light"
                  color="green"
                  radius="md"
                  size="lg"
                  styles={{
                    root: {
                      fontWeight: 600,
                      textTransform: 'none',
                      backgroundColor: '#DCFCE7',
                      color: '#15803D',
                    },
                  }}
                >
                  $ Fixed – {amount ? Number(amount).toLocaleString() : '0'}
                </Badge>
              );

            case 'RANGE':
              return (
                <Badge
                  variant="light"
                  color="orange"
                  radius="md"
                  size="lg"
                  styles={{
                    root: {
                      fontWeight: 600,
                      textTransform: 'none',
                      backgroundColor: '#FEF3C7',
                      color: '#C2410C',
                    },
                  }}
                >
                  🔸 Range
                </Badge>
              );

            default:
              return '-';
          }
        },
      },
      {
        accessorKey: 'serviceChargeAmount',
        header: 'Charge Amount',
        cell: ({ getValue }) =>
          getValue() ? Number(getValue()).toFixed(2) : '-',
      },
      {
        accessorKey: 'effectiveFrom',
        header: 'Effective From',
        cell: ({ getValue }) => {
          const value = getValue() as string | Date | null;
          return value ? dayjs(value).format('YYYY-MM-DD') : '-';
        },
      },
      {
        accessorKey: 'effectiveTo',
        header: 'Effective To',
        cell: ({ getValue }) => {
          const value = getValue() as string | Date | null;
          return value ? dayjs(value).format('YYYY-MM-DD') : '-';
        },
      },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ getValue }) =>
          getValue() ? (
            <Badge color="green" variant="light">
              Active
            </Badge>
          ) : (
            <Badge color="red" variant="light">
              Inactive
            </Badge>
          ),
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          const config = row.original;
          const actions = [
            {
              label: 'Edit',
              icon: <PencilSquareIcon height={16} />,
              action: () => handleEdit(config),
            },
            {
              label: 'Delete',
              icon: <TrashIcon height={16} />,
              action: () => handleDelete(config.id, config.setupName),
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
    [page, limit]
  );

  /* ----------------------------- Render ----------------------------- */
  return (
    <div className="p-6 bg-white rounded-xl shadow space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <Title order={3} className="text-orange-600">
            Advance Cash Configuration List
          </Title>
          <p className="text-gray-600 text-sm">
            Manage all configured cash advance policies
          </p>
        </div>

        <Link href="/payroll/advance-salary/advance-salary-setup">
          <Button color="orange" variant="filled">
            + Create New Configuration
          </Button>
        </Link>
      </div>

      <DataTable
        data={configs}
        columns={columns}
        loading={isFetching}
        searchPlaceholder="Search configuration..."
        hideExport
        manualPagination
        pagination={{
          pageIndex: (meta?.currentPage ?? 1) - 1,
          pageSize: limit,
        }}
        onPaginationChange={(next) => setPage(next.pageIndex + 1)}
        pageCount={meta?.lastPage ?? 1}
        totalRows={meta?.total ?? 0}
      />
    </div>
  );
}
