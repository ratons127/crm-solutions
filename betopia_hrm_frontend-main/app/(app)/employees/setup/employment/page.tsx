'use client';

import ConfigPageLayout from '@/(app)/administration/ConfigPageLayout';
import EmploymentGroup from './EmploymentGroup';
import EmploymentType from './EmploymentType';

export default function EmploymentPage() {
  return (
    <ConfigPageLayout
      title="Qualification"
      sections={[
        {
          id: 'employment-group',
          title: 'Employment Group',
          content: <EmploymentGroup />,
        },
        {
          id: 'employment-type',
          title: 'Employment Type',
          content: <EmploymentType />,
        },
      ]}
    />
  );
}
