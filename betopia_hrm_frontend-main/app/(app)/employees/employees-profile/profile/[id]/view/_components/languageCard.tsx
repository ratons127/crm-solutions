import { Button } from '@mantine/core';
import React from 'react';

type LanguageCardProps = {
  title: string;
  value?: string;
  color: string;
  textColor: string;
  //   className?: string;
};

const LanguageCard: React.FC<LanguageCardProps> = ({
  title,
  value,
  color,
  textColor,
}) => {
  return (
    <div className=" bg-[#F8FAFC]  rounded-lg  w-full ">
      <div className="grid grid-cols-2  justify-between  py-3 px-3 ">
        <h6>{title}</h6>
        <div className=" flex justify-end">
          <Button
            variant="filled"
            radius="md"
            size="sm"
            styles={{
              label: {
                color: textColor,
              },
              root: {
                backgroundColor: color,
              },
            }}
          >
            {value}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default LanguageCard;
