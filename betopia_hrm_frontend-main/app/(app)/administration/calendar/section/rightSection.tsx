'use client';

import { useState, useMemo } from 'react';
import dayjs from 'dayjs';
import { DatePicker } from '@mantine/dates';
import { ActionIcon, Group, Loader, Tooltip } from '@mantine/core';
import { FaAngleLeft, FaAngleRight } from 'react-icons/fa6';
import { notifications } from '@mantine/notifications';
import { useGetCurrentYearHolidaysQuery } from '@/lib/features/calendar/calendarHolidayAPI';
import { useCalendarContext } from '../calendarContext';

export default function RightSection() {
  const currentYear = new Date().getFullYear();
  const [year, setYear] = useState(currentYear);
  const { selectedDates, setSelectedDates } = useCalendarContext();

  const { data, isLoading, isError } = useGetCurrentYearHolidaysQuery();
  const holidays = data?.data ?? [];

  /* ---------- Generate months for selected year ---------- */
  const months = useMemo(
    () =>
      Array.from({ length: 12 }, (_, i) =>
        dayjs(`${year}-01-01`).add(i, 'month').format('YYYY-MM-DD')
      ),
    [year]
  );

  /* ---------- Navigation ---------- */
  const increment = () => setYear((prev) => prev + 1);
  const decrement = () => setYear((prev) => prev - 1);

  /* ---------- Toggle date selection ---------- */
  const toggleDateSelect = (date: string) => {
    const prev = selectedDates ?? [];
    const newDates: string[] = prev.includes(date)
      ? prev.filter((d: string) => d !== date)
      : [...prev, date];
    setSelectedDates(newDates);
  };

  /* ---------- Select full weekday column ---------- */
  const handleWeekdayClick = (weekdayIndex: number) => {
    const allDates = months.flatMap((month) => {
      const start = dayjs(month).startOf('month');
      const end = dayjs(month).endOf('month');
      const result: string[] = [];
      for (
        let d = start;
        d.isBefore(end) || d.isSame(end, 'day');
        d = d.add(1, 'day')
      ) {
        if (d.day() === weekdayIndex)
          result.push(d.format('YYYY-MM-DD'));
      }
      return result;
    });

    setSelectedDates(allDates);
    notifications.show({
      title: 'Dates Selected',
      message: `${allDates.length} ${
        ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'][weekdayIndex]
      } days selected for ${year}`,
      color: 'blue',
    });
  };

  const getHolidayColor = (date: string) => {
    if (selectedDates.includes(date)) return '#FF9800'; // highlight selected ones
    const entry = holidays.find((h) => h.holidayDate === date);
    if (entry?.isHoliday) return entry.colorCode || '#FF6666';
    return null;
  };

  if (isLoading) return <Loader />;
  if (isError) return <div className="text-red-500">Error loading holidays</div>;

  const weekdays = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'];

  return (
    <div className="p-2">
      {/* Year Navigation */}
      <div className="flex justify-between items-center w-full px-2">
        <div className="mt-2">
          <Group gap={5} justify="flex-end" wrap="nowrap">
            <ActionIcon variant="outline" disabled onClick={decrement}>
              <FaAngleLeft size={20} />
            </ActionIcon>
            <h2 className="px-4 font-semibold text-lg">{year}</h2>
            <ActionIcon variant="outline" disabled onClick={increment}>
              <FaAngleRight size={20} />
            </ActionIcon>
          </Group>
        </div>

        {/* Legend */}
        <div className="flex flex-wrap items-center justify-center gap-5 mt-2 text-xs sm:text-sm text-gray-600">
          <div className="flex items-center gap-2">
            <span className="w-3 h-3 bg-red-400 rounded-full" />
            <span>Existing Holiday</span>
          </div>
          <div className="flex items-center gap-2">
            <span className="w-3 h-3 bg-orange-400 rounded-full" />
            <span>Selected</span>
          </div>
        </div>
      </div>

      {/* Weekday Header Buttons */}
      <div className="flex justify-center gap-4 mt-2 mb-2 text-sm">
        {weekdays.map((day, i) => (
          <button
            key={day}
            onClick={() => handleWeekdayClick(i)}
            className="w-8 h-8 text-white font-medium bg-orange-500 hover:bg-orange-600 hover:text-gray-200 transition rounded-full"
          >
            {day}
          </button>
        ))}
      </div>

      {/* Monthly Calendars */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-10 justify-items-center py-5 px-5">
        {months.map((month, i) => (
          <div
            key={i}
            className="border border-[#E8E8E8] rounded-lg shadow-sm hover:shadow-md transition bg-gray-50"
          >
            <DatePicker
              key={i}
              defaultDate={new Date(month)}
              renderDay={(dateString) => {
                const d = dayjs(dateString);
                const dateIso = d.format('YYYY-MM-DD');
                const color = getHolidayColor(dateIso);
                const holiday = holidays.find((h) => h.holidayDate === dateIso);
                const tooltip = holiday
                  ? holiday.description || 'Holiday'
                  : selectedDates.includes(dateIso)
                  ? 'Selected for New Holiday'
                  : 'Working Day';

                return (
                  <Tooltip
                    label={tooltip}
                    withArrow
                    position="top"
                    key={dateIso}
                  >
                    <div
                      className={`relative flex items-center justify-center w-8 h-8 cursor-pointer rounded-full transition-all duration-200 ${
                        color ? 'text-white font-semibold' : ''
                      }`}
                      style={{
                        backgroundColor: color || 'transparent',
                      }}
                      onClick={() => toggleDateSelect(dateIso)}
                    >
                      {d.date()}
                    </div>
                  </Tooltip>
                );
              }}
              styles={{
                calendarHeader: { justifyContent: 'center' },
                monthCell: { backgroundColor: 'transparent' },
              }}
            />
          </div>
        ))}
      </div>
    </div>
  );
}
