'use client';

import { WorkCardProps } from '@/types';
import clsx from 'clsx';

export interface ExtendedWorkCardProps extends WorkCardProps {
  iconGradient?: string;     // optional gradient background class for icon
  bgGradient?: string;       // optional gradient for the whole card
  labelClass?: string;       // optional override for label color/classes
  valueClass?: string;       // optional override for value color/classes
  titleClass?: string;       // optional override for title color/classes
  borderClass?: string;      // optional border class for the card
  imageUrl?: string;         // ✅ new image support
  imageSize?: number;        // optional: customize image size
  imageRounded?: boolean;    // optional: round or square image
}

export default function WorkCard({
  icon,
  label,
  value,
  title,
  iconBg = 'bg-orange-400',
  iconGradient,
  bgGradient,
  labelClass,
  valueClass,
  titleClass,
  borderClass,
  imageUrl,
  imageSize = 30,
  imageRounded = true,
}: ExtendedWorkCardProps) {
  // Icon background (gradient overrides solid)
  const iconClass = clsx(
    'flex items-center justify-center w-8 h-8 rounded-lg text-white',
    iconGradient ? iconGradient : iconBg
  );

  // Card background (gradient overrides white)
  const cardClass = clsx(
    'shadow-sm rounded-xl p-4 flex items-center space-x-4 min-h-[50px]',
    bgGradient ? bgGradient : 'bg-white',
    borderClass
  );

  // If gradient is present, default to light text; allow explicit overrides
  const isGradient = Boolean(bgGradient);

  const labelTextClass = clsx(
    'text-xs',
    labelClass ?? (isGradient ? 'text-white/80' : 'text-title-sub')
  );

  const valueTextClass = clsx(
    'text-sm font-semibold',
    valueClass ?? (isGradient ? 'text-white' : 'text-title')
  );

  const titleTextClass = clsx(
    'text-xs',
    titleClass ?? (isGradient ? 'text-white/80' : 'text-title-sub')
  );

  return (
    <div className={cardClass}>
      {/* ✅ Image OR Icon */}
      {imageUrl && imageUrl.trim() !== '' ? (
        <div
          className={clsx(
            'overflow-hidden flex-shrink-0',
            imageRounded ? 'rounded-full' : 'rounded-md',
            'border border-white/30 shadow-sm',
          )}
          style={{ width: imageSize, height: imageSize }}
        >
          <img
            src={imageUrl}
            alt="work-card-img"
            className="w-full h-full object-cover"
            onError={(e) => {
              e.currentTarget.style.display = 'none';
            }}
          />
        </div>
      ) : (
        <div className={iconClass}>{icon}</div>
      )}

      {/* Text */}
      <div className="flex flex-col justify-center">
        <span className={labelTextClass}>{label}</span>
        <span className={valueTextClass}>{value}</span>
        {title && <span className={titleTextClass}>{title}</span>}
      </div>
    </div>
  );
}
