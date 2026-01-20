import { Tabs } from '@mantine/core';
import { BsPerson } from 'react-icons/bs';
import { FaRegAddressCard } from 'react-icons/fa6';
import { LuPhone } from 'react-icons/lu';
import { PiBagSimpleLight } from 'react-icons/pi';
// import { RiGraduationCapLine } from 'react-icons/ri';
import PersonalPage from './personalPage';

import NonMVP from '@/components/guards/mvp';
import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';
import { useParams } from 'next/navigation';
import BankingInformation from './BankingInformation';
import ContactPage from './contactPage';
import EducationalQualifications from './EducationalQualifications';
import EmploymentDetails from './EmploymentDetails';
import FamilyInfoPages from './FamilyInfoPages';
import GovernmentDocuments from './GovernmentDocuments';
import Hobbies from './Hobbies';
import InfoCard from './InfoCard';
import InfoForm from './InfoFrom';
import Languages from './Languages';
import OrganizationalStructure from './OrganizationalStructure';
import PermanentAddress from './PermanentAddress';
import PresentAddress from './PresentAddress';
import PreviousWorkExperience from './PreviousWorkExperience';
import ProfessionalCertifications from './ProfessionalCertifications';
import PromotionHistory from './PromotionHistory';
import TransferHistory from './TransferHistory';

const ProfileNavbar = () => {
  const { id }: { id: string } = useParams();
  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });

  return (
    <div>
      <Tabs variant="pills" radius="md" defaultValue="Personal">
        {/*  */}
        <div className="bg-[#D9D9D9] w-full py-1 rounded-lg">
          <Tabs.List justify="space-around">
            <Tabs.Tab
              value="Personal"
              color="bg-[#F69348]!"
              px="xl"
              leftSection={<BsPerson size={12} />}
            >
              Personal
            </Tabs.Tab>
            <Tabs.Tab
              value="Contact"
              color="bg-[#F69348]!"
              px="xl"
              leftSection={<LuPhone size={12} />}
            >
              Contact
            </Tabs.Tab>
            <Tabs.Tab
              value="Employment"
              color="bg-[#F69348]!"
              px="xl"
              leftSection={<PiBagSimpleLight size={12} />}
            >
              Employment
            </Tabs.Tab>
            <Tabs.Tab
              value="Documents"
              color="bg-[#F69348]!"
              px="xl"
              leftSection={<FaRegAddressCard size={12} />}
            >
              Documents
            </Tabs.Tab>
            {/* <Tabs.Tab
              value="Education"
              color="bg-[#F69348]!"
              px="xl"
              leftSection={<RiGraduationCapLine size={12} />}
            >
              Education
            </Tabs.Tab> */}
          </Tabs.List>
        </div>
        {/*  */}
        <div className="py-8">
          <Tabs.Panel value="Personal">
            <div className=" space-y-5">
              <PersonalPage />
              <NonMVP>
                <FamilyInfoPages />
                <Hobbies />
              </NonMVP>

              <div className="grid  grid-cols-1  lg:grid-cols-2  gap-5 ">
                <NonMVP>
                  <Languages />
                </NonMVP>
              </div>
            </div>
          </Tabs.Panel>
          <Tabs.Panel value="Contact">
            <div className="py-5">
              <ContactPage />
            </div>

            <NonMVP>
              <div className="grid  grid-cols-1  lg:grid-cols-2  gap-5 ">
                <PresentAddress />
                <PermanentAddress />
              </div>
            </NonMVP>
            <InfoCard
              label="Address"
              description="Present and Permanent Addresses"
            >
              <div className="grid  grid-cols-1  lg:grid-cols-2  gap-5 ">
                <InfoForm
                  title="Present Address"
                  value={data?.data?.presentAddress}
                />
                <InfoForm
                  title="Permanent Address"
                  value={data?.data?.permanentAddress}
                />
              </div>
            </InfoCard>
          </Tabs.Panel>
          <Tabs.Panel value="Employment">
            <div className="space-y-5">
              <EmploymentDetails />
              <BankingInformation />
              <OrganizationalStructure />
            </div>

            <NonMVP>
              <div className="grid  grid-cols-1  lg:grid-cols-2  gap-5 py-5">
                <PromotionHistory />
                <TransferHistory />
              </div>
            </NonMVP>
          </Tabs.Panel>
          <Tabs.Panel value="Documents">
            <div className="py-5">
              <GovernmentDocuments />
            </div>
            <NonMVP>
              <div className="grid  grid-cols-1   gap-5 py-5">
                <PreviousWorkExperience />
              </div>
            </NonMVP>
          </Tabs.Panel>
          <Tabs.Panel value="Education">
            <div className="py-5">
              <EducationalQualifications />
            </div>
            <div className="grid  grid-cols-1   gap-5 py-5">
              <ProfessionalCertifications />
            </div>
          </Tabs.Panel>
        </div>
      </Tabs>
    </div>
  );
};

export default ProfileNavbar;
