import { Button } from '@mantine/core';
import React from 'react';

export type PreviousWorkCardProps = {
  header?: string;
  changeText?: string;

  title: string;
  value: string;
  label: string;
  values: string;

  second_title: string;
  second_value: string;
  second_label: string;
  second_values: string;

  responsibilities: string;

  responsibilitiesText: string[];

  achievements: string;

  achievementsText: string[];

  TechnologiesUsed: string;
  tags: string[];

  /** Button styling (Mantine) */
  buttonBg?: string;
  buttonText?: string;
  buttonProps?: React.ComponentProps<typeof Button>;
};

const PreviousWorkCard: React.FC<PreviousWorkCardProps> = ({
  header = 'Software Engineer at Digital Solutions Ltd.',
  changeText = '2 years',

  title = 'Location',
  value = 'Jane Smith',
  label = 'Duration',
  values = 'January 15, 2020 - December 31, 2021',

  second_title = 'Reporting Manager',
  second_value = 'Jane Smith',
  second_label = 'Reason for Leaving',
  second_values = 'Career Growth',

  responsibilities = 'Key Responsibilities',

  responsibilitiesText = [],

  achievements = 'Key Achievements',
  achievementsText = [],

  TechnologiesUsed = 'Technologies Used',
  //   text = "",
  tags = ['React', 'Node.js', 'MongoDB', 'AWS'],

  buttonBg = '#F1F5F9',
  buttonText = '#1D293D',
  buttonProps,
}) => {
  return (
    <div>
      <div className=" bg-[#F8FAFC] border border-[#E2E8F0] px-5 py-5 rounded-2xl ">
        {/* header */}
        <div className="grid grid-cols-2 px-5 py-5">
          <div className="flex flex-wrap gap-3 items-center justify-start">
            <div className="text-[18px]">
              <div className="flex items-center gap-3">
                <span className="text-[18px]">{header}</span>
              </div>
            </div>
          </div>
          <div className="flex place-content-end">
            <Button
              variant="filled"
              radius="md"
              size="sm"
              styles={{
                root: { backgroundColor: buttonBg },
                label: { color: buttonText },
              }}
              {...buttonProps}
            >
              {changeText}
            </Button>
          </div>
        </div>
        {/* title */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5 px-5  py-2">
          <div className="flex items-center gap-3">
            <span>{title} :</span>
            <span>{value}</span>
          </div>
          <div className="flex items-center gap-3">
            <span>{label} :</span>
            <span>{values}</span>
          </div>
          <div className="flex items-center gap-3">
            <span>{second_title} :</span>
            <span>{second_value}</span>
          </div>
          <div className="flex items-center gap-3">
            <span>{second_label} :</span>
            <span>{second_values}</span>
          </div>
        </div>
        <div className=" px-5 py-3">
          <h5>{responsibilities} :</h5>
          <ul>
            {responsibilitiesText.map((x, i) => (
              <li key={i}>
                <span>{x}</span>
              </li>
            ))}
          </ul>
        </div>
        <div className=" px-5  py-3">
          <h5>{achievements} :</h5>
          <ul>
            {achievementsText.map((x, i) => (
              <li key={i}>
                <span>{x}</span>
              </li>
            ))}
          </ul>
        </div>
        <div className="   px-3 ">
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
      </div>
    </div>
  );
};

export default PreviousWorkCard;
