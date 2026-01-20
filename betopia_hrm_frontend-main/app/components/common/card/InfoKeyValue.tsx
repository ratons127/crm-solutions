import React from 'react';

type InfoKeyValueProps = {
  title: string;
  value?: string;
  icons?: React.ReactNode;
  //   className?: string;
};

const InfoKeyValue: React.FC<InfoKeyValueProps> = ({ title, value, icons }) => {
  return (
    <div className=" border-b border-gray-200 py-5 ">
      <div className="flex items-start  gap-4">
        <div className="flex items-center  gap-2">
          {icons && <span className="text-gray-500">{icons}</span>}
          <span className="text-sm text-gray-500">{title}:</span>
        </div>
        <span className="text-sm font-medium text-gray-900">{value} </span>
      </div>
    </div>
  );
};

export default InfoKeyValue;
