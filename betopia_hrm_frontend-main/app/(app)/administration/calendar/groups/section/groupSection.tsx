'use client';

import { motion } from 'framer-motion';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { BsThreeDotsVertical } from 'react-icons/bs';
import { RiGroupLine } from 'react-icons/ri';
import { CiCalendar } from 'react-icons/ci';
import HolidaySectionCard from './holidaySectionCard';

type GroupSectionProps = {
  title: string;
  description: string;
  label: string;
  value: string;
  labelNum1: number;
  labelNum2: number;
};

const GroupSection = ({
  title,
  description,
  label,
  value,
  labelNum1,
  labelNum2,
}: GroupSectionProps) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 40 }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true, amount: 0.8 }}
      transition={{ duration: 0.8, ease: 'easeInOut' }}
      className="bg-white w-full md:w-[340px] rounded-2xl border border-gray-200 overflow-hidden shadow-sm hover:shadow-md transition-all duration-300"
    >
      {/* Header */}
      <div className="flex items-center justify-between gap-10 mb-3 px-4 py-4">
        <div>
          <h5 className="text-base sm:text-lg font-bold text-[#FF6900]">{title}</h5>
          <p className="text-sm sm:text-base text-[#4A5565]">{description}</p>
        </div>
        <RowActionDropdown
          data={[
            { label: 'EDIT', action: () => {} },
            { label: 'DELETE', action: () => {} },
          ]}
        >
          <BsThreeDotsVertical />
        </RowActionDropdown>
      </div>

      {/* Info Section */}
      <div>
        <div className="flex flex-col gap-2">
          <div className="flex items-center gap-5 px-4">
            <div className="bg-[#4A4A4A1A] w-6 h-6 rounded-lg flex items-center justify-center text-[#6A7282]">
              <RiGroupLine size={12} />
            </div>
            <h6>
              <span>{labelNum1}</span>
              <span> {label}</span>
            </h6>
          </div>

          <div className="flex items-center gap-5 px-4">
            <div className="bg-[#4A4A4A1A] w-6 h-6 rounded-lg flex items-center justify-center text-[#6A7282]">
              <CiCalendar size={12} />
            </div>
            <h6>
              <span>{labelNum2}</span>
              <span> {value}</span>
            </h6>
          </div>
        </div>

        <div className="border-b border-gray-200 mx-4 py-2"></div>
      </div>

      {/* Holidays Section */}
      <HolidaySectionCard />
    </motion.div>
  );
};

export default GroupSection;
