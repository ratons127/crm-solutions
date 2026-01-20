import SubmitSection from '@/components/common/form/SubmitSection';
import { useGetAttendanceDeviceListQuery } from '@/lib/features/attendanceDevice/attendanceDeviceAPI';
import {
  useCreateAttendanceDeviceAssignMutation,
  useUpdateAttendanceDeviceAssignMutation,
} from '@/lib/features/attendanceDeviceAssign/attendanceDeviceAssignAPI';
import { useAppSelector } from '@/lib/hooks';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetEmployeeListQuery } from '@/services/api/employee/employeeProfileAPI';
import { Select, Switch, TextInput, Textarea } from '@mantine/core';
import { DateTimePicker } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

interface AssignDeviceRequest {
  attendanceDeviceId: number;
  employeeId: number;
  deviceUserId: number;
  assignedBy: number;
  assignedAt: string;
  notes: string;
  status: boolean;
}

type Props = {
  action: 'create' | 'update' | null;
  data?: any;
  onClose: () => void;
  selectedEmployeeId?: string;
};

const validationSchema = Yup.object().shape({
  attendanceDeviceId: Yup.number().required('Device is required'),
  employeeId: Yup.number().required('Employee is required'),
  deviceUserId: Yup.number()
    .min(1, 'Device User ID must be at least 1')
    .required('Device User ID is required'),
  assignedBy: Yup.number().required('Assigned By is required'), // Auto-populated from current user
  assignedAt: Yup.string().required('Assigned At is required'),
  notes: Yup.string().optional(),
  status: Yup.boolean().optional(),
});

export default function AssignDeviceForm({
  action,
  data,
  onClose,
  selectedEmployeeId,
}: Props) {
  // Fetch data from API
  const { data: deviceListData } = useGetAttendanceDeviceListQuery(undefined);
  const { data: employeeListData } = useGetEmployeeListQuery(undefined);
  const currentUser = useAppSelector(state => state.app.auth.user);

  // Mutations
  const [createAssignment, { isLoading: isCreating }] =
    useCreateAttendanceDeviceAssignMutation();
  const [updateAssignment, { isLoading: isUpdating }] =
    useUpdateAttendanceDeviceAssignMutation();

  const handleSubmit = async (values: AssignDeviceRequest) => {
    try {
      if (action === 'create') {
        const result = await createAssignment(values).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Device Assignment',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateAssignment({
          id: data.id,
          data: values,
        }).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Device Assignment',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      notifications.show({
        title: `Error while ${action} device assignment`,
        message: error?.data?.message || 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<AssignDeviceRequest>({
    initialValues:
      action === 'update' && data
        ? {
            attendanceDeviceId: data.attendanceDeviceId,
            employeeId: data.employeeId,
            deviceUserId: data.deviceUserId,
            assignedBy: currentUser?.id ? parseInt(currentUser.id) : 0,
            assignedAt: data.assignedAt,
            notes: data.notes || '',
            status: data.status,
          }
        : {
            attendanceDeviceId: 0,
            employeeId: selectedEmployeeId ? parseInt(selectedEmployeeId) : 0,
            deviceUserId: 0,
            assignedBy: currentUser?.id ? parseInt(currentUser.id) : 0,
            assignedAt: new Date().toISOString(),
            notes: '',
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center justify-between mb-4">
        <h1 className="text-xl font-semibold">
          {action === 'create' ? 'Assign Device' : 'Update Device Assignment'}
        </h1>
        <Switch
          labelPosition="left"
          className="col-span-4"
          classNames={{ label: 'font-semibold' }}
          label="Active Status"
          checked={Boolean(formik.values.status)}
          onChange={event =>
            formik.setFieldValue('status', event.currentTarget.checked)
          }
        />
      </div>
      <div className="grid grid-cols-12 gap-3">
        <Select
          placeholder="Select Device"
          label="Attendance Device"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            deviceListData?.data?.map(d => ({
              label: d.deviceName,
              value: String(d.id),
            })) ?? []
          }
          name="attendanceDeviceId"
          value={String(formik.values.attendanceDeviceId || '')}
          onChange={value =>
            formik.setFieldValue(
              'attendanceDeviceId',
              value ? parseInt(value) : 0
            )
          }
          onBlur={() => formik.setFieldTouched('attendanceDeviceId', true)}
          error={
            formik.touched.attendanceDeviceId &&
            formik.errors.attendanceDeviceId
          }
        />
        <TextInput
          placeholder="Enter device user ID"
          label="Device User ID"
          withAsterisk
          type="number"
          className="col-span-6"
          name="deviceUserId"
          value={formik.values.deviceUserId || ''}
          onChange={e =>
            formik.setFieldValue('deviceUserId', parseInt(e.target.value) || 0)
          }
          onBlur={formik.handleBlur}
          error={formik.touched.deviceUserId && formik.errors.deviceUserId}
        />
        <Select
          placeholder="Select Employee"
          label="Employee"
          withAsterisk
          searchable
          clearable
          className="col-span-12"
          data={
            employeeListData?.data?.map(e => ({
              label: `${e.firstName} ${e.lastName} (ID: ${e.id})`,
              value: String(e.id),
            })) ?? []
          }
          name="employeeId"
          value={String(formik.values.employeeId || '')}
          onChange={value =>
            formik.setFieldValue('employeeId', value ? parseInt(value) : 0)
          }
          onBlur={() => formik.setFieldTouched('employeeId', true)}
          error={formik.touched.employeeId && formik.errors.employeeId}
        />
        <DateTimePicker
          size="xs"
          placeholder="Select date and time"
          label="Assigned At"
          withAsterisk
          className="col-span-12"
          value={
            formik.values.assignedAt ? new Date(formik.values.assignedAt) : null
          }
          onChange={value => formik.setFieldValue('assignedAt', value || '')}
          error={formik.touched.assignedAt && formik.errors.assignedAt}
        />
        <Textarea
          placeholder="Enter notes (optional)"
          label="Notes"
          className="col-span-12"
          name="notes"
          value={formik.values.notes}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.notes && formik.errors.notes}
          rows={4}
        />
        <div className="col-span-12">
          <SubmitSection
            isLoading={isCreating || isUpdating}
            onCancel={onClose}
            confirmText={action === 'create' ? 'Assign' : 'Update'}
          />
        </div>
      </div>
    </form>
  );
}
