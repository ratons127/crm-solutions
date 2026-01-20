// lib/features/employees/employeeSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { Employee } from '../../types/employee';

interface EmployeeState {
  list: Employee[];
  loading: boolean;
  error: string | null;
  supervisorName: string | null;
}

const initialState: EmployeeState = {
  list: [],
  loading: false,
  error: null,
  supervisorName: null,
};

const employeeSlice = createSlice({
  name: 'employees',
  initialState,
  reducers: {
    fetchEmployeesStart(state) {
      state.loading = true;
      state.error = null;
    },
    fetchEmployeesSuccess(state, action: PayloadAction<Employee[]>) {
      state.list = action.payload;
      state.loading = false;
    },
    fetchEmployeesFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },

    // ✅ new reducers for supervisorName
    fetchSupervisorNameStart(state) {
      state.loading = true;
      state.error = null;
    },
    fetchSupervisorNameSuccess(state, action: PayloadAction<string | null>) {
      state.supervisorName = action.payload;
      state.loading = false;
    },
    fetchSupervisorNameFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },
  },
});

export const {
  fetchEmployeesStart,
  fetchEmployeesSuccess,
  fetchEmployeesFailure,
  fetchSupervisorNameStart,
  fetchSupervisorNameSuccess,
  fetchSupervisorNameFailure,
} = employeeSlice.actions;

export default employeeSlice.reducer;
