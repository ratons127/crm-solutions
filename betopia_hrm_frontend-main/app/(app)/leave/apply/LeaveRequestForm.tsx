'use client';

import {
  Checkbox,
  FileInput,
  NumberInput,
  Paper,
  Select,
  Textarea,
  Title,
} from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import dayjs from 'dayjs';
import { useFormik } from 'formik';
import { useState } from 'react';
import * as Yup from 'yup';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import SubmitSection from '@/components/common/form/SubmitSection';
import { useAppState } from '@/lib/features/app/appSlice';

import {
  useCreateLeaveRequestMutation,
  useUpdateLeaveRequestMutation,
} from '@/lib/features/leaveRequest/leaveRequestAPI';
import { LeaveRequest, LeaveRequestValues } from '@/lib/types/leaveRequest';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetEmployeeListQuery } from '@/services/api/employee/employeeProfileAPI';

// ✅ Validation Schema
const validationSchema = Yup.object().shape({
  employeeId: Yup.string().required('Employee is required'),
  leaveTypeId: Yup.string().required('Leave type is required'),
  // leaveGroupAssignId: Yup.string().required('Leave group assign is required'),
  startDate: Yup.date().nullable().required('Start date is required'),
  endDate: Yup.date()
    .nullable()
    .required('End date is required')
    .test(
      'endDate-after-startDate',
      'End date must be on or after start date',
      function (value) {
        const { startDate } = this.parent as { startDate?: Date | null };
        if (!value || !startDate) return true;
        // Compare by date only (ignore time) to avoid timezone/time-of-day issues
        const v = dayjs(value).startOf('day');
        const s = dayjs(startDate).startOf('day');
        return v.isSame(s) || v.isAfter(s);
      }
    ),
  reason: Yup.string().required('Reason is required').trim(),
  justification: Yup.string().optional(),
  // hasProof: Yup.boolean().optional(),

  // ✅ Conditional validation for proofDocument
  files: Yup.mixed()
    .nullable()
    .when('hasProof', {
      is: true,
      then: schema =>
        schema
          .required('Proof document is required')
          .test(
            'fileType',
            'Only PDF, JPG, JPEG, PNG, or WEBP files are allowed',
            value => {
              if (!value) return false;
              const allowed = [
                'application/pdf',
                'image/jpeg',
                'image/jpg',
                'image/png',
                'image/webp',
              ];
              return allowed.includes((value as File).type);
            }
          ),
      otherwise: schema => schema.nullable().notRequired(),
    }),
});

// ✅ Utility for days calculation
function diffDaysInclusive(a: Date | null, b: Date | null): number {
  if (!a || !b) return 0;
  const d1 = new Date(a);
  const d2 = new Date(b);
  d1.setHours(0, 0, 0, 0);
  d2.setHours(0, 0, 0, 0);
  const ms = d2.getTime() - d1.getTime();
  const days = Math.floor(ms / (24 * 60 * 60 * 1000));
  return days >= 0 ? days + 1 : 0;
}

type LeaveRequestFormProps = {
  onClose?: () => void;
  employeeBalance?: any;
} & ({ action?: 'create' } | { action: 'edit'; data: LeaveRequest });

