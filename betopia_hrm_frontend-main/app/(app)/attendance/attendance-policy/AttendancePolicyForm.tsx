'use client';

import { useGetCompanyListQuery } from '@/lib/features/employment/employmentGroupAPI';
import type { AttendancePolicy } from '@/lib/types/attendancePolicy';
import {
  useCreateAttendancePolicyMutation,
  useUpdateAttendancePolicyMutation,
} from '@/services/api/attendance/attendancePolicyAPI';
import {
  Button,
  Select,
  Switch,
  Text,
  Textarea,
  TextInput,
  Tooltip,
} from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useEffect, type ChangeEvent } from 'react';
import { AiOutlineQuestionCircle } from 'react-icons/ai';
import { FiAlertTriangle } from 'react-icons/fi';
import { GoPerson } from 'react-icons/go';
import { IoSettingsOutline } from 'react-icons/io5';
import { MdOutlineWatchLater } from 'react-icons/md';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update';
  data: AttendancePolicy | null;
  onClose: () => void;
};

type FormValues = {
  companyId: string;
  effectiveFrom: Date | null;
  effectiveTo: Date | null;
  graceInMinutes: string;
  graceOutMinutes: string;
  lateThresholdMinutes: string;
  earlyLeaveThresholdMinutes: string;
  minWorkMinutes: string;
  absenceThresholdMinutes: string;
  movementEnabled: boolean;
  movementAllowMinutes: string;
  notes: string;
  halfDayThresholdMinutes: string;
  status?: boolean;
};

const numberRequired = (label: string) =>
  Yup.number()
    .typeError(`${label} must be a number`)
    .min(0, `${label} cannot be negative`)
    .required(`${label} is required`);

