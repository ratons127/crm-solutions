'use client';

import { ChangeEvent, FocusEvent } from 'react';

interface TextInputProps {
  label?: string;
  type?: string;
  name: string;
  value: string | number;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
  onBlur?: (e: FocusEvent<HTMLInputElement>) => void;
  placeholder?: string;
  required?: boolean;
  className?: string;
  disabled?: boolean;
  autoComplete?: string;
  placeholderColor?: string;
  error?: string;
}

export default function TextInput({
  label,
  type = 'text',
  name,
  value,
  onChange,
  onBlur,
  placeholder = '',
  required = false,
  className = '',
  disabled = false,
  autoComplete = 'off',
  placeholderColor = 'placeholder-gray-300',
  error = '',
}: TextInputProps) {
  return (
    <div className="w-full">
      {label && (
        <label
          htmlFor={name}
          className="block mb-1 text-sm font-medium text-gray-700 dark:text-gray-200"
        >
          {label}
        </label>
      )}
      <input
        type={type}
        name={name}
        id={name}
        value={value}
        onChange={onChange}
        onBlur={onBlur}
        placeholder={placeholder}
        required={required}
        disabled={disabled}
        autoComplete={autoComplete}
        className={`w-full border p-3 rounded focus:outline-none focus:ring-2 disabled:opacity-50 transition
          ${error ? 'border-red-500 focus:ring-red-400' : 'border-gray-300 focus:ring-blue-400'}
          ${placeholderColor} ${className}`}
      />
      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  );
}
