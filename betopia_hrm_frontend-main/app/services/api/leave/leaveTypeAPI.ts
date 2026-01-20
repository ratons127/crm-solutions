'use client';
import {
  LeaveType,
  LeaveTypePaginatedResponse,
  LeaveTypeUpdate,
} from '../../../lib/types/leave';
import { PaginationParams } from '../../../lib/types';
import { baseApi } from '../baseAPI';

export const leaveTypeAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeaveType: builder.mutation<LeaveType, Partial<LeaveType>>({
      query: body => {
        return {
          method: 'POST',
          url: '/v1/leave-types/save',
          body,
        };
      },
      invalidatesTags: ['LeaveType'],
    }),
    deleteLeaveType: builder.mutation<LeaveType, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/leave-types/delete/${id}`,
        };
      },
      invalidatesTags: ['LeaveType'],
    }),
    updateLeaveType: builder.mutation<
      LeaveType,
      { id: number; data: LeaveTypeUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/leave-types/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['LeaveType'],
    }),

    // getLeaveTypeList
    getLeaveTypeList: builder.query<{ data: LeaveType[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: '/v1/leave-types/all',
        };
      },
      providesTags: ['LeaveType'],
    }),

    // Get Paginated Leave Types
    getPaginatedLeaveTypes: builder.query<
      LeaveTypePaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: '/v1/leave-types',
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['LeaveType'],
    }),

    // End of API
  }),
});

export const {
  useCreateLeaveTypeMutation,
  useGetLeaveTypeListQuery,
  useGetPaginatedLeaveTypesQuery,
  useDeleteLeaveTypeMutation,
  useUpdateLeaveTypeMutation,
} = leaveTypeAPI;
