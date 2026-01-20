import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LeaveYearType } from '../../../types/leave';

interface leaveYearState {
  list: LeaveYearType[];
  loading: boolean;
  error: string | null;
}

const initialState: leaveYearState = {
  list: [],
  loading: false,
  error: null,
};

const leaveYearSlice = createSlice({
  name: 'Leave-Group-Type',
  initialState,
  reducers: {
    // get list
    fetchleaveYearStart(state) {
      state.loading = true;
      state.error = null;
    },
    fetchleaveYearSuccess(state, action: PayloadAction<LeaveYearType[]>) {
      state.list = action.payload;
      state.loading = false;
    },
    fetchleaveYearFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },

    // create item
    startCreatingleaveYear(state) {
      state.loading = true;
      state.error = null;
    },
    successCreatingleaveYear(state, action: PayloadAction<LeaveYearType>) {
      state.list = [action.payload, ...state.list];
      state.loading = false;
    },
    failureCreatingleaveYear(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // update item
    startUpdatingleaveYear(state) {
      state.loading = true;
      state.error = null;
    },
    successUpdatingleaveYear(
      state,
      action: PayloadAction<Partial<LeaveYearType>>
    ) {
      // //  console.log(action.payload);
      state.list = state.list.map(x => {
        if (x.id === action.payload.id) {
          return { ...x, ...action.payload };
        }
        return x;
      });
      state.loading = false;
    },
    failureUpdatingleaveYear(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // update item
    startDeletingleaveYear(state) {
      state.loading = true;
      state.error = null;
    },
    successDeletingleaveYear(
      state,
      action: PayloadAction<LeaveYearType['id']>
    ) {
      state.list = state.list.filter(x => x.id !== action.payload);
      state.loading = false;
    },
    failureDeletingleaveYear(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
  },
});

export const {
  fetchleaveYearFailure,
  fetchleaveYearStart,
  fetchleaveYearSuccess,
  //
  failureCreatingleaveYear,
  failureDeletingleaveYear,
  failureUpdatingleaveYear,
  //
  startCreatingleaveYear,
  successUpdatingleaveYear,
  startUpdatingleaveYear,
  //
  successCreatingleaveYear,
  successDeletingleaveYear,
  startDeletingleaveYear,
} = leaveYearSlice.actions;

export const leaveYearReducer = leaveYearSlice.reducer;
