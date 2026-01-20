// src/lib/features/auth/authApi.ts
import { baseApi } from '@/services/api/baseAPI';
import apiClient from '../../../services/apiClient';
import { encryptJSONToLocalStorage, clearSessionEncryptionKey } from '../../utils/secureStorage';
import { AppDispatch } from '../../store';
import { clearAuth, setAuth } from '../app/appSlice';
import {
  logout as authSliceLogout,
  forgotPasswordFailure,
  forgotPasswordStart,
  forgotPasswordSuccess,
  loginFailure,
  loginStart,
  loginSuccess,
  registerFailure,
  registerStart,
  registerSuccess,
  resetPasswordFailure,
  resetPasswordStart,
  resetPasswordSuccess,
} from './authSlice';

interface Credentials {
  identifier: string;
  password: string;
  name?: string;
}

/* =========================
        LOGIN USER
========================= */
export const loginUser =
  (credentials: Credentials) => async (dispatch: AppDispatch) => {
    dispatch(loginStart());
    try {
      const res = await apiClient.post('/auth/login', {
        identifier: credentials.identifier,
        password: credentials.password,
      });

      const {
        token,
        refreshToken,
        user,
        role,
        permissions,
        menus,
        employeeId,
        companyId,
        employeeSerialId,
      } = res.data;

      // Combine user data for simplicity
      const userWithRelations = {
        ...user,
        role,
        permissions,
        menus,
        employeeId,
        companyId,
        employeeSerialId,
      };

      // Persist to localStorage (encrypted)
      // We avoid plaintext fallbacks. If encryption fails, clients should re-login.
      try {
        await encryptJSONToLocalStorage('token', token, { ttlMs: 1000 * 60 * 60 * 8 });
        await encryptJSONToLocalStorage('refreshToken', refreshToken, { ttlMs: 1000 * 60 * 60 * 24 * 7 });
        const { permissions: _omitPerms, menus: _omitMenus, ...sanitizedUser } = userWithRelations as any;
        await encryptJSONToLocalStorage('user', sanitizedUser, { ttlMs: 1000 * 60 * 60 * 8 });
        await encryptJSONToLocalStorage('permissions', permissions || [], { ttlMs: 1000 * 60 * 60 * 8 });
        await encryptJSONToLocalStorage('menus', menus || [], { ttlMs: 1000 * 60 * 60 * 8 });
      } catch {}

      // Update Redux state
      dispatch(
        loginSuccess({
          user: userWithRelations,
          token,
          role,
          permissions,
          menus,
          employeeId,
          companyId,
          employeeSerialId,
        })
      );

      // Sync with global app state for interceptors
      dispatch(
        setAuth({ accessToken: token, refreshToken, user: userWithRelations })
      );
    } catch (err: any) {
      dispatch(
        loginFailure(err?.response?.data?.message || 'Login failed. Try again.')
      );
    }
  };

/* =========================
      REGISTER USER
========================= */
export const registerUser =
  (credentials: Credentials) => async (dispatch: AppDispatch) => {
    dispatch(registerStart());
    try {
      const res = await apiClient.post('/auth/register', {
        name: credentials.name,
        identifier: credentials.identifier,
        password: credentials.password,
      });
      const { token, refreshToken, user } = res.data;

      try {
        await encryptJSONToLocalStorage('token', token, { ttlMs: 1000 * 60 * 60 * 8 });
        await encryptJSONToLocalStorage('refreshToken', refreshToken, { ttlMs: 1000 * 60 * 60 * 24 * 7 });
        await encryptJSONToLocalStorage('user', user, { ttlMs: 1000 * 60 * 60 * 8 });
      } catch {}

      dispatch(setAuth({ accessToken: token, refreshToken, user }));
      dispatch(registerSuccess({ user, token }));
    } catch (err: any) {
      dispatch(
        registerFailure(
          err?.response?.data?.message || 'Registration failed. Try again.'
        )
      );
    }
  };

/* =========================
   FORGOT / RESET PASSWORD
========================= */
export const forgotPassword =
  (email: string) => async (dispatch: AppDispatch) => {
    dispatch(forgotPasswordStart());
    try {
      await apiClient.post('/auth/forgot-password', { email: email });
      dispatch(
        forgotPasswordSuccess(
          'Reset password link sent successfully to your email'
        )
      );
    } catch (err: any) {
      dispatch(
        forgotPasswordFailure(
          err?.response?.data?.message || 'Could not send reset link.'
        )
      );
    }
  };

export const resetPassword =
  (payload: { token: string; newPassword: string; confirmPassword: string }) =>
  async (dispatch: AppDispatch) => {
    dispatch(resetPasswordStart());
    try {
      const res = await apiClient.post('/auth/reset-password', payload);
      const { message } = res.data || {};
      dispatch(
        resetPasswordSuccess(message || 'Your password has been reset.')
      );
      // Return API response so UI can show server messages
      return res.data;
    } catch (err: any) {
      const errorMessage =
        err?.response?.data?.message || 'Could not reset password.';
      dispatch(resetPasswordFailure(errorMessage));
      // Return structured error so UI can show server messages
      return {
        success: false,
        message: errorMessage,
        status: err?.response?.status || 500,
      };
    }
  };

/* =========================
        CHANGE PASSWORD
========================= */
/* =========================
        CHANGE PASSWORD
========================= */
export const changePassword =
  (payload: {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
  }) =>
  async (dispatch: AppDispatch) => {
    dispatch(resetPasswordStart());
    try {
      const res = await apiClient.post('/change-password', payload);

      const { message, success } = res.data;

      if (success) {
        dispatch(
          resetPasswordSuccess(message || 'Password changed successfully.')
        );
      } else {
        dispatch(resetPasswordFailure(message || 'Password change failed.'));
      }

      // ✅ Return the full response so the component can read it
      return res.data;
    } catch (err: any) {
      const errorMessage =
        err?.response?.data?.message || 'Could not change password.';
      dispatch(resetPasswordFailure(errorMessage));

      // ✅ Return structured error to the frontend
      return {
        success: false,
        message: errorMessage,
        status: err?.response?.status || 500,
      };
    }
  };

/* =========================
           LOGOUT
========================= */
export const logoutUser = () => async (dispatch: AppDispatch) => {
  try {
    await apiClient.post('/logout'); // optional
  } catch {
    // ignore
  } finally {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    localStorage.removeItem('permissions');
    localStorage.removeItem('menus');
    try { clearSessionEncryptionKey(); } catch {}

    if (apiClient.defaults.headers?.common?.Authorization) {
      delete apiClient.defaults.headers.common.Authorization;
    }

    dispatch(clearAuth());
    dispatch(baseApi.util.resetApiState());
    dispatch(authSliceLogout());

    if (typeof window !== 'undefined') {
      window.location.replace('/auth/login');
    }
  }
};
