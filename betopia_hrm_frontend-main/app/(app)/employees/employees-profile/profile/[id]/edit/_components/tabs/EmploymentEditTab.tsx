// PersonalPage.tsx
'use client';

import SaveConfirmation from '@/components/common/form/SaveConfirmation';
import NonMVP from '@/components/guards/mvp';
import { useGetBankListQuery } from '@/lib/features/banks/banksAPI';
import { useGetCompanyListQuery } from '@/lib/features/employment/employmentGroupAPI';
import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
import { EmployeeUpdate } from '@/lib/types/employee/index';
import {
  useGetEmployeeByIdQuery,
  useUpdateEmployeeMutation,
} from '@/services/api/employee/employeeProfileAPI';
import { useGetLookupItemsByCategoryQuery } from '@/services/api/lookup/lookupSetupEntryDetailsAPI';
import { useGetOrganizationStructureListQuery } from '@/services/api/organization/organizationStructureAPI';
import { useGetDesignationListQuery } from '@/services/api/workStructure/designationAPI';
import { useGetGradeListQuery } from '@/services/api/workStructure/gradeAPI';
import { Select, TextInput } from '@mantine/core';
import { DateInput, DatePickerInput } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { useParams } from 'next/navigation';
import { useEffect } from 'react';
import { FaRegCalendarAlt } from 'react-icons/fa';
import { LiaCreditCard } from 'react-icons/lia';
import { LuBuilding2 } from 'react-icons/lu';
import { MdOutlineWatchLater } from 'react-icons/md';
import InfoCard from '../../../view/_components/InfoCard';
import { getEmployeeFormData } from '../../_utils';

