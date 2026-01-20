import { Switch, Textarea, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

import SubmitSection from '@/components/common/form/SubmitSection';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateLeaveTypeMutation,
  useUpdateLeaveTypeMutation,
} from '@/services/api/leave/leaveTypeAPI';
import { LeaveType, LeaveTypeCreate } from '../../../lib/types/leave';

type LeaveTypeFormProps =
  | {
      action: 'edit';
      data: LeaveType;
      onClose: () => void;
    }
  | {
      action: 'create';
      onClose: () => void;
    };

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Leave Type name is required'),
  code: Yup.string().required('Code is required'),
  description: Yup.string().optional(),
  status: Yup.boolean().optional(),
});

export default function LeaveTypeForm(props: LeaveTypeFormProps) {
  const { action, onClose } = props;
  const [createLeaveType, { isLoading }] = useCreateLeaveTypeMutation();
  const [updateLeaveType, { isLoading: isLoadingUpdate }] =
    useUpdateLeaveTypeMutation();

  const handleSubmit = async (values: LeaveTypeCreate) => {
    try {
      if (action === 'create') {
        const result = await createLeaveType(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Type',
          onSuccess: onClose,
        });
      } else {
        const result = await updateLeaveType({
          id: props.data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Type',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      notifications.show({
        title: 'Leave Type',
        message: error?.data?.message,
        color: 'red',
      });
    }
  };

  const formik = useFormik<LeaveTypeCreate>({
    initialValues:
      action === 'edit'
        ? {
            status: props.data.status,
            code: props.data.code,
            name: props.data.name,
            description: props.data.description,
          }
        : {
            status: true,
            code: '',
            name: '',
            description: '',
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  const heading = ` ${action === 'create' ? 'Create' : 'Update'} Leave Type`;

  return (
    <div>
      <form onSubmit={formik.handleSubmit} className="grid grid-cols-12 gap-3">
        <div className="col-span-12 flex items-center">
          <h1 className="  text-lg">{heading}</h1>
        </div>
        <TextInput
          withAsterisk
          label="Leave Type Name"
          placeholder="leave type name"
          className=" col-span-8"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
        />
        <TextInput
          withAsterisk
          className=" col-span-4"
          label=" Leave Code"
          placeholder="leave code"
          name="code"
          value={formik.values.code}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.code && formik.errors.code}
        />

        <Textarea
          className="col-span-12"
          rows={3}
          label={'Description'}
          placeholder="description about leave"
          name="description"
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.description && formik.errors.description}
        />
        <Switch
          labelPosition="left"
          classNames={{ label: 'font-semibold' }}
          label="Status"
          checked={Boolean(formik.values.status)}
          onChange={event =>
            formik.setFieldValue('status', event.currentTarget.checked)
          }
        />

        <div className="col-span-12">
          <SubmitSection
            isLoading={isLoading || isLoadingUpdate}
            cancelText="Cancel"
            confirmText={action === 'create' ? 'Create' : 'Update'}
            onCancel={onClose}
          />
        </div>
      </form>
    </div>
  );
}
