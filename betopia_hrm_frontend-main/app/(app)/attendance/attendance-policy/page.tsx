'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { AttendancePolicy } from '@/lib/types/attendancePolicy';
import { useGetPaginatedAttendancePoliciesQuery } from '@/services/api/attendance/attendancePolicyAPI';
import { EyeIcon, PencilSquareIcon } from '@heroicons/react/24/outline';
import { Badge, Button, Modal } from '@mantine/core';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import AttendancePolicyForm from './AttendancePolicyForm';

function formatDateStr(d?: string | null) {
  if (!d) return '-';
  try {
    const dt = new Date(d);
    if (Number.isNaN(dt.getTime())) return d as string;
    return dt.toISOString().split('T')[0];
  } catch {
    return d as string;
  }
}

const AttendancePolicyListPage = () => {
  const [detailsOpen, setDetailsOpen] = useState(false);
  const [selected, setSelected] = useState<AttendancePolicy | null>(null);
  const [action, setAction] = useState<null | 'create' | 'update'>(null);

  // Server-side pagination
  const [pageIndex, setPageIndex] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const { data, isLoading } = useGetPaginatedAttendancePoliciesQuery({
    page: pageIndex + 1,
    perPage: pageSize,
    sortDirection: 'DESC',
  });
  const rows: AttendancePolicy[] = data?.data ?? [];

  const columns: ColumnDef<AttendancePolicy, any>[] = useMemo(
    () => [
      {
        accessorKey: 'companyId',
        header: 'Company Name',
        cell: ({ row }) => (
          <span className="font-medium text-gray-800">
            {row.original.companyId}
          </span>
        ),
      },
      {
        accessorKey: 'effectiveFrom',
        header: 'Effective From',
        cell: ({ row }) => (
          <div className="flex items-center gap-1">
            <span className="text-gray-700">
              {formatDateStr(row.original.effectiveFrom)}
            </span>
          </div>
        ),
      },
      {
        accessorKey: 'effectiveTo',
        header: 'Effective To',
        cell: ({ row }) => {
          const to = row.original.effectiveTo;
          const isOpen = !to || to === '9999-12-31';
          return isOpen ? (
            <span className="px-2 py-0.5 rounded-full text-xs bg-emerald-50 text-emerald-700 border border-emerald-200">
              Open-ended
            </span>
          ) : (
            <span className="text-gray-700">{formatDateStr(to)}</span>
          );
        },
      },
      {
        header: 'Grace (In/Out)',
        cell: ({ row }) => (
          <div className="text-xs">
            <span className="text-emerald-700">
              In: {row.original.graceInMinutes} min
            </span>
            <span className="text-gray-400"> / </span>
            <span className="text-blue-700">
              Out: {row.original.graceOutMinutes} min
            </span>
          </div>
        ),
      },
      {
        header: 'Late / Early Thresholds',
        cell: ({ row }) => (
          <div className="text-xs">
            <span className="text-red-700 font-medium">
              Late: {row.original.lateThresholdMinutes} min
            </span>
            <span className="mx-1 text-gray-400">|</span>
            <span className="text-orange-700">
              Early: {row.original.earlyLeaveThresholdMinutes} min
            </span>
          </div>
        ),
      },
      {
        header: 'Half Day / Absence Rules',
        cell: ({ row }) => (
          <div className="text-xs">
            <span className="text-gray-700">
              Half-day {'<'} {row.original.halfDayThresholdMinutes} min
            </span>
            <span className="mx-1 text-gray-400">|</span>
            <span className="text-gray-700">
              Absent {'<'} {row.original.absenceThresholdMinutes} min
            </span>
          </div>
        ),
      },
      {
        accessorKey: 'minWorkMinutes',
        header: 'Min Work (min)',
        cell: ({ row }) => (
          <span className="text-gray-700">{row.original.minWorkMinutes}</span>
        ),
      },
      {
        accessorKey: 'movementAllowMinutes',
        header: 'Movement',
        cell: ({ row }) => (
          <div className="text-xs">
            {row.original.movementEnabled ? (
              <span className="text-emerald-700">
                Enabled • {row.original.movementAllowMinutes} min
              </span>
            ) : (
              <span className="text-gray-500">Disabled</span>
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
                label: 'View',
                icon: <EyeIcon height={16} />,
                action: () => {
                  setSelected(row.original);
                  setDetailsOpen(true);
                },
              },
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setAction('update');
                  setSelected(row.original);
                },
              },
            ]}
          >
            <BsThreeDots />
          </RowActionDropdown>
        ),
      },
    ],
    []
  );

  return (
    <div className="space-y-6">
      <Breadcrumbs />

      {/* Header */}
      <div className="space-y-1">
        <h1 className="text-3xl font-bold text-[#1D293D]">
          Attendance Policy View
        </h1>
        <p className="text-base text-[#45556C]">
          View and manage attendance policies across your organization
        </p>
      </div>

      {/* List Card */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        <div className="flex items-center justify-between pb-3">
          <div className="flex flex-col">
            <div className="flex items-center gap-2 text-[#1E293B] font-semibold">
              <span>Attendance Policies</span>
            </div>
          </div>
          <Button
            onClick={() => {
              setAction('create');
              setSelected(null);
            }}
            radius="md"
            color="orange"
          >
            Create Policy
          </Button>
        </div>

        <DataTable<AttendancePolicy>
          data={rows}
          columns={columns}
          hideExport
          hideColumnVisibility
          hideSearch
          loading={isLoading}
          manualPagination
          pageCount={data?.meta?.lastPage ?? 0}
          pagination={{ pageIndex, pageSize }}
          onPaginationChange={({ pageIndex, pageSize }) => {
            setPageIndex(pageIndex);
            setPageSize(pageSize);
          }}
          totalRows={data?.meta?.total}
        />
      </div>

      {/* Details Modal */}
      <Modal
        opened={detailsOpen}
        onClose={() => setDetailsOpen(false)}
        radius="md"
        size="xl"
        withCloseButton
        title={
          <div className="flex items-center justify-between w-full pr-6">
            <div className="flex items-center gap-2">
              {selected && (
                <Badge
                  color={selected.status ? 'green' : 'gray'}
                  variant="light"
                  radius="sm"
                >
                  {selected.status ? 'Active' : 'Inactive'}
                </Badge>
              )}
            </div>
            {selected && (
              <span className="text-xs text-gray-500">
                Policy ID: {selected.id}
              </span>
            )}
          </div>
        }
      >
        {selected ? (
          <div className="space-y-4">
            {/* General Information */}
            <section className="rounded-xl border border-orange-100 bg-orange-50/50 p-4">
              <h3 className="text-sm font-semibold text-orange-700">
                General Information
              </h3>
              <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
                <div className="text-sm">
                  <div className="text-gray-500">Company</div>
                </div>
                <div className="text-sm">
                  <div className="text-gray-500">Effective From</div>
                  <div className="text-gray-900">
                    {formatDateStr(selected.effectiveFrom)}
                  </div>
                </div>
                <div className="text-sm">
                  <div className="text-gray-500">Effective To</div>
                  <div className="text-gray-900">
                    {!selected.effectiveTo ||
                    selected.effectiveTo === '9999-12-31'
                      ? 'Open-ended'
                      : formatDateStr(selected.effectiveTo)}
                  </div>
                </div>
                <div className="text-sm md:col-span-2">
                  <div className="text-gray-500">Description</div>
                  <div className="text-gray-900">{selected.notes || '—'}</div>
                </div>
              </div>
            </section>

            {/* Grace Periods */}
            <section className="rounded-xl border border-gray-100 bg-white p-4">
              <h3 className="text-sm font-semibold text-orange-700">
                Grace Periods
              </h3>
              <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
                <div className="rounded-lg border border-blue-200 bg-blue-50 p-3">
                  <div className="text-xs text-blue-700">In Grace</div>
                  <div className="text-blue-700 font-semibold">
                    {selected.graceInMinutes} minutes
                  </div>
                </div>
                <div className="rounded-lg border border-indigo-200 bg-indigo-50 p-3">
                  <div className="text-xs text-indigo-700">Out Grace</div>
                  <div className="text-indigo-700 font-semibold">
                    {selected.graceOutMinutes} minutes
                  </div>
                </div>
              </div>
            </section>

            {/* Thresholds */}
            <section className="rounded-xl border border-gray-100 bg-white p-4">
              <h3 className="text-sm font-semibold text-orange-700">
                Thresholds
              </h3>
              <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
                <div className="rounded-lg border border-red-200 bg-red-50 p-3">
                  <div className="text-xs text-red-700">Late Threshold</div>
                  <div className="text-red-700 font-semibold">
                    {selected.lateThresholdMinutes} minutes
                  </div>
                </div>
                <div className="rounded-lg border border-amber-200 bg-amber-50 p-3">
                  <div className="text-xs text-amber-700">
                    Early Leave Threshold
                  </div>
                  <div className="text-amber-700 font-semibold">
                    {selected.earlyLeaveThresholdMinutes} minutes
                  </div>
                </div>
                <div className="rounded-lg border border-violet-200 bg-violet-50 p-3">
                  <div className="text-xs text-violet-700">Half Day Rule</div>
                  <div className="text-violet-700 font-semibold">
                    Half-day {'<'} {selected.halfDayThresholdMinutes} min
                  </div>
                </div>
                <div className="rounded-lg border border-pink-200 bg-pink-50 p-3">
                  <div className="text-xs text-pink-700">Absent Rule</div>
                  <div className="text-pink-700 font-semibold">
                    Absent {'<'} {selected.absenceThresholdMinutes} min
                  </div>
                </div>
              </div>
            </section>

            {/* Working / Movement */}
            <section className="rounded-xl border border-gray-100 bg-white p-4">
              <h3 className="text-sm font-semibold text-orange-700">
                Additional Rules
              </h3>
              <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
                <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
                  <div className="text-xs text-slate-700">
                    Minimum Working Minutes
                  </div>
                  <div className="text-slate-800 font-semibold">
                    {selected.minWorkMinutes} minutes
                  </div>
                </div>
                <div className="rounded-lg border border-emerald-200 bg-emerald-50 p-3">
                  <div className="text-xs text-emerald-700">
                    Geo Fence / Movement
                  </div>
                  <div className="text-emerald-700 font-semibold">
                    {selected.movementEnabled
                      ? `Enabled • ${selected.movementAllowMinutes} minutes`
                      : 'Disabled'}
                  </div>
                </div>
              </div>
            </section>

            {/* Audit */}
            {/* <section className="rounded-xl border border-gray-100 bg-white p-4">
              <h3 className="text-sm font-semibold text-orange-700">
                Audit Information
              </h3>
              <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
                <div>
                  <div className="text-gray-500">Created By</div>
                  <div className="text-gray-900">
                    User ID #{selected.createdBy ?? '—'}
                  </div>
                </div>
                <div>
                  <div className="text-gray-500">Created At</div>
                  <div className="text-gray-900">
                    {formatDateStr(selected.createdDate)}
                  </div>
                </div>
                <div>
                  <div className="text-gray-500">Last Modified By</div>
                  <div className="text-gray-900">
                    User ID #{selected.lastModifiedBy ?? '—'}
                  </div>
                </div>
                <div>
                  <div className="text-gray-500">Last Modified At</div>
                  <div className="text-gray-900">
                    {formatDateStr(selected.lastModifiedDate)}
                  </div>
                </div>
              </div>
            </section> */}

            <div className="flex items-center justify-end gap-2 pt-1">
              <Button
                color="orange"
                radius="md"
                onClick={() => {
                  setAction('update');
                  setDetailsOpen(false);
                }}
              >
                Edit Policy
              </Button>
              <Button
                variant="default"
                radius="md"
                onClick={() => setDetailsOpen(false)}
              >
                Close
              </Button>
            </div>
          </div>
        ) : (
          <div className="text-sm text-gray-500">No policy selected.</div>
        )}
      </Modal>

      {/* Create / Edit Modal */}
      <Modal
        opened={action !== null}
        onClose={() => setAction(null)}
        radius="md"
        size="2xl"
        withCloseButton
        title={
          action === 'create'
            ? 'Create Attendance Policy Setup'
            : 'Edit Attendance Policy Setup'
        }
      >
        {action && (
          <AttendancePolicyForm
            action={action}
            data={action === 'update' ? selected : null}
            onClose={() => setAction(null)}
          />
        )}
      </Modal>
    </div>
  );
};

export default AttendancePolicyListPage;
