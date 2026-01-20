// PersonalPage.tsx
'use client';

import { useGetDocumentTypeListQuery } from '@/lib/features/document/documentAPI';
import { useGetFieldOfStudyQuery } from '@/lib/features/qualification/fieldOfStudyAPI';
import { useGetInstituteNameQuery } from '@/lib/features/qualification/instituteNameAPI';
import { useGetQualificationLevelQuery } from '@/lib/features/qualification/qualificationLevelAPI';
import { useGetQualificationRatingQuery } from '@/lib/features/qualification/qualificationRatingAPI';
import { useGetQualificationTypeQuery } from '@/lib/features/qualification/qualificationTypeAPI';
import { EmployeeCertificate } from '@/lib/types/employee/employeeCertificate';
import { EmployeeEducationalQualification } from '@/lib/types/employee/employeeEducationalQualification';
import {
  useCreateEmployeeCertificateMutation,
  useGetEmployeeCertificateListQuery,
  useUpdateEmployeeCertificateMutation,
} from '@/services/api/employee/employeeCertificateAPI';
import { useCreateEmployeeEducationalQualificationMutation } from '@/services/api/employee/employeeQualificationAPI';
import {
  Button,
  Checkbox,
  FileInput,
  Select,
  Textarea,
  TextInput,
} from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { useParams } from 'next/navigation';
import { IoAdd } from 'react-icons/io5';
import InfoCard from '../../../view/_components/InfoCard';

