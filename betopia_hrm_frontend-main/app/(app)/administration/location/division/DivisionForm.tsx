import SubmitSection from '@/components/common/form/SubmitSection';
import { useGetCountryListQuery } from '@/services/api/admin/location/countryAPI';
import {
  useCreateLocationMutation,
  useUpdateLocationMutation,
} from '@/services/api/admin/location/locationAPI';
import { Select, TextInput } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';

type Props = {
  action: 'create' | 'update' | null;
  data: any;
  onClose: () => void;
};

export default function DivisionForm({ action, data, onClose }: Props) {
  const { data: countryListData } = useGetCountryListQuery({});
  const [createAsync] = useCreateLocationMutation();
  const [updateAsync] = useUpdateLocationMutation();
  const form = useForm<any>({
    initialValues:
      action === 'update' && data
        ? {
            countryId: data.country?.id?.toString(),
            // geoHash: data.geoHash,
            name: data.name,
            parentId: null,
            status: data.status,
            type: 'DIVISION',
          }
        : {
            countryId: '',
            // geoHash: '',
            name: '',
            parentId: null,
            status: true,
            type: 'DIVISION',
          },
    validate: {
      countryId: value => !value && 'Country is required',
      name: value => !value && 'Name is required',
      // geoHash: value => !value && 'Geo Hash is required.',
    },
  });

  const handleSubmit = async (values: typeof form.values) => {
    try {
      //   throw new Error('P');
      if (action === 'create') {
        await createAsync(values);
      } else {
        await updateAsync({
          id: data.id,
          data: values,
        });
      }

      notifications.show({
        title: action === 'create' ? 'Create' : 'Update',
        message: `Division ${action === 'create' ? 'created' : 'updated'} successfully`,
        color: 'green',
      });
    } catch (error) {
      notifications.show({
        title: 'Error',
        message: `Got an error while ${action}`,
        color: 'red',
      });
    } finally {
      onClose();
    }
  };

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <div className="space-y-2">
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-6 gap-2">
          <Select
            withAsterisk
            label="Country"
            placeholder="Select country"
            className="col-span-2 sm:col-span-3"
            {...form.getInputProps('countryId')}
            data={countryListData?.data?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
          />
          <TextInput
            withAsterisk
            className="col-span-2 sm:col-span-3"
            label="Division Name"
            placeholder="division name"
            {...form.getInputProps('name')}
          />
          {/* <TextInput
            withAsterisk
            className="col-span-6"
            label="Geo Hash"
            placeholder="geo hash"
            {...form.getInputProps('geoHash')}
          /> */}
        </div>

        <SubmitSection
          onCancel={onClose}
          confirmText={action === 'create' ? 'Create' : 'Update'}
          //   onConfirm={form.onSubmit(handleSubmit)}
        />
      </div>
    </form>
  );
}
