import SubmitSection from '@/components/common/form/SubmitSection';
import { EmploymentTypesRequest } from '@/lib/types/employment';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateEmploymentTypesMutation,
  useUpdateEmploymentTypesMutation,
} from '@/lib/features/employment/employmentTypesAPI';
import { Switch, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (EmploymentTypesRequest & { id?: number }) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string()
    .required('Name is required')
    .min(2, 'Name must be at least 2 characters')
    .max(100, 'Name must not exceed 100 characters')
    .trim(),
  description: Yup.string()
    .required('Description is required')
    .min(3, 'Description must be at least 3 characters')
    .max(500, 'Description must not exceed 500 characters')
    .trim(),
  status: Yup.boolean().required('Status is required'),
});

export default function EmploymentTypesForm({ action, data, onClose }: Props) {
  const [createEmploymentTypes, { isLoading }] =
    useCreateEmploymentTypesMutation();
  const [updateEmploymentTypes, { isLoading: isLoadingUpdate }] =
    useUpdateEmploymentTypesMutation();

  const handleSubmit = async (values: EmploymentTypesRequest) => {
    try {
      if (action === 'create') {
        const result = await createEmploymentTypes(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Employment Types',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateEmploymentTypes({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Employment Types',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} Employment Types`,
        message: isDuplicate
          ? 'Duplicate Employment name or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<EmploymentTypesRequest>({
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
      <div className="col-span-12 flex items-center justify-between">
        <h1>{`Employment Types ${action === 'create' ? 'Create' : 'Update'}`}</h1>
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
        <TextInput
          placeholder="Type Description URL"
          label="Description"
          withAsterisk
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
