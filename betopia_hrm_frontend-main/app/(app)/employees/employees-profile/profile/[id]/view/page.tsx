'use client';

import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
// import { Button, Group, LoadingOverlay, Progress } from '@mantine/core';
import { Button, LoadingOverlay } from '@mantine/core';
import Image from 'next/image';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { FileButton, ActionIcon } from '@mantine/core';
import { MdEdit } from 'react-icons/md';
// import { BsDot, BsPersonCheckFill } from 'react-icons/bs';
// import { FaRegEdit } from 'react-icons/fa';
// import { FiDownload } from 'react-icons/fi';
import { HiOutlineMail } from 'react-icons/hi';
import { RiLockPasswordLine } from 'react-icons/ri';
// import { IoIosTrendingUp } from 'react-icons/io';
// import { IoStarOutline } from 'react-icons/io5';
import { LiaPhoneSolid } from 'react-icons/lia';
// import { MdEngineering, MdOutlineVerifiedUser } from 'react-icons/md';
import { MdOutlineVerifiedUser } from 'react-icons/md';
import { RiIdCardLine } from 'react-icons/ri';
// import { TbEye } from 'react-icons/tb';
// import ProfileCard from './_components/profileCard';
import ProfileNavbar from './_components/profileNavbar';
import { notifications } from '@mantine/notifications';
import { useState } from 'react';
import { uploadEmployeeImage } from '@/services/api/employee/employeeUploadImageAPI';

