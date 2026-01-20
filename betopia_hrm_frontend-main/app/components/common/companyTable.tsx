'use client';

import { ColumnDef } from '@tanstack/react-table';
import { useMemo } from 'react';
import { DataTableDash } from './table/DataTableDash';

/* ------------------------- Company Table Type ------------------------- */
export type ConpaneTable = {
  id: number | string;
  name: string;
  status?: string;
};

/* --------------------------- Dummy fallback data -------------------------- */
const dummyCompanies: ConpaneTable[] = [
  {
    id: 1,
    name: 'EAF',
    status: 'Active',
  },
  {
    id: 2,
    name: 'EAF',
    status: 'Deactive',
  }, 
  {
    id: 3,
    name: 'EAF',
    status: 'Active',
  },
  {
    id: 4,
    name: 'EAF',
    status: 'Deactive',
  },
  {
    id: 5,
    name: 'EAF',
    status: 'Active',
  },
];

/* ---------------------------------- Page ---------------------------------- */
export default function CompanyTablePage() {
  /* -------------------------- Table Columns -------------------------- */
  const columns: ColumnDef<ConpaneTable, any>[] = useMemo(
    () => [
      {
        accessorKey: 'name',
        header: 'Company Name',
        cell: ({ row }) => {
          const e = row.original as ConpaneTable;
          return (
            <div className="flex items-center gap-3">
              <div className="font-medium text-gray-700">{e.name}</div>
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
      <DataTableDash<ConpaneTable>
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
