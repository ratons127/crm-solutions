'use client';

import { Paper, Stack, Title } from '@mantine/core';
import { ColumnDef } from '@tanstack/react-table';

import { DataTable } from '@/components/common/table/DataTable';

import type { LeaveBalanceEmployee } from '@/lib/types/leave';

function prettyTypeName(row: any) {
  return (
    row?.leaveType?.name ??
    row?.leaveTypeName ??
    row?.type?.name ??
    row?.typeName ??
    '—'
  );
}
function getNumber(row: any, keys: string[], fallback = 0) {
  for (const k of keys) {
    if (row?.[k] != null && !Number.isNaN(Number(row[k])))
      return Number(row[k]);
  }
  return fallback;
}

export default function LeaveBalancePanel({ employeeBalance }: any) {
  const columns: ColumnDef<LeaveBalanceEmployee, any>[] = [
    // { accessorKey: 'id', header: 'ID' },
    {
      id: 'leaveType',
      header: 'Leave Type',
      cell: ({ row }) => <span>{prettyTypeName(row.original)}</span>,
    },
    {
      id: 'year',
      header: 'Year',
      cell: ({ row }) => String((row.original as any).year ?? '—'),
    },
    {
      id: 'entitled',
      header: 'Entitled',
      cell: ({ row }) => getNumber(row.original, ['entitledDays']),
    },
    {
      id: 'carriedForward',
      header: 'Carried Forward',
      cell: ({ row }) => getNumber(row.original, ['carriedForward']),
    },
    {
      id: 'encashed',
      header: 'En cashed',
      cell: ({ row }) => getNumber(row.original, ['encashed']),
    },
    {
      id: 'used',
      header: 'Used',
      cell: ({ row }) => getNumber(row.original, ['usedDays']),
    },
    {
      id: 'balance',
      header: 'Balance',
      cell: ({ row }) => getNumber(row.original, ['balance']),
    },
  ];

  return (
    <Paper withBorder p="md" radius="md">
      <Stack gap="sm">
        <Title order={5}>Leave Balance</Title>
        <DataTable<LeaveBalanceEmployee>
          data={employeeBalance?.data ?? []}
          columns={columns}
          initialColumnVisibility={{}}
          hideSearch
          hideColumnVisibility
          hideExport
          hidePagination
        />
      </Stack>
    </Paper>
  );
}
