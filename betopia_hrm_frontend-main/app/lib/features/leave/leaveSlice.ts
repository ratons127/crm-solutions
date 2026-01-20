// lib/features/leave/leaveSlice.ts
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';

export const fetchLeave = createAsyncThunk('leave/fetchLeave', async () => {
  const res = await axios.get('/api/leave'); 
  return res.data;
});

const leaveSlice = createSlice({
  name: 'leave',
  initialState: {
    list: [] as any[],
    loading: false,
    error: null as string | null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchLeave.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchLeave.fulfilled, (state, action) => {
        state.loading = false;
        state.list = action.payload;
      })
      .addCase(fetchLeave.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch leave data';
      });
  },
});

export default leaveSlice.reducer;
