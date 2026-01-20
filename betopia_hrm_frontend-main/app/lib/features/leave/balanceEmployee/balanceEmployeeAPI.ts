// src/lib/features/leave/balanceEmployee/balanceEmployeeAPI.ts
import apiClient from '../../../../services/apiClient';
import { DBModel } from '../../../types';
import { LeaveBalanceEmployee } from '../../../types/leave';

const routePath = `leave-balance-employee`;

export const createLeaveBalanceEmployee = async (payload: {
  year: number;
  employeeId: number;
}) => {
  const { data } = await apiClient.post<{ data: LeaveBalanceEmployee }>(
    `/${routePath}/initialize/${payload.employeeId}/${payload.year}`
  );
  return data?.data;
};

export const getLeaveBalanceEmployeeList = async ({
  year,
  employeeId,
}: {
  year: number;
  employeeId: number;
}) => {
  const res = await apiClient.get(`/${routePath}/${employeeId}/${year}`);

  // ✅ Normalize to array no matter what the backend wraps
  const payload = res?.data;
  const list: unknown = Array.isArray(payload)
    ? payload
    : Array.isArray(payload?.data)
      ? payload.data
      : Array.isArray(payload?.data?.content)
        ? payload.data.content
        : [];

  return list as LeaveBalanceEmployee[];
};

export const updateLeaveBalanceEmployeeById = async (
  id: LeaveBalanceEmployee['id'],
  payload: Partial<Omit<LeaveBalanceEmployee, keyof DBModel>>
) => {
  const { data } = await apiClient.put<{ data: LeaveBalanceEmployee }>(
    `/${routePath}/update/${id}`,
    payload
  );
  return data?.data;
};

export const deleteLeaveBalanceEmployeeById = async (
  id: LeaveBalanceEmployee['id']
) => {
  await apiClient.delete(`/${routePath}/delete/${id}`);
  return true;
};

// New Employee Leave Balance API setup
import { baseApi } from '@/services/api/baseAPI';

export const leaveBalanceAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    getEmployeeBalanceList: builder.query<
      { data: any[] },
      { employeeId: number | string; year: number }
    >({
      query: ({ employeeId, year }) => ({
        method: 'GET',
        url: `/v1/${routePath}/${employeeId}/${year}`,
      }),
      providesTags: ['leave-balance'],
    }),
  }),
});

export const { useGetEmployeeBalanceListQuery } = leaveBalanceAPI;
