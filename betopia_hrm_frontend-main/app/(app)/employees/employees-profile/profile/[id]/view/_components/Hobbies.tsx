import InfoCard from './InfoCard';

import { IoGameControllerOutline } from 'react-icons/io5';
import HobbiesCard from './hobbies-card';

const Hobbies = () => {
  const formData = [
    { id: 1, title: 'Photography' },
    { id: 1, title: 'Travel' },
    { id: 1, title: 'Reading' },
    { id: 1, title: 'Coding' },
    { id: 1, title: 'Swimming' },
    { id: 1, title: 'Music Production' },
  ];

  return (
    <div>
      <InfoCard
        label="Languages"
        description=""
        labelIcon={<IoGameControllerOutline size={18} />}
      >
        <div className=" flex flex-wrap gap-3 ">
          {formData.map(x => (
            <HobbiesCard key={x.id} title={x.title} />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default Hobbies;
