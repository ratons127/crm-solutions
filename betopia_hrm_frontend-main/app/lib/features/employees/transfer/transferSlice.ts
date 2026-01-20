import { PayloadAction, createSlice } from '@reduxjs/toolkit';

/* ======================= Types ======================= */
export type TransferRequestType = 'IntraCompany' | 'InterCompany' | string;
export type ApprovalStatus = 'Pending' | 'Approved' | 'Rejected' | string;

export interface TransferRow {
  id: number | string;
  employeeId: number | string;

  requestType: TransferRequestType;

  fromCompanyId: number | string | null;
  toCompanyId: number | string | null;

  fromWorkplaceId: number | string | null;
  toWorkplaceId: number | string | null;

  fromDepartmentId: number | string | null;
  toDepartmentId: number | string | null;

  fromDesignationId: number | string | null;
  toDesignationId: number | string | null;

  reason: string | null;
  effectiveDate: string | null;         // "YYYY-MM-DD"
  approvalStatus: ApprovalStatus | null;
  approvedById: number | string | null;
  approvedAt: string | null;            // ISO date-time
  status: boolean;

  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export type TransferCreatePayload = {
  employeeId: number | string;
  requestType: TransferRequestType;
  fromCompanyId: number | string | null;
  toCompanyId: number | string | null;
  fromWorkplaceId: number | string | null;
  toWorkplaceId: number | string | null;
  fromDepartmentId: number | string | null;
  toDepartmentId: number | string | null;
  fromDesignationId: number | string | null;
  toDesignationId: number | string | null;
  reason: string;
  effectiveDate: string; // "YYYY-MM-DD"
  approvalStatus?: ApprovalStatus; // backend defaults to "Pending" usually
  approvedById?: number | string | null;
  approvedAt?: string | null;
  status: boolean | number;
};

export type TransferUpdatePayload = Partial<TransferCreatePayload>;

type Page = {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number; // 0-based
};

type SliceState = {
  list: TransferRow[];
  page: Page;
  current: TransferRow | null;

  loadingList: boolean;
  loadingCreate: boolean;
  loadingUpdate: boolean;
  loadingDelete: boolean;
  loadingShow: boolean;

  errorList: string | null;
  errorCreate: string | null;
  errorUpdate: string | null;
  errorDelete: string | null;
  errorShow: string | null;
};

const initialState: SliceState = {
  list: [],
  page: { size: 10, totalElements: 0, totalPages: 0, number: 0 },
  current: null,

  loadingList: false,
  loadingCreate: false,
  loadingUpdate: false,
  loadingDelete: false,
  loadingShow: false,

  errorList: null,
  errorCreate: null,
  errorUpdate: null,
  errorDelete: null,
  errorShow: null,
};

/* ======================= Slice ======================= */
const transferSlice = createSlice({
  name: 'transfer',
  initialState,
  reducers: {
    // LIST
    transferFetchStart(state) {
      state.loadingList = true;
      state.errorList = null;
    },
    transferFetchSuccess(
      state,
      action: PayloadAction<{ list: TransferRow[]; page: Page }>
    ) {
      state.loadingList = false;
      state.list = action.payload.list;
      state.page = action.payload.page;
    },
    transferFetchFailure(state, action: PayloadAction<string>) {
      state.loadingList = false;
      state.errorList = action.payload;
    },

    // SHOW
    transferShowStart(state) {
      state.loadingShow = true;
      state.errorShow = null;
    },
    transferShowSuccess(state, action: PayloadAction<TransferRow>) {
      state.loadingShow = false;
      state.current = action.payload;
    },
    transferShowFailure(state, action: PayloadAction<string>) {
      state.loadingShow = false;
      state.errorShow = action.payload;
    },

    // CREATE
    transferCreateStart(state) {
      state.loadingCreate = true;
      state.errorCreate = null;
    },
    transferCreateSuccess(state, action: PayloadAction<TransferRow>) {
      state.loadingCreate = false;
      state.list = [action.payload, ...state.list];
    },
    transferCreateFailure(state, action: PayloadAction<string>) {
      state.loadingCreate = false;
      state.errorCreate = action.payload;
    },

    // UPDATE
    transferUpdateStart(state) {
      state.loadingUpdate = true;
      state.errorUpdate = null;
    },
    transferUpdateSuccess(state, action: PayloadAction<TransferRow>) {
      state.loadingUpdate = false;
      const idx = state.list.findIndex(x => String(x.id) === String(action.payload.id));
      if (idx >= 0) state.list[idx] = action.payload;
      if (state.current && String(state.current.id) === String(action.payload.id)) {
        state.current = action.payload;
      }
    },
    transferUpdateFailure(state, action: PayloadAction<string>) {
      state.loadingUpdate = false;
      state.errorUpdate = action.payload;
    },

    // DELETE
    transferDeleteStart(state) {
      state.loadingDelete = true;
      state.errorDelete = null;
    },
    transferDeleteSuccess(state, action: PayloadAction<string>) {
      state.loadingDelete = false;
      state.list = state.list.filter(x => String(x.id) !== String(action.payload));
      if (state.current && String(state.current.id) === String(action.payload)) {
        state.current = null;
      }
    },
    transferDeleteFailure(state, action: PayloadAction<string>) {
      state.loadingDelete = false;
      state.errorDelete = action.payload;
    },
  },
});

export const {
  transferFetchStart,
  transferFetchSuccess,
  transferFetchFailure,

  transferShowStart,
  transferShowSuccess,
  transferShowFailure,

  transferCreateStart,
  transferCreateSuccess,
  transferCreateFailure,

  transferUpdateStart,
  transferUpdateSuccess,
  transferUpdateFailure,

  transferDeleteStart,
  transferDeleteSuccess,
  transferDeleteFailure,
} = transferSlice.actions;

export default transferSlice.reducer;

/* ======================= Selectors ======================= */
// (optional) example selectors:
export const selectTransfers = (s: any) => s.transfer.list as TransferRow[];
export const selectTransferPage = (s: any) => s.transfer.page as Page;
export const selectTransferCurrent = (s: any) => s.transfer.current as TransferRow | null;
