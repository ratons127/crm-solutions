'use client';

import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'manual-attendance';

export const manualAttendanceAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    createManualAttendance: builder.mutation<any, any>({
      query: body => ({
        method: 'POST',
        url: `/v1/${routePath}/save`,
        body,
      }),
      invalidatesTags: [routePath],
    }),
    // Get Paginated Manual Attendance list
    getPaginatedManualAttendance: builder.query<
      any,
      PaginationParams & { keyword?: string; userId?: number }
    >({
      query: ({
        page = 1,
        perPage = 10,
        sortDirection = 'DESC',
        keyword = '',
        userId,
      }) => ({
        method: 'GET',
        url: `/v1/${routePath}`,
        params: {
          sortDirection,
          page,
          perPage,
          keyword: keyword.trim() || undefined,
          userId: userId || undefined, // only include if present
        },
      }),
      providesTags: [routePath],
    }),
  }),
});

export const {
  useCreateManualAttendanceMutation,
  useGetPaginatedManualAttendanceQuery,
} = manualAttendanceAPI;
