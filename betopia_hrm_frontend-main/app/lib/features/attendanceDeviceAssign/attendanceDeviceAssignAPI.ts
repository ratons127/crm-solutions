'use client';

import {
  AttendanceDeviceAssign,
  AttendanceDeviceAssignRequest,
  AttendanceDeviceAssignPaginatedResponse,
} from '@/lib/types/attendanceDeviceAssign';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'attendance-device-assign';

export const attendanceDeviceAssignAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Device Assignment
    createAttendanceDeviceAssign: builder.mutation<
      { status: boolean; message: string; data: AttendanceDeviceAssign },
      Partial<AttendanceDeviceAssignRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['attendance-device-assigns'],
    }),

    // Update Device Assignment
    updateAttendanceDeviceAssign: builder.mutation<
      { status: boolean; message: string; data: AttendanceDeviceAssign },
      { id: number; data: AttendanceDeviceAssignRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['attendance-device-assigns'],
    }),

    // Delete Device Assignment
    deleteAttendanceDeviceAssign: builder.mutation<
      { status: boolean; message: string },
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['attendance-device-assigns'],
    }),

    // Get Device Assignment List (all)
    getAttendanceDeviceAssignList: builder.query<
      { data: AttendanceDeviceAssign[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['attendance-device-assigns'],
    }),

    // Get Device Assignments by Employee ID
    getAttendanceDeviceAssignByEmployee: builder.query<
      { data: AttendanceDeviceAssign[] },
      { employeeId: number }
    >({
      query: ({ employeeId }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}/employee/${employeeId}`,
        };
      },
      providesTags: ['attendance-device-assigns'],
    }),

    // Get Paginated Device Assignments
    getPaginatedAttendanceDeviceAssigns: builder.query<
      AttendanceDeviceAssignPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['attendance-device-assigns'],
    }),

    // End of API
  }),
});

export const {
  useGetAttendanceDeviceAssignListQuery,
  useGetAttendanceDeviceAssignByEmployeeQuery,
  useGetPaginatedAttendanceDeviceAssignsQuery,
  useCreateAttendanceDeviceAssignMutation,
  useUpdateAttendanceDeviceAssignMutation,
  useDeleteAttendanceDeviceAssignMutation,
} = attendanceDeviceAssignAPI;