const EducationEditTab = () => {
  // const { id }: { id: string } = useParams();
  const { data: employeeCertificateList } =
    useGetEmployeeCertificateListQuery();

  console.log(employeeCertificateList, 'CERT');

  return (
    <div className=" space-y-5">
      <InfoCard
        label="Educational Qualifications"
        description="Academic background and certifications"
        labelIcon={
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="27"
            height="27"
            viewBox="0 0 27 27"
            fill="none"
          >
            <path
              d="M16.911 14.3164L18.5577 23.5838C18.5761 23.6929 18.5608 23.8051 18.5138 23.9053C18.4668 24.0055 18.3903 24.0889 18.2946 24.1444C18.1989 24.2 18.0885 24.225 17.9781 24.2161C17.8678 24.2072 17.7628 24.1648 17.6773 24.0947L13.786 21.174C13.5981 21.0337 13.3699 20.9578 13.1354 20.9578C12.9009 20.9578 12.6727 21.0337 12.4849 21.174L8.58704 24.0936C8.50152 24.1636 8.39668 24.2059 8.28649 24.2148C8.17631 24.2237 8.06603 24.1988 7.97037 24.1434C7.87471 24.088 7.79821 24.0048 7.75109 23.9048C7.70396 23.8048 7.68844 23.6928 7.70661 23.5838L9.35226 14.3164"
              stroke="white"
              strokeWidth="2.17391"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <path
              d="M13.1311 15.522C16.733 15.522 19.6529 12.6021 19.6529 9.00025C19.6529 5.3984 16.733 2.47852 13.1311 2.47852C9.52926 2.47852 6.60938 5.3984 6.60938 9.00025C6.60938 12.6021 9.52926 15.522 13.1311 15.522Z"
              stroke="white"
              strokeWidth="2.17391"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      >
        <div className="flex flex-col gap-5">
          {employeeCertificateList?.data?.map((_entry, _idx) => (
            <div className="bg-[#F8FAFC]/50 border border-[#E2E8F0] rounded-lg px-5 py-5">
              <div className="flex items-center justify-between">
                <h6 className="text-[18px] text-[#314158] py-1"></h6>
              </div>

              <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
                {/* From Location */}
                <Select
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Qualification Type</span>
                    </span>
                  }
                  placeholder="Select type"
                  variant="default"
                  size="sm"
                  radius="md"
                  allowDeselect
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <Select
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Qualification</span>
                    </span>
                  }
                  placeholder="Select type"
                  variant="default"
                  size="sm"
                  radius="md"
                  allowDeselect
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />

                {/* To Location */}
                <Select
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">
                        Institute Name (Add option required)
                      </span>
                    </span>
                  }
                  placeholder="Enter location"
                  variant="default"
                  size="sm"
                  radius="md"
                  allowDeselect
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <TextInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Field of Study</span>
                    </span>
                  }
                  placeholder="Enter field of study"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <TextInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Subject</span>
                    </span>
                  }
                  placeholder="Enter subject"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <Select
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Rating Method</span>
                    </span>
                  }
                  placeholder="Enter Rating Method"
                  variant="default"
                  size="sm"
                  radius="md"
                  allowDeselect
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <TextInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Result</span>
                    </span>
                  }
                  placeholder="Enter result"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                {/* Transfer Date (Date | null) */}
                <DateInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Passing Year</span>
                    </span>
                  }
                  placeholder="Enter passing year"
                  variant="default"
                  size="sm"
                  radius="md"
                  valueFormat="MMMM D, YYYY"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />

                {/* Reason */}
                <TextInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Duration</span>
                    </span>
                  }
                  placeholder="Enter duration"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <Textarea
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Remarks</span>
                    </span>
                  }
                  placeholder="Any additional remarks"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <div className="py-10">
                  <Checkbox defaultChecked label="Last Education" />
                </div>
              </div>
            </div>
          ))}

          {/* Add new entry */}
          <div className="py-1">
            <Button
              onClick={() => {
                modals.open({
                  centered: true,
                  children: <EmployeeEducationForm action="create" />,
                });
              }}
              leftSection={<IoAdd size={18} color="white" />}
              variant="filled"
              color="#F69348"
              justify="center"
              size="lg"
              radius="md"
              fullWidth
            >
              Add Transfer Record
            </Button>
          </div>
        </div>
      </InfoCard>
      <InfoCard
        label="Professional Certifications"
        description="Professional credentials and industry certifications"
        labelIcon={
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="24"
            viewBox="0 0 20 24"
            fill="none"
          >
            <path
              d="M18.2599 13.2494C18.2599 18.3892 14.662 20.9591 10.3857 22.4497C10.1618 22.5256 9.91852 22.5219 9.69696 22.4394C5.41036 20.9591 1.8125 18.3892 1.8125 13.2494V6.05371C1.8125 5.78108 1.9208 5.51961 2.11358 5.32683C2.30636 5.13405 2.56783 5.02575 2.84046 5.02575C4.89638 5.02575 7.46628 3.7922 9.25493 2.2297C9.47271 2.04364 9.74975 1.94141 10.0362 1.94141C10.3226 1.94141 10.5997 2.04364 10.8174 2.2297C12.6164 3.80248 15.176 5.02575 17.2319 5.02575C17.5045 5.02575 17.766 5.13405 17.9588 5.32683C18.1516 5.51961 18.2599 5.78108 18.2599 6.05371V13.2494Z"
              stroke="white"
              strokeWidth="2.05592"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      >
        <div className="flex flex-col gap-5">
          {employeeCertificateList?.data?.map((_entry, _idx) => (
            <div
              // key={entry.id}
              className="bg-[#F8FAFC]/50 border border-[#E2E8F0] rounded-lg px-5 py-5"
            >
              <div className="flex items-center justify-between">
                <h6 className="text-[18px] text-[#314158] py-1">
                  {/* {countLabel(idx + 1)} */}
                </h6>
              </div>

              <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                {/* From Location */}
                <TextInput
                  required
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Certification Name</span>
                    </span>
                  }
                  placeholder="e.g., AWS Certified Solutions Architect"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <TextInput
                  required
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">
                        Issuing Organization
                      </span>
                    </span>
                  }
                  placeholder="e.g., Amazon Web Services"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />

                {/* Transfer Date (Date | null) */}
                <DateInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Issue Date</span>
                    </span>
                  }
                  placeholder="Select transfer date"
                  variant="default"
                  size="sm"
                  radius="md"
                  valueFormat="MMMM D, YYYY"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />

                {/* Effective Date (Date | null) */}
                <DateInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Expiry Date</span>
                    </span>
                  }
                  placeholder="Select effective date"
                  variant="default"
                  size="sm"
                  radius="md"
                  valueFormat="MMMM D, YYYY"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />

                {/* Reason */}
                <TextInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Credential ID</span>
                    </span>
                  }
                  placeholder="Enter credential ID"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <TextInput
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Credential URL</span>
                    </span>
                  }
                  placeholder="https://..."
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <Textarea
                  label={
                    <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                      <span className="text-[#314158]">Description</span>
                    </span>
                  }
                  placeholder="Brief description of the certification"
                  variant="default"
                  size="sm"
                  radius="md"
                  classNames={{
                    input:
                      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                  }}
                />
                <div className="py-10">
                  <Checkbox
                    defaultChecked
                    label="This certification does not expire"
                  />
                </div>
              </div>
            </div>
          ))}

          {/* Add new entry */}
          <div className="py-1">
            <Button
              leftSection={<IoAdd size={18} color="white" />}
              variant="filled"
              color="#F69348"
              justify="center"
              size="lg"
              radius="md"
              fullWidth
              onClick={() => {
                modals.open({
                  children: <EmployeeCertificateForm action="create" />,
                });
              }}
            >
              Add Transfer Record
            </Button>
          </div>
        </div>
      </InfoCard>
    </div>
  );
};

