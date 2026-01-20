export interface AttendanceDeviceAssign {
  id: number;
  attendanceDeviceId?: number;
  employeeId?: number;
  deviceUserId: number;
  assignedBy: number;
  assignedAt: string;
  notes?: string;
  status: boolean;
  createdAt?: string;
  updatedAt?: string;
  // Relations
  attendanceDevice?: {
    id: number;
    deviceName: string;
    deviceType: string;
    status: boolean;
  };
  employee?: {
    id: number;
    firstName: string;
    lastName: string;
    gender: string;
    phone: string;
    email: string;
    presentAddress?: string;
  };
  assignedByUser?: {
    id: number;
    name: string;
  };
}

export interface AttendanceDeviceAssignRequest {
  attendanceDeviceId: number;
  employeeId: number;
  deviceUserId: number;
  assignedBy: number;
  assignedAt: string;
  notes: string;
  status: boolean;
}

export interface AttendanceDeviceAssignPaginatedResponse {
  data: AttendanceDeviceAssign[];
  meta: {
    total: number;
    perPage: number;
    currentPage: number;
    lastPage: number;
    firstPage: number;
    firstPageUrl: string;
    lastPageUrl: string;
    nextPageUrl: string | null;
    previousPageUrl: string | null;
  };
}
