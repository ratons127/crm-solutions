import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export type PageInfo = {
  currentPage: number;
  lastPage: number;
  perPage?: number;
  total: number;
  from?: number | null;
  to?: number | null;
};

export type WorkplaceGroupRow = {
  id: number;
  businessUnitId: number;
  name: string;
  code?: string | null;
  description?: string | null;
  status?: 'ACTIVE' | 'INACTIVE' | string | null;

  // optional audit fields if your backend returns them
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  deletedAt?: string | null;
  createdBy?: number | null;
  lastModifiedBy?: number | null;
};

type WorkplaceGroupState = {
  list: WorkplaceGroupRow[];
  item: WorkplaceGroupRow | null;

  // list (paginated) flags
  loading: boolean;
  error: string | null;
  page: PageInfo | null;

  // create
  creating: boolean;
  createError: string | null;

  // update
  updating: boolean;
  updateError: string | null;

  // delete
  deleting: boolean;
  deleteError: string | null;
};

const initialState: WorkplaceGroupState = {
  list: [],
  item: null,

  loading: false,
  error: null,
  page: null,

  creating: false,
  createError: null,

  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,
};

const slice = createSlice({
  name: 'workplaceGroup',
  initialState,
  reducers: {
    /* -------- list (GET /workplace-group) -------- */
    wgFetchStart(state) {
      state.loading = true;
      state.error = null;
    },
    wgFetchSuccess(
      state,
      action: PayloadAction<{ list: WorkplaceGroupRow[]; page: PageInfo | null }>
    ) {
      state.list = action.payload.list;
      state.page = action.payload.page;
      state.loading = false;
    },
    wgFetchFailure(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
    wgClearList(state) {
      state.list = [];
      state.page = null;
      state.error = null;
    },

    /* -------- show one (GET /workplace-group/{id}) -------- */
    wgShowStart(_state) {},
    wgShowSuccess(state, action: PayloadAction<WorkplaceGroupRow>) {
      state.item = action.payload;
    },
    wgShowFailure(state, action: PayloadAction<string>) {
      state.item = null;
      state.error = action.payload;
    },

    /* -------- create (POST /workplace-group) -------- */
    wgCreateStart(state) {
      state.creating = true;
      state.createError = null;
    },
    wgCreateSuccess(state, action: PayloadAction<WorkplaceGroupRow>) {
      state.creating = false;
      state.list = [action.payload, ...state.list];
      if (state.page) state.page.total += 1;
    },
    wgCreateFailure(state, action: PayloadAction<string>) {
      state.creating = false;
      state.createError = action.payload;
    },

    /* -------- update (PUT /workplace-group/{id}) -------- */
    wgUpdateStart(state) {
      state.updating = true;
      state.updateError = null;
    },
    wgUpdateSuccess(state, action: PayloadAction<WorkplaceGroupRow>) {
      state.updating = false;
      state.item = action.payload;
      const idx = state.list.findIndex(
        r => String(r.id) === String(action.payload.id)
      );
      if (idx >= 0) state.list[idx] = action.payload;
    },
    wgUpdateFailure(state, action: PayloadAction<string>) {
      state.updating = false;
      state.updateError = action.payload;
    },

    /* -------- delete (DELETE /workplace-group/{id}) -------- */
    wgDeleteStart(state) {
      state.deleting = true;
      state.deleteError = null;
    },
    wgDeleteSuccess(state, action: PayloadAction<string>) {
      state.deleting = false;
      const before = state.list.length;
      state.list = state.list.filter(r => String(r.id) !== String(action.payload));
      if (state.page && state.list.length < before)
        state.page.total = Math.max(0, state.page.total - 1);
      if (state.item && String(state.item.id) === String(action.payload)) {
        state.item = null;
      }
    },
    wgDeleteFailure(state, action: PayloadAction<string>) {
      state.deleting = false;
      state.deleteError = action.payload;
    },
  },
});

export const {
  wgFetchStart,
  wgFetchSuccess,
  wgFetchFailure,
  wgClearList,

  wgShowStart,
  wgShowSuccess,
  wgShowFailure,

  wgCreateStart,
  wgCreateSuccess,
  wgCreateFailure,

  wgUpdateStart,
  wgUpdateSuccess,
  wgUpdateFailure,

  wgDeleteStart,
  wgDeleteSuccess,
  wgDeleteFailure,
} = slice.actions;

export default slice.reducer;
export type { WorkplaceGroupState };
