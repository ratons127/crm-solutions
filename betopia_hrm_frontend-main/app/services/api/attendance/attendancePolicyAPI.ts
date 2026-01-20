'use client';

import { PaginationParams } from '@/lib/types';
import {
  AttendancePolicy,
  AttendancePolicyCreate,
  AttendancePolicyPaginatedResponse,
  AttendancePolicyUpdate,
} from '@/lib/types/attendancePolicy';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'attendance-policy';

export const attendancePolicyAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    createAttendancePolicy: builder.mutation<
      any,
      Partial<AttendancePolicyCreate>
    >({
      query: body => ({
        method: 'POST',
        url: `/v1/${routePath}/save`,
        body,
      }),
      invalidatesTags: ['attendance-policy'],
    }),

    updateAttendancePolicy: builder.mutation<
      any,
      { id: number; data: AttendancePolicyUpdate }
    >({
      query: ({ id, data }) => ({
        method: 'PUT',
        url: `/v1/${routePath}/update/${id}`,
        body: data,
      }),
      invalidatesTags: ['attendance-policy'],
    }),
    // Fetch list or filter by companyId when provided
    getAttendancePolicies: builder.query<
      { data: AttendancePolicy[] } | AttendancePolicyPaginatedResponse,
      { companyId?: number } | undefined
    >({
      query: params => ({
        method: 'GET',
        url: `/v1/${routePath}/all`,
        params,
      }),
      providesTags: ['attendance-policy'],
    }),

    // Paginated list (follow banksAPI.ts pattern)
    getPaginatedAttendancePolicies: builder.query<
      AttendancePolicyPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => ({
        method: 'GET',
        url: `/v1/${routePath}`,
        params: {
          sortDirection,
          page,
          perPage,
        },
      }),
      providesTags: ['attendance-policy'],
    }),
  }),
});

export const {
  useCreateAttendancePolicyMutation,
  useUpdateAttendancePolicyMutation,
  useGetAttendancePoliciesQuery,
  useGetPaginatedAttendancePoliciesQuery,
} = attendancePolicyAPI;
