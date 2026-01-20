'use client';

import React, { ChangeEvent, FocusEvent, useContext, useId } from 'react';
import { FormikContext, FormikContextType } from 'formik';

type CommonProps = {
  label?: string;
  name: string;
  placeholder?: string;
  required?: boolean;
  className?: string;
  disabled?: boolean;
  autoComplete?: string;
  placeholderColor?: string;
  error?: string; // external error (when not in Formik)
  as?: 'input' | 'textarea';
  rows?: number;
};

type InputProps = {
  type?: string;
  value?: string | number;
  onChange?: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  onBlur?: (e: FocusEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
};

export type FloatingInputProps = CommonProps & InputProps;

export default function FloatingInput({
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
  error,
  as = 'input',
  rows = 3,
}: FloatingInputProps) {
  const reactId = useId();
  const id = name || reactId;

  // Safe Formik access (undefined if used outside Formik)
  const formik = useContext<FormikContextType<any> | undefined>(
    FormikContext as any
  );
  const inFormik = !!formik;

  const fieldProps = inFormik ? formik!.getFieldProps(name) : null;
  const meta = inFormik ? formik!.getFieldMeta(name) : null;

  const currentValue = inFormik ? (fieldProps!.value ?? '') : (value ?? '');
  const hasValue =
    currentValue !== undefined &&
    currentValue !== null &&
    String(currentValue).length > 0;

  const currentError =
    (inFormik && meta?.touched && meta.error ? String(meta.error) : '') ||
    (!inFormik && error ? String(error) : '');

  const floatingEnabled = Boolean(label);

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    if (inFormik) fieldProps!.onChange(e as any);
    if (onChange) onChange(e);
  };

  const handleBlur = (
    e: FocusEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    if (inFormik) fieldProps!.onBlur(e as any);
    if (onBlur) onBlur(e);
  };

  const baseClasses = [
    'peer w-full rounded-lg border bg-white dark:bg-gray-900 text-sm leading-6 outline-none transition',
    'px-3 py-1',
    disabled
      ? 'border-gray-200 text-gray-400 cursor-not-allowed'
      : currentError
        ? 'border-red-500 focus:border-red-500 focus:ring-2 focus:ring-red-400/30'
        : 'border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20',
    floatingEnabled ? 'placeholder-transparent' : placeholderColor,
  ].join(' ');

  const labelClasses = [
    'pointer-events-none absolute left-3 px-1 bg-white dark:bg-gray-900 transition-all duration-150',
    !hasValue ? 'top-1/2 -translate-y-1/2 text-sm text-gray-500' : '',
    hasValue ? '-top-2 translate-y-0 text-xs text-gray-600' : '',
    'peer-focus:-top-2 peer-focus:translate-y-0 peer-focus:text-xs',
    currentError ? 'peer-focus:text-red-600' : 'peer-focus:text-blue-600',
    'peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-sm peer-placeholder-shown:text-gray-500',
  ].join(' ');

  const sharedProps = {
    id,
    name,
    disabled,
    required,
    autoComplete,
    'aria-invalid': !!currentError,
    'aria-describedby': currentError ? `${id}-error` : undefined,
    className: baseClasses,
    value: currentValue as any,
    onChange: handleChange,
    onBlur: handleBlur,
    placeholder: floatingEnabled ? ' ' : placeholder,
  };

  return (
    <div className={`w-full ${className}`}>
      {!floatingEnabled && label && (
        <label
          htmlFor={id}
          className="block mb-1 text-sm font-medium text-gray-700 dark:text-gray-200"
        >
          {label}
          {required ? ' *' : ''}
        </label>
      )}

      <div className="relative">
        {as === 'textarea' ? (
          <textarea rows={rows} {...sharedProps} />
        ) : (
          <input type={type} {...sharedProps} />
        )}

        {floatingEnabled && (
          <label htmlFor={id} className={labelClasses}>
            {label}
            {required ? ' *' : ''}
          </label>
        )}
      </div>

      {currentError && (
        <p id={`${id}-error`} className="mt-1 text-sm text-red-600">
          {currentError}
        </p>
      )}
    </div>
  );
}
