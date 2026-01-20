import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface TeamRow {
  id: number;
  departmentId: number;
  name: string;
  code?: string | null;
  description?: string | null;
  status: string;
}

export interface PageInfo {
  currentPage: number;
  lastPage: number;
  total: number;
}

export interface TeamState {
  list: TeamRow[];
  page: PageInfo | null;
  loading: boolean;
  error: string | null;

  creating: boolean;
  createError: string | null;

  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;
}

const initialState: TeamState = {
  list: [],
  page: null,
  loading: false,
  error: null,

  creating: false,
  createError: null,

  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,
};

const teamSlice = createSlice({
  name: 'team',
  initialState,
  reducers: {
    setLoading(state, action: PayloadAction<boolean>) {
      state.loading = action.payload;
    },
    setError(state, action: PayloadAction<string | null>) {
      state.error = action.payload;
    },
    setList(state, action: PayloadAction<TeamRow[]>) {
      state.list = action.payload;
    },
    setPage(state, action: PayloadAction<PageInfo | null>) {
      state.page = action.payload;
    },

    // CREATE
    setCreating(state, action: PayloadAction<boolean>) {
      state.creating = action.payload;
    },
    setCreateError(state, action: PayloadAction<string | null>) {
      state.createError = action.payload;
    },
    addItemInList(state, action: PayloadAction<TeamRow>) {
      state.list.push(action.payload);
    },

    // UPDATE
    setUpdating(state, action: PayloadAction<boolean>) {
      state.updating = action.payload;
    },
    setUpdateError(state, action: PayloadAction<string | null>) {
      state.updateError = action.payload;
    },
    updateItemInList(state, action: PayloadAction<TeamRow>) {
      const idx = state.list.findIndex((x) => x.id === action.payload.id);
      if (idx >= 0) state.list[idx] = action.payload;
    },

    // DELETE
    setDeleting(state, action: PayloadAction<boolean>) {
      state.deleting = action.payload;
    },
    setDeleteError(state, action: PayloadAction<string | null>) {
      state.deleteError = action.payload;
    },
    removeItemFromList(state, action: PayloadAction<number>) {
      state.list = state.list.filter((x) => x.id !== action.payload);
    },
  },
});

export const teamActions = teamSlice.actions;
export default teamSlice.reducer;
