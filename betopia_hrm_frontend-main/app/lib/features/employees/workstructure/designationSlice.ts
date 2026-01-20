import { PayloadAction, createSlice } from '@reduxjs/toolkit';

/* ---------- Types ---------- */

export type PageInfo = {
  currentPage: number;
  lastPage: number;
  perPage: number;
  total: number;
  from: number | null;
  to: number | null;
} | null;

export type DesignationRow = {
  id: number | string;
  name: string;
  description: string | null;
  status: boolean;
  createdDate: string | null;
  lastModifiedDate: string | null;
  deletedAt: string | null;
};

type ListPayload = { list: DesignationRow[]; page: PageInfo };

type SliceState = {
  list: DesignationRow[];
  page: PageInfo;
  loading: boolean;
  error: string | null;
  success: string | null;
  current: DesignationRow | null; // for Show/Edit screen
};

const initialState: SliceState = {
  list: [],
  page: null,
  loading: false,
  error: null,
  success: null,
  current: null,
};

/* ---------- Slice ---------- */

const designationSlice = createSlice({
  name: 'designation',
  initialState,
  reducers: {
    // LIST
    designationsFetchStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    designationsFetchSuccess: (s, a: PayloadAction<ListPayload>) => {
      s.loading = false;
      s.list = a.payload.list;
      s.page = a.payload.page;
    },
    designationsFetchFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // SHOW
    designationShowStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    designationShowSuccess: (s, a: PayloadAction<DesignationRow>) => {
      s.loading = false;
      s.current = a.payload;
    },
    designationShowFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // CREATE
    designationCreateStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    designationCreateSuccess: (s, a: PayloadAction<DesignationRow>) => {
      s.loading = false;
      s.success = 'Designation created';
      s.list = [a.payload, ...s.list];
      s.current = a.payload;
    },
    designationCreateFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // UPDATE
    designationUpdateStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    designationUpdateSuccess: (s, a: PayloadAction<DesignationRow>) => {
      s.loading = false;
      s.success = 'Designation updated';
      s.current = a.payload;
      s.list = s.list.map((x) =>
        String(x.id) === String(a.payload.id) ? a.payload : x
      );
    },
    designationUpdateFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // DELETE
    designationDeleteStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    designationDeleteSuccess: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.success = 'Designation deleted';
      s.list = s.list.filter((x) => String(x.id) !== String(a.payload));
      if (s.current && String(s.current.id) === String(a.payload))
        s.current = null;
    },
    designationDeleteFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },
  },
});

export const {
  designationsFetchStart,
  designationsFetchSuccess,
  designationsFetchFailure,

  designationShowStart,
  designationShowSuccess,
  designationShowFailure,

  designationCreateStart,
  designationCreateSuccess,
  designationCreateFailure,

  designationUpdateStart,
  designationUpdateSuccess,
  designationUpdateFailure,

  designationDeleteStart,
  designationDeleteSuccess,
  designationDeleteFailure,
} = designationSlice.actions;

export default designationSlice.reducer;

// Re-export for convenience in API file
export const designationActions = designationSlice.actions;
export type { SliceState as DesignationState };
