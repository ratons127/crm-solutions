'use client';

import { useAppState } from '@/lib/features/app/appSlice';
import { useCreateManualAttendanceMutation } from '@/services/api/attendance/manualAttendanceAPI';
import { ActionIcon, Button, Select, Textarea } from '@mantine/core';
import { DateInput, TimeInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useMemo, useRef } from 'react';
import { LuClock } from 'react-icons/lu';
import { TfiSave } from 'react-icons/tfi';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'edit';
  data?: any | null;
  onClose: () => void;
};

const toDateTimeString = (date: Date | null, time: string) => {
  if (!date || !time) return '';
  const [hours, minutes] = time.split(':').map(Number);
  const dt = new Date(date);
  dt.setHours(hours, minutes, 0, 0);
  const iso = dt.toISOString().split('.')[0];
  return iso;
};

const parseDate = (value: any): Date | null => {
  if (!value) return null;
  try {
    // value may be 'YYYY-MM-DD' or ISO string
    const d = new Date(value);
    return isNaN(d.getTime()) ? null : d;
  } catch {
    return null;
  }
};

const timeFromDateTime = (value?: string): string => {
  if (!value) return '';
  try {
    const t = value.includes('T') ? value.split('T')[1] : value;
    return t.slice(0, 5); // HH:mm
  } catch {
    return '';
  }
};

