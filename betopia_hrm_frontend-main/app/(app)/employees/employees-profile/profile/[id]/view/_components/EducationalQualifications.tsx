'use client';
import React from 'react';
import { PiMedalLight } from 'react-icons/pi';
import { RiGraduationCapLine } from 'react-icons/ri';
import EducationalCard from './EducationalCard';
import InfoCard from './InfoCard';

const EducationalQualifications: React.FC = () => {
  const formData = [
    {
      id: 1,
      header: 'Master of Science in Software Engineering — MIT',


      changeText: 'Highest Degree',
      bt_icons: [<PiMedalLight key="medal" size={16} />],


      dataList: [
        { title: 'Field of Study', value: 'Software Engineering' },
        { title: 'Result', value: 'CGPA 3.90 / 4.00' },
        { title: 'Duration', value: '2019 – 2021' },
        { title: 'Thesis', value: 'Fault-tolerant Microservices' },
        { title: 'Thesis', value: 'Fault-tolerant Microservices' },
        { title: 'Thesis', value: 'Fault-tolerant Microservices' },
      ],
      TechnologiesUsed: 'Technologies Used',
      tags: ['Java', 'Spring Boot', 'Postgres', 'Kafka'],
      remarks: 'Remarks',
      remarksText: 'Graduated with distinction; TA for Distributed Systems.',
      // optional button
      buttonBg: '#16a34a',
      buttonText: '#fff',
      icon_color: 'bg-[#F69348]',
      buttonProps: { children: 'Verify', onClick: () => {} },
    },
    // {
    //   id: 2,
    //   header: 'B.Sc. in Computer Science — BUET',


    //   changeText: ' Degree',
    //   bt_icons: [<PiMedalLight key="medal" size={16} />],
      
    //   dataList: [
    //     {
    //       title: 'Field of Study',
    //       value: 'Computer Science',
    //       icons: <TbCertificate size={16} />,
    //     },
    //     { title: 'Result', value: 'CGPA 3.75 / 4.00' },
    //     { title: 'Duration', value: '2015 – 2019' },
    //   ],
    //   TechnologiesUsed: 'Technologies Used',
    //   tags: ['C', 'C++', 'Algorithms', 'DBMS'],
    //   remarks: 'Remarks',
    //   remarksText: 'Team lead, ACM ICPC regional participant.',
    //   // optional button
    //   buttonBg: '#16a34a',
    //   buttonText: '#fff',
    //   icon_color: 'bg-[#F69348]',
    //   buttonProps: { children: 'Verify', onClick: () => {} },
    // },
  ];

  return (
    <div>
      <InfoCard
        label="Educational Qualifications"
        description="Degrees, institutions and results"
        labelIcon={<RiGraduationCapLine size={18} />}
      >
        <div className="grid grid-cols-1  gap-6 py-5 ">
          {formData.map(x => (
            <EducationalCard
              icon_color={x.icon_color}
              key={x.id}
              header={x.header}
              changeText={x.changeText}
              bt_icons={x.bt_icons}
              dataList={x.dataList}
              TechnologiesUsed={x.TechnologiesUsed}
              tags={x.tags}
              remarks={x.remarks}
              remarksText={x.remarksText}
              buttonBg={x.buttonBg}
              buttonText={x.buttonText}
              buttonProps={x.buttonProps}
            />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};

export default EducationalQualifications;
