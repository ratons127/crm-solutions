import { DBModel, PaginatedResponse } from './index';
import { LeaveGroupAssign, LeaveType } from './leave';

export interface LeaveRequest extends DBModel {
  employeeId: number;
  leaveTypeId: number;
  leaveGroupAssignId: number;
  startDate: string;
  endDate: string;
  daysRequested: number;
  reason: string;
  proofDocumentPath?: string | null;
  justification?: string | null;
  hasProof: boolean;
  status: string;
  requestedAt?: string | null;
  approvedBy?: number | null;
  approvedAt?: string | null;
  leaveType?: LeaveType;
  leaveGroupAssign?: LeaveGroupAssign;
}

export interface LeaveRequestValues {
  employeeId: string;
  // leaveGroupAssignId: string;
  leaveTypeId: string;
  startDate: Date | null;
  endDate: Date | null;
  daysRequested: number | string;
  reason: string;
  justification?: string;
  files?: File | null;
}

export type LeaveRequestPaginatedResponse = PaginatedResponse<LeaveRequest>;
