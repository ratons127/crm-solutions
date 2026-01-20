'use client';
import { Button, Group, Input } from '@mantine/core';
import { CiFilter } from 'react-icons/ci';
import { IoIosSearch } from 'react-icons/io';
import { TbArrowsSort } from 'react-icons/tb';
import { IoAdd } from 'react-icons/io5';
import { LuDownload } from 'react-icons/lu';
import { HiOutlineDotsVertical } from 'react-icons/hi';
import { CiGlobe } from 'react-icons/ci';
import { useDisclosure } from '@mantine/hooks';
import { Modal } from '@mantine/core';
import HolidayButton from './holidayButton';

const LeftSection = () => {
  const [opened, { open, close }] = useDisclosure(false);
  return (
    <div>
      <div className=" bg-white w-full h-full mb-5 gap-4 border-l border-gray-200">
        <div className=" border-b border-gray-200 w-full  ">
          <div className="bg-white px-4 rounded-4xl pb-10 ">
            <h5 className="py-5">Holiday Management</h5>
            <Input
              placeholder="Search holidays... "
              leftSection={<IoIosSearch size={16} />}
              radius="md"
            />
            <Group justify="center" className="py-5 flex gap-5 ">
              <Button
                leftSection={<CiFilter size={14} />}
                variant="default"
                px={57}
              >
                Filter
              </Button>
              <Button
                leftSection={<TbArrowsSort size={14} />}
                variant="default"
                px={57}
              >
                Sort
              </Button>
            </Group>
            <Group justify="center" className=" flex  ">
              <Button
                leftSection={<LuDownload size={14} />}
                variant="outline"
                color="#F69348"
                px={110}
              >
                Export Holidays
              </Button>
            </Group>
          </div>
        </div>
        {/* scend part */}
        <div className=" px-4 border-b border-gray-200 ">
          <h5 className="text-[14px] text-[#101828] py-5">Filter By</h5>
          <div>
            <h4 className="text-[14px] text-[#101828] ">Workplace group</h4>
            <Group justify="start" className="py-5 flex  ">
              <Button
                variant="light"
                color="orange"
                className="text-[16px] rounded-2xl! "
                size="xs"
              >
                Software
              </Button>
              <Button
                variant="light"
                color="blue"
                className="text-[16px] rounded-2xl! "
                size="xs"
              >
                bd calling
              </Button>
              <Button
                variant="light"
                color="yellow"
                className="text-[16px] rounded-2xl! "
                size="xs"
              >
                Fire AI
              </Button>

              <Button
                variant="outline"
                color="black"
                className="text-[16px] rounded-2xl! border border-gray-200!"
                size="xs"
              >
                Sales
              </Button>
              <Button
                variant="light"
                color="yellow"
                className="text-[16px] rounded-2xl! "
                size="xs"
              >
                Marketing
              </Button>
            </Group>
          </div>
        </div>
        {/* third part */}

        <div className="px-4 border-b border-gray-200  py-5">
          <div className="flex  items-center justify-between py-5">
            <h5>Holidays (10)</h5>
            <Button
              leftSection={<IoAdd size={14} />}
              variant="filled"
              color="orange"
              className="text-[16px] rounded-lg! py-5"
              size="sm"
              onClick={open}
            >
              Add Holiday
            </Button>
          </div>

          {dataCard.map(x => (
            <div key={x.id} className="py-2 mb-2">
              <CardData name={x.name} date={x.date} />
            </div>
          ))}
        </div>
        {/*  */}
        <Modal
          withCloseButton
          title={<h2 className="text-lg font-semibold ">Add New Holiday</h2>}
          opened={opened}
          onClose={close}
          radius={'lg'}
        >
          {/* Modal content */}
          <HolidayButton />
        </Modal>
      </div>
    </div>
  );
};

export default LeftSection;

const dataCard = [
  { id: 1, name: "New Year's Day", date: 'Jan 1, 2024' },
  { id: 2, name: "New Year's Day", date: 'Jan 1, 2024' },
  { id: 3, name: "New Year's Day", date: 'Jan 1, 2024' },
  { id: 4, name: "New Year's Day", date: 'Jan 1, 2024' },
  { id: 5, name: "New Year's Day", date: 'Jan 1, 2024' },
  { id: 6, name: "New Year's Day", date: 'Jan 1, 2024' },
];

const CardData = ({ name, date }: { name: string; date: string }) => {
  return (
    <div className="border border-gray-300 rounded-2xl px-4 py-4">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-[16px] text-[#101828]">{name}</h2>
          <h5 className="text-[14px] text-[#4A5565]">{date}</h5>

          <div>
            <Group justify="center" className=" flex py-2 gap-2 ">
              <Button
                leftSection={<CiGlobe size={14} />}
                variant="filled"
                color="green"
                className="text-[16px] rounded-2xl! "
                size="xs"
              >
                Public
              </Button>
              <Button
                variant="transparent"
                color="black"
                className="text-[16px] text-[#4A5565]! rounded-2xl! "
                size="xs"
              >
                All groups
              </Button>
            </Group>
          </div>
        </div>
        <HiOutlineDotsVertical />
      </div>
    </div>
  );
};
