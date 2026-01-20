'use client';

import { baseApi } from '@/services/api/baseAPI';

type ShiftAssignmentPayload = {
  id: number;
  employeeIds: number[];
  shiftId: number;
  effectiveFrom: string; // YYYY-MM-DD
  effectiveTo: string;   // YYYY-MM-DD
  status: boolean;
  assignmentSource: 'manual' | string;
  assignedBy: number;
  assignedAt: string; // ISO timestamp
};

export const shiftAssignmentAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    createShiftAssignment: builder.mutation<any, ShiftAssignmentPayload>({
      query: body => ({
        method: 'POST',
        url: `/v1/shift-assignment`,
        body,
      }),
      invalidatesTags: ['shift-definitions', 'employees'],
    }),
  }),
});

export const { useCreateShiftAssignmentMutation } = shiftAssignmentAPI;

