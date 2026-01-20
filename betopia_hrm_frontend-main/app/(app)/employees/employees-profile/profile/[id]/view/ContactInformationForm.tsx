import { Checkbox, Select, Textarea, TextInput } from '@mantine/core';
import { useState } from 'react';

const ContactInformationForm = () => {

  
  const [checked, setChecked] = useState(false);

  return (
    <div>
      <form className="p-5 grid grid-cols-12 gap-5">
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Email"
          placeholder="Enter Your Email"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Work Email"
          placeholder="Enter Work Email"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Phone "
          placeholder="Enter Your Phone Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Work Phone"
          placeholder="Enter Your Work Phone Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Emergency Contact No"
          placeholder="Enter Your Emergency Contact Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Emergency Contact Relation"
          placeholder="Enter Your Emergency Contact Number"
        />

        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Country"
          placeholder="Pick value"
          data={['Bangladesh', 'India', 'USA', 'UK']}
          defaultValue="Bangladesh"
          clearable
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Division"
          placeholder="Pick value"
          data={[
            'DHAKA',
            'Barista',
            'Chattogram',
            'Khulna',
            'Rajshahi',
            'Ranger',
            'Mymensingh',
            'Sylhet',
          ]}
          defaultValue="DHAKA"
          clearable
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="District"
          placeholder="Pick value"
          data={[
            'DHAKA',
            'Barista',
            'Chattogram',
            'Khulna',
            'Rajshahi',
            'Ranger',
            'Mymensingh',
            'Sylhet',
          ]}
          defaultValue="DHAKA"
          clearable
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Police Station"
          placeholder="Pick value"
          data={[
            'DHAKA',
            'Barista',
            'Chattogram',
            'Khulna',
            'Rajshahi',
            'Ranger',
            'Mymensingh',
            'Sylhet',
          ]}
          defaultValue="DHAKA"
          clearable
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Post Office"
          placeholder="Pick value"
          data={[
            'DHAKA',
            'Barista',
            'Chattogram',
            'Khulna',
            'Rajshahi',
            'Ranger',
            'Mymensingh',
            'Sylhet',
          ]}
          defaultValue="DHAKA"
          clearable
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Post Code"
          placeholder="Pick value"
          data={[
            'DHAKA',
            'Barista',
            'Chattogram',
            'Khulna',
            'Rajshahi',
            'Ranger',
            'Mymensingh',
            'Sylhet',
          ]}
          defaultValue="DHAKA"
          clearable
        />

        <Textarea
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Address"
          placeholder="Input placeholder"
        />

        <Checkbox
          className="col-span-12 sm:col-span-6 lg:col-span-2 py-8"
          defaultChecked
          label="Present Address"
          checked={checked}
          onChange={event => setChecked(event.currentTarget.checked)}
        />
        {checked && (
          <>
            <TextInput
              className="col-span-12 sm:col-span-6 lg:col-span-3"
              variant="filled"
              label="Present Address "
              placeholder="Input placeholder X"
            />

            <Select
              className="col-span-12 sm:col-span-6 lg:col-span-3"
              variant="filled"
              label="Country"
              placeholder="Pick value"
              data={['Bangladesh', 'India', 'USA', 'UK']}
              defaultValue="Bangladesh"
              clearable
            />
            <Select
              className="col-span-12 sm:col-span-6 lg:col-span-4"
              variant="filled"
              label="Division"
              placeholder="Pick value"
              data={[
                'DHAKA',
                'Barista',
                'Chattogram',
                'Khulna',
                'Rajshahi',
                'Ranger',
                'Mymensingh',
                'Sylhet',
              ]}
              defaultValue="DHAKA"
              clearable
            />
            <Select
              className="col-span-12 sm:col-span-6 lg:col-span-4"
              variant="filled"
              label="District"
              placeholder="Pick value"
              data={[
                'DHAKA',
                'Barista',
                'Chattogram',
                'Khulna',
                'Rajshahi',
                'Ranger',
                'Mymensingh',
                'Sylhet',
              ]}
              defaultValue="DHAKA"
              clearable
            />
            <Select
              className="col-span-12 sm:col-span-6 lg:col-span-4"
              variant="filled"
              label="Police Station"
              placeholder="Pick value"
              data={[
                'DHAKA',
                'Barista',
                'Chattogram',
                'Khulna',
                'Rajshahi',
                'Ranger',
                'Mymensingh',
                'Sylhet',
              ]}
              defaultValue="DHAKA"
              clearable
            />
            <Select
              className="col-span-12 sm:col-span-6 lg:col-span-4"
              variant="filled"
              label="Post Office"
              placeholder="Pick value"
              data={[
                'DHAKA',
                'Barista',
                'Chattogram',
                'Khulna',
                'Rajshahi',
                'Ranger',
                'Mymensingh',
                'Sylhet',
              ]}
              defaultValue="DHAKA"
              clearable
            />
            <Select
              className="col-span-12 sm:col-span-6 lg:col-span-4"
              variant="filled"
              label="Post Code"
              placeholder="Pick value"
              data={[
                'DHAKA',
                'Barista',
                'Chattogram',
                'Khulna',
                'Rajshahi',
                'Ranger',
                'Mymensingh',
                'Sylhet',
              ]}
              defaultValue="DHAKA"
              clearable
            />

            <Textarea
              className="col-span-12 sm:col-span-6 lg:col-span-4"
              variant="filled"
              label="Address"
              placeholder="Input placeholder"
            />
          </>
        )}
      </form>
    </div>
  );
};

export default ContactInformationForm;
