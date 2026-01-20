import SubmitSection from '@/components/common/form/SubmitSection';
import { BankBranch, BankBranchRequest } from '@/lib/types/banks';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetLocationListQuery } from '@/services/api/admin/location/locationAPI';
import {
  useCreateBankBranchMutation,
  useUpdateBankBranchMutation,
} from '@/lib/features/banks/bankBranchAPI';
import { useGetBankListQuery } from '@/lib/features/banks/banksAPI';
import { Select, Switch, Textarea, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (BankBranch | (BankBranchRequest & { id?: number })) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  branchName: Yup.string().required('Branch Name is required'),
  bankId: Yup.string().required('Bank is required'),
  locationId: Yup.string().optional(),
  branchCode: Yup.string().optional(),
  routingNo: Yup.string()
    .matches(/^\d*$/, 'Routing No. must be numeric')
    .optional(),
  swiftCode: Yup.string().optional(),
  email: Yup.string().email('Invalid email address').optional(),
  addressLine1: Yup.string().optional(),
  addressLine2: Yup.string().optional(),
  district: Yup.string().optional(),
  status: Yup.boolean().optional(),
});

export default function BankBranchForm({ action, data, onClose }: Props) {
  const { data: bankList } = useGetBankListQuery(undefined);
  const { data: locationList } = useGetLocationListQuery({});
  const [createBankBranch, { isLoading }] = useCreateBankBranchMutation();
  const [updateBankBranch, { isLoading: isLoadingUpdate }] =
    useUpdateBankBranchMutation();

  const handleSubmit = async (values: BankBranchRequest) => {
    try {
      if (action === 'create') {
        const result = await createBankBranch(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Bank Branch',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateBankBranch({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Bank Branch',
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

  const formik = useFormik<BankBranchRequest>({
    initialValues:
      action === 'update' && data
        ? {
            branchName: data.branchName,
            bankId: String('bankId' in data ? data.bankId : data.bank?.id),
            locationId: String(
              'locationId' in data ? data.locationId : data.location?.id
            ),
            branchCode: data.branchCode,
            routingNo: data.routingNo,
            swiftCode: data.swiftCode,
            email: data.email,
            addressLine1: data.addressLine1,
            addressLine2: data.addressLine2,
            district: data.district,
            status: data.status,
          }
        : {
            branchName: '',
            bankId: '',
            locationId: '',
            branchCode: '',
            routingNo: '',
            swiftCode: '',
            email: '',
            addressLine1: '',
            addressLine2: '',
            district: '',
            status: true,
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 mb-4">
        <h1 className="text-xl sm:text-2xl">{`Bank Branch ${action === 'create' ? 'Create' : 'Update'}`}</h1>
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
          placeholder="Select Bank"
          label="Bank"
          withAsterisk
          searchable
          clearable
          className="col-span-12 sm:col-span-6"
          data={
            bankList?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
          name="bankId"
          value={String(formik.values.bankId)}
          onChange={value => formik.setFieldValue('bankId', value || '')}
          onBlur={() => formik.setFieldTouched('bankId', true)}
          error={formik.touched.bankId && formik.errors.bankId}
        />
        <TextInput
          placeholder="Type Branch name"
          label="Branch Name"
          withAsterisk
          className="col-span-12 sm:col-span-6"
          name="branchName"
          value={formik.values.branchName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.branchName && formik.errors.branchName}
        />
        <Select
          placeholder="Select Location"
          label="Location"
          searchable
          clearable
          className="col-span-12 sm:col-span-6"
          data={
            locationList?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
          name="locationId"
          value={String(formik.values.locationId)}
          onChange={value => formik.setFieldValue('locationId', value || '')}
          onBlur={() => formik.setFieldTouched('locationId', true)}
          error={formik.touched.locationId && formik.errors.locationId}
        />

        <TextInput
          placeholder="Type Branch code"
          label="Branch code"
          className="col-span-12 sm:col-span-6"
          name="branchCode"
          value={formik.values.branchCode}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.branchCode && formik.errors.branchCode}
        />
        <TextInput
          placeholder="Type Routing No."
          label="Routing No."
          className="col-span-12 sm:col-span-6"
          name="routingNo"
          value={formik.values.routingNo}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.routingNo && formik.errors.routingNo}
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
        <TextInput
          placeholder="Type Email"
          label="Email"
          className="col-span-12 sm:col-span-6"
          name="email"
          value={formik.values.email}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.email && formik.errors.email}
        />
        <TextInput
          placeholder="Type District"
          label="District"
          className="col-span-12 sm:col-span-6"
          name="district"
          value={formik.values.district}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.district && formik.errors.district}
        />
        <Textarea
          placeholder="Type AddressLine1"
          label="AddressLine1"
          className="col-span-12 sm:col-span-6"
          name="addressLine1"
          value={formik.values.addressLine1}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.addressLine1 && formik.errors.addressLine1}
        />
        <Textarea
          placeholder="Type AddressLine2"
          label="AddressLine2"
          className="col-span-12 sm:col-span-6"
          name="addressLine2"
          value={formik.values.addressLine2}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.addressLine2 && formik.errors.addressLine2}
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
