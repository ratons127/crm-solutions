import React from 'react';
import InfoCard from './InfoCard';
import { GoPerson } from 'react-icons/go';
import { IoAlertCircleOutline } from 'react-icons/io5';
import FamilyInfoFrom from './FamilyInfoFrom';

const FamilyInfoPages = () => {
  return (
    <div>
      <InfoCard label="Family Information" description='' labelIcon={<GoPerson size={18} />}>
        <div className=" w-full  py-5">
          {/*  */}
          <FamilyInfoFrom
            items={[
              { label: 'Relationship', value: 'Spouse' },
              { label: 'Name', value: 'Sarah Anderson' },
              { label: 'Contact', value: '+1 (555) 123-4568' },
            ]}
            badge={{
              text: 'Emergency Contact',
              icon: <IoAlertCircleOutline size={14} />,
              variant: 'danger',
              title: 'This person is marked as an emergency contact',
            }}
          />

          {/*  */}
        </div>
        {/*  */}
        <div className=" w-full  py-5">
          {/*  */}
          <FamilyInfoFrom
            items={[
              { label: 'Relationship', value: 'Spouse' },
              { label: 'Name', value: 'Sarah Anderson' },
              { label: 'Contact', value: 'N/A' },
            ]}
            badge={{
              text: 'Emergency Contact',
              icon: <IoAlertCircleOutline size={14} />,
              variant: 'danger',
              title: 'This person is marked as an emergency contact',
            }}
          />

          {/*  */}
        </div>
        {/*  */}
        <div className=" w-full  py-5">
          {/*  */}
          <FamilyInfoFrom
            items={[
              { label: 'Relationship', value: 'Spouse' },
              { label: 'Name', value: 'Sarah Anderson' },
              { label: 'Contact', value: 'N/A' },
            ]}
            badge={{
              text: 'Emergency Contact',
              icon: <IoAlertCircleOutline size={14} />,
              variant: 'danger',
              title: 'This person is marked as an emergency contact',
            }}
          />

          {/*  */}
        </div>
        {/*  */}
        <div className=" w-full  py-5">
          {/*  */}
          <FamilyInfoFrom
            items={[
              { label: 'Relationship', value: 'Spouse' },
              { label: 'Name', value: 'Sarah Anderson' },
              { label: 'Contact', value: '+1 (555) 123-4568' },
            ]}
            badge={{
              text: 'Emergency Contact',
              icon: <IoAlertCircleOutline size={14} />,
              variant: 'danger',
              title: 'This person is marked as an emergency contact',
            }}
          />

          {/*  */}
        </div>
      </InfoCard>
    </div>
  );
};

export default FamilyInfoPages;
