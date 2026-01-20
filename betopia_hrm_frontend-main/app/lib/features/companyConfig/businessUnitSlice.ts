import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export type PageInfo = {
  currentPage: number;
  lastPage: number;
  perPage: number;
  total: number;
  from?: number | null;
  to?: number | null;
};

export type BusinessUnitRow = {
  id: number | string;
  companyId: number | string | null;
  companyName?: string | null;
  companyShortName?: string | null;
  companyCode?: string | null;

  name: string;
  code?: string | null;                     // << align with API
  description?: string | null;
  status?: 'ACTIVE' | 'INACTIVE' | null;    // << align with API

  createdDate?: string | null;
  lastModifiedDate?: string | null;
  deletedAt?: string | null;
};

type BusinessUnitState = {
  list: BusinessUnitRow[];
  loading: boolean;
  error: string | null;

  page: PageInfo | null;

  creating: boolean;
  createError: string | null;

  currentUnit: BusinessUnitRow | null;
  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;
};

const initialState: BusinessUnitState = {
  list: [],
  loading: false,
  error: null,

  page: null,

  creating: false,
  createError: null,

  currentUnit: null,
  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,
};

const businessUnitSlice = createSlice({
  name: 'businessUnit',
  initialState,
  reducers: {
    /* --------- list --------- */
    unitsFetchStart(state) {
      state.loading = true;
      state.error = null;
    },
    unitsFetchSuccess(
      state,
      action: PayloadAction<{ list: BusinessUnitRow[]; page: PageInfo | null }>
    ) {
      state.list = action.payload.list;
      state.page = action.payload.page;
      state.loading = false;
    },
    unitsFetchFailure(state, action: PayloadAction<string>) {
      state.loading = false;
      state.error = action.payload;
    },
    clearUnits(state) {
      state.list = [];
      state.page = null;
      state.error = null;
    },

    /* --------- create --------- */
    unitCreateStart(state) {
      state.creating = true;
      state.createError = null;
    },
    unitCreateSuccess(state, action: PayloadAction<BusinessUnitRow>) {
      state.creating = false;
      state.list = [action.payload, ...state.list];
      if (state.page) state.page.total += 1;
    },
    unitCreateFailure(state, action: PayloadAction<string>) {
      state.creating = false;
      state.createError = action.payload;
    },

    /* --------- show one --------- */
    unitShowStart(_state) {},
    unitShowSuccess(state, action: PayloadAction<BusinessUnitRow>) {
      state.currentUnit = action.payload;
    },
    unitShowFailure(state, action: PayloadAction<string>) {
      state.currentUnit = null;
      state.error = action.payload;
    },

    /* --------- update --------- */
    unitUpdateStart(state) {
      state.updating = true;
      state.updateError = null;
    },
    unitUpdateSuccess(state, action: PayloadAction<BusinessUnitRow>) {
      state.updating = false;
      state.currentUnit = action.payload;
      const idx = state.list.findIndex(
        r => String(r.id) === String(action.payload.id)
      );
      if (idx >= 0) state.list[idx] = action.payload;
    },
    unitUpdateFailure(state, action: PayloadAction<string>) {
      state.updating = false;
      state.updateError = action.payload;
    },

    /* --------- delete --------- */
    unitDeleteStart(state) {
      state.deleting = true;
      state.deleteError = null;
    },
    unitDeleteSuccess(state, action: PayloadAction<string>) {
      state.deleting = false;
      const before = state.list.length;
      state.list = state.list.filter(
        r => String(r.id) !== String(action.payload)
      );
      if (state.page && state.list.length < before)
        state.page.total = Math.max(0, state.page.total - 1);
      if (
        state.currentUnit &&
        String(state.currentUnit.id) === String(action.payload)
      ) {
        state.currentUnit = null;
      }
    },
    unitDeleteFailure(state, action: PayloadAction<string>) {
      state.deleting = false;
      state.deleteError = action.payload;
    },
  },
});

export const {
  unitsFetchStart,
  unitsFetchSuccess,
  unitsFetchFailure,
  clearUnits,

  unitCreateStart,
  unitCreateSuccess,
  unitCreateFailure,

  unitShowStart,
  unitShowSuccess,
  unitShowFailure,

  unitUpdateStart,
  unitUpdateSuccess,
  unitUpdateFailure,

  unitDeleteStart,
  unitDeleteSuccess,
  unitDeleteFailure,
} = businessUnitSlice.actions;

export default businessUnitSlice.reducer;
export type { BusinessUnitState };
