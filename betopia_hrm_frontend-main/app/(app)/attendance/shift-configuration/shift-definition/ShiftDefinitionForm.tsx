import SubmitSection from '@/components/common/form/SubmitSection';
import { useGetShiftCategoryListQuery } from '@/lib/features/shiftCategories/shiftCategoriesAPI';
import {
  useCreateShiftDefinitionMutation,
  useUpdateShiftDefinitionMutation,
} from '@/lib/features/shiftDefinition/shiftDefinitionAPI';
import {
  ShiftDefinition,
  ShiftDefinitionRequest,
} from '@/lib/types/shiftDefinition';
import { useGetCompanyAllListQuery } from '@/services/api/admin/company/companyAPI';
import {
  Checkbox,
  NumberInput,
  Select,
  Switch,
  TextInput,
} from '@mantine/core';
import { TimeInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useRef } from 'react';
import { GoClock } from 'react-icons/go';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: ShiftDefinition | null;
  onClose: () => void;
};

const DAYS_OF_WEEK = [
  { value: 'SUNDAY', label: 'Sunday' },
  { value: 'MONDAY', label: 'Monday' },
  { value: 'TUESDAY', label: 'Tuesday' },
  { value: 'WEDNESDAY', label: 'Wednesday' },
  { value: 'THURSDAY', label: 'Thursday' },
  { value: 'FRIDAY', label: 'Friday' },
  { value: 'SATURDAY', label: 'Saturday' },
];

const validationSchema = Yup.object().shape({
  shiftCategoryId: Yup.number()
    .required('Shift category is required')
    .min(1, 'Shift category is required'),
  shiftName: Yup.string()
    .required('Shift name is required')
    .min(2, 'Shift name must be at least 2 characters')
    .max(100, 'Shift name must not exceed 100 characters')
    .trim(),
  shiftCode: Yup.string()
    .required('Shift code is required')
    .min(2, 'Shift code must be at least 2 characters')
    .max(50, 'Shift code must not exceed 50 characters')
    .trim(),
  companyId: Yup.number()
    .required('Company is required')
    .min(1, 'Company is required'),
  // businessUnitId: Yup.number()
  //   .required('Business unit is required')
  //   .min(1, 'Business unit is required'),
  // workPlaceGroupId: Yup.number()
  //   .required('Workplace group is required')
  //   .min(1, 'Workplace group is required'),
  // workPlaceId: Yup.number()
  //   .required('Workplace is required')
  //   .min(1, 'Workplace is required'),
  // departmentId: Yup.number()
  //   .required('Department is required')
  //   .min(1, 'Department is required'),
  // teamId: Yup.number().required('Team is required').min(1, 'Team is required'),
  startTime: Yup.string().required('Start time is required'),
  endTime: Yup.string().required('End time is required'),
  breakMinutes: Yup.number()
    .optional()
    .min(0, 'Break minutes must be at least 0')
    .max(480, 'Break minutes must not exceed 480'),
  graceInMinutes: Yup.number()
    .optional()
    .min(0, 'Grace in minutes must be at least 0')
    .max(60, 'Grace in minutes must not exceed 60'),
  graceOutMinutes: Yup.number()
    .optional()
    .min(0, 'Grace out minutes must be at least 0')
    .max(60, 'Grace out minutes must not exceed 60'),
  isNightShift: Yup.boolean().optional(),
  status: Yup.boolean().optional(),
  weeklyOffs: Yup.array()
    .of(
      Yup.object().shape({
        dayOfWeek: Yup.string(),
      })
    )
    .optional(),
});

