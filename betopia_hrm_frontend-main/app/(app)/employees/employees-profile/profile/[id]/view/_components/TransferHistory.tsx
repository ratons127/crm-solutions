import React from 'react';
import InfoCard from './InfoCard';
import { FaArrowTrendUp } from 'react-icons/fa6';
import TransferCard from './TransferCard';
import { RiArrowLeftRightLine } from 'react-icons/ri';

const TransferHistory: React.FC = () => {
  const transfers = [
    {
      id: 10,
      tag: 1,
      title: 'Transfer',
      changeText: 'Internal Transfer',
      icon: <RiArrowLeftRightLine  />,
      fromValue: 'Software Engineer',
      toValue: 'Senior Software Engineer',
      promotionDate: 'January 15, 2023', // or rename field to transferDate in both places
      effectiveDate: 'February 1, 2023',
      reason: 'Performance Excellence',
      remarks: 'Exceptional performance in project delivery and team leadership',
      gradientFrom: '#EFF6FF',
      gradientTo: '#EEF2FF',
      borderColor: '#BEDBFF',
      headingColor: '#016630',
      buttonBg: '#00C950',
      buttonText: '#FFFFFF',
    },
  ];

  return (
    <div>
      <InfoCard
        label="Transfer History" // ← was Promotion History
        description="Career movement and position/location changes"
        labelIcon={<FaArrowTrendUp size={18} />}
      >
        <div className="flex flex-col gap-4 py-3">
          {transfers.map((p) => (
            <TransferCard
              key={p.id}
              tag={p.tag}
              title={p.title}
              changeText={p.changeText}
              icon={p.icon}
              fromValue={p.fromValue}
              toValue={p.toValue}
              promotionDate={p.promotionDate}
              effectiveDate={p.effectiveDate}
              reason={p.reason}
              remarks={p.remarks}
              gradientFrom={p.gradientFrom}
              gradientTo={p.gradientTo}
              borderColor={p.borderColor}
              headingColor={p.headingColor}
              buttonBg={p.buttonBg}
              buttonText={p.buttonText}
            />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default TransferHistory;
