import SubmitSection from '@/components/common/form/SubmitSection';
import { LeaveGroupDataRequest } from '@/lib/types/leave';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateLeaveGroupMutation,
  useUpdateLeaveGroupMutation,
} from '@/services/api/leave/leaveGroupAPI';
import { Switch, Textarea, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (LeaveGroupDataRequest & { id?: number }) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string()
    .required('Name is required')
    .min(2, 'Name must be at least 2 characters')
    .max(100, 'Name must not exceed 100 characters')
    .trim(),
  description: Yup.string()
    .min(3, 'Description must be at least 3 characters')
    .max(500, 'Description must not exceed 500 characters')
    .trim()
    .optional(),
  status: Yup.boolean().required('Status is required'),
});

export default function LeaveGroupForm({ action, data, onClose }: Props) {
  const [createLeaveGroup, { isLoading }] = useCreateLeaveGroupMutation();
  const [updateLeaveGroup, { isLoading: isLoadingUpdate }] =
    useUpdateLeaveGroupMutation();

  const handleSubmit = async (values: LeaveGroupDataRequest) => {
    try {
      if (action === 'create') {
        const result = await createLeaveGroup(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Group',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateLeaveGroup({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Group',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} Leave Group`,
        message: isDuplicate
          ? 'Duplicate Leave Group found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<LeaveGroupDataRequest>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            description: data.description,
            status: Boolean(data.status),
          }
        : {
            name: '',
            description: '',
            status: true,
          },
    validationSchema,
    validateOnChange: true,
    validateOnBlur: true,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          placeholder="Type name"
          label="Name"
          withAsterisk
          className="col-span-12"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={
            formik.touched.name && formik.errors.name
              ? formik.errors.name
              : undefined
          }
        />
        <Textarea
          placeholder="Type Description"
          label="Description"
          className="col-span-12"
          name="description"
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={
            formik.touched.description && formik.errors.description
              ? formik.errors.description
              : undefined
          }
        />
        <Switch
          labelPosition="left"
          className="col-span-4"
          classNames={{ label: 'font-semibold' }}
          label="Status"
          checked={Boolean(formik.values.status)}
          onChange={event =>
            formik.setFieldValue('status', event.currentTarget.checked)
          }
        />
        <div className="col-span-12">
          <SubmitSection
            onCancel={onClose}
            confirmText={action === 'create' ? 'Create' : 'Update'}
            isLoading={isLoading || isLoadingUpdate}
          />
        </div>
      </div>
    </form>
  );
}
