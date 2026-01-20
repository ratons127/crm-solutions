'use client';

import { baseApi } from '@/services/api/baseAPI';

type ShiftAssignFilterPayload = {
  // employeeId?: number | null;
  companyId: number | null;
  // businessUnitId: number | null;
  // workplaceGroupId: number | null;
  // workplaceId: number | null;
  // departmentId: number | null;
  // teamId: number | null;
};

export const shiftAssignAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    fetchShiftAssignableEmployees: builder.mutation<
      { data: any[] },
      ShiftAssignFilterPayload
    >({
      query: body => ({
        method: 'POST',
        url: `/v1/employees/shift-assign`,
        body,
      }),
    }),
  }),
});

export const { useFetchShiftAssignableEmployeesMutation } = shiftAssignAPI;
