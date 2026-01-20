'use client';

import { PaginationParams } from '@/lib/types';
import {
  AttendanceApproval,
  PaginatedResponse,
  AttendanceApprovalStatusBulkRequest,
} from '@/lib/types/attendanceApproval';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'attendance-approval';

export const attendanceApprovalAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // ✅ Get paginated list with keyword search
    getPaginatedAttendanceApprovalList: builder.query<
      PaginatedResponse<AttendanceApproval>,
      PaginationParams & { keyword?: string }
    >({
      query: ({
        page = 1,
        perPage = 10,
        sortDirection = 'DESC',
        keyword = '',
      }) => ({
        method: 'GET',
        url: `/v1/${routePath}`,
        params: {
          sortDirection,
          page,
          perPage,
          keyword: keyword.trim() || undefined,
        },
      }),
      providesTags: [routePath],
    }),

    // ✅ Update approval status
    updateManualAttendanceApprovalStatus: builder.mutation<
      AttendanceApproval,
      { id: number; data: Partial<AttendanceApproval> }
    >({
      query: ({ id, data }) => ({
        method: 'PATCH',
        url: `/v1/${routePath}/status/${id}`,
        body: data,
      }),
      invalidatesTags: [routePath],
    }),

    // �o. Bulk update approval status
    bulkUpdateManualAttendanceApprovalStatus: builder.mutation<
      any,
      AttendanceApprovalStatusBulkRequest
    >({
      query: body => ({
        method: 'PATCH',
        url: `/v1/${routePath}/status`,
        body,
      }),
      invalidatesTags: [routePath],
    }),
  }),
});

export const {
  useUpdateManualAttendanceApprovalStatusMutation,
  useBulkUpdateManualAttendanceApprovalStatusMutation,
  useGetPaginatedAttendanceApprovalListQuery,
} = attendanceApprovalAPI;
