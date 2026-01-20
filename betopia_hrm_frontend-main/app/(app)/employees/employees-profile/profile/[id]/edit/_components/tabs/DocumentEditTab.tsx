// PersonalPage.tsx
'use client';

import {
  Button,
  FileInput,
  NumberInput,
  Textarea,
  TextInput,
} from '@mantine/core';

import SaveConfirmation from '@/components/common/form/SaveConfirmation';
import { DBModel } from '@/lib/types';
import { EmployeeUpdate } from '@/lib/types/employee/index';
import {
  useGetEmployeeByIdQuery,
  useUpdateEmployeeMutation,
} from '@/services/api/employee/employeeProfileAPI';
import {
  useCreateEmployeeWorkExperienceMutation,
  useGetEmployeeWorkExperienceListByIdQuery,
  useUpdateEmployeeWorkExperienceMutation,
  useUploadEmployeeWorkExperienceDocumentMutation,
} from '@/services/api/employee/employeeWorkExperience';
import { DateInput } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { useParams } from 'next/navigation';
import { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { IoAdd } from 'react-icons/io5';
import { MdOutlineShield } from 'react-icons/md';
import InfoCard from '../../../view/_components/InfoCard';
import { getEmployeeApiData, getEmployeeFormData } from '../../_utils';

interface WorkExperienceCreate {
  id?: number;
  employeeId: number;
  companyName: string;
  jobTitle: string;
  location: string;
  jobDescription: string;
  fromDate: string;
  toDate: string;
  tenure: number;
  file?: File | null;
}

type WorkExperience = WorkExperienceCreate & DBModel;

export default function DocumentEditTab() {
  const { id }: { id: string } = useParams();
  const { data } = useGetEmployeeByIdQuery({ id: Number(id) });

  const employeeExperienceList = useGetEmployeeWorkExperienceListByIdQuery({
    id: Number(id),
  });

  const [entries, setEntries] = useState<WorkExperienceCreate[]>([]);

  // 🧩 Load fetched experiences
  useEffect(() => {
    if (Array.isArray(employeeExperienceList?.data?.data)) {
      setEntries(employeeExperienceList?.data?.data);
    }
  }, [data]);

  const form = useForm<EmployeeUpdate>({});

  useEffect(() => {
    if (data?.data) {
      form.setValues(getEmployeeFormData(data?.data));
    }
  }, [data]);

  const [updateEmployee] = useUpdateEmployeeMutation();

  // 💾 Save (create or update)

  const handleSubmit = async () => {
    form.validate();
    const isValid = form.isValid();
    if (!isValid) throw new Error('Validation Error');

    await updateEmployee({
      id: Number(id),
      data: getEmployeeApiData(form.getValues()),
    }).unwrap();

    // //  update workExperience
    // for (const wxp of entries) {
    //   let id = wxp.id;
    //   if (id) {
    //     await updateEmployeeWX({
    //       id,
    //       data: {
    //         ...wxp,
    //       },
    //     }).unwrap();
    //     if (wxp.file !== null) {
    //     }
    //   } else {
    //     let { data } = await createEmployeeWX(wxp).unwrap();
    //     id = data?.id;
    //   }

    //   // attach file (upload file)
    // }

    //
  };

  return (
    <div className=" space-y-3">
      <InfoCard
        label="Government ID Numbers"
        description="Official identification documents"
        labelIcon={<MdOutlineShield size={18} />}
      >
        <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
          <TextInput
            required
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <span className="text-[#314158]">NID Number</span>
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
            {...form.getInputProps('nationalId')}
          />

          <TextInput
            required
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <span className="text-[#314158]">
                  Enter Birth Certificate Number
                </span>
              </span>
            }
            placeholder="Enter birth certificate number"
            variant="default"
            size="sm"
            radius="md"
            classNames={{
              input:
                'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
            }}
            {...form.getInputProps('birthCertificateNumber')}
          />
          <TextInput
            required
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <span className="text-[#314158]">Passport Number</span>
              </span>
            }
            placeholder="Enter passport number"
            variant="default"
            size="sm"
            radius="md"
            classNames={{
              input:
                'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
            }}
            {...form.getInputProps('passportNumber')}
          />
          <TextInput
            required
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <span className="text-[#314158]">Driving License Number</span>
              </span>
            }
            placeholder="Enter driving license number"
            variant="default"
            size="sm"
            radius="md"
            classNames={{
              input:
                'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
            }}
            {...form.getInputProps('drivingLicenseNumber')}
          />
          <TextInput
            required
            label={
              <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                <span className="text-[#314158]">TIN Number</span>
              </span>
            }
            placeholder="Enter TIN number"
            variant="default"
            size="sm"
            radius="md"
            classNames={{
              input:
                'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
            }}
            {...form.getInputProps('tinNumber')}
          />
        </div>
      </InfoCard>

      <SaveConfirmation onSubmit={handleSubmit} />

      <WorkExperienceSection
        employeeId={Number(id)}
        entries={entries}
        setEntries={setEntries}
      />
    </div>
  );
}

