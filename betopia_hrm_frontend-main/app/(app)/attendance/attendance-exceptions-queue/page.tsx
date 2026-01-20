'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import WorkCard from '@/components/common/WorkCard';
import { Badge, Button, Modal, Select, TextInput } from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { useMemo, useState } from 'react';
import { AiOutlineCheckCircle, AiOutlineEye } from 'react-icons/ai';
import { BsThreeDots } from 'react-icons/bs';
import { CiClock2, CiSearch } from 'react-icons/ci';
import { FiDownload, FiEdit, FiFilter, FiRefreshCcw } from 'react-icons/fi';
import { GoAlert, GoPeople } from 'react-icons/go';
import { IoIosTrendingUp } from 'react-icons/io';
import { IoReload } from 'react-icons/io5';

/* ===========================
   Sample Data
=========================== */
const attendanceData = [
  {
    id: 'EXC-001',
    employee: 'John Smith',
    code: 'E001',
    department: 'Engineering',
    date: '2024-01-15',
    day: 'Monday',
    inTime: '09:15',
    outTime: '--',
    expectedIn: '09:00',
    expectedOut: '18:00',
    exceptionType: 'Missing Punch Out',
    severity: 'High',
    priority: 1,
    status: 'Pending',
    assignedTo: 'Unassigned',
    remarks:
      'Employee checked in at 09:15 but no checkout record found. Last recorded activity at 18:30.',
  },
  {
    id: 'EXC-002',
    employee: 'Sarah Johnson',
    code: 'E002',
    department: 'Marketing',
    date: '2024-01-15',
    day: 'Monday',
    inTime: '09:45',
    outTime: '18:00',
    expectedIn: '09:00',
    expectedOut: '18:00',
    exceptionType: 'Late Arrival',
    severity: 'Medium',
    priority: 2,
    status: 'Under Review',
    assignedTo: 'HR Manager',
    remarks: 'Auto-resolvable exception.',
  },
  {
    id: 'EXC-003',
    employee: 'Mike Wilson',
    code: 'E003',
    department: 'Operations',
    date: '2024-01-14',
    day: 'Sunday',
    inTime: '08:00',
    outTime: '20:00',
    expectedIn: '09:00',
    expectedOut: '18:00',
    exceptionType: 'Overtime Violation',
    severity: 'High',
    priority: 1,
    status: 'Escalated',
    assignedTo: 'Operations Manager',
    remarks: 'Worked extended hours beyond permitted schedule.',
  },
];

