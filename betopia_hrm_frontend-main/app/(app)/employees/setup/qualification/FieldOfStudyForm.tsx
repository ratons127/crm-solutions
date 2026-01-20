'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { EmployeeFieldOfStudyRequest } from '@/lib/types/qualification';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateFieldOfStudyMutation,
  useUpdateFieldOfStudyMutation,
} from '@/lib/features/qualification/fieldOfStudyAPI';
import { useGetQualificationLevelQuery } from '@/lib/features/qualification/qualificationLevelAPI';
import { Select, TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (EmployeeFieldOfStudyRequest & { id?: number }) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  qualificationLevelId: Yup.string().required('Qualification Level is required'),
  fieldStudyName: Yup.string().required('Field of Study is required'),
});

export default function FieldOfStudyForm({ action, data, onClose }: Props) {
  const { data: QualificationLevel } = useGetQualificationLevelQuery(undefined);
  const [createFieldOfStudy, { isLoading }] = useCreateFieldOfStudyMutation();
  const [updateFieldOfStudy, { isLoading: isLoadingUpdate }] =
    useUpdateFieldOfStudyMutation();

  const handleSubmit = async (values: EmployeeFieldOfStudyRequest) => {
    try {
      if (action === 'create') {
        const result = await createFieldOfStudy(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Field of Study',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateFieldOfStudy({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Field of Study',
          onSuccess: onClose,
        });
      }

      notifications.show({
        title: `Field of Study ${action === 'create' ? 'created' : 'updated'} successfully`,
        message: '',
        color: 'green',
      });

      onClose();
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} Field of Study`,
        message: isDuplicate
          ? 'Duplicate Field of Study type or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<EmployeeFieldOfStudyRequest>({
    initialValues:
      action === 'update' && data
        ? {
            qualificationLevelId: String(
              'qualificationLevelId' in data && data.qualificationLevelId
            ),
            fieldStudyName: data.fieldStudyName,
          }
        : {
            qualificationLevelId: '',
            fieldStudyName: '',
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          placeholder="Type field of study"
          label="Field of Study"
          withAsterisk
          className="col-span-6"
          name="fieldStudyName"
          value={formik.values.fieldStudyName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.fieldStudyName && formik.errors.fieldStudyName}
        />
        <Select
          placeholder="Select Qualification Level"
          label="Qualification Level"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            QualificationLevel?.data?.map(c => ({
              label: c.levelName,
              value: String(c.id),
            })) ?? []
          }
          name="qualificationLevelId"
          value={String(formik.values.qualificationLevelId)}
          onChange={value => formik.setFieldValue('qualificationLevelId', value || '')}
          onBlur={() => formik.setFieldTouched('qualificationLevelId', true)}
          error={formik.touched.qualificationLevelId && formik.errors.qualificationLevelId}
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
