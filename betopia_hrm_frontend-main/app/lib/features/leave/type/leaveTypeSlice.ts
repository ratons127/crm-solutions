import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LeaveType } from '../../../types/leave';

interface LeaveTypeState {
  list: LeaveType[];
  loading: boolean;
  error: string | null;
}

const initialState: LeaveTypeState = {
  list: [],
  loading: false,
  error: null,
};

const leaveTypeSlice = createSlice({
  name: 'leave-type',
  initialState,
  reducers: {
    // get list
    fetchLeaveTypeStart(state) {
      state.loading = true;
      state.error = null;
    },
    fetchLeaveTypeSuccess(state, action: PayloadAction<LeaveType[]>) {
      state.list = action.payload;
      state.loading = false;
    },
    fetchLeaveTypeFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },

    // create item
    startCreatingLeaveType(state) {
      state.loading = true;
      state.error = null;
    },
    successCreatingLeaveType(state, action: PayloadAction<LeaveType>) {
      state.list = [action.payload, ...state.list];
      state.loading = false;
    },
    failureCreatingLeaveType(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // update item
    startUpdatingLeaveType(state) {
      state.loading = true;
      state.error = null;
    },
    successUpdatingLeaveType(state, action: PayloadAction<Partial<LeaveType>>) {
      // //  console.log(action.payload);
      state.list = state.list.map(x => {
        if (x.id === action.payload.id) {
          return { ...x, ...action.payload };
        }
        return x;
      });
      state.loading = false;
    },
    failureUpdatingLeaveType(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // update item
    startDeletingLeaveType(state) {
      state.loading = true;
      state.error = null;
    },
    successDeletingLeaveType(state, action: PayloadAction<LeaveType['id']>) {
      state.list = state.list.filter(x => x.id !== action.payload);
      state.loading = false;
    },
    failureDeletingLeaveType(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
  },
});

export const {
  fetchLeaveTypeFailure,
  fetchLeaveTypeStart,
  fetchLeaveTypeSuccess,
  //
  failureCreatingLeaveType,
  failureDeletingLeaveType,
  failureUpdatingLeaveType,
  //
  startCreatingLeaveType,
  successUpdatingLeaveType,
  startUpdatingLeaveType,
  //
  successCreatingLeaveType,
  successDeletingLeaveType,
  startDeletingLeaveType,
} = leaveTypeSlice.actions;

export default leaveTypeSlice.reducer;
