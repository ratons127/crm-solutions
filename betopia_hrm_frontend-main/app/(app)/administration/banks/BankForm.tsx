import SubmitSection from '@/components/common/form/SubmitSection';
import { Bank, BankRequest } from '@/lib/types/banks';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetCountryListQuery } from '@/services/api/admin/location/countryAPI';
import {
  useCreateBankMutation,
  useUpdateBankMutation,
} from '@/lib/features/banks/banksAPI';
import { Select, Switch, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (Bank | (BankRequest & { id?: number })) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Name is required'),
  shortName: Yup.string().optional(),
  bankCode: Yup.string().optional(),
  swiftCode: Yup.string().optional(),
  countryId: Yup.string().required('Country is required'),
  website: Yup.string().url('Invalid URL format').optional(),
  supportEmail: Yup.string().email('Invalid email address').optional(),
  status: Yup.boolean().optional(),
});

export default function BankForm({ action, data, onClose }: Props) {
  const { data: countryList } = useGetCountryListQuery({});
  const [createBank, { isLoading }] = useCreateBankMutation();
  const [updateBank, { isLoading: isLoadingUpdate }] = useUpdateBankMutation();

  const handleSubmit = async (values: BankRequest) => {
    try {
      if (action === 'create') {
        const result = await createBank(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Bank',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateBank({ id: data.id, data: values }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Bank',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');
      notifications.show({
        title: `Error while ${action} bank`,
        message: isDuplicate
          ? 'Duplicate bank name or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<BankRequest>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            shortName: data.shortName,
            bankCode: data.bankCode,
            swiftCode: data.swiftCode,
            countryId: String(
              'countryId' in data ? data.countryId : data.country?.id
            ),
            website: data.website,
            supportEmail: data.supportEmail,
            status: data.status,
          }
        : {
            name: '',
            shortName: '',
            bankCode: '',
            swiftCode: '',
            countryId: '',
            website: '',
            supportEmail: '',
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 mb-4">
        <h1 className="text-xl sm:text-2xl">{`Bank ${action === 'create' ? 'Create' : 'Update'}`}</h1>
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
          placeholder="Type bank name"
          label="Bank Name"
          withAsterisk
          className="col-span-12 sm:col-span-6"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
        />
        <TextInput
          placeholder="Type short name"
          label="Short Name"
          className="col-span-12 sm:col-span-6"
          name="shortName"
          value={formik.values.shortName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.shortName && formik.errors.shortName}
        />
        <TextInput
          placeholder="Type bank code"
          label="Bank Code"
          className="col-span-12 sm:col-span-6"
          name="bankCode"
          value={formik.values.bankCode}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.bankCode && formik.errors.bankCode}
        />

        <TextInput
          placeholder="Type Swift Code"
          label="Swift Code"
          className="col-span-12 sm:col-span-6"
          name="swiftCode"
          value={formik.values.swiftCode}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.swiftCode && formik.errors.swiftCode}
        />
        <Select
          placeholder="Select Country"
          label="Country"
          withAsterisk
          searchable
          clearable
          className="col-span-12 sm:col-span-6"
          data={
            countryList?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
          name="countryId"
          value={String(formik.values.countryId)}
          onChange={(value) => formik.setFieldValue('countryId', value || '')}
          onBlur={() => formik.setFieldTouched('countryId', true)}
          error={formik.touched.countryId && formik.errors.countryId}
        />

        <TextInput
          placeholder="Type website URL"
          label="Website"
          className="col-span-12 sm:col-span-6"
          name="website"
          value={formik.values.website}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.website && formik.errors.website}
        />

        <TextInput
          placeholder="Type support email"
          label="Support Email"
          className="col-span-12"
          name="supportEmail"
          value={formik.values.supportEmail}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.supportEmail && formik.errors.supportEmail}
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
