import { forwardRef } from "react";

interface RadioGroupProps extends React.InputHTMLAttributes<HTMLInputElement> {
    data: { label: string; value: string }[];
    name: string;
    label?: string;
    error?: string;
}

const RadioGroup = forwardRef<HTMLInputElement, RadioGroupProps>(
    ({ data, name, label, ...props }, ref) => {
        return (
            <div className="w-full">
                {label && (
                    <span className="block mb-1 text-sm font-medium text-gray-700 dark:text-gray-200">
                        {label}
                    </span>
                )}
                <div className="flex items-center gap-2">
                    {data.map((item) => (
                        <label
                            key={item.value}
                            className="flex items-center gap-2 cursor-pointer text-gray-700 dark:text-gray-200"
                        >
                            <input
                                {...props}
                                ref={ref}
                                type="radio"
                                name={name}
                                checked={props.value === item.value}
                                value={item.value}
                                className="h-4 w-4 text-blue-600 border-gray-300 focus:ring-blue-500"
                            />
                            {item.label}
                        </label>
                    ))}
                </div>

                {props.error && (
                    <p className="mt-1 text-sm text-red-500">{props.error}</p>
                )}
            </div>
        );
    }
);

RadioGroup.displayName = "RadioGroup";

export default RadioGroup;
