'use client';

import { baseApi } from '@/services/api/baseAPI';

const routePath = 'shift-assignment';

export const shiftAssignAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Shift Assign
    createShiftAssign: builder.mutation<any, any>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/employee`,
          body,
        };
      },
      invalidatesTags: ['shift-assign'],
    }),

    // Create Shift Assign
    getShiftHistory: builder.mutation<any, any>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/get-shifts`,
          body,
        };
      },
      invalidatesTags: ['shift-assign'],
    }),

    // End of API
  }),
});

export const { useCreateShiftAssignMutation, useGetShiftHistoryMutation } =
  shiftAssignAPI;
