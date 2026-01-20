'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { QualificationRatingMethod } from '@/lib/types/qualification';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useUpdateQualificationRatingMutation } from '@/lib/features/qualification/qualificationRatingAPI';
import { TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  data: QualificationRatingMethod | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  code: Yup.string().trim().required('Code is required'),
  methodName: Yup.string().trim().required('Method name is required'),
});

export default function QualificationRatingMethodForm({
  data,
  onClose,
}: Props) {
  const [updateQualificationRating, { isLoading }] =
    useUpdateQualificationRatingMutation();

  const handleSubmit = async (values: { code: string; methodName: string }) => {
    if (!data?.id) return;

    try {
      const payload = {
        code: values.code.trim(),
        methodName: values.methodName.trim(),
      };

      const result = await updateQualificationRating({
        id: data.id,
        data: payload,
      }).unwrap();

      showApiNotification({
        status: result?.status,
        message: result?.message,
        title: 'Qualification Rating Method Name',
        onSuccess: onClose,
      });
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: 'Error while updating Qualification Rating Method',
        message: isDuplicate
          ? 'Duplicate qualification Rating or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik({
    initialValues: {
      code: data?.code || '',
      methodName: data?.methodName || '',
    },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
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
        <TextInput
          placeholder="Type Method name"
          label="Method Name"
          withAsterisk
          className="col-span-6"
          name="methodName"
          value={formik.values.methodName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.methodName && formik.errors.methodName}
        />

        <div className="col-span-12">
          <SubmitSection
            isLoading={isLoading}
            onCancel={onClose}
            confirmText="Update"
          />
        </div>
      </div>
    </form>
  );
}
