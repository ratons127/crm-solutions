import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LeaveGroupType } from '../../../types/leave';

interface leaveGroupState {
  list: LeaveGroupType[];
  loading: boolean;
  error: string | null;
}

const initialState: leaveGroupState = {
  list: [],
  loading: false,
  error: null,
};

const leaveGroupSlice = createSlice({
  name: 'Leave-Group-Type',
  initialState,
  reducers: {
    // get list
    fetchleaveGroupStart(state) {
      state.loading = true;
      state.error = null;
    },
    fetchleaveGroupSuccess(state, action: PayloadAction<LeaveGroupType[]>) {
      state.list = action.payload;
      state.loading = false;
    },
    fetchleaveGroupFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },

    // create item
    startCreatingleaveGroup(state) {
      state.loading = true;
      state.error = null;
    },
    successCreatingleaveGroup(state, action: PayloadAction<LeaveGroupType>) {
      state.list = [action.payload, ...state.list];
      state.loading = false;
    },
    failureCreatingleaveGroup(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // update item
    startUpdatingleaveGroup(state) {
      state.loading = true;
      state.error = null;
    },
    successUpdatingleaveGroup(
      state,
      action: PayloadAction<Partial<LeaveGroupType>>
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
    failureUpdatingleaveGroup(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },

    // update item
    startDeletingleaveGroup(state) {
      state.loading = true;
      state.error = null;
    },
    successDeletingleaveGroup(
      state,
      action: PayloadAction<LeaveGroupType['id']>
    ) {
      state.list = state.list.filter(x => x.id !== action.payload);
      state.loading = false;
    },
    failureDeletingleaveGroup(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
  },
});

export const {
  fetchleaveGroupFailure,
  fetchleaveGroupStart,
  fetchleaveGroupSuccess,
  //
  failureCreatingleaveGroup,
  failureDeletingleaveGroup,
  failureUpdatingleaveGroup,
  //
  startCreatingleaveGroup,
  successUpdatingleaveGroup,
  startUpdatingleaveGroup,
  //
  successCreatingleaveGroup,
  successDeletingleaveGroup,
  startDeletingleaveGroup,
} = leaveGroupSlice.actions;

export const leaveGroupReducer = leaveGroupSlice.reducer;
