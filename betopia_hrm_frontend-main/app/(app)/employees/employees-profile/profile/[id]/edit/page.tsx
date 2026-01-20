'use client';

import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { FileButton, Progress, Tabs, Tooltip } from '@mantine/core';
import Image from 'next/image';
import { useParams } from 'next/navigation';
import { useState } from 'react';
import { BsPerson } from 'react-icons/bs';
import { FaRegAddressCard } from 'react-icons/fa6';
import { LuPhone } from 'react-icons/lu';
import { PiBagSimpleLight } from 'react-icons/pi';
import { RiGraduationCapLine } from 'react-icons/ri';
import ContactEditTab from './_components/tabs/ContactEditTab';
import DocumentEditTab from './_components/tabs/DocumentEditTab';
import EducationEditTab from './_components/tabs/EducationEditTab';
import EmploymentEditTab from './_components/tabs/EmploymentEditTab';
import PersonalEditTab from './_components/tabs/PersonalEditTab';

const ProfileEditPage = () => {
  const { id }: { id: string } = useParams();
  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });
  const [_, setFile] = useState<File | null>(null);

  const tabList = [
    {
      value: 'Personal',
      icon: <BsPerson size={12} />,
      tab: <PersonalEditTab />,
    },
    { value: 'Contact', icon: <LuPhone size={12} />, tab: <ContactEditTab /> },
    {
      value: 'Employment',
      icon: <PiBagSimpleLight size={12} />,
      tab: <EmploymentEditTab />,
    },
    {
      value: 'Documents',
      icon: <FaRegAddressCard size={12} />,
      tab: <DocumentEditTab />,
    },
    {
      value: 'Education',
      icon: <RiGraduationCapLine size={12} />,
      tab: <EducationEditTab />,
    },
  ];

  return (
    <div>
      <div className=" w-full rounded-lg shadow-lg px-5 py-5">
        {/*  */}
        <div className="grid grid-cols-12 items-center justify-start gap-6">
          {/* Profile Image Section */}
          {/* Image */}

          <div className="col-span-12 md:col-span-2">
            <div className="flex flex-col ">
              <div className="w-38 h-38 bg-gray-200 border-5 border-blue-200 rounded-full bg-cover bg-center flex items-center justify-center overflow-hidden">
                <FileButton onChange={setFile} accept="image/png,image/jpeg">
                  {/* Image upload pending */}
                  {props => (
                    <Tooltip
                      label="Click on image to change"
                      position="bottom"
                      withArrow
                    >
                      <Image
                        loading="lazy"
                        src={'/user_img.png'}
                        width={120}
                        height={120}
                        alt="Profile"
                        className="size-40 cursor-pointer object-cover rounded-full"
                        {...props}
                      />
                    </Tooltip>
                  )}
                </FileButton>
              </div>
            </div>
          </div>
          {/* section */}
          <div className="col-span-12 md:col-span-10">
            <div className="flex flex-col items-start justify-start gap-2">
              <h1 className="text-[35px] text-[#1D293D] font-bold">
                {data?.data?.displayName}
              </h1>
              <h5 className="text-[20px] text-[#45556C] font-semibold ">
                Complete the form to create employee profile
              </h5>
            </div>

            <div className="w-50 lg:w-full relative ">
              <h6 className="text-[16px] text-[#314158] py-2">
                Profile Completion
              </h6>
              <Progress value={1} striped animated />
              <span className="absolute top-0 -right-1">1%</span>
            </div>
          </div>
          {/*  */}
        </div>
        {/* Profile Edit Navbar */}
      </div>
      <div className=" pt-10">
        <Tabs variant="pills" radius="md" defaultValue="Personal">
          {/* ---- Tabs Header ---- */}
          <div className="bg-[#D9D9D9] w-full py-1 rounded-lg mb-5">
            <Tabs.List justify="space-around">
              <>
                {tabList.map(tab => (
                  <Tabs.Tab
                    key={tab.value}
                    value={tab.value}
                    px="xl"
                    leftSection={tab.icon}
                    classNames={{
                      tab: 'data-[active=true]:bg-[#F69348] data-[active=true]:text-white',
                    }}
                  >
                    {tab.value}
                  </Tabs.Tab>
                ))}
              </>
            </Tabs.List>
          </div>
          <>
            {tabList?.map(x => (
              <Tabs.Panel keepMounted key={x.value} value={x.value}>
                {x.tab}
              </Tabs.Panel>
            ))}
          </>
        </Tabs>
      </div>
    </div>
  );
};

export default ProfileEditPage;
