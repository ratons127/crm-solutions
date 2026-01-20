import { DBModel, ForeignKeyId, PaginatedResponse } from '.';

export interface AttendanceDeviceCategory {
  id: number;
  name: string;
  communicationType: string;
  biometricMode: string;
  description: string;
  status: boolean;
}

export interface Location {
  id: number;
  parentId: number;
  name: string;
  type: string;
  status: boolean;
}

export interface AttendanceDevice extends DBModel {
  category: AttendanceDeviceCategory;
  location: Location;
  deviceName: string;
  deviceType: string;
  serialNumber: string;
  ipAddress: string;
  macAddress: string;
  firmwareVersion: string;
  deviceCount: number;
  status: boolean;
}

export interface AttendanceDeviceRequest {
  categoryId: ForeignKeyId;
  locationId: ForeignKeyId;
  deviceName: string;
  deviceType: string;
  serialNumber: string;
  ipAddress: string;
  macAddress: string;
  firmwareVersion: string;
  deviceCount: number;
  status: boolean | number;
  message?: string;
}

// Pagination type alias
export type AttendanceDevicePaginatedResponse = PaginatedResponse<AttendanceDevice>;