interface Props {
  employeeId: number;
  entries: WorkExperienceCreate[];
  setEntries: Dispatch<SetStateAction<WorkExperienceCreate[]>>;
}

export function WorkExperienceSection({ entries }: Props) {
  // ➕ Add new entry
  // const addEntry = () => {
  //   const newItem: WorkExperienceCreate = {
  //     employeeId,
  //     companyName: '',
  //     jobTitle: '',
  //     location: '',
  //     jobDescription: '',
  //     fromDate: '',
  //     toDate: '',
  //     tenure: 1,
  //   };
  //   setEntries(prev => [...prev, newItem]);
  // };

  // // 🧭 Update any field in entry
  // const updateEntryField = <K extends keyof WorkExperience>(
  //   index: number,
  //   field: K,
  //   value: WorkExperience[K]
  // ) => {
  //   setEntries(prev => {
  //     const copy = [...prev];
  //     copy[index] = { ...copy[index], [field]: value };
  //     return copy;
  //   });
  // };

  return (
    <InfoCard label="Work Experience" description="Previous employment history">
      <div className="flex flex-col gap-5">
        {entries.map((_entry, _idx) => {
          return (
            <div
              key={`${_entry.id}-${_idx}`}
              className="bg-[#F8FAFC]/50 border border-[#E2E8F0] rounded-lg px-5 py-5"
            >
              <h6 className="text-[18px] text-[#314158] mb-3">
                Work Experience #{_idx + 1}
              </h6>

              <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                <TextInput
                  label="Company Name"
                  value={_entry.companyName}
                  // onChange={e =>
                  //   updateEntryField(_idx, 'companyName', e.currentTarget.value)
                  // }
                />
                <TextInput
                  label="Job Title"
                  value={_entry.jobTitle}
                  // onChange={e =>
                  //   updateEntryField(_idx, 'jobTitle', e.currentTarget.value)
                  // }
                />
                <TextInput
                  label="Location"
                  value={_entry.location}
                  // onChange={e =>
                  //   updateEntryField(_idx, 'location', e.currentTarget.value)
                  // }
                />
                <DateInput
                  label="From Date"
                  value={_entry.fromDate ? new Date(_entry.fromDate) : null}
                  // onChange={date =>
                  //   updateEntryField(
                  //     _idx,
                  //     'fromDate',
                  //     date ? date.toString() : ''
                  //   )
                  // }
                />
                <DateInput
                  label="To Date"
                  value={_entry.toDate ? new Date(_entry.toDate) : null}
                  // onChange={date =>
                  //   updateEntryField(
                  //     _idx,
                  //     'toDate',
                  //     date ? date?.toString() : ''
                  //   )
                  // }
                />
                <NumberInput
                  min={1}
                  label="Tenure"
                  value={_entry.tenure ?? 1}
                  // onChange={v => updateEntryField(_idx, 'tenure', Number(v))}
                />
                <Textarea
                  label="Job Description"
                  value={_entry.jobDescription}
                  // onChange={e =>
                  //   updateEntryField(
                  //     _idx,
                  //     'jobDescription',
                  //     e.currentTarget.value
                  //   )
                  // }
                />
                <FileInput
                  label="Upload Files"
                  placeholder="Attach document"
                  value={_entry.file}
                  // onChange={e => updateEntryField(_idx, 'file', e)}
                />
              </div>

              {/* <div className="mt-4 text-right">
              <Button
                color="#314158"
                onClick={() => handleSave(entry)}
                size="sm"
              >
                {_entry.id ? 'Update' : 'Save'}
              </Button>
            </div> */}
            </div>
          );
        })}

        {/* ➕ Add new button */}
        <div className="py-1">
          <Button
            onClick={() =>
              modals.open({
                id: `workxp-form`,
                centered: true,
                children: <WorkExperienceForm action="create" />,
              })
            }
            leftSection={<IoAdd size={18} color="white" />}
            variant="filled"
            color="#314158"
            justify="center"
            size="lg"
            radius="md"
            fullWidth
          >
            Add Work Experience
          </Button>
        </div>
      </div>
    </InfoCard>
  );
}

