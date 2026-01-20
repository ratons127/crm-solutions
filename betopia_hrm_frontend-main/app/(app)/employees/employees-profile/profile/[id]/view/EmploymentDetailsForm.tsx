import React from 'react';
import {
 
  Select,
  TextInput,
} from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { cn } from '@/lib/utils/utils';

const DivideY = ({ strong = false }: { strong?: boolean }) => {
  return (
    <div
      className={cn(
        `col-span-12 border-b  `,
        strong ? 'border-gray-400/50 ' : 'border-gray-400/20'
      )}
    ></div>
  );
};

const EmploymentDetailsForm = () => {
  return (
    <div>
      <form className="p-5 grid grid-cols-12 gap-5">
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Job Grade"
          placeholder="Enter Your Job Grade"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Corporate Title"
          placeholder="Enter Your Corporate Title"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Designation"
          placeholder="Enter Your Designation"
        />
        <DivideY strong />
        <DateInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Join Date"
          placeholder="Join Date"
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Employment Type"
          data={['Full-Time Employee', 'Part-Time Employee']}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Probation Duration"
          placeholder="Enter Your Probation Duration"
          data={['3 Months', '6 Months']}
        />
        <Select
          className="col-span-12 sm:col-span-6"
          variant="filled"
          label="Pay Grade"
          placeholder="Select Pay Grade"
          data={[
            { value: 'grade-1', label: 'Grade 1' },
            { value: 'grade-2', label: 'Grade 2' },
            { value: 'grade-3', label: 'Grade 3' },
            { value: 'grade-4', label: 'Grade 4' },
          ]}
        />

        <Select
          className="col-span-12 sm:col-span-6"
          variant="filled"
          label="Payment Type"
          placeholder="Enter your Payment Type"
          data={['Cash', 'Bank']}
        />
        <DivideY strong />

        <TextInput
          className="col-span-12 sm:col-span-6"
        variant="filled"
          label="Attendance ID"
          placeholder="Enter Your Attendance ID"
        />
        <DateInput
          className="col-span-12 sm:col-span-6"
          variant="filled"
          label="Date for Confirmation"
          placeholder="Enter your Date for Confirmation"
        />
      </form>
    </div>
  );
};

export default EmploymentDetailsForm;
