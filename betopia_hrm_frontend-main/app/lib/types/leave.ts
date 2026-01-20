import {
  BusinessUnit,
  Company,
  Department,
  Team,
  Workplace,
  WorkplaceGroup,
} from './admin/company';
import { DBModel, ForeignKeyId, PaginatedResponse, Simplify } from './index';
export interface LeaveType extends DBModel {
  code: string;
  name: string;
  status: boolean;
  description: string;
  message?: string;
}
export type LeaveTypeCreate = Omit<LeaveType, keyof DBModel>;
export type LeaveTypeUpdate = Partial<LeaveTypeCreate>;

export interface LeavePolicy extends DBModel {
  // Base relations
  leaveTypeId?: number | string;
  leaveType?: LeaveType;
  leaveGroupAssignId: number | string;
  leaveGroupAssign?: any;
  employeeTypeId?: number | string;

  // Core configuration
  defaultQuota: number;
  minDays: number;
  maxDays: number;
  status: boolean;

  // Carry forward settings
  carryForwardAllowed: boolean;
  carryForwardCap: number;

  // Leave approval & workflow
  requiresApproval: boolean;
  expeditedApproval: boolean;
  coveringEmployee: boolean;
  prorata: boolean;

  // Proof/documentation
  proofRequired: boolean;
  proofThreshold: number;

  // Gender restriction
  genderRestricted: boolean;
  eligibleGender?: string;

  // Tenure & occurrence limits
  tenureRequiredDays: number; // ✅ keep this, replaces old tenureRequired
  maxOccurrences: number;

  // Encashment & balance
  encashable: boolean;
  allowNegativeBalance: boolean;
  maxAdvanceDays: number;

  // Earned leave settings
  earnedLeave: boolean;
  earnedAfterDays: number;
  earnedLeaveDays: number;
  accrualRatePerMonth: number;
  extraDaysAfterYears: number;

  // Bridge leave restriction
  restrictBridgeLeave: boolean;
  maxBridgeDays: number;

  // Other policy controls
  linkedToOvertime: boolean;
  allowHalfDay: boolean;
}

export type LeavePolicyCreate = Simplify<Omit<LeavePolicy, keyof DBModel>>;
export type LeavePolicyUpdate = Simplify<Partial<LeavePolicyCreate>>;

export interface LeaveBalanceEmployee extends DBModel {
  employeeId: number;
  leaveTypeId: number;

  //
  leaveType?: LeaveType; // populated
  employee?: null; // populated

  //
  year: number;
  entitledDays: number;
  carriedForward: number;
  encashed: number;
  usedDays: number;
  balance: number;

  //
  deletedAt: string;
  createdBy: number;
  lastModifiedBy: number;
  createdAt: number;
  updatedAt: number;
  version: number;
}

export type LeaveBalanceEmployeeInput = Omit<
  LeaveBalanceEmployee,
  | keyof DBModel
  | 'deletedAt'
  | 'createdBy'
  | 'lastModifiedBy'
  | 'createdAt'
  | 'updatedAt'
  | 'version'
>;

export interface LeaveGroupType {
  id: number;
  name: string;
  description: string;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: number;
  lastModifiedBy: number;
}
export type LeaveGroupInput = Omit<
  LeaveGroupType,
  'id' | 'createdDate' | 'lastModifiedDate' | 'createdBy' | 'lastModifiedBy'
>;

export interface LeaveYearType {
  id: number;
  startDate: string | Date;
  endDate: string | Date;
  isActive: boolean;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: number;
  lastModifiedBy: number;
}

// Leave Type Rules

export type LeaveTypeRule = Simplify<
  DBModel & {
    leaveType: LeaveType;
    ruleKey: string;
    ruleValue: string;
    description: string;
    isActive: boolean;
  }
>;

export type LeaveTypeRuleCreate = Simplify<
  Omit<LeaveTypeRule, 'leaveType' | keyof DBModel> & {
    leaveTypeId: number | string;
  }
>;

export type LeaveTypeRuleUpdate = Simplify<Partial<LeaveTypeRuleCreate>>;

// End of Leave Type Rules

// Leave ELIGIBILITY

export type LeaveEligibility = Simplify<
  DBModel & {
    leaveType: LeaveType;
    leaveGroup: LeaveGroupType;
    gender: string;
    minTenureMonths: number;
    maxTenureMonths: number;
    employmentStatus: string;
    isActive: boolean;
  }
>;
export type LeaveEligibilityCreate = Simplify<
  Omit<LeaveEligibility, keyof DBModel | 'leaveType' | 'leaveGroup'> & {
    leaveTypeId: number | string;
    leaveGroupId: number | string;
  }
>;
export type LeaveEligibilityUpdate = Simplify<Partial<LeaveEligibilityCreate>>;

// END

export type LeaveCategoryCreate = {
  name: string;
  parentId?: ForeignKeyId | null;
  status: boolean;
  message?: string;
};

export type LeaveCategoryUpdate = Partial<LeaveCategoryCreate>;
export type LeaveCategory = Simplify<DBModel & LeaveCategoryCreate>;

// Leave Group Types
export interface LeaveGroupData extends DBModel {
  name: string;
  description: string;
  status: boolean;
}

export interface LeaveGroupDataRequest {
  name: string;
  description: string;
  status: boolean | number;
  message?: string;
  success?: boolean;
}

export type LeaveGroupCreate = {
  name: string;
  code: string;
  description: string;
  status: boolean;
};
export type LeaveGroupUpdate = Partial<LeaveGroupCreate>;
export type LeaveGroup = Simplify<DBModel & LeaveGroupCreate>;

export interface LeaveGroupAssignCreate {
  leaveTypeId: ForeignKeyId;
  leaveGroupId: ForeignKeyId;
  companyId: ForeignKeyId;
  businessUnitId: ForeignKeyId;
  workPlaceGroupId: ForeignKeyId;
  workPlaceId: ForeignKeyId;
  departmentId?: ForeignKeyId;
  teamId?: ForeignKeyId;
  description: string;
  status: boolean;
  message?: string;
}

export type LeaveGroupAssignUpdate = Partial<LeaveGroupAssignCreate>;
export type LeaveGroupAssign = Simplify<
  DBModel &
    Omit<
      LeaveGroupAssignCreate,
      | 'leaveTypeId'
      | 'leaveGroupId'
      | 'companyId'
      | 'businessUnitId'
      | 'workPlaceGroupId'
      | 'workPlaceId'
      | 'departmentId'
      | 'teamId'
    >
> & {
  leaveType: LeaveType;
  leaveGroup: LeaveGroup;
  company: Company;
  businessUnit: BusinessUnit;
  workplaceGroup: WorkplaceGroup;
  workplace: Workplace;
  department: Department;
  team: Team;
};

// Pagination type aliases
export type LeaveYearPaginatedResponse = PaginatedResponse<LeaveYearType>;
export type LeaveCategoryPaginatedResponse = PaginatedResponse<LeaveCategory>;
export type LeaveTypePaginatedResponse = PaginatedResponse<LeaveType>;
export type LeavePolicyPaginatedResponse = PaginatedResponse<LeavePolicy>;
export type LeaveGroupPaginatedResponse = PaginatedResponse<LeaveGroupData>;
export type LeaveGroupAssignPaginatedResponse =
  PaginatedResponse<LeaveGroupAssign>;
