'use client';
import {
  LeavePolicy,
  LeavePolicyCreate,
  LeavePolicyPaginatedResponse,
  LeavePolicyUpdate,
} from '../../../lib/types/leave';
import { PaginationParams } from '../../../lib/types';
import { baseApi } from '../baseAPI';

export const leavePolicyAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeavePolicy: builder.mutation<LeavePolicy, LeavePolicyCreate>({
      query: body => {
        return {
          method: 'POST',
          url: '/v1/leave-policies/save',
          body,
        };
      },
      invalidatesTags: ['LeavePolicy'],
    }),
    deleteLeavePolicy: builder.mutation<void, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/leave-policies/delete/${id}`,
        };
      },
      invalidatesTags: ['LeavePolicy'],
    }),
    updateLeavePolicy: builder.mutation<
      LeavePolicy,
      { id: number; data: LeavePolicyUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/leave-policies/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['LeavePolicy'],
    }),

    // getLeavePolicyList
    getLeavePolicyList: builder.query<{ data: LeavePolicy[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: '/v1/leave-policies/all',
        };
      },
      providesTags: ['LeavePolicy'],
    }),

    // Get Paginated Leave Policies
    getPaginatedLeavePolicies: builder.query<
      LeavePolicyPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: '/v1/leave-policies',
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['LeavePolicy'],
    }),

    // End of API
  }),
});

export const {
  useCreateLeavePolicyMutation,
  useGetLeavePolicyListQuery,
  useGetPaginatedLeavePoliciesQuery,
  useDeleteLeavePolicyMutation,
  useUpdateLeavePolicyMutation,
} = leavePolicyAPI;
