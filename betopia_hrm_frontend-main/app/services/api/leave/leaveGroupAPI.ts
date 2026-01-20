'use client';
import {
  LeaveGroupData,
  LeaveGroupDataRequest,
  LeaveGroupPaginatedResponse,
} from '@/lib/types/leave';
import { PaginationParams } from '../../../lib/types';
import { baseApi } from '../baseAPI';

export const leaveGroupAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeaveGroup: builder.mutation<
      LeaveGroupDataRequest,
      Partial<LeaveGroupDataRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: '/v1/leave-groups/save',
          body,
        };
      },
      invalidatesTags: ['LeaveGroup'],
    }),
    deleteLeaveGroup: builder.mutation<LeaveGroupData, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/leave-groups/delete/${id}`,
        };
      },
      invalidatesTags: ['LeaveGroup'],
    }),
    updateLeaveGroup: builder.mutation<
      LeaveGroupDataRequest,
      { id: number; data: LeaveGroupDataRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/leave-groups/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['LeaveGroup'],
    }),

    // getLeaveGroupList
    getLeaveGroupList: builder.query<{ data: LeaveGroupData[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: '/v1/leave-groups/all',
        };
      },
      providesTags: ['LeaveGroup'],
    }),

    // Get Paginated Leave Groups
    getPaginatedLeaveGroups: builder.query<
      LeaveGroupPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: '/v1/leave-groups',
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['LeaveGroup'],
    }),

    // End of API
  }),
});

export const {
  useCreateLeaveGroupMutation,
  useGetLeaveGroupListQuery,
  useGetPaginatedLeaveGroupsQuery,
  useDeleteLeaveGroupMutation,
  useUpdateLeaveGroupMutation,
} = leaveGroupAPI;
