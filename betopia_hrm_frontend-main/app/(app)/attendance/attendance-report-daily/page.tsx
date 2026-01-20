'use client';

import { DataTable } from '@/components/common/table/DataTable';
import { useAppState } from '@/lib/features/app/appSlice';
import { useGetShiftDefinitionListQuery } from '@/lib/features/shiftDefinition/shiftDefinitionAPI';
import { useLazyGetHierarchyFilterQuery } from '@/services/api/admin/company/hierarchyFilterAPI';
import {
  useExportAttendanceDailyReportMutation,
  useGetAttendanceDailyReportMutation,
} from '@/services/api/attendance/attendanceReportAPI';
import { useGetEmployeeListQuery } from '@/services/api/employee/employeeProfileAPI';
import { Button, Select } from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useEffect, useMemo, useState } from 'react';
import { CiFilter, CiLocationOn } from 'react-icons/ci';
import { FaRegFilePdf } from 'react-icons/fa6';
import { FiFilter } from 'react-icons/fi';
import { GrDocumentCsv } from 'react-icons/gr';
import { IoBusinessOutline } from 'react-icons/io5';
import { LuBuilding2 } from 'react-icons/lu';
import { RiFileExcel2Line, RiGroupLine } from 'react-icons/ri';

type IdName = { id: number; name: string };
const toOptions = (arr: IdName[] = []) =>
  arr.map(x => ({ value: String(x.id), label: x.name ?? String(x.id) }));

