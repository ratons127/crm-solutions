'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { useAppState } from '@/lib/features/app/appSlice';
import { useCreateManualAttendanceMutation } from '@/services/api/attendance/manualAttendanceAPI';
import { ActionIcon, Button, Select, Textarea } from '@mantine/core';
import { DateInput, TimeInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useRef } from 'react';
import { AiOutlineExclamationCircle } from 'react-icons/ai';
import { IoCalendarClearOutline } from 'react-icons/io5';
import { LuClock } from 'react-icons/lu';
import { TfiSave } from 'react-icons/tfi';
import * as Yup from 'yup';

const toDateTimeString = (date: Date | null, time: string) => {
  if (!date || !time) return '';
  // time is like "09:30"
  const [hours, minutes] = time.split(':').map(Number);

  const dt = new Date(date);
  dt.setHours(hours, minutes, 0, 0);

  // Convert to ISO but remove timezone offset (for consistency)
  const iso = dt.toISOString().split('.')[0]; // "2025-10-22T09:30:00"
  return iso;
};

const ManualAttendanceAdjustment = () => {
  // Separate refs for in-time and out-time
  const inTimeRef = useRef<HTMLInputElement>(null);
  const outTimeRef = useRef<HTMLInputElement>(null);
  const { auth } = useAppState();

  const adjustmentType = [
    {
      label: 'Missing Biometric',
      value: 'MISSING_BIOMETRIC',
    },
    {
      label: 'Device Offline',
      value: 'DEVICE_OFFLINE',
    },
    {
      label: 'Forgot to Punch',
      value: 'FORGOT_TO_PUNCH',
    },
    {
      label: 'Other',
      value: 'OTHER',
    },
  ];
  const [createManualAttendance, { isLoading: submitting }] =
    useCreateManualAttendanceMutation();

  // Master data queries
  // const { data: companyList } = useGetCompanyListQuery(undefined);
  // const { data: shiftList } = useGetShiftDefinitionListQuery(undefined);
  // const { data: policyList } = useGetAttendancePoliciesQuery(undefined);

  const formik = useFormik({
    initialValues: {
      employeeId: auth.user?.employeeId,
      attendanceDate: null as Date | null,
      inTime: '',
      outTime: '',
      adjustmentType: '',
      reason: '',
      // companyId: '',
      // shiftId: '',
      submittedById: auth?.user?.id,
      sourceChannel: 'EMPLOYEE',
      // attendancePolicyId: '',
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
      // companyId: Yup.string().required('Company is required'),
      // shiftId: Yup.string().required('Shift is required'),
      // attendancePolicyId: Yup.string().required(
      //   'Attendance Policy is required'
      // ),
    }),
    onSubmit: async (values, { setSubmitting }) => {
      try {
        const toYMD = (d: Date | null) =>
          d ? new Date(d).toISOString().split('T')[0] : '';
        const payload = {
          employeeId: Number(values.employeeId),
          attendanceDate: toYMD(values.attendanceDate),
          inTime: toDateTimeString(values.attendanceDate, values.inTime),
          outTime: toDateTimeString(values.attendanceDate, values.outTime),
          adjustmentType: values.adjustmentType,
          reason: values.reason,
          // companyId: Number(values.companyId),
          // shiftId: Number(values.shiftId),
          // attendancePolicyId: Number(values.attendancePolicyId),
          submittedById: auth?.user?.id,
          submittedAt: new Date().toISOString(),
          sourceChannel: 'EMPLOYEE',
          isLocked: false,
          manualAttendanceStatus: 'PENDING',
        };

        await createManualAttendance(payload).unwrap();
        notifications.show({
          color: 'green',
          message: 'Manual attendance saved',
        });
        formik.resetForm();
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
    <div>
      {/* ===== Header ===== */}
      <div className="grid grid-cols-1 md:grid-cols-2 items-center">
        <div>
          <Breadcrumbs />
          <h1 className="text-3xl font-bold text-[#1D293D] mt-3">
            Manual Attendance Adjustment
          </h1>
          <p className="text-base text-[#45556C]">
            Manually record or adjust attendance for employees
          </p>
        </div>

        <div className="flex gap-3 justify-start md:justify-end items-center mt-5 md:mt-0">
          <div className="flex items-center justify-center w-80  px-5 py-2 bg-white border border-[#E2E8F0] rounded-lg gap-1">
            <div className="flex items-center justify-center gap-3">
              <IoCalendarClearOutline size={18} />
              <span>Today :</span>
            </div>
            <span>{new Date().toDateString()}</span>
          </div>
        </div>
      </div>
      {/* Important Guidelines */}
      <div className="py-10 ">
        <div className="w-full bg-green-200 rounded-lg px-8 py-8 border border-[#E1FCEA] bg-[linear-gradient(0deg,#E0FCEA_0%,#E0FCEA_100%)] ">
          <div className="flex  items-start justify-start gap-3">
            <div className="py-1">
              <AiOutlineExclamationCircle size={18} color="#01922C" />
            </div>

            <div className="">
              <h5 className="text-base text-[#01922C]">Important Guidelines</h5>
              <h5 className="text-base text-[#01922C]">
                Manual attendance adjustments should be made with proper
                authorization and valid reasons. All adjustments are logged and
                auditable. Please ensure accuracy before submission.
              </h5>
            </div>
          </div>
        </div>
      </div>
      {/* table section */}
      <div className="bg-white rounded-xl shadow-lg  ">
        <div className="rounded-t-xl border-b border-t border-[#E2E8F0] bg-gradient-to-r from-[#F8FAFC] to-[#F1F5F9]">
          <div className="flex items-center  gap-3 px-10 py-5 ">
            <div className="w-10 h-10 bg-[#F69348] flex items-center justify-center rounded-lg ">
              <LuClock size={20} color="white" />
            </div>
            <div className="flex flex-col ">
              <span className="text-base text-[#1E293B]">
                Attendance Details
              </span>
              <span className="text-base text-[#64748B]">
                Enter attendance information
              </span>
            </div>
          </div>
        </div>

        <form onSubmit={formik.handleSubmit} className="py-10 px-10 space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <DateInput
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <IoCalendarClearOutline size={16} />
                  <span className="text-[#314158]">Attendance Date</span>
                </span>
              }
              placeholder="Click icon to show browser picker"
              variant="filled"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              value={formik.values.attendanceDate}
              onChange={d => formik.setFieldValue('attendanceDate', d)}
              onBlur={() => formik.setFieldTouched('attendanceDate', true)}
              error={
                formik.touched.attendanceDate
                  ? (formik.errors.attendanceDate as any)
                  : undefined
              }
            />
            <Select
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">Adjustment Type</span>
                </span>
              }
              placeholder="Pick value"
              data={adjustmentType}
              variant="filled"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              value={formik.values.adjustmentType}
              onChange={v => formik.setFieldValue('adjustmentType', v || '')}
              onBlur={() => formik.setFieldTouched('adjustmentType', true)}
              error={
                formik.touched.adjustmentType
                  ? (formik.errors.adjustmentType as string | undefined)
                  : undefined
              }
            />
          </div>
          {/* Company, Shift, Policy */}
          {/* <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Select
              required
              label={<span className="text-[#314158]">Company</span>}
              placeholder="Select company"
              data={(companyList?.data ?? []).map((c: any) => ({
                value: String(c.id),
                label: c.name ?? `#${c.id}`,
              }))}
              variant="filled"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              value={formik.values.companyId}
              onChange={v => {
                formik.setFieldValue('companyId', v || '');
                formik.setFieldValue('shiftId', '');
                formik.setFieldValue('policyId', '');
              }}
              onBlur={() => formik.setFieldTouched('companyId', true)}
              error={
                formik.touched.companyId
                  ? (formik.errors.companyId as string | undefined)
                  : undefined
              }
            />
            <Select
              required
              label="Shift"
              placeholder="Select Shift"
              data={(shiftList?.data ?? []).map((s: any) => ({
                value: String(s.id),
                label: `${s.shiftName} - ${s.shiftCode}`,
              }))}
              variant="filled"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              value={formik.values.shiftId}
              onChange={v => formik.setFieldValue('shiftId', v || '')}
              onBlur={() => formik.setFieldTouched('shiftId', true)}
              error={
                formik.touched.shiftId
                  ? (formik.errors.shiftId as string | undefined)
                  : undefined
              }
            />
            <Select
              required
              label="Policy"
              placeholder="Select Policy"
              data={(policyList?.data ?? []).map((p: any) => ({
                value: String(p.id),
                label: `ID:${p.id}-${p.effectiveFrom} → ${p.effectiveTo}`,
              }))}
              variant="filled"
              size="sm"
              radius="md"
              classNames={{
                input:
                  'bg-white/0 border border-[#E5EEF5] text-slate-800 placeholder:text-slate-400 focus:border-[#9BD6B8]',
              }}
              value={formik.values.attendancePolicyId}
              onChange={v =>
                formik.setFieldValue('attendancePolicyId', v || '')
              }
              onBlur={() => formik.setFieldTouched('attendancePolicyId', true)}
              error={
                formik.touched.attendancePolicyId
                  ? (formik.errors.attendancePolicyId as string | undefined)
                  : undefined
              }
            />
          </div> */}
          <div className="grid grid-cols-2 items-center justify-center gap-3 py-5">
            <TimeInput
              required
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
              required
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
          <div className="py-5">
            <Textarea
              required
              label={
                <span className="inline-flex items-center gap-2 text-[#0C8A4A]">
                  <span className="text-[#314158]">
                    Reason for Manual Adjustment{' '}
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
              onChange={e =>
                formik.setFieldValue('reason', e.currentTarget.value)
              }
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
              size="lg"
              fullWidth
              type="submit"
              loading={submitting}
              disabled={submitting}
            >
              Save Manual Attendance
            </Button>
            <Button
              variant="transparent"
              radius="md"
              size="lg"
              fullWidth
              styles={{
                label: {
                  color: '#1E293B',
                  fontWeight: 400,
                  fontSize: '16px',
                  textTransform: 'none',
                },
                root: {
                  border: '1px solid #94A3B833',
                  background: '',
                  borderRadius: '8px',
                  transition: 'all 0.2s ease',
                },
              }}
              onClick={() => formik.resetForm()}
              disabled={submitting}
            >
              Reset
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ManualAttendanceAdjustment;
