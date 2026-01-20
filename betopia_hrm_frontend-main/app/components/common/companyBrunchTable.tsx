'use client';

import { ColumnDef } from '@tanstack/react-table';
import { useMemo } from 'react';
import { DataTableDash } from './table/DataTableDash';

/* ------------------------- Company Table Type ------------------------- */
export type CompanyBrunchdata = {
  id: number | string;
  name: string;
  brunch: number; // ✅ brunch যোগ করা হলো
  status?: string;
};

/* --------------------------- Dummy fallback data -------------------------- */
const dummyCompanies: CompanyBrunchdata[] = [
  {
    id: 1,
    name: 'Company Name 1',
    brunch: 10,
     status: 'Active',
  },
  {
    id: 2,
    name: 'Company Name 2',
    brunch: 15,
     status: 'Active',
  },
  {
    id: 3,
    name: 'Company Name 3',
    brunch: 12,
     status: 'Active',
  },
  {
    id: 4,
    name: 'Company Name 4',
    brunch: 18,
     status: 'Active',
  },
  {
    id: 5,
    name: 'Company Name 5',
    brunch: 20,
    status: 'Deactive',
  },
];

/* ---------------------------------- Page ---------------------------------- */
export default function CompanyBrunchTablePage() {
  /* -------------------------- Table Columns -------------------------- */
  const columns: ColumnDef<CompanyBrunchdata, any>[] = useMemo(
    () => [
      {
        accessorKey: 'name',
        header: 'Company Name',
        cell: ({ row }) => {
          const e = row.original as CompanyBrunchdata;
          return (
            <div className="flex items-center gap-3">
              <div className="font-medium text-gray-700">{e.name}</div>
            </div>
          );
        },
      },
      {
        accessorKey: 'name',
        header: 'Total Branch',
        cell: ({ row }) => {
          const e = row.original as CompanyBrunchdata;
          return (
            <div className="flex items-center gap-3 justify-center">
              <div className="font-medium text-gray-700">{e.brunch}</div>
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
      <DataTableDash<CompanyBrunchdata>
        data={dummyCompanies}
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
