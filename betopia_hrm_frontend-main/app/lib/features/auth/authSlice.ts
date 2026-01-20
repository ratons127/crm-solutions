// src/lib/features/auth/authSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { User } from '../../types/auth';

interface AuthState {
  user: User | null;
  token: string | null;
  role: any | null;
  permissions: any[];
  menus: any[];
  employeeId: number | null;
  companyId: number | null;
  employeeSerialId: number | null;
  loading: boolean;
  error: string | null;
  success: string | null;
}

const initialState: AuthState = {
  user: null,
  token: null,
  role: null,
  permissions: [],
  menus: [],
  employeeId: null,
  companyId: null,
  employeeSerialId: null,
  loading: false,
  error: null,
  success: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    // ---------------- LOGIN ----------------
    loginStart(state) {
      state.loading = true;
      state.error = null;
      state.success = null;
    },
    loginSuccess(
      state,
      action: PayloadAction<{
        user: User;
        token: string;
        role?: any;
        permissions?: any[];
        menus?: any[];
        employeeId?: number;
        companyId?: number;
        employeeSerialId?: number;
      }>
    ) {
      const payload = action.payload;
      state.user = payload.user;
      state.token = payload.token;
      state.role = payload.role ?? null;
      state.permissions = payload.permissions ?? [];
      state.menus = payload.menus ?? [];
      state.employeeId = payload.employeeId ?? null;
      state.companyId = payload.companyId ?? null;
      state.employeeSerialId = payload.employeeSerialId ?? null;
      state.loading = false;
      state.error = null;
    },
    loginFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },

    // ---------------- REGISTER ----------------
    registerStart(state) {
      state.loading = true;
      state.error = null;
      state.success = null;
    },
    registerSuccess(state, action: PayloadAction<{ user: User; token: string }>) {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.loading = false;
      state.error = null;
    },
    registerFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },

    // ---------------- PASSWORD MANAGEMENT ----------------
    forgotPasswordStart(state) {
      state.loading = true;
      state.error = null;
      state.success = null;
    },
    forgotPasswordSuccess(state, action: PayloadAction<string>) {
      state.loading = false;
      state.success = action.payload || 'Reset email sent.';
    },
    forgotPasswordFailure(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    resetPasswordStart(state) {
      state.loading = true;
      state.error = null;
      state.success = null;
    },
    resetPasswordSuccess(state, action: PayloadAction<string>) {
      state.loading = false;
      state.success = action.payload || 'Your password has been reset.';
    },
    resetPasswordFailure(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // ---------------- CLEAR / LOGOUT ----------------
    clearAuthMessages(state) {
      state.error = null;
      state.success = null;
    },
    logout(state) {
      state.user = null;
      state.token = null;
      state.role = null;
      state.permissions = [];
      state.menus = [];
      state.employeeId = null;
      state.companyId = null;
      state.employeeSerialId = null;
      state.loading = false;
      state.error = null;
      state.success = null;
    },
  },
});

export const {
  loginStart,
  loginSuccess,
  loginFailure,
  registerStart,
  registerSuccess,
  registerFailure,
  forgotPasswordStart,
  forgotPasswordSuccess,
  forgotPasswordFailure,
  resetPasswordStart,
  resetPasswordSuccess,
  resetPasswordFailure,
  clearAuthMessages,
  logout,
} = authSlice.actions;

export default authSlice.reducer;
