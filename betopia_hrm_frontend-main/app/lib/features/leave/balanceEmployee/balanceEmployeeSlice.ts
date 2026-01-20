import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { useAppSelector } from '../../../hooks';
import { LeaveBalanceEmployee } from '../../../types/leave';

const slice = createSlice({
  name: 'leaveBalanceEmployee',
  initialState: {
    list: [],
    item: null,
  } as {
    list: LeaveBalanceEmployee[];
    item: null | LeaveBalanceEmployee;
  },
  reducers: {
    setList: (state, action: PayloadAction<LeaveBalanceEmployee[]>) => {
      state.list = action.payload;
    },
    setItem: (state, action: PayloadAction<LeaveBalanceEmployee>) => {
      state.item = action.payload;
    },
    addItemInList: (state, action: PayloadAction<LeaveBalanceEmployee>) => {
      state.list = [action.payload, ...state.list];
    },
    removeItemFromList: (
      state,
      action: PayloadAction<LeaveBalanceEmployee['id']>
    ) => {
      state.list = state.list.filter(x => x.id !== action.payload);
    },
    updateItemInList: (state, action: PayloadAction<LeaveBalanceEmployee>) => {
      state.list = state.list.map(x => {
        if (x.id === action.payload.id) return action.payload;
        return x;
      });
    },
  },
});

export const leaveBalanceEmployeeReducer = slice.reducer;
export const leaveBalanceEmployeeActions = slice.actions;
export const useLeaveBalanceEmployee = () =>
  useAppSelector(state => state.leaveBalanceEmployee);