const WorkExperienceForm = (props: {
  action: 'create' | 'update';
  data?: WorkExperience;
}) => {
  const form = useForm<Partial<WorkExperience>>({
    initialValues: props.data ? props.data : { tenure: 1 },
    validateInputOnBlur: true,
    validate: {
      companyName: value => !value && 'Company Name is required',
      jobTitle: value => !value && 'Job Title is required',
      jobDescription: value => !value && 'Job description is required',
      // employeeId: value => !value && 'Company title is required',
      employeeId: value =>
        props.action === 'update' && !value && 'Employee id is missing',
      fromDate: value => !value && 'From date is required',
      toDate: value => !value && 'To date is required',
      location: value => !value && 'Location is required',
      tenure: value =>
        !value
          ? 'Tenure is required'
          : value < 1 && 'Tenure should be greater than 1',
    },
  });
  const [createEmployeeWX] = useCreateEmployeeWorkExperienceMutation();
  const [updateEmployeeWX] = useUpdateEmployeeWorkExperienceMutation();
  const [uploadEmployeeWXDoc] =
    useUploadEmployeeWorkExperienceDocumentMutation();

  const handleCloseForm = () => {
    form.reset();
    modals.closeAll();
  };

  const handleSubmit = async (values: typeof form.values) => {
    //
    let id = props.data?.id ?? '';
    try {
      if (props.action === 'create') {
        // create
        await createEmployeeWX(values).unwrap();
      } else if (props.data) {
        // update
        const { data } = await updateEmployeeWX({
          id: props.data?.id,
          data: values,
        }).unwrap();

        id = data?.id ?? '';
      }

      // attach file

      if (values.file) {
        await uploadEmployeeWXDoc({ id, file: values.file }).unwrap();
      }

      notifications.show({
        title: 'Saved',
        message: 'Working experience saved',
      });
    } catch (error) {
      //
      notifications.show({
        color: 'red',
        title: 'Failed',
        message: 'Unable to save employee working experience',
      });
    }
  };

  return (
    <form
      onSubmit={form.onSubmit(handleSubmit)}
      className="grid grid-cols-1 gap-6 md:grid-cols-2"
    >
      <TextInput label="Company Name" {...form.getInputProps('companyName')} />
      <TextInput label="Job Title" {...form.getInputProps('jobTitle')} />
      <TextInput label="Location" {...form.getInputProps('location')} />
      <DateInput label="From Date" {...form.getInputProps('fromDate')} />
      <DateInput
        label="To Date"
        {...form.getInputProps('toDate')}
        minDate={
          form.values.fromDate ? new Date(form.values.fromDate) : undefined
        }
      />
      <NumberInput min={1} label="Tenure" {...form.getInputProps('tenure')} />
      <Textarea
        label="Job Description"
        {...form.getInputProps('jobDescription')}
      />
      <FileInput
        label="Upload Files"
        placeholder="Attach document"
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

//  <InfoCard
//       label="Work Experience"
//       description="Previous employment history"
//       labelIcon={<IoDocumentTextOutline size={18} />}
//     >
//       <div className="flex flex-col gap-5">
//         {entries.map((_entry,__idx) => (
//           <div
//             key={_idx}
//             className="bg-[#F8FAFC]/50 border border-[#E2E8F0] rounded-lg px-5 py-5"
//           >
//             <div className="flex items-center justify-between">
//               <h6 className="text-[18px] text-[#314158] py-1">
//                 {countLabel(_idx + 1)}
//                 {/* {_idx + 1} */}
//               </h6>

//               {entries.length > 1 && (
//                 <Button
//                   variant="subtle"
//                   color="red"
//                   size="xs"
//                   onClick={() => removeEntry(_entry.id)}
//                 >
//                   Remove
//                 </Button>
//               )}
//             </div>

//             <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
//               {/* From Location */}
//               <TextInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">Company Name</span>
//                   </span>
//                 }
//                 placeholder="Enter company name"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />
//               <TextInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">Job Title</span>
//                   </span>
//                 }
//                 placeholder="Enter job title"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />

//               {/* To Location */}
//               <TextInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">Location</span>
//                   </span>
//                 }
//                 placeholder="Enter location"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 onChange={v => updateField(_entry.id, 'toLocation', v)}
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />

//               {/* Transfer Date (Date | null) */}
//               <DateInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">From Date</span>
//                   </span>
//                 }
//                 placeholder="Select transfer date"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 valueFormat="MMMM D, YYYY"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />

//               {/* Effective Date (Date | null) */}
//               <DateInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">To Date</span>
//                   </span>
//                 }
//                 placeholder="Select effective date"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 valueFormat="MMMM D, YYYY"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />

//               {/* Reason */}
//               <TextInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">
//                       Tenure (Calculated)
//                     </span>
//                   </span>
//                 }
//                 placeholder="Will be calculated"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />
//               <Textarea
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">Job Description</span>
//                   </span>
//                 }
//                 placeholder="Describe your role and responsibilities"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />
//               <FileInput
//                 label={
//                   <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
//                     <span className="text-[#314158]">Upload Files</span>
//                   </span>
//                 }
//                 clearable
//                 placeholder="Upload files"
//                 variant="default"
//                 size="sm"
//                 radius="md"
//                 classNames={{
//                   input:
//                     'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
//                 }}
//               />
//             </div>
//           </div>
//         ))}

//         {/* Add new entry */}
//         <div className="py-1">
//           <Button
//             leftSection={<IoAdd size={18} color="white" />}
//             variant="filled"
//             color="#314158"
//             justify="center"
//             size="lg"
//             radius="md"
//             fullWidth
//             onClick={addEntry}
//           >
//             Add Transfer Record
//           </Button>
//         </div>
//       </div>
//     </InfoCard>
