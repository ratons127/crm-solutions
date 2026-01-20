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

type Props =
  | {
      action: 'create';
      onClose: () => void;
    }
  | {
      action: 'update';
      onClose: () => void;
      data: any;
    };

export default function PostOfficeForm(props: Props) {
  const form = useForm<any>({
    initialValues:
      props?.action === 'create'
        ? {
            countryId: '',
            // geoHash: '',
            name: '',
            parentId: '',
            status: true,
            type: 'POST_OFFICE',
          }
        : {
            countryId: props.data.countryId?.toString() || props.data.country?.id?.toString(),
            // geoHash: props.data.geoHash,
            name: props.data.name,
            parentId: props.data.parentId?.toString() ?? null,
            status: props.data.status,
            type: 'POST_OFFICE',
          },
    validate: {
      countryId: value => props.action === 'create' && !value && 'Country is required',
      parentId: value => props.action === 'create' && !value && 'District is required',
      name: value => !value && 'Name is required',
      // geoHash: value => !value && 'Geo Hash is required.',
    },
  });

  const { data: countryListData } = useGetCountryListQuery({});
  const { data: locationData } = useGetLocationListQuery({});

  const districtList =
    locationData?.data?.filter(x => {
      return (
        x.countryId?.toString() === form.values.countryId &&
        x.type === 'DISTRICT'
      );
    }) ?? [];

  const [createAsync] = useCreateLocationMutation();
  const [updateAsync] = useUpdateLocationMutation();
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
        message: `Post office ${props.action === 'create' ? 'created' : 'updated'} successfully`,
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
          <Select
            withAsterisk={props.action === 'create'}
            className="col-span-2 sm:col-span-3"
            label="Country"
            placeholder="Select country"
            {...form.getInputProps('countryId')}
            searchable
            readOnly={props.action === 'update'}
            disabled={props.action === 'update'}
            data={countryListData?.data?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
          />
          <Select
            withAsterisk={props.action === 'create'}
            className="col-span-2 sm:col-span-3"
            label="District"
            placeholder="Select district"
            searchable
            key={form.values.countryId}
            {...form.getInputProps('parentId')}
            readOnly={props.action === 'update'}
            disabled={props.action === 'update' || !form.values.countryId}
            data={districtList?.map(x => ({
              label: x.name,
              value: x.id.toString(),
            }))}
          />
          <TextInput
            withAsterisk
            className="col-span-2 sm:col-span-3"
            label="Post Office Name"
            placeholder="post office name"
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
          onCancel={props.onClose}
          confirmText={props.action === 'create' ? 'Create' : 'Update'}
          //   onConfirm={form.onSubmit(handleSubmit)}
        />
      </div>
    </form>
  );
}
