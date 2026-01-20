import { valueWithFallback } from '@/lib/utils/helpers';
import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { LoadingOverlay } from '@mantine/core';
import { useParams } from 'next/navigation';
import { BiShield } from 'react-icons/bi';
import InfoCard from './InfoCard';
import InfoForm from './InfoFrom';

const GovernmentDocuments = () => {
  const { id }: { id: string } = useParams();
  const { data, isLoading } = useGetEmployeeByIdQuery({ id: Number(id) });
  const formData = [
    {
      id: 1,
      title: 'Identification Number',
      value: valueWithFallback(data?.data?.nationalId),
    },
    {
      id: 2,
      title: 'Passport',
      value: valueWithFallback(data?.data?.passportNumber),
    },
    {
      id: 3,
      title: 'Driving License',
      value: valueWithFallback(data?.data?.drivingLicenseNumber),
    },
    {
      id: 4,
      title: 'TIN Number',
      value: valueWithFallback(data?.data?.tinNumber),
    },
    {
      id: 5,
      title: 'Birth Certificate:',
      value: valueWithFallback(data?.data?.birthCertificateNumber),
    },
  ];

  if (isLoading) return <LoadingOverlay />;

  return (
    <div>
      <InfoCard
        label="Government Documents"
        description=""
        labelIcon={<BiShield size={18} />}
      >
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10 py-6">
          {formData.map(x => (
            <InfoForm key={x.id} title={x.title} value={x.value} />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};
export default GovernmentDocuments;
