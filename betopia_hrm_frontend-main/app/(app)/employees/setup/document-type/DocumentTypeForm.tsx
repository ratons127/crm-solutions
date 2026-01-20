'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import {
  useCreateDocumentTypeMutation,
  useUpdateDocumentTypeMutation,
} from '@/lib/features/document/documentTypeAPI';
import type {
  DocumentType,
  DocumentTypeFormValues,
} from '@/lib/types/document';
import { showApiNotification } from '@/lib/utils/showNotification';
import { Select, TextInput, Textarea } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

const DOCUMENT_CATEGORIES = [
  { value: 'IDENTITY', label: 'Identity' },
  { value: 'COMPLIANCE', label: 'Compliance' },
  { value: 'CERTIFICATION', label: 'Certification' },
  { value: 'VISA', label: 'Visa' },
  { value: 'LICENSE', label: 'License' },
  { value: 'CONTRACT', label: 'Contract' },
  { value: 'MISCELLANEOUS', label: 'Miscellaneous' },
];

type Props = {
  action: 'create' | 'update' | null;
  data?: DocumentType | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  name: Yup.string()
    .required('Document Name is required')
    .min(2, 'Document Name must be at least 2 characters')
    .max(100, 'Document Name must not exceed 100 characters')
    .trim(),
  category: Yup.string()
    .required('Category is required')
    .oneOf(
      [
        'IDENTITY',
        'COMPLIANCE',
        'CERTIFICATION',
        'VISA',
        'LICENSE',
        'CONTRACT',
        'MISCELLANEOUS',
      ],
      'Invalid category selected'
    ),
  description: Yup.string()
    .min(3, 'Description must be at least 3 characters')
    .max(500, 'Description must not exceed 500 characters')
    .trim()
    .optional(),
});

export default function DocumentTypeForm({ action, data, onClose }: Props) {
  const [createDocumentType, { isLoading }] = useCreateDocumentTypeMutation();
  const [updateDocumentType, { isLoading: isLoadingUpdate }] =
    useUpdateDocumentTypeMutation();

  const handleSubmit = async (values: DocumentTypeFormValues) => {
    try {
      if (action === 'create') {
        const result = await createDocumentType(values).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Document Type',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateDocumentType({
          id: data.id,
          data: values,
        }).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Document Type',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} document type`,
        message: isDuplicate
          ? 'Duplicate document type name found'
          : error?.data?.message || error?.message || 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<DocumentTypeFormValues>({
    initialValues:
      action === 'update' && data
        ? {
            name: data.name,
            category: data.category,
            description: data.description,
          }
        : {
            name: '',
            category: '',
            description: '',
          },
    validationSchema,
    validateOnChange: true,
    validateOnBlur: true,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="col-span-12 flex items-center">
        <h1 className="text-lg font-semibold">
          Document Type {action === 'create' ? 'Create' : 'Update'}
        </h1>
      </div>
      <div className="grid grid-cols-12 gap-3">
        <Select
          placeholder="Select Document Category"
          label="Document Category"
          withAsterisk
          searchable
          clearable
          className="col-span-12"
          name="category"
          data={DOCUMENT_CATEGORIES}
          value={formik.values.category}
          onChange={value => formik.setFieldValue('category', value || '')}
          onBlur={() => formik.setFieldTouched('category', true)}
          error={
            formik.touched.category && formik.errors.category
              ? formik.errors.category
              : undefined
          }
        />
        <TextInput
          placeholder="Enter Document Type Name"
          label="Document Name"
          withAsterisk
          className="col-span-12"
          name="name"
          value={formik.values.name}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={
            formik.touched.name && formik.errors.name
              ? formik.errors.name
              : undefined
          }
        />
        <Textarea
          placeholder="Enter description"
          label="Description"
          className="col-span-12"
          name="description"
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={
            formik.touched.description && formik.errors.description
              ? formik.errors.description
              : undefined
          }
          rows={4}
        />
        <div className="col-span-12">
          <SubmitSection
            onCancel={onClose}
            confirmText={action === 'create' ? 'Create' : 'Update'}
            isLoading={isLoading || isLoadingUpdate}
          />
        </div>
      </div>
    </form>
  );
}
