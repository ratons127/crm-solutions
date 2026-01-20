import { valueWithFallback } from '@/lib/utils/helpers';
import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { useParams } from 'next/navigation';
import { CiLocationOn } from 'react-icons/ci';
import { LuBuilding2 } from 'react-icons/lu';
import { RiGroupLine } from 'react-icons/ri';
import InfoCard from './InfoCard';
import InfoForm from './InfoFrom';

const OrganizationalStructure = () => {
  const { id }: { id: string } = useParams();
  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });
  const formData = [
    {
      id: 1,
      title: 'Company ',
      value: valueWithFallback(data?.data?.companyId?.name),
      icons: <LuBuilding2 size={14} />,
    },
    {
      id: 2,
      title: 'Workplace Group ',
      value: valueWithFallback(data?.data?.workplaceGroupId?.name),
      icons: <RiGroupLine size={14} />,
    },
    {
      id: 3,
      title: 'Department ',
      value: valueWithFallback(data?.data?.departmentId?.name),
      icons: <LuBuilding2 size={14} />,
    },
    {
      id: 4,
      title: 'Business Unit ',
      value: valueWithFallback(data?.data?.businessUnitId?.name),
      icons: <LuBuilding2 size={14} />,
    },
    {
      id: 5,
      title: 'Workplace ',
      value: valueWithFallback(data?.data?.workPlaceId?.name),
      icons: <CiLocationOn size={14} />,
    },
    {
      id: 6,
      title: 'Team ',
      value: valueWithFallback(data?.data?.teamId?.name),
      icons: <CiLocationOn size={14} />,
    },
    {
      id: 7,
      title: 'Office Address ',
      value: valueWithFallback(data?.data?.companyId?.address),
      icons: <CiLocationOn size={14} />,
    },
    {
      id: 8,
      title: 'Location ',
      value: valueWithFallback(data?.data?.companyId?.address),
      icons: <CiLocationOn size={14} />,
    },
    {
      id: 9,
      title: 'Reporting Manager ',
      value: valueWithFallback(data?.data?.supervisorId?.firstName),
      icons: <CiLocationOn size={14} />,
    },
  ];

  return (
    <div>
      <InfoCard
        label="Organizational Structure"
        description="Company hierarchy and team assignments"
        labelIcon={<LuBuilding2 size={18} />}
      >
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10 py-6">
          {formData.map(x => (
            <InfoForm
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

export default OrganizationalStructure;