export default function AttendancePolicyForm({ action, data, onClose }: Props) {
  const { data: companyList } = useGetCompanyListQuery(undefined);
  const [createAttendancePolicy, { isLoading: creating }] =
    useCreateAttendancePolicyMutation();
  const [updateAttendancePolicy, { isLoading: updating }] =
    useUpdateAttendancePolicyMutation();

  const formik = useFormik<FormValues>({
    initialValues: {
      companyId: data?.companyId ? String(data.companyId) : '',
      effectiveFrom: data?.effectiveFrom ? new Date(data.effectiveFrom) : null,
      effectiveTo: data?.effectiveTo ? new Date(data.effectiveTo) : null,
      graceInMinutes:
        data?.graceInMinutes != null ? String(data.graceInMinutes) : '',
      graceOutMinutes:
        data?.graceOutMinutes != null ? String(data.graceOutMinutes) : '',
      lateThresholdMinutes:
        data?.lateThresholdMinutes != null
          ? String(data.lateThresholdMinutes)
          : '',
      earlyLeaveThresholdMinutes:
        data?.earlyLeaveThresholdMinutes != null
          ? String(data.earlyLeaveThresholdMinutes)
          : '',
      minWorkMinutes:
        data?.minWorkMinutes != null ? String(data.minWorkMinutes) : '',
      absenceThresholdMinutes:
        data?.absenceThresholdMinutes != null
          ? String(data.absenceThresholdMinutes)
          : '',
      movementEnabled: data?.movementEnabled ?? true,
      movementAllowMinutes:
        data?.movementAllowMinutes != null
          ? String(data.movementAllowMinutes)
          : '',
      notes: data?.notes ?? '',
      halfDayThresholdMinutes:
        data?.halfDayThresholdMinutes != null
          ? String(data.halfDayThresholdMinutes)
          : '',
      status: data?.status ?? true,
    },
    enableReinitialize: true,
    validationSchema: Yup.object({
      companyId: Yup.string().required('Company is required'),
      effectiveFrom: Yup.date()
        .nullable()
        .typeError('Effective from is required')
        .required('Effective from is required'),
      effectiveTo: Yup.date()
        .nullable()
        .typeError('Effective to is required')
        .required('Effective to is required'),
      lateThresholdMinutes: numberRequired('Late threshold'),
      earlyLeaveThresholdMinutes: numberRequired('Early leave threshold'),
      graceInMinutes: numberRequired('Grace period - In'),
      graceOutMinutes: numberRequired('Grace period - Out'),
      minWorkMinutes: numberRequired('Minimum working minutes'),
      halfDayThresholdMinutes: numberRequired('Half day threshold'),
      absenceThresholdMinutes: numberRequired('Absent threshold'),
      movementEnabled: Yup.boolean().required('Movement enabled is required'),
      movementAllowMinutes: Yup.number()
        .typeError('Movement allow minutes must be a number')
        .min(0, 'Movement allow minutes cannot be negative')
        .when('movementEnabled', {
          is: true,
          then: schema => schema.required('Movement allow minutes is required'),
          otherwise: schema => schema.notRequired(),
        }),
      notes: Yup.string().required('Notes is required'),
      status: Yup.boolean().required('Status is required'),
    }),
    onSubmit: async values => {
      const toInt = (v: string) => (v === '' ? undefined : parseInt(v, 10));
      const toIntOrZero = (v: string) => {
        const n = parseInt(v ?? '', 10);
        return Number.isNaN(n) ? 0 : n;
      };
      const formatDate = (d: Date | null) => {
        if (!d) return undefined;
        return new Date(d).toISOString().split('T')[0];
      };

      const payload: any = {
        companyId: toIntOrZero(values.companyId),
        effectiveFrom: formatDate(values.effectiveFrom),
        effectiveTo: formatDate(values.effectiveTo),
        graceInMinutes: toInt(values.graceInMinutes),
        graceOutMinutes: toInt(values.graceOutMinutes),
        lateThresholdMinutes: toInt(values.lateThresholdMinutes),
        earlyLeaveThresholdMinutes: toInt(values.earlyLeaveThresholdMinutes),
        minWorkMinutes: toInt(values.minWorkMinutes),
        halfDayThresholdMinutes: toInt(values.halfDayThresholdMinutes),
        absenceThresholdMinutes: toInt(values.absenceThresholdMinutes),
        movementEnabled: values.movementEnabled,
        movementAllowMinutes: values.movementEnabled
          ? toInt(values.movementAllowMinutes)
          : undefined,
        notes: values.notes,
        status: values.status ?? true,
      };

      Object.keys(payload).forEach(
        k => payload[k] === undefined && delete payload[k]
      );

      try {
        if (action === 'create') {
          await createAttendancePolicy(payload).unwrap();
          notifications.show({
            color: 'green',
            message: 'Attendance policy created',
          });
        } else if (action === 'update' && data?.id) {
          await updateAttendancePolicy({ id: data.id, data: payload }).unwrap();
          notifications.show({
            color: 'green',
            message: 'Attendance policy updated',
          });
        }
        onClose();
      } catch (e: any) {
        notifications.show({
          color: 'red',
          message: e?.data?.message || 'Failed to save policy',
        });
      }
    },
  });

  useEffect(() => {
    // ensure touched resets when switching modes
    formik.setTouched({});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [action, data?.id]);

  const numChange =
    (field: keyof FormValues) => (e: ChangeEvent<HTMLInputElement>) =>
      formik.setFieldValue(field, e.currentTarget.value);

  return (
    <form onSubmit={formik.handleSubmit} className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-x-2">
          <Select
            label="Company"
            size="sm"
            radius={8}
            placeholder="Select Company"
            className="min-w-60"
            withAsterisk
            searchable
            clearable
            data={
              companyList?.data?.map(c => ({
                label: c.name,
                value: String(c.id),
              })) ?? []
            }
            name="companyId"
            value={formik.values.companyId}
            onChange={val => formik.setFieldValue('companyId', val ?? '')}
            onBlur={() => formik.setFieldTouched('companyId', true)}
            error={
              formik.touched.companyId || formik.submitCount > 0
                ? (formik.errors.companyId as string | undefined)
                : undefined
            }
          />
          <DatePickerInput
            label="Effective From"
            placeholder="Effective From"
            withAsterisk
            size="sm"
            radius="md"
            className="min-w-60"
            value={formik.values.effectiveFrom}
            onChange={date => formik.setFieldValue('effectiveFrom', date)}
            onBlur={() => formik.setFieldTouched('effectiveFrom', true)}
            error={formik.touched.effectiveFrom && formik.errors.effectiveFrom}
          />
          <DatePickerInput
            label="Effective To"
            placeholder="Effective To"
            withAsterisk
            size="sm"
            radius="md"
            className="min-w-60"
            value={formik.values.effectiveTo}
            onChange={date => formik.setFieldValue('effectiveTo', date)}
            onBlur={() => formik.setFieldTouched('effectiveTo', true)}
            error={formik.touched.effectiveTo && formik.errors.effectiveTo}
          />
        </div>
      </div>

      {/* Late/Early Thresholds */}
      <div className="bg-white p-5 rounded-2xl shadow-lg">
        <div className="grid grid-cols-2 items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="bg-[#FFE2E2] flex items-center justify-center p-2 rounded-lg">
              <FiAlertTriangle size={25} color="#E7000B" />
            </div>
            <div>
              <h5 className="text-[18px] text-[#1E293B] font-medium">
                Late/Early Thresholds
              </h5>
              <p className="text-[14px] text-[#64748B]">
                Configure thresholds for marking employees late or early
              </p>
            </div>
          </div>
        </div>

        <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
          <div className="rounded-lg border border-rose-200 bg-rose-50 p-3">
            <div className="flex items-center gap-2">
              <div className="text-xs text-rose-700">
                Late Threshold (minutes)
              </div>
              <Tooltip
                label="Minutes after scheduled time to be marked late"
                withArrow
                color="gray"
              >
                <AiOutlineQuestionCircle size={14} className="text-rose-600" />
              </Tooltip>
            </div>
            <TextInput
              variant="unstyled"
              placeholder="Enter late threshold"
              name="lateThresholdMinutes"
              value={formik.values.lateThresholdMinutes}
              onChange={numChange('lateThresholdMinutes')}
              onBlur={formik.handleBlur}
              error={
                formik.touched.lateThresholdMinutes
                  ? (formik.errors.lateThresholdMinutes as string | undefined)
                  : undefined
              }
            />
          </div>
          <div className="rounded-lg border border-amber-200 bg-amber-50 p-3">
            <div className="flex items-center gap-2">
              <div className="text-xs text-amber-700">
                Early Leave Threshold (minutes)
              </div>
              <Tooltip
                label="Minutes before end time to be marked early leave"
                withArrow
                color="gray"
              >
                <AiOutlineQuestionCircle size={14} className="text-amber-600" />
              </Tooltip>
            </div>
            <TextInput
              variant="unstyled"
              placeholder="Enter early leave threshold"
              name="earlyLeaveThresholdMinutes"
              value={formik.values.earlyLeaveThresholdMinutes}
              onChange={numChange('earlyLeaveThresholdMinutes')}
              onBlur={formik.handleBlur}
              error={
                formik.touched.earlyLeaveThresholdMinutes
                  ? (formik.errors.earlyLeaveThresholdMinutes as
                      | string
                      | undefined)
                  : undefined
              }
            />
          </div>
        </div>
      </div>

      {/* Grace Periods */}
      <div className="bg-white p-5 rounded-2xl shadow-lg">
        <div className="flex items-center gap-3">
          <div className="bg-[#E0E7FF] flex items-center justify-center p-2 rounded-lg">
            <MdOutlineWatchLater size={25} className="text-[#4F46E5]" />
          </div>
          <div>
            <h5 className="text-[18px] text-[#1E293B] font-medium">
              Grace Periods
            </h5>
            <p className="text-[14px] text-[#64748B]">
              Allowed grace time for in/out
            </p>
          </div>
        </div>
        <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
          <div className="rounded-lg border border-blue-200 bg-blue-50 p-3">
            <div className="text-xs text-blue-700">In Grace (minutes)</div>
            <TextInput
              variant="unstyled"
              value={formik.values.graceInMinutes}
              onChange={numChange('graceInMinutes')}
              onBlur={formik.handleBlur}
              placeholder="Enter In Grace (minutes)"
              error={
                formik.touched.graceInMinutes
                  ? (formik.errors.graceInMinutes as string | undefined)
                  : undefined
              }
            />
          </div>
          <div className="rounded-lg border border-indigo-200 bg-indigo-50 p-3">
            <div className="text-xs text-indigo-700">Out Grace (minutes)</div>
            <TextInput
              variant="unstyled"
              value={formik.values.graceOutMinutes}
              onChange={numChange('graceOutMinutes')}
              onBlur={formik.handleBlur}
              placeholder="Enter Out Grace (minutes)"
              error={
                formik.touched.graceOutMinutes
                  ? (formik.errors.graceOutMinutes as string | undefined)
                  : undefined
              }
            />
          </div>
        </div>
      </div>

      {/* Additional Rules */}
      <div className="bg-white p-5 rounded-2xl shadow-lg">
        <div className="flex items-center gap-3">
          <div className="bg-[#E2E8F0] flex items-center justify-center p-2 rounded-lg">
            <IoSettingsOutline size={24} className="text-[#0F172A]" />
          </div>
          <div>
            <h5 className="text-[18px] text-[#1E293B] font-medium">
              Additional Rules
            </h5>
            <p className="text-[14px] text-[#64748B]">
              Minimum work minutes and geo movement
            </p>
          </div>
        </div>
        <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
          <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
            <div className="text-xs text-slate-700">
              Minimum Working Minutes
            </div>
            <TextInput
              placeholder="Enter Minimum Working Minutes"
              variant="unstyled"
              value={formik.values.minWorkMinutes}
              onChange={numChange('minWorkMinutes')}
              onBlur={formik.handleBlur}
              error={
                formik.touched.minWorkMinutes
                  ? (formik.errors.minWorkMinutes as string | undefined)
                  : undefined
              }
            />
          </div>
          <div className="rounded-lg border border-emerald-200 bg-emerald-50 p-3">
            <div className="flex items-center justify-between">
              <div className="text-xs text-emerald-700">Movement</div>
              <Switch
                color="#10B981"
                size="sm"
                checked={formik.values.movementEnabled}
                onChange={e =>
                  formik.setFieldValue(
                    'movementEnabled',
                    e.currentTarget.checked
                  )
                }
              />
            </div>
            <TextInput
              placeholder="Minutes"
              disabled={!formik.values.movementEnabled}
              variant="unstyled"
              value={formik.values.movementAllowMinutes}
              onChange={numChange('movementAllowMinutes')}
              onBlur={formik.handleBlur}
              error={
                formik.touched.movementAllowMinutes
                  ? (formik.errors.movementAllowMinutes as string | undefined)
                  : undefined
              }
            />
          </div>
        </div>
      </div>

      {/* Absence / Half-day */}
      <div className="bg-white p-5 rounded-2xl shadow-lg">
        <div className="flex items-center gap-3">
          <div className="bg-[#FFF7ED] flex items-center justify-center p-2 rounded-lg">
            <GoPerson size={22} className="text-[#F59E0B]" />
          </div>
          <div>
            <h5 className="text-[18px] text-[#1E293B] font-medium">
              Half Day / Absence
            </h5>
            <p className="text-[14px] text-[#64748B]">
              Thresholds for half-day and absence
            </p>
          </div>
        </div>
        <div className="mt-3 grid grid-cols-1 md:grid-cols-2 gap-3">
          <div className="rounded-lg border border-orange-200 bg-orange-50 p-3">
            <div className="text-xs text-orange-700">
              Half Day Threshold (minutes)
            </div>
            <TextInput
              placeholder="Enter Half Day Threshold (minutes)"
              variant="unstyled"
              value={formik.values.halfDayThresholdMinutes}
              onChange={numChange('halfDayThresholdMinutes')}
              onBlur={formik.handleBlur}
              error={
                formik.touched.halfDayThresholdMinutes
                  ? (formik.errors.halfDayThresholdMinutes as
                      | string
                      | undefined)
                  : undefined
              }
            />
          </div>
          <div className="rounded-lg border border-red-200 bg-red-50 p-3">
            <div className="text-xs text-red-700">
              Absent Threshold (minutes)
            </div>
            <TextInput
              placeholder="Enter Absent Threshold (minutes)"
              variant="unstyled"
              value={formik.values.absenceThresholdMinutes}
              onChange={numChange('absenceThresholdMinutes')}
              onBlur={formik.handleBlur}
              error={
                formik.touched.absenceThresholdMinutes
                  ? (formik.errors.absenceThresholdMinutes as
                      | string
                      | undefined)
                  : undefined
              }
            />
          </div>
        </div>
      </div>

      {/* Notes + Status */}
      <div className="bg-white p-5 rounded-2xl shadow-lg">
        <div className="grid grid-cols-1 gap-4">
          <div className="flex flex-col gap-2">
            <div className="flex items-center gap-2">
              <Text className="text-sm text-[#1E293B] font-medium">Notes</Text>
              <Tooltip label="Add any additional notes" withArrow color="gray">
                <AiOutlineQuestionCircle size={15} className="text-[#94A3B8]" />
              </Tooltip>
            </div>
            <Textarea
              minRows={3}
              placeholder="Enter notes or comments about this attendance policy..."
              value={formik.values.notes}
              onChange={e =>
                formik.setFieldValue('notes', e.currentTarget.value)
              }
              onBlur={formik.handleBlur}
              error={
                formik.touched.notes
                  ? (formik.errors.notes as string | undefined)
                  : undefined
              }
              className="col-span-12"
            />
          </div>
        </div>
        <div className="flex items-center gap-3 mt-3">
          <Text className="text-xs text-[#64748B]">Status</Text>
          <Switch
            color="#F69348"
            size="xs"
            checked={formik.values.status ?? true}
            onChange={e =>
              formik.setFieldValue('status', e.currentTarget.checked)
            }
          />
        </div>
      </div>

      {/* Footer */}
      <div className="flex items-center justify-end gap-2 pt-2">
        <Button
          variant="default"
          radius="md"
          onClick={onClose}
          disabled={creating || updating}
        >
          Cancel
        </Button>
        <Button
          radius="md"
          color="orange"
          type="submit"
          loading={formik.isSubmitting || creating || updating}
          disabled={creating || updating}
        >
          {action === 'create' ? 'Create Policy' : 'Update Policy'}
        </Button>
      </div>
    </form>
  );
}
