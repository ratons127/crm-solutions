'use client';

import { Badge, Button, Collapse, Select } from '@mantine/core';
import { useMemo, useState } from 'react';
import { ColumnDef } from '@tanstack/react-table';
import { DataTable } from '@/components/common/table/DataTable';
import { FiFilter, FiRefreshCcw, FiUsers } from 'react-icons/fi';
import { IoCalendarClearOutline } from 'react-icons/io5';
import { DateInput } from '@mantine/dates';
import { TbReport } from 'react-icons/tb';
import { BsDownload } from 'react-icons/bs';
import { FaFileAlt } from 'react-icons/fa';
import { GoChevronDown, GoChevronUp } from 'react-icons/go';

/* ============================
   Type Definition
============================ */
interface AttendanceReportBusinessUnit {
  id: number;
  empId: string;
  name: string;
  designation: string;
  totalDays: number;
  presentDays: number;
  presentPercent: number;
  absentDays: number;
  leaveDays: number;
  lateDays: number;
  earlyOutDays: number;
  manualAttendance: number;
  offDays: number;
  holidays: number;
}

/* ============================
   Sample Data
============================ */
const departmentData: AttendanceReportBusinessUnit[] = [
  {
    id: 1,
    empId: 'EMP001',
    name: 'John Doe',
    designation: 'Senior Software Engineer',
    totalDays: 22,
    presentDays: 20,
    presentPercent: 90,
    absentDays: 0,
    leaveDays: 2,
    lateDays: 3,
    earlyOutDays: 1,
    manualAttendance: 2,
    offDays: 4,
    holidays: 2,
  },
  {
    id: 2,
    empId: 'EMP002',
    name: 'Jane Smith',
    designation: 'Product Manager',
    totalDays: 22,
    presentDays: 21,
    presentPercent: 95,
    absentDays: 1,
    leaveDays: 0,
    lateDays: 2,
    earlyOutDays: 2,
    manualAttendance: 2,
    offDays: 4,
    holidays: 2,
  },
  {
    id: 3,
    empId: 'EMP003',
    name: 'Mike Johnson',
    designation: 'QA Lead',
    totalDays: 22,
    presentDays: 19,
    presentPercent: 86,
    absentDays: 2,
    leaveDays: 1,
    lateDays: 4,
    earlyOutDays: 3,
    manualAttendance: 2,
    offDays: 4,
    holidays: 2,
  },
];

