import React from 'react';
import InfoCard from './InfoCard';
import { IoDocumentTextOutline } from 'react-icons/io5';
import PreviousWorkCard from './PreviousWorkCard';

const PreviousWorkExperience = () => {
  const promotionsData = [
    {
      id: 101,
      header: 'Software Engineer at Digital Solutions Ltd.',
      changeText: '2 years',

      title: 'Location',
      value: 'Jane Smith',
      label: 'Duration',
      values: 'January 15, 2020 - December 31, 2021',

      second_title: 'Reporting Manager',
      second_value: 'Jane Smith',
      second_label: 'Reason for Leaving',
      second_values: 'Career Growth',

      responsibilities: 'Key Responsibilities',

      responsibilitiesText: [
        'Developed responsive web applications using React and Node.js',
        'Led a team of 3 junior developers',
        'Implemented automated testing and CI/CD pipelines',
        'Collaborated with product managers and designers',
      ],

      achievements: 'Key Achievements',
      achievementsText: ['Increased application performance by 40%','Reduced deployment time by 60%','Mentored 5+ junior developers'],

      TechnologiesUsed: 'Technologies Used',

      tags: ['React', 'Node.js', 'MongoDB', 'AWS'],

      buttonBg: '#F1F5F9',
      buttonText: '#1D293D',
    },
    {
      id: 102,
      header: 'Junior Developer at Innovation Hub',
      changeText: '1.5 years',

      title: 'Austin, TX',
      value: 'Mike Johnson',
      label: 'Duration',
      values: 'January 15, 2020 - December 31, 2021',

      second_title: 'Reporting Manager',
      second_value: 'Jane Smith',
      second_label: 'Reason for Leaving',
      second_values: 'Career Growth',

      responsibilities: 'Key Responsibilities',

      responsibilitiesText: [
        'Developed responsive web applications using React and Node.js',
        'Led a team of 3 junior developers',
        'Implemented automated testing and CI/CD pipelines',
        'Collaborated with product managers and designers',
      ],

      achievements: 'Key Achievements',
      achievementsText: ['Increased application performance by 40%','Reduced deployment time by 60%','Mentored 5+ junior developers'],

      TechnologiesUsed: 'Technologies Used',

      tags: ['HTML5', 'CSS3', 'JavaScript', 'jQuery','Bootstrap','React', 'Node.js'],

      buttonBg: '#F1F5F9',
      buttonText: '#1D293D',
    },
  ];

  return (
    <div>
      <InfoCard
        label="Previous Work Experience"
        description=""
        labelIcon={<IoDocumentTextOutline size={18} />}
      >
        <div className=" flex flex-col gap-4 py-3">
          {promotionsData.map(x => (
            <PreviousWorkCard
              key={x.id}
              header={x.header}
              changeText={x.changeText}
              title={x.title}
              value={x.value}
              label={x.label}
              values={x.values}
              second_title={x.second_title}
              second_value={x.second_value}
              second_label={x.second_label}
              second_values={x.second_values}
              responsibilities={x.responsibilities}
              responsibilitiesText={x.responsibilitiesText}
              achievements={x.achievements}
              achievementsText={x.achievementsText}
              TechnologiesUsed={x.TechnologiesUsed}
              tags={x.tags}
              buttonBg={x.buttonBg}
              buttonText={x.buttonText}
            />
          ))}
        </div>
      </InfoCard>
    </div>
  );
};
export default PreviousWorkExperience;
