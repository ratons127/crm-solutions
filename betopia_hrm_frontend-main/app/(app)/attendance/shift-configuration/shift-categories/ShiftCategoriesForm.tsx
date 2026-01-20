import SubmitSection from '@/components/common/form/SubmitSection';
import {
  useCreateShiftCategoryMutation,
  useUpdateShiftCategoryMutation,
} from '@/lib/features/shiftCategories/shiftCategoriesAPI';
import {
  ShiftCategory,
  ShiftCategoryRequest,
} from '@/lib/types/shiftCategories';
import { showApiNotification } from '@/lib/utils/showNotification';
import { Switch, TextInput, Textarea } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (ShiftCategory | (ShiftCategoryRequest & { id?: number })) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string()
    .required('Shift Category Name is required')
    .min(2, 'Shift Category Name must be at least 2 characters')
    .max(100, 'Shift Category Name must not exceed 100 characters')
    .trim(),
  type: Yup.string()
    .required('Shift Type is required')
    .min(2, 'Shift Type must be at least 2 characters')
    .max(50, 'Shift Type must not exceed 50 characters')
    .trim(),
  description: Yup.string()
    .min(3, 'Shift Category Description must be at least 3 characters')
    .max(500, 'Shift Category Description must not exceed 500 characters')
    .trim()
    .optional(),
  status: Yup.boolean().optional(),
});

export default function ShiftCategoriesForm({ action, data, onClose }: Props) {
  const [createShiftCategory, { isLoading }] = useCreateShiftCategoryMutation();
  const [updateShiftCategory, { isLoading: isLoadingUpdate }] =
    useUpdateShiftCategoryMutation();

  const handleSubmit = async (values: ShiftCategoryRequest) => {
    try {
      if (action === 'create') {
        const result = await createShiftCategory(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Shift Category',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateShiftCategory({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Shift Category',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');
      notifications.show({
        title: `Error while ${action} shift category`,
        message: isDuplicate
          ? 'Duplicate shift category name found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<ShiftCategoryRequest>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            type: data.type,
            description: data.description || '',
            status: data.status,
          }
        : {
            name: '',
            type: '',
            description: '',
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center justify-between">
        <h1 className="text-lg font-semibold">{`Shift Category ${action === 'create' ? 'Create' : 'Update'}`}</h1>
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
          placeholder="Enter shift category name"
          label="Shift Category Name"
          withAsterisk
          className="col-span-6"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
        />
        <TextInput
          placeholder="Enter shift type"
          label="Shift Type"
          withAsterisk
          className="col-span-6"
          name="type"
          value={formik.values.type}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.type && formik.errors.type}
        />
        <Textarea
          placeholder="Enter description (optional)"
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
