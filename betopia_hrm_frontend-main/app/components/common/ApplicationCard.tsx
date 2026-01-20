'use client';

import Badge from './Badge';

interface ApplicationCardProps {
  index: number;
  subject: string;
  description: string;
  status: 'Pending' | 'In review' | 'Approved' | 'Default';
  date: string;
}

const statusVariantMap: Record<
  ApplicationCardProps['status'],
  'info' | 'warning' | 'success' | 'default'
> = {
  Pending: 'info',
  'In review': 'warning',
  Approved: 'success',
  Default: 'default',
};

export default function ApplicationCard({
  index,
  subject,
  description,
  status,
  date,
}: ApplicationCardProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b last:border-b-0 border-gray-200 py-3 px-1">
      {/* Left part */}
      <div className="flex flex-1 gap-3 min-w-0">
        <span className="text-sm font-medium text-title-sub shrink-0">{index}.</span>
        <div className="flex-1 min-w-0">
          <h3 className="text-sm font-semibold text-title mb-1">{subject}</h3>
          <p className="text-xs text-title-sub line-clamp-1">{description}</p>
        </div>
      </div>

      {/* Right part */}
      <div className="flex items-center justify-between sm:justify-end gap-3 sm:gap-4 shrink-0">
        <Badge variant={statusVariantMap[status]}>{status}</Badge>
        <span className="text-xs text-title-sub whitespace-nowrap">{date}</span>
      </div>
    </div>
  );
}
