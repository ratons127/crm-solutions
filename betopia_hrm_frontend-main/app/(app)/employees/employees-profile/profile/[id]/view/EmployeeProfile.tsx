'use client';

import ConfigPageLayout from '@/(app)/administration/ConfigPageLayout';
import PersonalInformationForm from './PersonalInformationForm';
import WorkstationDetailsForm from './WorkstationDetailsForm';
import EmploymentDetailsForm from './EmploymentDetailsForm';
import ContactInformationForm from './ContactInformationForm';
import IdentificationForm from './IdentificationForm';

export default function EmployeeProfile() {
  return (
    <ConfigPageLayout
      title="Profile"
      sections={[
        {
          id: 'personal',
          title: 'Personal Information',
          content: <PersonalInformationForm />,
        },
        {
          id: 'contact ',
          title: 'Contact Information',
          content: <ContactInformationForm />,
        },
        {
          id: 'identification',
          title: 'Identification',
          content: <IdentificationForm />,
        },
        {
          id: 'workstation',
          title: 'Workstation Details',
          content: <WorkstationDetailsForm />,
        },
        {
          id: 'employment',
          title: 'Employment Details',
          content: <EmploymentDetailsForm />,
        },
      ]}
    />
  );
}
