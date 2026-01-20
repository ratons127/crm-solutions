// 'use client';

// import { Tabs } from '@mantine/core';
// import { BsPerson } from 'react-icons/bs';
// import { FaRegAddressCard } from 'react-icons/fa6';
// import { LuPhone } from 'react-icons/lu';
// import { PiBagSimpleLight } from 'react-icons/pi';
// import { RiGraduationCapLine } from 'react-icons/ri';

// import ContactInformation from '../../edit/_components/ContactInformation';
// import EducationalEditQualifications from '../../edit/_components/EducationalEditQualifications';
// import EmploymentEditDetails from '../../edit/_components/EmploymentDetails';
// import PersonalDetails from '../../edit/_components/PersonalDetails';
// import ProfessionalEditCertifications from '../../edit/_components/ProfessionalEditCertifications';
// import DocumentEditTab from '../../edit/_components/tabs/document-tab';
// const ProfileEditNavbar = () => {
//   return (
//     <div>
//       <Tabs variant="pills" radius="md" defaultValue="Personal">
//         {/* ---- Tabs Header ---- */}
//         <div className="bg-[#D9D9D9] w-full py-1 rounded-lg">
//           <Tabs.List justify="space-around">
//             {[
//               { value: 'Personal', icon: <BsPerson size={12} /> },
//               { value: 'Contact', icon: <LuPhone size={12} /> },
//               { value: 'Employment', icon: <PiBagSimpleLight size={12} /> },
//               { value: 'Documents', icon: <FaRegAddressCard size={12} /> },
//               { value: 'Education', icon: <RiGraduationCapLine size={12} /> },
//             ].map(tab => (
//               <Tabs.Tab
//                 key={tab.value}
//                 value={tab.value}
//                 px="xl"
//                 leftSection={tab.icon}
//                 classNames={{
//                   tab: 'data-[active=true]:bg-[#F69348] data-[active=true]:text-white',
//                 }}
//               >
//                 {tab.value}
//               </Tabs.Tab>
//             ))}
//           </Tabs.List>
//         </div>

//         {/* ---- Tabs Panels ---- */}
//         <div className="py-8">
//           {/* Personal */}
//           <Tabs.Panel value="Personal">
//             <div className="py-5">
//               <PersonalDetails />
//             </div>
//           </Tabs.Panel>

//           {/* Contact */}
//           <Tabs.Panel value="Contact">
//             <div className="py-5">
//               <ContactInformation />
//             </div>
//           </Tabs.Panel>

//           {/* Employment */}
//           <Tabs.Panel value="Employment">
//             <div className="py-5">
//               <EmploymentEditDetails />
//             </div>
//           </Tabs.Panel>

//           {/* Documents */}
//           <Tabs.Panel value="Documents">
//             <DocumentEditTab />
//           </Tabs.Panel>

//           {/* Education */}
//           <Tabs.Panel value="Education">
//             <div className="py-5">
//               <EducationalEditQualifications />
//             </div>
//             <div className="py-5">
//               <ProfessionalEditCertifications />
//             </div>
//           </Tabs.Panel>
//         </div>
//       </Tabs>
//     </div>
//   );
// };

// export default ProfileEditNavbar;
