import React from 'react'
import InfoCard from './InfoCard';
import { LuChartColumn } from 'react-icons/lu';
import InfoForm from './InfoFrom';

const PerformanceReviews = () => {
  const formData = [
    { id: 1, title: 'Current Rating' , value:"Excellent" , },
    { id: 2, title: 'Last Review' , value:"January 15, 2024"  ,},
    { id: 3, title: 'Next Review' , value:"July 15, 2024" ,},
    { id: 4, title: 'Total Experience' , value:"6 years" ,},
  
  ];

  return (
    <div>
      <InfoCard
        label="Performance & Reviews"
        description=''
        labelIcon={<LuChartColumn  size={18} />}
      >
        <div className=" gap-3 py-3">
          {formData.map(x => (
            <div key={x.id} className=' py-3 '>

            <InfoForm key={x.id} title={x.title} value={x.value}  />
            </div>
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default PerformanceReviews
