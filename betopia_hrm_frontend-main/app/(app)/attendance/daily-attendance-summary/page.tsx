'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import WorkCard from '@/components/common/WorkCard';
import { Badge, Button, Modal, Select, TextInput } from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { CiRead, CiSearch, CiTimer } from 'react-icons/ci';
import { FiDownload, FiEdit, FiFilter, FiRefreshCcw } from 'react-icons/fi';
import { GoPeople, GoPersonAdd } from 'react-icons/go';
import {
  IoPersonRemoveOutline,
  IoReload,
  IoTimeOutline,
} from 'react-icons/io5';
import { MdOutlineRemoveRedEye } from 'react-icons/md';
import { TbStopwatch } from 'react-icons/tb';

/* ===========================
   Sample Data
=========================== */
const attendanceData = [
  {
    id: 1,
    name: 'John Smith',
    code: 'EMP-001',
    department: 'Engineering',
    shift: 'Morning Shift (09:00-18:00)',
    inTime: '09:15',
    outTime: '18:30',
    workedHours: '8.75',
    ot: '+0.5h',
    late: '15min',
    earlyLeave: '-',
    status: 'Late',
    remarks: 'Traffic delay',
  },
  {
    id: 2,
    name: 'Sarah Johnson',
    code: 'EMP-002',
    department: 'Marketing',
    shift: 'Morning Shift (09:00-18:00)',
    inTime: '08:55',
    outTime: '18:00',
    workedHours: '8.08',
    ot: '',
    late: '-',
    earlyLeave: '-',
    status: 'Present',
    remarks: '--',
  },
  {
    id: 3,
    name: 'Mike Wilson',
    code: 'EMP-003',
    department: 'Engineering',
    shift: 'Night Shift (22:00-06:00)',
    inTime: '22:30',
    outTime: '06:15',
    workedHours: '7.25h',
    ot: '+0.25h',
    late: '30min',
    earlyLeave: '-',
    status: 'Late',
    remarks: 'Personal emergency',
  },
];

