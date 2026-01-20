import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/* ======================= Types ======================= */
export type UserRow = {
  id: number | string;
  name: string;
  email: string;
  phone?: string | null; 
  avatar?: string | null;
  role?: string | null;
  lastLogin?: string | null;
  twoStep?: 'Enabled' | 'Disabled' | string | null;
  joinedDate?: string | null;
};

export type PermissionRow = {
  id: string | number;
  name: string;
  guardName?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  selfHref?: string | null;
};

type PageInfo = {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number; // zero-based
};

type UsersState = {
  /* ---- Users list ---- */
  list: UserRow[];
  loading: boolean;
  error: string | null;

  page: PageInfo | null;
  /* ---- User create ---- */
  creatingUser: boolean;
  createUserError: string | null;

  /* ---- User update/delete ---- */
  updatingUser: boolean;
  updateUserError: string | null;
  deletingUser: boolean;
  deleteUserError: string | null;

  /* ---- Permissions list ---- */
  permissions: PermissionRow[];
  permissionsPage: PageInfo | null;
  permissionsLoading: boolean;
  permissionsError: string | null;

  /* ---- Create permission ---- */
  creatingPermission: boolean;
  createPermissionError: string | null;

  /* ---- Single permission flows (GET/PUT/DELETE) ---- */
  currentPermission: PermissionRow | null;
  permissionViewLoading: boolean;
  permissionViewError: string | null;

  updatingPermission: boolean;
  updatePermissionError: string | null;

  deletingPermission: boolean;
  deletePermissionError: string | null;
};

/* ======================= Initial ======================= */
const initialState: UsersState = {
  /* Users */
  list: [],
  loading: false,
  error: null,
  page: { size: 10, totalElements: 0, totalPages: 0, number: 0 },

  creatingUser: false,
  createUserError: null,

  updatingUser: false,
  updateUserError: null,
  deletingUser: false,
  deleteUserError: null,

  /* Permissions */
  permissions: [],
  permissionsPage: null,
  permissionsLoading: false,
  permissionsError: null,

  creatingPermission: false,
  createPermissionError: null,

  currentPermission: null,
  permissionViewLoading: false,
  permissionViewError: null,

  updatingPermission: false,
  updatePermissionError: null,

  deletingPermission: false,
  deletePermissionError: null,
};

