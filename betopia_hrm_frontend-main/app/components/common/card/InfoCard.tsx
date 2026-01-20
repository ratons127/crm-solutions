// InfoCard.tsx
import { PropsWithChildren, ReactNode } from 'react';

interface InfoCardProps {
  label: string;
  description: string;
  labelIcon?: ReactNode;
  content?: ReactNode; // optional, for backward-compat
}

const InfoCard = ({
  label,
  description,
  labelIcon,
  content,
  children,
}: PropsWithChildren<InfoCardProps>) => {
  return (
    <div>
      <div className="bg-white w-full rounded-2xl shadow-lg">
        <div className="flex flex-col items-start g bg-[#314158] text-white rounded-tl-lg rounded-tr-lg px-10 py-4">
          <div className='flex items-center gap-4 bg-[#314158] text-white'>
            {labelIcon}
            <h1 className="text-lg font-light">{label}</h1>
          </div>
          <p className='text-[14px]'>{description}</p>
        </div>
        <div className="py-5 px-10">{content ?? children}</div>
      </div>
    </div>
  );
};

export default InfoCard;
