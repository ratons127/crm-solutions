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

export type GradeRow = {
  id: number | string;
  code: string;
  name: string;
  description: string | null;
  status: boolean;
  createdDate: string | null;
  lastModifiedDate: string | null;
  deletedAt: string | null;
};

type ListPayload = { list: GradeRow[]; page: PageInfo };

type SliceState = {
  list: GradeRow[];
  page: PageInfo;
  loading: boolean;
  error: string | null;
  success: string | null;
  current: GradeRow | null;
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

const gradeSlice = createSlice({
  name: 'grade',
  initialState,
  reducers: {
    // LIST
    gradesFetchStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    gradesFetchSuccess: (s, a: PayloadAction<ListPayload>) => {
      s.loading = false;
      s.list = a.payload.list;
      s.page = a.payload.page;
    },
    gradesFetchFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // SHOW
    gradeShowStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    gradeShowSuccess: (s, a: PayloadAction<GradeRow>) => {
      s.loading = false;
      s.current = a.payload;
    },
    gradeShowFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // CREATE
    gradeCreateStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    gradeCreateSuccess: (s, a: PayloadAction<GradeRow>) => {
      s.loading = false;
      s.success = 'Grade created';
      s.list = [a.payload, ...s.list];
      s.current = a.payload;
    },
    gradeCreateFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // UPDATE
    gradeUpdateStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    gradeUpdateSuccess: (s, a: PayloadAction<GradeRow>) => {
      s.loading = false;
      s.success = 'Grade updated';
      s.current = a.payload;
      s.list = s.list.map((x) =>
        String(x.id) === String(a.payload.id) ? a.payload : x
      );
    },
    gradeUpdateFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },

    // DELETE
    gradeDeleteStart: (s) => {
      s.loading = true;
      s.error = null;
      s.success = null;
    },
    gradeDeleteSuccess: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.success = 'Grade deleted';
      s.list = s.list.filter((x) => String(x.id) !== String(a.payload));
      if (s.current && String(s.current.id) === String(a.payload))
        s.current = null;
    },
    gradeDeleteFailure: (s, a: PayloadAction<string>) => {
      s.loading = false;
      s.error = a.payload;
    },
  },
});

export const {
  gradesFetchStart,
  gradesFetchSuccess,
  gradesFetchFailure,

  gradeShowStart,
  gradeShowSuccess,
  gradeShowFailure,

  gradeCreateStart,
  gradeCreateSuccess,
  gradeCreateFailure,

  gradeUpdateStart,
  gradeUpdateSuccess,
  gradeUpdateFailure,

  gradeDeleteStart,
  gradeDeleteSuccess,
  gradeDeleteFailure,
} = gradeSlice.actions;

export default gradeSlice.reducer;

// For API file
export const gradeActions = gradeSlice.actions;
export type { SliceState as GradeState };
