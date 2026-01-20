'use client';

import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'leave-requests';

export const leaveRequestAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Leave Request
    createLeaveRequest: builder.mutation<any, FormData>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body,
        };
      },
      invalidatesTags: ['leave-requests'],
    }),

    // Update Leave Request
    updateLeaveRequest: builder.mutation<
      any,
      { id: number; data: Partial<any> }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['leave-requests'],
    }),

    // Delete Leave Request
    deleteLeaveRequest: builder.mutation<any, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/delete/${id}`,
        };
      },
      invalidatesTags: ['leave-requests'],
    }),

    // Get Leave Request List (all)
    getLeaveRequestList: builder.query<{ data: any[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['leave-requests'],
    }),

    // Get Leave Request List (all) by Id
    getLeaveRequestListById: builder.query<
      { data: any[] },
      { userId?: number }
    >({
      query: ({ userId }) => ({
        method: 'GET',
        url: `/v1/${routePath}/all`,
        params: { userId }, // Backend filters by userId
      }),
      providesTags: ['leave-requests'],
    }),

    // Get Paginated Leave Requests
    getPaginatedLeaveRequests: builder.query<
      any,
      PaginationParams & { keyword?: string; userId?: number }
    >({
      query: ({
        page = 1,
        perPage = 10,
        sortDirection = 'DESC',
        keyword = '',
        userId,
      }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: {
            sortDirection,
            page,
            perPage,
            keyword: keyword.trim() || undefined, // only include when not empty
            userId: userId || undefined, // include when provided
          },
        };
      },
      providesTags: ['leave-requests'],
    }),

    // Get Leave Request by ID
    getLeaveRequestById: builder.query<{ data: any }, number>({
      query: id => {
        return {
          method: 'GET',
          url: `/v1/${routePath}/${id}`,
        };
      },
      providesTags: ['leave-requests'],
    }),

    // Get Leave Requests by Employee
    getLeaveRequestsByEmployee: builder.query<{ data: any[] }, number>({
      query: employeeId => {
        return {
          method: 'GET',
          url: `/v1/${routePath}/employee/${employeeId}`,
        };
      },
      providesTags: ['leave-requests'],
    }),

    // End of API
  }),
});

export const {
  useCreateLeaveRequestMutation,
  useUpdateLeaveRequestMutation,
  useDeleteLeaveRequestMutation,
  useGetLeaveRequestListQuery,
  useGetPaginatedLeaveRequestsQuery,
  useGetLeaveRequestByIdQuery,
  useGetLeaveRequestsByEmployeeQuery,
  useGetLeaveRequestListByIdQuery,
} = leaveRequestAPI;
