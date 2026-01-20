'use client';

import { forwardRef, MouseEventHandler, ReactNode } from 'react';

const sizeClasses = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-5 py-2.5 text-base',
  lg: 'px-6 py-3 text-lg',
} as const;

const variantClasses = {
  gradient:
    'bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white',
  outline:
    'border border-blue-500 text-blue-500 hover:bg-blue-500 hover:text-white',
  danger: 'bg-red-500 hover:bg-red-600 text-white',
  success: 'bg-green-500 hover:bg-green-600 text-white',
  secondary: 'bg-gray-500 hover:bg-gray-600 text-white',
  primary: `bg-gradient-to-t from-slate-900 to-slate-700 text-white`,
} as const;

type Size = keyof typeof sizeClasses;
type Variant = keyof typeof variantClasses;

interface GradientButtonProps {
  type?: 'button' | 'submit' | 'reset';
  onClick?: MouseEventHandler<HTMLButtonElement>;
  children: ReactNode;
  isLoading?: boolean;
  disabled?: boolean;
  fullWidth?: boolean;
  icon?: ReactNode | null;
  className?: string;
  variant?: Variant;
  size?: Size;
  loaderColor?: string;
}

const GradientButton = forwardRef<HTMLButtonElement, GradientButtonProps>(
  (
    {
      type = 'button',
      onClick,
      children,
      isLoading = false,
      disabled = false,
      fullWidth = true,
      icon = null,
      className = '',
      variant = 'gradient',
      size = 'md',
      loaderColor = 'text-white',
    },
    ref
  ) => {
    const baseClass = `
      inline-flex items-center justify-center gap-2 font-semibold rounded
      transition duration-300 ease-in-out focus:outline-none
      ${fullWidth ? 'w-full' : ''}
      ${disabled || isLoading ? 'opacity-50 cursor-not-allowed' : ''}
      ${sizeClasses[size] || sizeClasses.md}
      ${variantClasses[variant] || variantClasses.gradient}
      ${className}
    `;

    return (
      <button
        ref={ref}
        type={type}
        onClick={onClick}
        disabled={disabled || isLoading}
        className={baseClass}
      >
        {isLoading && (
          <svg
            className={`animate-spin h-5 w-5 ${loaderColor}`}
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            />
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
            />
          </svg>
        )}

        {icon && !isLoading && <span>{icon}</span>}

        {children}
      </button>
    );
  }
);

GradientButton.displayName = 'GradientButton';

export default GradientButton;
