'use client';

import { ColumnDef } from '@tanstack/react-table';
import { useMemo } from 'react';
import {DataTableDash} from '@/components/common/table/DataTableDash'

/* ------------------------- Company Table Type ------------------------- */
export type ConpaneTabledata = {
  id: number | string;
  name: string;
  department: number;
  status?: string;
};

/* --------------------------- Dummy fallback data -------------------------- */
const dummyCompanies: ConpaneTabledata[] = [
  {
    id: 1,
    name: 'Branch Name',
    department: 5,
    status: 'Active',
  },
  {
    id: 2,
    name: 'Branch Name',
    department: 5,
    status: 'Deactive',
  },
  {
    id: 3,
    name: 'Branch Name',
   department: 5,
    status: 'Active',
  },
  {
    id: 4,
    name: 'Branch Name',
    department: 5,
    status: 'Active',
  },
  {
    id: 5,
    name: 'Branch Name',
    department: 5,
    status: 'Deactive',
  },
];

/* ---------------------------------- Page ---------------------------------- */
export default function CompanyTablesPage() {
  /* -------------------------- Table Columns -------------------------- */
  const columns: ColumnDef<ConpaneTabledata, any>[] = useMemo(
    () => [
      {
        accessorKey: 'name',
        header: 'Branch Name',
        cell: ({ row }) => {
          const e = row.original as ConpaneTabledata;
          return (
            <div className="flex items-center gap-3">
              <div className="font-medium text-gray-700">{e.name}</div>
            </div>
          );
        },
      },
      {
        accessorKey: 'department',
        header: 'Total Department ',
        cell: ({ row }) => {
          const e = row.original as ConpaneTabledata;
          return (
            <div className="flex items-center gap-3 justify-center">
              <div className="font-medium text-gray-700 ">{e.department}</div>
            </div>
          );
        },
      },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ row }) => (
          <span
            className={`px-2 py-1 rounded text-sm ${
              row.original.status === 'Active'
                ? 'bg-green-100 text-green-700'
                : 'bg-red-100 text-red-600'
            }`}
          >
            {row.original.status}
          </span>
        ),
      },
    ],
    []
  );

  return (
    <div className="max-w-full">
      <DataTableDash<ConpaneTabledata>
        data={dummyCompanies} // 👈 শুধু dummy data পাঠানো হচ্ছে
        columns={columns}
        enableRowSelection
        hideColumnVisibility
        hidePagination
        hideSearch
        hideExport
      />
    </div>
  );
}
