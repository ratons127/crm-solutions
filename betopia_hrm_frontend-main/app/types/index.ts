export interface LeaveBalance {
  title: string;
  taken: number;
  remaining: number;
  total: number;
}

export interface CheckInRecord {
  date: string;
  checkIn: string;
  checkOut: string;
  duration: string;
}

export type ApplicationStatus =
  | 'Pending'
  | 'In review'
  | 'Approved'
  | 'Rejected';

export interface Application {
  subject: string;
  description: string;
  status: ApplicationStatus;
  date: string;
}

export type PayslipStatus = 'Received' | 'Pending' | 'Failed';

export interface Payslip {
  amount: string;
  bank: string;
  date: string;
  status: PayslipStatus;
}

export type AttendanceStatus = 'present' | 'absent' | 'leave' | 'off' | 'late';

export interface AttendanceMap {
  [date: string]: AttendanceStatus;
}

export interface AttendanceRecord {
  date: string;
  status: AttendanceStatus;
}

export interface WorkCardProps {
  icon?: React.ReactNode;
  label: string;
  title?: string;
  value: string | number;
  iconBg?: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: string;
}

export interface AuthTokens {
  token: string;
  refreshToken: string;
}

export interface ApiResponse<T = any> {
  data: T;
  message?: string;
  success: boolean;
}

