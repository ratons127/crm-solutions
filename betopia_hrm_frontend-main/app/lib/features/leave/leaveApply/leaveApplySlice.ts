// lib/features/leave/leaveApply/leaveApplySlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/** ---------- Types ---------- */
export type LeaveTypeRow = {
  id: number | string;
  name: string;
  code: string;
  active: boolean;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  deletedAt?: string | null;
};

export type LeaveGroupAssignRow = {
  id: number | string;
  leaveGroup?: { name?: string } | null;
  name?: string | null;
};

export type LeaveStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | string;

export type LeaveApplyRow = {
  id: number | string;
  employeeId: number | string;

  leaveType: LeaveTypeRow | null;

  // NEW
  leaveGroupAssign?: LeaveGroupAssignRow | null;
  leaveGroupAssignId?: number | string | null;

  startDate: string;            // "YYYY-MM-DD"
  endDate: string;              // "YYYY-MM-DD"
  daysRequested: number;

  reason?: string | null;
  proofDocumentPath?: string | null;
  justification?: string | null;

  status: LeaveStatus;

  requestedAt?: string | null;
  approvedBy?: number | string | null;
  approvedAt?: string | null;

  createdBy?: number | string | null;
  lastModifiedBy?: number | string | null;
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

type ListPagePair = {
  list: LeaveApplyRow[];
  page: PageInfo | null;
  loading: boolean;
  error: string | null;
};

type LeaveApplyState = {
  global: ListPagePair;
  byEmployee: ListPagePair;

  creating: boolean;
  createError: string | null;

  currentItem: LeaveApplyRow | null;
  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;
};

const baseListPage: ListPagePair = {
  list: [],
  page: null,
  loading: false,
  error: null,
};

const initialState: LeaveApplyState = {
  global: { ...baseListPage },
  byEmployee: { ...baseListPage },

  creating: false,
  createError: null,

  currentItem: null,
  updating: false,
  updateError: null,

  deleting: false,
  deleteError: null,
};

const leaveApplySlice = createSlice({
  name: 'leaveApply',
  initialState,
  reducers: {
    /* --------- GLOBAL list (paginated) --------- */
    leaveFetchStart(state) {
      state.global.loading = true;
      state.global.error = null;
    },
    leaveFetchSuccess(
      state,
      action: PayloadAction<{ list: LeaveApplyRow[]; page: PageInfo | null }>
    ) {
      state.global.list = action.payload.list;
      state.global.page = action.payload.page;
      state.global.loading = false;
    },
    leaveFetchFailure(state, action: PayloadAction<string>) {
      state.global.error = action.payload;
      state.global.loading = false;
    },
    clearLeaveList(state) {
      state.global = { ...baseListPage };
    },

    /* --------- EMPLOYEE list --------- */
    empFetchStart(state) {
      state.byEmployee.loading = true;
      state.byEmployee.error = null;
    },
    empFetchSuccess(
      state,
      action: PayloadAction<{ list: LeaveApplyRow[]; page: PageInfo | null }>
    ) {
      state.byEmployee.list = action.payload.list;
      state.byEmployee.page = action.payload.page;
      state.byEmployee.loading = false;
    },
    empFetchFailure(state, action: PayloadAction<string>) {
      state.byEmployee.error = action.payload;
      state.byEmployee.loading = false;
    },
    clearEmpList(state) {
      state.byEmployee = { ...baseListPage };
    },

    /* --------- create --------- */
    leaveCreateStart(state) {
      state.creating = true;
      state.createError = null;
    },
    leaveCreateSuccess(state, action: PayloadAction<LeaveApplyRow>) {
      state.creating = false;

      state.byEmployee.list = [action.payload, ...state.byEmployee.list];
      state.global.list = [action.payload, ...state.global.list];

      if (state.global.page) state.global.page.total += 1;
    },
    leaveCreateFailure(state, action: PayloadAction<string>) {
      state.creating = false;
      state.createError = action.payload;
    },

    /* --------- show one --------- */
    leaveShowStart(_state) {},
    leaveShowSuccess(state, action: PayloadAction<LeaveApplyRow>) {
      state.currentItem = action.payload;
    },
    leaveShowFailure(state, action: PayloadAction<string>) {
      state.currentItem = null;
      state.global.error = action.payload;
    },

    /* --------- update --------- */
    leaveUpdateStart(state) {
      state.updating = true;
      state.updateError = null;
    },
    leaveUpdateSuccess(state, action: PayloadAction<LeaveApplyRow>) {
      state.updating = false;
      const row = action.payload;
      state.currentItem = row;

      const replaceById = (arr: LeaveApplyRow[]) => {
        const i = arr.findIndex(r => String(r.id) === String(row.id));
        if (i >= 0) arr[i] = row;
      };

      replaceById(state.global.list);
      replaceById(state.byEmployee.list);
    },
    leaveUpdateFailure(state, action: PayloadAction<string>) {
      state.updating = false;
      state.updateError = action.payload;
    },

    /* --------- delete --------- */
    leaveDeleteStart(state) {
      state.deleting = true;
      state.deleteError = null;
    },
    leaveDeleteSuccess(state, action: PayloadAction<string>) {
      state.deleting = false;
      const targetId = String(action.payload);

      const beforeGlobal = state.global.list.length;
      state.global.list = state.global.list.filter(r => String(r.id) !== targetId);
      if (state.global.page && state.global.list.length < beforeGlobal) {
        state.global.page.total = Math.max(0, state.global.page.total - 1);
      }

      state.byEmployee.list = state.byEmployee.list.filter(r => String(r.id) !== targetId);

      if (state.currentItem && String(state.currentItem.id) === targetId) {
        state.currentItem = null;
      }
    },
    leaveDeleteFailure(state, action: PayloadAction<string>) {
      state.deleting = false;
      state.deleteError = action.payload;
    },
  },
});

export const {
  // Global list
  leaveFetchStart,
  leaveFetchSuccess,
  leaveFetchFailure,
  clearLeaveList,

  // Employee list
  empFetchStart,
  empFetchSuccess,
  empFetchFailure,
  clearEmpList,

  // Create
  leaveCreateStart,
  leaveCreateSuccess,
  leaveCreateFailure,

  // Show
  leaveShowStart,
  leaveShowSuccess,
  leaveShowFailure,

  // Update
  leaveUpdateStart,
  leaveUpdateSuccess,
  leaveUpdateFailure,

  // Delete
  leaveDeleteStart,
  leaveDeleteSuccess,
  leaveDeleteFailure,
} = leaveApplySlice.actions;

export default leaveApplySlice.reducer;

/** ---------- Selectors (optional) ---------- */
export const selectLeaveGlobal = (s: any) => s.leaveApply.global;
export const selectLeaveByEmployee = (s: any) => s.leaveApply.byEmployee;
export const selectLeaveCurrent = (s: any) => s.leaveApply.currentItem;
export const selectLeaveCreating = (s: any) => ({
  creating: s.leaveApply.creating,
  error: s.leaveApply.createError,
});
export const selectLeaveUpdating = (s: any) => ({
  updating: s.leaveApply.updating,
  error: s.leaveApply.updateError,
});
export const selectLeaveDeleting = (s: any) => ({
  deleting: s.leaveApply.deleting,
  error: s.leaveApply.deleteError,
});
