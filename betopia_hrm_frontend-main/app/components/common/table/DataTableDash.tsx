// =============================================
// File: components/common/table/DataTable.tsx
// Reusable table with:
// - Header row: LEFT SLOT (headerLeft) + RIGHT (Search + Columns + Export)
// - Per-column header filters (single-select via funnel icon)
// - Column visibility popover (cannot hide select/__columns__; prevents hiding last visible data column)
// - CSV export of selected rows only (shows modal if none selected)
// - Sorting, pagination, row selection
// =============================================
'use client';

import { FunnelIcon, ViewColumnsIcon } from '@heroicons/react/24/outline';
import {
  ChevronDownIcon,
  ChevronUpDownIcon,
  ChevronUpIcon,
  PlusIcon,
} from '@heroicons/react/24/solid';
import {
  ColumnDef,
  FilterFn,
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  RowSelectionState,
  SortingState,
  useReactTable,
  VisibilityState,
} from '@tanstack/react-table';
import Papa from 'papaparse';
import React, { useEffect, useMemo, useRef, useState } from 'react';

// ---- Types ----
export type SelectFilterDef<T extends object> = {
  id: keyof T & string; // column id to filter (must match accessorKey/id)
  label?: string;
  options: { label: string; value: string }[];
};

export type DataTableProps<T extends object> = {
  data: T[];
  columns: ColumnDef<T, any>[];
  initialColumnVisibility?: VisibilityState;
  searchPlaceholder?: string;
  selectFilters?: SelectFilterDef<T>[];
  pageSizeOptions?: number[];
  csvFileName?: string;
  enableRowSelection?: boolean;
  /** Content to render on the LEFT side of the header row (e.g., <Breadcrumbs/>) */
  headerLeft?: React.ReactNode;
  onPressAdd?: () => void;

  /** Toggle toolbar pieces (default: all visible) */
  hideSearch?: boolean;
  hideColumnVisibility?: boolean;
  hideExport?: boolean;

  /** Toggle pagination UI */
  hidePagination?: boolean;
};

// ---- Utilities ----
function useOutsideClick(
  ref: React.RefObject<HTMLElement | null>,
  onOutside: () => void,
  active: boolean
) {
  useEffect(() => {
    if (!active) return;
    const handler = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) onOutside();
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, [active, onOutside, ref]);
}

const equals: FilterFn<any> = (row, columnId, filterValue) => {
  if (filterValue == null || filterValue === '') return true;
  const v = row.getValue(columnId);
  return String(v) === String(filterValue);
};

// ---- Column Visibility Menu ----
function ColumnVisibilityMenu<T extends object>({
  table,
  onClose,
}: {
  table: ReturnType<typeof useReactTable<T>>;
  onClose: () => void;
}) {
  const ref = useRef<HTMLDivElement | null>(null);
  useOutsideClick(ref, onClose, true);

  // exclude selection + special control columns
  const allLeafCols = table.getAllLeafColumns();
  const manageableCols = allLeafCols.filter(
    c => c.getCanHide() && c.id !== 'select' && c.id !== '__columns__'
  );
  const visibleCount = manageableCols.filter(c => c.getIsVisible()).length;

  return (
    <div ref={ref} className="">
      <div className="px-2 py-1 text-xs font-semibold text-gray-500">
        Columns
      </div>
      <div className="max-h-72 overflow-auto pr-1">
        {manageableCols.map(col => {
          const isVisible = col.getIsVisible();
          const disableHide = visibleCount <= 1 && isVisible; // don't hide last visible data column
          return (
            <label
              key={col.id}
              className="flex items-center gap-2 px-2 py-1 text-sm"
            >
              <input
                type="checkbox"
                className="h-4 w-4 accent-blue-600"
                checked={isVisible}
                disabled={disableHide}
                onChange={() => col.toggleVisibility(!isVisible)}
              />
              <span
                className={`truncate ${
                  disableHide ? 'text-gray-400' : 'text-gray-700'
                }`}
              >
                {String(col.columnDef.header ?? col.id)}
              </span>
            </label>
          );
        })}
      </div>
    </div>
  );
}

