'use client';

import * as React from 'react';
import { cva, VariantProps } from 'class-variance-authority';
import { cn } from '../../lib/utils/utils';

const badgeVariants = cva(
  'inline-flex items-center rounded-full px-3 py-1 text-xs font-medium transition',
  {
    variants: {
      variant: {
        info: 'bg-purple-100 text-purple-600 hover:bg-purple-200',
        warning: 'bg-orange-100 text-orange-600 hover:bg-orange-200',
        success: 'bg-green-100 text-green-600 hover:bg-green-200',
        default: 'bg-gray-100 text-gray-600 hover:bg-gray-200',
      },
    },
    defaultVariants: {
      variant: 'default',
    },
  }
);

export interface BadgeProps
  extends React.HTMLAttributes<HTMLElement>,
    VariantProps<typeof badgeVariants> {
  as?: React.ElementType; // ✅ valid JSX element type
}

export default function Badge({
  className,
  variant,
  as: Component = 'span',
  ...props
}: BadgeProps) {
  return (
    <Component
      className={cn(badgeVariants({ variant }), className)}
      {...props}
    />
  );
}
