import { DBModel, PaginatedResponse } from '.';

export interface AttendanceDeviceCategory extends DBModel {
  name: string;
  communicationType: string;
  biometricMode: string;
  description: string;
  status: boolean;
}

export interface AttendanceDeviceCategoryRequest {
  name: string;
  communicationType: string;
  biometricMode: string;
  description: string;
  status: boolean | number;
  message?: string;
}

// Pagination type alias
export type AttendanceDeviceCategoryPaginatedResponse = PaginatedResponse<AttendanceDeviceCategory>;
