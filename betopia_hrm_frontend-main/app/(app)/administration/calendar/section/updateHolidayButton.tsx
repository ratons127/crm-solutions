
'use client';
import { Button, MultiSelect, Select, Textarea } from '@mantine/core';
import { useCalendarContext } from '../calendarContext';
import {
  calendarHolidayAPI,
  useGetCurrentYearHolidaysQuery,
  useUpdateIsHolidayMutation,
} from '@/lib/features/calendar/calendarHolidayAPI';
import { notifications } from '@mantine/notifications';
import dayjs from 'dayjs';
import { useState } from 'react';
import { useAppDispatch } from '@/lib/hooks';

interface Props {
  onSuccess?: () => void;
}

export default function UpdateHolidayButton({ onSuccess }: Props) {
  const dispatch = useAppDispatch();
  const { selectedDates, setSelectedDates } = useCalendarContext();
  const [isHoliday, setIsHoliday] = useState<'true' | 'false'>('true');
  const [description, setDescription] = useState('');
  const { data: holidayData } = useGetCurrentYearHolidaysQuery();
  const [updateIsHoliday] = useUpdateIsHolidayMutation();
  const existingHolidays = holidayData?.data || [];

  const handleSave = async () => {
    if (selectedDates.length === 0) {
      notifications.show({
        title: 'No Dates Selected',
        message: 'Please select at least one date from the calendar.',
        color: 'red',
      });
      return;
    }
    const payload = selectedDates.reduce<any[]>((acc, date) => {
      const found = existingHolidays.find((h) => h.holidayDate === date);
      if (found) {
        acc.push({
          id: found.id,
          holidayDate: date,
          isHoliday: isHoliday === 'true',
        });
      }
      return acc;
    }, []);

    if (payload.length === 0) {
      notifications.show({
        title: 'No Valid Holidays Found',
        message: 'Selected dates don’t exist in current calendar records.',
        color: 'yellow',
      });
      return;
    }
    try {
      await updateIsHoliday(payload).unwrap();
     (dispatch as any)(
        calendarHolidayAPI.util.updateQueryData(
          'getCurrentYearHolidays',
          undefined,
          (draft: any) => {
            for (const p of payload) {
              const found = draft.data.find((h: any) => h.id === p.id);
              if (found) found.isHoliday = p.isHoliday;
            }
          }
        )
      );
      notifications.show({
        title: 'Success',
        message: `Holidays successfully updated to ${
          isHoliday === 'true' ? 'ACTIVE' : 'INACTIVE'
        }.`,
        color: 'green',
      });
      setSelectedDates([]);
      setDescription('');
      if (onSuccess) onSuccess(); // ✅ close modal instantly
    } catch (err: any) {
      notifications.show({
        title: 'Error',
        message: err?.data?.message || 'Failed to update holidays.',
        color: 'red',
      });
    }
  };

  return (
    <div>
      <Select
        label="Holiday Status"
        data={[
          { label: 'Mark as Holiday (true)', value: 'true' },
          { label: 'Remove Holiday (false)', value: 'false' },
        ]}
        value={isHoliday}
        onChange={(v) => setIsHoliday(v as 'true' | 'false')}
        variant="filled"
        radius="md"
        required
      />
      <Textarea
        label="Note (optional)"
        placeholder="Add any notes or reason for update"
        variant="filled"
        size="md"
        radius="md"
        mt="md"
        minRows={2}
        value={description}
        onChange={(e) => setDescription(e.currentTarget.value)}
      />
      <MultiSelect
        label="Selected Dates"
        data={selectedDates.map((d) => ({
          label: dayjs(d).format('MMM D, YYYY'),
          value: d,
        }))}
        value={selectedDates}
        searchable
        clearable
        readOnly
        placeholder={
          selectedDates.length > 0
            ? 'Selected from calendar'
            : 'No dates selected yet'
        }
        mt="md"
      />
      <Button fullWidth color="blue" mt="lg" radius="md" onClick={handleSave}>
        Update Holidays or Weekends Status
      </Button>
    </div>
  );
}

