import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { useAppSelector } from '../../../hooks';
import { LeavePolicy } from './../../../types/leave';

const slice = createSlice({
  name: 'leavePolicy',
  initialState: {
    list: [],
    item: null,
  } as {
    list: LeavePolicy[];
    item: null | LeavePolicy;
  },
  reducers: {
    setList: (state, action: PayloadAction<LeavePolicy[]>) => {
      state.list = action.payload;
    },
    setItem: (state, action: PayloadAction<LeavePolicy>) => {
      state.item = action.payload;
    },
    addItemInList: (state, action: PayloadAction<LeavePolicy>) => {
      state.list = [action.payload, ...state.list];
    },
    removeItemFromList: (state, action: PayloadAction<LeavePolicy['id']>) => {
      state.list = state.list.filter(x => x.id !== action.payload);
    },
    updateItemInList: (state, action: PayloadAction<LeavePolicy>) => {
      state.list = state.list.map(x => {
        if (x.id === action.payload.id) return action.payload;
        return x;
      });
    },
  },
});

export const leavePolicyReducer = slice.reducer;
export const leavePolicyActions = slice.actions;
export const useLeavePolicy = () =>
  useAppSelector(state => state.leavePolicies);
