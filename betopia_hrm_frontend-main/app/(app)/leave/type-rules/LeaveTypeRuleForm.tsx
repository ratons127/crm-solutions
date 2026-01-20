import { getLeaveTypeList } from '@/lib/features/leave/type/leaveTypeAPI';
import {
  useCreateLeaveTypeRuleMutation,
  useUpdateLeaveTypeRuleMutation,
} from '@/services/api/leave/leaveTypeRuleAPI';
import { Button, Select, Switch, Textarea, TextInput } from '@mantine/core';
import { Form, useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useEffect, useState } from 'react';
import {
  LeaveType,
  LeaveTypeRule,
  LeaveTypeRuleCreate,
} from '../../../lib/types/leave';

type FormProps =
  | {
      action: 'create';
      onClose: () => void;
    }
  | {
      action: 'update';
      data: LeaveTypeRule;
      onClose: () => void;
    };

export default function LeaveTypeRuleForm(props: FormProps) {
  const [leaveTypeList, setLeaveTypeList] = useState<LeaveType[]>([]);
  const [createAsync] = useCreateLeaveTypeRuleMutation();
  const [updateAsync] = useUpdateLeaveTypeRuleMutation();

  const form = useForm<LeaveTypeRuleCreate>({
    initialValues:
      props.action === 'update'
        ? {
            leaveTypeId: (props.data.leaveType as LeaveType)?.id!,
            description: props.data.description,
            isActive: props.data.isActive,
            ruleKey: props.data.ruleKey,
            ruleValue: props.data.ruleValue,
          }
        : {
            leaveTypeId: 0,
            description: '',
            isActive: true,
            ruleKey: '',
            ruleValue: '',
          },
    validate: {
      leaveTypeId(value) {
        return !Boolean(value) ? 'Leave Type required' : null;
      },
      description(value) {
        return !value ? 'Description required' : null;
      },
      ruleKey(value) {
        return !value ? 'Rule key required' : null;
      },
      ruleValue(value) {
        return !value ? 'Rule value required' : null;
      },
    },
  });

  useEffect(() => {
    getLeaveTypeList().then(data => {
      setLeaveTypeList(data);
    });
  }, []);

  return (
    <Form
      form={form}
      onSubmit={async values => {
        try {
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
            title: 'Creation error',
            message: 'Creation failed',
          });
          // //  console.log('GOT_CREATION_ERROR', error);
        } finally {
          props.onClose();
        }
      }}
    >
      <div className="space-y-3 w-full flex-1">
        <div className="flex justify-between items-center gap-5">
          <Select
            placeholder="Select Leave Type"
            flex={1}
            data={leaveTypeList
              ?.filter(x => x.status)
              .map((x: LeaveType) => ({
                label: `${x.name} (${x.code})`,
                value: x.id.toString(),
              }))}
            label="Leave type"
            {...form.getInputProps('leaveTypeId')}
          />
          <Switch
            label="Is Active"
            placeholder="Active"
            {...form.getInputProps('isActive', { type: 'checkbox' })}
          />
        </div>
        <div className="flex items-center gap-2">
          <TextInput
            flex={1}
            label="Rule Key"
            placeholder="rule key"
            {...form.getInputProps('ruleKey')}
          />
          <TextInput
            flex={1}
            label="Rule Value"
            placeholder="rule value"
            {...form.getInputProps('ruleValue')}
          />
        </div>
        <Textarea
          label="Description"
          placeholder="description"
          {...form.getInputProps('description')}
        />

        <div className=" flex items-center  gap-5 justify-end mt-10">
          <Button
            onClick={() => {
              props.onClose();
            }}
            color="red"
            type="button"
          >
            Cancel
          </Button>
          <Button type="submit">
            {props.action === 'create' ? 'Create' : 'Update'}
          </Button>
        </div>
      </div>
    </Form>
  );
}
