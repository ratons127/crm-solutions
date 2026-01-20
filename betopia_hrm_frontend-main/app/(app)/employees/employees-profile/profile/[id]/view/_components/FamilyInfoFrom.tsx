'use client';
import React from 'react';
const BADGE_VARIANTS = ['danger', 'warning', 'info', 'success', 'neutral'] as const;
type BadgeVariant = typeof BADGE_VARIANTS[number];
type LabelValue = { label: string; value?: React.ReactNode };
export type ContactInfoRowProps = {
  items: [LabelValue, ...LabelValue[]];
  badge?: {
    text: string;
    icon?: React.ReactNode;
    title?: string;
    variant?: BadgeVariant;
  };
  rightSlot?: React.ReactNode;
  size?: 'sm' | 'md';
  interactive?: boolean;
  onClick?: () => void;
  className?: string;
  contentClassName?: string;
};
/** Map of variant → Tailwind classes */
const badgeTone: Record<BadgeVariant, string> = {
  danger:  'bg-red-100 text-red-700 ring-1 ring-red-200',
  warning: 'bg-amber-100 text-amber-800 ring-1 ring-amber-200',
  info:    'bg-blue-100 text-blue-700 ring-1 ring-blue-200',
  success: 'bg-emerald-100 text-emerald-700 ring-1 ring-emerald-200',
  neutral: 'bg-gray-100 text-gray-700 ring-1 ring-gray-200',
};
export default function FamilyInfoFrom({
  items,
  badge,
  rightSlot,
  size = 'md',
  interactive = false,
  onClick,
  className = '',
  contentClassName = '',
}: ContactInfoRowProps) {
  const pad = size === 'sm' ? 'py-3 px-4' : 'py-4 px-5';
  const labelText = size === 'sm' ? 'text-xs' : 'text-sm';
  const valueText = size === 'sm' ? 'text-sm' : 'text-base';
  const baseCard =
    'w-full rounded-xl bg-gray-50 dark:bg-zinc-900/50 ring-1 ring-gray-200 dark:ring-zinc-800 transition-shadow';
  const honorable = interactive ? 'hover:shadow-sm cursor-pointer' : '';
  const variant: BadgeVariant = badge?.variant ?? 'danger';
  return (
    <div onClick={onClick} className={`${baseCard} ${pad} ${honorable} ${className}`}>
      <div className={`flex items-center gap-3 ${contentClassName}`}>
        {/* Labeled values */}
        <div className="grid w-full gap-x-8 gap-y-2 md:grid-cols-3">
          {items.map((it, idx) => (
            <div key={idx} className="min-w-0">
              <div className={`${labelText} text-gray-500 dark:text-zinc-400`}>{it.label}:</div>
              <div className={`${valueText} truncate font-medium text-gray-900 dark:text-zinc-100`}>
                {it.value ?? '—'}
              </div>
            </div>
          ))}
        </div>
        {/* Right side */}
        <div className="ml-auto flex shrink-0 items-center gap-3">
          {badge?.text && (
            <span
              title={badge.title}
              className={`inline-flex items-center gap-1 rounded-full px-2.5 py-1 text-xs font-medium ${badgeTone[variant]}`}
            >
              {badge.icon}
              {badge.text}
            </span>
          )}
          {rightSlot}
        </div>
      </div>
    </div>
  );
}