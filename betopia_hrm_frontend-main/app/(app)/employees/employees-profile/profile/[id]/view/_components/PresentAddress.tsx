import React from 'react';
import InfoCard from './InfoCard';
import InfoFrom from './InfoFrom';
import { LuHouse } from "react-icons/lu";



const PresentAddress = () => {
  const formData = [
    {
      id: 1,
      title: 'Address',
      value: '456 Valencia Street, San Francisco, CA 94110',
      
    },
    {
      id: 2,
      title: 'City',
      value: 'San Francisco',
     
    },
    {
      id: 3,
      title: 'State',
      value: 'California',
    
    },
    {
      id: 4,
      title: 'ZIP Code:',
      value: '94110',
   
    },
    {
      id: 5,
      title: 'Country:',
      value: 'Bangladesh',
    
    },
    {
      id: 6,
      title: 'Type',
      value: 'Apartment (Rented)',
    
    },
  ];

  return (
    <div>
      <InfoCard label="Present Address" description='' labelIcon={<LuHouse  size={18} />}>
        <div className="grid grid-cols-1 gap-10 py-6">
          {formData.map(x => (
            <InfoFrom
              key={x.id}
              title={x.title}
              value={x.value}
            
            />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default PresentAddress;
