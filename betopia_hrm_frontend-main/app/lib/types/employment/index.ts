import { DBModel, ForeignKeyId, PaginatedResponse } from '..';

// Employment Group

interface BaseEntity {
  id: number;
  name: string;
}

interface company extends BaseEntity {}
interface workplace extends BaseEntity {}

export interface EmploymentGroupData extends DBModel {
  company: company;
  workplace: workplace;
  name: string;
  code: string;
  description: string;
  status: boolean;
}

export interface EmploymentGroupRequest {
  companyId: ForeignKeyId;
  workplaceId: ForeignKeyId;
  name: string;
  code: string;
  description: string;
  status: boolean;
  message?: string;
}

// Employment Types
export interface EmploymentTypesData extends DBModel {
  name: string;
  description: string;
  status: boolean;
}

export interface EmploymentTypesRequest {
  name: string;
  description: string;
  status: boolean | number;
  message?: string;
  success?: boolean;
}

// Pagination type aliases
export type EmploymentGroupPaginatedResponse =
  PaginatedResponse<EmploymentGroupData>;
export type EmploymentTypesPaginatedResponse =
  PaginatedResponse<EmploymentTypesData>;
