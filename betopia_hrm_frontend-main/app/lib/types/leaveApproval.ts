import { DBModel, PaginatedResponse } from '.';

/* ---------- ENUM ---------- */
export type LeaveStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';

/* ---------- ENTITY ---------- */
export interface LeaveApproval extends DBModel {
  leaveRequestId: number;
  approverId: number;
  level: number;
  leaveStatus: LeaveStatus;
  remarks: string;
  actionDate: string;
  leaveApprovalId: number;
}

/* ---------- CREATE / UPDATE REQUEST ---------- */
export interface LeaveApprovalRequest {
  leaveRequestId: number;
  approverId: number;
  level: number;
  leaveStatus: LeaveStatus;
  remarks: string;
  actionDate: string;
  leaveApprovalId?: number;
}

/* ---------- PARTIAL UPDATE (PATCH) ---------- */
export interface LeaveApprovalStatusUpdateRequest {
  leaveStatus: LeaveStatus;
  remarks: string;
}

/* ---------- BULK STATUS UPDATE (PATCH /status) ---------- */
export interface LeaveApprovalStatusBulkItem {
  id: number;
  leaveStatus: LeaveStatus;
  remarks: string;
}
export type LeaveApprovalStatusBulkRequest = LeaveApprovalStatusBulkItem[];

/* ---------- PUT /update/{id} ---------- */
export interface LeaveApprovalUpdateRequest extends LeaveApprovalRequest {}

/* ---------- PAGINATION RESPONSE ---------- */
export type LeaveApprovalPaginatedResponse =
  PaginatedResponse<LeaveApproval>;
