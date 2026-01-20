'use client';

import { CalendarDaysIcon, ClockIcon, PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { Button, Grid, Select, Switch, Text, TextInput } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useEffect, useMemo, useState } from 'react';
import { useSelector } from 'react-redux';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import Modal from '@/components/common/Modal';
import { DataTable, SelectFilterDef } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import WorkCard from '@/components/common/WorkCard';

import { useAppDispatch } from '@/lib/hooks';
import { RootState } from '@/lib/store';

/* ===================== TRANSFER ===================== */
import {
  createTransfer,
  deleteTransferById,
  fetchTransfers,
  updateTransferById,
} from '@/lib/features/employees/transfer/transferApi';
import {
  TransferCreatePayload,
  TransferRow,
} from '@/lib/features/employees/transfer/transferSlice';

/* ===================== COMPANIES (new slice) ===================== */
import type { CompanyRow } from '@/lib/features/companyConfig/companyConfigApi';
import { fetchCompanies } from '@/lib/features/companyConfig/companyConfigSlice';

/* ===================== WORKPLACES ===================== */
import { fetchWorkplaces } from '@/lib/features/companyConfig/workplaceApi';
import type { WorkplaceRow } from '@/lib/features/companyConfig/workplaceSlice';

/* ===================== DESIGNATIONS ===================== */
import { fetchDesignations } from '@/lib/features/employees/workstructure/designationApi';
import type { DesignationRow } from '@/lib/features/employees/workstructure/designationSlice';

/* ===================== DEPARTMENTS ===================== */
import { fetchDepartments } from '@/lib/features/companyConfig/departmentApi';
import type { DepartmentRow } from '@/lib/features/companyConfig/departmentSlice';

/* ===================== EMPLOYEES ===================== */
import { fetchEmployees } from '@/lib/features/employeesProfile/employeeProfileApi';
import type { EmployeeRow } from '@/lib/features/employeesProfile/employeeProfileSlice';

/* ---------------------------- Helpers ---------------------------- */
type Option = { value: string; label: string };

/** Defensive array selectors (tolerate older reducer keys too) */
const selectCompanyArray = (s: RootState): CompanyRow[] =>
  (((s as any).companies?.items ?? (s as any).companyConfig?.list) || []) as CompanyRow[];

const selectWorkplaceArray = (s: RootState): WorkplaceRow[] =>
  (((s as any).workplaces?.list ?? (s as any).workplace?.list) || []) as WorkplaceRow[];

const selectDesignationArray = (s: RootState): DesignationRow[] =>
  ((s as any).designation?.list || []) as DesignationRow[];

const selectDepartmentArray = (s: RootState): DepartmentRow[] =>
  (((s as any).departments?.list ?? (s as any).department?.list) || []) as DepartmentRow[];

const selectEmployeeArray = (s: RootState): EmployeeRow[] =>
  (((s as any).employeesProfile?.list ?? (s as any).employee?.list) || []) as EmployeeRow[];

/** Option builders (tolerant to field naming differences) */
const toCompanyOptions = (rows: CompanyRow[] = []): Option[] =>
  rows.map((c: any) => ({
    value: String(c.id),
    label: c.name || c.companyName || `Company ${c.id}`,
  }));

const toWorkplaceOptions = (rows: WorkplaceRow[] = []): Option[] =>
  rows.map((w: any) => ({
    value: String(w.id),
    label: w.name || w.workplaceName || `Workplace ${w.id}`,
  }));

const toDesignationOptions = (rows: DesignationRow[] = []): Option[] =>
  rows.map((d: any) => ({
    value: String(d.id),
    label: d.name ?? d.title ?? d.designationName ?? `Designation ${d.id}`,
  }));

const toDepartmentOptions = (rows: DepartmentRow[] = []): Option[] =>
  rows.map((d: any) => ({
    value: String(d.id),
    label: d.name ?? d.departmentName ?? `Department ${d.id}`,
  }));

const toEmployeeOptions = (rows: EmployeeRow[] = []): Option[] =>
  rows.map((e: any) => ({
    value: String(e.id),
    label:
      e.fullName ||
      [String(e.firstName || ''), String(e.lastName || '')].filter(Boolean).join(' ') ||
      `#${e.id}`,
  }));

