import React from 'react';
import {  Select } from '@mantine/core';


const WorkstationDetailsForm = () => {
  return (
    <div>
      <form className="p-5 grid grid-cols-12 gap-5">
        
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Company Name"
          data={['Muslim', 'Hinduism', 'Christianity']}
        />

      
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Business Unit"
          data={['A+', 'A-', 'B+', 'B-', ' AB+', 'AB-', 'O+', 'O-']}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Workplace Group"
          data={[
            'Single',
            'Married',
            'Widowed',
            'Divorced',
            ' Separated',
            'Registered Partnership/De Facto Union',
          ]}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Workplace"
          data={[
            'Single',
            'Married',
            'Widowed',
            'Divorced',
            ' Separated',
            'Registered Partnership/De Facto Union',
          ]}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Department"
          data={[
            'Single',
            'Married',
            'Widowed',
            'Divorced',
            ' Separated',
            'Registered Partnership/De Facto Union',
          ]}
        />
        <Select
          className="col-span-12 sm:col-span-6 lg:col-span-4"
          variant="filled"
          label="Team"
          data={[
            'Single',
            'Married',
            'Widowed',
            'Divorced',
            ' Separated',
            'Registered Partnership/De Facto Union',
          ]}
        />

      
      </form>
    </div>
  );
};

export default WorkstationDetailsForm;
