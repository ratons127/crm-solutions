import SubmitSection from '@/components/common/form/SubmitSection';
import { getLeaveGroupList } from '@/lib/features/leave/leaveGroup/leaveGroupAPI';
import { getLeaveTypeList } from '@/lib/features/leave/type/leaveTypeAPI';
import {
  LeaveEligibility,
  LeaveEligibilityCreate,
  LeaveGroupType,
  LeaveType,
} from '@/lib/types/leave';
import {
  useCreateLeaveEligibilityMutation,
  useUpdateLeaveEligibilityMutation,
} from '@/services/api/leave/leaveEligibilityAPI';
import { NumberInput, Select, TextInput } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useEffect, useState } from 'react';

type Props =
  | {
      action: 'create';
      onClose: () => void;
    }
  | {
      action: 'update';
      onClose: () => void;
      data: LeaveEligibility;
    };

export default function LeaveEligibilityForm(props: Props) {
  const [leaveTypeList, setLeaveTypeList] = useState<LeaveType[]>([]);
  const [leaveGroupList, setLeaveGroupList] = useState<LeaveGroupType[]>([]);
  const [createAsync] = useCreateLeaveEligibilityMutation();
  const [updateAsync] = useUpdateLeaveEligibilityMutation();
  const form = useForm<LeaveEligibilityCreate>({
    initialValues:
      props?.action === 'create'
        ? {
            gender: 'MALE',
            leaveTypeId: '1',
            leaveGroupId: '1',
            employmentStatus: 'Full Time',
            isActive: true,
            maxTenureMonths: 1,
            minTenureMonths: 1,
          }
        : {
            gender: props.data.gender,
            leaveGroupId: props.data.leaveGroup?.id.toString() ?? 0,
            leaveTypeId: props.data.leaveType?.id.toString() ?? 0,
            employmentStatus: props.data.employmentStatus,
            isActive: props.data.isActive,
            maxTenureMonths: props.data.maxTenureMonths,
            minTenureMonths: props.data.minTenureMonths,
          },
    validate: {
      gender: value => !value && 'Gender required',
      leaveTypeId: value => !value && 'Leave type required',
      leaveGroupId: value => !value && 'Leave group required',
      employmentStatus: value => !value && 'Employee status required',
    },
  });

  const handleSubmit = async (values: typeof form.values) => {
    try {
      //   throw new Error('P');
      if (props.action === 'create') {
        await createAsync(values);
      } else {
        await updateAsync({
          id: props.data.id,
          data: values,
        });
      }

      notifications.show({
        title: props.action === 'create' ? 'Create' : 'Update',
        message: `Leave Type ${props.action === 'create' ? 'created' : 'updated'} successfully`,
      });
    } catch (error) {
      notifications.show({
        title: 'Error',
        message: `Got an error while ${props.action}`,
        color: 'red',
      });
    } finally {
      props.onClose();
    }
  };

  useEffect(() => {
    getLeaveTypeList().then(data => {
      setLeaveTypeList(data);
    });
    getLeaveGroupList().then(data => {
      setLeaveGroupList(data);
    });
  }, []);

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <div className="space-y-3">
        <Select
          label="Leave Type"
          placeholder="Select Leave type"
          {...form.getInputProps('leaveTypeId')}
          data={leaveTypeList?.map((x: LeaveType) => ({
            label: `${x.name} (${x.code})`,
            value: x.id.toString(),
          }))}
        />
        <Select
          label="Leave Group"
          placeholder="Select Leave Group"
          {...form.getInputProps('groupId')}
          data={leaveGroupList?.map((x: LeaveGroupType) => ({
            label: x.name,
            value: x.id.toString(),
          }))}
        />

        <Select
          label="Gender"
          data={[
            { label: 'Male', value: 'MALE' },
            {
              label: 'Female',
              value: 'FEMALE',
            },
          ]}
          {...form.getInputProps('gender')}
        />
        <div className="flex items-center justify-between gap-3">
          <TextInput
            flex={1}
            label="Employment status"
            placeholder="employment status"
            {...form.getInputProps('employmentStatus')}
          />

          <Select
            label="Status"
            placeholder="status"
            data={[
              {
                label: 'Active',
                value: '1',
              },
              {
                label: 'Disabled',
                value: '0',
              },
            ]}
            {...form.getInputProps('isActive')}
            value={form.getInputProps('isActive').value === true ? '1' : '0'}
            onChange={v => {
              form.getInputProps('isActive').onChange(v === '1' ? true : false);
            }}
          />
        </div>
        <div className="flex items-center justify-between gap-3">
          <NumberInput
            flex={1}
            label="Min Tenure Months"
            placeholder="min Tenure Months"
            {...form.getInputProps('minTenureMonths')}
            min={1}
          />
          <NumberInput
            flex={1}
            label="Max Tenure Months"
            placeholder="max Tenure Months"
            {...form.getInputProps('maxTenureMonths')}
            min={1}
          />
        </div>

        <SubmitSection
          onCancel={props.onClose}
          confirmText={props.action === 'create' ? 'Create' : 'Update'}
          //   onConfirm={form.onSubmit(handleSubmit)}
        />
      </div>
    </form>
  );
}
