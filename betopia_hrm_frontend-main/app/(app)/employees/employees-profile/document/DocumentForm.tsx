'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { useAppState } from '@/lib/features/app/appSlice';
import {
  useCreateEmployeeDocumentMutation,
  useGetDocumentTypeListQuery,
  useUpdateEmployeeDocumentMutation,
} from '@/lib/features/document/documentAPI';
import type {
  DocumentFormValues,
  DocumentPayloadData,
  EmployeeDocument,
} from '@/lib/types/document';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetEmployeeListQuery } from '@/services/api/employee/employeeProfileAPI';
import { FileInput, Select, Switch } from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data?: EmployeeDocument | null;
  onClose: () => void;
};

const getValidationSchema = (action: 'create' | 'update' | null) =>
  Yup.object().shape({
    employeeId: Yup.string().required('Employee is required'),
    documentTypeId: Yup.string().required('Document type is required'),
    filePath:
      action === 'create'
        ? Yup.mixed().required('File is required')
        : Yup.mixed().nullable(),
    issueDate: Yup.date().nullable(),
    expiryDate: Yup.date().nullable(),
    status: Yup.boolean().optional(),
  });

export default function DocumentForm({ action, data, onClose }: Props) {
  const { auth } = useAppState();
  const { data: employeeList } = useGetEmployeeListQuery(undefined);
  const { data: documentTypeList } = useGetDocumentTypeListQuery(undefined);
  const [createDocument, { isLoading }] = useCreateEmployeeDocumentMutation();
  const [updateDocument, { isLoading: isLoadingUpdate }] =
    useUpdateEmployeeDocumentMutation();

  const handleSubmit = async (values: DocumentFormValues) => {
    try {
      const formData = new FormData();

      // Prepare the data object
      const dataPayload: DocumentPayloadData = {
        employeeId: Number(values.employeeId),
        documentTypeId: Number(values.documentTypeId),
        documentStatus: values.status ? 'ACTIVE' : 'INACTIVE',
        status: values.status,
      };

      // Add dates if provided
      if (values.issueDate) {
        const issueDate =
          values.issueDate instanceof Date
            ? values.issueDate
            : new Date(values.issueDate);
        dataPayload.issueDate = issueDate.toISOString().split('T')[0];
      }
      if (values.expiryDate) {
        const expiryDate =
          values.expiryDate instanceof Date
            ? values.expiryDate
            : new Date(values.expiryDate);
        dataPayload.expiryDate = expiryDate.toISOString().split('T')[0];
      }

      // Add employeeDocumentVersions with the logged-in user ID
      if (auth.user?.id) {
        dataPayload.employeeDocumentVersions = [
          {
            userId: Number(auth.user.id),
          },
        ];
      }

      // Append file first
      if (values.filePath) {
        formData.append('file', values.filePath);
      }

      // Create a Blob from the JSON data with application/json content type
      const dataBlob = new Blob([JSON.stringify(dataPayload)], {
        type: 'application/json',
      });
      formData.append('data', dataBlob);
      if (action === 'create') {
        const result = await createDocument(formData).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Document',
          onSuccess: onClose,
        });
      } else if (action === 'update' && data?.id) {
        const result = await updateDocument({
          id: data.id,
          data: formData,
        }).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Document',
          onSuccess: onClose,
        });
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while ${action} document`,
        message: isDuplicate
          ? 'Duplicate document found'
          : error?.data?.message || error?.message || 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<DocumentFormValues>({
    initialValues:
      action === 'update' && data
        ? {
            employeeId: String(data.employee),
            documentTypeId: String(data.documentType.id),
            filePath: null,
            issueDate: data.issueDate ? new Date(data.issueDate) : null,
            expiryDate: data.expiryDate ? new Date(data.expiryDate) : null,
            status: data.status,
          }
        : {
            employeeId: '',
            documentTypeId: '',
            filePath: null,
            issueDate: null,
            expiryDate: null,
            status: true,
          },
    validationSchema: getValidationSchema(action),
    validateOnChange: false,
    validateOnBlur: true,
    onSubmit: handleSubmit,
  });

  return (
    <form onSubmit={formik.handleSubmit} className="space-y-4">
      <div className="col-span-12 flex items-center justify-between">
        <h1 className="text-lg font-semibold">
          Employee Document {action === 'create' ? 'Create' : 'Update'}
        </h1>
        <Switch
          labelPosition="left"
          className="col-span-4"
          classNames={{ label: 'font-semibold' }}
          label="Status"
          checked={Boolean(formik.values.status)}
          onChange={event =>
            formik.setFieldValue('status', event.currentTarget.checked)
          }
        />
      </div>

      <div className="grid grid-cols-12 gap-3">
        <Select
          placeholder="Select Employee ID"
          label="Employee ID"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            employeeList?.data?.map(emp => ({
              label: String(emp.id),
              value: String(emp.id),
            })) ?? []
          }
          name="employeeId"
          value={formik.values.employeeId}
          onChange={value => formik.setFieldValue('employeeId', value || '')}
          onBlur={() => formik.setFieldTouched('employeeId', true)}
          error={formik.touched.employeeId && formik.errors.employeeId}
        />

        <Select
          placeholder="Select Document"
          label="Document Type"
          withAsterisk
          searchable
          clearable
          className="col-span-6"
          data={
            documentTypeList?.data?.map(doc => ({
              label: doc.name,
              value: String(doc.id),
            })) ?? []
          }
          name="documentTypeId"
          value={formik.values.documentTypeId}
          onChange={value =>
            formik.setFieldValue('documentTypeId', value || '')
          }
          onBlur={() => formik.setFieldTouched('documentTypeId', true)}
          error={formik.touched.documentTypeId && formik.errors.documentTypeId}
        />

        <FileInput
          placeholder="Select File"
          label="File"
          accept="application/pdf,image/*"
          className="col-span-12"
          size="xs"
          withAsterisk={action === 'create'}
          value={formik.values.filePath}
          onChange={file => formik.setFieldValue('filePath', file)}
          onBlur={() => formik.setFieldTouched('filePath', true)}
          error={formik.touched.filePath && formik.errors.filePath}
        />

        <DatePickerInput
          label="Issue Date"
          placeholder="YYYY-MM-DD"
          className="col-span-6"
          size="xs"
          value={formik.values.issueDate}
          onChange={date => formik.setFieldValue('issueDate', date)}
          onBlur={() => formik.setFieldTouched('issueDate', true)}
          error={formik.touched.issueDate && formik.errors.issueDate}
        />

        <DatePickerInput
          label="Expiry Date"
          placeholder="YYYY-MM-DD"
          className="col-span-6"
          size="xs"
          value={formik.values.expiryDate}
          onChange={date => formik.setFieldValue('expiryDate', date)}
          onBlur={() => formik.setFieldTouched('expiryDate', true)}
          error={formik.touched.expiryDate && formik.errors.expiryDate}
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
