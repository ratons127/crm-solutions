// lib/features/employeesProfile/employeeProfileSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export type PageInfo = {
  currentPage: number;
  lastPage: number;
  perPage: number;
  total: number;
  from?: number | null;
  to?: number | null;
};

export type EmployeeRow = {
  id: number | string;

  firstName?: string | null;
  lastName?: string | null;
  gender?: string | null;

  dateOfJoining?: string | null; // YYYY-MM-DD
  dob?: string | null;           // YYYY-MM-DD

  phone?: string | null;
  email?: string | null;
  nationalId?: string | null;
  photo?: string | null;

  presentAddress?: string | null;
  permanentAddress?: string | null;
  maritalStatus?: string | null;

  emergencyContactName?: string | null;
  emergencyContactRelation?: string | null;
  emergencyContactPhone?: string | null;

  employeeTypeId?: number | string | null;
  departmentId?: number | string | null;
  designationId?: number | string | null;
  supervisorId?: number | string | null;
  workPlaceId?: number | string | null;

  companyId?: number | string | null;
  businessUnitId?: number | string | null;
  workPlaceGroupId?: number | string | null;
  businessUnit?: { id: number | string; name?: string } | string | null;
  workPlaceGroup?: { id: number | string; name?: string } | string | null;
  teamId?: number | string | null;

  roleId?: number | string | null;

  createdDate?: string | null;
  lastModifiedDate?: string | null;
  deletedAt?: string | null;

  fullName?: string | null;
};

type EmployeeState = {
  list: EmployeeRow[];
  loading: boolean;
  error: string | null;

  page: PageInfo | null;

  creating: boolean;
  createError: string | null;

  currentEmployee: EmployeeRow | null;
  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;
};

const initialState: EmployeeState = {
  list: [],
  loading: false,
  error: null,

  page: null,

  creating: false,
  createError: null,

  currentEmployee: null,
  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,
};

const employeeSlice = createSlice({
  name: 'employeesProfile',
  initialState,
  reducers: {
    /* list */
    employeesFetchStart(state) {
      state.loading = true;
      state.error = null;
    },
    employeesFetchSuccess(
      state,
      action: PayloadAction<{ list: EmployeeRow[]; page: PageInfo | null }>
    ) {
      state.list = action.payload.list;
      state.page = action.payload.page;
      state.loading = false;
    },
    employeesFetchFailure(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
    clearEmployees(state) {
      state.list = [];
      state.page = null;
      state.error = null;
    },

    /* create */
    employeeCreateStart(state) {
      state.creating = true;
      state.createError = null;
    },
    employeeCreateSuccess(state, action: PayloadAction<EmployeeRow>) {
      state.creating = false;
      state.list = [action.payload, ...state.list];
      if (state.page) state.page.total = Math.max(0, (state.page.total ?? 0) + 1);
      state.currentEmployee = action.payload;
    },
    employeeCreateFailure(state, action: PayloadAction<string>) {
      state.creating = false;
      state.createError = action.payload;
    },

    /* show */
    employeeShowStart(_state) {},
    employeeShowSuccess(state, action: PayloadAction<EmployeeRow>) {
      state.currentEmployee = action.payload;
    },
    employeeShowFailure(state, action: PayloadAction<string>) {
      state.currentEmployee = null;
      state.error = action.payload;
    },

    /* update */
    employeeUpdateStart(state) {
      state.updating = true;
      state.updateError = null;
    },
    employeeUpdateSuccess(state, action: PayloadAction<EmployeeRow>) {
      state.updating = false;
      state.currentEmployee = action.payload;
      const idx = state.list.findIndex(r => String(r.id) === String(action.payload.id));
      if (idx >= 0) state.list[idx] = action.payload;
    },
    employeeUpdateFailure(state, action: PayloadAction<string>) {
      state.updating = false;
      state.updateError = action.payload;
    },

    /* delete */
    employeeDeleteStart(state) {
      state.deleting = true;
      state.deleteError = null;
    },
    employeeDeleteSuccess(state, action: PayloadAction<string | number>) {
      state.deleting = false;
      const before = state.list.length;
      state.list = state.list.filter(r => String(r.id) !== String(action.payload));
      if (state.page && state.list.length < before) {
        state.page.total = Math.max(0, (state.page.total ?? 0) - 1);
      }
      if (state.currentEmployee && String(state.currentEmployee.id) === String(action.payload)) {
        state.currentEmployee = null;
      }
    },
    employeeDeleteFailure(state, action: PayloadAction<string>) {
      state.deleting = false;
      state.deleteError = action.payload;
    },
  },
});

export const {
  employeesFetchStart,
  employeesFetchSuccess,
  employeesFetchFailure,
  clearEmployees,
  employeeCreateStart,
  employeeCreateSuccess,
  employeeCreateFailure,
  employeeShowStart,
  employeeShowSuccess,
  employeeShowFailure,
  employeeUpdateStart,
  employeeUpdateSuccess,
  employeeUpdateFailure,
  employeeDeleteStart,
  employeeDeleteSuccess,
  employeeDeleteFailure,
} = employeeSlice.actions;

export default employeeSlice.reducer;
export type { EmployeeState };
