import { DBModel, PaginatedResponse } from '.';

export interface ShiftRotationPatternDetail extends DBModel {
  pattern: string;
  dayNumber: number;
  shiftId: number;
  offDay: boolean;
}

export interface ShiftRotationPattern extends DBModel {
  patternName: string;
  companyId: number;
  businessUnitId: number;
  workPlaceGroupId: number;
  workPlaceId: number;
  departmentId: number;
  teamId: number;
  description: string;
  rotationDays: number;
  status: boolean;
  shiftRotationPatternDetails: ShiftRotationPatternDetail[];
}

export interface ShiftRotationPatternRequest {
  patternName: string;
  companyId: number;
  businessUnitId: number;
  workPlaceGroupId: number;
  workPlaceId: number;
  departmentId: number;
  teamId: number;
  description: string;
  rotationDays: number;
  status: boolean;
  shiftRotationPatternDetails: ShiftRotationPatternDetail[];
}

export type ShiftRotationPatternPaginatedResponse =
  PaginatedResponse<ShiftRotationPattern>;
