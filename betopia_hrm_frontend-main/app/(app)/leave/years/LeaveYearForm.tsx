import SubmitSection from '@/components/common/form/SubmitSection';
import { DatePickerInput } from '@mantine/dates';
import { useForm } from '@mantine/form';
import {
  useCreateLeaveYearMutation,
  useUpdateLeaveYearMutation,
} from '../../../lib/features/leave/leaveYear/leaveYearRTKAPI';
import { LeaveYearType } from '../../../lib/types/leave';
import { notifications } from '@mantine/notifications';

type LeaveYearFormProps =
  | {
      action: 'edit';
      data: LeaveYearType;
      onClose: () => void;
    }
  | {
      action: 'create';
      onClose: () => void;
    };

export default function LeaveYearForm(props: LeaveYearFormProps) {
  const [createLeaveYear] = useCreateLeaveYearMutation();
  const [updateLeaveYear] = useUpdateLeaveYearMutation();

  const form = useForm({
    initialValues:
      props.action === 'edit'
        ? {
            startDate: new Date(props.data.startDate),
            endDate: new Date(props.data.endDate),
          }
        : {
            startDate: new Date(),
            endDate: new Date(),
          },
    validate: {
      startDate: value => (!value ? 'Start date is required!' : null),
      endDate: (value, values) => {
        if (!value) return 'End date is required!';
        if (values.startDate && value < values.startDate) {
          return 'End date must be after start date!';
        }
        return null;
      },
    },
  });

  const handleSubmit = async (values: typeof form.values) => {
    const sanitizedData = {
      startDate: values.startDate,
      endDate: values.endDate,
    };

    try {
      if (props.action === 'create') {
        await createLeaveYear(sanitizedData).unwrap();
        notifications.show({
          title: 'Create',
          message: 'Leave year created successfully',
        });
      } else {
        await updateLeaveYear({
          id: props.data.id,
          data: sanitizedData,
        }).unwrap();
        notifications.show({
          title: 'Update',
          message: 'Leave year updated successfully',
        });
      }
      props.onClose();
    } catch (error) {
      notifications.show({
        title: props.action === 'create' ? 'Create' : 'Update',
        message: `Leave year ${props.action === 'create' ? 'creation' : 'update'} failed`,
        color: 'red',
      });
    }
  };

  return (
    <div>
      <form onSubmit={form.onSubmit(handleSubmit)} className="space-y-3">
        <h1 className="text-lg font-semibold">
          {props.action === 'create' ? 'Create' : 'Update'} Leave Year
        </h1>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <DatePickerInput
            label="From Date"
            placeholder="YYYY-MM-DD"
            value={form.values.startDate}
            onChange={form.getInputProps('startDate').onChange}
            error={form.errors.startDate}
          />

          <DatePickerInput
            label="End Date"
            placeholder="YYYY-MM-DD"
            value={form.values.endDate}
            onChange={form.getInputProps('endDate').onChange}
            error={form.errors.endDate}
            minDate={form.values.startDate || undefined}
          />
        </div>

        <SubmitSection
          onCancel={props.onClose}
          confirmText={props.action === 'create' ? 'Create' : 'Update'}
          cancelText="Cancel"
        />
      </form>
    </div>
  );
}
