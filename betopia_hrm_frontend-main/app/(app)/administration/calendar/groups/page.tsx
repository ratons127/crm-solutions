'use client';
import { Button } from '@mantine/core';
import { IoCalendarOutline } from 'react-icons/io5';
import { RiGroupFill } from 'react-icons/ri';
import LeftSection from './section/leftSection';
import RightSection from './section/rightSection';
// import { IoSettingsOutline } from 'react-icons/io5';
import Link from 'next/link';

import Breadcrumbs from '@/components/common/Breadcrumbs';

const Groups = () => {
  return (
    <div className=" h-auto ">
      {/* header */}
      <div className="bg-white h-auto  border-b-1 border-gray-300 py-2 px-5">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between ">
          <div>
            <h1 className="text-xl font-semibold text-gray-700 py-3">
              Holiday Management - Admin
            </h1>
            
            <Breadcrumbs />
          </div>
          <div className="flex items-center gap-2 mt-3 sm:mt-0">
            <Link href={'/administration/calendar'}>
              <Button
                leftSection={<IoCalendarOutline size={14} />}
                variant="outline"
                color="black"
                className="text-[16px] rounded-lg! border border-gray-200!  "
                size="md"
              >
                Calendar
              </Button>
            </Link>
            <Link href={'/administration/calendar/groups'}>
              <Button
                leftSection={<RiGroupFill size={14} />}
                variant="filled"
                color="orange"
                className="text-[16px] rounded-lg! border border-gray-200! "
                size="md"
              >
                Group
              </Button>
            </Link>
            {/* <Button
              leftSection={<IoSettingsOutline size={14} />}
              variant="outline"
              color="black"
              className="text-[16px] rounded-lg! border border-gray-200! "
              size="md"
            >
              Settings
            </Button> */}
          </div>
        </div>
      </div>
      {/* header */}
      <div className=" h-auto rounded-md shadow-md">
        <div className="flex justify-between gap-5  ">
          <div className="w-3/2  ">
          
            {/* Right section */}
            <div className="bg-white h-auto px-5  py-5">
              <RightSection />
            </div>
          </div>
          {/* left section */}
          <div className="w-1/2  ">
            <LeftSection />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Groups;
