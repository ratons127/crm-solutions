// 'use client';
// import { Button, MultiSelect, Select, TextInput, Textarea } from '@mantine/core';
// import { useCalendarContext } from '../calendarContext';
// import { useUpdateHolidayMutation } from '@/lib/features/calendar/calendarHolidayAPI';
// import { notifications } from '@mantine/notifications';
// import dayjs from 'dayjs';
// import { useState } from 'react';

// export default function HolidayButton() {
//   const { selectedDates, setSelectedDates } = useCalendarContext();
//   const [holidayType, setHolidayType] = useState<string | null>('HOLIDAY');
//   const [holidayDescription, setHolidayDescription] = useState('');
//   const [updateHoliday] = useUpdateHolidayMutation();

//   const handleSave = async () => {
//     if (selectedDates.length === 0) {
//       notifications.show({
//         title: 'No Dates Selected',
//         message: 'Please select at least one date from the calendar.',
//         color: 'red',
//       });
//       return;
//     }

//     // ✅ Build minimal payload (you can add description if needed)
//     const payload = selectedDates.map((date) => ({
//       id: 0, // for new holidays, or existing id if updating
//       holidayDate: date,
//       isHoliday: true,
//       ...(holidayDescription && { description: holidayDescription }),
//       ...(holidayType && { weekendType: holidayType }),
//     }));

//     try {
//       await updateHoliday(payload).unwrap();
//       notifications.show({
//         title: 'Success',
//         message: 'Holiday dates saved successfully!',
//         color: 'green',
//       });
//       setSelectedDates([]);
//       setHolidayDescription('');
//     } catch (err: any) {
//       notifications.show({
//         title: 'Error',
//         message: err?.data?.message || 'Failed to save holidays',
//         color: 'red',
//       });
//     }
//   };

//   return (
//     <div>

//       {/* Type */}
//       <Select
//         label="Type"
//         data={['HOLIDAY', 'OFFDAY']}
//         value={holidayType}
//         onChange={setHolidayType}
//         variant="filled"
//         size="md"
//         radius="md"
//         allowDeselect={false}
//         required
//       />

//       {/* Description */}
//       <Textarea
//         label="Description"
//         placeholder="Enter a holiday description (optional)"
//         variant="filled"
//         size="md"
//         radius="md"
//         mt="md"
//         minRows={2}
//         value={holidayDescription}
//         onChange={(e) => setHolidayDescription(e.currentTarget.value)}
//       />

//       {/* Selected Dates */}
//       <MultiSelect
//         label="Selected Dates"
//         data={
//           selectedDates.length > 0
//             ? selectedDates.map((d) => ({
//                 label: dayjs(d).format('MMM D, YYYY'),
//                 value: d,
//               }))
//             : []
//         }
//         value={selectedDates}
//         searchable
//         clearable
//         readOnly
//         placeholder={
//           selectedDates.length > 0
//             ? 'Selected from calendar'
//             : 'No dates selected yet'
//         }
//         mt="md"
//       />

//       {/* Save Button */}
//       {/* <Button fullWidth color="orange" mt="lg" radius="md" onClick={handleSave}>
//         Save New Calendar
//       </Button> */}
//     </div>
//   );
// }


'use client';
import { Button, MultiSelect, Select, Textarea } from '@mantine/core';
import { useCalendarContext } from '../calendarContext';
import {
  UpdateHolidayRequest,
  useGetCurrentYearHolidaysQuery,
  useUpdateHolidayMutation,
} from '@/lib/features/calendar/calendarHolidayAPI';
import { notifications } from '@mantine/notifications';
import dayjs from 'dayjs';
import { useState } from 'react';

interface Props {
  onSuccess?: () => void;
}

export default function HolidayButton({ onSuccess }: Props) {
  const { selectedDates, setSelectedDates } = useCalendarContext();
  const [holidayType, setHolidayType] = useState<'HOLIDAY' | 'OFFDAY'>('HOLIDAY');
  const [holidayDescription, setHolidayDescription] = useState('');

  const { data: holidayData, refetch } = useGetCurrentYearHolidaysQuery();
  const [updateHoliday] = useUpdateHolidayMutation();

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

    // ✅ Match selected dates with existing holidays
     // ✅ Filter valid existing holidays before map
  const payload = selectedDates.reduce<UpdateHolidayRequest[]>((acc, date) => {
    const found = existingHolidays.find((h) => h.holidayDate === date);
    if (!found) return acc; // skip unknown date
    acc.push({
      id: found.id,
      holidayDate: date,
      isHoliday: true,
      ...(holidayDescription && { description: holidayDescription }),
      weekendType: holidayType,
    });
    return acc;
  }, []);

  if (payload.length === 0) {
    notifications.show({
      title: 'No Valid Holidays Found',
      message:
        'None of the selected dates match existing records to update.',
      color: 'yellow',
    });
    return;
  }

  try {
    await updateHoliday(payload).unwrap();
    await refetch();
    notifications.show({
      title: 'Success',
      message: `${holidayType} dates updated successfully!`,
      color: 'green',
    });
    setSelectedDates([]);
    setHolidayDescription('');
    if (onSuccess) onSuccess();
  } catch (err: any) {
    notifications.show({
      title: 'Error',
      message: err?.data?.message || `Failed to update ${holidayType} dates.`,
      color: 'red',
    });
  }
};

  return (
    <div>
      <p className="text-md text-[#717182] pb-3">
        Update existing {holidayType.toLowerCase()} days.
      </p>

      {/* Type Selector */}
      <Select
        label="Type"
        data={[
          { label: 'Holiday', value: 'HOLIDAY' },
          { label: 'Weekend', value: 'OFFDAY' },
        ]}
        value={holidayType}
        onChange={(val) => setHolidayType(val as 'HOLIDAY' | 'OFFDAY')}
        variant="filled"
        size="md"
        radius="md"
        allowDeselect={false}
        required
      />

      {/* Description Field */}
      <Textarea
        label="Description"
        placeholder={`Enter ${holidayType.toLowerCase()} description (optional)`}
        variant="filled"
        size="md"
        radius="md"
        mt="md"
        minRows={2}
        value={holidayDescription}
        onChange={(e) => setHolidayDescription(e.currentTarget.value)}
      />

      {/* Selected Dates */}
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

      {/* Save Button */}
      <Button fullWidth color="orange" mt="lg" radius="md" onClick={handleSave}>
        Save New Calendar
      </Button>
    </div>
  );
}
