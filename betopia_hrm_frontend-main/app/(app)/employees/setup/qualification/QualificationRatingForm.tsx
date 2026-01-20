'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import {
  useCreateQualificationRatingMutation,
  useUpdateQualificationRatingMutation,
} from '@/lib/features/qualification/qualificationRatingAPI';
import {
  QualificationRatingMethod,
  QualificationRatingMethodDetail,
} from '@/lib/types/qualification';
import { showApiNotification } from '@/lib/utils/showNotification';
import { TextInput } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useMemo } from 'react';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'add-more' | 'update' | null;
  data:
    | (QualificationRatingMethod & {
        singleRatingDetails?: QualificationRatingMethodDetail;
      })
    | null;
  onClose: () => void;
};

// Readonly input styles for better reusability
const READONLY_INPUT_STYLES = {
  input: {
    backgroundColor: '#f9fafb',
    cursor: 'not-allowed',
    color: '#6b7280',
  },
};

const getInitialValues = (action: Props['action'], data: Props['data']) => {
  if (action === 'add-more' && data) {
    return {
      code: data.code,
      methodName: data.methodName,
      grade: '',
      maximumMark: '',
      minimumMark: '',
      averageMark: '',
    };
  }

  if (action === 'update' && data?.singleRatingDetails) {
    return {
      code: data.code,
      methodName: data.methodName,
      grade: data.singleRatingDetails.grade,
      maximumMark: String(data.singleRatingDetails.maximumMark),
      minimumMark: String(data.singleRatingDetails.minimumMark),
      averageMark: String(data.singleRatingDetails.averageMark),
    };
  }

  return {
    code: '',
    methodName: '',
    grade: '',
    maximumMark: '',
    minimumMark: '',
    averageMark: '',
  };
};

const validationSchema = Yup.object().shape({
  code: Yup.string().trim().required('Code is required'),
  methodName: Yup.string().trim().required('Method name is required'),
  grade: Yup.string().trim().required('Grade name is required'),
  maximumMark: Yup.number()
    .typeError('Maximum mark must be a number')
    .required('Maximum mark is required'),
  minimumMark: Yup.number()
    .typeError('Minimum mark must be a number')
    .required('Minimum mark is required')
    .test(
      'min-less-than-max',
      'Minimum mark cannot be greater than maximum mark',
      function (value) {
        const { maximumMark } = this.parent;
        return value == null || maximumMark == null || value <= maximumMark;
      }
    ),
  averageMark: Yup.number()
    .typeError('Average mark must be a number')
    .required('Average mark is required')
    .test(
      'avg-in-range',
      'Average mark must be between minimum and maximum mark',
      function (value) {
        const { minimumMark, maximumMark } = this.parent;
        return (
          value == null ||
          minimumMark == null ||
          maximumMark == null ||
          (value >= minimumMark && value <= maximumMark)
        );
      }
    ),
});

export default function QualificationRatingForm({
  action,
  data,
  onClose,
}: Props) {
  const [createQualificationRating, { isLoading }] =
    useCreateQualificationRatingMutation();
  const [updateQualificationRating, { isLoading: isLoadingUpdate }] =
    useUpdateQualificationRatingMutation();

  const isReadOnly = action === 'add-more' || action === 'update';

  const confirmText = useMemo(() => {
    if (action === 'create') return 'Create';
    if (action === 'update') return 'Update';
    return 'Add More';
  }, [action]);

  // Submit Handler
  const handleSubmit = async (values: {
    code: string;
    methodName: string;
    grade: string;
    maximumMark: string;
    minimumMark: string;
    averageMark: string;
  }) => {
    try {
      const newDetail = {
        id: data?.singleRatingDetails?.id,
        grade: values.grade,
        maximumMark: Number(values.maximumMark),
        minimumMark: Number(values.minimumMark),
        averageMark: Number(values.averageMark),
      };

      let payload;

      if (action === 'add-more' && data) {
        payload = {
          code: data.code,
          methodName: data.methodName,
          qualificationRatingMethodDetails: [
            ...(data.qualificationRatingMethodDetails || []),
            newDetail,
          ],
        };
      } else if (action === 'update' && data) {
        payload = {
          code: data.code,
          methodName: data.methodName,
          qualificationRatingMethodDetails: (
            data.qualificationRatingMethodDetails || []
          ).map(detail =>
            detail.id === data.singleRatingDetails?.id ? newDetail : detail
          ),
        };
      } else {
        payload = {
          code: values.code,
          methodName: values.methodName,
          qualificationRatingMethodDetails: [newDetail],
        };
      }

      if (action === 'create') {
        const result = await createQualificationRating(payload).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Qualification Rating',
          onSuccess: onClose,
        });
      } else if ((action === 'add-more' || action === 'update') && data) {
        const result = await updateQualificationRating({
          id: data.id,
          data: payload,
        }).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Qualification Rating',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action === 'create' ? 'creating' : 'updating'} Qualification Rating`,
        message: isDuplicate
          ? 'Duplicate qualification Rating or code found'
          : 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik({
    initialValues: getInitialValues(action, data),
    validationSchema,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <TextInput
          readOnly={isReadOnly}
          placeholder="Type Code"
          label="Code"
          withAsterisk
          className="col-span-6"
          styles={isReadOnly ? READONLY_INPUT_STYLES : undefined}
          name="code"
          value={formik.values.code}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.code && formik.errors.code}
        />
        <TextInput
          readOnly={isReadOnly}
          placeholder="Type Method Name"
          label="Method Name"
          withAsterisk
          className="col-span-6"
          styles={isReadOnly ? READONLY_INPUT_STYLES : undefined}
          name="methodName"
          value={formik.values.methodName}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.methodName && formik.errors.methodName}
        />

        <TextInput
          placeholder="Type Grade Name"
          label="Grade Name"
          withAsterisk
          className="col-span-6"
          name="grade"
          value={formik.values.grade}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.grade && formik.errors.grade}
        />
        <TextInput
          placeholder="Type Maximum Mark"
          label="Maximum Mark"
          withAsterisk
          className="col-span-6"
          type="number"
          name="maximumMark"
          value={formik.values.maximumMark}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.maximumMark && formik.errors.maximumMark}
        />
        <TextInput
          placeholder="Type Minimum Mark"
          label="Minimum Mark"
          withAsterisk
          className="col-span-6"
          type="number"
          name="minimumMark"
          value={formik.values.minimumMark}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.minimumMark && formik.errors.minimumMark}
        />
        <TextInput
          placeholder="Type Average Mark"
          label="Average Mark"
          withAsterisk
          className="col-span-6"
          type="number"
          name="averageMark"
          value={formik.values.averageMark}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched.averageMark && formik.errors.averageMark}
        />

        <div className="col-span-12">
          <SubmitSection
            isLoading={isLoading || isLoadingUpdate}
            onCancel={onClose}
            confirmText={confirmText}
          />
        </div>
      </div>
    </form>
  );
}