// ---- Header Filter dropdown (single-select) ----
function HeaderFilterDropdown<T extends object>({
  show,
  onClose,
  table,
  columnId,
  options,
}: {
  show: boolean;
  onClose: () => void;
  table: ReturnType<typeof useReactTable<T>>;
  columnId: string;
  options: { label: string; value: string }[];
}) {
  const ref = useRef<HTMLDivElement>(null);
  useOutsideClick(ref, onClose, show);

  if (!show) return null;

  const currentValue =
    (table.getState().columnFilters.find(cf => cf.id === columnId)
      ?.value as string) ?? '';

  return (
    <div
      ref={ref}
      className="absolute right-0 top-7 mt-1 w-48 bg-white border border-gray-200 rounded-md shadow-lg z-30 p-2"
    >
      <button
        onClick={() => {
          table.getColumn(columnId)?.setFilterValue(undefined);
          onClose();
        }}
        className={`w-full text-left px-2 py-1.5 text-sm rounded ${
          currentValue === '' ? 'bg-blue-50 text-blue-700' : 'hover:bg-gray-50'
        }`}
      >
        All
      </button>
      {options.map(opt => {
        const active = currentValue === opt.value;
        return (
          <button
            key={opt.value}
            onClick={() => {
              table.getColumn(columnId)?.setFilterValue(opt.value);
              onClose();
            }}
            className={`w-full text-left px-2 py-1.5 text-sm rounded ${
              active
                ? 'bg-blue-600 text-white'
                : 'hover:bg-gray-50 text-gray-700'
            }`}
          >
            {opt.label}
          </button>
        );
      })}
    </div>
  );
}

