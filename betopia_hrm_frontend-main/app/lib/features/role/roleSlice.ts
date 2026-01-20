import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export type RolePermission = {
  id: number | string;
  name: string;
  guardName?: string | null;
};

export type RoleRow = {
  id: number | string;
  name: string;
  guardName?: string | null;
  level?: number | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  permissions: RolePermission[];
};

type PageInfo = {
  currentPage: number;
  lastPage: number;
  perPage: number;
  total: number;
  from?: number | null;
  to?: number | null;
};

type RolesState = {
  list: RoleRow[];
  loading: boolean;
  error: string | null;

  page: PageInfo | null;

  creating: boolean;
  createError: string | null;

  // NEW: single role + edit/delete states
  currentRole: RoleRow | null;
  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;

  /** ✅ NEW: for /roles/all dropdown usage */
  allRoles: RoleRow[];
  allRolesLoading: boolean;
  allRolesError: string | null;
};

const initialState: RolesState = {
  list: [],
  loading: false,
  error: null,

  page: null,

  creating: false,
  createError: null,

  // NEW
  currentRole: null,
  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,

  allRoles: [],
  allRolesLoading: false,
  allRolesError: null,
};

const rolesSlice = createSlice({
  name: 'roles',
  initialState,
  reducers: {
    /* --------- list --------- */
    rolesFetchStart(state) {
      state.loading = true;
      state.error = null;
    },
    rolesFetchSuccess(
      state,
      action: PayloadAction<{ list: RoleRow[]; page: PageInfo | null }>
    ) {
      state.list = action.payload.list;
      state.page = action.payload.page;
      state.loading = false;
    },
    rolesFetchFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },
    clearRoles(state) {
      state.list = [];
      state.page = null;
      state.error = null;
    },

    /* --------- create --------- */
    roleCreateStart(state) {
      state.creating = true;
      state.createError = null;
    },
    roleCreateSuccess(state, action: PayloadAction<RoleRow>) {
      state.creating = false;
      state.list = [action.payload, ...state.list];
      if (state.page) state.page.total += 1;
    },
    roleCreateFailure(state, action: PayloadAction<string>) {
      state.creating = false;
      state.createError = action.payload;
    },

    /* --------- show one (/roles/{id} GET) --------- */
    roleShowStart(_state) {
      // optional spinner if you load single role
    },
    roleShowSuccess(state, action: PayloadAction<RoleRow>) {
      state.currentRole = action.payload;
    },
    roleShowFailure(state, action: PayloadAction<string>) {
      state.currentRole = null;
      state.error = action.payload;
    },

    /* --------- update one (/roles/{id} PUT) --------- */
    roleUpdateStart(state) {
      state.updating = true;
      state.updateError = null;
    },
    roleUpdateSuccess(state, action: PayloadAction<RoleRow>) {
      state.updating = false;
      state.currentRole = action.payload;
      const idx = state.list.findIndex(
        r => String(r.id) === String(action.payload.id)
      );
      if (idx >= 0) state.list[idx] = action.payload;
    },
    roleUpdateFailure(state, action: PayloadAction<string>) {
      state.updating = false;
      state.updateError = action.payload;
    },

    /* --------- delete one (/roles/{id} DELETE) --------- */
    roleDeleteStart(state) {
      state.deleting = true;
      state.deleteError = null;
    },
    roleDeleteSuccess(state, action: PayloadAction<string>) {
      state.deleting = false;
      const before = state.list.length;
      state.list = state.list.filter(
        r => String(r.id) !== String(action.payload)
      );
      if (state.page && state.list.length < before)
        state.page.total = Math.max(0, state.page.total - 1);
      if (
        state.currentRole &&
        String(state.currentRole.id) === String(action.payload)
      ) {
        state.currentRole = null;
      }
    },
    roleDeleteFailure(state, action: PayloadAction<string>) {
      state.deleting = false;
      state.deleteError = action.payload;
    },

    /* ✅ --------- allRoles (no pagination) --------- */
    rolesAllFetchStart(state) {
      state.allRolesLoading = true;
      state.allRolesError = null;
    },
    rolesAllFetchSuccess(state, action: PayloadAction<RoleRow[]>) {
      state.allRoles = action.payload;
      state.allRolesLoading = false;
    },
    rolesAllFetchFailure(state, action: PayloadAction<string>) {
      state.allRolesError = action.payload;
      state.allRolesLoading = false;
    },
  },
});

export const {
  rolesFetchStart,
  rolesFetchSuccess,
  rolesFetchFailure,
  clearRoles,

  roleCreateStart,
  roleCreateSuccess,
  roleCreateFailure,

  roleShowStart,
  roleShowSuccess,
  roleShowFailure,

  roleUpdateStart,
  roleUpdateSuccess,
  roleUpdateFailure,

  roleDeleteStart,
  roleDeleteSuccess,
  roleDeleteFailure,

  rolesAllFetchStart,
  rolesAllFetchSuccess,
  rolesAllFetchFailure,
} = rolesSlice.actions;

export default rolesSlice.reducer;
