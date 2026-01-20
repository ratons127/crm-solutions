'use client';

interface PayslipCardProps {
  amount: string;
  bank: string;
  date: string;
  status: 'Received' | 'Pending';
}

const statusColors: Record<PayslipCardProps['status'], string> = {
  Received: 'text-green-600',
  Pending: 'text-yellow-600',
};

export default function PayslipCard({
  amount,
  bank,
  date,
  status,
}: PayslipCardProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b last:border-b-0 border-gray-200 py-3">
      {/* Amount */}
      <span className="text-base font-semibold text-title">{amount}</span>

      {/* Bank */}
      <span className="text-xs sm:text-sm bg-gray-100 text-gray-700 px-3 py-1 rounded-lg">
        {bank}
      </span>

      {/* Date */}
      <span className="text-xs text-title-sub">{date}</span>

      {/* Status */}
      <span className={`text-xs font-medium ${statusColors[status]}`}>
        {status}
      </span>
    </div>
  );
}
