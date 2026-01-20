// PersonalPage.tsx
import {
  formatDateLocale,
  getDuration,
  valueWithFallback,
} from '@/lib/utils/helpers';
import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { LoadingOverlay } from '@mantine/core';
import { useParams } from 'next/navigation';
import { GoPerson } from 'react-icons/go';
import InfoCard from './InfoCard';
import InfoFrom from './InfoFrom';

const PersonalPage = () => {
  const { id }: { id: string } = useParams();
  const { data, isLoading } = useGetEmployeeByIdQuery({ id: Number(id) });

  if (isLoading) return <LoadingOverlay />;
  const formData = [
    {
      id: 1,
      title: 'First Name',
      value: valueWithFallback(data?.data?.firstName),
    },
    {
      id: 2,
      title: 'Age',
      value: `${getDuration(data?.data?.dob!).years} years `,
    },
    {
      id: 3,
      title: 'Blood Group',
      value: valueWithFallback(data?.data?.bloodGroupId?.name),
    },
    {
      id: 4,
      title: 'Last Name',
      value: valueWithFallback(data?.data?.lastName),
    },
    {
      id: 5,
      title: 'Marital Status',
      value: valueWithFallback(data?.data?.maritalStatus),
    },
    {
      id: 6,
      title: 'Emergency Contact',
      value: valueWithFallback(data?.data?.phone),
    },
    { id: 7, title: 'Gender', value: valueWithFallback(data?.data?.gender) },
    {
      id: 8,
      title: 'Religion',
      value: valueWithFallback(data?.data?.religionId?.name),
    },
    // { id: 6, title: '', value: '' },
    {
      id: 9,
      title: 'Date of Birth',
      value: valueWithFallback(
        data?.data?.dob ? formatDateLocale(data?.data?.dob) : ''
      ),
    },
    {
      id: 10,
      title: 'Nationality',
      value: valueWithFallback(data?.data?.nationalityId?.name),
    },
  ];

  return (
    <div>
      {/* {JSON.stringify(data?.data, null, 2)} */}
      <InfoCard
        label="Personal Information"
        description=""
        labelIcon={<GoPerson size={18} />}
      >
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 lg:gap-10 py-6">
          {formData.map(x => (
            <InfoFrom key={x.id} title={x.title} value={x.value} />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default PersonalPage;
