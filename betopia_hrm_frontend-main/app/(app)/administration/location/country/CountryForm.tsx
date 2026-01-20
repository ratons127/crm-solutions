import SubmitSection from '@/components/common/form/SubmitSection';
import { Country, CountryCreate } from '@/lib/types/admin/location';
import {
  useCreateCountryMutation,
  useUpdateCountryMutation,
} from '@/services/api/admin/location/countryAPI';
import { TextInput } from '@mantine/core';
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
      data: Country;
    };

export default function CountryForm(props: Props) {
  const [createAsync] = useCreateCountryMutation();
  const [updateAsync] = useUpdateCountryMutation();
  const form = useForm<CountryCreate>({
    initialValues:
      props?.action === 'create'
        ? {
            code: '',
            name: '',
            region: '',
          }
        : {
            code: props.data.code,
            name: props.data.name,
            region: props.data.region,
          },
    validate: {
      code: value => !value && 'Code name is required',
      name: value => !value && 'Name is required',
      region: value => !value && 'Region is required',
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
        message: `Country ${props.action === 'create' ? 'created' : 'updated'} successfully`,
        color: 'green',
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

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <div className="space-y-2">
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-6 gap-2">
          <TextInput
            className="col-span-2 sm:col-span-3 md:col-span-4"
            label="Country Name"
            placeholder="country name"
            {...form.getInputProps('name')}
          />
          <TextInput
            className="col-span-2 sm:col-span-3 md:col-span-2"
            label="Country Code"
            placeholder="country code"
            {...form.getInputProps('code')}
          />
          <TextInput
            className="col-span-2 sm:col-span-3 md:col-span-6"
            label="Region"
            placeholder="region"
            {...form.getInputProps('region')}
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
