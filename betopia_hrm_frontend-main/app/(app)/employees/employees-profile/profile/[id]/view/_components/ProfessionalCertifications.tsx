import InfoCard from './InfoCard';
// import InfoForm from './InfoFrom';
import ProfessionalCertificationsCard from './ProfessionalCertificationsCard';

const ProfessionalCertifications = () => {
  const formData = [
    {
      id: 1,
      title: 'AWS Certified Developer ',
      value: 'Active',
      Label: 'Amazon Web Services',
      IssuedValue: 'June 15, 2023',
      ExpiresValue: 'June 15, 2026',
      idValue: 'AWS-',
      DevValue: '2023-001',
    },
    {
      id: 2,
      title: 'React Developer Certification',
      value: 'Active',
      Label: 'Meta',
      IssuedValue: 'June 15, 2023',
      ExpiresValue: 'June 15, 2026',
      idValue: 'AWS-',
      DevValue: '2023-001',
    },
    {
      id: 3,
      title: 'Project Management Professional (PMP) ',
      value: 'Active',
      Label: 'Project Management Institute',
      IssuedValue: 'June 15, 2023',
      ExpiresValue: 'June 15, 2026',
      idValue: 'AWS-',
      DevValue: '2023-001',
    },
  ];

  return (
    <div>
      <InfoCard
        label="Professional Certifications"
        description=""
        // labelIcon={<RiGraduationCapLine  size={18} />}
        labelIcon={
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="26"
            height="26"
            viewBox="0 0 26 26"
            fill="none"
          >
            <path
              d="M16.3186 13.7305L17.9112 22.6928C17.929 22.7983 17.9142 22.9068 17.8687 23.0037C17.8232 23.1005 17.7493 23.1812 17.6567 23.235C17.5641 23.2887 17.4574 23.3129 17.3507 23.3042C17.244 23.2956 17.1425 23.2547 17.0597 23.1868L13.2965 20.3623C13.1148 20.2266 12.8942 20.1533 12.6674 20.1533C12.4406 20.1533 12.2199 20.2266 12.0383 20.3623L8.26875 23.1858C8.18605 23.2535 8.08465 23.2944 7.9781 23.303C7.87154 23.3116 7.7649 23.2876 7.67238 23.234C7.57987 23.1804 7.50589 23.0999 7.46032 23.0032C7.41474 22.9065 7.39973 22.7982 7.4173 22.6928L9.00878 13.7305"
              stroke="white"
              strokeWidth="2.10235"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <path
              d="M12.6625 14.8973C16.1458 14.8973 18.9695 12.0735 18.9695 8.59024C18.9695 5.10696 16.1458 2.2832 12.6625 2.2832C9.17923 2.2832 6.35547 5.10696 6.35547 8.59024C6.35547 12.0735 9.17923 14.8973 12.6625 14.8973Z"
              stroke="white"
              strokeWidth="2.10235"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      >
        <div className="grid grid-cols-1  gap-10 py-6">
          {formData.map(x => (
            <ProfessionalCertificationsCard
              key={x.id}
              title={x.title}
              value={x.value}
              Label={x.Label}
              IssuedValue={x.IssuedValue}
              ExpiresValue={x.ExpiresValue}
              idValue={x.idValue}
              DevValue={x.DevValue}
            />
          ))}

          {/* <ProfessionalCertificationsCard /> */}
        </div>
      </InfoCard>
    </div>
  );
};

export default ProfessionalCertifications;
