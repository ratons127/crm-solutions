
'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { createLeaveBalanceEmployee } from '@/lib/features/leave/balanceEmployee/balanceEmployeeAPI';
import { Select } from '@mantine/core';
import { YearPickerInput } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useEffect, useMemo, useState } from 'react';
import { getEmployeeList } from '../../../lib/features/employees/employeeAPI';
import { LeaveBalanceEmployee } from '../../../lib/types/leave';

type FormProps = {
  action: 'edit' | 'create' | null;
  data: LeaveBalanceEmployee | null;
  onClose: () => void;
  selectedEmployeeId?: number; // optional preselect
};

type EmployeeOption = { label: string; value: string };

type FormValues = {
  employeeId: string;      // Mantine Select -> string
  year: string | null;     // we'll keep a 4-digit year string in state
};

export default function LeaveBalanceEmployeeForm(props: FormProps) {
  const [employeeList, setEmployeeList] = useState<any[]>([]);
  const employeeOptions: EmployeeOption[] = useMemo(
    () =>
      (employeeList ?? []).map((x: any) => ({
        label:
          x.name ||
          x.fullName ||
          [x.firstName, x.lastName].filter(Boolean).join(' ') ||
          `#${x.id}`,
        value: String(x.id),
      })),
    [employeeList]
  );

  const form = useForm<FormValues>({
    initialValues: {
      employeeId: '',
      year: String(new Date().getFullYear()), // default to current year (string)
    },
    validate: {
      employeeId: (v) => (!v ? 'Employee is required' : null),
      year: (v) => (!v ? 'Year is required' : null),
    },
  });

  // Load employees
  useEffect(() => {
    getEmployeeList().then((data) => setEmployeeList(data || []));
  }, []);

  // Prefill from props
  useEffect(() => {
    if (props.selectedEmployeeId) {
      form.setFieldValue('employeeId', String(props.selectedEmployeeId));
    }
    if (props.action === 'edit' && props.data) {
      if (props.data.employeeId) form.setFieldValue('employeeId', String(props.data.employeeId));
      if (props.data.year) form.setFieldValue('year', String(props.data.year));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [props.selectedEmployeeId, props.action, props.data]);

  // Helper: robustly parse to a number year
  const parseYearNumber = (val: string | null): number | null => {
    if (!val) return null;
    // if it's already a 4-digit year
    if (/^\d{4}$/.test(val)) return Number(val);
    // try to extract 4 digits from strings like "2024-01-01"
    const match = val.match(/\b(\d{4})\b/);
    if (match) return Number(match[1]);
    // last resort: Date parse
    const d = new Date(val);
    const y = d.getFullYear();
    return Number.isNaN(y) ? null : y;
  };

  return (
    <div>
      <form
        onSubmit={form.onSubmit(async (values) => {
          const yearNum = parseYearNumber(values.year);

          if (!yearNum) {
            form.setFieldError('year', 'Valid year is required');
            return;
          }
          if (!values.employeeId) {
            form.setFieldError('employeeId', 'Employee is required');
            return;
          }

          const payload = {
            employeeId: Number(values.employeeId),
            year: yearNum, // strictly number for the API
          };

          try {
            if (props.action === 'create') {
              await createLeaveBalanceEmployee(payload);
              notifications.show({
                title: 'Create',
                message: 'Leave balance employee created successfully',
                color: 'green',
              });
            } else if (props.action === 'edit' && props.data) {
              // TODO: call update API when available
              notifications.show({
                title: 'Update',
                message: 'Leave balance employee updated',
                color: 'green',
              });
            }
            props.onClose();
          } catch {
            notifications.show({
              title: props.action === 'create' ? 'Creation' : 'Update',
              message: 'Leave balance employee failed',
              color: 'red',
            });
          }
        })}
        className="space-y-3"
      >
        <h1 className="text-lg font-semibold">
          {props.action === 'create' ? 'Create' : 'Update'} Leave Balance Employee
        </h1>

        {/* Employee */}
        <Select
          label="Select Employee"
          placeholder="Choose an employee"
          searchable
          clearable
          data={employeeOptions}
          value={form.values.employeeId || null}
          onChange={(v) => form.setFieldValue('employeeId', v ?? '')}
          error={form.errors.employeeId}
          withAsterisk
          comboboxProps={{ withinPortal: true }}
        />

        {/* Year */}
        <YearPickerInput
          label="Pick year"
          placeholder="Pick a year"
          minDate={new Date(2010, 0, 1)}
          maxDate={new Date(new Date().getFullYear(), 11, 31)}
          value={form.values.year}
          // Normalize whatever the picker gives us into a 4-digit year string
          onChange={(value) => {
            const yearStr =
              value === null
                ? null
                : String(value).match(/\b(\d{4})\b/)?.[1] ?? '';

            form.setFieldValue('year', yearStr);
          }}
          error={form.errors.year}
          valueFormat="YYYY"   // display just the year
          allowDeselect
          clearable
          popoverProps={{ withinPortal: true }}
        />

        <SubmitSection
          onCancel={props.onClose}
          confirmText={props.action === 'create' ? 'Create' : 'Update'}
          cancelText="Cancel"
        />
      </form>
    </div>
  );
}
