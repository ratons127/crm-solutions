import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

/**
 * Merge Tailwind classes with conditional support
 * Example: cn("p-2", condition && "bg-red-500")
 */
export function cn(...inputs: any[]) {
  return twMerge(clsx(inputs));
}

export const getFilteredParams = (params: Record<string, unknown>) =>
  Object.fromEntries(Object.entries(params).filter(([_, value]) => value));