/* ---------------------------- Create Transfer Modal ---------------------------- */
function CreateTransferModal({
  open,
  onClose,
  onConfirm,
  busy,
  error,
  success,
  employeeOptions,
  companyOptions,
  workplaceOptions,     // ← FLAT, no dependency
  departmentOptions,    // ← NEW
  designationOptions,
}: {
  open: boolean;
  onClose: () => void;
  onConfirm: (payload: TransferCreatePayload) => void;
  busy: boolean;
  error: string | null;
  success: boolean;

  employeeOptions: Option[];
  companyOptions: Option[];
  workplaceOptions: Option[];
  departmentOptions: Option[];    // ← NEW
  designationOptions: Option[];
}) {
  const [employeeId, setEmployeeId] = useState<string>('');
  const [requestType, setRequestType] = useState<string>('IntraCompany');

  const [fromCompanyId, setFromCompanyId] = useState<string>('');
  const [toCompanyId, setToCompanyId] = useState<string>('');

  const [fromWorkplaceId, setFromWorkplaceId] = useState<string>('');
  const [toWorkplaceId, setToWorkplaceId] = useState<string>('');

  const [fromDepartmentId, setFromDepartmentId] = useState<string>(''); // now select
  const [toDepartmentId, setToDepartmentId] = useState<string>('');     // now select

  const [fromDesignationId, setFromDesignationId] = useState<string>('');
  const [toDesignationId, setToDesignationId] = useState<string>('');

  const [reason, setReason] = useState('');
  const [effectiveDate, setEffectiveDate] = useState<string>('2025-10-12');
  const [approvalStatus, setApprovalStatus] = useState<string>('Pending');
  const [approvedById, setApprovedById] = useState<string>('');
  const [approvedAt, setApprovedAt] = useState<string>('');
  const [status, setStatus] = useState<boolean>(true);

  useEffect(() => {
    if (open) {
      setEmployeeId('');
      setRequestType('IntraCompany');
      setFromCompanyId('');
      setToCompanyId('');
      setFromWorkplaceId('');
      setToWorkplaceId('');
      setFromDepartmentId('');
      setToDepartmentId('');
      setFromDesignationId('');
      setToDesignationId('');
      setReason('');
      setEffectiveDate('2025-10-12');
      setApprovalStatus('Pending');
      setApprovedById('');
      setApprovedAt('');
      setStatus(true);
    }
  }, [open]);

  if (!open) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Transfer Request">
      <div className="space-y-3">
        <Grid gutter="md">
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="Employee"
              placeholder="Select employee"
              data={employeeOptions}
              value={employeeId || null}
              onChange={(v) => setEmployeeId(v || '')}
              disabled={busy || success}
              searchable
              withAsterisk
              clearable
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="Request Type"
              data={[
                { value: 'IntraCompany', label: 'IntraCompany' },
                { value: 'InterCompany', label: 'InterCompany' },
              ]}
              value={requestType}
              onChange={(v) => setRequestType(v || 'IntraCompany')}
              disabled={busy || success}
              clearable
            />
          </Grid.Col>

          {/* From / To (Company) */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="From Company"
              placeholder="Select company"
              data={companyOptions}
              value={fromCompanyId || null}
              onChange={(v) => setFromCompanyId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="To Company"
              placeholder="Select company"
              data={companyOptions}
              value={toCompanyId || null}
              onChange={(v) => setToCompanyId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>

          {/* From / To (Workplace) — FLAT LIST */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="From Workplace"
              placeholder="Select workplace"
              data={workplaceOptions}
              value={fromWorkplaceId || null}
              onChange={(v) => setFromWorkplaceId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="To Workplace"
              placeholder="Select workplace"
              data={workplaceOptions}
              value={toWorkplaceId || null}
              onChange={(v) => setToWorkplaceId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>

          {/* From / To (Department) — FLAT LIST */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="From Department"
              placeholder="Select department"
              data={departmentOptions}
              value={fromDepartmentId || null}
              onChange={(v) => setFromDepartmentId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="To Department"
              placeholder="Select department"
              data={departmentOptions}
              value={toDepartmentId || null}
              onChange={(v) => setToDepartmentId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>

          {/* From / To (Designation) — FLAT LIST */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="From Designation"
              placeholder="Select designation"
              data={designationOptions}
              value={fromDesignationId || null}
              onChange={(v) => setFromDesignationId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="To Designation"
              placeholder="Select designation"
              data={designationOptions}
              value={toDesignationId || null}
              onChange={(v) => setToDesignationId(v || '')}
              disabled={busy || success}
              searchable
              clearable
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Reason"
              value={reason}
              onChange={(e) => setReason(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Effective Date (YYYY-MM-DD)"
              value={effectiveDate}
              onChange={(e) => setEffectiveDate(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="Approval Status"
              data={[
                { value: 'Pending', label: 'Pending' },
                { value: 'Approved', label: 'Approved' },
                { value: 'Rejected', label: 'Rejected' },
              ]}
              value={approvalStatus}
              onChange={(v) => setApprovalStatus(v || 'Pending')}
              disabled={busy || success}
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Approved By (User ID)"
              value={approvedById}
              onChange={(e) => setApprovedById(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Approved At (ISO datetime)"
              placeholder="2025-10-12T04:18:26.965Z"
              value={approvedAt}
              onChange={(e) => setApprovedAt(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Switch
              label="Active"
              checked={status}
              onChange={(e) => setStatus(e.currentTarget.checked)}
              disabled={busy || success}
            />
          </Grid.Col>
        </Grid>

        {!!error && <Text c="red" size="sm">{error}</Text>}
        {success && !error && <Text c="green" size="sm">Transfer created. Closing…</Text>}

        <div className="flex items-center gap-5 justify-end mt-8">
          <Button onClick={onClose} color="red" type="button" disabled={busy}>
            Cancel
          </Button>
          <Button
            type="submit"
            onClick={() =>
              onConfirm({
                employeeId: Number(employeeId),
                requestType: requestType as any,
                fromCompanyId: fromCompanyId ? Number(fromCompanyId) : null,
                toCompanyId: toCompanyId ? Number(toCompanyId) : null,
                fromWorkplaceId: fromWorkplaceId ? Number(fromWorkplaceId) : null,
                toWorkplaceId: toWorkplaceId ? Number(toWorkplaceId) : null,
                fromDepartmentId: fromDepartmentId ? Number(fromDepartmentId) : null,
                toDepartmentId: toDepartmentId ? Number(toDepartmentId) : null,
                fromDesignationId: fromDesignationId ? Number(fromDesignationId) : null,
                toDesignationId: toDesignationId ? Number(toDesignationId) : null,
                reason: reason.trim(),
                effectiveDate,
                approvalStatus: approvalStatus as any,
                approvedById: approvedById ? Number(approvedById) : undefined,
                approvedAt: approvedAt || undefined,
                status,
              })
            }
            disabled={busy || !employeeId || !effectiveDate || success}
          >
            {busy ? 'Creating…' : 'Create'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* ---------------------------- Edit Transfer Modal ---------------------------- */
function EditTransferModal({
  open,
  onClose,
  row,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  row: TransferRow | null;
  onConfirm: (payload: Partial<TransferCreatePayload>) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  const [approvalStatus, setApprovalStatus] = useState<string>('Pending');
  const [effectiveDate, setEffectiveDate] = useState<string>('');
  const [reason, setReason] = useState<string>('');
  const [status, setStatus] = useState<boolean>(true);

  useEffect(() => {
    if (open && row) {
      setApprovalStatus(row.approvalStatus || 'Pending');
      setEffectiveDate(row.effectiveDate || '');
      setReason(row.reason || '');
      setStatus(!!row.status);
    }
  }, [open, row]);

  if (!open || !row) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Transfer Request">
      <div className="space-y-3">
        <Grid gutter="md">
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="Approval Status"
              data={[
                { value: 'Pending', label: 'Pending' },
                { value: 'Approved', label: 'Approved' },
                { value: 'Rejected', label: 'Rejected' },
              ]}
              value={approvalStatus}
              onChange={(v) => setApprovalStatus(v || 'Pending')}
              disabled={busy || success}
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Effective Date (YYYY-MM-DD)"
              value={effectiveDate}
              onChange={(e) => setEffectiveDate(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12 }}>
            <TextInput
              label="Reason"
              value={reason}
              onChange={(e) => setReason(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12 }}>
            <Switch
              label="Active"
              checked={status}
              onChange={(e) => setStatus(e.currentTarget.checked)}
              disabled={busy || success}
            />
          </Grid.Col>
        </Grid>

        {!!error && <Text c="red" size="sm">{error}</Text>}
        {success && !error && <Text c="green" size="sm">Transfer updated. Closing…</Text>}

        <div className="flex items-center gap-5 justify-end mt-8">
          <Button onClick={onClose} color="red" type="button" disabled={busy}>
            Cancel
          </Button>
          <Button
            type="submit"
            onClick={() =>
              onConfirm({
                approvalStatus: approvalStatus as any,
                effectiveDate,
                reason,
                status,
              })
            }
            disabled={busy || success}
          >
            {busy ? 'Saving…' : 'Save'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* --------------------------- Delete Transfer Modal --------------------------- */
function DeleteTransferModal({
  open,
  onClose,
  row,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  row: TransferRow | null;
  onConfirm: () => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  if (!open || !row) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete transfer request">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete transfer #{row.id} for Employee {row.employeeId}?
      </p>

      {!!error && <p className="mt-2 text-sm text-red-600">{error}</p>}
      {success && !error && (
        <p className="mt-2 text-sm text-green-600">Transfer deleted. Closing…</p>
      )}

      <div className="mt-4 flex items-center justify-end gap-2">
        <button
          onClick={onClose}
          className="px-3 py-1.5 rounded-md border text-sm"
          disabled={busy}
        >
          {success ? 'Close now' : 'Cancel'}
        </button>
        <button
          onClick={onConfirm}
          className="px-3 py-1.5 rounded-md bg-red-600 text-white text-sm disabled:opacity-60"
          disabled={busy || success}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </button>
      </div>
    </Modal>
  );
}

/* ---------------------------------- Page ---------------------------------- */
export default function TransferEmployee() {
  const dispatch = useAppDispatch();
  // const { user } = useAppSelector(s => s.auth);

  //  console.log('userr===',user)

  // transfer slice
  const {
    list,
    loadingList: loading,
    errorList: error,
    loadingCreate: creating,
    errorCreate: createError,
    loadingUpdate: updating,
    errorUpdate: updateError,
    loadingDelete: deleting,
    errorDelete: deleteError,
  } = useSelector((s: RootState) => s.transfer);

  // source lists
  const companies    = useSelector(selectCompanyArray);
  const workplaces   = useSelector(selectWorkplaceArray);
  const designations = useSelector(selectDesignationArray);
  const departments  = useSelector(selectDepartmentArray);
  const employees    = useSelector(selectEmployeeArray);

  // initial fetch (explicit params so it works with your thunks)
  useEffect(() => {
    dispatch<any>(fetchTransfers(0, 10));

    if (!companies.length)     dispatch<any>(fetchCompanies({ page: 1, size: 1000 } as any));
    if (!workplaces.length)    dispatch<any>(fetchWorkplaces(1, 1000));
    if (!designations.length)  dispatch<any>(fetchDesignations(1, 1000));
    if (!departments.length)   dispatch<any>(fetchDepartments(1, 1000));  // ← NEW
    if (!employees.length)     dispatch<any>(fetchEmployees(1, 1000));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // options
  const companyOptions     = useMemo(() => toCompanyOptions(companies), [companies]);
  const workplaceOptions   = useMemo(() => toWorkplaceOptions(workplaces), [workplaces]); // FLAT
  const designationOptions = useMemo(() => toDesignationOptions(designations), [designations]);
  const departmentOptions  = useMemo(() => toDepartmentOptions(departments), [departments]); // NEW
  const employeeOptions    = useMemo(() => toEmployeeOptions(employees), [employees]);

  const data = useMemo<TransferRow[]>(() => list ?? [], [list]);

  // Create modal state
  const [openCreate, setOpenCreate] = useState(false);
  const [justSubmittedCreate, setJustSubmittedCreate] = useState(false);
  const creationSuccess = justSubmittedCreate && !creating && !createError;

  // Edit modal state
  const [openEdit, setOpenEdit] = useState(false);
  const [editing, setEditing] = useState<TransferRow | null>(null);
  const [justSubmittedEdit, setJustSubmittedEdit] = useState(false);
  const editSuccess = justSubmittedEdit && !updating && !updateError;

  // Delete modal state
  const [openDelete, setOpenDelete] = useState(false);
  const [deletingTarget, setDeletingTarget] = useState<TransferRow | null>(null);
  const [justSubmittedDelete, setJustSubmittedDelete] = useState(false);
  const deleteSuccess = justSubmittedDelete && !deleting && !deleteError;

  // Auto-close effects
  useEffect(() => {
    if (!(creationSuccess && openCreate)) return;
    const t = setTimeout(() => {
      setOpenCreate(false);
      setJustSubmittedCreate(false);
    }, 1200);
    return () => clearTimeout(t);
  }, [creationSuccess, openCreate]);

  useEffect(() => {
    if (!(editSuccess && openEdit)) return;
    const t = setTimeout(() => {
      setOpenEdit(false);
      setJustSubmittedEdit(false);
    }, 800);
    return () => clearTimeout(t);
  }, [editSuccess, openEdit]);

  useEffect(() => {
    if (!(deleteSuccess && openDelete)) return;
    const t = setTimeout(() => {
      setOpenDelete(false);
      setJustSubmittedDelete(false);
      setDeletingTarget(null);
    }, 800);
    return () => clearTimeout(t);
  }, [deleteSuccess, openDelete]);

  const columns: ColumnDef<TransferRow, any>[] = useMemo(
    () => [
      {
        id: 'select',
        enableSorting: false,
        enableHiding: false,
        header: ({ table }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={table.getIsAllPageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        ),
        cell: ({ row }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={row.getIsSelected()}
            disabled={!row.getCanSelect()}
            onChange={row.getToggleSelectedHandler()}
          />
        ),
      },
      {
        accessorKey: 'employeeId',
        header: 'Employee',
        cell: ({ row }) => {
          const e = row.original as TransferRow;
          return (
            <div className="flex items-center gap-3">
              <div className="h-8 w-8 flex items-center justify-center rounded-full bg-gray-200 text-gray-600 font-semibold">
                {(String(e.employeeId) || '?').charAt(0)}
              </div>
              <div>
                <div className="font-medium text-gray-700">#{e.employeeId}</div>
                <div className="text-xs text-gray-400">{e.requestType}</div>
              </div>
            </div>
          );
        },
      },
      {
        id: 'fromTo',
        header: 'From → To',
        cell: ({ row }) => {
          const r = row.original;
          const from = `C${r.fromCompanyId ?? '-'} / W${r.fromWorkplaceId ?? '-'} / D${r.fromDepartmentId ?? '-'} / Des${r.fromDesignationId ?? '-'}`;
          const to = `C${r.toCompanyId ?? '-'} / W${r.toWorkplaceId ?? '-'} / D${r.toDepartmentId ?? '-'} / Des${r.toDesignationId ?? '-'}`;
          return (
            <div className="text-xs">
              <div className="text-gray-600">{from}</div>
              <div className="text-gray-400">→ {to}</div>
            </div>
          );
        },
      },
      { accessorKey: 'effectiveDate', header: 'Effective Date' },
      {
        accessorKey: 'approvalStatus',
        header: 'Approval',
        filterFn: 'equals',
        cell: ({ getValue }) => {
          const v = String(getValue() ?? 'Pending');
          const isApproved = v === 'Approved';
          const isRejected = v === 'Rejected';
          const cls = isApproved
            ? 'bg-green-100 text-green-700'
            : isRejected
              ? 'bg-red-100 text-red-700'
              : 'bg-amber-100 text-amber-700';
          return <span className={`px-2 py-0.5 text-xs font-medium rounded ${cls}`}>{v}</span>;
        },
      },
      {
        accessorKey: 'status',
        header: 'Active',
        filterFn: 'equals',
        cell: ({ getValue }) =>
          getValue() ? (
            <span className="px-2 py-0.5 text-xs font-medium rounded bg-green-100 text-green-700">
              Active
            </span>
          ) : (
            <span className="px-2 py-0.5 text-xs font-medium rounded bg-gray-100 text-gray-600">
              Inactive
            </span>
          ),
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
                  setEditing(row.original as TransferRow);
                  setOpenEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  modals.openConfirmModal({
                    title: 'Delete confirmation',
                    centered: true,
                    children: <Text size="sm">Are you sure you want to delete this transfer request?</Text>,
                    labels: { confirm: 'Delete', cancel: 'Cancel' },
                    confirmProps: { color: 'red' },
                    onConfirm: async () => {
                      try {
                        setDeletingTarget(row.original as TransferRow);
                        setOpenDelete(true);
                      } catch (error) {
                        console.error('Error message:', error);
                        notifications.show({
                          title: 'Failed to delete',
                          message: 'Something went wrong',
                        });
                      }
                    },
                  });
                },
              },
            ]}
          >
            ...
          </RowActionDropdown>
        ),
      },
    ],
    []
  );

  const selectFilters: SelectFilterDef<TransferRow>[] = [
    {
      id: 'approvalStatus',
      label: 'Approval',
      options: [
        { label: 'Pending', value: 'Pending' },
        { label: 'Approved', value: 'Approved' },
        { label: 'Rejected', value: 'Rejected' },
      ],
    },
    {
      id: 'requestType',
      label: 'Type',
      options: [
        { label: 'IntraCompany', value: 'IntraCompany' },
        { label: 'InterCompany', value: 'InterCompany' },
      ],
    },
  ];

  const handleCreateConfirm = async (payload: TransferCreatePayload) => {
    setJustSubmittedCreate(true);
    await dispatch<any>(createTransfer(payload));
  };

  const handleEditConfirm = async (payload: Partial<TransferCreatePayload>) => {
    if (!editing) return;
    setJustSubmittedEdit(true);
    await dispatch<any>(updateTransferById(editing.id, payload));
  };

  const handleDeleteConfirm = async () => {
    if (!deletingTarget) return;
    setJustSubmittedDelete(true);
    await dispatch<any>(deleteTransferById(deletingTarget.id));
  };

  return (
    <>
      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
        <WorkCard
          icon={<ClockIcon className="h-5 w-5" />}
          label="Transfers (sample KPI)"
          value={String(data.length)}
          bgGradient="bg-gradient-to-r from-sky-500 to-emerald-500"
          labelClass="text-white/70"
          valueClass="text-yellow-50"
          title="Total loaded"
          titleClass="text-white/70"
          iconBg="bg-red-600"
        />
        <WorkCard
          icon={<ClockIcon className="h-5 w-5" />}
          label="Working shift"
          value="09:00 AM - 06:00 PM"
          title="General shift"
          iconBg="bg-primary"
        />
        <WorkCard
          icon={<CalendarDaysIcon className="h-5 w-5" />}
          label="Joining date"
          value="01 Jan, 2024"
          iconBg="bg-primary"
        />
        <WorkCard
          icon={<CalendarDaysIcon className="h-5 w-5" />}
          label="Length of service"
          value="8 months 10 days"
          iconBg="bg-primary"
        />
      </div>

      {/* Table & Modals */}
      <div className="p-6 bg-white rounded-lg shadow max-w-full">
        <DataTable<TransferRow>
          data={data}
          columns={columns}
          selectFilters={selectFilters}
          initialColumnVisibility={{}}
          searchPlaceholder="Search..."
          csvFileName="transfer-requests"
          enableRowSelection
          headerLeft={<Breadcrumbs />}
          onPressAdd={() => {
            setOpenCreate(true);
            setJustSubmittedCreate(false);
          }}
        />

        {loading && <p className="mt-3 text-sm text-gray-500">Loading transfers…</p>}
        {error && <p className="mt-3 text-sm text-red-600">{error}</p>}
        {!loading && !error && data.length === 0 && (
          <p className="mt-3 text-sm text-gray-500">No data found</p>
        )}

        {/* Create */}
        <CreateTransferModal
          open={openCreate}
          onClose={() => setOpenCreate(false)}
          onConfirm={handleCreateConfirm}
          busy={creating}
          error={createError}
          success={creationSuccess}
          employeeOptions={employeeOptions}
          companyOptions={companyOptions}
          workplaceOptions={workplaceOptions}      // FLAT
          departmentOptions={departmentOptions}    // NEW
          designationOptions={designationOptions}
        />

        {/* Edit */}
        <EditTransferModal
          open={openEdit}
          onClose={() => setOpenEdit(false)}
          row={editing}
          onConfirm={handleEditConfirm}
          busy={updating}
          error={updateError}
          success={editSuccess}
        />

        {/* Delete */}
        <DeleteTransferModal
          open={openDelete}
          onClose={() => setOpenDelete(false)}
          row={deletingTarget}
          onConfirm={handleDeleteConfirm}
          busy={deleting}
          error={deleteError}
          success={deleteSuccess}
        />
      </div>
    </>
  );
}
