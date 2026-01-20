import InfoCard from './InfoCard';
import { CiGlobe } from "react-icons/ci";
import LanguageCard from './languageCard';

const Languages = () => {
  const formData = [
    { id: 1, title: 'Photography' , value:"Native" , color:"#DCFCE7",textColor:"#016630"},
    { id: 2, title: 'Spanish' , value:"Intermediate"  ,color:"#FEF9C2" , textColor:"#894B00"},
    { id: 3, title: 'French' , value:"Beginner" ,color:"#DBEAFE" , textColor:"#193CB8"},
  
  ];

  return (
    <div>
      <InfoCard
        label="Languages"
        description=''
        labelIcon={<CiGlobe  size={18} />}
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

export default Languages;
