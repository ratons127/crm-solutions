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

export default function DistrictForm({ action, data, onClose }: Props) {
  const form = useForm<any>({
    initialValues:
      action === 'update' && data
        ? {
            countryId: data.countryId?.toString() || data.country?.id?.toString(),
            // geoHash: data.geoHash,
            name: data.name,
            parentId: data.parentId?.toString() ?? null,
            status: data.status,
            type: 'DISTRICT',
          }
        : {
            countryId: '',
            // geoHash: '',
            name: '',
            parentId: '',
            status: true,
            type: 'DISTRICT',
          },
    validate: {
      countryId: (value) => action === 'create' && !value && 'Country is required',
      parentId: (value) => action === 'create' && !value && 'Division is required',
      name: (value) => !value && 'Name is required',
      // geoHash: value => !value && 'Geo Hash is required.',
    },
  });
  const { data: countryListData } = useGetCountryListQuery({});
  const { data: locationData } = useGetLocationListQuery({});
  
  const divisionList =
    locationData?.data?.filter(x => {
      return (
        x.type === 'DIVISION' &&
        x.countryId?.toString() === form.values.countryId
      );
    }) ?? [];

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
        message: `District ${action === 'create' ? 'created' : 'updated'} successfully`,
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
            withAsterisk={action === 'create'} // ✅ Only required when creating
            className="col-span-2 sm:col-span-3"
            label="Country"
            placeholder="Select country"
            searchable
            readOnly={action === 'update'} // ✅ Readonly when updating
            disabled={action === 'update'} // ✅ Disabled when updating
            data={countryListData?.data?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
            // This replaces the need for useEffect
            value={form.values.countryId}
            onChange={value => {
              form.setFieldValue('countryId', value);
              form.setFieldValue('parentId', ''); // reset division
              form.validateField('parentId'); // revalidate division
            }}
            error={form.errors.countryId}
          />
          <Select
            withAsterisk={action === 'create'} // ✅ Only required when creating
            className="col-span-2 sm:col-span-3"
            label="Division"
            placeholder="Select division"
            searchable
            key={form.values.countryId} // Force re-render when country changes
            {...form.getInputProps('parentId')}
            readOnly={action === 'update'} // ✅ Readonly when updating
            disabled={action === 'update' || !form.values.countryId} // ✅ Disabled when updating or no country selected
            data={divisionList?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
          />
          <TextInput
            withAsterisk
            className="col-span-2 sm:col-span-3"
            label="District Name"
            placeholder="district name"
            {...form.getInputProps('name')}
          />
          {/* <TextInput
            withAsterisk
            className="col-span-3"
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
