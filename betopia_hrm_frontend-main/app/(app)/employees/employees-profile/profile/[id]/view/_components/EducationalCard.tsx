import { Button } from '@mantine/core';
import React from 'react';

type EducationalProps = {
  dataList: {
    title: string;
    value?: string;
    icons?: React.ReactNode;
  }[];

  header: string;
  changeText: string;
  bt_icons?: React.ReactNode[];
  icon_color?: string;

  TechnologiesUsed: string;
  tags: string[];

  remarks: string;
  remarksText: string;

  /** Button styling (Mantine) */
  buttonBg?: string;
  buttonText?: string;
  buttonProps?: React.ComponentProps<typeof Button>;
};

const EducationalCard: React.FC<EducationalProps> = ({
  header = 'Master of Science in Software Engineering - MIT',
  changeText = 'Highest Degree',
  bt_icons = [],
  dataList = [
    {
      title: 'Field of Study',
      value: 'Software Engineering',
      icons: '',
    },
  ],

  icon_color = '',

  TechnologiesUsed = 'Technologies Used',
  //   text = "",
  tags = ['React', 'Node.js', 'MongoDB', 'AWS'],

  remarks = 'Remarks',
  remarksText = 'Specialized in Web Development and User Experience Design',
}) => {
  return (
    <div className=" border border-[#FFD6A7] py-5 px-5 rounded-lg ">
      {/* header */}
      <div className="grid grid-cols-2  py-5">
        <div className="flex flex-wrap gap-3 items-center justify-start">
          <div className="text-[18px]">
            <div className="flex items-center gap-3">
              <span className="text-[18px]">{header}</span>
            </div>
          </div>
        </div>
        <div className="flex place-content-end gap-5">
          {bt_icons.map((x, i) => (
            <div key={i}>
              <div
                className={`flex  items-center    ${icon_color}  rounded-3xl px-3 py-1 `}
              >
                {bt_icons && <span className="text-white">{x} </span>}
                <p className="text-sm text-white ">{changeText}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
      {/*  */}
      <div className=" grid  grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-5">
        {dataList.map((x, i) => (
          <div
            key={i}
            className="flex items-start border-b border-gray-200   gap-4 py-5  "
          >
            <div className="flex items-center  gap-2">
              {x.icons && <span className="text-gray-500">{x.icons}</span>}
              <span className="text-sm text-gray-500">{x.title} :</span>
            </div>
            <span className="text-sm font-medium text-gray-900">{x.value}</span>
          </div>
        ))}
      </div>
      {/*  */}
      <div className="  py-5 px-3 ">
        <h5 className="">{TechnologiesUsed}</h5>

        <ul className="flex items-center gap-3 py-3">
          {tags.map((x, i) => (
            <li key={i}>
              <span className="text-[14px] text-[#193CB8] bg-[#DBEAFE] px-5 py-1 flex items-center justify-center rounded-2xl ">
                {x}
              </span>
            </li>
          ))}
        </ul>
      </div>
      {/*  */}
      <div>
        <h1>{remarks} :</h1>
        <div>
          <span>{remarksText}</span>
        </div>
      </div>
    </div>
  );
};

export default EducationalCard;