export default EducationEditTab;

const EmployeeEducationForm = (props: {
  action: 'create' | 'update';
  data?: EmployeeEducationalQualification;
}) => {
  const { id }: { id: string } = useParams();
  const form = useForm<Partial<EmployeeEducationalQualification>>({
    initialValues: props.data
      ? { ...props.data, employeeId: id }
      : { employeeId: id },
    validate: {
      employeeId: value =>
        !value && props.action === 'update' && 'Employee required',
      qualificationLevelId: value => !value && 'Qualification Level required',
      qualificationTypeId: value => !value && 'Qualification Type required',
      instituteNameId: value => !value && 'Institute Name required',
      fieldStudyId: value => !value && 'Field Study required',
      qualificationRatingMethodId: value =>
        !value && 'Qualification Rating Method required',
      subject: value => !value && 'Subject required',
      result: value => !value && 'Result required',
      passingYear: value =>
        !value
          ? 'PassingYear required'
          : new Date().getFullYear() < Number(value)
            ? 'Invalid Year'
            : undefined,
    },
  });

  const fieldOfStudyData = useGetFieldOfStudyQuery(undefined);
  const instituteNameData = useGetInstituteNameQuery(undefined);
  const qualificationTypeData = useGetQualificationTypeQuery(undefined);
  const qualificationLevelData = useGetQualificationLevelQuery(undefined);
  const qualificationRatingMethodData =
    useGetQualificationRatingQuery(undefined);
  const [createEmployeeEQ] =
    useCreateEmployeeEducationalQualificationMutation();

  const handleCloseForm = () => {
    form.reset();
    modals.closeAll();
  };
  const handleSubmit = async (values: typeof form.values) => {
    try {
      await createEmployeeEQ(values).unwrap();
      handleCloseForm();
    } catch (error) {}
  };

  console.log(form.errors);

  return (
    <form
      className="grid grid-cols-1 sm:grid-cols-2 gap-2"
      onSubmit={form.onSubmit(handleSubmit)}
    >
      <h1 className=" col-span-1 sm:col-span-2">
        {props.action === 'create'
          ? 'Add Educational Qualification'
          : 'Update Educational Qualification'}
      </h1>
      <Select
        label="Qualification Type"
        data={qualificationTypeData?.data?.data?.map(x => ({
          label: x.typeName,
          value: x.id.toString(),
        }))}
        {...form.getInputProps('qualificationTypeId')}
      />
      <Select
        data={qualificationLevelData?.data?.data?.map(x => ({
          label: x.levelName,
          value: x.id.toString(),
        }))}
        label="Qualification level"
        {...form.getInputProps('qualificationLevelId')}
      />
      <Select
        label="Institute Name"
        data={instituteNameData?.data?.data?.map(x => ({
          label: x.name,
          value: x.id.toString(),
        }))}
        {...form.getInputProps('instituteNameId')}
      />
      <Select
        data={fieldOfStudyData?.data?.data?.map(x => ({
          label: x.fieldStudyName,
          value: x.id.toString(),
        }))}
        label="Field of Study"
        {...form.getInputProps('fieldStudyId')}
      />
      <Select
        data={qualificationRatingMethodData?.data?.data?.map(x => ({
          label: x.methodName,
          value: x.id.toString(),
        }))}
        label="Qualification rating method"
        {...form.getInputProps('qualificationRatingMethodId')}
      />
      <TextInput label="Subject" {...form.getInputProps('subject')} />
      <TextInput label="Result" {...form.getInputProps('result')} />
      <TextInput label="Passing Year" {...form.getInputProps('passingYear')} />
      <div className=" ml-auto col-span-1 md:col-span-2  space-x-3 mt-5">
        <Button
          onClick={() => {
            handleCloseForm();
          }}
          variant="outline"
        >
          Cancel
        </Button>
        <Button type="submit">Save</Button>
      </div>
    </form>
  );
};

