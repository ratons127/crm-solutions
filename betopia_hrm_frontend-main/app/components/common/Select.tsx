import { forwardRef } from 'react';

interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  data: { label: string; value: string }[];
  label?: string;
  error?: string;
}

const Select = forwardRef<HTMLSelectElement, SelectProps>(
  ({ data, ...props }, ref) => {
    return (
      <div className="w-full">
        {props.label && (
          <label
            htmlFor={props.name}
            className="block mb-1 text-sm font-medium text-gray-700 dark:text-gray-200"
          >
            {props.label}
          </label>
        )}
        <select
          {...props}
          ref={ref}
          className="block outline-none p-3 rounded-sm border border-gray-400/30 w-full"
        >
          {data?.map(item => (
            <option key={item.value} value={item.value}>
              {item.label}
            </option>
          ))}
          {data?.length === 0 && (
            <option className="" disabled>
              No option available
            </option>
          )}
        </select>
        {props.error && (
          <p className="mt-1 text-sm text-red-500">{props.error}</p>
        )}
      </div>
    );
  }
);

Select.displayName = 'Select';

export default Select;