/* ======================= Slice ======================= */
const usersSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {
    /* -------- Users: list -------- */
    usersFetchStart(state) {
      state.loading = true;
      state.error = null;
    },
    usersFetchSuccess(
      state,
      action: PayloadAction<{ list: UserRow[]; page: PageInfo }>
    ) {
      state.loading = false;
      state.list = action.payload.list;
      state.page = action.payload.page;
    },

    usersFetchFailure(state, action: PayloadAction<string>) {
      state.error = action.payload;
      state.loading = false;
    },
    clearUsers(state) {
      state.list = [];
      state.error = null;
    },

    /* -------- Users: create -------- */
    userCreateStart(state) {
      state.creatingUser = true;
      state.createUserError = null;
    },
    userCreateSuccess(state, action: PayloadAction<UserRow>) {
      state.creatingUser = false;
      state.list = [action.payload, ...state.list];
    },
    userCreateFailure(state, action: PayloadAction<string>) {
      state.creatingUser = false;
      state.createUserError = action.payload;
    },

    /* -------- Users: update -------- */
    userUpdateStart(state) {
      state.updatingUser = true;
      state.updateUserError = null;
    },
    userUpdateSuccess(state, action: PayloadAction<UserRow>) {
      state.updatingUser = false;
      const updated = action.payload;
      const idx = state.list.findIndex(
        u => String(u.id) === String(updated.id)
      );
      if (idx >= 0) state.list[idx] = { ...state.list[idx], ...updated };
    },
    userUpdateFailure(state, action: PayloadAction<string>) {
      state.updatingUser = false;
      state.updateUserError = action.payload;
    },

    /* -------- Users: delete -------- */
    userDeleteStart(state) {
      state.deletingUser = true;
      state.deleteUserError = null;
    },
    userDeleteSuccess(state, action: PayloadAction<string>) {
      state.deletingUser = false;
      state.list = state.list.filter(
        u => String(u.id) !== String(action.payload)
      );
    },
    userDeleteFailure(state, action: PayloadAction<string>) {
      state.deletingUser = false;
      state.deleteUserError = action.payload;
    },

    /* -------- Permissions: list -------- */
    permissionsFetchStart(state) {
      state.permissionsLoading = true;
      state.permissionsError = null;
    },
    permissionsFetchSuccess(
      state,
      action: PayloadAction<{ list: PermissionRow[]; page: PageInfo }>
    ) {
      state.permissions = action.payload.list;
      state.permissionsPage = action.payload.page;
      state.permissionsLoading = false;
    },
    permissionsFetchFailure(state, action: PayloadAction<string>) {
      state.permissionsError = action.payload;
      state.permissionsLoading = false;
    },
    clearPermissions(state) {
      state.permissions = [];
      state.permissionsPage = null;
      state.permissionsError = null;
    },

    /* -------- Permissions: create -------- */
    permissionsCreateStart(state) {
      state.creatingPermission = true;
      state.createPermissionError = null;
    },
    permissionsCreateSuccess(state, action: PayloadAction<PermissionRow>) {
      state.creatingPermission = false;
      state.permissions = [action.payload, ...state.permissions];
      if (state.permissionsPage) {
        state.permissionsPage.totalElements += 1;
      }
    },
    permissionsCreateFailure(state, action: PayloadAction<string>) {
      state.creatingPermission = false;
      state.createPermissionError = action.payload;
    },

    /* -------- Single permission: show -------- */
    permissionShowStart(state) {
      state.permissionViewLoading = true;
      state.permissionViewError = null;
    },
    permissionShowSuccess(state, action: PayloadAction<PermissionRow>) {
      state.permissionViewLoading = false;
      state.currentPermission = action.payload;
    },
    permissionShowFailure(state, action: PayloadAction<string>) {
      state.permissionViewLoading = false;
      state.permissionViewError = action.payload;
    },

    /* -------- Single permission: update -------- */
    permissionUpdateStart(state) {
      state.updatingPermission = true;
      state.updatePermissionError = null;
    },
    permissionUpdateSuccess(state, action: PayloadAction<PermissionRow>) {
      state.updatingPermission = false;
      state.currentPermission = action.payload;
      const idx = state.permissions.findIndex(
        p => String(p.id) === String(action.payload.id)
      );
      if (idx >= 0) state.permissions[idx] = action.payload;
    },
    permissionUpdateFailure(state, action: PayloadAction<string>) {
      state.updatingPermission = false;
      state.updatePermissionError = action.payload;
    },

    /* -------- Single permission: delete -------- */
    permissionDeleteStart(state) {
      state.deletingPermission = true;
      state.deletePermissionError = null;
    },
    permissionDeleteSuccess(state, action: PayloadAction<string>) {
      state.deletingPermission = false;

      const before = state.permissions.length;
      state.permissions = state.permissions.filter(
        p => String(p.id) !== String(action.payload)
      );

      if (state.permissionsPage && state.permissions.length < before) {
        state.permissionsPage.totalElements = Math.max(
          0,
          state.permissionsPage.totalElements - 1
        );
      }

      if (
        state.currentPermission &&
        String(state.currentPermission.id) === String(action.payload)
      ) {
        state.currentPermission = null;
      }
    },
    permissionDeleteFailure(state, action: PayloadAction<string>) {
      state.deletingPermission = false;
      state.deletePermissionError = action.payload;
    },
  },
});

/* ======================= Exports ======================= */
export const {
  // Users list
  usersFetchStart,
  usersFetchSuccess,
  usersFetchFailure,
  clearUsers,

  // Users create
  userCreateStart,
  userCreateSuccess,
  userCreateFailure,

  // Users update/delete
  userUpdateStart,
  userUpdateSuccess,
  userUpdateFailure,
  userDeleteStart,
  userDeleteSuccess,
  userDeleteFailure,

  // Permissions list
  permissionsFetchStart,
  permissionsFetchSuccess,
  permissionsFetchFailure,
  clearPermissions,

  // Permissions create
  permissionsCreateStart,
  permissionsCreateSuccess,
  permissionsCreateFailure,

  // Single permission flows
  permissionShowStart,
  permissionShowSuccess,
  permissionShowFailure,

  permissionUpdateStart,
  permissionUpdateSuccess,
  permissionUpdateFailure,

  permissionDeleteStart,
  permissionDeleteSuccess,
  permissionDeleteFailure,
} = usersSlice.actions;

export default usersSlice.reducer;
