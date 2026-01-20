'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import WorkCard from '@/components/common/WorkCard';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import {
  Badge,
  Button,
  Modal,
  Select,
  Switch,
  Text,
  TextInput,
} from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { modals } from '@mantine/modals';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { CiSearch, CiTimer } from 'react-icons/ci';
import { FaRegCalendar } from 'react-icons/fa6';
import { FiFilter } from 'react-icons/fi';
import { GoPeople } from 'react-icons/go';
import { IoMdCheckmarkCircleOutline } from 'react-icons/io';
import { IoReload, IoTimeOutline } from 'react-icons/io5';
import { MdOutlineHistory, MdOutlinePersonAddAlt } from 'react-icons/md';

/* ===========================
   Sample Data
=========================== */
const employees = [
  {
    id: 1,
    name: 'John Smith',
    code: 'EMP-001',
    team: 'Engineering',
    shiftCode: 'MS',
    shiftName: 'Morning Shift',
    shiftTime: '09:00 - 18:00',
    duration: '2024-01-01 to 2024-12-31',
    status: 'Active',
    assignedBy: 'Admin',
    assignedDate: '2023-12-20',
    remarks: 'Regular assignment',
  },
  {
    id: 2,
    name: 'Sarah Johnson',
    code: 'EMP-002',
    team: 'Marketing',
    shiftCode: 'FH',
    shiftName: 'Flexible Hours',
    shiftTime: '10:00 - 19:00',
    duration: '2024-01-15 to 2024-06-30',
    status: 'Active',
    assignedBy: 'HR Manager',
    assignedDate: '2024-01-10',
    remarks: 'Temporary flexible arrangement',
  },
  {
    id: 3,
    name: 'Mike Wilson',
    code: 'EMP-003',
    team: 'Operations',
    shiftCode: 'NS',
    shiftName: 'Night Shift',
    shiftTime: '22:00 - 06:00',
    duration: '2024-02-01 to 2024-04-30',
    status: 'Upcoming',
    assignedBy: 'Operations Manager',
    assignedDate: '2024-01-20',
    remarks: 'Coverage for project deadline',
  },
];

