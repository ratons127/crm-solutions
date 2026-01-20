import { FileInput, NumberInput, Select, TextInput } from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { MdOutlineCloudUpload } from 'react-icons/md';

const PersonalInformationForm = () => {
  const icon = <MdOutlineCloudUpload size={18} />;
 
  

  return (
    <div>
      <form className="p-5 grid grid-cols-12 gap-5">
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="First name"
          placeholder="Enter Your First Name"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Last name"
          placeholder="Enter Your Last Name"
        />

        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Gender"
          placeholder="Gender"
          data={['Man', 'Woman']}
        />
        <DateInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Date of Birth"
          placeholder="Date of Birth"
        />
        <NumberInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Age"
          placeholder="Enter Your Age"
          min={18}
          max={60}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Religion"
          data={['Muslim', 'Hinduism', 'Christianity']}
        />

        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Nationality"
          placeholder="Enter your Nationality Name"
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Blood Group"
          data={['A+', 'A-', 'B+', 'B-', ' AB+', 'AB-', 'O+', 'O-']}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Marital Status"
          data={['Single', 'Married', 'Widowed', 'Divorced', ' Separated']}
        />

        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Spouse Name "
          placeholder="Enter your Spouse Name "
        />
        <NumberInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="No. of Children"
          placeholder="No. of Children"
          min={0}
          max={20}
        />
        <FileInput
          leftSection={icon}
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Upload files"
          placeholder="Upload files"
          multiple
        />
      </form>
    </div>
  );
};

export default PersonalInformationForm;
