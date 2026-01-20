'use client';

import { CheckIcon, ArrowRightIcon } from '@heroicons/react/24/outline';

interface CheckInRecord {
  date: string;
  checkIn: string;
  checkOut?: string; // optional in case missing
  duration?: string;
}

interface CheckInCardProps {
  title?: string;
  records: CheckInRecord[];
}

export default function CheckInCard({
  title = 'Check in',
  records,
}: CheckInCardProps) {
  return (
    <div className="bg-bg-primary shadow-sm rounded-xl p-4">
      {/* Header */}
      <div className="grid grid-cols-2 text-sm font-semibold text-title mb-4">
        <span className="text-green-600">{title}</span>
        <span className="text-right">Check out</span>
      </div>

      {/* Records */}
      <div className="space-y-4  overflow-y-auto pr-1">
        {records.map((rec, idx) => (
          <div
            key={idx}
            className="grid grid-cols-2 text-sm items-start border-b border-gray-100 pb-2 last:border-0"
          >
            {/* Left side (Check In) */}
            <div className="flex flex-col space-y-1">
              <span className="text-xs text-title-sub">{rec.date}</span>
              <div className="flex items-center gap-1 text-title">
                <CheckIcon className="h-4 w-4 text-green-500" />
                <span>{rec.checkIn}</span>
              </div>
            </div>

            {/* Right side (Check Out + Duration) */}
            <div className="flex flex-col items-end space-y-1">
              {rec.duration && (
                <span className="bg-gray-100 text-gray-700 text-xs px-2 py-0.5 rounded-full">
                  {rec.duration}
                </span>
              )}
              <div className="flex items-center gap-1 text-title">
                <ArrowRightIcon className="h-4 w-4 text-title-sub" />
                <span>{rec.checkOut || '--'}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
