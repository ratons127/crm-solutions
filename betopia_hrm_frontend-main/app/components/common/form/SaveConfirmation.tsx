import { Button } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useState } from 'react';
import { BsCheck2Circle } from 'react-icons/bs';

export default function SaveConfirmation(props: {
  onSubmit: () => Promise<void>;
}) {
  const [isSaving, setIsSaving] = useState(false);

  return (
    <div className="flex w-full items-center justify-end bg-[#EEE] gap-5 rounded-lg shadow-md py-5 px-5">
      <Button
        variant="transparent"
        className="text-[#45556C]! border border-[#CAD5E2]!"
        onClick={() => {
          notifications.show({
            title: 'Cancelled',
            message: 'Profile editing cancelled.',
            color: 'gray',
          });
          // router.back();
        }}
      >
        Cancel
      </Button>

      <Button
        variant="filled"
        radius="md"
        type="submit"
        leftSection={<BsCheck2Circle size={14} />}
        loading={isSaving}
        onClick={async () => {
          setIsSaving(true);
          props
            .onSubmit()
            .then(() => {
              notifications.show({
                title: 'Profile Saved Successfully',
                message: 'Employee profile has been updated successfully!',
                color: 'green',
                icon: <BsCheck2Circle size={18} />,
              });
              // setTimeout(() => {
              //   router.back(); // <-- change this path to your actual profile page route
              // }, 1200);
            })
            .finally(() => setIsSaving(false))
            .catch(e => {
              notifications.show({
                title: 'Error while saving profile.',
                message: e?.data?.message ?? 'Something went wrong',
                color: 'red',
              });
            });
        }}
      >
        Save Employee Profile
      </Button>
    </div>
  );
}
