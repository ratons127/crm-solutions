'use client';

import ConfigPageLayout from '@/(app)/administration/ConfigPageLayout';
import FieldOfStudy from './FieldOfStudy';
import InstituteName from './InstituteName';
import QualificationLevel from './QualificationLevel';
import QualificationRatingMethodPage from './QualificationRatingMethod';
import QualificationType from './QualificationType';

export default function QualificationPage() {
  return (
    <ConfigPageLayout
      title="Qualification"
      sections={[
        {
          id: 'qualification-rating-method',
          title: 'Qualification Rating Method',
          content: <QualificationRatingMethodPage />,
        },
        {
          id: 'qualification-type',
          title: 'Qualification Type',
          content: <QualificationType />,
        },
        {
          id: 'qualification-level',
          title: 'Qualification Level',
          content: <QualificationLevel />,
        },
        {
          id: 'field-of-Study',
          title: 'Field Of Study',
          content: <FieldOfStudy />,
        },
        {
          id: 'institute-name',
          title: 'Institute Name',
          content: <InstituteName />,
        },
      ]}
    />
  );
}
