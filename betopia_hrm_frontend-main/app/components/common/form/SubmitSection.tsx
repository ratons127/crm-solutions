import { Button } from '@mantine/core';

interface Props {
  onCancel?: () => void;
  onConfirm?: () => void;
  cancelText?: string;
  confirmText?: string;
  isLoading?: boolean;
}

export default function SubmitSection({
  cancelText = 'Cancel',
  confirmText = 'Submit',
  onCancel = () => {},
  onConfirm = () => {},
  isLoading = false,
}: Props) {
  return (
    <div className=" flex items-center  gap-5 justify-end mt-5">
      <Button variant="outline" onClick={onCancel} color="red" type="button">
        {cancelText}
      </Button>
      <Button
        onClick={onConfirm}
        type="submit"
        disabled={isLoading}
        loading={isLoading}
        loaderProps={{ type: 'dots' }}
      >
        {confirmText}
      </Button>
    </div>
  );
}
