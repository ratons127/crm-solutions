'use client';

import React from 'react';
import { ImSpinner10 } from 'react-icons/im';

interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
  text?: string;
}

const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  size = 'md',
  className = '',
  text,
}) => {
  const sizeClasses = {
    sm: 'w-4 h-4 text-base',
    md: 'w-6 h-6 text-xl',
    lg: 'w-8 h-8 text-2xl',
    xl: 'w-12 h-12 text-4xl',
  };

  return (
    <div
      className={`flex flex-col items-center justify-center gap-2 ${className}`}
    >
      <ImSpinner10
        className={`${sizeClasses[size]} text-primary animate-spin`}
      />
      {text && <span className="text-sm text-gray-600">{text}</span>}
    </div>
  );
};

export default LoadingSpinner;
