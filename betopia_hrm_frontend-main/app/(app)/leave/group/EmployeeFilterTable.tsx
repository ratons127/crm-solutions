import { DataTable } from '@/components/common/table/DataTable';
import { Employee } from '@/lib/types/employee/index';
import { MultiSelect, Select } from '@mantine/core';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';

export type EmployeeFilterType =
  | 'all'
  | 'company'
  | 'business_unit'
  | 'workplace_group'
  | 'workplace'
  | 'department'
  | 'team';
// type EmployeeFilterItem = {
//   label: string;
//   placeholder: string;
//   onChange: (data: string[]) => void;
//   value: string[];
//   data: { label: string; value: string }[];
// };

export default function EmployeeFilterTable() {
  const [employeeList] = useState<Employee[]>([]);
  const columns: ColumnDef<Employee, any>[] = useMemo(
    () => [
      {
        id: 'select',
        enableSorting: false,
        enableHiding: false,
        header: ({ table }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={table.getIsAllPageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        ),
        cell: ({ row }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={row.getIsSelected()}
            disabled={!row.getCanSelect()}
            onChange={row.getToggleSelectedHandler()}
          />
        ),
      },
      { accessorKey: 'id', header: 'ID', filterFn: 'equals' },
      {
        accessorKey: 'name',
        header: 'Name',
      },
      { accessorKey: 'description', header: 'Description ' },
    ],
    []
  );
  const [filterType, setFilterType] = useState<EmployeeFilterType>('all');

  const [companies, setCompanies] = useState<string[]>([]);
  const [businessUnits, setBusinessUnits] = useState<string[]>([]);
  const [workplaceGroups, setWorkplaceGroups] = useState<string[]>([]);
  const [workplaces, setWorkplaces] = useState<string[]>([]);
  const [departments, setDepartments] = useState<string[]>([]);
  const [teams, setTeams] = useState<string[]>([]);
  // const [employees, setEmployees] = useState<Employee[]>([]);

  const filterItems = {
    company: {
      label: 'Companies',
      placeholder: 'Select companies',
      onChange: setCompanies,
      value: companies,
      data: [
        {
          label: 'Betopia',
          value: 'betopia',
        },
        {
          label: 'Brac',
          value: 'brac',
        },
      ],
    },
    business_unit: {
      label: 'Business Units',
      placeholder: 'select business units',
      onChange: setBusinessUnits,
      value: businessUnits,
      data: [
        {
          label: 'Softvence',
          value: 'softvence',
        },
        {
          label: 'SM Technology',
          value: 'sm_technology',
        },
      ],
    },
    workplace_group: {
      label: 'Workplace Group',
      placeholder: 'select workplace group',
      onChange: setWorkplaceGroups,
      value: workplaceGroups,
      data: [
        {
          label: 'WP Group1',
          value: 'wp1',
        },
        {
          label: 'WP Group2',
          value: 'wp2',
        },
      ],
    },
    workplace: {
      label: 'Workplace ',
      placeholder: 'select workplace ',
      onChange: setWorkplaces,
      value: workplaces,
      data: [
        {
          label: 'WP 1',
          value: 'wp22',
        },
        {
          label: 'WP 2',
          value: 'wp33',
        },
      ],
    },
    department: {
      label: 'Departments ',
      placeholder: 'select department ',
      onChange: setDepartments,
      value: departments,
      data: [
        {
          label: 'Department 1',
          value: 'softvence',
        },
        {
          label: 'Department 2',
          value: 'sm_technology',
        },
      ],
    },
    teams: {
      label: 'Teams ',
      placeholder: 'select teams ',
      onChange: setTeams,
      value: teams,
      data: [
        {
          label: 'Department 1',
          value: 'softvence',
        },
        {
          label: 'Department 2',
          value: 'sm_technology',
        },
      ],
    },
  };

  const filterBy =
    filterItems?.[filterType as keyof typeof filterItems] ?? null;

  return (
    <div>
      <div className="min-h-[50vh] w-full space-y-3 ">
        <div className=" flex  items-end justify-between gap-3 ">
          <div className="grid grid-cols-8 gap-3 w-full">
            <Select
              className=" col-span-1"
              label="Filter By"
              placeholder="filter-by"
              data={[
                { label: 'All', value: 'all' },
                ...Object.entries(filterItems).map(([key, value]) => ({
                  label: value.label,
                  value: key,
                })),
              ]}
              value={filterType}
              onChange={v => {
                if (v) {
                  setFilterType(v as 'all' | 'company');
                }
              }}
            />

            {filterType === 'all' ? null : (
              <MultiSelect
                placeholder={filterBy.placeholder}
                className=" col-span-7"
                label={filterBy.label}
                multiple
                onChange={filterBy.onChange}
                data={filterBy.data}
                value={filterBy.value}
                defaultValue={filterBy.value}
              />
            )}

            {/* {filterType === 'company' && (
              <>
                {}
                <MultiSelect
                  placeholder="Select companies"
                  className=" col-span-2"
                  label="Companies"
                  multiple
                  onChange={setCompanies}
                  data={[
                    {
                      label: 'Betopia',
                      value: 'betopia',
                    },
                    {
                      label: 'Brac',
                      value: 'brac',
                    },
                  ]}
                />
                <MultiSelect
                  className=" col-span-2"
                  label="Business Unit"
                  disabled={companies.length === 0}
                  multiple
                  onChange={setBusinessUnits}
                  data={[
                    {
                      label: 'Softvence',
                      value: 'softvence',
                    },
                    {
                      label: 'SparkTech',
                      value: 'sparktech',
                    },
                  ]}
                />
                <MultiSelect
                  className=" col-span-2"
                  label="Workplace Group"
                  disabled={businessUnits.length === 0}
                  multiple
                  onChange={setWorkplaceGroups}
                  data={[
                    {
                      label: 'WorkplaceGroup1',
                      value: 'softvence',
                    },
                    {
                      label: 'WorkplaceGroup2',
                      value: 'sparktech',
                    },
                  ]}
                />
                <MultiSelect
                  className=" col-span-2"
                  label="Workplace"
                  disabled={workplaceGroups.length === 0}
                  multiple
                  onChange={setWorkplaces}
                  data={[
                    {
                      label: 'Workplace1',
                      value: 'softvence',
                    },
                    {
                      label: 'Workplace2',
                      value: 'sparktech',
                    },
                  ]}
                />
                <MultiSelect
                  className=" col-span-2"
                  label="Department"
                  disabled={workplaces.length === 0}
                  multiple
                  onChange={setDepartments}
                  data={[
                    {
                      label: 'Backend',
                      value: 'softvence',
                    },
                    {
                      label: 'Frontend',
                      value: 'sparktech',
                    },
                  ]}
                />
                <MultiSelect
                  className=" col-span-2"
                  label="Team"
                  disabled={departments.length === 0}
                  multiple
                  onChange={setBusinessUnits}
                  data={[
                    {
                      label: 'Team1',
                      value: 'softvence',
                    },
                    {
                      label: 'Team2',
                      value: 'sparktech',
                    },
                  ]}
                />
              </>
            )} */}
          </div>
        </div>
        <DataTable<Employee>
          data={employeeList}
          columns={columns}
          selectFilters={[]}
          initialColumnVisibility={{}}
          searchPlaceholder="Search..."
          hideExport
          headerLeft={<h1>Employee List</h1>}
          //   hideColumnVisibility
          //   hideSearch
        />
      </div>
    </div>
  );
}

// export function EmployeeFilterTableV1() {
//   const [employeeList, setEmployeeList] = useState<Employee[]>([]);
//   const columns: ColumnDef<Employee, any>[] = useMemo(
//     () => [
//       {
//         id: 'select',
//         enableSorting: false,
//         enableHiding: false,
//         header: ({ table }) => (
//           <input
//             type="checkbox"
//             className="h-4 w-4 accent-blue-600 cursor-pointer"
//             checked={table.getIsAllPageRowsSelected()}
//             onChange={table.getToggleAllPageRowsSelectedHandler()}
//           />
//         ),
//         cell: ({ row }) => (
//           <input
//             type="checkbox"
//             className="h-4 w-4 accent-blue-600 cursor-pointer"
//             checked={row.getIsSelected()}
//             disabled={!row.getCanSelect()}
//             onChange={row.getToggleSelectedHandler()}
//           />
//         ),
//       },
//       { accessorKey: 'id', header: 'ID', filterFn: 'equals' },
//       {
//         accessorKey: 'name',
//         header: 'Name',
//       },
//       { accessorKey: 'description', header: 'Description ' },
//     ],
//     []
//   );

//   const [filterType, setFilterType] = useState<'all' | 'company'>('all');
//   const [companies, setCompanies] = useState<string[]>([]);
//   const [businessUnits, setBusinessUnits] = useState<string[]>([]);
//   const [workplaceGroups, setWorkplaceGroups] = useState<string[]>([]);
//   const [workplaces, setWorkplaces] = useState<string[]>([]);
//   const [departments, setDepartments] = useState<string[]>([]);
//   const [teams, setTeams] = useState<string[]>([]);
//   const [employees, setEmployees] = useState<Employee[]>([]);

//   return (
//     <div>
//       <div className="min-h-[60vh] w-[60vw] space-y-5 py-5">
//         <div className="grid grid-cols-8 gap-3 flex-1 w-full">
//           <Select
//             className=" col-span-2"
//             label="Filter By"
//             placeholder="filter-by"
//             data={[
//               { label: 'All', value: 'all' },
//               { label: 'Company', value: 'company' },
//             ]}
//             value={filterType}
//             onChange={v => {
//               if (v) {
//                 setFilterType(v as 'all' | 'company');
//               }
//             }}
//           />

//           {filterType === 'company' && (
//             <>
//               <MultiSelect
//                 placeholder="Select companies"
//                 className=" col-span-2"
//                 label="Companies"
//                 multiple
//                 onChange={setCompanies}
//                 data={[
//                   {
//                     label: 'Betopia',
//                     value: 'betopia',
//                   },
//                   {
//                     label: 'Brac',
//                     value: 'brac',
//                   },
//                 ]}
//               />
//               <MultiSelect
//                 className=" col-span-2"
//                 label="Business Unit"
//                 disabled={companies.length === 0}
//                 multiple
//                 onChange={setBusinessUnits}
//                 data={[
//                   {
//                     label: 'Softvence',
//                     value: 'softvence',
//                   },
//                   {
//                     label: 'SparkTech',
//                     value: 'sparktech',
//                   },
//                 ]}
//               />
//               <MultiSelect
//                 className=" col-span-2"
//                 label="Workplace Group"
//                 disabled={businessUnits.length === 0}
//                 multiple
//                 onChange={setWorkplaceGroups}
//                 data={[
//                   {
//                     label: 'WorkplaceGroup1',
//                     value: 'softvence',
//                   },
//                   {
//                     label: 'WorkplaceGroup2',
//                     value: 'sparktech',
//                   },
//                 ]}
//               />
//               <MultiSelect
//                 className=" col-span-2"
//                 label="Workplace"
//                 disabled={workplaceGroups.length === 0}
//                 multiple
//                 onChange={setWorkplaces}
//                 data={[
//                   {
//                     label: 'Workplace1',
//                     value: 'softvence',
//                   },
//                   {
//                     label: 'Workplace2',
//                     value: 'sparktech',
//                   },
//                 ]}
//               />
//               <MultiSelect
//                 className=" col-span-2"
//                 label="Department"
//                 disabled={workplaces.length === 0}
//                 multiple
//                 onChange={setDepartments}
//                 data={[
//                   {
//                     label: 'Backend',
//                     value: 'softvence',
//                   },
//                   {
//                     label: 'Frontend',
//                     value: 'sparktech',
//                   },
//                 ]}
//               />
//               <MultiSelect
//                 className=" col-span-2"
//                 label="Team"
//                 disabled={departments.length === 0}
//                 multiple
//                 onChange={setBusinessUnits}
//                 data={[
//                   {
//                     label: 'Team1',
//                     value: 'softvence',
//                   },
//                   {
//                     label: 'Team2',
//                     value: 'sparktech',
//                   },
//                 ]}
//               />
//             </>
//           )}
//         </div>
//         <div className=" flex items-center justify-between ">
//           <Text>Employee</Text>
//           <TextInput size="sm" rightSection={<FaMagnifyingGlass />} />
//         </div>
//         <DataTable<Employee>
//           data={employeeList}
//           columns={columns}
//           selectFilters={[]}
//           initialColumnVisibility={{}}
//           searchPlaceholder="Search..."
//           hideExport
//           hideColumnVisibility
//           hideSearch
//         />
//         {/* <div className="flex items-center gap3">
//           <Button onCL fullWidth>Confirm</Button>
//         </div> */}
//       </div>
//     </div>
//   );
// }
