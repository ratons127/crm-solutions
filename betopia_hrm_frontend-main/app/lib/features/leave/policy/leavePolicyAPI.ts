import apiClient from '../../../../services/apiClient';
import { DBModel } from '../../../types';
import { LeavePolicy } from '../../../types/leave';

export const createLeavePolicy = async (
  payload: Omit<LeavePolicy, keyof DBModel>
) => {
  const { data } = await apiClient.post<{ data: LeavePolicy }>(
    '/leave-policies/save',
    payload
  );
  return data.data;
};

export const getLeavePolicyList = async () => {
  const { data } = await apiClient.get<{ data: LeavePolicy[] }>(
    '/leave-policies/all'
  );
  return data.data;
};

export const updateLeavePolicyById = async (
  id: LeavePolicy['id'],
  payload: Partial<Omit<LeavePolicy, keyof DBModel>>
) => {
  const { data } = await apiClient.put<{ data: LeavePolicy }>(
    `/leave-policies/update/${id}`,
    payload
  );
  return data.data;
};
export const deleteLeavePolicyById = async (id: LeavePolicy['id']) => {
  await apiClient.delete(`/leave-policies/delete/${id}`);

  return true;
};
