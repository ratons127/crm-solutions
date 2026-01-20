import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/* ---------- Types ---------- */

export type WorkplaceStatus = 'ACTIVE' | 'INACTIVE' | string;

export type WorkplaceGroupRef = {
  id: number | string;
  name?: string | null;
};

export type WorkplaceRow = {
  id: number | string;
  workplaceGroupId?: number | string | null;
  workplaceGroup?: WorkplaceGroupRef | null;

  name: string;
  code?: string | null;
  address?: string | null;
  description?: string | null;
  status?: WorkplaceStatus | null;

  createdDate?: string | null;
  lastModifiedDate?: string | null;
  deletedAt?: string | null;
};

export type PageInfo = {
  currentPage: number;
  lastPage: number;
  perPage: number;
  total: number;
  from?: number | null;
  to?: number | null;
};

type WorkplaceState = {
  list: WorkplaceRow[];
  loading: boolean;
  error: string | null;

  page: PageInfo | null;

  creating: boolean;
  createError: string | null;

  currentWorkplace: WorkplaceRow | null;
  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;
};

const initialState: WorkplaceState = {
  list: [],
  loading: false,
  error: null,

  page: null,

  creating: false,
  createError: null,

  currentWorkplace: null,
  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,
};

/* ---------- Slice ---------- */

const workplaceSlice = createSlice({
  name: 'workplaces',
  initialState,
  reducers: {
    /* List */
    workplaceFetchStart(state) {
      state.loading = true;
      state.error = null;
    },
    workplaceFetchSuccess(
      state,
      action: PayloadAction<{ list: WorkplaceRow[]; page: PageInfo | null }>
    ) {
      state.loading = false;
      state.list = action.payload.list;
      state.page = action.payload.page;
    },
    workplaceFetchFailure(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
    clearWorkplaceList(state) {
      state.list = [];
      state.page = null;
      state.error = null;
    },

    /* Create */
    workplaceCreateStart(state) {
      state.creating = true;
      state.createError = null;
    },
    workplaceCreateSuccess(state, action: PayloadAction<WorkplaceRow>) {
      state.creating = false;
      state.list = [action.payload, ...state.list];
      if (state.page) state.page.total += 1;
    },
    workplaceCreateFailure(state, action: PayloadAction<string>) {
      state.creating = false;
      state.createError = action.payload;
    },

    /* Show one */
    workplaceShowStart(_state) {},
    workplaceShowSuccess(state, action: PayloadAction<WorkplaceRow>) {
      state.currentWorkplace = action.payload;
    },
    workplaceShowFailure(state, action: PayloadAction<string>) {
      state.currentWorkplace = null;
      state.error = action.payload;
    },

    /* Update */
    workplaceUpdateStart(state) {
      state.updating = true;
      state.updateError = null;
    },
    workplaceUpdateSuccess(state, action: PayloadAction<WorkplaceRow>) {
      state.updating = false;
      state.currentWorkplace = action.payload;
      const idx = state.list.findIndex(
        r => String(r.id) === String(action.payload.id)
      );
      if (idx >= 0) state.list[idx] = action.payload;
    },
    workplaceUpdateFailure(state, action: PayloadAction<string>) {
      state.updating = false;
      state.updateError = action.payload;
    },

    /* Delete */
    workplaceDeleteStart(state) {
      state.deleting = true;
      state.deleteError = null;
    },
    workplaceDeleteSuccess(state, action: PayloadAction<string>) {
      state.deleting = false;
      const before = state.list.length;
      state.list = state.list.filter(r => String(r.id) !== String(action.payload));
      if (state.page && state.list.length < before) {
        state.page.total = Math.max(0, state.page.total - 1);
      }
      if (
        state.currentWorkplace &&
        String(state.currentWorkplace.id) === String(action.payload)
      ) {
        state.currentWorkplace = null;
      }
    },
    workplaceDeleteFailure(state, action: PayloadAction<string>) {
      state.deleting = false;
      state.deleteError = action.payload;
    },
  },
});

export const {
  workplaceFetchStart,
  workplaceFetchSuccess,
  workplaceFetchFailure,
  clearWorkplaceList,

  workplaceCreateStart,
  workplaceCreateSuccess,
  workplaceCreateFailure,

  workplaceShowStart,
  workplaceShowSuccess,
  workplaceShowFailure,

  workplaceUpdateStart,
  workplaceUpdateSuccess,
  workplaceUpdateFailure,

  workplaceDeleteStart,
  workplaceDeleteSuccess,
  workplaceDeleteFailure,
} = workplaceSlice.actions;

export default workplaceSlice.reducer;
export type { WorkplaceState };
