// PersonalPage.tsx
'use client';

import { Select, TextInput } from '@mantine/core';
import { DateInput } from '@mantine/dates';

import { EmployeeUpdate } from '@/lib/types/employee/index';
import { getDuration } from '@/lib/utils/helpers';
import {
  useGetEmployeeByIdQuery,
  useUpdateEmployeeMutation,
} from '@/services/api/employee/employeeProfileAPI';
import { useGetLookupItemsByCategoryQuery } from '@/services/api/lookup/lookupSetupEntryDetailsAPI';
import { useForm } from '@mantine/form';
import { useParams } from 'next/navigation';
import { CiLocationOn } from 'react-icons/ci';
import { FaRegCalendarAlt } from 'react-icons/fa';
import { GoPerson } from 'react-icons/go';
import { IoHeartOutline } from 'react-icons/io5';
import { MdOutlineBloodtype, MdOutlineWatchLater } from 'react-icons/md';
import { PiStarThin } from 'react-icons/pi';

import SaveConfirmation from '@/components/common/form/SaveConfirmation';
import { useEffect } from 'react';
import InfoCard from '../../../view/_components/InfoCard';
import { getEmployeeApiData, getEmployeeFormData } from '../../_utils';

const PersonalEditTab = () => {
  const { id }: { id: string } = useParams();
  // const { data: genderData } = useGetLookupItemsByCategoryQuery({
  //   type: 'gender',
  // });
  const { data: bloodGroupData } = useGetLookupItemsByCategoryQuery({
    type: 'bloodGroup',
  });
  const { data: religionData } = useGetLookupItemsByCategoryQuery({
    type: 'religion',
  });
  const { data: nationalityData } = useGetLookupItemsByCategoryQuery({
    type: 'nationality',
  });
  // const { data: maritalStatusData } = useGetLookupItemsByCategoryQuery({
  //   type: 'maritalStatus',
  // });

  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });
  const form = useForm<EmployeeUpdate>({
    initialValues: data?.data ? getEmployeeFormData(data.data) : {},
    validate: {
      firstName: value => !value && 'First name is required',
      lastName: value => !value && 'Last name is required',
    },
  });

  useEffect(() => {
    if (data?.data) {
      form.setValues(getEmployeeFormData(data?.data));
    }
  }, [data]);

  const [updateEmployee] = useUpdateEmployeeMutation();

  const handleSubmit = async () => {
    form.validate();
    const isValid = form.isValid();
    if (!isValid) throw new Error('Validation Error');
    console.log(form.getValues(), data?.data);
    await updateEmployee({
      id: Number(id),
      data: getEmployeeApiData(form.getValues()),
    }).unwrap();
  };

  const inputClassNames = {
    input:
      'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
  };

  console.log(form.values.bloodGroupId, 'BLODD', bloodGroupData);

  return (
    <div>
      <InfoCard
        label="Personal Information"
        description="Basic personal information and demographics"
        labelIcon={<GoPerson size={18} />}
      >
        <form>
          {/* Row 1 */}
          <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
            <TextInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <GoPerson size={16} />
                  <span className="text-[#314158]">First Name</span>
                </span>
              }
              placeholder="Enter first name"
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('firstName')}
            />

            <TextInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <GoPerson size={16} />
                  <span className="text-[#314158]">Last Name</span>
                </span>
              }
              placeholder="Enter last name"
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('lastName')}
            />

            <TextInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <PiStarThin size={18} />
                  <span className="text-[#314158]">Display Name</span>
                </span>
              }
              placeholder="Enter display name"
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('displayName')}
            />
          </div>

          {/* Row 2 */}
          <div className="grid grid-cols-1 gap-6 py-5 md:grid-cols-4">
            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <GoPerson size={16} />
                  <span className="text-[#314158]">Gender</span>
                </span>
              }
              placeholder="Select gender"
              // data={genderData?.data?.map(x => ({
              //   label: x.name,
              //   value: x.id.toString(),
              // }))}
              data={[
                {
                  label: 'Male',
                  value: 'male',
                },
                {
                  label: 'Female',
                  value: 'female',
                },
                {
                  label: 'Other',
                  value: 'other',
                },
              ]}
              allowDeselect={false}
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('gender')}
            />

            {/* Date of Birth (controlled with normalized Date) */}
            <DateInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <FaRegCalendarAlt size={16} />
                  <span className="text-[#314158]">Date of Birth</span>
                </span>
              }
              placeholder="Select Date of Birth"
              variant="default"
              size="sm"
              radius="md"
              valueFormat="MMMM D, YYYY"
              maxDate={new Date()} // prevent future dates
              classNames={inputClassNames}
              {...form.getInputProps('dob')}
            />

            {/* Age (Years or Days) */}
            <TextInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <MdOutlineWatchLater size={18} />
                  <span className="text-[#314158]">Age</span>
                </span>
              }
              placeholder="Calculated automatically"
              value={`${form?.values?.dob ? getDuration(form?.values?.dob).years : 0} years`}
              readOnly
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
            />

            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#F6339A]">
                  <IoHeartOutline size={16} />
                  <span className="text-[#314158]">Marital Status</span>
                </span>
              }
              placeholder="Select status"
              // data={maritalStatusData?.data?.map(x => ({
              //   label: x.name,
              //   value: x.id?.toString(),
              // }))}
              data={['Single', 'Married', 'Divorced', 'Widowed'].map(x => ({
                label: x,
                value: x,
              }))}
              allowDeselect={false}
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('maritalStatus')}
            />
          </div>

          {/* Row 3 */}
          <div className="grid grid-cols-1 gap-6 py-5 md:grid-cols-3">
            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Religion</span>
                </span>
              }
              placeholder="Select religion"
              data={religionData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              allowDeselect
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('religionId')}
            />

            <Select
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <CiLocationOn size={16} />
                  <span className="text-[#314158]">Nationality</span>
                </span>
              }
              placeholder="Enter nationality"
              variant="default"
              size="sm"
              radius="md"
              data={nationalityData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              classNames={inputClassNames}
              {...form.getInputProps('nationalityId')}
            />

            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <MdOutlineBloodtype size={16} />
                  <span className="text-[#314158]">Blood Group</span>
                </span>
              }
              placeholder="Select blood group"
              data={bloodGroupData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              allowDeselect={false}
              variant="default"
              size="sm"
              radius="md"
              classNames={inputClassNames}
              {...form.getInputProps('bloodGroupId')}
            />
          </div>

          <SaveConfirmation onSubmit={handleSubmit} />
        </form>
      </InfoCard>
    </div>
  );
};

export default PersonalEditTab;
