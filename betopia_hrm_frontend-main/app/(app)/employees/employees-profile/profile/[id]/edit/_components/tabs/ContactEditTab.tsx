// PersonalPage.tsx
'use client';

import SaveConfirmation from '@/components/common/form/SaveConfirmation';
import { EmployeeUpdate } from '@/lib/types/employee/index';
import {
  useGetEmployeeByIdQuery,
  useUpdateEmployeeMutation,
} from '@/services/api/employee/employeeProfileAPI';
import { Textarea, TextInput } from '@mantine/core';
import { useForm } from '@mantine/form';
import { useParams } from 'next/navigation';
import { useEffect } from 'react';
import { AiFillHome } from 'react-icons/ai';
import { BsTelephone } from 'react-icons/bs';
import { CiMail } from 'react-icons/ci';
import { TbHomeInfinity } from 'react-icons/tb';
import InfoCard from '../../../view/_components/InfoCard';
import { getEmployeeApiData, getEmployeeFormData } from '../../_utils';

const ContactEditTab = () => {
  const { id }: { id: string } = useParams();
  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });
  const form = useForm<EmployeeUpdate>({
    mode: 'controlled',
    // initialValues: data?.data ? getEmployeeFormData(data?.data) : {},
    validate: {
      email: value => !value && 'Email is required',
      phone: value => !value && 'Phone is required',
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
    if (!isValid) throw new Error('Validation error');
    const data = getEmployeeApiData(form.getValues());
    await updateEmployee({
      id: Number(id),
      data,
    }).unwrap();
  };

  return (
    <div className=" space-y-10">
      <InfoCard
        label="Contact Information"
        description="Email addresses and phone numbers"
        labelIcon={<BsTelephone size={18} />}
      >
        <div>
          {/* Row 1 */}
          <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
            <TextInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <CiMail size={16} />
                  <span className="text-[#314158]">Personal Email</span>
                </span>
              }
              placeholder="Enter personal email"
              variant="default"
              size="sm"
              type="email"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('email')}
            />

            <TextInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <CiMail size={16} />
                  <span className="text-[#314158]">Office Email</span>
                </span>
              }
              placeholder="Enter Office Email"
              variant="default"
              size="sm"
              type="email"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('officeEmail')}
            />

            <TextInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <BsTelephone size={18} />
                  <span className="text-[#314158]">Mobile No.</span>
                </span>
              }
              placeholder="Enter Mobile number"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('phone')}
            />
            <TextInput
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <BsTelephone size={18} />
                  <span className="text-[#314158]">Office Phone No.</span>
                </span>
              }
              placeholder="Enter office phone number"
              variant="default"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              {...form.getInputProps('officePhone')}
            />
          </div>
        </div>
      </InfoCard>
      <InfoCard label="Address" description="present & permanent address">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <Textarea
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <TbHomeInfinity size={18} />
                <span className="text-[#314158]">Present Address</span>
              </span>
            }
            placeholder="Enter Present Address"
            variant="default"
            size="sm"
            radius="md"
            classNames={{
              input:
                'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
            }}
            {...form.getInputProps('presentAddress')}
          />
          <Textarea
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <AiFillHome size={18} />
                <span className="text-[#314158]">Permanent Address</span>
              </span>
            }
            placeholder="Enter Permanent Address"
            variant="default"
            size="sm"
            radius="md"
            classNames={{
              input:
                'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
            }}
            {...form.getInputProps('permanentAddress')}
          />
        </div>
      </InfoCard>
      {/* <NonMVP>
        <>
          <div className="py-5">
            <SocialMedia />
          </div>

          <div className="py-5">
            <PresentEditAddress />
          </div>
        </>
      </NonMVP> */}
      <SaveConfirmation onSubmit={handleSubmit} />
    </div>
  );
};

export default ContactEditTab;