/* ============================
   Component
============================ */
export default function ReportBusinessUnit() {
  const [opened, setOpened] = useState(false);

  const totalEmployees = departmentData.length;
  const totalPresent = departmentData.reduce((a, b) => a + b.presentDays, 0);
  const totalAbsent = departmentData.reduce((a, b) => a + b.absentDays, 0);
  const totalLate = departmentData.reduce((a, b) => a + b.lateDays, 0);
  const avgAttendance = Math.round(
    departmentData.reduce((a, b) => a + b.presentPercent, 0) / totalEmployees
  );

  /* ===== Table Columns ===== */
  const columns: ColumnDef<AttendanceReportBusinessUnit, any>[] = useMemo(
    () => [
      {
        header: 'Employee Name',
        cell: ({ row }) => (
          <div className="flex flex-col">
            <span className="font-semibold text-slate-800">
              {row.original.name}
            </span>
            <span className="text-xs text-slate-500">{row.original.empId}</span>
          </div>
        ),
      },
      { header: 'Designation', accessorKey: 'designation' },
      { header: 'Total Working Days', accessorKey: 'totalDays' },
      {
        header: 'Present Days',
        cell: ({ row }) => (
          <div className="flex items-center gap-1">
            <Badge color="green" variant="light" radius="sm">
              {row.original.presentDays}
            </Badge>
            <span className="text-slate-500 text-xs font-medium">
              ({row.original.presentPercent}%)
            </span>
          </div>
        ),
      },
      {
        header: 'Absent Days',
        cell: ({ row }) => (
          <span
            className={`${
              row.original.absentDays > 0
                ? 'text-red-500 font-medium'
                : 'text-slate-500'
            }`}
          >
            {row.original.absentDays}
          </span>
        ),
      },
      {
        header: 'Leave',
        cell: ({ row }) => (
          <Badge color="blue" variant="light" radius="sm">
            {row.original.leaveDays}
          </Badge>
        ),
      },
      {
        header: 'Late Days',
        cell: ({ row }) => (
          <Badge color="yellow" variant="light" radius="sm">
            {row.original.lateDays}
          </Badge>
        ),
      },
      {
        header: 'Early Out Days',
        cell: ({ row }) => (
          <Badge color="orange" variant="light" radius="sm">
            {row.original.earlyOutDays}
          </Badge>
        ),
      },
      {
        header: 'Manual Attendance',
        cell: ({ row }) => (
          <Badge color="indigo" variant="light" radius="sm">
            {row.original.manualAttendance}
          </Badge>
        ),
      },
      {
        header: 'Off Days',
        cell: ({ row }) => (
          <Badge color="violet" variant="light" radius="sm">
            {row.original.offDays}
          </Badge>
        ),
      },
      {
        header: 'Holidays',
        cell: ({ row }) => (
          <Badge color="cyan" variant="light" radius="sm">
            {row.original.holidays}
          </Badge>
        ),
      },
    ],
    []
  );

  return (
    <div className="px-5 py-6 space-y-8">
      {/* ===== Header ===== */}
      <div className="grid grid-cols-2 items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-[#F69348]  flex items-center justify-center rounded-xl">
            <TbReport size={20} color="white" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-[#1D293D]">
              Attendance Report - Business Unit Wise
            </h1>
            <p className="text-base text-[#45556C]">
              Generate comprehensive attendance reports grouped by business unit
            </p>
          </div>
        </div>

        <div className="flex justify-end gap-3">
          {/* Reset Filters Button */}
          <Button
            leftSection={<FiRefreshCcw size={16} />}
            variant="outline"
            radius="md"
            color="gray"
            styles={{
              root: {
                backgroundColor: '#F8FAFC',
                border: '1px solid #E2E8F0',
                color: '#1E293B',
                fontWeight: 500,
                fontSize: '14px',
                paddingInline: '12px',
              },
              section: { marginRight: 6, color: '#64748B' },
            }}
          >
            Reset Filters
          </Button>

          {/* Toggle Filters Button */}
          <Button
            onClick={() => setOpened(prev => !prev)}
            variant="outline"
            radius="md"
            color="gray"
            leftSection={
              opened ? (
                <GoChevronUp size={16} className="text-gray-600" />
              ) : (
                <GoChevronDown size={16} className="text-gray-600" />
              )
            }
            styles={{
              root: {
                backgroundColor: '#F8FAFC',
                border: '1px solid #E2E8F0',
                color: '#1E293B',
                fontWeight: 500,
                fontSize: '14px',
                paddingInline: '12px',
              },
              section: { marginRight: 6, color: '#64748B' },
            }}
          >
            {opened ? 'Hide Filters' : 'Show Filters'}
          </Button>
        </div>
      </div>

      {/* ===== Filter Section (Animated) ===== */}
      <Collapse in={opened} transitionDuration={250}>
        <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
          <div className="">
            <div className="flex items-center gap-3">
              <FiFilter className="text-blue-600" />
              <span className="text-base font-semibold text-[#1E293B]">
                Filters
              </span>
            </div>
          </div>
          <div className="py-3 ">
            <div className=" grid grid-cols-1 md:grid-cols-4 gap-3 ">
              <DateInput
                leftSection={<IoCalendarClearOutline size={18} />}
                required
                size="md"
                radius="md"
                label="Start Date "
                placeholder="Select date"
              />
              <DateInput
                required
                size="md"
                radius="md"
                label="End Date"
                leftSection={<IoCalendarClearOutline size={18} />}
                placeholder="Select date"
              />

              <Select
                size="md"
                radius="md"
                label="Company"
                placeholder="Company name"
                data={['IT Department', 'Finance', 'HR']}
              />
              <Select
                size="md"
                radius="md"
                label="Shift"
                placeholder="ALL"
                data={['IT Department', 'Finance', 'HR']}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-5 gap-3 mt-4">
              <Select
                size="md"
                radius="md"
                label="Business Unit"
                placeholder="ALL"
                data={[
                  'Missing Biometric',
                  'Forgot to Punch',
                  'Device Malfunction',
                ]}
              />
              <Select
                size="md"
                radius="md"
                placeholder="ALl"
                label="Workplace Group"
                data={[
                  'Missing Biometric',
                  'Forgot to Punch',
                  'Device Malfunction',
                ]}
              />
              <Select
                size="md"
                radius="md"
                placeholder="All"
                label="Workplace"
                data={['Pending', 'Approved', 'Rejected']}
              />
              <Select
                size="md"
                radius="md"
                label="Department"
                placeholder="All"
                data={['Pending', 'Approved', 'Rejected']}
              />
              <Select
                size="md"
                radius="md"
                label="Team"
                placeholder="All"
                data={['Pending', 'Approved', 'Rejected']}
              />
            </div>
          </div>
        </div>
      </Collapse>

      {/* ===== Export Section ===== */}
      <div className="bg-white rounded-2xl shadow-sm border border-slate-200 px-6 py-4 flex items-center justify-between">
        <div className="flex flex-col">
          <h3 className="text-base font-semibold text-slate-800">
            Export Report
          </h3>
          <p className="text-sm text-slate-500">
            Download this report in your preferred format
          </p>
        </div>

        <div className="flex gap-3">
          <Button
            leftSection={<BsDownload size={16} />}
            variant="outline"
            radius="md"
            color="gray"
            styles={{
              root: {
                backgroundColor: '#F8FAFC',
                border: '1px solid #E2E8F0',
                color: '#1E293B',
                fontWeight: 500,
                fontSize: '14px',
                paddingInline: '14px',
              },
              section: { marginRight: 6, color: '#64748B' },
            }}
          >
            Export CSV
          </Button>

          <Button
            leftSection={<FaFileAlt size={16} />}
            radius="md"
            color="orange"
            styles={{
              root: {
                backgroundColor: '#F97316',
                color: 'white',
                fontWeight: 500,
                fontSize: '14px',
                paddingInline: '14px',
              },
              section: { marginRight: 6, color: 'white' },
            }}
          >
            Export PDF
          </Button>
        </div>
      </div>

      {/* ===== Department Summary Table ===== */}
      <div className="bg-white rounded-2xl shadow-md border border-slate-200 overflow-hidden">
        <div className="flex justify-between items-center bg-[#F69348] px-6 py-4 text-white">
          <div className="flex flex-col">
            <div className="flex items-center gap-2">
              <div className="bg-white/25 rounded-lg p-2">
                <FiUsers size={18} />
              </div>
              <h2 className="text-lg font-semibold">Technology</h2>
            </div>
            <p className="text-sm text-orange-50 mt-1">
              {totalEmployees} Employees • Avg Attendance: {avgAttendance}%
            </p>
          </div>

          <div className="flex gap-6 text-sm font-medium">
            <div className="flex flex-col items-center">
              <span className="text-xs text-orange-100">Present</span>
              <span className="text-white">{totalPresent}</span>
            </div>
            <div className="flex flex-col items-center">
              <span className="text-xs text-orange-100">Absent</span>
              <span className="text-white">{totalAbsent}</span>
            </div>
            <div className="flex flex-col items-center">
              <span className="text-xs text-orange-100">Late</span>
              <span className="text-white">{totalLate}</span>
            </div>
          </div>
        </div>

        <div className="p-6">
          <DataTable<AttendanceReportBusinessUnit>
            data={departmentData}
            columns={columns}
            hideExport
            hideSearch
            hideColumnVisibility
            hidePagination
          />
        </div>
      </div>
    </div>
  );
}