export function DataTableDash<T extends object>({
  data,
  columns,
  initialColumnVisibility,
  searchPlaceholder = 'Search...',
  selectFilters,
  pageSizeOptions = [5, 10, 20, 30, 50, 100],
  csvFileName = 'export',
  enableRowSelection = true,
  headerLeft,
  onPressAdd,

  // toolbar flags
  hideSearch = false,
  hideColumnVisibility = false,
  hideExport = false,

  // pagination flag
  hidePagination = false,
}: DataTableProps<T>) {
  const [globalFilter, setGlobalFilter] = useState('');
  const [sorting, setSorting] = useState<SortingState>([]);
  const [rowSelection, setRowSelection] = useState<RowSelectionState>({});
  const [columnFilters, setColumnFilters] = useState<any[]>([]);
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>(
    initialColumnVisibility ?? {}
  );
  const [showCols, setShowCols] = useState(false);

  // Which column's filter popover is open
  const [openHeaderFilterFor, setOpenHeaderFilterFor] = useState<string | null>(
    null
  );
  // Modal for no-selection export
  const [showNoSelectionModal, setShowNoSelectionModal] = useState(false);

  // Quick lookup for select filters
  const selectFilterMap = useMemo(() => {
    const m = new Map<string, SelectFilterDef<T>>();
    (selectFilters ?? []).forEach(f => m.set(f.id, f));
    return m;
  }, [selectFilters]);

  const table = useReactTable({
    data,
    columns,
    state: {
      globalFilter,
      sorting,
      rowSelection,
      columnFilters,
      columnVisibility,
    },
    onSortingChange: setSorting,
    onRowSelectionChange: setRowSelection,
    onGlobalFilterChange: setGlobalFilter,
    onColumnFiltersChange: setColumnFilters,
    onColumnVisibilityChange: setColumnVisibility,
    enableRowSelection,
    filterFns: { equals },
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  // CSV export (selected rows only; show modal if none selected)
  const exportCSV = () => {
    const selected = table.getSelectedRowModel().rows.map(r => r.original);

    if (selected.length === 0) {
      setShowNoSelectionModal(true);
      return;
    }

    const leafCols = table
      .getAllLeafColumns()
      .filter(c => c.getIsVisible() && c.id !== 'select' && c.id !== 'actions');

    const flat = selected.map((row: any) => {
      const o: Record<string, any> = {};
      leafCols.forEach(c => {
        const k = c.id;
        o[k] = row[k];
      });
      return o;
    });

    const csv = Papa.unparse(flat);
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    const date = new Date().toISOString().slice(0, 10);
    a.download = `${csvFileName}-${date}.csv`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  };

  // Decide whether to render header + right tools
  const showRightTools =
    !hideSearch || !hideColumnVisibility || !hideExport || !!onPressAdd;
  const showHeaderRow = !!headerLeft || showRightTools;

  return (
    <div className="w-full">
      {/* Header row: LEFT (headerLeft) + RIGHT (Search + Columns + Export) */}
      {showHeaderRow && (
        <div className="mb-2 space-y-2">
          {/* Top row: headerLeft */}
          {headerLeft && (
            <div className="min-w-0 flex items-center">{headerLeft}</div>
          )}

          {/* Bottom row: Tools - Stack on mobile, row on desktop */}
          {showRightTools && (
            <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-2 sm:justify-end relative">
              {!hideSearch && (
                <input
                  type="text"
                  placeholder={searchPlaceholder}
                  value={globalFilter ?? ''}
                  onChange={e => setGlobalFilter(e.target.value)}
                  className="px-3 py-2 sm:py-1.5 border border-gray-200 rounded-md text-sm w-full sm:w-48 md:w-64 focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
              )}

              <div className="flex items-center gap-2 flex-wrap sm:flex-nowrap">
                {!hideColumnVisibility && (
                  <>
                    <button
                      onClick={() => setShowCols(s => !s)}
                      className="flex items-center justify-center gap-1 px-3 py-2 sm:py-1.5 border border-gray-800 rounded-md text-sm text-gray-700 hover:bg-gray-50 min-h-[44px] sm:min-h-0 relative"
                      title="Columns"
                      type="button"
                    >
                      <ViewColumnsIcon className="h-4 w-4" />
                      <span className="hidden sm:inline">Columns</span>
                    </button>

                    {showCols && (
                      <div className="absolute right-0 top-12 sm:top-10 mt-1 w-56 bg-white border border-gray-200 rounded-md shadow-lg z-30 p-2">
                        <ColumnVisibilityMenu
                          table={table}
                          onClose={() => setShowCols(false)}
                        />
                      </div>
                    )}
                  </>
                )}

                {!hideExport && (
                  <button
                    onClick={exportCSV}
                    className="px-3 py-2 sm:py-1.5 bg-emerald-600 text-white text-sm rounded-md hover:bg-emerald-700 min-h-[44px] sm:min-h-0 flex-1 sm:flex-initial"
                    type="button"
                  >
                    Export
                  </button>
                )}

                {!!onPressAdd && (
                  <button
                    onClick={onPressAdd}
                    className="px-3 py-2 sm:py-1.5 bg-orange-400 text-white text-sm rounded-md hover:bg-orange-500 flex items-center justify-center gap-1 min-h-[44px] sm:min-h-0 flex-1 sm:flex-initial"
                    type="button"
                  >
                    Create <PlusIcon className="size-4" />
                  </button>
                )}
              </div>
            </div>
          )}
        </div>
      )}

      {/* Desktop table */}
      <div className="hidden sm:block overflow-x-auto">
        <table className="min-w-full border border-gray-200 rounded-md text-sm">
          <thead className="bg-gray-500 ">
            {table.getHeaderGroups().map(hg => (
              <tr key={hg.id}>
                {hg.headers.map((header, i) => {
                  const colId = header.column.id as string;
                  const hasHeaderFilter = selectFilterMap.has(colId);
                  const currentV =
                    (table.getState().columnFilters.find(cf => cf.id === colId)
                      ?.value as string) ?? '';

                  const canSort = header.column.getCanSort();
                  const onSort = canSort
                    ? header.column.getToggleSortingHandler()
                    : undefined;

                  return (
                    <th
                      key={i}
                      onClick={onSort}
                      className="relative border-b border-gray-200 px-3 py-1.5 text-left text-sm font-medium text-gray-50 select-none"
                      style={{ cursor: canSort ? 'pointer' : 'default' }}
                    >
                      <div className="flex items-center gap-1">
                        {flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                        {header.column.getIsSorted() === 'asc' && (
                          <ChevronUpIcon className="h-4 w-4 text-gray-100" />
                        )}
                        {header.column.getIsSorted() === 'desc' && (
                          <ChevronDownIcon className="h-4 w-4 text-gray-100" />
                        )}
                        {!header.column.getIsSorted() && canSort && (
                          <ChevronUpDownIcon className="h-4 w-4 text-gray-100" />
                        )}

                        {hasHeaderFilter && (
                          <button
                            type="button"
                            onClick={e => {
                              e.stopPropagation();
                              setOpenHeaderFilterFor(prev =>
                                prev === colId ? null : colId
                              );
                            }}
                            className={`ml-1 p-1 rounded hover:bg-gray-100 ${
                              currentV ? 'text-blue-600' : 'text-gray-500'
                            }`}
                            title={
                              selectFilterMap.get(colId)?.label || 'Filter'
                            }
                          >
                            <FunnelIcon className="h-4 w-4" />
                          </button>
                        )}
                      </div>

                      {hasHeaderFilter && openHeaderFilterFor === colId && (
                        <HeaderFilterDropdown
                          show
                          onClose={() => setOpenHeaderFilterFor(null)}
                          table={table}
                          columnId={colId}
                          options={selectFilterMap.get(colId)!.options}
                        />
                      )}
                    </th>
                  );
                })}
              </tr>
            ))}
          </thead>
          <tbody>
            {table.getRowModel().rows.length === 0 && (
              <tr>
                <td
                  colSpan={table.getAllColumns().length}
                  className="text-center p-4 text-gray-500"
                >
                  No data found.
                </td>
              </tr>
            )}
            {table.getRowModel().rows.map((row, i) => (
              <tr
                key={i}
                className={`
                            border-b border-gray-200 transition 
                            ${i % 2 === 0 ? 'bg-gray-100 ' : 'bg-gray-200'} 
                            ${row.getIsSelected() ? 'bg-blue-50' : ''}
                            `}
              >
                {row.getVisibleCells().map((cell, i) => (
                  <td
                    key={i}
                    className="px-3 py-1.5 text-sm text-gray-700 whitespace-nowrap"
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Mobile cards */}
      <div className="sm:hidden space-y-4">
        {table.getRowModel().rows.length === 0 ? (
          <div className="text-center p-4 text-gray-500">No data found.</div>
        ) : (
          table.getRowModel().rows.map(row => (
            <div
              key={row.id}
              className={`border rounded-lg p-4 shadow-sm transition ${
                row.getIsSelected() ? 'bg-blue-50 border-blue-300' : 'bg-white'
              }`}
            >
              {row.getVisibleCells().map((cell, i) => {
                // Skip select column in mobile view
                if (cell.column.id === 'select') return null;

                return (
                  <div key={i} className="text-sm text-gray-700 mb-2 last:mb-0">
                    <span className="font-medium text-gray-900">
                      {String(cell.column.columnDef.header ?? cell.column.id)}
                      :{' '}
                    </span>
                    <span className="text-gray-700">
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </span>
                  </div>
                );
              })}
            </div>
          ))
        )}
      </div>

      {/* Pagination */}
      {!hidePagination && (
        <div className="flex flex-col sm:flex-row items-center justify-between mt-4 gap-3 text-sm">
          <div className="text-gray-600 text-center sm:text-left">
            <span className="font-medium">
              Page {table.getState().pagination.pageIndex + 1} of{' '}
              {table.getPageCount()}
            </span>
          </div>
          <div className="flex flex-col sm:flex-row items-center gap-2 w-full sm:w-auto">
            {/* Page Navigation */}
            <div className="flex items-center gap-1">
              <button
                onClick={() => table.previousPage()}
                disabled={!table.getCanPreviousPage()}
                className="px-3 py-2 border border-gray-300 rounded disabled:opacity-50 hover:bg-gray-50 min-h-[44px] sm:min-h-0 sm:py-1"
                aria-label="Previous page"
              >
                ‹
              </button>

              {/* Show fewer page numbers on mobile */}
              <div className="flex items-center gap-1">
                {Array.from({ length: table.getPageCount() }).map((_, i) => {
                  const current = table.getState().pagination.pageIndex;
                  // Mobile: show only current, first, and last
                  const isMobile =
                    typeof window !== 'undefined' && window.innerWidth < 640;
                  const showOnMobile =
                    i === 0 || i === table.getPageCount() - 1 || i === current;
                  const showOnDesktop =
                    i === 0 ||
                    i === table.getPageCount() - 1 ||
                    (i >= current - 1 && i <= current + 1);

                  if (isMobile ? showOnMobile : showOnDesktop) {
                    return (
                      <button
                        key={i}
                        onClick={() => table.setPageIndex(i)}
                        className={`px-3 py-2 sm:py-1 rounded min-h-[44px] sm:min-h-0 ${
                          i === current
                            ? 'border bg-[#F69348] text-white'
                            : 'border border-gray-200 text-gray-700 hover:bg-gray-100'
                        }`}
                      >
                        {i + 1}
                      </button>
                    );
                  }
                  if (
                    (isMobile && i === current - 1 && current > 1) ||
                    (!isMobile && (i === current - 2 || i === current + 2))
                  ) {
                    return (
                      <span key={i} className="px-1 sm:px-2 text-gray-400">
                        …
                      </span>
                    );
                  }
                  return null;
                })}
              </div>

              <button
                onClick={() => table.nextPage()}
                disabled={!table.getCanNextPage()}
                className="px-3 py-2 border border-gray-300 rounded disabled:opacity-50 hover:bg-gray-50 min-h-[44px] sm:min-h-0 sm:py-1"
                aria-label="Next page"
              >
                ›
              </button>
            </div>

            {/* Page Size Selector */}
            <select
              className="border border-gray-200 text-gray-600 rounded py-2 sm:py-1.5 px-2 w-full sm:w-auto min-h-[44px] sm:min-h-0"
              value={table.getState().pagination.pageSize}
              onChange={e => table.setPageSize(Number(e.target.value))}
            >
              {pageSizeOptions.map(opt => (
                <option key={opt} value={opt}>
                  Show {opt}
                </option>
              ))}
              <option key="all" value={data.length}>
                Show All
              </option>
            </select>
          </div>
        </div>
      )}

      {/* No selection modal */}
      {showNoSelectionModal && (
        <div className="fixed inset-0 bg-black/30 z-50 flex items-center justify-center">
          <div className="bg-white rounded-md shadow-lg w-96 max-w-[95vw] p-5">
            <h3 className="text-lg font-semibold text-gray-800">
              No rows selected
            </h3>
            <p className="text-sm text-gray-600 mt-2">
              Please select at least one row to export.
            </p>
            <div className="mt-4 flex justify-end gap-2">
              <button
                onClick={() => setShowNoSelectionModal(false)}
                className="px-4 py-2 text-sm rounded-md border border-gray-300 hover:bg-gray-50"
              >
                OK
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
