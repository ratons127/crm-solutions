import SubmitSection from '@/components/common/form/SubmitSection';
import { useGetCountryListQuery } from '@/services/api/admin/location/countryAPI';
import {
  useCreateLocationMutation,
  useGetLocationListQuery,
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

export default function PoliceStationForm({ action, data, onClose }: Props) {
  const form = useForm<any>({
    initialValues:
      action === 'update' && data
        ? {
            countryId:
              data.countryId?.toString() || data.country?.id?.toString(),
            // geoHash: data.geoHash,
            name: data.name,
            parentId: data.parentId?.toString() ?? null,
            status: data.status,
            type: 'POLICE_STATION',
          }
        : {
            countryId: '',
            // geoHash: '',
            name: '',
            parentId: '',
            status: true,
            type: 'POLICE_STATION',
          },

    validate: {
      countryId: value =>
        action === 'create' && !value && 'Country is required',
      parentId: value =>
        action === 'create' && !value && 'District is required',
      name: value => !value && 'Name is required',
      // geoHash: value => !value && 'Geo Hash is required.',
    },
  });

  const { data: countryListData } = useGetCountryListQuery({});
  const { data: locationData } = useGetLocationListQuery({});

  const districtList =
    locationData?.data?.filter(
      x =>
        x.type === 'DISTRICT' &&
        x.countryId?.toString() === form.values.countryId
    ) ?? [];

  const [createAsync] = useCreateLocationMutation();
  const [updateAsync] = useUpdateLocationMutation();
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
        message: `Police Station ${action === 'create' ? 'created' : 'updated'} successfully`,
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
            withAsterisk={action === 'create'}
            className="col-span-2 sm:col-span-3"
            label="Country"
            placeholder="Select country"
            {...form.getInputProps('countryId')}
            searchable
            readOnly={action === 'update'}
            disabled={action === 'update'}
            data={countryListData?.data?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
          />
          <Select
            withAsterisk={action === 'create'}
            className="col-span-2 sm:col-span-3"
            label="District"
            placeholder="Select district"
            searchable
            key={form.values.countryId}
            {...form.getInputProps('parentId')}
            readOnly={action === 'update'}
            disabled={action === 'update' || !form.values.countryId}
            data={districtList?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
          />
          <TextInput
            withAsterisk
            className="col-span-2 sm:col-span-3"
            label="Police Station Name"
            placeholder="police Station name"
            {...form.getInputProps('name')}
          />
          {/* <TextInput
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
