'use client';
import { LeaveTypeRule, LeaveTypeRuleUpdate } from '../../../lib/types/leave';
import { baseApi } from '../baseAPI';

export const leaveTypeRuleAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeaveTypeRule: builder.mutation<
      LeaveTypeRule,
      Partial<LeaveTypeRule>
    >({
      query: body => {
        return {
          method: 'POST',
          url: '/v1/leave-type-rules/save',
          body,
        };
      },
      invalidatesTags: ['LeaveTypeRule'],
    }),
    deleteLeaveTypeRule: builder.mutation<LeaveTypeRule, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/leave-type-rules/delete/${id}`,
        };
      },
      invalidatesTags: ['LeaveTypeRule'],
    }),
    updateLeaveTypeRule: builder.mutation<
      LeaveTypeRule,
      { id: number; data: LeaveTypeRuleUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/leave-type-rules/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['LeaveTypeRule'],
    }),

    // getLeaveTypeList
    getLeaveTypeRuleList: builder.query<{ data: LeaveTypeRule[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: '/v1/leave-type-rules/all',
        };
      },
      providesTags: ['LeaveTypeRule'],
    }),

    // End of API
  }),
});

export const {
  useCreateLeaveTypeRuleMutation,
  useGetLeaveTypeRuleListQuery,
  useDeleteLeaveTypeRuleMutation,
  useUpdateLeaveTypeRuleMutation,
} = leaveTypeRuleAPI;
