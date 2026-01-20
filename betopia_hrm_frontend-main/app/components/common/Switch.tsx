import { forwardRef } from 'react';

interface SwitchProps {
  label?: string;
  error?: string;
  value?: boolean;
  onChange?: (checked: boolean) => void;
}

const Switch = forwardRef<HTMLInputElement, SwitchProps>(
  ({ label, error, value = false, onChange = () => {}, ...props }, ref) => {
    return (
      <div className="w-full">
        {label && (
          <label className="block mb-1 text-sm font-medium text-gray-700 dark:text-gray-200">
            {label}
          </label>
        )}

        <label className="relative inline-flex items-center cursor-pointer">
          <input
            type="checkbox"
            ref={ref}
            checked={value}
            onChange={() => {
              onChange(!value);
            }}
            className="sr-only peer"
            {...props}
          />
          {/* background */}
          <div className="w-11 h-6 bg-gray-300 rounded-full peer-checked:bg-blue-600 transition-colors"></div>
          {/* circle */}
          <div
            className={`absolute left-0.5 top-0.5 w-5 h-5 bg-white rounded-full shadow-md transition-transform duration-200 ${
              value ? 'translate-x-5' : ''
            }`}
          ></div>
        </label>

        {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
      </div>
    );
  }
);

Switch.displayName = 'Switch';

export default Switch;
