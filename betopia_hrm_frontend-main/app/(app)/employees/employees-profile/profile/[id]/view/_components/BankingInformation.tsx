import { FaRegCreditCard } from 'react-icons/fa6';
import { LuBuilding2 } from 'react-icons/lu';
import { PiBagSimpleFill } from 'react-icons/pi';
import InfoCard from './InfoCard';
import InfoForm from './InfoFrom';

const BankingInformation = () => {
  // const { id }: { id: string } = useParams();
  // const { data } = useGetEmployeeByIdQuery({ id: Number(id) });
  const formData = [
    {
      id: 1,
      title: 'Bank Name ',
      value: 'Chase Bank',
      icons: <FaRegCreditCard size={14} />,
    },
    {
      id: 2,
      title: 'Branch: ',
      value: 'Downtown Branch',
      icons: <LuBuilding2 size={14} />,
    },
    {
      id: 3,
      title: 'Account Number ',
      value: '1234567890',
      icons: <LuBuilding2 size={14} />,
    },
    {
      id: 4,
      title: 'Routing Number: ',
      value: '021000021',
      icons: <LuBuilding2 size={14} />,
    },
  ];

  return (
    <div>
      <InfoCard
        label="Banking Information"
        description="Bank details for salary processing"
        labelIcon={<PiBagSimpleFill size={18} />}
      >
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-10 py-6">
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

export default BankingInformation;
