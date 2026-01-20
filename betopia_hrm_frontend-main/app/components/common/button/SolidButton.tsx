'use client';

import React, { forwardRef, ReactNode, MouseEventHandler } from 'react';

const sizeClasses = {
  sm: 'px-3 py-1.5 text-xs',
  md: 'px-5 py-2.5 text-sm',
  lg: 'px-6 py-3 text-lg',
} as const;

const variantClasses = {
  solid: 'bg-blue-500 hover:bg-blue-600 text-white',
  primary: 'bg-[#F69348] hover:bg-[#f77614] text-white',
  outline:
    'border border-blue-500 text-blue-500 hover:bg-blue-500 hover:text-white',
  danger: 'bg-red-500 hover:bg-red-600 text-white',
  success: 'bg-green-500 hover:bg-green-600 text-white',
  secondary: 'bg-gray-500 hover:bg-gray-600 text-white',
  color: '', // This variant is for custom colors
} as const;

type Size = keyof typeof sizeClasses;
type Variant = keyof typeof variantClasses;
type IconPosition = 'left' | 'right';

interface SolidButtonProps {
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
  height?: string | number;
  width?: string | number;
  loaderColor?: string;
  iconPosition?: IconPosition;
  backgroundColor?: string;
  textColor?: string;
  iconColor?: string; // New prop for icon color
  gap?: string;
}

const SolidButton = forwardRef<HTMLButtonElement, SolidButtonProps>(
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
      variant = 'solid',
      size = 'md',
      height,
      width,
      loaderColor = 'text-white',
      iconPosition = 'left',
      backgroundColor,
      textColor,
      iconColor, // Destructure the new prop
      gap = 'gap-2',
    },
    ref
  ) => {
    const baseClass = `
      inline-flex items-center justify-center font-semibold rounded
      transition duration-300 ease-in-out focus:outline-none
      ${!width && fullWidth ? 'w-full' : ''}
      ${disabled || isLoading ? 'opacity-50 cursor-not-allowed' : ''}
      ${sizeClasses[size] || sizeClasses.md}
      ${variantClasses[variant] || variantClasses.solid}
      ${gap}
      ${className}
    `;

    const style = {
      ...(width && { width: typeof width === 'number' ? `${width}px` : width }),
      ...(height && {
        height: typeof height === 'number' ? `${height}px` : height,
      }),
      ...(backgroundColor && variant === 'color' && { backgroundColor }),
      ...(textColor && variant === 'color' && { color: textColor }),
    };

    const renderIcon = () => {
      if (isLoading) {
        return (
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
        );
      }
      return icon ? <span style={{ color: iconColor }}>{icon}</span> : null;
    };

    return (
      <button
        ref={ref}
        type={type}
        onClick={onClick}
        disabled={disabled || isLoading}
        className={baseClass}
        style={style}
      >
        {iconPosition === 'left' && renderIcon()}
        {children}
        {iconPosition === 'right' && renderIcon()}
      </button>
    );
  }
);

SolidButton.displayName = 'SolidButton';

export default SolidButton;
