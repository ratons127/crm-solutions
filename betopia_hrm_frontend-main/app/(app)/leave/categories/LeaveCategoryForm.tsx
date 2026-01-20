import SubmitSection from '@/components/common/form/SubmitSection';
import { LeaveCategory, LeaveCategoryCreate } from '@/lib/types/leave';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateLeaveCategoryMutation,
  useUpdateLeaveCategoryMutation,
} from '@/services/api/leave/leaveCategoryAPI';
import { Switch, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: LeaveCategory | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  // parentId: Yup.string().nullable().optional(),
  status: Yup.boolean().optional(),
});

export default function LeaveCategoryForm({ action, data, onClose }: Props) {
  const resourceName = 'Leave Category';

  // const { data: leaveGroupList } = useGetLeaveCategoryListQuery({});

  const [create, { isLoading }] = useCreateLeaveCategoryMutation();
  const [update, { isLoading: isLoadingUpdate }] =
    useUpdateLeaveCategoryMutation();

  const handleSubmit = async (values: LeaveCategoryCreate) => {
    try {
      if (action === 'create') {
        const result = await create(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: resourceName,
          onSuccess: onClose,
        });
      } else if (data) {
        const result = await update({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: resourceName,
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} ${resourceName}`,
        message: isDuplicate
          ? 'Duplicate category name found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<LeaveCategoryCreate>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            // parentId: data.parentId,
            status: data.status,
          }
        : {
            name: '',
            // parentId: null,
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          label="Name"
          placeholder="Type name"
          withAsterisk
          className=" col-span-12"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
        />
        {/* <Select
          className="col-span-12"
          label="Parent Category"
          placeholder="Select parent category"
          clearable
          searchable
          data={leaveGroupList?.data?.map(x => ({
            label: x.name,
            value: x.id.toString(),
          }))}
          name="parentId"
          value={formik.values.parentId?.toString() || ''}
          onChange={value => formik.setFieldValue('parentId', value || null)}
          onBlur={() => formik.setFieldTouched('parentId', true)}
          error={formik.touched.parentId && formik.errors.parentId}
        /> */}
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
            isLoading={isLoading || isLoadingUpdate}
            onCancel={onClose}
            confirmText={action === 'create' ? 'Create' : 'Update'}
          />
        </div>
      </div>
    </form>
  );
}
