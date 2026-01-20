import SubmitSection from '@/components/common/form/SubmitSection';
import {
  useCreateAttendanceDeviceMutation,
  useUpdateAttendanceDeviceMutation,
} from '@/lib/features/attendanceDevice/attendanceDeviceAPI';
import { useGetAttendanceDeviceCategoryListQuery } from '@/lib/features/attendanceDeviceCategory/attendanceDeviceCategoryAPI';
import {
  AttendanceDevice,
  AttendanceDeviceRequest,
} from '@/lib/types/attendanceDevice';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetLocationListQuery } from '@/services/api/admin/location/locationAPI';
import { Select, Switch, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (AttendanceDevice | (AttendanceDeviceRequest & { id?: number })) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  categoryId: Yup.string().required('Category is required'),
  locationId: Yup.string().optional(),
  deviceName: Yup.string().required('Device Name is required'),
  deviceType: Yup.string().required('Device Type is required'),
  serialNumber: Yup.string().required('Serial Number is required'),
  ipAddress: Yup.string()
    .matches(
      /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
      'Invalid IP address format'
    )
    .required('IP Address is required'),
  macAddress: Yup.string()
    .matches(
      /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/,
      'Invalid MAC address format'
    )
    .required('MAC Address is required'),
  firmwareVersion: Yup.string().required('Firmware Version is required'),
  deviceCount: Yup.number()
    .min(1, 'Device count must be at least 1')
    .required('Device Count is required'),
  status: Yup.boolean().optional(),
});

export default function AttendanceDeviceForm({ action, data, onClose }: Props) {
  const { data: categoryList } =
    useGetAttendanceDeviceCategoryListQuery(undefined);
  const { data: locationList } = useGetLocationListQuery({});
  const [createDevice, { isLoading }] = useCreateAttendanceDeviceMutation();
  const [updateDevice, { isLoading: isLoadingUpdate }] =
    useUpdateAttendanceDeviceMutation();

  const handleSubmit = async (values: AttendanceDeviceRequest) => {
    try {
      if (action === 'create') {
        const result = await createDevice(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Attendance Device',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateDevice({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Attendance Device',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');
      notifications.show({
        title: `Error while ${action} attendance device`,
        message: isDuplicate
          ? 'Duplicate device serial number found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<AttendanceDeviceRequest>({
    initialValues:
      action === 'update' && data
        ? {
            categoryId: String(
              'categoryId' in data ? data.categoryId : data.category?.id
            ),
            locationId: String(
              'locationId' in data ? data.locationId : data.location?.id
            ),
            deviceName: data.deviceName,
            deviceType: data.deviceType,
            serialNumber: data.serialNumber,
            ipAddress: data.ipAddress,
            macAddress: data.macAddress,
            firmwareVersion: data.firmwareVersion,
            deviceCount: data.deviceCount,
            status: data.status,
          }
        : {
            categoryId: '',
            locationId: '',
            deviceName: '',
            deviceType: '',
            serialNumber: '',
            ipAddress: '',
            macAddress: '',
            firmwareVersion: '',
            deviceCount: 1,
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center justify-between mb-4">
        <h1>{`Attendance Device ${action === 'create' ? 'Create' : 'Update'}`}</h1>
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
          placeholder="Select Category"
          label="Category"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            categoryList?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
          name="categoryId"
          value={String(formik.values.categoryId)}
          onChange={value => formik.setFieldValue('categoryId', value || '')}
          onBlur={() => formik.setFieldTouched('categoryId', true)}
          error={formik.touched.categoryId && formik.errors.categoryId}
        />
        <Select
          placeholder="Select Location"
          label="Location"
          searchable
          clearable
          className="col-span-6"
          data={
            locationList?.data?.map(l => ({
              label: l.name,
              value: String(l.id),
            })) ?? []
          }
          name="locationId"
          value={String(formik.values.locationId)}
          onChange={value => formik.setFieldValue('locationId', value || '')}
          onBlur={() => formik.setFieldTouched('locationId', true)}
          error={formik.touched.locationId && formik.errors.locationId}
        />
        <TextInput
          placeholder="Type device name"
          label="Device Name"
          withAsterisk
          className="col-span-6"
          name="deviceName"
          value={formik.values.deviceName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.deviceName && formik.errors.deviceName}
        />
        <TextInput
          placeholder="Type device type"
          label="Device Type"
          withAsterisk
          className="col-span-6"
          name="deviceType"
          value={formik.values.deviceType}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.deviceType && formik.errors.deviceType}
        />
        <TextInput
          placeholder="Type serial number"
          label="Serial Number"
          withAsterisk
          className="col-span-6"
          name="serialNumber"
          value={formik.values.serialNumber}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.serialNumber && formik.errors.serialNumber}
        />
        <TextInput
          placeholder="192.168.1.1"
          label="IP Address"
          withAsterisk
          className="col-span-6"
          name="ipAddress"
          value={formik.values.ipAddress}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.ipAddress && formik.errors.ipAddress}
        />
        <TextInput
          placeholder="00:1A:2B:3C:4D:5E"
          label="MAC Address"
          withAsterisk
          className="col-span-6"
          name="macAddress"
          value={formik.values.macAddress}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.macAddress && formik.errors.macAddress}
        />
        <TextInput
          placeholder="Type firmware version"
          label="Firmware Version"
          withAsterisk
          className="col-span-6"
          name="firmwareVersion"
          value={formik.values.firmwareVersion}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={
            formik.touched.firmwareVersion && formik.errors.firmwareVersion
          }
        />
        <TextInput
          placeholder="Type device count"
          label="Device Count"
          withAsterisk
          type="number"
          className="col-span-12"
          name="deviceCount"
          value={formik.values.deviceCount}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.deviceCount && formik.errors.deviceCount}
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
