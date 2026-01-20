import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { LoadingOverlay } from '@mantine/core';
import { useParams } from 'next/navigation';
import { IoAlertCircleOutline, IoChatbubblesOutline } from 'react-icons/io5';
import { LuPhone } from 'react-icons/lu';
import { MdOutlineEmail } from 'react-icons/md';
import { SlPhone } from 'react-icons/sl';
import InfoCard from './InfoCard';
import InfoFrom from './InfoFrom';

const ContactPage = () => {
  const { id }: { id: string } = useParams();
  const { data, isLoading } = useGetEmployeeByIdQuery({ id: Number(id) });

  const formData = [
    {
      id: 1,
      title: 'Personal Email',
      value: data?.data?.email,
      icons: <MdOutlineEmail size={14} />,
    },
    {
      id: 2,
      title: 'Office Phone',
      value: '+1 (555) 765-4321',
      icons: <LuPhone size={14} />,
    },
    {
      id: 3,
      title: 'Office Email',
      value: 'john.anderson@betopia.com',
      icons: <MdOutlineEmail size={14} />,
    },
    {
      id: 4,
      title: 'WhatsApp',
      value: 'john.anderson@betopia.com',
      icons: <IoChatbubblesOutline size={14} />,
    },
    {
      id: 5,
      title: 'Mobile Number',
      value: data?.data?.phone,
      icons: <LuPhone size={14} />,
    },
    {
      id: 6,
      title: 'Emergency Contact',
      value: data?.data?.emergencyContactPhone,
      icons: <IoAlertCircleOutline size={14} />,
    },
  ];

  if (isLoading) return <LoadingOverlay />;

  return (
    <div>
      <InfoCard
        label="Contact Information"
        description=""
        labelIcon={<SlPhone size={18} />}
      >
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-10 py-6">
          {formData.map(x => (
            <InfoFrom
              key={x.id}
              title={x.title}
              value={x.value}
              icons={x.icons}
            />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default ContactPage;