const EmployeeCertificateForm = (props: {
  action: 'create' | 'update';
  data?: EmployeeCertificate;
}) => {
  const { id }: { id: string } = useParams();
  const form = useForm<Partial<EmployeeCertificate>>({
    initialValues: props.data
      ? { ...props.data, employeeId: id }
      : {
          status: '1',
          documentStatus: 'ACTIVE',
          employeeId: id,
        },
    validate: {
      employeeId: value => !value && 'Employee required',
      documentStatus: value => !value && 'Document status required',
      issueDate: value => !value && 'Issue date is required',
      expiryDate: value => !value && 'Expiry date is required',
      documentTypeId: value => !value && 'Document Type is required',
    },
  });

  const { data: documentTypeListData } = useGetDocumentTypeListQuery(undefined);

  const [createEmployeeDocument] =
    useCreateEmployeeCertificateMutation(undefined);
  const [updateEmployeeDocument] =
    useUpdateEmployeeCertificateMutation(undefined);

  const handleCloseForm = () => {
    form.reset();
    modals.closeAll();
  };
  const handleSubmit = async (values: typeof form.values) => {
    try {
      if (props.action === 'create') {
        await createEmployeeDocument(values).unwrap();
      } else if (props.data?.id) {
        await updateEmployeeDocument({
          id: props.data.id,
          data: values,
        }).unwrap();
      }
      handleCloseForm();
      notifications.show({
        message: 'Employee certificate',
        title:
          props.action === 'create'
            ? 'Created successfully'
            : 'Updated successfully',
      });
    } catch (error) {
      notifications.show({
        title: 'Employee certificate',
        message:
          (error as { message: string })?.message ?? 'Something went wrong',
      });
    }
  };

  return (
    <form
      onSubmit={form.onSubmit(handleSubmit)}
      className=" grid grid-cols-1 sm:grid-cols-2 gap-2"
    >
      <h1 className=" col-span-1 sm:col-span-2">
        {props.action === 'create' ? 'Add Certificate' : 'Update Certificate'}
      </h1>
      <Select
        data={documentTypeListData?.data?.map(x => ({
          label: x.name,
          value: x.id.toString(),
        }))}
        label="Document Type"
        {...form.getInputProps('documentTypeId')}
      />
      <DateInput label="Issue Date" {...form.getInputProps('issueDate')} />
      <DateInput
        minDate={
          form.values?.issueDate ? new Date(form.values.issueDate) : undefined
        }
        label="Expire Date"
        {...form.getInputProps('expiryDate')}
      />
      <Select
        data={[
          {
            label: 'ACTIVE',
            value: 'ACTIVE',
          },
          {
            label: 'EXPIRED',
            value: 'EXPIRED',
          },
          {
            label: 'ARCHIVED',
            value: 'ARCHIVED',
          },
        ]}
        label="Document Status"
        {...form.getInputProps('documentStatus')}
      />
      <Select
        data={[
          {
            label: 'Active',
            value: '1',
          },
          {
            label: 'Disable',
            value: '0',
          },
        ]}
        label="Status"
        {...form.getInputProps('status')}
      />
      <FileInput
        label="Document"
        placeholder="Upload your document"
        {...form.getInputProps('file')}
      />
      <div className=" ml-auto col-span-1 md:col-span-2  space-x-3">
        <Button
          onClick={() => {
            handleCloseForm();
          }}
          variant="outline"
        >
          Cancel
        </Button>
        <Button type="submit">Save</Button>
      </div>
    </form>
  );
};
