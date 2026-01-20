import SubmitSection from '@/components/common/form/SubmitSection';
import { 
  AttendanceDeviceCategory, 
  AttendanceDeviceCategoryRequest 
} from '@/lib/types/attendanceDeviceCategory';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateAttendanceDeviceCategoryMutation,
  useUpdateAttendanceDeviceCategoryMutation,
} from '@/lib/features/attendanceDeviceCategory/attendanceDeviceCategoryAPI';
import { Switch, TextInput, Textarea } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (AttendanceDeviceCategory | (AttendanceDeviceCategoryRequest & { id?: number })) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  communicationType: Yup.string().required('Communication Type is required'),
  biometricMode: Yup.string().required('Biometric Mode is required'),
  description: Yup.string().optional(),
  status: Yup.boolean().optional(),
});

export default function DeviceCategoryForm({ action, data, onClose }: Props) {
  const [createCategory, { isLoading }] = useCreateAttendanceDeviceCategoryMutation();
  const [updateCategory, { isLoading: isLoadingUpdate }] = useUpdateAttendanceDeviceCategoryMutation();

  const handleSubmit = async (values: AttendanceDeviceCategoryRequest) => {
    try {
      if (action === 'create') {
        const result = await createCategory(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Device Category',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateCategory({ id: data.id, data: values }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Device Category',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');
      notifications.show({
        title: `Error while ${action} device category`,
        message: isDuplicate
          ? 'Duplicate device category name found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<AttendanceDeviceCategoryRequest>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            communicationType: data.communicationType,
            biometricMode: data.biometricMode,
            description: data.description,
            status: data.status,
          }
        : {
            name: '',
            communicationType: '',
            biometricMode: '',
            description: '',
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center justify-between">
        <h1>{`Device Category ${action === 'create' ? 'Create' : 'Update'}`}</h1>
        <Switch
          labelPosition="left"
          className="col-span-4"
          classNames={{ label: 'font-semibold' }}
          label="Active Status"
          checked={Boolean(formik.values.status)}
          onChange={(event) => formik.setFieldValue('status', event.currentTarget.checked)}
        />
      </div>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          placeholder="Type device category name"
          label="Name"
          withAsterisk
          className="col-span-12"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
        />
        <TextInput
          placeholder="Type communication type"
          label="Communication Type"
          withAsterisk
          className="col-span-6"
          name="communicationType"
          value={formik.values.communicationType}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.communicationType && formik.errors.communicationType}
        />
        <TextInput
          placeholder="Type biometric mode"
          label="Biometric Mode"
          withAsterisk
          className="col-span-6"
          name="biometricMode"
          value={formik.values.biometricMode}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.biometricMode && formik.errors.biometricMode}
        />
        <Textarea
          placeholder="Type description"
          label="Description"
          className="col-span-12"
          name="description"
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.description && formik.errors.description}
          rows={4}
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
