'use client';

import { useGetCompanyListQuery } from '@/lib/features/employment/employmentGroupAPI';
import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
import {
  useCreateEmployeeMutation,
  useGetEmployeeListQuery,
  useUpdateEmployeeMutation,
} from '@/services/api/employee/employeeProfileAPI';
import { useGetOrganizationStructureListQuery } from '@/services/api/organization/organizationStructureAPI';
import { useGetDesignationListQuery } from '@/services/api/workStructure/designationAPI';
import { Button, Grid, Select, TextInput, Textarea } from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useMemo } from 'react';
import * as Yup from 'yup';

type EmployeeFormProps = {
  action: 'create' | 'update';
  data?: any; // optional existing employee (for update)
  onSuccess?: () => void;
  onCancel?: () => void;
};

type Values = {
  firstName: string;
  lastName: string;
  gender: string;
  dateOfJoining: string; // YYYY-MM-DD
  dob: string; // YYYY-MM-DD
  //   photo: string | null;
  nationalId: string | null;
  phone: string;
  email: string | null;
  presentAddress: string | null;
  permanentAddress: string | null;
  maritalStatus: string | null;
  emergencyContactName: string | null;
  emergencyContactRelation: string | null;
  emergencyContactPhone: string | null;

  employeeTypeId: string | number | null;
  departmentId: string | number | null;
  designationId: string | number | null;
  supervisorId: string | number | null;
  workPlaceId: string | number | null;
  businessUnitId: string | number | null;
  workPlaceGroupId: string | number | null;
  //   gradeId: number | null;
  //   lineManagerId: number | null;
  companyId: string | number | null;
  //   jobTitle: string | null;
  roleId: string | number | null;
  teamId: number | null;
  photo?: null;
  //   deviceUserId: string | null;
  //   grossSalary: number | null;
  //   religionId: number | null;
  //   nationalityId: number | null;
  //   bloodGroupId: number | null;
  //   paymentTypeId: number | null;
  //   probationDurationId: number | null;
  //   birthCertificateNumber: number | null;
  //   passportNumber: string | null;
  //   drivingLicenseNumber: string | null;
  //   tinNumber: string | null;
  //   officePhone: string | null;
  //   officeEmail: string | null;
  //   estimatedConfirmationDate: string | null;
  //   actualConfirmationDate: string | null;
};

const toYMD = (value?: string | Date | null): string => {
  if (!value) return '';
  const d = value instanceof Date ? value : new Date(value);
  if (Number.isNaN(d.getTime())) return '';
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
};

const fromYMD = (s?: string | null): Date | null => {
  if (!s) return null;
  const [y, m, d] = s.split('-').map(Number);
  if (!y || !m || !d) return null;
  return new Date(y, m - 1, d);
};

const numOrNull = (v: any): number | null => {
  if (v === '' || v === null || v === undefined) return null;
  const n = Number(v);
  return Number.isNaN(n) ? null : n;
};

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required('First name is required').min(2),
  lastName: Yup.string().required('Last name is required').min(1),
  gender: Yup.string().required('Gender is required'),

  phone: Yup.string()
    .required('Phone number is required')
    .matches(/^(01)[0-9]{9}$/, 'Enter a valid phone number (e.g. 01XXXXXXXXX)'),

  email: Yup.string()
    .nullable()
    .email('Enter a valid email')
    .required('Email is required'),

  dateOfJoining: Yup.string().required('Date of joining is required'),
  dob: Yup.string().required('Date of Birth is required'),
  companyId: Yup.number()
    .typeError('Company is required')
    .required('Company is required'),
  businessUnitId: Yup.number()
    .typeError('Business Unit is required')
    .required('Business Unit is required'),
  workPlaceGroupId: Yup.number()
    .typeError('Workplace Group is required')
    .required('Workplace Group is required'),
  workPlaceId: Yup.number()
    .typeError('Workplace is required')
    .required('Workplace is required'),
  employeeTypeId: Yup.number()
    .typeError('Employee Type is required')
    .required('Employee Type is required'),
  designationId: Yup.number()
    .typeError('Designation is required')
    .required('Designation is required'),
  supervisorId: Yup.number()
    .typeError('Supervisor is required')
    .required('Supervisor is required'),

  // 🟩 Emergency Contact Fields
  emergencyContactName: Yup.string().nullable(),
  emergencyContactRelation: Yup.string().nullable(),
  emergencyContactPhone: Yup.string()
    .nullable()
    .when(['emergencyContactName', 'emergencyContactRelation'], {
      is: (name: string | null, relation: string | null) =>
        !!name && name.trim() !== '' && !!relation && relation.trim() !== '',
      then: schema =>
        schema
          .required('Emergency contact phone is required')
          .matches(
            /^(01)[0-9]{9}$/,
            'Enter a valid phone number (e.g. 01XXXXXXXXX)'
          ),
      otherwise: schema => schema.nullable(),
    }),
});