/* ===========================
   Table Component
=========================== */
const EmployeeShiftAssignment = () => {
  const [value, setValue] = useState<string | null>(null);
  const [action, setAction] = useState<null | 'create' | 'update' | 'history'>(
    null
  );
  // const [item, setItem] = useState<any | null>(null);

  /* ---- Summary Cards ---- */
  const cardData = [
    {
      id: 1,
      icon: <GoPeople className="h-5 w-5" />,
      label: 'Total Assignments',
      value: '3',
      bgGradient: 'bg-gradient-to-r from-[#EFF6FF] to-[#DBEAFE]',
      labelClass: 'text-[#155DFC]',
      valueClass: 'text-[#193CB8]',
      iconBg: 'bg-[#2B7FFF]',
      borderClass: 'border border-[#BEDBFF]',
    },
    {
      id: 2,
      icon: <IoMdCheckmarkCircleOutline className="h-5 w-5" />,
      label: 'Active',
      value: '2',
      bgGradient: 'bg-gradient-to-r from-[#F0FDF4] to-[#DCFCE7]',
      labelClass: 'text-[#00A63E]',
      valueClass: 'text-[#016630]',
      iconBg: 'bg-[#00C950]',
      borderClass: 'border border-[#B9F8CF]',
    },
    {
      id: 3,
      icon: <IoTimeOutline className="h-5 w-5" />,
      label: 'Upcoming',
      value: '1',
      bgGradient: 'bg-gradient-to-r from-[#FFF7ED] to-[#FFEDD4]',
      labelClass: 'text-[#F54900]',
      valueClass: 'text-[#9F2D00]',
      iconBg: 'bg-[#FF6900]',
      borderClass: 'border border-[#FFD6A7]',
    },
    {
      id: 4,
      icon: <IoReload className="h-5 w-5" />,
      label: 'Available Shifts',
      value: '4',
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
            <span className="text-xs text-gray-500">{row.original.code}</span>
            <span className="text-xs text-gray-400">{row.original.team}</span>
          </div>
        ),
      },
      {
        header: 'Shift Details',
        cell: ({ row }) => {
          const code = row.original.shiftCode;
          const colorMap: Record<string, string> = {
            MS: 'green',
            FH: 'orange',
            NS: 'violet',
          };
          return (
            <div className="flex flex-col">
              <div className="flex items-center gap-2">
                <Badge
                  color={colorMap[code] || 'gray'}
                  variant="light"
                  size="sm"
                  radius="sm"
                >
                  {code}
                </Badge>
                <span className="font-medium text-gray-800">
                  {row.original.shiftName}
                </span>
              </div>
              <span className="text-xs text-gray-500">
                {row.original.shiftTime}
              </span>
            </div>
          );
        },
      },
      {
        header: 'Duration',
        accessorKey: 'duration',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.original.duration}</span>
        ),
      },
      {
        header: 'Status',
        accessorKey: 'status',
        cell: ({ row }) => <StatusField status={row.original.status} />,
      },
      {
        header: 'Assigned By',
        cell: ({ row }) => (
          <div className="flex flex-col text-sm text-gray-700">
            <span className="font-medium text-gray-800">
              {row.original.assignedBy}
            </span>
            <span className="text-xs text-gray-500">
              {row.original.assignedDate}
            </span>
          </div>
        ),
      },
      {
        header: 'Remarks',
        accessorKey: 'remarks',
        cell: ({ row }) => (
          <span className="text-sm text-gray-600">{row.original.remarks}</span>
        ),
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => (
          <div className="flex  justify-start items-center gap-2">
            <button
              className="p-2 text-gray-500 hover:text-blue-600"
              title="History"
              onClick={() => {
                setAction('history');
              }}
            >
              <MdOutlineHistory size={18} />
            </button>
            <RowActionDropdown
              data={[
                {
                  label: 'Edit',
                  icon: <PencilSquareIcon height={16} />,
                  action: () => {
                    setAction('update');
                    // setItem(row.original);
                  },
                },
                {
                  label: 'Delete',
                  icon: <TrashIcon height={16} />,
                  action: () => {
                    modals.openConfirmModal({
                      title: 'Delete confirmation',
                      centered: true,
                      children: (
                        <Text size="sm">
                          Are you sure you want to delete this record? This
                          action is permanent.
                        </Text>
                      ),
                      labels: { confirm: 'Delete', cancel: 'Cancel' },
                      confirmProps: { color: 'red' },
                      onConfirm: () =>
                        console.log('Deleted:', row.original.name),
                    });
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
      {/* ===== Header Section ===== */}
      <div className="grid grid-cols-1 md:grid-cols-2 items-center">
        <div>
          <Breadcrumbs />
          <h1 className="text-3xl font-bold text-[#1D293D] mt-3">
            Employee Shift Assignment
          </h1>
          <h6 className="text-base text-[#45556C] mt-1">
            Assign and manage employee shift schedules with validation
          </h6>
        </div>

        {/* Action Button */}
        <div className="flex justify-start md:justify-end items-center mt-5 md:mt-0">
          <Button
            leftSection={<MdOutlinePersonAddAlt size={18} color="#FFFFFF" />}
            variant="filled"
            color="orange"
            radius="md"
            className="font-medium"
            onClick={() => {
              setAction('create');
              // setItem(null);
            }}
          >
            Assign Shift
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

        <div className="py-3 grid grid-cols-1 md:grid-cols-3 gap-3">
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
            label="Department"
            placeholder="Select department"
            data={['Engineering', 'Design', 'HR', 'Marketing']}
          />
          <Select
            size="md"
            radius="md"
            label="Shift Type"
            placeholder="Select shift"
            data={['Morning', 'Afternoon', 'Night']}
          />
        </div>
      </div>

      {/* ===== Table Section ===== */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        <div className="flex items-center gap-3 pb-3">
          <FaRegCalendar className="text-blue-600" />
          <span className="text-base font-semibold text-[#1E293B]">
            Current Assignments <span>({employees.length})</span>
          </span>
        </div>

        <DataTable<any>
          data={employees}
          columns={columns}
          selectFilters={[]}
          hideExport
          hideColumnVisibility
          hideSearch
          hidePagination
        />
      </div>

      {/* ===== Modal Section ===== */}
      <Modal
        title={
          <>
            <div className="flex items-center gap-3">
              {action === 'create' ? (
                <MdOutlinePersonAddAlt className="text-blue-600" size={20} />
              ) : action === 'update' ? (
                <MdOutlinePersonAddAlt className="text-blue-600" size={20} />
              ) : (
                <CiTimer className="text-blue-600" size={20} />
              )}

              <h2 className="text-lg font-semibold text-[#1E293B]">
                {action === 'create'
                  ? 'Assign Shift'
                  : action === 'update'
                    ? 'Edit Shift Assignment'
                    : 'Assignment History - John Smith'}
              </h2>
            </div>
            <div>
              <p className="text-sm text-gray-500 pt-2">
                {action === 'create'
                  ? 'Assign a new shift to an employee'
                  : action === 'update'
                    ? 'Update the shift assignment details'
                    : 'Previous shift assignments for this employee'}
              </p>
            </div>
          </>
        }
        opened={action !== null}
        onClose={() => {
          setAction(null);
          // setItem(null);
        }}
        withCloseButton
        centered
        size="lg"
        overlayProps={{ backgroundOpacity: 0.3, blur: 3 }}
        radius="md"
      >
        {action === 'history' ? (
          <div>
            <div className="bg-white rounded-2xl ">
              <div className="space-y-6">
                {/* Card */}
                <div className="bg-[#F8FAFC] border border-[rgba(148,163,184,0.20)] rounded-xl p-5 flex flex-col md:flex-row justify-between gap-4">
                  {/* ===== Left Section ===== */}
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                      {/* Shift Code */}
                      <Badge color="green" variant="light" radius="sm">
                        MS
                      </Badge>
                      <span className="font-semibold text-gray-800 text-lg">
                        Morning Shift
                      </span>
                      <Badge color="green" variant="light" radius="sm">
                        Active
                      </Badge>
                    </div>

                    <div className="text-sm text-gray-700 space-y-1">
                      <p>
                        <span className="font-medium text-gray-600">
                          Duration:
                        </span>
                        2024-01-01 to 2024-12-31
                      </p>
                      <p>
                        <span className="font-medium text-gray-600">
                          Remarks:
                        </span>
                        Regular assignment
                      </p>
                    </div>
                  </div>

                  {/* ===== Right Section ===== */}
                  <div className="flex flex-col justify-between items-end text-sm text-gray-700">
                    <div className="text-gray-500 font-medium text-sm mb-2">
                      ID:
                      <span className="text-[#334155] font-semibold">
                        SA-001
                      </span>
                    </div>
                    <p>
                      <span className="font-medium text-gray-600">
                        Assigned by:
                      </span>
                      Admin
                    </p>
                  </div>
                </div>

                {/* Close Button */}
                <div className="flex justify-end">
                  <Button
                    color="orange"
                    radius="md"
                    onClick={() => {
                      setAction(null);
                      // setItem(null);
                    }}
                  >
                    Close
                  </Button>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="space-y-5">
            {/* Employee + Pattern ID */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Select
                label="Employee "
                placeholder="Select employee"
                data={[
                  { value: '1', label: 'John Smith • Engineering' },
                  { value: '2', label: 'Sarah Johnson • Marketing' },
                  { value: '3', label: 'Mike Wilson • Operations' },
                ]}
                searchable
                radius="md"
                withAsterisk
              />
              <Select
                label="Pattern ID "
                placeholder="Select pattern"
                data={[
                  { value: 'AUTO', label: 'Auto Pattern' },
                  { value: 'FIXED', label: 'Fixed Pattern' },
                  { value: 'ROTATE', label: 'Rotation Pattern' },
                ]}
                radius="md"
                withAsterisk
              />
            </div>

            {/* Effective Dates */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <DateInput
                value={value}
                onChange={setValue}
                label="Effective From "
                placeholder="Select start date"
                radius="md"
                withAsterisk
              />
              <DateInput
                value={value}
                onChange={setValue}
                label="Effective To "
                placeholder="Select end date"
                radius="md"
                withAsterisk
              />
            </div>

            {/* Cycle Date */}

            <DateInput
              value={value}
              onChange={setValue}
              label="Date input"
              placeholder="Date input"
            />

            {/* Active Toggle */}
            <div className=" pt-2">
              <Switch defaultChecked label="I agree to sell my privacy" />
            </div>

            {/* Action Buttons */}
            <div className="flex justify-end gap-3 pt-4">
              <Button
                variant="default"
                radius="md"
                onClick={() => setAction(null)}
              >
                Cancel
              </Button>
              <Button
                color="orange"
                radius="md"
                leftSection={<MdOutlinePersonAddAlt size={18} />}
              >
                {action === 'create' ? 'Assign Shift' : 'Update Assignment'}
              </Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default EmployeeShiftAssignment;
