// =============================================
// File: components/common/table/DataTable.tsx
// Reusable table with:
// - Header row: LEFT SLOT (headerLeft) + RIGHT (Search + Columns + Export)
// - Per-column header filters (single-select via funnel icon)
// - Column visibility popover (cannot hide select/__columns__; prevents hiding last visible data column)
// - CSV export of selected rows only (shows modal if none selected)
// - Sorting, pagination (client OR server), row selection
// =============================================
'use client';

import { FunnelIcon, ViewColumnsIcon } from '@heroicons/react/24/outline';
import {
  ChevronDownIcon,
  ChevronUpDownIcon,
  ChevronUpIcon,
  PlusIcon,
} from '@heroicons/react/24/solid';
import { Button } from '@mantine/core';
import { useDebouncedValue } from '@mantine/hooks';
import {
  ColumnDef,
  FilterFn,
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  PaginationState,
  RowSelectionState,
  SortingState,
  useReactTable,
  VisibilityState,
} from '@tanstack/react-table';
import Papa from 'papaparse';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import LoadingSpinner from '../LoadingSpinner';

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
  /** Initial column filters (e.g., [{ id: 'status', value: 'PENDING' }]) */
  initialColumnFilters?: any[];
  searchPlaceholder?: string;
  /** If provided, DataTable will call this with a debounced value and will NOT apply client-side global filtering when using manual pagination. */
  onSearchChange?: (value: string) => void;
  /** Notify parent when selected rows change (current page selection). */
  onSelectedRowsChange?: (rows: T[]) => void;
  /** Initial value for the built-in search input when using onSearchChange (server-side search). */
  defaultSearchValue?: string;
  /** Debounce duration in ms for onSearchChange. Default 500ms. */
  searchDebounceMs?: number;
  selectFilters?: SelectFilterDef<T>[];
  pageSizeOptions?: number[];
  csvFileName?: string;
  enableRowSelection?: boolean;
  /** Content to render on the LEFT side of the header row (e.g., <Breadcrumbs/>) */
  headerLeft?: React.ReactNode;
  headerRight?: React.ReactNode;
  onPressAdd?: () => void;
  addButtonDisabled?: boolean;

  /** Toggle toolbar pieces (default: all visible) */
  hideSearch?: boolean;
  hideColumnVisibility?: boolean;
  hideExport?: boolean;

  /** Toggle pagination UI */
  hidePagination?: boolean;

  /** -------- New: server-side pagination controls -------- */
  manualPagination?: boolean;
  /** Current pagination (required when manualPagination=true) */
  pagination?: PaginationState; // { pageIndex, pageSize }
  /** Fires when user changes page or size (required when manualPagination=true) */
  onPaginationChange?: (next: PaginationState) => void;
  /** Total pages from server (required when manualPagination=true) */
  pageCount?: number;
  /** Optional total rows for "Showing X–Y of Z" display */
  totalRows?: number;
  /** Optional loading flag */
  loading?: boolean;
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
    <div
      ref={ref}
      className="absolute right-0 top-10 mt-1 w-56 bg-white border border-gray-200 rounded-md shadow-lg z-30 p-2"
    >
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
                className={`truncate ${disableHide ? 'text-gray-400' : 'text-gray-700'}`}
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
  anchorEl,
}: {
  show: boolean;
  onClose: () => void;
  table: ReturnType<typeof useReactTable<T>>;
  columnId: string;
  options: { label: string; value: string }[];
  anchorEl?: HTMLElement | null;
}) {
  const ref = useRef<HTMLDivElement>(null);
  useOutsideClick(ref, onClose, show);

  const [pos, setPos] = useState<{ top: number; left: number } | null>(null);

  useEffect(() => {
    if (!show || !anchorEl) return;
    const width = 192; // w-48
    const gap = 4; // ~mt-1
    const compute = () => {
      const rect = anchorEl.getBoundingClientRect();
      let left = Math.round(rect.right - width);
      const minPad = 8;
      const maxLeft = window.innerWidth - width - minPad;
      if (left < minPad) left = minPad;
      if (left > maxLeft) left = maxLeft;
      const top = Math.round(rect.bottom + gap);
      setPos({ top, left });
    };
    compute();
    const opts: AddEventListenerOptions = { capture: true, passive: true };
    window.addEventListener('scroll', compute, opts);
    window.addEventListener('resize', compute);
    return () => {
      window.removeEventListener('scroll', compute, opts as any);
      window.removeEventListener('resize', compute);
    };
  }, [show, anchorEl]);

  if (!show) return null;

  const currentValue =
    (table.getState().columnFilters.find(cf => cf.id === columnId)
      ?.value as string) ?? '';

  if (!pos) return null;

  const content = (
    <div
      ref={ref}
      className="fixed z-50 w-48 bg-white border border-gray-200 rounded-md shadow-lg p-2"
      style={pos ?? undefined}
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

  return createPortal(content, document.body);
}

export function DataTable<T extends object>({
  data,
  columns,
  initialColumnVisibility,
  initialColumnFilters,
  searchPlaceholder = 'Search...',
  onSearchChange,
  onSelectedRowsChange,
  defaultSearchValue,
  searchDebounceMs,
  selectFilters,
  pageSizeOptions = [5, 10, 20, 30, 50, 100],
  csvFileName = 'export',
  enableRowSelection = true,
  headerLeft,
  headerRight,
  onPressAdd,
  addButtonDisabled = false,

  // toolbar flags
  hideSearch = false,
  hideColumnVisibility = false,
  hideExport = false,

  // pagination flag
  hidePagination = false,

  // ---- New manual pagination props ----
  manualPagination = false,
  pagination, // required when manualPagination=true
  onPaginationChange,
  pageCount, // required when manualPagination=true
  totalRows,
  loading = false,
}: DataTableProps<T>) {
  const [globalFilter, setGlobalFilter] = useState('');
  const [searchTerm, setSearchTerm] = useState(defaultSearchValue ?? '');
  const [sorting, setSorting] = useState<SortingState>([]);
  const [rowSelection, setRowSelection] = useState<RowSelectionState>({});
  const [columnFilters, setColumnFilters] = useState<any[]>(
    initialColumnFilters ?? []
  );
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>(
    initialColumnVisibility ?? {}
  );
  const [showCols, setShowCols] = useState(false);

  // Which column's filter popover is open
  const [openHeaderFilterFor, setOpenHeaderFilterFor] = useState<string | null>(
    null
  );
  const filterButtonRefs = useRef<Record<string, HTMLButtonElement | null>>({});
  // Modal for no-selection export
  const [showNoSelectionModal, setShowNoSelectionModal] = useState(false);

  // Local pagination state (used only when NOT manual)
  const [localPagination, setLocalPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: pageSizeOptions?.[0] ?? 5,
  });

  // Quick lookup for select filters
  const selectFilterMap = useMemo(() => {
    const m = new Map<string, SelectFilterDef<T>>();
    (selectFilters ?? []).forEach(f => m.set(f.id, f));
    return m;
  }, [selectFilters]);

  // Resolve the effective pagination state
  const effectivePagination: PaginationState = manualPagination
    ? (pagination as PaginationState)
    : localPagination;

  // React Table
  const table = useReactTable({
    data,
    columns,
    state: {
      // When doing server-side search via onSearchChange and manualPagination,
      // avoid applying client-side global filter to prevent double filtering.
      globalFilter: onSearchChange && manualPagination ? '' : globalFilter,
      sorting,
      rowSelection,
      columnFilters,
      columnVisibility,
      pagination: effectivePagination,
    },
    onSortingChange: setSorting,
    onRowSelectionChange: setRowSelection,
    onGlobalFilterChange: setGlobalFilter,
    onColumnFiltersChange: setColumnFilters,
    onColumnVisibilityChange: setColumnVisibility,
    // pagination change flow
    onPaginationChange: updater => {
      const next =
        typeof updater === 'function' ? updater(effectivePagination) : updater;
      if (manualPagination) {
        onPaginationChange?.(next);
      } else {
        setLocalPagination(next);
      }
    },
    enableRowSelection,
    filterFns: { equals },
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getSortedRowModel: getSortedRowModel(),
    // Only use client-side pagination when NOT manual
    getPaginationRowModel: manualPagination
      ? undefined
      : getPaginationRowModel(),
    manualPagination: !!manualPagination,
    pageCount: manualPagination ? (pageCount ?? -1) : undefined,
  });

  // Inform parent of selected rows whenever selection changes
  useEffect(() => {
    if (!onSelectedRowsChange) return;
    const selected = table.getSelectedRowModel().rows.map(r => r.original as T);
    onSelectedRowsChange(selected);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [rowSelection]);

  // Debounce the server-side search changes (if enabled)
  const [debouncedSearch] = useDebouncedValue(
    searchTerm,
    (typeof searchDebounceMs === 'number' ? searchDebounceMs : 1000) as number
  );

  useEffect(() => {
    if (onSearchChange) {
      onSearchChange(debouncedSearch);
      // Reset to first page when search changes
      if (manualPagination) {
        table.setPageIndex(0);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [debouncedSearch]);

  // CSV export (selected rows only; note: selection is per rendered rows)
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

  const showRightTools =
    !hideSearch || !hideColumnVisibility || !hideExport || !!onPressAdd;
  const showHeaderRow = !!headerLeft || showRightTools;

  // Derived helpers for pagination UI
  const current = table.getState().pagination.pageIndex;
  const size = table.getState().pagination.pageSize;
  const totalPages = manualPagination ? (pageCount ?? 0) : table.getPageCount();

  // For “Showing X–Y of Z”
  const showingFrom = manualPagination
    ? current * size + 1
    : table.getRowModel().flatRows.length
      ? current * size + 1
      : 0;
  const showingTo = manualPagination
    ? Math.min((current + 1) * size, totalRows ?? (current + 1) * size)
    : Math.min(
        (current + 1) * size,
        table.getPrePaginationRowModel().rows.length
      );
  const totalForDisplay = manualPagination
    ? (totalRows ?? undefined)
    : table.getPrePaginationRowModel().rows.length;

  return (
    <div className="w-full overflow-x-visible">
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
              {!hideSearch &&
                (onSearchChange ? (
                  <input
                    type="text"
                    placeholder={searchPlaceholder}
                    value={searchTerm}
                    onChange={e => {
                      const v = e.target.value;
                      setSearchTerm(v);
                      if (!manualPagination) setGlobalFilter(v);
                    }}
                    className="px-3 py-2 sm:py-1.5 border border-gray-200 rounded-md text-sm w-full sm:w-48 md:w-64 focus:ring-2 focus:ring-primary focus:outline-none"
                  />
                ) : (
                  <input
                    type="text"
                    placeholder={searchPlaceholder}
                    value={globalFilter ?? ''}
                    onChange={e => setGlobalFilter(e.target.value)}
                    className="px-3 py-2 sm:py-1.5 border border-gray-200 rounded-md text-sm w-full sm:w-48 md:w-64 focus:ring-2 focus:ring-blue-500 focus:outline-none"
                  />
                ))}

              <div className="flex items-center gap-2 flex-wrap sm:flex-nowrap">
                {!hideColumnVisibility && (
                  <>
                    <button
                      onClick={() => setShowCols(s => !s)}
                      className="flex items-center justify-center gap-1 px-3 py-2 sm:py-1.5 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50 min-h-11 sm:min-h-0"
                      title="Columns"
                      type="button"
                    >
                      <ViewColumnsIcon className="h-4 w-4" />
                      <span className="hidden sm:inline">Columns</span>
                    </button>

                    {showCols && (
                      <ColumnVisibilityMenu
                        table={table}
                        onClose={() => setShowCols(false)}
                      />
                    )}
                  </>
                )}

                {!hideExport && (
                  <button
                    onClick={exportCSV}
                    className="px-3 py-2 sm:py-1.5 bg-emerald-600 text-white text-sm rounded-md hover:bg-emerald-700 min-h-11 sm:min-h-0 flex-1 sm:flex-initial"
                    type="button"
                  >
                    Export
                  </button>
                )}

                {!!onPressAdd && (
                  <Button
                    onClick={onPressAdd}
                    disabled={addButtonDisabled}
                    rightSection={<PlusIcon className="size-4" />}
                    classNames={{
                      root: 'min-h-[44px] sm:min-h-0 flex-1 sm:flex-initial',
                    }}
                  >
                    Create
                  </Button>
                )}

                {!!headerRight && (
                  <div className="flex items-center">{headerRight}</div>
                )}
              </div>
            </div>
          )}
        </div>
      )}

      {/* Responsive table with horizontal scroll - only table scrolls */}
      <div className="w-full overflow-x-auto border border-gray-200 rounded-md">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-50">
            {table.getHeaderGroups().map((hg, hgIndex) => (
              <tr key={hg.id}>
                {hg.headers.map((header, headerIndex) => {
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
                      key={`${hgIndex}-${header.id}-${headerIndex}`}
                      onClick={onSort}
                      className="relative border-b border-gray-200 px-2 py-1.5 text-left text-sm font-medium text-gray-600 select-none"
                      style={{ cursor: canSort ? 'pointer' : 'default' }}
                    >
                      <div className="flex items-center gap-1">
                        {flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                        {header.column.getIsSorted() === 'asc' && (
                          <ChevronUpIcon className="h-4 w-4 text-gray-500" />
                        )}
                        {header.column.getIsSorted() === 'desc' && (
                          <ChevronDownIcon className="h-4 w-4 text-gray-500" />
                        )}
                        {!header.column.getIsSorted() && canSort && (
                          <ChevronUpDownIcon className="h-4 w-4 text-gray-400" />
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
                            ref={el => {
                              filterButtonRefs.current[colId] = el;
                            }}
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
                          anchorEl={filterButtonRefs.current[colId]}
                        />
                      )}
                    </th>
                  );
                })}
              </tr>
            ))}
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td
                  colSpan={table.getAllColumns().length}
                  className="text-center p-8"
                >
                  <LoadingSpinner size="lg" />
                </td>
              </tr>
            ) : table.getRowModel().rows.length === 0 ? (
              <tr>
                <td
                  colSpan={table.getAllColumns().length}
                  className="text-center p-4 text-gray-500"
                >
                  No data found.
                </td>
              </tr>
            ) : (
              table.getRowModel().rows.map(row => (
                <tr
                  key={row.id}
                  className={`border-b border-gray-200 hover:bg-gray-50 transition ${
                    row.getIsSelected() ? 'bg-blue-50' : ''
                  }`}
                >
                  {row.getVisibleCells().map((cell, cellIndex) => (
                    <td
                      key={`${row.id}-${cell.column.id}-${cellIndex}`}
                      className="px-2 py-1 text-xs text-gray-700 whitespace-nowrap"
                    >
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {!hidePagination && (
        <div className="flex flex-col sm:flex-row items-center justify-between mt-2 gap-2 text-sm">
          <div className="text-gray-600 text-center sm:text-left">
            <span className="font-medium">
              Page {current + 1} of {totalPages || 1}
            </span>
            {typeof totalForDisplay === 'number' && (
              <span className="ml-2 text-gray-400 hidden md:inline">
                • Showing {showingFrom}-{showingTo} of {totalForDisplay}
              </span>
            )}
          </div>
          <div className="flex flex-col sm:flex-row items-center gap-2 w-full sm:w-auto">
            {/* Page Navigation */}
            <div className="flex items-center gap-1">
              <button
                onClick={() => table.previousPage()}
                disabled={current <= 0}
                className="px-3 py-2 border border-gray-300 rounded disabled:opacity-50 hover:bg-gray-50 min-h-11 sm:min-h-0 sm:py-1"
                aria-label="Previous page"
              >
                ‹
              </button>

              {/* Show fewer page numbers on mobile */}
              <div className="flex items-center gap-1">
                {Array.from({ length: Math.max(totalPages, 1) }).map((_, i) => {
                  // Mobile: show only current, first, and last
                  const isMobile =
                    typeof window !== 'undefined' && window.innerWidth < 640;
                  const showOnMobile =
                    i === 0 || i === totalPages - 1 || i === current;
                  const showOnDesktop =
                    i === 0 ||
                    i === totalPages - 1 ||
                    (i >= current - 1 && i <= current + 1);

                  if (isMobile ? showOnMobile : showOnDesktop) {
                    return (
                      <button
                        key={`page-${i}`}
                        onClick={() => table.setPageIndex(i)}
                        className={`px-3 py-2 sm:py-1 rounded min-h-11 sm:min-h-0 ${
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
                      <span
                        key={`ellipsis-${i}`}
                        className="px-1 sm:px-2 text-gray-400"
                      >
                        …
                      </span>
                    );
                  }
                  return null;
                })}
              </div>

              <button
                onClick={() => table.nextPage()}
                disabled={current >= totalPages - 1}
                className="px-3 py-2 border border-gray-300 rounded disabled:opacity-50 hover:bg-gray-50 min-h-11 sm:min-h-0 sm:py-1"
                aria-label="Next page"
              >
                ›
              </button>
            </div>

            {/* Page Size Selector */}
            <select
              className="border border-gray-200 text-gray-600 rounded py-2 sm:py-1.5 px-2 w-full sm:w-auto min-h-11 sm:min-h-0"
              value={size}
              onChange={e => table.setPageSize(Number(e.target.value))}
            >
              {pageSizeOptions.map(opt => (
                <option key={opt} value={opt}>
                  Show {opt}
                </option>
              ))}
              <option
                key="all"
                value={manualPagination ? (totalRows ?? 999999) : data.length}
              >
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
