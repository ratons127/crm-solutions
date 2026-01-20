'use client';
import {
  LeaveEligibility,
  LeaveEligibilityUpdate,
} from '../../../lib/types/leave';
import { baseApi } from '../baseAPI';

const routePath = `leave-eligibility`;

export const leaveEligibilityAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeaveEligibility: builder.mutation<
      LeaveEligibility,
      Partial<LeaveEligibility>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body,
        };
      },
      invalidatesTags: [`LeaveEligibility`],
    }),
    deleteLeaveEligibility: builder.mutation<LeaveEligibility, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/delete/${id}`,
        };
      },
      invalidatesTags: [`LeaveEligibility`],
    }),
    updateLeaveEligibility: builder.mutation<
      LeaveEligibility,
      { id: number; data: LeaveEligibilityUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: [`LeaveEligibility`],
    }),

    // getLeaveTypeList
    getLeaveEligibilityList: builder.query<
      { data: LeaveEligibility[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [`LeaveEligibility`],
    }),

    // End of API
  }),
});

export const {
  useCreateLeaveEligibilityMutation,
  useGetLeaveEligibilityListQuery,
  useDeleteLeaveEligibilityMutation,
  useUpdateLeaveEligibilityMutation,
} = leaveEligibilityAPI;
