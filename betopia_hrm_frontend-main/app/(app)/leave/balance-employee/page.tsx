// src/app/administration/leave/balance-employee/page.tsx (or your path)
'use client';

import { ColumnDef } from '@tanstack/react-table';
import { DataTable } from '../../../components/common/table/DataTable';
import Paper from '../../../components/layout/Paper';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { useAppState } from '@/lib/features/app/appSlice';
import { useGetEmployeeBalanceListQuery } from '@/lib/features/leave/balanceEmployee/balanceEmployeeAPI';
import type { LeaveBalanceEmployee } from '../../../lib/types/leave';

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

export default function LeaveBalanceEmployeePage() {
  const { auth } = useAppState();
  const currentYear = new Date().getFullYear();
  const { data } = useGetEmployeeBalanceListQuery({
    employeeId: String(auth?.user?.employeeId),
    year: currentYear,
  });

  const columns: ColumnDef<LeaveBalanceEmployee, any>[] = [
    { accessorKey: 'id', header: 'ID' },
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
      cell: ({ row }) =>
        getNumber(row.original, [
          'entitled',
          'total',
          'totalDays',
          'allocated',
          'allowed',
        ]),
    },
    {
      id: 'used',
      header: 'Used',
      cell: ({ row }) =>
        getNumber(row.original, ['used', 'taken', 'usedDays', 'takenDays']),
    },
    {
      id: 'balance',
      header: 'Balance',
      cell: ({ row }) => {
        const entitled = getNumber(row.original, [
          'balance',
          'remaining',
          'remainingDays',
        ]);
        if (entitled) return entitled;
        const e = getNumber(row.original, [
          'entitled',
          'total',
          'totalDays',
          'allocated',
          'allowed',
        ]);
        const u = getNumber(row.original, [
          'used',
          'taken',
          'usedDays',
          'takenDays',
        ]);
        return Math.max(0, e - u);
      },
    },
  ];

  return (
    <Paper>
      <DataTable<LeaveBalanceEmployee>
        data={data?.data ?? []}
        columns={columns}
        initialColumnVisibility={{}}
        hideSearch
        hideColumnVisibility
        hideExport
        hidePagination
        headerLeft={<Breadcrumbs />}
      />
    </Paper>
  );
}