export default function AttendanceApplyForm({ action, data, onClose }: Props) {
  const { auth } = useAppState();
  const inTimeRef = useRef<HTMLInputElement>(null);
  const outTimeRef = useRef<HTMLInputElement>(null);
  const [createManualAttendance, { isLoading: submitting }] =
    useCreateManualAttendanceMutation();

  const adjustmentType = useMemo(
    () => [
      { label: 'Missing Biometric', value: 'MISSING_BIOMETRIC' },
      { label: 'Device Offline', value: 'DEVICE_OFFLINE' },
      { label: 'Forgot to Punch', value: 'FORGOT_TO_PUNCH' },
      { label: 'Other', value: 'OTHER' },
    ],
    []
  );

  const formik = useFormik({
    initialValues:
      action === 'edit' && data
        ? {
            id: data.id,
            employeeId: data.employeeId ?? auth.user?.employeeId,
            attendanceDate: parseDate(data.attendanceDate),
            inTime: timeFromDateTime(data.inTime),
            outTime: timeFromDateTime(data.outTime),
            adjustmentType: data.adjustmentType ?? '',
            reason: data.reason ?? '',
            submittedById: auth?.user?.id,
            sourceChannel: 'EMPLOYEE',
            isLocked: Boolean(data.isLocked ?? false),
            manualAttendanceStatus: data.manualAttendanceStatus ?? 'PENDING',
          }
        : {
            employeeId: auth.user?.employeeId,
            attendanceDate: null as Date | null,
            inTime: '',
            outTime: '',
            adjustmentType: '',
            reason: '',
            submittedById: auth?.user?.id,
            sourceChannel: 'EMPLOYEE',
            isLocked: false,
            manualAttendanceStatus: 'PENDING',
          },
    validationSchema: Yup.object({
      attendanceDate: Yup.date()
        .nullable()
        .typeError('Attendance date is required')
        .required('Attendance date is required'),
      inTime: Yup.string().required('In time is required'),
      outTime: Yup.string().required('Out time is required'),
      adjustmentType: Yup.string().required('Adjustment type is required'),
      reason: Yup.string().required('Reason is required'),
    }),
    enableReinitialize: true,
    onSubmit: async (values, { setSubmitting }) => {
      try {
        const toYMD = (d: Date | null) =>
          d ? new Date(d).toISOString().split('T')[0] : '';

        const payload: any = {
          id: (values as any).id,
          employeeId: Number(values.employeeId),
          attendanceDate: toYMD(values.attendanceDate),
          inTime: toDateTimeString(values.attendanceDate, values.inTime),
          outTime: toDateTimeString(values.attendanceDate, values.outTime),
          adjustmentType: values.adjustmentType,
          reason: values.reason,
          submittedById: auth?.user?.id,
          submittedAt: new Date().toISOString(),
          sourceChannel: 'EMPLOYEE',
          isLocked: Boolean((values as any).isLocked ?? false),
          manualAttendanceStatus:
            (values as any).manualAttendanceStatus ?? 'PENDING',
        };

        // The backend endpoint is `/v1/manual-attendance/save` which typically
        // accepts both create and update based on presence of `id`.
        await createManualAttendance(payload).unwrap();

        notifications.show({
          color: 'green',
          message:
            action === 'edit'
              ? 'Manual attendance updated'
              : 'Manual attendance saved',
        });
        onClose();
      } catch (e: any) {
        console.error('Submission error:', e);
        notifications.show({
          color: 'red',
          message: e?.data?.message || 'Failed to save',
        });
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <form onSubmit={formik.handleSubmit} className="p-2">
      <div className="grid grid-cols-2 gap-3 py-3">
        <DateInput
          label={
            <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
              <span className="text-[#314158]">Attendance Date</span>
            </span>
          }
          placeholder="Pick attendance date"
          leftSection={<LuClock size={16} />}
          value={formik.values.attendanceDate}
          onChange={v => formik.setFieldValue('attendanceDate', v)}
          onBlur={() => formik.setFieldTouched('attendanceDate', true)}
          classNames={{
            input:
              'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
          }}
          error={
            formik.touched.attendanceDate
              ? (formik.errors.attendanceDate as string | undefined)
              : undefined
          }
        />

        <Select
          data={adjustmentType}
          label={
            <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
              <span className="text-[#314158]">Adjustment Type</span>
            </span>
          }
          placeholder="Select type"
          value={formik.values.adjustmentType}
          onChange={v => formik.setFieldValue('adjustmentType', v || '')}
          onBlur={() => formik.setFieldTouched('adjustmentType', true)}
          classNames={{
            input:
              'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
          }}
          error={
            formik.touched.adjustmentType
              ? (formik.errors.adjustmentType as string | undefined)
              : undefined
          }
        />
      </div>

      <div className="grid grid-cols-2 items-center justify-center gap-3 py-3">
        <TimeInput
          label={
            <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
              <span className="text-[#314158]">In Time</span>
            </span>
          }
          placeholder="Enter in time"
          variant="filled"
          size="sm"
          radius="md"
          ref={inTimeRef}
          rightSection={
            <ActionIcon
              variant="subtle"
              color="gray"
              onClick={() => inTimeRef.current?.showPicker()}
            >
              <LuClock size={16} />
            </ActionIcon>
          }
          classNames={{
            input:
              'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
          }}
          value={formik.values.inTime}
          onChange={e =>
            formik.setFieldValue(
              'inTime',
              (e?.currentTarget?.value || '').trim()
            )
          }
          onBlur={() => formik.setFieldTouched('inTime', true)}
          error={
            formik.touched.inTime
              ? (formik.errors.inTime as string | undefined)
              : undefined
          }
        />

        <TimeInput
          label={
            <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
              <span className="text-[#314158]">Out Time</span>
            </span>
          }
          placeholder="Enter out time"
          variant="filled"
          size="sm"
          radius="md"
          ref={outTimeRef}
          rightSection={
            <ActionIcon
              variant="subtle"
              color="gray"
              onClick={() => outTimeRef.current?.showPicker()}
            >
              <LuClock size={16} />
            </ActionIcon>
          }
          classNames={{
            input:
              'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
          }}
          value={formik.values.outTime}
          onChange={e =>
            formik.setFieldValue(
              'outTime',
              (e?.currentTarget?.value || '').trim()
            )
          }
          onBlur={() => formik.setFieldTouched('outTime', true)}
          error={
            formik.touched.outTime
              ? (formik.errors.outTime as string | undefined)
              : undefined
          }
        />
      </div>

      <div className="py-3">
        <Textarea
          label={
            <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
              <span className="text-[#314158]">
                Reason for Manual Adjustment
              </span>
            </span>
          }
          placeholder="Enter detailed reason for manual attendance entry (e.g., Device malfunction, Forgot to punch, Correcting error, etc.)"
          variant="filled"
          size="sm"
          radius="md"
          classNames={{
            input:
              'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
          }}
          value={formik.values.reason}
          onChange={e => formik.setFieldValue('reason', e.currentTarget.value)}
          onBlur={() => formik.setFieldTouched('reason', true)}
          error={
            formik.touched.reason
              ? (formik.errors.reason as string | undefined)
              : undefined
          }
        />
      </div>

      <div className="py-2 flex items-center justify-center gap-3">
        <Button
          leftSection={<TfiSave size={18} />}
          color="#F69348"
          radius="md"
          size="md"
          fullWidth
          type="submit"
          loading={submitting}
          disabled={submitting}
        >
          {action === 'edit' ? 'Update' : 'Submit'} Manual Attendance
        </Button>
        <Button
          variant="transparent"
          radius="md"
          size="md"
          fullWidth
          styles={{
            label: {
              color: '#1E293B',
              fontWeight: 400,
              fontSize: '14px',
              textTransform: 'none',
            },
            root: {
              border: '1px solid #94A3B833',
              background: '',
              borderRadius: '8px',
              transition: 'all 0.2s ease',
            },
          }}
          onClick={() => onClose()}
          disabled={submitting}
        >
          Cancel
        </Button>
      </div>
    </form>
  );
}
