import React from 'react'
import InfoCard from './InfoCard';
import { CiCalendar } from 'react-icons/ci';
import LanguageCard from './languageCard';

const LeaveBalance = () => {
  const formData = [
    { id: 1, title: 'Annual Leave' , value:"18 days" , color:"#DCFCE7",textColor:"#016630"},
    { id: 2, title: 'Sick Leave' , value:"5 days"  ,color:"#FEF9C2" , textColor:"#894B00"},
    { id: 3, title: 'Personal Leave' , value:"3 days" ,color:"#DBEAFE" , textColor:"#193CB8"},
  
  ];

  return (
    <div>
      <InfoCard
        label="Leave Balance"
        description=''
        labelIcon={<CiCalendar   size={18} />}
      >
        <div className=" gap-3 py-3">
          {formData.map(x => (
            <div key={x.id} className=' py-3 '>

            <LanguageCard key={x.id} title={x.title} value={x.value} color={x.color} textColor={x.textColor} />
            </div>
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default LeaveBalance