/* ===========================
   Component
=========================== */
const AttendanceExceptionsQueue = () => {
  const [action, setAction] = useState<'view' | 'resolve' | null>(null);
  const [selected, setSelected] = useState<any>(null);

  /* ---- Summary Cards ---- */
  const cardData = [
    {
      id: 1,
      icon: <GoPeople className="h-5 w-5" />,
      label: 'Total Exceptions',
      value: '5',
      bgGradient: 'bg-gradient-to-r from-[#FFF7ED] to-[#FFEDD4]',
      labelClass: 'text-[#F54900]',
      valueClass: 'text-[#9F2D00]',
      iconBg: 'bg-[#FF6900]',
      borderClass: 'border border-[#BEDBFF]',
    },
    {
      id: 2,
      icon: <CiClock2 className="h-5 w-5" />,
      label: 'Pending',
      value: '2',
      bgGradient: 'bg-gradient-to-r from-[#FEF2F2]  to-[#FFE2E2]',
      labelClass: 'text-[#E7000B]',
      valueClass: 'text-[#9F0712]',
      iconBg: 'bg-[#FB2C36]',
      borderClass: 'border border-[#FFC9C9]',
    },
    {
      id: 3,
      icon: <IoIosTrendingUp className="h-5 w-5" />,
      label: 'High Severity',
      value: '2',
      bgGradient: 'bg-gradient-to-r from-[#FEF9C2] to-[#FEFCE8]',
      labelClass: 'text-[#D08700]',
      valueClass: 'text-[#894B00]',
      iconBg: 'bg-[#F0B100]',
      borderClass: 'border border-[#FFF085]',
    },
    {
      id: 4,
      icon: <IoReload className="h-5 w-5" />,
      label: 'Auto-Resolvable',
      value: '0',
      bgGradient: 'bg-gradient-to-r from-[#EFF6FF] to-[#DBEAFE]',
      labelClass: 'text-[#155DFC]',
      valueClass: 'text-[#193CB8]',
      iconBg: 'bg-[#2B7FFF]',
      borderClass: 'border border-[#BEDBFF]',
    },
  ];

  /* ====== Table Columns ====== */
  const columns = useMemo(
    () => [
      {
        header: 'Employee',
        cell: ({ row }: any) => (
          <div>
            <p className="font-medium text-gray-800">{row.original.employee}</p>
            <p className="text-xs text-gray-500">
              {row.original.code} • {row.original.department}
            </p>
          </div>
        ),
      },
      {
        header: 'Date & Exception',
        cell: ({ row }: any) => (
          <div className="text-sm text-gray-700">
            <p>{row.original.date}</p>
            <p className="text-xs text-gray-400">{row.original.day}</p>
            <Badge color="orange" variant="light" radius="sm" className="mt-1">
              {row.original.exceptionType}
            </Badge>
          </div>
        ),
      },
      {
        header: 'Time Details',
        cell: ({ row }: any) => (
          <div className="text-sm">
            <p>
              In: <span className="font-semibold">{row.original.inTime}</span>{' '}
              <span className="text-gray-400 text-xs">
                (exp: {row.original.expectedIn})
              </span>
            </p>
            <p>
              Out: <span className="font-semibold">{row.original.outTime}</span>{' '}
              <span className="text-gray-400 text-xs">
                (exp: {row.original.expectedOut})
              </span>
            </p>
          </div>
        ),
      },
      {
        header: 'Severity',
        cell: ({ row }: any) => (
          <div className="text-sm">
            <Badge
              color={
                row.original.severity === 'High'
                  ? 'red'
                  : row.original.severity === 'Medium'
                    ? 'yellow'
                    : 'blue'
              }
              variant="light"
              radius="sm"
            >
              {row.original.severity}
            </Badge>
            <p className="text-xs text-gray-500 mt-1">
              Priority: {row.original.priority}
            </p>
          </div>
        ),
      },
      {
        header: 'Status',
        cell: ({ row }: any) => {
          const statusColors: Record<string, string> = {
            Pending: 'bg-orange-50 text-orange-700',
            'Under Review': 'bg-blue-50 text-blue-700',
            Escalated: 'bg-red-50 text-red-700',
            Resolved: 'bg-green-50 text-green-700',
          };
          return (
            <Badge
              color="gray"
              variant="light"
              radius="sm"
              className={`${statusColors[row.original.status]} capitalize`}
            >
              {row.original.status}
            </Badge>
          );
        },
      },
      {
        header: 'Assigned To',
        cell: ({ row }: any) => (
          <div className="text-sm text-gray-700">
            {row.original.assignedTo}
            {row.original.assignedTo !== 'Unassigned' && (
              <p className="text-xs text-gray-400">1/15/2024</p>
            )}
          </div>
        ),
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }: any) => (
          <div className="flex gap-2">
            {/* <button
              className="p-2 text-gray-500 hover:text-blue-600"
              onClick={() => {
                setSelected(row.original);
                setAction('view');
              }}
            >
              <AiOutlineEye size={18} />
            </button>
            <button
              className="p-2 text-gray-500 hover:text-orange-600"
              onClick={() => {
                setSelected(row.original);
                setAction('resolve');
              }}
            >
              <FiEdit size={16} />
            </button> */}

            {/* ✅ Fixed RowActionDropdown */}
            <RowActionDropdown
              data={[
                {
                  label: 'View',
                  icon: <AiOutlineEye size={14} />,
                  action: () => {
                    setSelected(row.original);
                    setAction('view');
                  },
                },
                {
                  label: 'Resolve',
                  icon: <FiEdit size={16} />,
                  action: () => {
                    setSelected(row.original);
                    setAction('resolve');
                  },
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
            Attendance Exceptions Queue
          </h1>
          <p className="text-base text-[#45556C]">
            Review and resolve attendance exceptions with intelligent
            suggestions
          </p>
        </div>

        <div className="flex gap-3 justify-start md:justify-end items-center mt-5 md:mt-0">
          <Button
            leftSection={<FiRefreshCcw size={18} color="#1E293B" />}
            variant="default"
            radius="md"
          >
            Refresh
          </Button>
          <Button
            leftSection={<FiDownload size={18} color="#1E293B" />}
            variant="default"
            radius="md"
          >
            Export
          </Button>
        </div>
      </div>

      {/* ===== Summary Cards ===== */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
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
            <GoAlert size={18} className="text-blue-600" />
            <span className="text-base font-semibold text-[#1E293B]">
              Exceptions Queue ({attendanceData.length})
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

      {/* ================= VIEW MODAL ================= */}
      <Modal
        opened={action === 'view'}
        onClose={() => setAction(null)}
        centered
        radius="md"
        size="lg"
        withCloseButton
        overlayProps={{ backgroundOpacity: 0.3, blur: 3 }}
        title={
          <div className="flex items-center gap-3">
            <AiOutlineEye className="text-blue-600" size={20} />
            <div>
              <h2 className="text-lg font-semibold text-[#1E293B]">
                Exception Details - {selected?.id}
              </h2>
              <p className="text-sm text-gray-500">
                Detailed view of the attendance exception
              </p>
            </div>
          </div>
        }
      >
        {selected && (
          <div className="space-y-5 text-sm">
            {/* EMPLOYEE & DATE */}
            <div className="grid grid-cols-1 md:grid-cols-2">
              <div>
                <p className="text-gray-500">Employee</p>
                <p className="font-semibold text-gray-800">
                  {selected.employee}
                </p>
                <p className="text-xs text-gray-500">
                  {selected.code} • {selected.department}
                </p>
              </div>
              <div>
                <p className="text-gray-500">Date</p>
                <p className="font-semibold text-gray-800">
                  {selected.date} ({selected.day})
                </p>
              </div>
            </div>

            <hr />

            {/* Exception Type */}
            <div>
              <p className="text-gray-500 mb-1">Exception Type</p>
              <div className="flex gap-2 flex-wrap">
                <Badge color="orange" variant="light">
                  {selected.exceptionType}
                </Badge>
                <Badge color="red" variant="light">
                  {selected.severity} Severity
                </Badge>
              </div>
            </div>

            {/* Description */}
            <div>
              <p className="text-gray-500 mb-1">Description</p>
              <p className="text-gray-800">{selected.remarks}</p>
            </div>

            {/* Suggested Action */}
            <div>
              <p className="text-gray-500 mb-1">Suggested Action</p>
              <p className="text-blue-700 underline cursor-pointer">
                Contact employee for manual checkout time entry
              </p>
            </div>

            <hr />

            {/* Expected vs Actual */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div>
                <p className="font-semibold text-gray-700 mb-1">
                  Expected Times
                </p>
                <p>In: {selected.expectedIn}</p>
                <p>Out: {selected.expectedOut}</p>
              </div>
              <div>
                <p className="font-semibold text-gray-700 mb-1">Actual Times</p>
                <p>In: {selected.inTime}</p>
                <p>Out: {selected.outTime}</p>
              </div>
            </div>

            <div className="flex justify-end gap-3 pt-2">
              <Button variant="default" onClick={() => setAction(null)}>
                Close
              </Button>
              <Button color="orange" onClick={() => setAction('resolve')}>
                Resolve Exception
              </Button>
            </div>
          </div>
        )}
      </Modal>

      {/* ================= RESOLVE MODAL ================= */}
      <Modal
        opened={action === 'resolve'}
        onClose={() => setAction(null)}
        centered
        radius="md"
        size="lg"
        withCloseButton
        overlayProps={{ backgroundOpacity: 0.3, blur: 3 }}
        title={
          <div className="flex items-center gap-3">
            <AiOutlineCheckCircle className="text-green-600" size={20} />
            <div>
              <h2 className="text-lg font-semibold text-[#1E293B]">
                Resolve Exception - {selected?.id}
              </h2>
              <p className="text-sm text-gray-500">
                Select an action to resolve this attendance exception
              </p>
            </div>
          </div>
        }
      >
        <div className="space-y-5">
          <Select
            label="Resolution Action *"
            placeholder="Select resolution action"
            data={[
              'Manual Punch Entry',
              'Ignore Exception',
              'Reassign to Manager',
              'Mark as Resolved',
            ]}
            radius="md"
            size="md"
          />

          <TextInput
            label="Reason *"
            placeholder="Brief reason for this resolution"
            radius="md"
            size="md"
          />

          <TextInput
            label="Additional Notes"
            placeholder="Any additional notes or comments"
            radius="md"
            size="md"
          />

          <div className="flex justify-end gap-3 pt-4">
            <Button
              variant="default"
              radius="md"
              onClick={() => setAction(null)}
            >
              Cancel
            </Button>
            <Button color="orange" radius="md">
              Resolve Exception
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default AttendanceExceptionsQueue;