export default function LeaveRequestForm(
  props: LeaveRequestFormProps = { action: 'create' }
) {
  const { auth } = useAppState();
  const { data: employeeList } = useGetEmployeeListQuery(undefined);
  // const { data: leaveTypeList } = useGetLeaveTypeListQuery({});
  // const { data: leaveGroupAssignList } = useGetLeaveGroupAssignListQuery({});
  const [createLeaveRequest, { isLoading: isCreating }] =
    useCreateLeaveRequestMutation();
  const [updateLeaveRequest, { isLoading: isUpdating }] =
    useUpdateLeaveRequestMutation();
  const [showProofUpload, setShowProofUpload] = useState(false);
  const isEdit = props?.action === 'edit';

  // ✅ Submit Handler
  const handleSubmit = async (values: LeaveRequestValues) => {
    try {
      // Prepare the core data payload (shared for create/update)
      const basePayload: any = {
        employeeId: Number(values.employeeId),
        // leaveGroupAssignId: Number(values.leaveGroupAssignId),
        leaveTypeId: Number(values.leaveTypeId),
        daysRequested: diffDaysInclusive(values.startDate, values.endDate),
        reason: values.reason,
        justification: values.justification,
      };

      if (values.startDate) {
        const startDate =
          values.startDate instanceof Date
            ? values.startDate
            : new Date(values.startDate);
        basePayload.startDate = startDate.toISOString().split('T')[0];
      }
      if (values.endDate) {
        const endDate =
          values.endDate instanceof Date
            ? values.endDate
            : new Date(values.endDate);
        basePayload.endDate = endDate.toISOString().split('T')[0];
      }

      if (isEdit && 'data' in props) {
        // Update flow (JSON body)
        const result = await updateLeaveRequest({
          id: props.data.id,
          data: basePayload,
        }).unwrap();

        showApiNotification({
          status: result?.status ?? 200,
          message: result?.message ?? 'Leave request updated',
          title: 'Leave Request',
        });
        if (props.onClose) props.onClose();
      } else {
        // Create flow (multipart with files)
        const formData = new FormData();

        if (values.files) {
          formData.append('files', values.files);
        }

        const dataBlob = new Blob([JSON.stringify(basePayload)], {
          type: 'application/json',
        });
        formData.append('data', dataBlob);
        const result = await createLeaveRequest(formData).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Request',
        });
        props.onClose?.();
      }
    } catch (error: any) {
      const isDuplicate =
        typeof error?.message === 'string' &&
        error.message.includes('duplicate');

      notifications.show({
        title: `Error while Leave request`,
        message: isDuplicate
          ? 'Duplicate Leave request found'
          : error?.data?.message || error?.message || 'Something went wrong',
        color: 'red',
      });
    }
  };

  // ✅ Formik Init
  const formik = useFormik<LeaveRequestValues>({
    initialValues:
      isEdit && 'data' in props
        ? {
            employeeId: String(props.data.employeeId ?? ''),
            leaveTypeId: String(
              (props.data as LeaveRequest).leaveTypeId ??
                (props.data as LeaveRequest).leaveType?.id ??
                ''
            ),
            // leaveGroupAssignId: String(props.data.leaveGroupAssignId ?? ''),
            daysRequested: String(
              diffDaysInclusive(
                props.data.startDate ? new Date(props.data.startDate) : null,
                props.data.endDate ? new Date(props.data.endDate) : null
              )
            ),
            startDate: props.data.startDate
              ? new Date(props.data.startDate)
              : null,
            endDate: props.data.endDate ? new Date(props.data.endDate) : null,
            reason: props.data.reason ?? '',
            justification: props.data.justification ?? '',
            // hasProof: props.data.hasProof ?? false,
            files: null,
          }
        : {
            employeeId: auth?.user?.employeeId
              ? String(auth.user.employeeId)
              : '',
            leaveTypeId: '',
            // leaveGroupAssignId: '',
            daysRequested: '',
            startDate: null,
            endDate: null,
            reason: '',
            justification: '',
            // hasProof: false,
            files: null,
          },
    validationSchema,
    enableReinitialize: true,
    validateOnChange: false,
    validateOnBlur: true,
    onSubmit: handleSubmit,
  });

  const daysRequested = diffDaysInclusive(
    formik.values.startDate,
    formik.values.endDate
  );

  // Build Leave Type options; ensure current edit value is present even if not in balance list
  const leaveTypeOptions = (() => {
    const base =
      props.employeeBalance?.data?.map(
        (lt: { leaveType: { id: number; name: string } }) => ({
          value: String(lt.leaveType.id),
          label: lt.leaveType.name,
        })
      ) ?? [];
    const currentId =
      isEdit && 'data' in props
        ? String(
            ((props.data as LeaveRequest).leaveTypeId ??
              (props.data as LeaveRequest).leaveType?.id ??
              '') as any
          )
        : '';
    if (
      currentId &&
      !base.some((o: { value: string; label: string }) => o.value === currentId)
    ) {
      base.push({
        value: currentId,
        label:
          (isEdit && 'data' in props
            ? (props.data as LeaveRequest).leaveType?.name
            : undefined) ?? `Leave Type #${currentId}`,
      });
    }
    return base;
  })();

  return (
    <div className="p-0 mb-6">
      <div className="mb-4">
        <Breadcrumbs />
      </div>

      <Paper withBorder p="lg" radius="md">
        <form onSubmit={formik.handleSubmit} className="space-y-4">
          <div className="flex items-center justify-between">
            <Title order={4}>
              {isEdit ? 'Update Leave Request' : 'Create Leave Request'}
            </Title>
          </div>

          <div className="grid grid-cols-12 gap-3">
            {/* Employee */}
            <Select
              label="Employee"
              placeholder="Select employee"
              withAsterisk
              searchable
              clearable={false}
              className="col-span-6"
              data={
                employeeList?.data?.map((e: any) => ({
                  value: String(e.id),
                  label: `${e.firstName} ${e.lastName} - ${e.employeeSerialId}`,
                })) ?? []
              }
              name="employeeId"
              value={formik.values.employeeId?.toString() || ''}
              onChange={value =>
                formik.setFieldValue('employeeId', value || '')
              }
              error={formik.touched.employeeId && formik.errors.employeeId}
              disabled={!!auth.user?.employeeId} // disable if logged-in user is set
            />

            {/* Leave Type */}
            <Select
              label="Leave Type"
              placeholder="Select leave type"
              withAsterisk
              searchable
              clearable
              className="col-span-6"
              data={leaveTypeOptions}
              name="leaveTypeId"
              value={formik.values.leaveTypeId}
              onChange={value =>
                formik.setFieldValue('leaveTypeId', value || '')
              }
              onBlur={() => formik.setFieldTouched('leaveTypeId', true)}
              error={formik.touched.leaveTypeId && formik.errors.leaveTypeId}
            />

            {/* Leave Group Assign */}
            {/* <Select
              label="Leave Group Assign"
              placeholder="Select leave group assign"
              withAsterisk
              searchable
              clearable
              className="col-span-4"
              data={
                leaveGroupAssignList?.data?.map(lga => ({
                  value: String(lga.id),
                  label: lga.leaveType.name,
                })) ?? []
              }
              name="leaveGroupAssignId"
              value={formik.values.leaveGroupAssignId}
              onChange={value =>
                formik.setFieldValue('leaveGroupAssignId', value || '')
              }
              onBlur={() => formik.setFieldTouched('leaveGroupAssignId', true)}
              error={
                formik.touched.leaveGroupAssignId &&
                formik.errors.leaveGroupAssignId
              }
            /> */}

            {/* Dates */}
            <DatePickerInput
              label="Start Date"
              placeholder="YYYY-MM-DD"
              withAsterisk
              className="col-span-4"
              size="xs"
              value={formik.values.startDate}
              maxDate={formik.values.endDate ?? undefined}
              onChange={date => {
                // Set + mark touched, then explicitly validate to clear any stale required errors
                formik.setFieldValue('startDate', date);
                formik.setFieldTouched('startDate', true, false);
                if (date) formik.setFieldError('startDate', undefined);

                // If endDate exists but is before new startDate, align endDate to startDate
                if (date && formik.values.endDate) {
                  const sd = new Date(date);
                  sd.setHours(0, 0, 0, 0);
                  const ed = new Date(formik.values.endDate);
                  ed.setHours(0, 0, 0, 0);
                  if (ed.getTime() < sd.getTime()) {
                    formik.setFieldValue('endDate', date);
                    formik.setFieldError('endDate', undefined);
                  }
                }

                // Validate next tick to ensure state is updated (both fields)
                setTimeout(() => {
                  formik.validateField('startDate');
                  formik.validateField('endDate');
                });
              }}
              error={formik.touched.startDate && formik.errors.startDate}
              renderDay={dateValue => {
                const date = dayjs(dateValue);
                const today = dayjs();

                const isToday = date.isSame(today, 'day');
                const dayNumber = date.date();

                return (
                  <div
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      width: '100%',
                      height: '100%',
                    }}
                  >
                    <div
                      style={{
                        width: 28,
                        height: 28,
                        borderRadius: '8px', // rounded square
                        backgroundColor: isToday ? '#1c7ed6' : 'transparent', // Mantine primary blue
                        color: isToday ? 'white' : 'inherit',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontWeight: isToday ? 600 : 400,
                        transition: 'all 0.2s ease',
                      }}
                    >
                      {dayNumber}
                    </div>
                  </div>
                );
              }}
            />

            {/* <DatePickerInput
              label="End Date"
              placeholder="YYYY-MM-DD"
              withAsterisk
              className="col-span-4"
              size="xs"
            value={formik.values.endDate}
            onChange={date => {
              formik.setFieldValue('endDate', date);
              formik.setFieldTouched('endDate', true, false);
              if (date) formik.setFieldError('endDate', undefined);
              setTimeout(() => formik.validateField('endDate'));
            }}
            error={formik.touched.endDate && formik.errors.endDate}
            /> */}

            <DatePickerInput
              label="End Date"
              placeholder="YYYY-MM-DD"
              withAsterisk
              className="col-span-4"
              size="xs"
              value={formik.values.endDate}
              minDate={formik.values.startDate ?? undefined}
              onChange={date => {
                formik.setFieldValue('endDate', date);
                formik.setFieldTouched('endDate', true, false);
                if (date) formik.setFieldError('endDate', undefined);
                setTimeout(() => formik.validateField('endDate'));
              }}
              error={
                formik.touched.endDate
                  ? formik.values.endDate
                    ? formik.errors.endDate &&
                      formik.errors.endDate !== 'End date is required'
                      ? (formik.errors.endDate as string)
                      : undefined
                    : (formik.errors.endDate as string)
                  : undefined
              }
              renderDay={dateValue => {
                const date = dayjs(dateValue);
                const today = dayjs();

                const isToday = date.isSame(today, 'day');
                const dayNumber = date.date();

                return (
                  <div
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      width: '100%',
                      height: '100%',
                    }}
                  >
                    <div
                      style={{
                        width: 28,
                        height: 28,
                        borderRadius: '8px', // rounded square
                        backgroundColor: isToday ? '#1c7ed6' : 'transparent', // Mantine primary blue
                        color: isToday ? 'white' : 'inherit',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontWeight: isToday ? 600 : 400,
                        transition: 'all 0.2s ease',
                      }}
                    >
                      {dayNumber}
                    </div>
                  </div>
                );
              }}
            />

            <NumberInput
              label="Days Requested"
              placeholder="0"
              className="col-span-4"
              value={daysRequested}
              disabled
              min={0}
              clampBehavior="strict"
            />

            {/* Reason */}
            <Textarea
              label="Reason"
              placeholder="Why are you applying for leave?"
              withAsterisk
              className="col-span-12"
              name="reason"
              value={formik.values.reason}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={formik.touched.reason && formik.errors.reason}
              minRows={2}
            />

            {/* Justification */}
            <Textarea
              label="Justification (optional)"
              placeholder="Manager justification / comments"
              className="col-span-12"
              name="justification"
              value={formik.values.justification}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={
                formik.touched.justification && formik.errors.justification
              }
              minRows={1}
            />

            {/* Proof Checkbox */}
            <Checkbox
              size="xs"
              label="Has proof document?"
              className="col-span-12"
              // checked={formik.values.hasProof}
              onChange={e => {
                const checked = e.currentTarget.checked;
                formik.setFieldValue('hasProof', checked);
                setShowProofUpload(checked);
                if (!checked) {
                  formik.setFieldValue('proofDocument', null);
                }
              }}
            />

            {/* Proof Upload */}
            {showProofUpload && (
              <FileInput
                size="xs"
                label="Upload proof (PDF/JPG/PNG/WEBP)"
                placeholder="Choose file…"
                className="col-span-12"
                accept="application/pdf,image/png,image/jpeg,image/webp"
                clearable
                withAsterisk
                value={formik.values.files}
                onChange={file => formik.setFieldValue('files', file)}
                onBlur={() => formik.setFieldTouched('files', true)}
                error={formik.touched.files && formik.errors.files}
              />
            )}

            {/* Submit Buttons */}
            <div className="col-span-12">
              <SubmitSection
                isLoading={isEdit ? isUpdating : isCreating}
                onCancel={() => {
                  formik.resetForm();
                  setShowProofUpload(false);
                  props.onClose?.();
                }}
                confirmText={isEdit ? 'Update Request' : 'Submit Request'}
              />
            </div>
          </div>
        </form>
      </Paper>
    </div>
  );
}
