'use client';

import { LeaveYearType, LeaveYearPaginatedResponse } from '@/lib/types/leave';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'leave-years';

export const leaveYearAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Leave Year
    createLeaveYear: builder.mutation<LeaveYearType, Partial<LeaveYearType>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body,
        };
      },
      invalidatesTags: ['leave-years'],
    }),

    // Update Leave Year
    updateLeaveYear: builder.mutation<
      LeaveYearType,
      { id: number; data: Partial<LeaveYearType> }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['leave-years'],
    }),

    // Delete Leave Year
    deleteLeaveYear: builder.mutation<void, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/delete/${id}`,
        };
      },
      invalidatesTags: ['leave-years'],
    }),

    // Get Leave Year List (all)
    getLeaveYearList: builder.query<{ data: LeaveYearType[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['leave-years'],
    }),

    // Get Paginated Leave Years
    getPaginatedLeaveYears: builder.query<
      LeaveYearPaginatedResponse,
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
      providesTags: ['leave-years'],
    }),

    // End of API
  }),
});

export const {
  useGetLeaveYearListQuery,
  useGetPaginatedLeaveYearsQuery,
  useCreateLeaveYearMutation,
  useUpdateLeaveYearMutation,
  useDeleteLeaveYearMutation,
} = leaveYearAPI;
