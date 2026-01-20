
'use client';

import {
  Button,
  Loader,
  Modal,
  Text
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IoAdd } from 'react-icons/io5';
import { MdEditCalendar } from 'react-icons/md';
import { useGetCurrentYearHolidaysQuery } from '@/lib/features/calendar/calendarHolidayAPI';
import HolidayButton from './holidayButton';
import UpdateHolidayButton from './updateHolidayButton';

export default function LeftSection() {
  const [holidayOpened, { open: openHoliday, close: closeHoliday }] = useDisclosure(false);
  const [updateOpened, { open: openUpdate, close: closeUpdate }] = useDisclosure(false);

  const { isLoading, isError } = useGetCurrentYearHolidaysQuery();

  if (isLoading) return <Loader />;
  if (isError) return <Text c="red">Error loading holidays</Text>;

  return (
    <div className="bg-white w-full h-full">
      <div className="px-1">
        <div className="flex items-center justify-between py-5 gap-2">
          <Button
            leftSection={<IoAdd size={14} />}
            variant="filled"
            color="orange"
            size="sm"
            onClick={openHoliday}
          >
            Add Calendar
          </Button>

          <Button
            leftSection={<MdEditCalendar size={14} />}
            variant="outline"
            color="blue"
            size="sm"
            onClick={openUpdate}
          >
            Update Holidays or Weekends
          </Button>
        </div>
      </div>

      {/* Add Calendar Modal */}
      <Modal
        withCloseButton
        title={<h2 className="text-lg font-semibold">Add New Calendar</h2>}
        opened={holidayOpened}
        onClose={closeHoliday}
        radius="lg"
      >
        <HolidayButton onSuccess={closeHoliday} />
      </Modal>

      {/* Update Holiday Modal */}
      <Modal
        withCloseButton
        title={<h2 className="text-lg font-semibold">Update Holiday Status</h2>}
        opened={updateOpened}
        onClose={closeUpdate}
        radius="lg"
      >
        <UpdateHolidayButton onSuccess={closeUpdate} />
      </Modal>
    </div>
  );
}


