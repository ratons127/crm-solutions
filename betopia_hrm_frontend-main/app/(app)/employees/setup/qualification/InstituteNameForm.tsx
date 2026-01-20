'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { InstituteNameRequest } from '@/lib/types/qualification';
import { showApiNotification } from '@/lib/utils/showNotification';
import {
  useCreateInstituteNameMutation,
  useUpdateInstituteNameMutation,
} from '@/lib/features/qualification/instituteNameAPI';
import { TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: (InstituteNameRequest & { id?: number }) | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Institute Name is required'),
});

export default function InstituteNameForm({ action, data, onClose }: Props) {
  const [createInstituteName, { isLoading }] = useCreateInstituteNameMutation();
  const [updateInstituteName, { isLoading: isLoadingUpdate }] =
    useUpdateInstituteNameMutation();

  const handleSubmit = async (values: InstituteNameRequest) => {
    try {
      if (action === 'create') {
        const result = await createInstituteName(values).unwrap();
        // Show Create Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Institute Name',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateInstituteName({
          id: data.id,
          data: values,
        }).unwrap();
        // Show Update Notification
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Institute Name',
          onSuccess: onClose,
        });
      }
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

  const formik = useFormik<InstituteNameRequest>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
          }
        : {
            name: '',
          },
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          placeholder="Type Institute name"
          label="Institute Name"
          withAsterisk
          className="col-span-12"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.name && formik.errors.name}
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
