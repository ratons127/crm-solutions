import { DBModel, DBModelId, ForeignKeyId, PaginatedResponse } from '..';

// Employee Qualification Types
export interface EmployeeQualificationType extends DBModel {
  typeName: string;
}

export interface EmployeeQualificationTypeRequest {
  typeName: string;
  status?: number | boolean;
  message?: string;
}

// Employee Qualification Level
export interface EmployeeQualificationLevel extends DBModel {
  qualificationTypeId: number;
  levelName: string;
}

export interface EmployeeQualificationLevelRequest {
  qualificationTypeId: ForeignKeyId;
  levelName: string;
  status?: boolean | number;
  message?: string;
}

// Field of Study
export interface EmployeeFieldOfStudy extends DBModel {
  qualificationLevelId: ForeignKeyId;
  fieldStudyName: string;
}

export interface EmployeeFieldOfStudyRequest {
  qualificationLevelId: ForeignKeyId;
  fieldStudyName: string;
  status?: boolean | number;
  message?: string;
}

// Employee Qualification Rating method
export interface QualificationRatingMethodDetail {
  id?: DBModelId;
  grade: string;
  minimumMark: number;
  maximumMark: number;
  averageMark: number;
}

export interface QualificationRatingMethod extends DBModel {
  code: string;
  methodName: string;
  status?: boolean | number;
  message?: string;
  qualificationRatingMethodDetails: QualificationRatingMethodDetail[];
}

// Institute name
export interface InstituteNameData extends DBModel {
  name: string;
}

export interface InstituteNameRequest {
  name: string;
  status?: boolean | number;
  message?: string;
}

// Pagination type aliases
export type QualificationTypePaginatedResponse =
  PaginatedResponse<EmployeeQualificationType>;
export type QualificationLevelPaginatedResponse =
  PaginatedResponse<EmployeeQualificationLevel>;
export type FieldOfStudyPaginatedResponse =
  PaginatedResponse<EmployeeFieldOfStudy>;
export type InstituteNamePaginatedResponse =
  PaginatedResponse<InstituteNameData>;
