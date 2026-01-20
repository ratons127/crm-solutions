import React from 'react';
import { MdOutlineWatchLater } from 'react-icons/md';
import { PiHandbagLight } from 'react-icons/pi';
import { FaRegStar } from 'react-icons/fa6';
import { IoCalendarClearOutline } from 'react-icons/io5';

const ProfileCard = () => {
  const profileData = [
    {
      id: 1,
      label: 'Company Tenure',
      value: '2 years 3 months',
      //   iconBg: 'bg-[#EFF6FF]',
      bg_coles: 'bg-[#EFF6FF]',
      icon: <MdOutlineWatchLater size={20} color="green" />,
    },
    {
      id: 2,
      label: 'Total Experience',
      value: '6 years',

      icon: <PiHandbagLight size={20} color="#00A63E" />,
      bg_coles: 'bg-[#F0FDF4]',
    },
    {
      id: 3,
      label: 'Performance Rating',
      value: 'Excellent',

      icon: <FaRegStar size={20} color="#D08700" />,
      bg_coles: 'bg-[#FEFCE8]',
    },
    {
      id: 4,
      label: 'Annual Leave Balance',
      value: '18 days',

      icon: <IoCalendarClearOutline size={20} color="#9810FA" />,
      bg_coles: 'bg-[#FAF5FF]',
    },
  ];

  return (
    <div className="grid  grid-cols-1 md:grid-cols-2 xl:grid-cols-4  gap-5  justify-between  py-8">
      {profileData.map(x => (
        <CardData
          key={x.id}
          icon={x.icon}
          label={x.label}
          value={x.value}
        //   iconBg={x.iconBg}
          bg_coles={x.bg_coles}
        />
      ))}
    </div>
  );
};

export default ProfileCard;

const CardData = ({
  icon,
  label,
  value,
 
  bg_coles,
}: {
  icon: React.ReactNode;
  label: string;
  value: string;
 
  bg_coles: string;
}) => {
  return (
    <div
      className={`w-full h-23 ${bg_coles}  shadow-sm rounded-xl   flex items-center space-x-4 min-h-[50px] px-5 `}
    >
      {/* Icon */}
      <div
        className={`flex items-center justify-center w-10 h-10 rounded-lg  `}
      >
        {icon}
      </div>

      {/* Text */}
      <div className="flex flex-col justify-center text-[#45556C]">
        <span className="text-[14px] text-[#4A5565]">{label}</span>
        <span className="text-[14px] text-[#101828]">{value}</span>
      </div>
    </div>
  );
};
