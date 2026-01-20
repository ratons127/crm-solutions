import SubmitSection from '@/components/common/form/SubmitSection';
import {
  LookupSetupEntry,
  LookupSetupEntryCreate,
} from '@/lib/types/employee/lookup';
import {
  useCreateLookupSetupEntryMutation,
  useUpdateLookupSetupEntryMutation,
} from '@/services/api/employee/lookupSetupEntryAPI';
import { Select, TextInput } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';

type Props =
  | {
      action: 'create';
      onClose: () => void;
    }
  | {
      action: 'update';
      onClose: () => void;
      data: LookupSetupEntry;
    };

export default function LookupSetupEntryForm(props: Props) {
  const [createAsync] = useCreateLookupSetupEntryMutation();
  const [updateAsync] = useUpdateLookupSetupEntryMutation();
  const form = useForm<LookupSetupEntryCreate>({
    initialValues:
      props?.action === 'create'
        ? {
            name: '',
            status: true,
          }
        : {
            name: props.data.name,
            status: props.data.status,
          },
    validate: {
      name: value => !value && 'Name required',
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
    } catch (error) {
      notifications.show({
        title: 'Error',
        message: `Got an error while ${props.action}`,
      });
    } finally {
      props.onClose();
    }
  };

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <div className="space-y-3">
        <div className="flex items-center justify-between gap-3">
          <TextInput
            flex={1}
            label="Name"
            placeholder="name"
            {...form.getInputProps('name')}
          />
          {/* <Switch
            label="Is active"
            placeholder="is active"
            {...form.getInputProps('status', { type: 'checkbox' })}
          /> */}
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
            {...form.getInputProps('status')}
            value={form.getInputProps('status').value === true ? '1' : '0'}
            onChange={v => {
              form.getInputProps('status').onChange(v === '1' ? true : false);
            }}
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
