import { getDuration, valueWithFallback } from '@/lib/utils/helpers';
import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { LoadingOverlay } from '@mantine/core';
import { useParams } from 'next/navigation';
import { PiBagSimpleFill } from 'react-icons/pi';
import InfoCard from './InfoCard';
import InfoForm from './InfoFrom';

const EmploymentDetails = () => {
  const { id }: { id: string } = useParams();
  const { data, isLoading } = useGetEmployeeByIdQuery({ id: Number(id) });
  const { months = 0, years = 0 } = data?.data?.dateOfJoining
    ? getDuration(data?.data?.dateOfJoining as string)
    : {};

  const formData = [
    {
      id: 1,
      title: 'Employee ID: ',
      value: valueWithFallback(data?.data?.employeeSerialId),
    },
    {
      id: 2,
      title: 'Date of Joining',
      value: valueWithFallback(data?.data?.dateOfJoining),
    },
    {
      id: 3,
      title: 'Department:',
      value: valueWithFallback(data?.data?.departmentId?.name),
    },
    {
      id: 4,
      title: 'Designation:',
      value: valueWithFallback(data?.data?.designationId?.name),
    },
    {
      id: 5,
      title: 'Confirmation Date',
      value: valueWithFallback(data?.data?.actualConfirmationDate),
    },
    {
      id: 6,
      title: 'Team',
      value: valueWithFallback(data?.data?.teamId?.name),
    },
    {
      id: 7,
      title: 'Job Grade',
      value: valueWithFallback(data?.data?.gradeId?.name),
    },
    {
      id: 8,
      title: 'Company Tenure:',
      value: valueWithFallback(`${months} Month ${years} Year`),
    },
    { id: 9, title: 'Office Location', value: valueWithFallback() },
    {
      id: 10,
      title: 'Employment Type',
      value: valueWithFallback(data?.data?.employeeTypeId?.name),
    },
    {
      id: 11,
      title: 'Reporting Manager',
      value: valueWithFallback(data?.data?.supervisorId?.firstName),
    },
    // {
    //   id: 12,
    //   title: 'Working Hours',
    //   value: valueWithFallback('9:00 AM - 6:00 PM'), // missing
    // },
  ];

  if (isLoading) return <LoadingOverlay />;

  return (
    <div>
      <InfoCard
        label="Current Employment Details"
        description=""
        labelIcon={<PiBagSimpleFill size={18} />}
      >
        {data ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10 py-6">
            {formData.map(x => (
              <InfoForm key={x.id} title={x.title} value={x.value} />
            ))}
          </div>
        ) : (
          <p>Employee data not available</p>
        )}
      </InfoCard>
    </div>
  );
};

export default EmploymentDetails;