export default function EmployeeForm({
  action,
  data,
  onSuccess,
  onCancel,
}: EmployeeFormProps) {
  const [createEmployee, { isLoading: creating }] = useCreateEmployeeMutation();
  const [updateEmployee, { isLoading: updating }] = useUpdateEmployeeMutation();
  const { data: companyList } = useGetCompanyListQuery(undefined);
  const { data: designationList } = useGetDesignationListQuery(undefined);
  const { data: empTypesData } = useGetEmploymentTypesQuery(undefined);
  const { data: employeesAll } = useGetEmployeeListQuery(undefined);

  const initialValues: Values = useMemo(() => {
    const idFrom = (obj: any, keys: string[]): number | null => {
      for (const key of keys) {
        const v = obj?.[key];
        if (v === null || v === undefined) continue;
        if (typeof v === 'object') return numOrNull(v?.id);
        return numOrNull(v);
      }
      return null;
    };

    const v: Values = {
      firstName: data?.firstName ?? '',
      lastName: data?.lastName ?? '',
      gender: data?.gender ?? 'male',
      dateOfJoining: toYMD(data?.dateOfJoining) || '',
      dob: toYMD(data?.dob),
      photo: null,
      nationalId: data?.nationalId ?? null,
      phone: data?.phone ?? '',
      email: data?.email ?? null,
      presentAddress: data?.presentAddress ?? null,
      permanentAddress: data?.permanentAddress ?? null,
      maritalStatus: data?.maritalStatus ?? null,
      emergencyContactName: data?.emergencyContactName ?? null,
      emergencyContactRelation: data?.emergencyContactRelation ?? null,
      emergencyContactPhone: data?.emergencyContactPhone ?? null,

      employeeTypeId: (idFrom(data, ['employeeTypeId', 'employeeType']) ??
        '') as any,
      departmentId: (idFrom(data, ['departmentId', 'department']) ?? '') as any,
      designationId: (idFrom(data, ['designationId', 'designation']) ??
        '') as any,
      supervisorId: (idFrom(data, ['supervisorId', 'supervisor']) ?? '') as any,
      workPlaceId: (idFrom(data, [
        'workplaceId',
        'workPlaceId',
        'workplace',
        'workPlace',
      ]) ?? '') as any,
      businessUnitId: (idFrom(data, ['businessUnitId', 'businessUnit']) ??
        '') as any,
      workPlaceGroupId: (idFrom(data, [
        'workplaceGroupId',
        'workPlaceGroupId',
        'workplaceGroup',
        'workPlaceGroup',
      ]) ?? '') as any,
      //   gradeId: idFrom(data, ['gradeId', 'grade']),
      //   lineManagerId: idFrom(data, ['lineManagerId', 'lineManager']),
      companyId: (idFrom(data, ['companyId', 'company']) ?? '') as any,
      //   jobTitle: data?.jobTitle ?? null,
      roleId: 4,
      teamId: (idFrom(data, ['teamId', 'team']) ?? '') as any,
      //   deviceUserId: data?.deviceUserId ?? null,
      //   grossSalary: numOrNull(data?.grossSalary),
      //   religionId: idOf(data, 'religionId'),
      //   nationalityId: idOf(data, 'nationalityId'),
      //   bloodGroupId: idOf(data, 'bloodGroupId'),
      //   paymentTypeId: idOf(data, 'paymentTypeId'),
      //   probationDurationId: idOf(data, 'probationDurationId'),
      //   birthCertificateNumber: numOrNull(data?.birthCertificateNumber),
      //   passportNumber: data?.passportNumber ?? null,
      //   drivingLicenseNumber: data?.drivingLicenseNumber ?? null,
      //   tinNumber: data?.tinNumber ?? null,
      //   officePhone: data?.officePhone ?? null,
      //   officeEmail: data?.officeEmail ?? null,
      //   estimatedConfirmationDate: toISO(data?.estimatedConfirmationDate),
      //   actualConfirmationDate: toISO(data?.actualConfirmationDate),
    };
    return v;
  }, [data]);

  const formik = useFormik<Values>({
    initialValues,
    enableReinitialize: true,
    validationSchema,
    onSubmit: async values => {
      // normalize values
      const payload = {
        ...values,
        dateOfJoining: toYMD(values.dateOfJoining) || '',
        dob: values.dob ? toYMD(values.dob) : null,
        photo: null,
        // estimatedConfirmationDate: toISO(values.estimatedConfirmationDate),
        // actualConfirmationDate: toISO(values.actualConfirmationDate),
        employeeTypeId: numOrNull(values.employeeTypeId),
        departmentId: numOrNull(values.departmentId),
        designationId: numOrNull(values.designationId),
        supervisorId: numOrNull(values.supervisorId),
        workPlaceId: numOrNull(values.workPlaceId),
        businessUnitId: numOrNull(values.businessUnitId),
        workPlaceGroupId: numOrNull(values.workPlaceGroupId),
        // gradeId: numOrNull(values.gradeId),
        // lineManagerId: numOrNull(values.lineManagerId),
        companyId: numOrNull(values.companyId),
        roleId: 4,
        teamId: numOrNull(values.teamId),
        // grossSalary: numOrNull(values.grossSalary),
        // religionId: numOrNull(values.religionId),
        // nationalityId: numOrNull(values.nationalityId),
        // bloodGroupId: numOrNull(values.bloodGroupId),
        // paymentTypeId: numOrNull(values.paymentTypeId),
        // probationDurationId: numOrNull(values.probationDurationId),
        // birthCertificateNumber: numOrNull(values.birthCertificateNumber),
      };

      try {
        if (action === 'create') {
          const res = await createEmployee(payload as any).unwrap();
          notifications.show({
            title: 'Employee Created',
            message: (res as any)?.message || 'Employee added successfully.',
            color: 'green',
          });
          onSuccess?.();
        } else if (action === 'update' && data?.id) {
          const res = await updateEmployee({
            id: Number(data.id),
            data: payload as any,
          }).unwrap();
          notifications.show({
            title: 'Employee Updated',
            message: (res as any)?.message || 'Changes saved successfully.',
            color: 'green',
          });
          onSuccess?.();
        }
      } catch (err: any) {
        notifications.show({
          title: `Failed to ${action === 'create' ? 'Create' : 'Update'} Employee`,
          message:
            err?.data?.message || err?.message || 'Something went wrong.',
          color: 'red',
        });
      }
    },
  });

  const { data: organizationalStructuredData } =
    useGetOrganizationStructureListQuery({
      companyId: formik.values.companyId,
      businessUnitId: formik.values.businessUnitId,
      departmentId: (formik.values.departmentId as any) ?? '',
      workplaceGroupId: formik.values.workPlaceGroupId,
      workplaceId: formik.values.workPlaceId,
    });

  const busy = creating || updating;

  // Options
  const employmentTypeOptions = useMemo(
    () =>
      (empTypesData?.data || []).map((et: any) => ({
        value: String(et.id),
        label: et.name,
      })),
    [empTypesData]
  );
  const designationOptions = useMemo(
    () =>
      (designationList?.data || []).map((d: any) => ({
        value: String(d.id),
        label: d.name,
      })),
    [designationList]
  );
  // const supervisorsOptions = useMemo(
  //   () =>
  //     (employeesAll?.data || []).map((e: any) => {
  //       const name = (
  //         e.fullName || `${e.firstName ?? ''} ${e.lastName ?? ''}`
  //       ).trim();
  //       return {
  //         value: String(e.id),
  //         label: name,
  //         id: e.id, // 👈 keep ID separately
  //       };
  //     }),
  //   [employeesAll]
  // );

  // Hierarchy options (use API names only; no id fallbacks)
  const businessUnitOptions = useMemo(
    () =>
      (organizationalStructuredData?.data?.businessUnits || []).map(
        (x: any) => ({ value: String(x.id), label: x.name })
      ),
    [organizationalStructuredData]
  );
  const workplaceGroupOptions = useMemo(
    () =>
      (organizationalStructuredData?.data?.workplaceGroups || []).map(
        (x: any) => ({ value: String(x.id), label: x.name })
      ),
    [organizationalStructuredData]
  );
  const workplaceOptions = useMemo(
    () =>
      (organizationalStructuredData?.data?.workplaces || []).map((x: any) => ({
        value: String(x.id),
        label: x.name,
      })),
    [organizationalStructuredData]
  );
  const departmentOptions = useMemo(
    () =>
      (organizationalStructuredData?.data?.departments || []).map((x: any) => ({
        value: String(x.id),
        label: x.name,
      })),
    [organizationalStructuredData]
  );
  const teamOptions = useMemo(
    () =>
      (organizationalStructuredData?.data?.teams || []).map((x: any) => ({
        value: String(x.id),
        label: x.name,
      })),
    [organizationalStructuredData]
  );

  const getFallbackOption = (value: any, label: any) => {
    if (!value || !label) return [];
    return [{ value: String(value), label: String(label) }];
  };

  return (
    <form onSubmit={formik.handleSubmit} className="space-y-4">
      <Grid gutter="md">
        {/* Basic Info */}
        <Grid.Col span={6}>
          <TextInput
            label="First Name"
            name="firstName"
            placeholder="Enter First Name"
            value={formik.values.firstName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            withAsterisk
            error={formik.touched.firstName && formik.errors.firstName}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Last Name"
            placeholder="Enter Last Name"
            name="lastName"
            value={formik.values.lastName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            withAsterisk
            error={formik.touched.lastName && formik.errors.lastName}
          />
        </Grid.Col>

        <Grid.Col span={6}>
          <Select
            label="Gender"
            data={[
              { value: 'male', label: 'Male' },
              { value: 'female', label: 'Female' },
            ]}
            name="gender"
            value={formik.values.gender}
            onChange={v => formik.setFieldValue('gender', v)}
            onBlur={() => formik.setFieldTouched('gender', true)}
            withAsterisk
            error={formik.touched.gender && formik.errors.gender}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <DateInput
            label="Date of Joining"
            placeholder="Enter Date of Joining"
            value={fromYMD(formik.values.dateOfJoining)}
            onChange={v => formik.setFieldValue('dateOfJoining', toYMD(v))}
            valueFormat="YYYY-MM-DD"
            withAsterisk
            error={formik.touched.dateOfJoining && formik.errors.dateOfJoining}
          />
        </Grid.Col>

        <Grid.Col span={6}>
          <DateInput
            withAsterisk
            label="Date of Birth"
            placeholder="Enter Date of Birth"
            value={fromYMD(formik.values.dob)}
            onChange={v => formik.setFieldValue('dob', toYMD(v))}
            valueFormat="YYYY-MM-DD"
            error={formik.touched.dob && formik.errors.dob}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="National ID"
            placeholder="Enter National ID"
            name="nationalId"
            value={formik.values.nationalId ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col>

        {/* Contacts */}
        <Grid.Col span={6}>
          <TextInput
            label="Phone"
            placeholder="Ex: 01XXXXXXXXX"
            name="phone"
            maxLength={11}
            value={formik.values.phone}
            onChange={e => {
              // Allow only digits, prevent any letters/symbols
              const val = e.target.value.replace(/\D/g, '');
              formik.setFieldValue('phone', val);
            }}
            onBlur={formik.handleBlur}
            withAsterisk
            error={formik.touched.phone && formik.errors.phone}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            withAsterisk
            placeholder="Enter Email"
            label="Email"
            name="email"
            value={formik.values.email ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.email && formik.errors.email}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea
            label="Present Address"
            placeholder="Enter Present Address"
            name="presentAddress"
            value={formik.values.presentAddress ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea
            label="Permanent Address"
            placeholder="Enter Permanent Address"
            name="permanentAddress"
            value={formik.values.permanentAddress ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col>

        {/* Employment & Hierarchy (cascading like LeaveGroupAssignForm) */}
        <Grid.Col span={6}>
          <Select
            label="Company"
            placeholder="Select company"
            withAsterisk
            clearable
            searchable
            data={(companyList?.data || []).map((x: any) => ({
              label: x.name,
              value: String(x.id),
            }))}
            name="companyId"
            value={
              formik.values.companyId ? String(formik.values.companyId) : null
            }
            onChange={value => {
              if (!value) {
                formik.setFieldValue('companyId', '');
                formik.setFieldValue('businessUnitId', '');
                formik.setFieldValue('workPlaceGroupId', '');
                formik.setFieldValue('workPlaceId', '');
                formik.setFieldValue('departmentId', '');
                formik.setFieldValue('teamId', '');
                return;
              }
              formik.setFieldValue('companyId', value);
              // Reset dependents when switching company
              formik.setFieldValue('businessUnitId', '');
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
            onClear={() => {
              formik.setFieldValue('companyId', '');
              formik.setFieldValue('businessUnitId', '');
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
            error={formik.touched.companyId && formik.errors.companyId}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Business Unit"
            withAsterisk
            placeholder="Select business unit"
            clearable
            searchable
            key={`bu-${formik.values.companyId || 'none'}`}
            disabled={!formik.values.companyId}
            data={businessUnitOptions}
            name="businessUnitId"
            value={
              formik.values.businessUnitId
                ? String(formik.values.businessUnitId)
                : null
            }
            onChange={value => {
              if (!value) {
                formik.setFieldValue('businessUnitId', '');
                formik.setFieldValue('workPlaceGroupId', '');
                formik.setFieldValue('workPlaceId', '');
                formik.setFieldValue('departmentId', '');
                formik.setFieldValue('teamId', '');
                return;
              }
              formik.setFieldValue('businessUnitId', value);
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
            onClear={() => {
              formik.setFieldValue('businessUnitId', '');
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
            error={
              formik.touched.businessUnitId && formik.errors.businessUnitId
            }
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Workplace Group"
            withAsterisk
            placeholder="Select workplace group"
            searchable
            clearable
            data={
              workplaceGroupOptions.length
                ? workplaceGroupOptions
                : getFallbackOption(
                    data?.workplaceGroupId,
                    data?.workplaceGroupName || data?.workplaceGroup?.name
                  )
            }
            value={
              formik.values.workPlaceGroupId
                ? String(formik.values.workPlaceGroupId)
                : null
            }
            onChange={v => formik.setFieldValue('workPlaceGroupId', v)}
            error={
              formik.touched.workPlaceGroupId && formik.errors.workPlaceGroupId
            }
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Workplace"
            withAsterisk
            placeholder="Select workplace"
            clearable
            searchable
            key={`wp-${formik.values.workPlaceGroupId || 'none'}`}
            disabled={!formik.values.workPlaceGroupId}
            data={workplaceOptions}
            name="workPlaceId"
            value={
              formik.values.workPlaceId
                ? String(formik.values.workPlaceId)
                : null
            }
            onChange={value => {
              if (!value) {
                formik.setFieldValue('workPlaceId', '');
                formik.setFieldValue('departmentId', '');
                formik.setFieldValue('teamId', '');
                return;
              }
              formik.setFieldValue('workPlaceId', value);
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
            onClear={() => {
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Department"
            placeholder="Select department"
            clearable
            searchable
            key={`dept-${formik.values.workPlaceId || 'none'}`}
            disabled={!formik.values.workPlaceId}
            data={departmentOptions}
            name="departmentId"
            value={
              formik.values.departmentId
                ? String(formik.values.departmentId)
                : null
            }
            onChange={value => {
              if (!value) {
                formik.setFieldValue('departmentId', '');
                formik.setFieldValue('teamId', '');
                return;
              }
              formik.setFieldValue('departmentId', value);
              formik.setFieldValue('teamId', '');
            }}
            onClear={() => {
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
            }}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Team"
            placeholder="Select team"
            clearable
            searchable
            key={`team-${formik.values.departmentId || 'none'}`}
            disabled={!formik.values.departmentId}
            data={teamOptions}
            name="teamId"
            value={formik.values.teamId ? String(formik.values.teamId) : null}
            onChange={value => formik.setFieldValue('teamId', value)}
            onClear={() => formik.setFieldValue('teamId', '')}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Employee Type"
            placeholder="Select employee type"
            clearable
            withAsterisk
            searchable
            data={employmentTypeOptions}
            value={
              formik.values.employeeTypeId
                ? String(formik.values.employeeTypeId)
                : null
            }
            onChange={v => formik.setFieldValue('employeeTypeId', v)}
            error={
              formik.touched.employeeTypeId && formik.errors.employeeTypeId
            }
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Designation"
            withAsterisk
            placeholder="Select designation"
            clearable
            searchable
            data={designationOptions}
            value={
              formik.values.designationId
                ? String(formik.values.designationId)
                : null
            }
            onChange={v => formik.setFieldValue('designationId', v)}
            error={formik.touched.designationId && formik.errors.designationId}
          />
        </Grid.Col>

        {/* Role/Manager */}
        <Grid.Col span={6}>
          <TextInput label="Role" value="Employee" readOnly disabled />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Supervisor"
            withAsterisk
            placeholder="Select supervisor"
            clearable
            searchable
            data={
              employeesAll?.data?.map(c => ({
                label: `${c.firstName} - ${c.lastName} - ${c.id}`,
                value: String(c.id),
              })) ?? []
            }
            value={String(formik.values.supervisorId)}
            onChange={v => formik.setFieldValue('supervisorId', v)}
            error={formik.touched.supervisorId && formik.errors.supervisorId}
          />
        </Grid.Col>
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Line Manager ID"
            placeholder="Enter Line Manager ID"
            value={formik.values.lineManagerId ?? undefined}
            onChange={v => formik.setFieldValue('lineManagerId', v)}
          />
        </Grid.Col> */}

        {/* Job & Payroll */}
        {/* <Grid.Col span={6}>
          <TextInput
            label="Job Title"
            placeholder="Enter Job Title"
            name="jobTitle"
            value={formik.values.jobTitle ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <TextInput
            label="Device User ID"
            placeholder="Enter Device User ID"
            name="deviceUserId"
            value={formik.values.deviceUserId ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Gross Salary"
            placeholder="Enter Gross Salary"
            value={formik.values.grossSalary ?? undefined}
            onChange={v => formik.setFieldValue('grossSalary', v)}
            min={0}
          />
        </Grid.Col> */}

        {/* Lookup Relations */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Religion ID"
            value={formik.values.religionId ?? undefined}
            onChange={v => formik.setFieldValue('religionId', v)}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Nationality ID"
            value={formik.values.nationalityId ?? undefined}
            onChange={v => formik.setFieldValue('nationalityId', v)}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Blood Group ID"
            value={formik.values.bloodGroupId ?? undefined}
            onChange={v => formik.setFieldValue('bloodGroupId', v)}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Payment Type ID"
            value={formik.values.paymentTypeId ?? undefined}
            onChange={v => formik.setFieldValue('paymentTypeId', v)}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Probation Duration ID"
            value={formik.values.probationDurationId ?? undefined}
            onChange={v => formik.setFieldValue('probationDurationId', v)}
          />
        </Grid.Col> */}

        {/* Government/ID */}
        {/* <Grid.Col span={6}>
          <NumberInput
            label="Birth Certificate Number"
            value={formik.values.birthCertificateNumber ?? undefined}
            onChange={v => formik.setFieldValue('birthCertificateNumber', v)}
            min={0}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <TextInput
            label="Passport Number"
            name="passportNumber"
            value={formik.values.passportNumber ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <TextInput
            label="Driving License Number"
            name="drivingLicenseNumber"
            value={formik.values.drivingLicenseNumber ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col> */}
        {/* <Grid.Col span={6}>
          <TextInput
            label="TIN Number"
            name="tinNumber"
            value={formik.values.tinNumber ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col> */}

        {/* Office Contacts */}
        {/* <Grid.Col span={6}>
          <TextInput
            label="Office Phone"
            name="officePhone"
            value={formik.values.officePhone ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Office Email"
            name="officeEmail"
            value={formik.values.officeEmail ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col> */}

        {/* Confirmation Dates */}
        {/* <Grid.Col span={6}>
          <DateInput
            label="Estimated Confirmation Date"
            value={
              formik.values.estimatedConfirmationDate
                ? new Date(formik.values.estimatedConfirmationDate)
                : null
            }
            onChange={v =>
              formik.setFieldValue('estimatedConfirmationDate', toISO(v))
            }
            valueFormat="YYYY-MM-DD"
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <DateInput
            label="Actual Confirmation Date"
            value={
              formik.values.actualConfirmationDate
                ? new Date(formik.values.actualConfirmationDate)
                : null
            }
            onChange={v =>
              formik.setFieldValue('actualConfirmationDate', toISO(v))
            }
            valueFormat="YYYY-MM-DD"
          />
        </Grid.Col> */}

        <Grid.Col span={4}>
          <Select
            label="Marital Status"
            placeholder="Select marital status"
            data={[
              { value: 'Single', label: 'Single' },
              { value: 'Married', label: 'Married' },
              { value: 'Divorced', label: 'Divorced' },
            ]}
            name="maritalStatus"
            searchable
            clearable
            value={formik.values.maritalStatus}
            onChange={v => formik.setFieldValue('maritalStatus', v)}
            onBlur={() => formik.setFieldTouched('maritalStatus', true)}
            error={formik.touched.maritalStatus && formik.errors.maritalStatus}
          />
        </Grid.Col>

        {/* Emergency Contact */}
        <Grid.Col span={4}>
          <TextInput
            label="Emergency Contact Name"
            placeholder="Enter Emergency Contact Name"
            name="emergencyContactName"
            value={formik.values.emergencyContactName ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Emergency Contact Relation"
            placeholder="Enter Emergency Contact Relation"
            name="emergencyContactRelation"
            value={formik.values.emergencyContactRelation ?? ''}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Emergency Contact Phone"
            name="emergencyContactPhone"
            placeholder="Ex: 01XXXXXXXXX"
            maxLength={11}
            value={formik.values.emergencyContactPhone ?? ''}
            onChange={e => {
              const val = e.target.value.replace(/\D/g, '');
              formik.setFieldValue('emergencyContactPhone', val);
            }}
            onBlur={formik.handleBlur}
            error={
              formik.touched.emergencyContactPhone &&
              formik.errors.emergencyContactPhone
            }
          />
        </Grid.Col>
      </Grid>

      <div className="flex justify-end gap-3">
        <Button variant="light" color="red" onClick={onCancel} disabled={busy}>
          Cancel
        </Button>
        <Button type="submit" disabled={busy}>
          {busy
            ? action === 'create'
              ? 'Creating…'
              : 'Saving…'
            : action === 'create'
              ? 'Create'
              : 'Save Changes'}
        </Button>
      </div>
    </form>
  );
}
