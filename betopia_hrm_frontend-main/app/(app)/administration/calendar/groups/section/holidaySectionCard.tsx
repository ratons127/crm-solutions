import React, { useState } from 'react';



const HolidaySectionCard = () => {
  const [open, setOpen] = useState(false);

    const holidayData = [
      {
        id: 1,
        label: 'New Year Day',
        date: 'Jan 1',
        color: 'bg-[#DBEAFE] text-[#1447E6]',
      },
      {
        id: 2,
        label: 'New Year Day',
        date: 'Jan 1',
        color: 'bg-[#DBEAFE] text-[#1447E6]',
      },
      {
        id: 3,
        label: 'New Year Day',
        date: 'Jan 1',
        color: 'bg-[#DBEAFE] text-[#1447E6]',
      },
      {
        id: 4,
        label: 'New Year Day',
        date: 'Jan 1',
        color: 'bg-[#DBEAFE] text-[#1447E6]',
      },
      {
        id: 5,
        label: 'New Year Day',
        date: 'Jan 1',
        color: 'bg-[#DBEAFE] text-[#1447E6]',
      },
      {
        id: 6,
        label: 'New Year Day',
        date: 'Jan 1',
        color: 'bg-[#DBEAFE] text-[#1447E6]',
      },
    ];

    const visibleHolidays = open ? holidayData : holidayData.slice(0, 3);

  return (
    <div>

         <div className="  px-4 py-2">
          <h5 className="text-[16px] font-bold text-[#FF6900] py-2">
            Upcoming Holidays:
          </h5>
          <div className="flex flex-col gap-3">
            {visibleHolidays.map(x => (
              <HolidayCard
                key={x.id}
                label={x.label}
                date={x.date}
                color={x.color}
              />
            ))}
          </div>
        </div>
        <div className="">
         
          {holidayData.length > 2 && (
            <button
              onClick={() => setOpen(!open)}
              className="text-[#1447E6] text-sm font-medium  px-5 mb-5  overflow-hidden"
            >
              {open ? 'Show less' : `+${holidayData.length - 2} more`}
            </button>
          )}
        </div>


     
    </div>
  );
};

export default HolidaySectionCard;

const HolidayCard = ({
  label,
  date,
  color,
}: {
  label: string;
  date: string;
  color: string;
}) => {
  return (
    <div className="flex gap-5 items-center justify-between ">
      <span className="text-[16px] text-[#4A5565]">{label}</span>
      <span className={`"text-[8px]  ${color} px-4 py-1 rounded-2xl "`}>
        {date}
      </span>
    </div>
  );
};
