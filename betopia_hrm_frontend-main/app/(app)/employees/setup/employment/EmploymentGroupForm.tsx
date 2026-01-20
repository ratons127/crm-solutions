import SubmitSection from '@/components/common/form/SubmitSection';
import {
  EmploymentGroupData,
  EmploymentGroupRequest,
} from '@/lib/types/employment';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateEmploymentGroupMutation,
  useGetCompanyListQuery,
  useGetWorkPlacesListQuery,
  useUpdateEmploymentGroupMutation,
} from '@/lib/features/employment/employmentGroupAPI';
import { Select, Switch, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data:
    | (EmploymentGroupData | (EmploymentGroupRequest & { id?: number }))
    | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  code: Yup.string().required('Code is required'),
  description: Yup.string().required('Description is required'),
  companyId: Yup.string().required('Company is required'),
  workplaceId: Yup.string().required('Workplace is required'),
  status: Yup.boolean().optional(),
});

export default function EmploymentGroupForm({ action, data, onClose }: Props) {
  const { data: companyList } = useGetCompanyListQuery(undefined);
  const { data: workplaces } = useGetWorkPlacesListQuery(undefined);
  const [createEmploymentGroup, { isLoading }] =
    useCreateEmploymentGroupMutation();
  const [updateEmploymentGroup, { isLoading: isLoadingUpdate }] =
    useUpdateEmploymentGroupMutation();

  const handleSubmit = async (values: EmploymentGroupRequest) => {
    try {
      if (action === 'create') {
        const result = await createEmploymentGroup(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Employment Group',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateEmploymentGroup({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Employment Group',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} Employment`,
        message: isDuplicate
          ? 'Duplicate Employment name or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<EmploymentGroupRequest>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            code: data.code,
            description: data.description,
            companyId: String(
              'companyId' in data ? data.companyId : data.company.id
            ),
            workplaceId: String(
              'workplaceId' in data ? data.workplaceId : data.workplace.id
            ),
            status: data.status,
          }
        : {
            name: '',
            code: '',
            description: '',
            companyId: '',
            workplaceId: '',
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center justify-between">
        <h1>{`Employment Group ${action === 'create' ? 'Create' : 'Update'}`}</h1>
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
          className="col-span-6"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
        />
        <TextInput
          placeholder="Type Code"
          label="Code"
          withAsterisk
          className="col-span-6"
          name="code"
          value={formik.values.code}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.code && formik.errors.code}
        />
        <Select
          placeholder="Select Company"
          label="Company"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            companyList?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
          name="companyId"
          value={String(formik.values.companyId)}
          onChange={value => formik.setFieldValue('companyId', value || '')}
          onBlur={() => formik.setFieldTouched('companyId', true)}
          error={formik.touched.companyId && formik.errors.companyId}
        />
        <Select
          placeholder="Select Workplace"
          label="Workplace"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            workplaces?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
          name="workplaceId"
          value={String(formik.values.workplaceId)}
          onChange={value => formik.setFieldValue('workplaceId', value || '')}
          onBlur={() => formik.setFieldTouched('workplaceId', true)}
          error={formik.touched.workplaceId && formik.errors.workplaceId}
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
          error={formik.touched.description && formik.errors.description}
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
