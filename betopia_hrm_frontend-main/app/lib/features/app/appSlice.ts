import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ReactNode } from 'react';
import { AlertInfo } from '../../../components/common/Alert';
import { useAppSelector } from '../../hooks';
import { User } from '../../types/auth';

interface AppState {
  auth: {
    user: User | null;
    refreshToken: string | null;
    accessToken: string | null;
  };
  isLoading: boolean;
  alertInfo: AlertInfo | null;
  modalComponent: ReactNode | null;
}

const initialState: AppState = {
  auth: {
    accessToken: null,
    refreshToken: null,
    user: null,
  },
  alertInfo: null,
  isLoading: false,
  modalComponent: null,
};

const appSlice = createSlice({
  name: 'AppSlice',
  initialState,
  reducers: {
    setAuth: (state, action: PayloadAction<AppState['auth']>) => {
      state.auth = action.payload;
    },
    clearAuth: state => {
      state.auth = { user: null, accessToken: null, refreshToken: null };
    },

    showAlert: (state, action: PayloadAction<AlertInfo>) => {
      state.alertInfo = action.payload;
    },
    hideAlert: state => {
      state.alertInfo = null;
    },

    showModal: (state, action: PayloadAction<ReactNode>) => {
      state.modalComponent = action.payload;
    },
    hideModal: state => {
      state.modalComponent = null;
    },

    setIsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
  },
});

export const appReducer = appSlice.reducer;
export const useAppState = () => useAppSelector(state => state.app);
export const {
  showAlert,
  setIsLoading,
  hideAlert,
  hideModal,
  showModal,
  setAuth,
  clearAuth,
} = appSlice.actions;
