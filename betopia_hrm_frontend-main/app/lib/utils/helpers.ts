import { format } from 'date-fns';

export const formatDate = (date: Date | string | number, withTime = false) =>
  format(date, withTime ? 'dd-MM-yyyy' : 'dd-MM-yyyy, hh:mm a');

// Use the browser's default locale
export const formatDateLocale = (date: Date | string | number) => {
  const userLocale = navigator.language || 'en-US';
  //   const localizedDate = new Intl.DateTimeFormat(userLocale).format(date);

  //   // Or use multiple locales as fallbacks
  return new Intl.DateTimeFormat([userLocale, 'en-US'], {
    dateStyle: 'medium',
  }).format(new Date(date));
};

export const valueWithFallback = (
  data?: string | number | undefined | null
) => {
  if (!data) {
    return 'N/A';
  }
  return String(data);
};

export function getDuration(
  fromDate: string | Date,
  toDate?: string | Date
): {
  years: number;
  months: number;
  days: number;
  formatted: string;
} {
  const startDate = new Date(fromDate);
  const now = toDate ? new Date(toDate) : new Date();

  let years = now.getFullYear() - startDate.getFullYear();
  let months = now.getMonth() - startDate.getMonth();
  let days = now.getDate() - startDate.getDate();

  // Adjust for negative days
  if (days < 0) {
    months--;
    const lastMonth = new Date(now.getFullYear(), now.getMonth(), 0);
    days += lastMonth.getDate();
  }

  // Adjust for negative months
  if (months < 0) {
    years--;
    months += 12;
  }

  // Format the duration
  const parts: string[] = [];
  if (years > 0) parts.push(`${years} ${years === 1 ? 'year' : 'years'}`);
  if (months > 0) parts.push(`${months} ${months === 1 ? 'month' : 'months'}`);
  if (days > 0) parts.push(`${days} ${days === 1 ? 'day' : 'days'}`);

  const formatted = parts.length > 0 ? parts.join(', ') : '0 days';

  return { years, months, days, formatted };
}

export const lookupItemIds = {
  gender: 1,
  religion: 2,
  maritalStatus: 3,
  nationality: 4,
  paymentType: 5,
  probationDuration: 6,
  bloodGroup: 7,
};

export const getStringFromJSONWithBlob = (data: Record<string, unknown>) => {
  return new Blob([JSON.stringify(data)], {
    type: 'application/json',
  });
};
