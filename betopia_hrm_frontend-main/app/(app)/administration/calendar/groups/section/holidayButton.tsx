import { Button, Group, Select, TextInput } from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import React, { useState } from 'react';


const HolidayButton = () => {
  const [value, setValue] = useState<string | null>(null);

  return (
    <div>
      <div className="">
        <div className="pb-5">
          <p className="text-md text-[#717182]">
            Create a new holiday and assign it to workplace groups.
          </p>
        </div>
        <form className="relative space-y-4">
          <TextInput
            label="Holiday Name"
            variant="filled"
            size="md"
            radius="md"
            placeholder="Enter holiday name"
          />
          <DatePickerInput
            label="Pick date"
            placeholder="Enter your date"
            value={value}
            onChange={setValue}
          />
          <Select
            defaultValue={'Govt Holiday'}
            size="md"
            radius="md"
            label="Type"
            data={['Govt Holiday', 'Religious Holidays', 'Bank Holidays']}
          />
          <div className="">
            <h4 className="text-lg text-[#0A0A0A]">Workplace Groups</h4>
            <div className="flex justify-between">
              <Group justify="start" className="py-5 flex  ">
                <Button
                  variant="outline"
                  color="black"
                  className="text-[16px] rounded-2xl! border border-gray-200! text-[#0A0A0A]! "
                  size="xs"
                >
                  All Groups
                </Button>
                <Button
                  variant="outline"
                  color="black"
                  className="text-[16px] rounded-2xl! border border-gray-200! text-[#0A0A0A]! "
                  size="xs"
                >
                  Software
                </Button>
                <Button
                  variant="outline"
                  color="black"
                  className="text-[16px] rounded-2xl! border border-gray-200! text-[#0A0A0A]! "
                  size="xs"
                >
                  bd calling
                </Button>
                <Button
                  variant="outline"
                  color="black"
                  className="text-[16px] rounded-2xl! border border-gray-200! text-[#0A0A0A]! "
                  size="xs"
                >
                  Fire AI
                </Button>

                <Button
                  variant="outline"
                  color="black"
                  className="text-[16px] rounded-2xl! border border-gray-200! text-[#0A0A0A]! "
                  size="xs"
                >
                  Sales
                </Button>
                <Button
                  variant="outline"
                  color="black"
                  className="text-[16px] rounded-2xl! border border-gray-200! text-[#0A0A0A]! "
                  size="xs"
                >
                  Marketing
                </Button>
              </Group>
            </div>
            <div className="w-full">
              <Button radius={'md'} variant="filled" color="#FF6900" px={245}>
                Add Holiday
              </Button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default HolidayButton;
