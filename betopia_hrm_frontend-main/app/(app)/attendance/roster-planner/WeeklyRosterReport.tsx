'use client';

import dayjs from 'dayjs';
import React from 'react';
import { BsChevronLeft, BsChevronRight } from 'react-icons/bs';
import { FiCalendar } from 'react-icons/fi';

export type ShiftAssignment = {
  date: string;
  code: 'MS' | 'AS' | 'NS' | 'OFF' | 'WFH' | string;
  draft?: boolean;
};

export type Employee = {
  id: string;
  name: string;
  code: string;
  team: string;
  assignments: ShiftAssignment[];
};

interface WeeklyRosterReportProps {
  weekStart: string;
  employees: Employee[];
  onPrevWeek?: () => void;
  onNextWeek?: () => void;
  onToday?: () => void;
}

/* ===== Shift color styles ===== */
const shiftColors: Record<string, string> = {
  MS: 'bg-[#E7F8ED] text-[#18794E]',
  AS: 'bg-[#E7F0FB] text-[#2456B2]',
  NS: 'bg-[#F1E9FE] text-[#6A1EBB]',
  OFF: 'bg-[#F3F4F6] text-[#4B5563]',
  WFH: 'bg-[#FEF4E7] text-[#C2550B]',
};

const WeeklyRosterReport: React.FC<WeeklyRosterReportProps> = ({
  weekStart,
  employees,
  onPrevWeek,
  onNextWeek,
  onToday,
}) => {
  const start = dayjs(weekStart);
  const days = Array.from({ length: 7 }, (_, i) => start.add(i, 'day'));

  return (
    <div className="bg-white border border-gray-200 rounded-2xl shadow-sm overflow-hidden px-2 sm:px-5">
      {/* ===== Header ===== */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 px-3 sm:px-6 py-4 bg-white sticky top-0 z-10">
        <div className="flex items-center gap-2 text-gray-800 font-medium text-sm sm:text-base">
          <FiCalendar className="flex-shrink-0" />
          <span className="truncate">Week of {start.format('MMMM D, YYYY')}</span>
        </div>

        <div className="flex items-center gap-2 w-full sm:w-auto">
          <button
            onClick={onPrevWeek}
            className="p-2 rounded-md hover:bg-gray-100 border border-gray-200 flex-shrink-0"
          >
            <BsChevronLeft size={16} />
          </button>
          <button
            onClick={onToday}
            className="px-3 py-1 text-sm rounded-md border border-gray-200 hover:bg-gray-100 flex-1 sm:flex-none"
          >
            Today
          </button>
          <button
            onClick={onNextWeek}
            className="p-2 rounded-md hover:bg-gray-100 border border-gray-200 flex-shrink-0"
          >
            <BsChevronRight size={16} />
          </button>
        </div>
      </div>

      {/* ===== Table Grid ===== */}
      <div className="overflow-x-auto -mx-2 sm:mx-0">
        <table className="min-w-full border border-gray-200">
          {/* Table Head */}
          <thead>
            <tr>
              <th className="sticky left-0 z-10 text-left text-xs sm:text-sm text-gray-600 font-semibold bg-white px-2 sm:px-4 py-3 border-gray-200 w-32 sm:w-56">
                Employee
              </th>
              {days.map(d => (
                <th
                  key={d.format('YYYY-MM-DD')}
                  className="text-center text-xs sm:text-sm text-gray-600 font-medium bg-white border-b border-gray-200 py-3 min-w-[80px] sm:min-w-[100px]"
                >
                  <div>{d.format('ddd')}</div>
                  <div className="text-xs text-gray-400">
                    {d.format('MMM D')}
                  </div>
                </th>
              ))}
            </tr>
          </thead>

          {/* Table Body */}
          <tbody>
            {employees.map((emp, empIdx) => (
              <tr key={emp.id} className="bg-white">
                {/* Employee Info */}
                <td className="sticky left-0 bg-white border-t border-r border-gray-200 px-2 sm:px-4 py-3 align-top">
                  <div className="font-medium text-gray-800 text-xs sm:text-sm truncate">{emp.name}</div>
                  <div className="text-xs text-gray-500">{emp.code}</div>
                  <div className="text-xs text-gray-400 hidden sm:block">{emp.team}</div>
                </td>

                {/* Shift Cells */}
                {days.map((d, dayIdx) => {
                  const assign = emp.assignments.find(a =>
                    dayjs(a.date).isSame(d, 'day')
                  );

                  return (
                    <td
                      key={`${empIdx}-${dayIdx}`}
                      className="text-center align-middle border-t border-l border-gray-200 h-12 sm:h-16 bg-white hover:bg-gray-50 transition min-w-[80px] sm:min-w-[100px]"
                    >
                      {assign ? (
                        <div
                          className={`inline-flex px-1.5 sm:px-2 py-1 text-xs font-semibold rounded-md ${
                            shiftColors[assign.code] ||
                            'bg-gray-100 text-gray-700'
                          } ${assign.draft ? 'ring-1 sm:ring-2 ring-orange-400 ring-offset-1' : ''}`}
                        >
                          {assign.code}
                        </div>
                      ) : (
                        <div className="border border-transparent hover:border-orange-400 m-1 rounded-2xl border-dashed text-orange-400 text-xs opacity-0 hover:opacity-100 font-medium py-4 sm:py-8">
                          <span className="hidden sm:inline">No Assignment</span>
                          <span className="sm:hidden">+</span>
                        </div>
                      )}
                    </td>
                  );
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* ===== Legend Section ===== */}
      <div className="px-3 sm:px-6 py-4 bg-white text-xs sm:text-sm text-gray-600">
        <p className="font-semibold mb-2">Shift Legend</p>
        <div className="flex flex-wrap gap-2 sm:gap-4 items-center">
          <Legend
            color="bg-[#E7F8ED] text-[#18794E]"
            label="MS"
            text="Morning Shift (09:00 - 18:00)"
          />
          <Legend
            color="bg-[#E7F0FB] text-[#2456B2]"
            label="AS"
            text="Afternoon Shift (14:00 - 23:00)"
          />
          <Legend
            color="bg-[#F1E9FE] text-[#6A1EBB]"
            label="NS"
            text="Night Shift (22:00 - 06:00)"
          />
          <Legend
            color="bg-[#F3F4F6] text-[#4B5563]"
            label="OFF"
            text="Day Off (-- --)"
          />
          <Legend
            color="bg-[#FEF4E7] text-[#C2550B]"
            label="WFH"
            text="Work From Home (09:00 - 18:00)"
          />
        </div>
        <div className='flex items-center gap-2 sm:gap-4 py-2 mt-2'>
          <span className="bg-orange-400 w-2 h-2 rounded-full flex-shrink-0"></span>
          <p className="text-xs text-orange-500">
            Orange ring indicates draft (unpublished) assignments
          </p>
        </div>
      </div>
    </div>
  );
};

/* ===== Legend Component ===== */
const Legend = ({
  color,
  label,
  text,
}: {
  color: string;
  label: string;
  text: string;
}) => (
  <div className="flex items-center gap-1.5 sm:gap-2">
    <span className={`px-1.5 sm:px-2 py-1 rounded-md text-xs font-semibold ${color}`}>
      {label}
    </span>
    <span className="text-gray-600 text-xs sm:text-sm">{text}</span>
  </div>
);

export default WeeklyRosterReport;