export default function AttendanceReportDaily() {
  const { auth } = useAppState();
  const { data: employeeList } = useGetEmployeeListQuery(undefined);
  const { data: shiftList } = useGetShiftDefinitionListQuery(undefined);

  // Hierarchy options
  const [
    fetchHierarchy,
    { data: hierarchyData, isFetching: hierarchyLoading },
  ] = useLazyGetHierarchyFilterQuery();

  useEffect(() => {
    if (auth?.user?.employeeId) {
      setEmployeeId(String(auth.user.employeeId));
    }
  }, [auth?.user?.employeeId]);

  const [companies, setCompanies] = useState<IdName[]>([]);
  const [businessUnits, setBusinessUnits] = useState<IdName[]>([]);
  const [workplaceGroups, setWorkplaceGroups] = useState<IdName[]>([]);
  const [workplaces, setWorkplaces] = useState<IdName[]>([]);
  const [departments, setDepartments] = useState<IdName[]>([]);
  const [teams, setTeams] = useState<IdName[]>([]);

  // Filter values
  const [fromDate, setFromDate] = useState<string | null>(null); // YYYY-MM-DD
  const [toDate, setToDate] = useState<string | null>(null); // YYYY-MM-DD
  const [employeeId, setEmployeeId] = useState<string | null>(null);
  const [shiftId, setShiftId] = useState<string | null>(null);
  const [companyId, setCompanyId] = useState<string | null>(null);
  const [businessUnitId, setBusinessUnitId] = useState<string | null>(null);
  const [workplaceGroupId, setWorkplaceGroupId] = useState<string | null>(null);
  const [workplaceId, setWorkplaceId] = useState<string | null>(null);
  const [departmentId, setDepartmentId] = useState<string | null>(null);
  const [teamId, setTeamId] = useState<string | null>(null);

  // Daily report
  const [getDailyReport, { isLoading: fetchingReport }] =
    useGetAttendanceDailyReportMutation();
  const [rows, setRows] = useState<any[]>([]);

  // Export
  const [exportReport] = useExportAttendanceDailyReportMutation();
  const [exportingFormat, setExportingFormat] = useState<
    'excel' | 'pdf' | 'csv' | null
  >(null);

  const buildPayload = () => ({
    employeeId,
    fromDate,
    toDate,
    shiftId,
    businessUnitId,
    workplaceGroupId,
    workplaceId,
    departmentId,
    teamId,
  });

  const downloadBlob = (blob: Blob, filename: string) => {
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  };

  const handleExport = async (format: 'excel' | 'pdf' | 'csv') => {
    if (!employeeId) {
      notifications.show({
        color: 'red',
        message: 'Please select an Employee before exporting',
      });
      return;
    }
    const payload = { ...buildPayload(), format } as any;
    const ext = format === 'excel' ? 'xlsx' : format;
    const fname = `attendance_daily_${fromDate ?? 'from'}_${toDate ?? 'to'}.${ext}`;
    try {
      setExportingFormat(format);
      const blob = await exportReport(payload).unwrap();
      downloadBlob(blob as any, fname);
    } catch (e) {
      // no-op; backend message likely surfaced elsewhere
    } finally {
      setExportingFormat(null);
    }
  };

  useEffect(() => {
    fetchHierarchy({});
  }, [fetchHierarchy]);

  useEffect(() => {
    if (!hierarchyData?.data) return;
    const d = hierarchyData.data;
    setCompanies(d.companies ?? []);
    setBusinessUnits(d.businessUnits ?? []);
    setWorkplaceGroups(d.workplaceGroups ?? []);
    setWorkplaces(d.workplaces ?? []);
    setDepartments(d.departments ?? []);
    setTeams(d.teams ?? []);
  }, [hierarchyData]);

  const handleHierarchyChange = async (
    level: 'company' | 'bu' | 'wpg' | 'wp' | 'dept',
    id: string | null
  ) => {
    const params: Record<string, number> = {};
    if (level === 'company') {
      setCompanyId(id);
      setBusinessUnitId(null);
      setWorkplaceGroupId(null);
      setWorkplaceId(null);
      setDepartmentId(null);
      setTeamId(null);
      if (id) params.companyId = Number(id);
    } else if (level === 'bu') {
      setBusinessUnitId(id);
      setWorkplaceGroupId(null);
      setWorkplaceId(null);
      setDepartmentId(null);
      setTeamId(null);
      if (companyId) params.companyId = Number(companyId);
      if (id) params.businessUnitId = Number(id);
    } else if (level === 'wpg') {
      setWorkplaceGroupId(id);
      setWorkplaceId(null);
      setDepartmentId(null);
      setTeamId(null);
      if (companyId) params.companyId = Number(companyId);
      if (businessUnitId) params.businessUnitId = Number(businessUnitId);
      if (id) params.workplaceGroupId = Number(id);
    } else if (level === 'wp') {
      setWorkplaceId(id);
      setDepartmentId(null);
      setTeamId(null);
      if (companyId) params.companyId = Number(companyId);
      if (businessUnitId) params.businessUnitId = Number(businessUnitId);
      if (workplaceGroupId) params.workplaceGroupId = Number(workplaceGroupId);
      if (id) params.workplaceId = Number(id);
    } else if (level === 'dept') {
      setDepartmentId(id);
      setTeamId(null);
      if (companyId) params.companyId = Number(companyId);
      if (businessUnitId) params.businessUnitId = Number(businessUnitId);
      if (workplaceGroupId) params.workplaceGroupId = Number(workplaceGroupId);
      if (workplaceId) params.workplaceId = Number(workplaceId);
      if (id) params.departmentId = Number(id);
    }
    await fetchHierarchy(params);
  };

  const toNum = (s: string | null): number | undefined =>
    s ? Number(s) : undefined;
  const toYMD = (s: string | null): string | undefined => s ?? undefined;

  const onApplyFilters = async () => {
    const effectiveEmployeeId =
      employeeId ?? String(auth?.user?.employeeId ?? '');

    // Validate employee, fromDate, and toDate
    if (!effectiveEmployeeId) {
      notifications.show({
        color: 'red',
        message: 'Employee is required to fetch report',
      });
      return;
    }

    if (!fromDate || !toDate) {
      notifications.show({
        color: 'red',
        message:
          'Please select both From Date and To Date before applying filters',
      });
      return;
    }

    const payload = {
      employeeId: toNum(effectiveEmployeeId),
      fromDate: toYMD(fromDate),
      toDate: toYMD(toDate),
      shiftId: toNum(shiftId),
      businessUnitId: toNum(businessUnitId),
      workplaceGroupId: toNum(workplaceGroupId),
      workplaceId: toNum(workplaceId),
      departmentId: toNum(departmentId),
      teamId: toNum(teamId),
    } as any;

    try {
      const res = await getDailyReport(payload).unwrap();
      setRows(res?.data ?? []);
    } catch {
      setRows([]);
    }
  };

  const columns: ColumnDef<any, any>[] = useMemo(
    () => [
      {
        header: 'Date',
        accessorKey: 'date',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'Shift Name',
        accessorKey: 'shiftName',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'In Time',
        accessorKey: 'inTime',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'Out Time',
        accessorKey: 'outTime',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'Total Hours',
        accessorKey: 'totalWorkingHours',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'Late (min)',
        accessorKey: 'lateMinutes',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'Early Leave (min)',
        accessorKey: 'earlyLeaveMinutes',
        cell: ({ getValue }) => getValue() || '--',
      },
      {
        header: 'Day Status',
        accessorKey: 'dayStatus',
        cell: ({ getValue }) => getValue() || '--',
      },
    ],
    []
  );

  return (
    <div className="space-y-8">
      {/* ===== Header ===== */}
      <div className="flex items-center gap-3">
        <div className="w-10 h-10 bg-orange-500 flex items-center justify-center rounded-xl">
          <RiGroupLine size={20} color="white" />
        </div>
        <div>
          <h1 className="text-3xl font-bold text-[#1D293D]">
            Daily Attendance Report
          </h1>
          <p className="text-base text-[#45556C]">
            Filter and view daily attendance
          </p>
        </div>
      </div>

      {/* ===== Filter Section ===== */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        {/* Header Row */}
        <div className="grid grid-cols-2 items-center justify-between">
          <div className="flex flex-col gap-1">
            <div className="flex items-center gap-3">
              <FiFilter className="text-blue-600" size={19} />
              <span className="text-base font-semibold text-[#1E293B]">
                Filters
              </span>
            </div>
            <span className="text-base text-[#64748B] pb-2">
              Select filters and apply
            </span>
          </div>

          <div className="flex justify-end gap-2">
            <Button
              leftSection={<CiFilter size={18} />}
              radius="md"
              loading={fetchingReport}
              onClick={onApplyFilters}
            >
              Apply Filters
            </Button>
          </div>
        </div>

        {/* Filters Grid */}
        <div className="mt-5 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {/* From/To Dates */}
          <DatePickerInput
            withAsterisk
            label="From Date"
            placeholder="Select from date"
            size="sm"
            radius="md"
            value={fromDate ? new Date(fromDate) : null}
            onChange={d =>
              setFromDate(
                d ? new Date(d as any).toISOString().split('T')[0] : null
              )
            }
            clearable
          />
          <DatePickerInput
            withAsterisk
            label="To Date"
            placeholder="Select to date"
            size="sm"
            radius="md"
            value={toDate ? new Date(toDate) : null}
            onChange={d =>
              setToDate(
                d ? new Date(d as any).toISOString().split('T')[0] : null
              )
            }
            clearable
          />

          {/* Employee */}
          <Select
            withAsterisk
            label="Employee"
            size="sm"
            radius="md"
            data={
              auth.user?.employeeId
                ? [
                    {
                      value: String(auth.user.employeeId),
                      label: `${auth.user.name} - ${auth.user.employeeId}`,
                    },
                  ]
                : (employeeList?.data?.map((e: any) => ({
                    value: String(e.id),
                    label: `${e.firstName} ${e.lastName} - ${e.id}`,
                  })) ?? [])
            }
            value={employeeId ?? String(auth.user?.employeeId ?? '')}
            onChange={setEmployeeId}
            placeholder="Select Employee"
            disabled={!!auth.user?.employeeId} // 🔒 disables only if session has employeeId
          />

          {/* Shift */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <RiGroupLine className="text-[#0092B8]" />
                <span className="text-base text-[#1E293B]">Shift</span>
              </div>
            }
            size="sm"
            radius="md"
            data={
              shiftList?.data?.map((c: any) => ({
                label: `${c.shiftName} - ${c.shiftCode}`,
                value: String(c.id),
              })) ?? []
            }
            value={shiftId}
            onChange={setShiftId}
            placeholder="Select Shift"
            searchable
            clearable
          />

          {/* Company */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <IoBusinessOutline className="text-[#155DFC]" />
                <span className="text-base text-[#1E293B]">Company</span>
              </div>
            }
            size="sm"
            radius="md"
            data={toOptions(companies)}
            value={companyId}
            onChange={v => handleHierarchyChange('company', v)}
            placeholder="Select Company"
            searchable
            clearable
            disabled={hierarchyLoading}
          />

          {/* Business Unit */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <LuBuilding2 className="text-[#9810FA]" />
                <span className="text-base text-[#1E293B]">Business Unit</span>
              </div>
            }
            size="sm"
            radius="md"
            data={toOptions(businessUnits)}
            value={businessUnitId}
            onChange={v => handleHierarchyChange('bu', v)}
            placeholder="Select Business Unit"
            searchable
            clearable
            disabled={!companyId || hierarchyLoading}
          />

          {/* Workplace Group */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <CiLocationOn className="text-[#155DFC]" />
                <span className="text-base text-[#1E293B]">
                  Workplace Group
                </span>
              </div>
            }
            size="sm"
            radius="md"
            placeholder="Select Workplace Group"
            data={toOptions(workplaceGroups)}
            value={workplaceGroupId}
            onChange={v => handleHierarchyChange('wpg', v)}
            searchable
            clearable
            disabled={!businessUnitId || hierarchyLoading}
          />

          {/* Workplace */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <LuBuilding2 className="text-[#00A63E]" />
                <span className="text-base text-[#1E293B]">Workplace</span>
              </div>
            }
            placeholder="Select Workplace"
            size="sm"
            radius="md"
            data={toOptions(workplaces)}
            value={workplaceId}
            onChange={v => handleHierarchyChange('wp', v)}
            searchable
            clearable
            disabled={!workplaceGroupId || hierarchyLoading}
          />

          {/* Department */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 16 16"
                  fill="none"
                >
                  <path
                    d="M10.6693 13.334V2.66732C10.6693 2.3137 10.5288 1.97456 10.2787 1.72451C10.0287 1.47446 9.68956 1.33398 9.33594 1.33398H6.66927C6.31565 1.33398 5.97651 1.47446 5.72646 1.72451C5.47641 1.97456 5.33594 2.3137 5.33594 2.66732V13.334"
                    stroke="#F54900"
                    strokeWidth="1.33333"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M13.3359 4H2.66927C1.93289 4 1.33594 4.59695 1.33594 5.33333V12C1.33594 12.7364 1.93289 13.3333 2.66927 13.3333H13.3359C14.0723 13.3333 14.6693 12.7364 14.6693 12V5.33333C14.6693 4.59695 14.0723 4 13.3359 4Z"
                    stroke="#F54900"
                    strokeWidth="1.33333"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
                <span className="text-base text-[#1E293B]">Department</span>
              </div>
            }
            placeholder="Select Department"
            size="sm"
            radius="md"
            data={toOptions(departments)}
            value={departmentId}
            onChange={v => handleHierarchyChange('dept', v)}
            searchable
            clearable
            disabled={!workplaceId || hierarchyLoading}
          />

          {/* Team */}
          <Select
            label={
              <div className="flex items-center gap-3">
                <RiGroupLine className="text-[#0092B8]" />
                <span className="text-base text-[#1E293B]">Team</span>
              </div>
            }
            placeholder="Select Team"
            size="sm"
            radius="md"
            data={toOptions(teams)}
            value={teamId}
            onChange={setTeamId}
            searchable
            clearable
            disabled={!departmentId || hierarchyLoading}
          />
        </div>
      </div>

      {/* ===== Report Table ===== */}
      {rows.length > 0 ? (
        <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
          <div className="flex justify-between items-center mb-4">
            <div className="flex items-center gap-3 mb-4">
              <RiGroupLine size={18} className="text-[#F54900]" />
              <span className="text-base font-semibold text-[#1E293B]">
                Daily Attendance Report
              </span>
            </div>
            <div className="flex gap-x-2">
              <Button
                leftSection={<RiFileExcel2Line />}
                radius="md"
                loading={exportingFormat === 'excel'}
                disabled={!!exportingFormat}
                onClick={() => handleExport('excel')}
              >
                Export Excel
              </Button>
              <Button
                leftSection={<FaRegFilePdf />}
                radius="md"
                loading={exportingFormat === 'pdf'}
                disabled={!!exportingFormat}
                onClick={() => handleExport('pdf')}
              >
                Export PDF
              </Button>
              <Button
                leftSection={<GrDocumentCsv />}
                radius="md"
                loading={exportingFormat === 'csv'}
                disabled={!!exportingFormat}
                onClick={() => handleExport('csv')}
              >
                Export CSV
              </Button>
            </div>
          </div>
          <DataTable
            data={rows}
            columns={columns}
            hideExport
            hideColumnVisibility
            hideSearch
            pageSizeOptions={[100]}
          />
        </div>
      ) : (
        <div className="flex justify-center items-center text-purple-700 font-bold text-xl">
          Apply Filters to see report
        </div>
      )}
    </div>
  );
}