const ProfileView = () => {
  const { id }: { id: string } = useParams();
  const { data, isLoading, refetch } = useGetEmployeeByIdQuery({ id: Number(id) });
  const [uploading, setUploading] = useState(false);

  const handleUpload = async (file: File | null) => {
    if (!file) return;
    try {
      setUploading(true);
      const res = await uploadEmployeeImage(Number(id), file);
      console.log(res)
      notifications.show({
        title: 'Success',
        message: 'Profile image updated successfully!',
        color: 'green',
      });
      await refetch(); // Refresh employee data
    } catch (err) {
      console.error(err);
      notifications.show({
        title: 'Upload failed',
        message: 'Something went wrong while uploading the image.',
        color: 'red',
      });
    } finally {
      setUploading(false);
    }
  };

  if (isLoading) return <LoadingOverlay />;

  const profileImage =
    (data?.data as any)?.imageUrl || '/user_img.png';

  return (
    <div>
      <div className="bg-white w-full rounded-lg shadow-lg px-5 py-5">
        <div className="grid grid-cols-12 items-center justify-start gap-6">
          {/* Profile Image Section */}
          {/* Image */}

          <div className="col-span-12 md:col-span-2">
            <div className="flex flex-col ">
              <div className="relative w-38 h-38">
                <Image
                  src={profileImage}
                  width={150}
                  height={150}
                  alt="Profile"
                  unoptimized
                  className="size-40 object-cover rounded-full border-4 border-gray-100 shadow-sm"
                />

                {/* 📸 Edit Icon Overlay */}
                <div className="absolute bottom-0 right-0">
                  <FileButton onChange={handleUpload} accept="image/*">
                    {(props) => (
                      <ActionIcon
                        {...props}
                        variant="filled"
                        color="orange"
                        size="lg"
                        radius="xl"
                        loading={uploading}
                        className="shadow-md"
                      >
                        <MdEdit size={18} />
                      </ActionIcon>
                    )}
                  </FileButton>
                </div>
              </div>
              {/* <div className="py-3">
                <div className="flex items-center justify-center w-18 h-8 bg-[#00C950] rounded-3xl  ">
                  <p className="text-[14px] text-white "></p>
                  <Button
                    variant="filled"
                    color="#00C950"
                    className="text-[16px] rounded-2xl!  text-white! "
                    size="xs"
                  >
                    Active
                  </Button>
                </div>
                <p className="text-[15px] text-[#62748E] py-3">
                  Last Login: 2024-01-20 09:30 AM
                </p>
              </div> */}
            </div>
          </div>
          {/* section */}
          <div className="col-span-12 md:col-span-10">
            <div className="flex items-start justify-start gap-5">
              <h1 className="text-[30px] text-[#1D293D] font-bold">
                {data?.data.displayName ??
                  data?.data?.firstName + ' ' + data?.data?.lastName}
              </h1>
              <Button
                leftSection={<MdOutlineVerifiedUser size={14} />}
                variant="filled"
                color="#DCFCE7"
                className="text-[16px] rounded-2xl!  text-[#016630]! "
                size="xs"
              >
                Verified{}
              </Button>
            </div>
            {/* <div className="flex items-start justify-start gap-5">
              <h5 className="text-[18px] text-[#45556C] font-medium">
                Senior Software Engineer
              </h5>
              <div>
                <ul className="flex items-center justify-center">
                  <li>
                    <BsDot size={25} color="" />
                  </li>
                  <li className="text-[18px] text-[#45556C] font-medium">
                    Engineering
                  </li>
                </ul>
              </div>
            </div> */}
            <div className="flex flex-col items-start justify-start gap-2">
              <div>
                <ul className="flex items-center justify-center gap-4">
                  <li>
                    <HiOutlineMail size={20} color="#62748E" />{' '}
                  </li>
                  <li className="text-[17px] text-[#62748E] ">
                    {data?.data.email}
                  </li>
                </ul>
              </div>
              <div>
                <ul className="flex items-center justify-center gap-4">
                  <li>
                    <LiaPhoneSolid size={20} color="#62748E" />{' '}
                  </li>
                  <li className="text-[17px] text-[#62748E] ">
                    {data?.data.phone}
                  </li>
                </ul>
              </div>
              <div>
                <ul className="flex items-center justify-center gap-4">
                  <li>
                    <RiIdCardLine size={20} color="#62748E" />{' '}
                  </li>
                  <li className="text-[17px] text-[#62748E] ">
                    {data?.data?.employeeSerialId}
                  </li>
                </ul>
              </div>
            </div>

            {/* Change Password Button */}
            <div className="flex items-center justify-start gap-3 py-4">
              <Link href={`/employees/employees-profile/profile/${id}/change-password`}>
                <Button
                  leftSection={<RiLockPasswordLine size={16} />}
                  variant="outline"
                  color="orange"
                  className="text-[16px] rounded-md! border border-orange-500!  "
                  size="md"
                >
                  Change Password
                </Button>
              </Link>
            </div>

            {/* <div className="flex items-center justify-start gap-3 py-4">
              <Group justify="center">
                <Button
                  leftSection={<MdEngineering size={14} />}
                  variant="filled"
                  color="#314158"
                  className="text-[16px] rounded-md!  text-white! "
                  size="xs"
                >
                  Engineering
                </Button>
                <Button
                  leftSection={<BsPersonCheckFill size={14} />}
                  variant="filled"
                  color="#314158"
                  className="text-[16px] rounded-md!  text-white! "
                  size="xs"
                >
                  Full-time
                </Button>
                <Button
                  leftSection={<IoStarOutline size={14} />}
                  variant="filled"
                  color="#314158"
                  className="text-[16px] rounded-md!  text-white! "
                  size="xs"
                >
                  G6
                </Button>
                <Button
                  leftSection={<IoIosTrendingUp size={14} />}
                  variant="filled"
                  color="#314158"
                  className="text-[16px] rounded-md!  text-white! "
                  size="xs"
                >
                  Excellent
                </Button>
              </Group>
            </div>
            <div className="w-50 lg:w-full relative ">
              <h6 className="text-[16px] text-[#314158]">Profile Completion</h6>
              <Progress value={50} striped animated />
              <span className="absolute top-0 -right-1">50%</span>
            </div>

            <div className="flex items-center justify-start gap-3 py-4">
              <Group justify="center">
                <Link href={`/employees/employees-profile/profile/${id}/edit`}>
                  <Button
                    leftSection={<FaRegEdit size={14} />}
                    variant="outline"
                    color="black"
                    className="text-[16px] rounded-md! border border-gray-200! "
                    size="md"
                    onClick={() => {}}
                  >
                    Edit Profile
                  </Button>
                </Link>
                <Link href={'/#'}>
                  <Button
                    leftSection={<FiDownload size={14} />}
                    variant="outline"
                    color="black"
                    className="text-[14px] rounded-md! border border-gray-200!  "
                    size="md"
                  >
                    Export
                  </Button>
                </Link>
                <Link
                  href={'/employees/employees-profile/profile/profile-View'}
                >
                  <Button
                    leftSection={<TbEye size={14} />}
                    variant="filled"
                    color="orange"
                    className="text-[16px] rounded-lg! border border-gray-200!  "
                    size="md"
                  >
                    View History
                  </Button>
                </Link>
              </Group>
            </div> */}
          </div>
          {/*  */}
        </div>
      </div>

      {/* <ProfileCard /> */}

      <ProfileNavbar />
    </div>
  );
};

export default ProfileView;
