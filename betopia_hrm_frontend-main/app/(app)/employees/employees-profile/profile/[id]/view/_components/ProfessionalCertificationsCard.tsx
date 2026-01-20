import { Button } from '@mantine/core';
import React from 'react';

type ProfessionalCertificationsCardProps = {
  title: string;
  value?: string;
  Label?: string;
  IssuedValue: string;
  ExpiresValue: string;
  idValue: string;
  DevValue: string;
  //   color: string;
  //   textColor: string;
  //   className?: string;
};

const ProfessionalCertificationsCard: React.FC<
  ProfessionalCertificationsCardProps
> = ({
  title,
  value,
  Label,
  IssuedValue,
  ExpiresValue,
  idValue,
  DevValue,
  //   color,
  //   textColor,
}) => {
  return (
    <div className=" bg-[#F0FDF4] border border-[#B9F8CF] rounded-lg  w-full ">
      <div className="grid grid-cols-2  justify-between  py-3 px-3 ">
        <div className="flex  flex-col justify-baseline">
          <h6 className="text-[#016630] text-[16px] font-bold">{title}</h6>
          <h6 className="text-[#00A63E] text-[14px] py-2">{Label}</h6>
          <div className="flex items-center ">
            <div className="flex border-r border-gray-300  pr-3">
              <span>Issued : </span>
              <span> {IssuedValue}</span>
            </div>
            <div className="flex   pl-3">
              <span>Expires : </span>
              <span> {ExpiresValue}</span>
            </div>
          </div>
        </div>

        <div className=" flex   justify-end ">
          <div className="flex flex-col items-center justify-center gap-2">
            <Button
              variant="filled"
              radius="md"
              size="sm"
              styles={{
                label: {
                  color: '#016630',
                },
                root: {
                  backgroundColor: '#DCFCE7',
                },
              }}
            >
              {value}
            </Button>
            <h5 className='text-[12px]'>
             
              ID: <span>{idValue}</span>
            </h5>
            <h5 className='text-[12px]'>
         
              DEV-<span>{DevValue}</span>
            </h5>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfessionalCertificationsCard;
