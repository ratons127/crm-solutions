import React from 'react';
import { RiGroupLine  } from 'react-icons/ri';
import { CiCalendar } from "react-icons/ci";
import { MdMapsHomeWork } from "react-icons/md";
import { IoMdCheckmarkCircleOutline } from "react-icons/io";





const HolidayCard = () => {
  const holidayData = [
    {
      id: 1,
      label: 'Working Days',
      value: 142,
      iconBg: 'bg-[#F0FDF4]',
      icon: <RiGroupLine size={20} color="green"
               />,
    },
    {
      id: 2,
      label: 'Total Holidays',
      value: 10,
      iconBg: 'bg-[#FAF5FF]',
      icon: <CiCalendar  size={20} color='#155DFC' />,
    },
    {
      id: 3,
      label: 'Workplace Groups',
      value: 5,
      iconBg: 'bg-[#FAF5FF]',
      icon: <MdMapsHomeWork   size={20} color='#9810FA' />,
    },
    {
      id: 4,
      label: 'Public Holidays',
      value: 8,
      iconBg: 'bg-[#FFF7ED]',
      icon: <IoMdCheckmarkCircleOutline  size={20} color='#F54900'/>,
    },
   
  ];

  return (
    <div className="flex gap-5  justify-between px-0 py-5">
      {holidayData.map(x => (
        <CardData
          key={x.id}
          icon={x.icon}
          label={x.label}
          value={x.value}
          iconBg={x.iconBg}
        />
      ))}
    </div>
  );
};

export default HolidayCard;

const CardData = ({
  icon,
  label,
  value,
  iconBg,
}: {
  icon: React.ReactNode;
  label: string;
  value: number;
  iconBg: string;
}) => {
  return (
    <div className=" w-66 h-23  bg-white shadow-sm rounded-xl   flex items-center   px-5">
      {/* Icon */}
      <div
        className={`flex items-center justify-center w-10 h-10 rounded-lg ${iconBg} `}
      >
        {icon}
      </div>

      {/* Text */}
      <div className="flex flex-col justify-center">
        <span className="text-[14px] text-[#4A5565]">{label}</span>
        <span className="text-[24px] text-[#101828]">{value}</span>
    
      </div>
    </div>
  );
};
