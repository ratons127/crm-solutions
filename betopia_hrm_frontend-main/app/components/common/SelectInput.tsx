'use client';

import React, { useContext } from 'react';
import { FormikContext, FormikContextType } from 'formik';

export type SelectOption = { value: string | number; label: string };

type BaseProps = {
  label?: string;
  name: string;
  options: SelectOption[];
  placeholder?: string;
  disabled?: boolean;
  required?: boolean;
  className?: string;
  isLoading?: boolean;
  isClearable?: boolean;
  /** Extra callback after value set (useful for cascades) */
  onValueChange?: (value: string) => void;
};

/**
 * Formik-aware Select input. If used outside Formik, you can still pass `value` and `onChange`.
 */
export default function SelectInput({
  label,
  name,
  options,
  placeholder = 'Select…',
  disabled,
  required,
  className = '',
  isLoading = false,
  isClearable = false,
  onValueChange,
  ...rest
}: BaseProps & React.SelectHTMLAttributes<HTMLSelectElement>) {
  // Safe Formik access
  const formik = useContext<FormikContextType<any> | undefined>(
    FormikContext as any
  );
  const inFormik = !!formik;

  const fieldProps = inFormik ? formik!.getFieldProps(name) : null;
  const meta = inFormik ? formik!.getFieldMeta(name) : null;

  const value = inFormik
    ? (fieldProps!.value ?? '')
    : ((rest.value as any) ?? '');
  const error =
    inFormik && meta?.touched && meta?.error ? String(meta.error) : '';

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    if (inFormik) fieldProps!.onChange(e);
    if (rest.onChange) rest.onChange(e);
    if (onValueChange) onValueChange(e.target.value);
  };

  const handleBlur = (e: React.FocusEvent<HTMLSelectElement>) => {
    if (inFormik) fieldProps!.onBlur(e);
    if (rest.onBlur) rest.onBlur(e);
  };

  const handleClear = () => {
    if (inFormik) formik!.setFieldValue(name, '');
    if (onValueChange) onValueChange('');
  };

  return (
    <div className={`flex flex-col ${className}`}>
      {label && (
        <label
          htmlFor={name}
          className="mb-1 text-sm font-medium text-gray-700"
        >
          {label} {required ? <span className="text-red-600">*</span> : null}
        </label>
      )}
      <div className="relative">
        <select
          id={name}
          name={name}
          disabled={disabled || isLoading}
          className={`w-full rounded-md border text-sm text-gray-500 px-3 py-1.5 bg-white pr-9
            ${disabled ? 'opacity-60' : ''}
            ${error ? 'border-red-500' : 'border-gray-300'}`}
          value={value}
          onChange={handleChange}
          onBlur={handleBlur}
        >
          <option value="">{placeholder}</option>
          {options.map(opt => (
            <option key={opt.value} value={String(opt.value)}>
              {opt.label}
            </option>
          ))}
        </select>

        {isClearable && value !== '' && (
          <button
            type="button"
            className="absolute inset-y-0 right-5 my-auto text-gray-400 text-sm hover:text-gray-600"
            onClick={handleClear}
            aria-label="Clear selection"
          >
            ✕
          </button>
        )}
      </div>

      {isLoading && <div className="mt-1 text-xs text-gray-500">Loading…</div>}
      {error && <div className="mt-1 text-xs text-red-600">{error}</div>}
    </div>
  );
}
