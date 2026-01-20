import React from 'react';
import { TextInput } from '@mantine/core';

const IdentificationForm = () => {
  return (
    <div>
      <form className="p-5 grid grid-cols-12 gap-5">
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Employee ID"
          placeholder="Enter your Employee ID"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="NID Number"
          placeholder="Enter Your NID Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Birth Certificate Number"
          placeholder="Enter Your Birth Certificate Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Passport Number"
          placeholder="Enter Your Passport Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Driving License Number"
          placeholder="Enter Your Driving License Number"
        />
        <TextInput
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="TIN  Number"
          placeholder="Enter Your TIN Number"
        />
       
        
      </form>
    </div>
  );
};

export default IdentificationForm;
