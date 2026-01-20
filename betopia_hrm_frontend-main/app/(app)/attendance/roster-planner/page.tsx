'use client';

import React from 'react';
import Breadcrumbs from '@/components/common/Breadcrumbs';
import { Button, Select, TextInput } from '@mantine/core';
import { CiSearch } from 'react-icons/ci';
import { FiEdit, FiFilter } from 'react-icons/fi';
import { GoGear, GoPeople } from 'react-icons/go';
import { MdOutlineFileDownload } from 'react-icons/md';
import WeeklyRosterReport, { Employee } from './WeeklyRosterReport'; // ✅ make sure path matches your file
import WorkCard from '@/components/common/WorkCard';
import { FaRegCalendarAlt } from 'react-icons/fa';
 

const Page = () => {
  /* ============================
       Demo Data (Dynamic Props)
  ============================ */
  const employees: Employee[] = [
    {
      id: '1',
      name: 'John Smith',
      code: 'E001',
      team: 'Backend Team',
      assignments: [
        { date: '2025-10-20', code: 'MS' },
        // { date: '2025-10-21', code: 'AS', draft: true },
        // { date: '2025-10-21', code: 'OFF' },
      ],
    },
    {
      id: '2',
      name: 'Sarah Johnson',
      code: 'E002',
      team: 'Frontend Team',
      assignments: [
        { date: '2025-10-20', code: 'WFH' },
        // { date: '2025-10-21', code: 'AS', draft: true },
      ],
    },
    {
      id: '3',
      name: 'John Smith',
      code: 'E001',
      team: 'Backend Team',
      assignments: [
        { date: '2025-10-20', code: 'MS' },
        // { date: '2025-10-21', code: 'OFF' },
      ],
    },
    {
      id: '4',
      name: 'Sarah Johnson',
      code: 'E002',
      team: 'Frontend Team',
      assignments: [
        { date: '2025-10-20', code: 'WFH' },
        // { date: '2025-10-21', code: 'AS', draft: true },
      ],
    },
    {
      id: '5',
      name: 'John Smith',
      code: 'E001',
      team: 'Backend Team',
      assignments: [
        { date: '2025-10-20', code: 'MS' },
        // { date: '2025-10-21', code: 'OFF' },
      ],
    },
    {
      id: '6',
      name: 'Sarah Johnson',
      code: 'E002',
      team: 'Frontend Team',
      assignments: [
        { date: '2025-10-20', code: 'WFH' },
        // { date: '2025-10-21', code: 'AS', draft: true },
      ],
    },
  ];

  const cardData = [
    {
      icon: <GoPeople  className="h-5 w-5" />,
      label: 'Total Employees',
      value: '5',
      bgGradient: 'bg-gradient-to-r from-[#EFF6FF]  to-[#DBEAFE] ',
      labelClass: 'text-[#155DFC]',
      valueClass: 'text-[#193CB8]',
      iconBg: 'bg-[#2B7FFF]',
      borderClass: 'border border-[#BEDBFF]',
    },
    {
      icon: <FaRegCalendarAlt   className="h-5 w-5" />,
      label: 'Assigned Shifts',
      value: '3',
      bgGradient: 'bg-gradient-to-r from-[#F0FDF4]  to-[#DCFCE7] ',
      labelClass: 'text-[#00A63E]',
      valueClass: 'text-[#016630]',
      iconBg: 'bg-[#00C950]',
      borderClass: 'border border-[#B9F8CF]',
    },
    {
      icon: <FiEdit   className="h-5 w-5" />,
      label: 'Draft Changes',
      value: '1',
      bgGradient: 'bg-gradient-to-r from-[#FFF7ED]  to-[#FFEDD4] ',
      labelClass: 'text-[#F54900]',
      valueClass: 'text-[#9F2D00]',
      iconBg: 'bg-[#FF6900]',
      borderClass: 'border border-[#FFD6A7]',
    },
    {
      icon: <GoPeople  className="h-5 w-5" />,
      label: 'Published',
      value: '1',
      bgGradient: 'bg-gradient-to-r from-[#FAF5FF]  to-[#F3E8FF] ',
      labelClass: 'text-[#9810FA]',
      valueClass: 'text-[#9810FA]',
      iconBg: 'bg-[#2B7FFF]',
      borderClass: 'border border-[#E9D4FF]',
    },
  ];

  return (
    <div className="px-4 sm:px-6 lg:px-10 py-6 sm:py-8 lg:py-10 space-y-6 sm:space-y-8">
      {/* ===== Header Section ===== */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 py-6 sm:py-10">
        <div>
          <Breadcrumbs />
          <h1 className="text-2xl sm:text-3xl font-bold text-[#1D293D] py-3">
            Weekly Roster Plan
          </h1>
          <h6 className="text-[#475569] text-sm">
            Plan and manage employee shift assignments with drag-and-drop
            interface
          </h6>
        </div>

        {/* Action Buttons */}
        <div className="flex flex-col sm:flex-row lg:justify-end items-stretch sm:items-center gap-3">
          <Button
            leftSection={<MdOutlineFileDownload size={16} color="#1E293B" />}
            variant="outline"
            radius="md"
            fullWidth
            className="sm:w-auto"
            styles={{
              root: { borderColor: '#CBD5E1' },
              label: { color: '#1E293B' },
            }}
          >
            Export CSV
          </Button>
          <Button
            leftSection={<MdOutlineFileDownload size={16} color="#1E293B" />}
            variant="outline"
            radius="md"
            fullWidth
            className="sm:w-auto"
            styles={{
              root: { borderColor: '#CBD5E1' },
              label: { color: '#1E293B' },
            }}
          >
            Copy Week
          </Button>
          <Button
            leftSection={<MdOutlineFileDownload size={16} color="#FFFFFF" />}
            variant="filled"
            color="orange"
            radius="md"
            fullWidth
            className="sm:w-auto"
          >
            Publish Roster
          </Button>
        </div>
      </div>

      {/* ===== Filter & Roster Tool ===== */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 sm:gap-10">
        {/* Left: Filters */}
        <div className="bg-white px-4 sm:px-5 py-4 sm:py-5 rounded-2xl shadow-md">
          <div className="flex items-center gap-3 pb-3">
            <FiFilter className="text-blue-600" />
            <span className="text-base text-[#1E293B] font-semibold">
              Filters
            </span>
          </div>

          <div className="py-3 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
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
              placeholder="Select"
              data={['Engineering', 'Design', 'HR', 'Marketing']}
            />
            <Select
              size="md"
              radius="md"
              label="Shift Type"
              placeholder="Select"
              data={['Morning', 'Afternoon', 'Night']}
            />
          </div>
        </div>

        {/* Right: Shift Assignment Tool */}
        <div className="bg-white px-4 sm:px-5 py-4 sm:py-5 rounded-2xl shadow-md">
          <div className="flex items-center gap-3 pb-3">
            <GoGear className="text-blue-600" />
            <span className="text-base text-[#1E293B] font-semibold">
              Shift Assignment Tool
            </span>
          </div>

          <div className="py-3">
            <Select
              size="md"
              label="Select Shift to Assign"
              radius="md"
              placeholder="Pick shift"
              data={[
                'Morning Shift',
                'Afternoon Shift',
                'Night Shift',
                'Day Off',
              ]}
            />
          </div>

          <div className="bg-[#EFF6FF] rounded-2xl flex flex-col sm:flex-row gap-2 py-4 px-4 sm:px-5 mb-5">
            <span className="text-[14px] text-[#1E293B] font-bold">
              Instructions:
            </span>
            <span className="text-[14px] text-[#45556C]">
              Select a shift above, then click on any cell in the roster grid to
              assign that shift to the employee.
            </span>
          </div>
        </div>
      </div>
      {/* ===== Roster Grid Section ===== */}
      <div className="">
        <WeeklyRosterReport
          weekStart="2025-10-20"
          employees={employees}
          onPrevWeek={() =>  console.log('Previous week')}
          onNextWeek={() =>  console.log('Next week')}
          onToday={() =>  console.log('Go to Today')}
        />
      </div>
      <div className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-5'>
        {cardData.map((x, index) => (
          <WorkCard
            key={index}
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
    </div>
  );
};

export default Page;