/* ===========================
   Component
=========================== */
const DailyAttendanceSummary = () => {
  const [action, setAction] = useState<'view' | 'adjust' | null>(null);

  // const [value, setValue] = useState<any>(null);

  /* ---- Summary Cards ---- */
  const cardData = [
    {
      id: 1,
      icon: <GoPeople className="h-5 w-5" />,
      label: 'Total Employees',
      value: '4',
      bgGradient: 'bg-gradient-to-r from-[#EFF6FF] to-[#DBEAFE]',
      labelClass: 'text-[#155DFC]',
      valueClass: 'text-[#193CB8]',
      iconBg: 'bg-[#2B7FFF]',
      borderClass: 'border border-[#BEDBFF]',
    },
    {
      id: 2,
      icon: <IoPersonRemoveOutline className="h-5 w-5" />,
      label: 'Absent',
      value: '1',
      bgGradient: 'bg-gradient-to-r from-[#FEF2F2]  to-[#FFE2E2] ',
      labelClass: 'text-[#FB2C36]',
      valueClass: 'text-[#9F0712]',
      iconBg: 'bg-[#FB2C36]',
      borderClass: 'border border-[#FFC9C9]',
    },
    {
      id: 3,
      icon: <IoTimeOutline className="h-5 w-5" />,
      label: 'Late',
      value: '2',
      bgGradient: 'bg-gradient-to-r from-[#FEF9C2 ] to-[#FEFCE8]',
      labelClass: 'text-[#D08700]',
      valueClass: 'text-[#894B00]',
      iconBg: 'bg-[#F0B100]',
      borderClass: 'border border-[#FFF085]',
    },
    {
      id: 4,
      icon: <IoReload className="h-5 w-5" />,
      label: 'Absent',
      value: '1',
      bgGradient: 'bg-gradient-to-r from-[#F0FDF4 ] to-[#DCFCE7]',
      labelClass: 'text-[#00A63E]',
      valueClass: 'text-[#016630]',
      iconBg: 'bg-[#00C950]',
      borderClass: 'border border-[#B9F8CF]',
    },
    {
      id: 5,
      icon: <TbStopwatch className="h-5 w-5" />,
      label: 'OT Hours',
      value: '0.8',
      bgGradient: 'bg-gradient-to-r from-[#FAF5FF] to-[#F3E8FF]',
      labelClass: 'text-[#9810FA]',
      valueClass: 'text-[#9810FA]',
      iconBg: 'bg-[#AD46FF]',
      borderClass: 'border border-[#E9D4FF]',
    },
  ];

  /* ---- Table Columns ---- */
  const columns: ColumnDef<any, any>[] = useMemo(
    () => [
      {
        header: 'Employee',
        cell: ({ row }) => (
          <div className="flex flex-col">
            <span className="font-medium text-gray-800">
              {row.original.name}
            </span>
            <span className="text-xs text-gray-500">
              {row.original.code} • {row.original.department}
            </span>
            <span className="text-xs text-gray-400">{row.original.shift}</span>
          </div>
        ),
      },
      {
        header: 'In/Out Time',
        cell: ({ row }) => (
          <div className="text-sm">
            <p>
              <span className="text-green-600 font-medium">In:</span>{' '}
              <span className="text-[#0f172a] font-semibold">
                {row.original.inTime}
              </span>
            </p>
            <p>
              <span className="text-red-600 font-medium">Out:</span>{' '}
              <span className="text-[#0f172a] font-semibold">
                {row.original.outTime}
              </span>
            </p>
          </div>
        ),
      },
      {
        header: 'Worked Hours',
        cell: ({ row }) => (
          <div className="flex items-center gap-2">
            <span className="bg-blue-50 text-blue-700 px-2 py-0.5 rounded-md text-xs font-semibold">
              {row.original.workedHours}
            </span>
            {row.original.ot && (
              <span className="bg-purple-50 text-purple-700 px-2 py-0.5 rounded-md text-xs font-semibold">
                {row.original.ot}
              </span>
            )}
          </div>
        ),
      },
      {
        header: 'Late',
        cell: ({ row }) =>
          row.original.late !== '-' ? (
            <span className="bg-red-50 text-red-600 px-2 py-0.5 rounded-md text-xs font-semibold">
              {row.original.late}
            </span>
          ) : (
            <span className="text-gray-400 text-xs">-</span>
          ),
      },
      {
        header: 'Status',
        cell: ({ row }) => {
          const status = row.original.status;
          const colors: Record<string, string> = {
            Present: 'bg-green-50 text-green-700',
            Late: 'bg-yellow-50 text-yellow-700',
            Absent: 'bg-red-50 text-red-700',
          };
          return (
            <span
              className={`${colors[status] || 'bg-gray-50 text-gray-700'} px-2 py-0.5 rounded-md text-xs font-medium`}
            >
              {status}
            </span>
          );
        },
      },
      {
        header: 'Remarks',
        cell: ({ row }) => (
          <span className="text-sm text-gray-600">{row.original.remarks}</span>
        ),
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => (
          <div className="flex items-center gap-2">
            <RowActionDropdown
              data={[
                {
                  label: 'Edit',
                  icon: <BsThreeDots size={14} />,
                  action: () => setAction('adjust'),
                },
                {
                  label: 'Delete',
                  icon: <BsThreeDots size={14} />,
                  action: () =>   console.log('Deleted', row.original.name),
                },
                {
                  label: 'view',
                  icon: <CiRead size={14} />,
                  action: () => setAction('view'),
                },
              ]}
            >
              <BsThreeDots />
            </RowActionDropdown>
          </div>
        ),
      },
    ],
    []
  );

  return (
    <div className="px-5 py-6 space-y-8">
      {/* ===== Header ===== */}
      <div className="grid grid-cols-1 md:grid-cols-2 items-center">
        <div>
          <Breadcrumbs />
          <h1 className="text-3xl font-bold text-[#1D293D] mt-3">
            Daily Attendance Summary
          </h1>
          <p className="text-base text-[#45556C]">
            Monitor and manage daily attendance records
          </p>
        </div>

        <div className="flex gap-3 justify-start md:justify-end items-center mt-5 md:mt-0">
          <Button
            leftSection={<FiRefreshCcw size={18} color="#1E293B" />}
            variant="default"
            color="#94A3B833"
            radius="md"
            className="font-medium"
            // onClick={() => setAction('create')}
          >
            Refresh
          </Button>
          <Button
            leftSection={<FiDownload size={18} color="#1E293B" />}
            variant="default"
            color="#94A3B833"
            radius="md"
            className="font-medium"
            // onClick={() => setAction('create')}
          >
            Export
          </Button>
        </div>
      </div>

      {/* ===== Summary Cards ===== */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-5">
        {cardData.map(x => (
          <WorkCard
            key={x.id}
            icon={x.icon}
            label={x.label}
            value={x.value}
            bgGradient={x.bgGradient}
            labelClass={x.labelClass}
            valueClass={x.valueClass}
            iconBg={x.iconBg}
            borderClass={x.borderClass}
          />
        ))}
      </div>

      {/* ===== Filter Section ===== */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        <div className="flex items-center gap-3 pb-3">
          <FiFilter className="text-blue-600" />
          <span className="text-base font-semibold text-[#1E293B]">
            Filters
          </span>
        </div>
        <div className="py-3 grid grid-cols-1 md:grid-cols-5 gap-3">
          <DateInput
            size="md"
            radius="md"
            label="Date"
            placeholder="Select date"
          />
          <TextInput
            size="md"
            radius="md"
            leftSection={<CiSearch size={18} />}
            label="Search"
            placeholder="Search employees..."
          />
          <Select
            size="md"
            radius="md"
            label="Shift"
            data={['Morning', 'Afternoon', 'Night']}
          />
          <Select
            size="md"
            radius="md"
            label="Status"
            data={['Present', 'Late', 'Absent']}
          />
          <Select
            size="md"
            radius="md"
            label="Department"
            data={['Engineering', 'HR', 'Sales']}
          />
        </div>
      </div>

      {/* ===== Table ===== */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        <div className="flex flex-col gap-2 pb-3">
          <div className="flex items-center gap-3">
            <GoPersonAdd size={18} className="text-blue-600" />
            <span className="text-base font-semibold text-[#1E293B]">
              Attendance Records ({attendanceData.length})
            </span>
          </div>
          <span className="text-sm text-[#64748B]">
            Daily attendance summary with inline metrics
          </span>
        </div>

        <DataTable<any>
          data={attendanceData}
          columns={columns}
          selectFilters={[]}
          hideExport
          hideColumnVisibility
          hideSearch
          hidePagination
        />
      </div>

      {/* ===== Reusable Enhanced Modal ===== */}
      <Modal
        opened={action !== null}
        onClose={() => setAction(null)}
        withCloseButton
        centered
        radius="md"
        size="lg"
        overlayProps={{ backgroundOpacity: 0.3, blur: 3 }}
        title={
          <div className="">
            <div className="flex items-center gap-3">
              {action === 'adjust' ? (
                <FiEdit size={20} className="text-blue-600" />
              ) : (
                <MdOutlineRemoveRedEye size={20} className="text-blue-600" />
              )}
              <h2 className="text-lg font-semibold text-[#1E293B]">
                {action === 'adjust'
                  ? 'Manual Adjustment'
                  : 'Attendance Details'}
              </h2>
            </div>
            <p className="text-sm text-gray-500 py-2">
              {action === 'adjust'
                ? 'Adjust attendance record for John Smith'
                : 'Complete attendance summary for John Smith'}
            </p>
          </div>
        }
      >
        {action === 'view' ? (
          <div className="space-y-5 text-sm">
            {/* Employee Info */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-y-2">
              <div>
                <p className="text-gray-500">Employee</p>
                <p className="font-semibold text-gray-800 leading-tight">
                  John Smith
                </p>
                <p className="text-xs text-gray-500 font-medium">EMP-001</p>
              </div>
              <div>
                <p className="text-gray-500">Department</p>
                <p className="font-semibold text-gray-800 leading-tight">
                  Engineering
                </p>
              </div>
            </div>

            {/* Shift */}
            <div>
              <p className="text-gray-500">Shift</p>
              <p className="font-semibold text-gray-800">
                Morning Shift (09:00–18:00)
              </p>
            </div>

            {/* In / Out Time */}
            <div className="grid grid-cols-2 gap-3">
              <div>
                <p className="text-gray-500">In Time</p>
                <p className="font-semibold text-gray-900 text-lg">09:15</p>
              </div>
              <div>
                <p className="text-gray-500">Out Time</p>
                <p className="font-semibold text-gray-900 text-lg">18:30</p>
              </div>
            </div>

            {/* Hours + Late + Early */}
            <div className="grid grid-cols-3 gap-3">
              <div>
                <p className="text-gray-500">Worked Hours</p>
                <p className="font-semibold text-[#155DFC]">8.75h</p>
              </div>
              <div>
                <p className="text-gray-500">Late Minutes</p>
                <p className="font-semibold text-[#E11D48]">15</p>
              </div>
              <div>
                <p className="text-gray-500">Early Leave</p>
                <p className="font-semibold text-[#0F172A]">0</p>
              </div>
            </div>

            {/* Status */}
            <div>
              <p className="text-gray-500">Status</p>
              <Badge
                color="yellow"
                variant="light"
                size="sm"
                radius="sm"
                className="mt-1"
              >
                Late
              </Badge>
            </div>

            {/* Remarks */}
            <div>
              <p className="text-gray-500 mb-1">Remarks</p>
              <div className="bg-[#F1F5F9] border border-[#E2E8F0] text-gray-700 rounded-md px-3 py-2 text-sm">
                Traffic delay
              </div>
            </div>

            {/* Buttons */}
            <div className="flex justify-end pt-2">
              <Button
                color="orange"
                radius="md"
                className="px-6"
                onClick={() => setAction(null)}
              >
                Close
              </Button>
            </div>
          </div>
        ) : (
          /* ================= VIEW MODE ================= */

          /* ================= ADJUST MODE ================= */
          <div className="space-y-5">
            {/* In/Out Time */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <DateInput
                label="In Time"
                placeholder="Enter In Time"
                radius="md"
                size="md"
              />
              <DateInput
                label="Out Time"
                size="md"
                placeholder="Enter Out Time"
                radius="md"
              />
            </div>

            {/* Status */}
            <Select
              label="Status"
              size="md"
              placeholder="Select status"
              data={['Present', 'Late', 'Absent', 'Half Day']}
              radius="md"
            />

            {/* Remarks */}
            <TextInput
              size="md"
              label="Remarks (Required)"
              placeholder="Enter reason for adjustment..."
              radius="md"
            />

            {/* Timeline Preview */}
            <div className="bg-[#F1F5F9] border border-[rgba(148,163,184,0.20)] rounded-xl p-5 space-y-2">
              <div className="flex items-center gap-2 text-green-700 font-medium">
                <CiTimer size={18} /> Timeline Preview
              </div>

              {/* Before */}
              <div className="bg-white border border-gray-200 rounded-lg p-4">
                <p className="text-gray-700 font-semibold mb-2">Before</p>
                <div className="grid grid-cols-3 text-sm text-gray-600">
                  <div>
                    <p>In Time</p>
                    <p className="font-semibold">09:15</p>
                  </div>
                  <div>
                    <p>Out Time</p>
                    <p className="font-semibold">18:30</p>
                  </div>
                  <div>
                    <p>Worked Hours</p>
                    <p className="font-semibold">8.75h</p>
                  </div>
                </div>
                <div className="mt-2">
                  <Badge color="yellow" variant="light" radius="sm" size="sm">
                    Late
                  </Badge>
                </div>
              </div>

              <div className="flex justify-center text-gray-400 text-xl">→</div>

              {/* After */}
              <div className="bg-white border border-blue-200 rounded-lg p-4">
                <p className="text-gray-700 font-semibold mb-2">After</p>
                <div className="grid grid-cols-3 text-sm text-gray-600">
                  <div>
                    <p>In Time</p>
                    <p className="font-semibold">09:15</p>
                  </div>
                  <div>
                    <p>Out Time</p>
                    <p className="font-semibold">18:30</p>
                  </div>
                  <div>
                    <p>Worked Hours</p>
                    <p className="font-semibold text-blue-700">9.25h</p>
                  </div>
                </div>
                <div className="mt-2">
                  <Badge color="yellow" variant="light" radius="sm" size="sm">
                    Late
                  </Badge>
                </div>
              </div>

              <div className="text-[16px] text-green-700 bg-green-50 border border-green-100 rounded-md px-3 py-2">
                Change: Hours will change from 8.75h to 9.25h
              </div>
            </div>

            {/* Buttons */}
            <div className="flex justify-end gap-3 pt-4">
              <Button
                variant="default"
                radius="md"
                onClick={() => setAction(null)}
              >
                Cancel
              </Button>
              <Button color="orange" radius="md">
                Save Changes
              </Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default DailyAttendanceSummary;
