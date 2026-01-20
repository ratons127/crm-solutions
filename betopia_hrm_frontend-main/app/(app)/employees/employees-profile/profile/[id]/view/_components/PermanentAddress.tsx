import React from 'react';
import InfoCard from './InfoCard';
import InfoForm from './InfoFrom';
import { LuHouse } from 'react-icons/lu';





const PermanentAddress = () => {
  const formData = [
    {
      id: 1,
      title: 'Address',
      value: '789 Congress Avenue, Austin, TX 78701',
      
    },
    {
      id: 2,
      title: 'City',
      value: 'Austin',
     
    },
    {
      id: 3,
      title: 'State',
      value: 'Texas',
    
    },
    {
      id: 4,
      title: 'ZIP Code:',
      value: '78701',
   
    },
    {
      id: 5,
      title: 'Country:',
      value: 'Bangladesh',
    
    },
    {
      id: 6,
      title: 'Type',
      value: 'House (Owned)',
    
    },
  ];

  return (
    <div>
      <InfoCard label="Permanent Address" description='' labelIcon={<LuHouse  size={18} />}>
        <div className="grid grid-cols-1 gap-10 py-6">
          {formData.map(x => (
            <InfoForm
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

export default PermanentAddress;
