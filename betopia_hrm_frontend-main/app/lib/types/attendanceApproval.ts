// src/lib/types/attendanceApproval.ts

export type ManualApprovalStatus =
  | 'PENDING'
  | 'APPROVED'
  | 'REJECTED'
  | 'CANCELLED';

export interface AttendanceApproval {
  id: number;
  employeeId: number;
  employeeName: string;
  employeeSerialId: number;
  manualAttendanceId: number;
  date: string; // ISO date string
  inTime: string; // ISO datetime string
  outTime: string; // ISO datetime string
  manualApprovalStatus: ManualApprovalStatus;
  adjustmentType: string;
  reason: string;
  approverId: number;
  submittedBy: string;
  submittedAt: string; // ISO datetime string
}

export interface PaginatedResponse<T> {
  data: T[];
  meta: {
    total: number;
    page: number;
    perPage: number;
    lastPage: number;
  };
}

// Bulk update payload
export interface AttendanceApprovalStatusBulkItem {
  id: number;
  manualApprovalStatus: ManualApprovalStatus;
  reason: string;
}
export type AttendanceApprovalStatusBulkRequest =
  AttendanceApprovalStatusBulkItem[];
