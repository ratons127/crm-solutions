import { DBModel, PaginatedResponse, Simplify } from './index';

export type AttendancePolicyBase = {
  companyId: number;
  effectiveFrom: string; // YYYY-MM-DD
  effectiveTo: string;   // YYYY-MM-DD
  graceInMinutes: number;
  graceOutMinutes: number;
  lateThresholdMinutes: number;
  earlyLeaveThresholdMinutes: number;
  minWorkMinutes: number;
  halfDayThresholdMinutes: number;
  absenceThresholdMinutes: number;
  movementEnabled: boolean;
  movementAllowMinutes: number;
  notes: string;
  status: boolean;
};

export type AttendancePolicy = Simplify<DBModel & AttendancePolicyBase>;
export type AttendancePolicyCreate = Simplify<AttendancePolicyBase>;
export type AttendancePolicyUpdate = Simplify<Partial<AttendancePolicyCreate>>;

export type AttendancePolicyPaginatedResponse = PaginatedResponse<AttendancePolicy>;

