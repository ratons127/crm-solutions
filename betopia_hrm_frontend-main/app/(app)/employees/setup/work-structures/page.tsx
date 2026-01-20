'use client';

import ConfigPageLayout from '@/(app)/administration/ConfigPageLayout';
import GradeSection from './GradeSection';
import DesignationSection from './DesignationSection';

export default function WorkStructuresPage() {
  return (
    <ConfigPageLayout
      title="Work Structure"
      sections={[
        { id: 'grades', title: 'Job Grade', content: <GradeSection /> },
        { id: 'designations', title: 'Designation', content: <DesignationSection /> },
      ]}
    />
  );
}
