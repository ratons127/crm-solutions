'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { EmployeeQualificationLevelRequest } from '@/lib/types/qualification';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateQualificationLevelMutation,
  useUpdateQualificationLevelMutation,
} from '@/lib/features/qualification/qualificationLevelAPI';
import { useGetQualificationTypeQuery } from '@/lib/features/qualification/qualificationTypeAPI';
import { Select, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (EmployeeQualificationLevelRequest & { id?: number }) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  qualificationTypeId: Yup.string().required('Qualification Type Name is required'),
  levelName: Yup.string().required('Level Name is required'),
});

export default function QualificationLevelForm({
  action,
  data,
  onClose,
}: Props) {
  const { data: QualificationType } = useGetQualificationTypeQuery(undefined);
  const [createQualificationLevel, { isLoading }] =
    useCreateQualificationLevelMutation();
  const [updateQualificationLevel, { isLoading: isLoadingUpdate }] =
    useUpdateQualificationLevelMutation();

  const handleSubmit = async (values: EmployeeQualificationLevelRequest) => {
    try {
      if (action === 'create') {
        const result = await createQualificationLevel(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Qualification Level',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateQualificationLevel({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Qualification Level',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} Qualification Level`,
        message: isDuplicate
          ? 'Duplicate Qualification type or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<EmployeeQualificationLevelRequest>({
    initialValues:
      action === 'update' && data
        ? {
            qualificationTypeId: String(
              'qualificationTypeId' in data && data.qualificationTypeId
            ),
            levelName: data.levelName,
          }
        : {
            qualificationTypeId: '',
            levelName: '',
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          placeholder="Type level name"
          label="Level Name"
          withAsterisk
          className="col-span-6"
          name="levelName"
          value={formik.values.levelName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.levelName && formik.errors.levelName}
        />
        <Select
          placeholder="Select Qualification Type"
          label="Qualification Type"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            QualificationType?.data?.map(c => ({
              label: c.typeName,
              value: String(c.id),
            })) ?? []
          }
          name="qualificationTypeId"
          value={String(formik.values.qualificationTypeId)}
          onChange={value => formik.setFieldValue('qualificationTypeId', value || '')}
          onBlur={() => formik.setFieldTouched('qualificationTypeId', true)}
          error={formik.touched.qualificationTypeId && formik.errors.qualificationTypeId}
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
