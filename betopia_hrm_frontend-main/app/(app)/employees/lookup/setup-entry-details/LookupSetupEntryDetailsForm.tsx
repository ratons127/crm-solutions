import SubmitSection from '@/components/common/form/SubmitSection';
import {
  LookupSetupEntry,
  LookupSetupEntryDetails,
  LookupSetupEntryDetailsCreate,
} from '@/lib/types/employee/lookup';
import { useGetLookupSetupEntryListQuery } from '@/services/api/employee/lookupSetupEntryAPI';
import {
  useCreateLookupSetupEntryDetailsMutation,
  useUpdateLookupSetupEntryDetailsMutation,
} from '@/services/api/employee/lookupSetupEntryDetailsAPI';
import { Select, Textarea, TextInput } from '@mantine/core';
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
      data: LookupSetupEntryDetails;
    };

export default function LookupSetupEntryForm(props: Props) {
  const { data: lookupEntryListData } =
    useGetLookupSetupEntryListQuery(undefined);
  const [createAsync] = useCreateLookupSetupEntryDetailsMutation();
  const [updateAsync] = useUpdateLookupSetupEntryDetailsMutation();
  const form = useForm<LookupSetupEntryDetailsCreate>({
    initialValues:
      props?.action === 'update' && props.data
        ? {
            name: props.data.name,
            status: props.data.status,
            details: props.data.details,
            lookupSetupEntryId: props.data?.setup?.id.toString() ?? 0,
          }
        : {
            name: '',
            status: true,
            details: '',
            lookupSetupEntryId: '0',
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
      <div className="space-y-2">
        <TextInput
          flex={1}
          label="Name"
          placeholder="name"
          {...form.getInputProps('name')}
        />
        <div className="grid grid-cols-1 sm:grid-cols-2 items-center justify-between gap-3">
          <Select
            label="Lookup Setup Entry"
            data={lookupEntryListData?.data?.map((x: LookupSetupEntry) => ({
              label: x.name,
              value: x.id.toString(),
            }))}
            {...form.getInputProps('lookupSetupEntryId')}
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
            {...form.getInputProps('status')}
            value={form.getInputProps('status').value === true ? '1' : '0'}
            onChange={v => {
              form.getInputProps('status').onChange(v === '1' ? true : false);
            }}
          />
          <Textarea label="Details" {...form.getInputProps('details')} />
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