const EmploymentEditTab = () => {
  const { id }: { id: string } = useParams();
  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });

  console.log(data?.data, 'E_+DATA ');

  const { data: probationDuration } = useGetLookupItemsByCategoryQuery({
    type: 'probationDuration',
  });
  const { data: paymentTypeData } = useGetLookupItemsByCategoryQuery({
    type: 'paymentType',
  });

  const { data: gradeData } = useGetGradeListQuery(undefined);
  const { data: designationData } = useGetDesignationListQuery(undefined);

  const { data: employmentTypeData } = useGetEmploymentTypesQuery(undefined);
  const { data: bankData } = useGetBankListQuery(undefined);

  const form = useForm<EmployeeUpdate>({
    validate: {
      companyId: value => !value && 'Company is required',
      businessUnitId: value => !value && 'Business unit required',
      workplaceGroupId: value => !value && 'Workplace Group required',
      workPlaceId: value => !value && 'Workplace required',
    },
  });

  useEffect(() => {
    if (data?.data) {
      form.setValues(getEmployeeFormData(data?.data));
    }

    // form.setValues({
    //   firstName: data?.data?.firstName ?? '',
    //   lastName: data?.data?.lastName ?? '',
    //   phone: data?.data?.phone,
    //   email: data?.data?.email,
    //   nationalId: data?.data?.nationalId,
    //   dob: data?.data?.dob,
    //   // MUST
    //   employeeTypeId: data?.data?.employeeTypeId?.toString(), //
    //   dateOfJoining: data?.data?.dateOfJoining ?? '',
    //   probationDurationId: data?.data?.probationDurationId?.id?.toString(), //
    //   estimatedConfirmationDate: data?.data?.estimatedConfirmationDate,
    //   actualConfirmationDate: data?.data?.actualConfirmationDate,
    //   gradeId: data?.data?.gradeId?.id?.toString(),
    //   designationId: data?.data?.designationId?.id?.toString(),
    //   paymentTypeId: data?.data?.paymentTypeId?.id?.toString(),

    //   // ======== MISSING FIELDS =======
    //   // bankName:"",
    //   // bankBranch:"",
    //   // accountNumber:"",
    //   // routingNumber:"",

    //   // needs to be render the object in backend.
    //   companyId: data?.data?.companyId?.toString() ?? '',
    //   businessUnitId: data?.data?.businessUnitId?.toString() ?? '',
    //   workplaceGroupId: data?.data?.workplaceGroupId?.toString() ?? '',
    //   workPlaceId: data?.data?.workPlaceId?.toString() ?? '',
    //   departmentId: data?.data?.departmentId?.toString() ?? '',
    //   teamId: data?.data?.teamId?.toString() ?? '',
    // });
  }, [data]);

  const [updateEmployee] = useUpdateEmployeeMutation();

  const handleSubmit = async () => {
    form.validate();
    const isValid = form.isValid();
    if (!isValid) throw new Error('Validation Error');

    const data = form.getValues();

    await updateEmployee({
      id: Number(id),
      data: {
        ...data,
        paymentTypeId: Number(data?.paymentTypeId),
        probationDurationId: Number(data?.probationDurationId),
        gradeId: Number(data?.gradeId),
        //
        companyId: Number(data?.companyId),
        businessUnitId: Number(data?.businessUnitId),
        workplaceGroupId: Number(data?.businessUnitId),
        workPlaceId: Number(data?.workPlaceId),
        departmentId: data?.departmentId ? Number(data?.departmentId) : null,
        teamId: data?.teamId ? Number(data?.teamId) : null,
      },
    }).unwrap();
  };

  const { data: companyListData } = useGetCompanyListQuery(undefined);
  const { data: organizationalStructuredData } =
    useGetOrganizationStructureListQuery({
      companyId: form.values.companyId,
      businessUnitId: form.values.businessUnitId,
      departmentId: form.values.departmentId ?? '',
      workplaceGroupId: form.values.workplaceGroupId,
      workplaceId: form.values.workPlaceId,
    });

  return (
    <div className="  space-y-5">
      <InfoCard
        label="Employment Details"
        description="Job information and organizational structure"
        labelIcon={
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="26"
            height="26"
            viewBox="0 0 26 26"
            fill="none"
          >
            <path
              d="M17.2608 21.3416V4.43726C17.2608 3.87685 17.0381 3.33939 16.6419 2.94311C16.2456 2.54684 15.7081 2.32422 15.1477 2.32422H10.9216C10.3612 2.32422 9.82376 2.54684 9.42749 2.94311C9.03122 3.33939 8.80859 3.87685 8.80859 4.43726V21.3416"
              stroke="white"
              strokeWidth="2.11304"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <path
              d="M21.4861 6.55078H4.58179C3.41479 6.55078 2.46875 7.49682 2.46875 8.66382V19.229C2.46875 20.396 3.41479 21.3421 4.58179 21.3421H21.4861C22.6531 21.3421 23.5992 20.396 23.5992 19.229V8.66382C23.5992 7.49682 22.6531 6.55078 21.4861 6.55078Z"
              stroke="white"
              strokeWidth="2.11304"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      >
        <div>
          {/* Row 1 */}
          <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
            {/* <TextInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <IoPersonOutline size={16} />
                  <span className="text-[#314158]">Employee ID</span>
                </span>
              }
              placeholder="Enter employee ID"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
            /> */}

            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Employee Type</span>
                </span>
              }
              placeholder="Select employee type"
              variant="default"
              size="sm"
              radius="md"
              data={employmentTypeData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('employeeTypeId')}
            />
            <DatePickerInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <FaRegCalendarAlt size={16} />
                  <span className="text-[#314158]">Date of Joining</span>
                </span>
              }
              placeholder="Date of joining"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('dateOfJoining')}
            />
            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Probation Duration</span>
                </span>
              }
              placeholder="Select probation duration"
              data={probationDuration?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('probationDurationId')}
            />
            <DateInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <MdOutlineWatchLater size={16} />
                  <span className="text-[#314158]">Est. Confirmation Date</span>
                </span>
              }
              placeholder="Calculated automatically"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('estimatedConfirmationDate')}
            />
            <DateInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">
                    Actual Confirmation Date
                  </span>
                </span>
              }
              placeholder="Calculated automatically"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('actualConfirmationDate')}
            />
            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Job Grade</span>
                </span>
              }
              placeholder="Select job grader"
              data={gradeData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('gradeId')}
            />
            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Designation</span>
                </span>
              }
              placeholder="Select designation"
              data={designationData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('designationId')}
            />
            <Select
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Payment Type</span>
                </span>
              }
              placeholder="Cash or Bank"
              data={paymentTypeData?.data?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('paymentTypeId')}
            />
          </div>
        </div>
      </InfoCard>
      <NonMVP>
        <InfoCard
          label="Banking Information"
          description="Bank details for salary processing"
          labelIcon={<LiaCreditCard size={18} />}
        >
          <div>
            {/* Row 1 */}
            <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
              <Select
                label={
                  <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                    <span className="text-[#314158]">Bank Name</span>
                  </span>
                }
                placeholder="Select bank"
                variant="default"
                size="sm"
                radius="md"
                data={bankData?.data?.map(x => ({
                  label: x.name,
                  value: x.id.toString(),
                }))}
                // value={bankingInformation.bankName}
                // onChange={v => {
                //   onChange(v, 'bankName');
                // }}
                classNames={{
                  input:
                    'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                }}
              />
              <TextInput
                required
                label={
                  <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                    <span className="text-[#314158]">Bank Branch</span>
                  </span>
                }
                placeholder="Enter branch name"
                variant="default"
                size="sm"
                radius="md"
                classNames={{
                  input:
                    'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                }}
                // value={bankingInformation.bankBranch}
                // onChange={v => {
                //   onChange(v.target.value, 'bankBranch');
                // }}
              />
              <TextInput
                required
                label={
                  <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                    <span className="text-[#314158]">Account Number</span>
                  </span>
                }
                placeholder="Enter account number"
                variant="default"
                size="sm"
                radius="md"
                classNames={{
                  input:
                    'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                }}
                // value={bankingInformation.accountNumber}
                // onChange={v => {
                //   onChange(v.target.value, 'accountNumber');
                // }}
              />
              <TextInput
                required
                label={
                  <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                    <span className="text-[#314158]">Routing Number</span>
                  </span>
                }
                placeholder="Enter account routing number"
                variant="default"
                size="sm"
                radius="md"
                classNames={{
                  input:
                    'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
                }}
                // value={bankingInformation.routingNumber}
                // onChange={v => {
                //   onChange(v.target.value, 'routingNumber');
                // }}
              />
            </div>
          </div>
        </InfoCard>
      </NonMVP>

      <InfoCard
        label="Organizational Structure"
        description="Company hierarchy and team assignments"
        labelIcon={<LuBuilding2 size={18} />}
      >
        <div>
          {/* Row 1 */}
          <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
            <Select
              withAsterisk
              clearable
              onClear={() => {
                form.setValues({
                  businessUnitId: '',
                  workplaceGroupId: '',
                  workPlaceId: '',
                  departmentId: '',
                  teamId: '',
                });
              }}
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Company Name</span>
                </span>
              }
              placeholder="Select company"
              variant="default"
              size="sm"
              radius="md"
              data={companyListData?.data?.map(x => ({
                label: x.name,
                value: x.id?.toString(),
              }))}
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('companyId')}
            />
            <Select
              required
              clearable
              onClear={() => {
                form.setValues({
                  workplaceGroupId: '',
                  workPlaceId: '',
                  departmentId: '',
                  teamId: '',
                });
              }}
              onFocus={() => {
                form.setValues({
                  businessUnitId: '',
                });
              }}
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Business Unit</span>
                </span>
              }
              placeholder="Select business unit"
              variant="default"
              size="sm"
              radius="md"
              data={organizationalStructuredData?.data?.businessUnits?.map(
                x => ({ label: x.name, value: x.id.toString() })
              )}
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('businessUnitId')}
            />

            <Select
              key={'workplace-group'}
              required
              clearable
              onClear={() => {
                form.setValues({
                  workPlaceId: '',
                  departmentId: '',
                  teamId: '',
                });
              }}
              onBlur={() => {
                form.setValues({
                  workPlaceId: '',
                  departmentId: '',
                  teamId: '',
                });
              }}
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Workplace Group</span>
                </span>
              }
              placeholder="Enter workplace group"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              data={organizationalStructuredData?.data?.workplaceGroups?.map(
                x => ({ label: x.name, value: x.id.toString() })
              )}
              {...form.getInputProps('workplaceGroupId')}
            />
            <Select
              key={'workplace'}
              clearable
              onClear={() => {
                form.setValues({
                  departmentId: '',
                  teamId: '',
                });
              }}
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Workplace</span>
                </span>
              }
              placeholder="Enter workplace"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              data={organizationalStructuredData?.data?.workplaces?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              {...form.getInputProps('workPlaceId')}
            />
            <Select
              key={'department'}
              clearable
              onClear={() => {
                form.setValues({
                  teamId: '',
                });
              }}
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Department</span>
                </span>
              }
              placeholder="Select Department"
              variant="default"
              size="sm"
              radius="md"
              data={organizationalStructuredData?.data?.departments?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('departmentId')}
            />
            <Select
              key={'team'}
              clearable
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Team</span>
                </span>
              }
              placeholder="Select team"
              variant="default"
              size="sm"
              radius="md"
              data={organizationalStructuredData?.data?.teams?.map(x => ({
                label: x.name,
                value: x.id.toString(),
              }))}
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('teamId')}
            />
          </div>
        </div>
      </InfoCard>

      {/* Non MVP */}
      {/* <NonMVP>
        <div className="py-5">
          <PromotionEditHistory />
        </div>
        <div className="py-5">
          <TransferEditHistory />
        </div>
      </NonMVP> */}

      <SaveConfirmation onSubmit={handleSubmit} />
    </div>
  );
};

export default EmploymentEditTab;
