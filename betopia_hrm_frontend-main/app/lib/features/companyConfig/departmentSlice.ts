import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { useAppSelector } from '@/lib/hooks'; 

/** Keep this minimal and flexible to match your backend */
export type DepartmentRow = {
  id: number;
  workplaceId: number;
  workplaceName?: string | null;

  name: string;
  code?: string | null;
  description?: string | null;
  status?: 'ACTIVE' | 'INACTIVE' | string;

  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type PageInfo = {
  currentPage: number;
  lastPage: number;
  total: number;
  perPage?: number;
} | null;

type DepartmentState = {
  list: DepartmentRow[];
  item: DepartmentRow | null;

  page: PageInfo;

  loading: boolean;
  error: string | null;

  creating: boolean;
  createError: string | null;

  updating: boolean;
  updateError: string | null;

  deleting: boolean;
  deleteError: string | null;
};

const initialState: DepartmentState = {
  list: [],
  item: null,

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

const slice = createSlice({
  name: 'department',
  initialState,
  reducers: {
    // list + page
    setList: (state, action: PayloadAction<DepartmentRow[]>) => {
      state.list = action.payload ?? [];
    },
    setPage: (state, action: PayloadAction<PageInfo>) => {
      state.page = action.payload ?? null;
    },

    // single item
    setItem: (state, action: PayloadAction<DepartmentRow | null>) => {
      state.item = action.payload ?? null;
    },

    // mutations on list
    addItemInList: (state, action: PayloadAction<DepartmentRow>) => {
      state.list = [action.payload, ...state.list];
    },
    updateItemInList: (state, action: PayloadAction<DepartmentRow>) => {
      state.list = state.list.map((x) => (x.id === action.payload.id ? action.payload : x));
    },
    removeItemFromList: (state, action: PayloadAction<number>) => {
      state.list = state.list.filter((x) => x.id !== action.payload);
    },

    // flags
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
      if (action.payload) state.error = null;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },

    setCreating: (state, action: PayloadAction<boolean>) => {
      state.creating = action.payload;
      if (action.payload) state.createError = null;
    },
    setCreateError: (state, action: PayloadAction<string | null>) => {
      state.createError = action.payload;
    },

    setUpdating: (state, action: PayloadAction<boolean>) => {
      state.updating = action.payload;
      if (action.payload) state.updateError = null;
    },
    setUpdateError: (state, action: PayloadAction<string | null>) => {
      state.updateError = action.payload;
    },

    setDeleting: (state, action: PayloadAction<boolean>) => {
      state.deleting = action.payload;
      if (action.payload) state.deleteError = null;
    },
    setDeleteError: (state, action: PayloadAction<string | null>) => {
      state.deleteError = action.payload;
    },
  },
});

export const departmentReducer = slice.reducer;
export const departmentActions = slice.actions;

/** Convenience selector hook (matches your pattern) */
export const useDepartment = () =>
  useAppSelector((s) => (s as any).department as DepartmentState);
