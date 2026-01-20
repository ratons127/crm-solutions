'use client';
import { baseApi } from '@/services/api/baseAPI';

export type AttendanceStatusType =
  | 'PRESENT'
  | 'LATE'
  | 'EARLY_LEAVE'
  | 'HALF_DAY'
  | 'ABSENT'
  | 'LEAVE';

export interface AttendanceStatus {
  status: AttendanceStatusType;
  statusDate: string; // YYYY-MM-DD
}

export interface AttendanceStatusResponse {
  data: AttendanceStatus[];
  message: string;
  success: boolean;
  status: number;
}

const routePath = 'attendance-summary';

export const attendanceStatusAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getAttendanceStatus: builder.query<AttendanceStatusResponse, void>({
      query: () => ({
        url: `/v1/${routePath}/attendanceStatus`,
        method: 'GET',
      }),
      providesTags: ['attendance-status'],
    }),
  }),
});

export const { useGetAttendanceStatusQuery } = attendanceStatusAPI;
