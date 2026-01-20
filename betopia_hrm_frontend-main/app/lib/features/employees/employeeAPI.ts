import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import { Employee } from '../../types/employee';
import {
  fetchEmployeesFailure,
  fetchEmployeesStart,
  fetchEmployeesSuccess,
} from './employeeSlice';

export const fetchEmployees = () => async (dispatch: AppDispatch) => {
  dispatch(fetchEmployeesStart());
  try {
    const data = await getEmployeeList();
    dispatch(fetchEmployeesSuccess(data));
  } catch (err: any) {
    dispatch(
      fetchEmployeesFailure(
        err.response?.data?.message || 'Failed fetching employees'
      )
    );
  }
};
export const getEmployeeList = async () => {
  const { data } = await apiClient.get<{ data: Employee[] }>('/employees/all');
  return data.data;
};

/* ---------------- GET: supervisor name for logged-in user ---------------- */
export const getSupervisorName = async (): Promise<{
  firstName: string;
  lastName: string;
  employeeSerialId: string;
  imageUrl?: string | null;
} | null> => {
  const { data } = await apiClient.get<{
    data: {
      firstName: string;
      lastName: string;
      employeeSerialId: string;
      imageUrl?: string | null;
    } | null;
  }>('/employees/supervisorName');

  return data?.data ?? null;
};

/* ---------------- Get employee by ID ---------------- */
export const getEmployeeById = async (id: number) => {
  const { data } = await apiClient.get<{ data: Employee }>(`/employees/${id}`);
  return data.data;
};