export default function ShiftDefinitionForm({ action, data, onClose }: Props) {
  const [createShiftDefinition, { isLoading }] =
    useCreateShiftDefinitionMutation();
  const [updateShiftDefinition, { isLoading: isLoadingUpdate }] =
    useUpdateShiftDefinitionMutation();

  // Refs for time pickers
  const startTimeRef = useRef<HTMLInputElement>(null);
  const endTimeRef = useRef<HTMLInputElement>(null);

  // Fetch dropdown data
  const { data: shiftCategories } = useGetShiftCategoryListQuery(undefined);
  const { data: companies } = useGetCompanyAllListQuery({});
  // const { data: businessUnits } = useGetBusinessUnitListQuery({});
  // const { data: workplaceGroups } = useGetWorkplaceGroupListQuery({});
  // const { data: workplaces } = useGetWorkplaceListQuery({});
  // const { data: departments } = useGetDepartmentListQuery({});
  // const { data: teams } = useGetTeamListQuery({});

  const handleSubmit = async (values: ShiftDefinitionRequest) => {
    try {
      if (action === 'create') {
        const result = await createShiftDefinition(values).unwrap();
        if (result?.status === 201 && result?.success) {
          notifications.show({
            title: 'Success',
            message: result?.message,
            color: 'green',
          });
          onClose();
        }
      } else if (action === 'update' && data?.id) {
        const result = await updateShiftDefinition({
          id: data.id,
          data: values,
        }).unwrap();
        if (result?.status === 200 || result?.success) {
          notifications.show({
            title: 'Success',
            message: result?.message,
            color: 'blue',
          });
          onClose();
        }
      }
    } catch (error: any) {
      const errorMessage = error?.data?.message || error?.message;
      const statusCode = error?.data?.status || error?.status;

      // Check for duplicate/conflict error
      if (
        statusCode === 409 ||
        errorMessage?.toLowerCase().includes('already exists')
      ) {
        notifications.show({
          title: 'Duplicate Entry',
          message: errorMessage || 'The shift already exists.',
          color: 'orange',
        });
      } else {
        notifications.show({
          title: 'Error',
          message: errorMessage || 'Something went wrong',
          color: 'red',
        });
      }
    }
  };

  const formik = useFormik<ShiftDefinitionRequest>({
    initialValues:
      action === 'update' && data
        ? {
            shiftCategoryId: data.shiftCategory?.id || 0,
            shiftName: data.shiftName,
            shiftCode: data.shiftCode,
            companyId: data.company?.id || 0,
            // businessUnitId: data.businessUnit?.id || 0,
            // workPlaceGroupId: data.workPlaceGroup?.id || 0,
            // workPlaceId: data.workPlace?.id || 0,
            // departmentId: data.department?.id || 0,
            // teamId: data.team?.id || 0,
            startTime: data.startTime,
            endTime: data.endTime,
            breakMinutes: data.breakMinutes || 0,
            isNightShift: data.nightShift || false,
            graceInMinutes: data.graceInMinutes || 0,
            graceOutMinutes: data.graceOutMinutes || 0,
            status: data.status,
            weeklyOffs: data.weeklyOffs || [],
          }
        : {
            shiftCategoryId: 0,
            shiftName: '',
            shiftCode: '',
            companyId: 0,
            // businessUnitId: 0,
            // workPlaceGroupId: 0,
            // workPlaceId: 0,
            // departmentId: 0,
            // teamId: 0,
            startTime: '',
            endTime: '',
            breakMinutes: 0,
            isNightShift: false,
            graceInMinutes: 0,
            graceOutMinutes: 0,
            status: true,
            weeklyOffs: [],
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  const handleWeeklyOffChange = (day: string, checked: boolean) => {
    const currentWeeklyOffs = formik.values.weeklyOffs || [];
    if (checked) {
      formik.setFieldValue('weeklyOffs', [
        ...currentWeeklyOffs,
        { dayOfWeek: day },
      ]);
    } else {
      formik.setFieldValue(
        'weeklyOffs',
        currentWeeklyOffs.filter(off => off.dayOfWeek !== day)
      );
    }
  };

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center mb-4">
        <h1 className="text-lg font-semibold">{`Shift Definition ${action === 'create' ? 'Create' : 'Update'}`}</h1>
      </div>
      <div className="grid grid-cols-12 gap-3">
        {/* Shift Category */}
        <Select
          className="col-span-6"
          label="Shift Category"
          placeholder="Select shift category"
          clearable
          searchable
          withAsterisk
          name="shiftCategoryId"
          value={formik.values.shiftCategoryId?.toString()}
          onChange={value => {
            formik.setFieldValue('shiftCategoryId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('shiftCategoryId', true)}
          data={
            shiftCategories?.data?.map((cat: any) => ({
              value: cat.id.toString(),
              label: cat.name,
            })) || []
          }
          error={
            formik.touched.shiftCategoryId && formik.errors.shiftCategoryId
              ? String(formik.errors.shiftCategoryId)
              : undefined
          }
        />

        {/* Shift Name */}
        <TextInput
          placeholder="Enter shift name"
          label="Shift Name"
          withAsterisk
          className="col-span-6"
          name="shiftName"
          value={formik.values.shiftName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.shiftName && formik.errors.shiftName}
        />

        {/* Shift Code */}
        <TextInput
          placeholder="Enter shift code"
          label="Shift Code"
          withAsterisk
          className="col-span-6"
          name="shiftCode"
          value={formik.values.shiftCode}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.shiftCode && formik.errors.shiftCode}
        />

        <Select
          className="col-span-6"
          label="Company"
          placeholder="Select company"
          clearable
          searchable
          withAsterisk
          name="companyId"
          value={formik.values.companyId?.toString()}
          onChange={value => {
            formik.setFieldValue('companyId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('companyId', true)}
          data={
            companies?.data?.map((company: any) => ({
              value: company.id.toString(),
              label: company.name,
            })) || []
          }
          error={
            formik.touched.companyId && formik.errors.companyId
              ? String(formik.errors.companyId)
              : undefined
          }
        />

        {/* Company */}
        {/* <Select
          className="col-span-6"
          label="Company"
          placeholder="Select company"
          clearable
          searchable
          withAsterisk
          name="companyId"
          value={formik.values.companyId?.toString()}
          onChange={value => {
            formik.setFieldValue('companyId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('companyId', true)}
          data={
            companies?.data?.map((company: any) => ({
              value: company.id.toString(),
              label: company.name,
            })) || []
          }
          error={
            formik.touched.companyId && formik.errors.companyId
              ? String(formik.errors.companyId)
              : undefined
          }
        />

        
        <Select
          className="col-span-6"
          label="Business Unit"
          placeholder="Select business unit"
          clearable
          searchable
          withAsterisk
          name="businessUnitId"
          value={formik.values.businessUnitId?.toString()}
          onChange={value => {
            formik.setFieldValue('businessUnitId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('businessUnitId', true)}
          data={
            businessUnits?.data?.map((unit: any) => ({
              value: unit.id.toString(),
              label: unit.name,
            })) || []
          }
          error={
            formik.touched.businessUnitId && formik.errors.businessUnitId
              ? String(formik.errors.businessUnitId)
              : undefined
          }
        />

       
        <Select
          className="col-span-6"
          label="Workplace Group"
          placeholder="Select workplace group"
          clearable
          searchable
          withAsterisk
          name="workPlaceGroupId"
          value={formik.values.workPlaceGroupId?.toString()}
          onChange={value => {
            formik.setFieldValue('workPlaceGroupId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('workPlaceGroupId', true)}
          data={
            workplaceGroups?.data?.map((group: any) => ({
              value: group.id.toString(),
              label: group.name,
            })) || []
          }
          error={
            formik.touched.workPlaceGroupId && formik.errors.workPlaceGroupId
              ? String(formik.errors.workPlaceGroupId)
              : undefined
          }
        />

        
        <Select
          className="col-span-6"
          label="Workplace"
          placeholder="Select workplace"
          clearable
          searchable
          withAsterisk
          name="workPlaceId"
          value={formik.values.workPlaceId?.toString()}
          onChange={value => {
            formik.setFieldValue('workPlaceId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('workPlaceId', true)}
          data={
            workplaces?.data?.map((workplace: any) => ({
              value: workplace.id.toString(),
              label: workplace.name,
            })) || []
          }
          error={
            formik.touched.workPlaceId && formik.errors.workPlaceId
              ? String(formik.errors.workPlaceId)
              : undefined
          }
        />

        
        <Select
          className="col-span-6"
          label="Department"
          placeholder="Select department"
          clearable
          searchable
          withAsterisk
          name="departmentId"
          value={formik.values.departmentId?.toString()}
          onChange={value => {
            formik.setFieldValue('departmentId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('departmentId', true)}
          data={
            departments?.data?.map((dept: any) => ({
              value: dept.id.toString(),
              label: dept.name,
            })) || []
          }
          error={
            formik.touched.departmentId && formik.errors.departmentId
              ? String(formik.errors.departmentId)
              : undefined
          }
        />
        <Select
          className="col-span-6"
          label="Team"
          placeholder="Select team"
          clearable
          searchable
          withAsterisk
          name="teamId"
          value={formik.values.teamId?.toString()}
          onChange={value => {
            formik.setFieldValue('teamId', value ? Number(value) : 0);
          }}
          onBlur={() => formik.setFieldTouched('teamId', true)}
          data={
            teams?.data?.map((team: any) => ({
              value: team.id.toString(),
              label: team.name,
            })) || []
          }
          error={
            formik.touched.teamId && formik.errors.teamId
              ? String(formik.errors.teamId)
              : undefined
          }
        /> */}

        {/* Start Time */}
        <TimeInput
          ref={startTimeRef}
          label="Start Time"
          withAsterisk
          className="col-span-6"
          size="xs"
          name="startTime"
          value={formik.values.startTime}
          onChange={event => {
            const timeValue = event.currentTarget.value;
            // Convert HH:MM to HH:MM:SS format
            const formattedTime = timeValue ? `${timeValue}:00` : '';
            formik.setFieldValue('startTime', formattedTime);
          }}
          onBlur={formik.handleBlur}
          error={formik.touched.startTime && formik.errors.startTime}
          rightSection={
            <GoClock
              className="h-4 w-4 cursor-pointer"
              onClick={() => startTimeRef.current?.showPicker()}
            />
          }
        />

        {/* End Time */}
        <TimeInput
          ref={endTimeRef}
          label="End Time"
          withAsterisk
          className="col-span-6"
          size="xs"
          name="endTime"
          value={formik.values.endTime}
          onChange={event => {
            const timeValue = event.currentTarget.value;
            // Convert HH:MM to HH:MM:SS format
            const formattedTime = timeValue ? `${timeValue}:00` : '';
            formik.setFieldValue('endTime', formattedTime);
          }}
          onBlur={formik.handleBlur}
          error={formik.touched.endTime && formik.errors.endTime}
          rightSection={
            <GoClock
              className="h-4 w-4 cursor-pointer"
              onClick={() => endTimeRef.current?.showPicker()}
            />
          }
        />

        {/* Break Time (Minutes) */}
        <NumberInput
          placeholder="Enter Break Time (minutes)"
          label="Break Time (minutes)"
          className="col-span-6"
          size="xs"
          name="breakMinutes"
          value={formik.values.breakMinutes}
          onChange={value => formik.setFieldValue('breakMinutes', value || 0)}
          onBlur={formik.handleBlur}
          min={0}
          max={480}
          error={formik.touched.breakMinutes && formik.errors.breakMinutes}
        />

        {/* Grace In Minutes */}
        <NumberInput
          label="Grace In (minutes)"
          className="col-span-6"
          size="xs"
          name="graceInMinutes"
          value={formik.values.graceInMinutes}
          onChange={value => formik.setFieldValue('graceInMinutes', value || 0)}
          onBlur={formik.handleBlur}
          min={0}
          max={60}
          error={formik.touched.graceInMinutes && formik.errors.graceInMinutes}
        />

        {/* Grace Out Minutes */}
        <NumberInput
          label="Grace Out (minutes)"
          className="col-span-6"
          size="xs"
          name="graceOutMinutes"
          value={formik.values.graceOutMinutes}
          onChange={value =>
            formik.setFieldValue('graceOutMinutes', value || 0)
          }
          onBlur={formik.handleBlur}
          min={0}
          max={60}
          error={
            formik.touched.graceOutMinutes && formik.errors.graceOutMinutes
          }
        />

        {/* Weekly Offs */}
        <div className="col-span-12 mt-3">
          <label className="text-sm font-medium mb-2 block">Weekly Offs</label>
          <div className="grid grid-cols-4 gap-2">
            {DAYS_OF_WEEK.map(day => (
              <Checkbox
                size="xs"
                key={day.value}
                label={day.label}
                checked={
                  formik.values.weeklyOffs?.some(
                    off => off.dayOfWeek === day.value
                  ) || false
                }
                onChange={event =>
                  handleWeeklyOffChange(day.value, event.currentTarget.checked)
                }
              />
            ))}
          </div>
        </div>

        <Switch
          labelPosition="left"
          className="col-span-3 mt-3"
          classNames={{ label: 'font-semibold' }}
          label="Is Night Shift"
          checked={Boolean(formik.values.isNightShift)}
          onChange={event =>
            formik.setFieldValue('isNightShift', event.currentTarget.checked)
          }
        />
        <Switch
          labelPosition="left"
          className="col-span-3 mt-3"
          classNames={{ label: 'font-semibold' }}
          label="Active Shift"
          checked={Boolean(formik.values.status)}
          onChange={event =>
            formik.setFieldValue('status', event.currentTarget.checked)
          }
        />
        <div className="col-span-12">
          <SubmitSection
            isLoading={isLoading || isLoadingUpdate}
            onCancel={onClose}
            confirmText={action === 'create' ? 'Create' : 'Update'}
          />
        </div>
      </div>
    </form>
  );
}
