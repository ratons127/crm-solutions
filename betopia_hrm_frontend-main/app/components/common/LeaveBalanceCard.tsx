'use client';

import {
  CircularProgressbarWithChildren,
  buildStyles,
} from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';

interface LeaveBalanceCardProps {
  title: string;
  taken: number;
  remaining: number;
  total?: number; // optional, if you want % calculation
  color?: string;
  strokeWidth?: number;
}

export default function LeaveBalanceCard({
  title,
  taken,
  remaining,
  total,
  color = '#F69348',
  strokeWidth = 10,
}: LeaveBalanceCardProps) {
  // calculate percentage if total provided, else fallback
  const percentage = total ? Math.round((taken / total) * 100) : 0;

  return (
    <div className="bg-bg-primary shadow-sm rounded-xl p-4 flex flex-col items-center justify-center text-center space-y-4">
      {/* Title */}
      <h3 className="text-sm font-medium text-title">{title}</h3>

      {/* Circular Progress with custom content */}
      <div className="w-16 h-16 flex items-center justify-center">
        <CircularProgressbarWithChildren
          value={percentage}
          strokeWidth={strokeWidth}
          styles={buildStyles({
            pathColor: color,
            trailColor: '#E5E7EB',
          })}
        >
          <div className="flex flex-col items-center -mt-1">
            <span className="text-lg font-semibold text-title leading-none">
              {taken}
            </span>
            <span className="text-[10px] text-title-sub leading-none mt-0.5">
              Taken
            </span>
          </div>
        </CircularProgressbarWithChildren>
      </div>

      {/* Remaining */}
      <span className="text-xs text-title-sub">Remaining: {remaining}</span>
    </div>
  );
}
