import { DBModel, PaginatedResponse } from '.';

export interface WeeklyOff {
  id?: number;
  dayOfWeek: string;
}

export interface ShiftDefinition extends DBModel {
  shiftCategory: {
    id: number;
    name: string;
    type: string;
  };
  shiftName: string;
  shiftCode: string;
  company: {
    id: number;
    name: string;
    slug: string;
    shortName: string;
    code: string;
  };
  businessUnit: {
    id: number;
    name: string;
    code: string;
    companyId: number;
    companyName: string;
  };
  workPlaceGroup: {
    id: number;
    name: string;
    code: string;
    businessUnitId: number;
  };
  workPlace: {
    id: number;
    name: string;
    code: string;
    address: string;
    workplaceGroupId: number;
  };
  department: {
    id: number;
    name: string;
    code: string;
    description: string;
    workplaceId: number;
  };
  team: {
    id: number;
    name: string;
    code: string;
    description: string;
    departmentId: number;
  };
  startTime: string;
  endTime: string;
  breakMinutes: number;
  graceInMinutes: number;
  graceOutMinutes: number;
  status: boolean;
  weeklyOffs: WeeklyOff[];
  nightShift: boolean;
}

export interface ShiftDefinitionRequest {
  shiftCategoryId: number;
  shiftName: string;
  shiftCode: string;
  companyId: number;
  // businessUnitId: number;
  // workPlaceGroupId: number;
  // workPlaceId: number;
  // departmentId: number;
  // teamId: number;
  startTime: string;
  endTime: string;
  breakMinutes: number;
  isNightShift: boolean;
  graceInMinutes: number;
  graceOutMinutes: number;
  status: boolean;
  weeklyOffs: WeeklyOff[];
}

// Pagination type alias
export type ShiftDefinitionPaginatedResponse =
  PaginatedResponse<ShiftDefinition>;
