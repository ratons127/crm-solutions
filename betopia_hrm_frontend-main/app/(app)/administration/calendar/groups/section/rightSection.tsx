'use client';

import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import GroupSection from './groupSection';

export default function RightSection(){



  // const groupData = [
  //   {
  //     id: 1,
  //     title: 'Group Title',
  //     description: 'Group description',
  //     label: 'Group Label',
  //     value: 'group-value',
  //     labelNum1: 1,
  //     labelNum2: 2,
  //   },
  // ];

  return (
    <div>
      <div className="flex flex-wrap  items-start justify-center  gap-5">
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        {/*  */}
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
        <GroupSection
          title="Group Title"
          description="Group description"
          label="Group Label"
          value="group-value"
          labelNum1={1}
          labelNum2={2}
        />
      </div>
    </div>
  );
}
