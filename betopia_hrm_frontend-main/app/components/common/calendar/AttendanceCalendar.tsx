'use client';

import React, { useMemo, useState } from 'react';

/* ---------- Types ---------- */
export type AttendanceStatus =
  | 'present'
  | 'late'
  | 'early_leave'
  | 'half_day'
  | 'absent'
  | 'leave'
  | 'off';

export type AttendanceMap = Record<string, AttendanceStatus>;

type Props = {
  initialYear?: number;
  initialMonth?: number;
  data?: AttendanceMap;
  onMonthChange?: (year: number, month: number) => void;
  onDayClick?: (isoDate: string, status?: AttendanceStatus) => void;
};

/* ---------- Constants ---------- */
const MONTHS = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

const WEEKDAYS_FULL = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
const WEEKDAYS_NARROW = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];

const STATUS_BG: Record<AttendanceStatus, string> = {
  present: 'bg-green-400 text-white',
  late: 'bg-yellow-300 text-white',
  early_leave: 'bg-orange-400 text-white',
  half_day: 'bg-blue-400 text-white',
  absent: 'bg-red-400 text-white',
  leave: 'bg-indigo-400 text-white',
  off: 'bg-gray-300 text-white',
};

/* ---------- Utils ---------- */
const pad2 = (n: number) => (n < 10 ? `0${n}` : `${n}`);
const toISO = (y: number, m0: number, d: number) =>
  `${y}-${pad2(m0 + 1)}-${pad2(d)}`;

function buildMonthMatrix(year: number, month0: number) {
  const first = new Date(year, month0, 1);
  const last = new Date(year, month0 + 1, 0);
  const daysInMonth = last.getDate();

  const startIdx = first.getDay();
  const totalCells = 42; // 6 weeks × 7 days grid
  const grid: Array<{ day?: number; inMonth: boolean }> = [];

  const prevLast = new Date(year, month0, 0).getDate();
  for (let i = 0; i < totalCells; i++) {
    const dayNum = i - startIdx + 1;
    if (dayNum < 1) grid.push({ day: prevLast + dayNum, inMonth: false });
    else if (dayNum > daysInMonth)
      grid.push({ day: dayNum - daysInMonth, inMonth: false });
    else grid.push({ day: dayNum, inMonth: true });
  }
  return grid;
}

/* ---------- Component ---------- */
export default function AttendanceCalendar({
  initialYear,
  initialMonth,
  data = {},
  onMonthChange,
  onDayClick,
}: Props) {
  const today = new Date();
  const [year, setYear] = useState(initialYear ?? today.getFullYear());
  const [month0, setMonth0] = useState(initialMonth ?? today.getMonth());

  const grid = useMemo(() => buildMonthMatrix(year, month0), [year, month0]);

  const changeMonth = (delta: number) => {
    const d = new Date(year, month0 + delta, 1);
    setYear(d.getFullYear());
    setMonth0(d.getMonth());
    onMonthChange?.(d.getFullYear(), d.getMonth());
  };

  return (
    <section className="rounded-2xl p-4 bg-white shadow border border-gray-200">
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-base font-semibold text-gray-800">
          Attendance Calendar
        </h2>
        <div className="flex items-center gap-2">
          <button
            type="button"
            onClick={() => changeMonth(-1)}
            className="px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
            aria-label="Previous month"
          >
            ‹
          </button>
          <span className="font-medium text-gray-700">
            {MONTHS[month0]} {year}
          </span>
          <button
            type="button"
            onClick={() => changeMonth(1)}
            className="px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
            aria-label="Next month"
          >
            ›
          </button>
        </div>
      </div>

      {/* Scroll wrapper for tiny screens */}
      <div className="overflow-x-auto sm:px-2 md:px-2 lg:px-2">
        <div className="min-w-[300px] sm:min-w-0">
          {/* Weekday header */}
          <div className="grid grid-cols-7 gap-1.5 sm:gap-2 text-center mb-2">
            {WEEKDAYS_FULL.map((d, idx) => (
              <div
                key={d}
                className="text-[10px] sm:text-xs font-medium text-gray-500 select-none"
              >
                <span className="sm:hidden">{WEEKDAYS_NARROW[idx]}</span>
                <span className="hidden sm:inline">{d}</span>
              </div>
            ))}
          </div>

          {/* Calendar grid */}
          <div className="grid grid-cols-7 gap-1.5 sm:gap-4">
            {grid.map((cell, i) => {
              const iso = cell.day ? toISO(year, month0, cell.day) : '';
              const status = iso ? data[iso] : undefined;
              const isToday =
                cell.inMonth &&
                iso ===
                  toISO(today.getFullYear(), today.getMonth(), today.getDate());

              let cellClasses =
                'relative h-8 w-8 sm:h-10 sm:w-10 md:h-11 md:w-11 lg:h-12 lg:w-12 ' +
                'rounded-md text-[11px] sm:text-xs flex items-center justify-center transition';

              if (!cell.inMonth) {
                cellClasses += ' bg-gray-100 text-gray-400';
              } else if (status) {
                cellClasses += ` ${STATUS_BG[status]}`;
              } else {
                cellClasses += ' bg-white text-gray-700 border';
              }

              if (isToday) {
                cellClasses += ' ring-2 ring-orange-200';
              }

              return (
                <div key={i} className="flex items-center justify-center">
                  <button
                    type="button"
                    onClick={() => iso && onDayClick?.(iso, status)}
                    className={cellClasses}
                    title={iso || undefined}
                  >
                    {cell.day}
                  </button>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* Legend */}
      <div className="flex flex-wrap items-center gap-3 sm:gap-4 mt-4 text-xs sm:text-sm">
        {(
          [
            ['present', 'Present'],
            ['late', 'Late'],
            ['early_leave', 'Early Leave'],
            ['half_day', 'Half Day'],
            ['absent', 'Absent'],
            ['leave', 'Leave'],
            ['off', 'Off Day'],
          ] as [AttendanceStatus, string][]
        ).map(([key, label]) => (
          <div key={key} className="flex items-center gap-2">
            <span
              className={`h-3 w-3 rounded-sm ${STATUS_BG[key].split(' ')[0]}`}
            />
            <span className="text-gray-600">{label}</span>
          </div>
        ))}
      </div>
    </section>
  );
}
