import { EllipsisHorizontalIcon } from '@heroicons/react/24/solid';
import { useState } from 'react';

export default function ActionsDropdown<T>({
  row,
  ...props
}: {
  row: any;
  onPressDelete?: () => void;
  onPressEdit?: (item: T) => void;
}) {
  const [open, setOpen] = useState(false);

  return (
    <div className="relative">
      <button
        onClick={() => setOpen(p => !p)}
        className="flex items-center gap-1 px-2 py-1 text-gray-400 hover:bg-gray-100"
      >
        <EllipsisHorizontalIcon className="h-4 w-4" />
      </button>
      {open && (
        <div className="absolute right-0 mt-1 w-32 bg-white border border-gray-200 rounded-md shadow-lg z-10">
          {!!props.onPressEdit && (
            <button
              onClick={() => {
                setOpen(false);
                props?.onPressEdit?.(row.original);
              }}
              className="block w-full text-left px-3 py-2 text-sm hover:bg-gray-100"
            >
              Edit
            </button>
          )}
          {!!props?.onPressDelete && (
            <button
              onClick={() => {
                props.onPressDelete?.();
                setOpen(false);
              }}
              className="block w-full text-left px-3 py-2 text-sm hover:bg-red-50 text-red-600"
            >
              Delete
            </button>
          )}
        </div>
      )}
    </div>
  );
}
