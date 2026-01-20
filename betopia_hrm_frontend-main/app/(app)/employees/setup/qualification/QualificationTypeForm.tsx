'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { EmployeeQualificationTypeRequest } from '@/lib/types/qualification';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateQualificationTypeMutation,
  useUpdateQualificationTypeMutation,
} from '@/lib/features/qualification/qualificationTypeAPI';
import { TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (EmployeeQualificationTypeRequest & { id?: number }) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  typeName: Yup.string().required('Type Name is required'),
});

export default function QualificationTypeForm({
  action,
  data,
  onClose,
}: Props) {
  const [createQualificationType, { isLoading }] =
    useCreateQualificationTypeMutation();
  const [updateQualificationType, { isLoading: isLoadingUpdate }] =
    useUpdateQualificationTypeMutation();

  const handleSubmit = async (values: EmployeeQualificationTypeRequest) => {
    try {
      if (action === 'create') {
        const result = await createQualificationType(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Qualification Type',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateQualificationType({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Qualification Type',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} Qualification type`,
        message: isDuplicate
          ? 'Duplicate Qualification type or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<EmployeeQualificationTypeRequest>({
    initialValues:
      action === 'update' && data
        ? {
            typeName: data.typeName,
          }
        : {
            typeName: '',
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          placeholder="Type Qualification name"
          label="Type Name"
          withAsterisk
          className="col-span-12"
          name="typeName"
          value={formik.values.typeName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.typeName && formik.errors.typeName}
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
