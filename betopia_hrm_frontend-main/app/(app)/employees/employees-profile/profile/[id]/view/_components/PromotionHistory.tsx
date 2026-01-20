import React from 'react';
import InfoCard from './InfoCard';

import { FaArrowTrendUp } from 'react-icons/fa6';
import PromotionCard from './PromotionCard';

const PromotionHistory = () => {
  const promotionsData = [
    {
      id: 101,
      tag: 1,
      changeText: '+15%',
      fromValue: 'Software Engineer',
      toValue: 'Senior Software Engineer',
      promotionDate: 'January 15, 2023',
      effectiveDate: 'February 1, 2023',
      reason: 'Performance Excellence',
      remarks:
        'Exceptional performance in project delivery and team leadership',
      gradientFrom: '#F0FDF4',
      gradientTo: '#ECFDF5',
      borderColor: '#B9F8CF',
      headingColor: '#016630',
      buttonBg: '#00C950',
      buttonText: '#FFFFFF',
    },
  ];

  return (
    <div>
      <InfoCard
        label="Promotion History"
        description="Career advancement and position changes"
        labelIcon={<FaArrowTrendUp size={18} />}
      >
        <div className=" flex flex-col gap-4 py-3">
          {promotionsData.map(p => (
            <PromotionCard
              key={p.id}
              tag={p.tag}
              changeText={p.changeText}
              fromValue={p.fromValue}
              toValue={p.toValue}
              promotionDate={p.promotionDate}
              effectiveDate={p.effectiveDate}
              reason={p.reason}
              remarks={p.remarks}
              // optional theming (leave out if you like the defaults)
              gradientFrom={p.gradientFrom}
              gradientTo={p.gradientTo}
              borderColor={p.borderColor}
              headingColor={p.headingColor}
              buttonBg={p.buttonBg}
              buttonText={p.buttonText}
              className=""
            />
          ))}

          {/* <PromotionCard /> */}
        </div>
      </InfoCard>
    </div>
  );
};
export default PromotionHistory;
