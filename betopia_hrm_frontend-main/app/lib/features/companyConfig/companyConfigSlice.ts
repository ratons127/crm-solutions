// lib/features/company/companySlice.ts
import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import type { RootState } from '../../store';
import {
  listCompanies,
  showCompany,
  createCompany,
  updateCompany,
  deleteCompany,
  getAllZones,
  getAllCurrencies,
  type CompanyRow,
  type ListParams,
  type CreateCompanyPayload,
  type UpdateCompanyPayload,
  type SelectOption,
} from './companyConfigApi';

type PaginationState = {
  page: number;
  size: number;
  total: number;
  lastPage: number;
};

type CompanyState = {
  items: CompanyRow[];
  pagination: PaginationState;
  loadingList: boolean;
  loadingCreate: boolean;
  loadingUpdate: boolean;
  loadingDelete: boolean;
  loadingShow: boolean;
  current?: CompanyRow | null;
  error?: string | null;
  query: { sort?: string; q?: string };
  zones: SelectOption[];
  currencies: SelectOption[];
  loadingZones: boolean;
  loadingCurrencies: boolean;
};

const initialState: CompanyState = {
  items: [],
  pagination: { page: 1, size: 10, total: 0, lastPage: 1 },
  loadingList: false,
  loadingCreate: false,
  loadingUpdate: false,
  loadingDelete: false,
  loadingShow: false,
  current: null,
  error: null,
  query: { sort: undefined, q: '' },
  zones: [],
  currencies: [],
  loadingZones: false,
  loadingCurrencies: false,
};

/* ---------------- Thunks ---------------- */
export const fetchCompanies = createAsyncThunk(
  'companies/fetch',
  async (params: ListParams, { rejectWithValue }) => {
    try {
      const res = await listCompanies(params);
      return { res, params };
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to fetch companies');
    }
  }
);

export const fetchCompany = createAsyncThunk(
  'companies/show',
  async (id: number | string, { rejectWithValue }) => {
    try {
      return await showCompany(id);
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to fetch company');
    }
  }
);

export const createCompanyThunk = createAsyncThunk(
  'companies/create',
  async (payload: CreateCompanyPayload, { rejectWithValue }) => {
    try {
      // createCompany now uploads the logo if provided and returns the fresh record
      return await createCompany(payload);
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to create company');
    }
  }
);

export const updateCompanyThunk = createAsyncThunk(
  'companies/update',
  async (payload: UpdateCompanyPayload, { rejectWithValue }) => {
    try {
      return await updateCompany(payload);
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to update company');
    }
  }
);

export const deleteCompanyThunk = createAsyncThunk(
  'companies/delete',
  async (id: number | string, { rejectWithValue }) => {
    try {
      await deleteCompany(id);
      return id;
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to delete company');
    }
  }
);

export const fetchAllZonesThunk = createAsyncThunk(
  'companies/fetchAllZones',
  async (_, { rejectWithValue }) => {
    try {
      return await getAllZones();
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to fetch timezones');
    }
  }
);

export const fetchAllCurrenciesThunk = createAsyncThunk(
  'companies/fetchAllCurrencies',
  async (_, { rejectWithValue }) => {
    try {
      return await getAllCurrencies();
    } catch (e: any) {
      return rejectWithValue(e?.response?.data?.message || 'Failed to fetch currencies');
    }
  }
);

/* ---------------- Slice ---------------- */
const companySlice = createSlice({
  name: 'companies',
  initialState,
  reducers: {
    setQuery(state, action: PayloadAction<{ q?: string; sort?: string }>) {
      state.query = { ...state.query, ...action.payload };
    },
    resetCurrent(state) {
      state.current = null;
      state.error = null;
    },
  },
  extraReducers: builder => {
    // LIST
    builder.addCase(fetchCompanies.pending, (state) => {
      state.loadingList = true;
      state.error = null;
    });
    builder.addCase(fetchCompanies.fulfilled, (state, { payload }) => {
      state.loadingList = false;
      const { res, params } = payload as {
        res: { data: CompanyRow[]; links?: any; meta?: any };
        params: ListParams;
      };
      state.items = res.data || [];
      const meta = res.meta || {};
      state.pagination.page = meta.currentPage ?? params.page ?? 1;
      state.pagination.size = meta.perPage ?? params.size ?? state.pagination.size;
      state.pagination.total = meta.total ?? state.items.length;
      state.pagination.lastPage =
        meta.lastPage ?? Math.max(1, Math.ceil(state.pagination.total / state.pagination.size));
      if (params.sort !== undefined) state.query.sort = params.sort;
      if (params.q !== undefined) state.query.q = params.q;
    });
    builder.addCase(fetchCompanies.rejected, (state, action) => {
      state.loadingList = false;
      state.error = String(action.payload || action.error.message || 'Failed to fetch companies');
    });

    // SHOW
    builder.addCase(fetchCompany.pending, (state) => {
      state.loadingShow = true;
      state.error = null;
    });
    builder.addCase(fetchCompany.fulfilled, (state, { payload }) => {
      state.loadingShow = false;
      state.current = payload || null;
    });
    builder.addCase(fetchCompany.rejected, (state, action) => {
      state.loadingShow = false;
      state.error = String(action.payload || action.error.message || 'Failed to fetch company');
    });

    // CREATE
    builder.addCase(createCompanyThunk.pending, (state) => {
      state.loadingCreate = true;
      state.error = null;
    });
    builder.addCase(createCompanyThunk.fulfilled, (state, { payload }) => {
      state.loadingCreate = false;
      const created = payload as CompanyRow;
      // place new item at top if you're on first page
      if (state.pagination.page === 1) {
        state.items = [created, ...state.items];
        state.pagination.total += 1;
      }
      state.current = created;
    });
    builder.addCase(createCompanyThunk.rejected, (state, action) => {
      state.loadingCreate = false;
      state.error = String(action.payload || action.error.message || 'Failed to create company');
    });

    // UPDATE
    builder.addCase(updateCompanyThunk.pending, (state) => {
      state.loadingUpdate = true;
      state.error = null;
    });
    builder.addCase(updateCompanyThunk.fulfilled, (state, { payload }) => {
      state.loadingUpdate = false;
      const updated = payload as CompanyRow;
      const idx = state.items.findIndex(x => x.id === updated.id);
      if (idx >= 0) state.items[idx] = updated;
      if (state.current?.id === updated.id) state.current = updated;
    });
    builder.addCase(updateCompanyThunk.rejected, (state, action) => {
      state.loadingUpdate = false;
      state.error = String(action.payload || action.error.message || 'Failed to update company');
    });

    // DELETE
    builder.addCase(deleteCompanyThunk.pending, (state) => {
      state.loadingDelete = true;
      state.error = null;
    });
    builder.addCase(deleteCompanyThunk.fulfilled, (state, { payload }) => {
      state.loadingDelete = false;
      const id = payload as number | string;
      state.items = state.items.filter(x => String(x.id) !== String(id));
      state.pagination.total = Math.max(0, state.pagination.total - 1);
      if (state.current && String(state.current.id) === String(id)) {
        state.current = null;
      }
    });
    builder.addCase(deleteCompanyThunk.rejected, (state, action) => {
      state.loadingDelete = false;
      state.error = String(action.payload || action.error.message || 'Failed to delete company');
    });

    // ZONES
    builder.addCase(fetchAllZonesThunk.pending, (state) => {
      state.loadingZones = true;
    });
    builder.addCase(fetchAllZonesThunk.fulfilled, (state, { payload }) => {
      state.loadingZones = false;
      state.zones = (payload as SelectOption[]) ?? [];
    });
    builder.addCase(fetchAllZonesThunk.rejected, (state, action) => {
      state.loadingZones = false;
      state.error = String(action.payload || action.error.message || 'Failed to fetch timezones');
    });

    // CURRENCIES
    builder.addCase(fetchAllCurrenciesThunk.pending, (state) => {
      state.loadingCurrencies = true;
    });
    builder.addCase(fetchAllCurrenciesThunk.fulfilled, (state, { payload }) => {
      state.loadingCurrencies = false;
      state.currencies = (payload as SelectOption[]) ?? [];
    });
    builder.addCase(fetchAllCurrenciesThunk.rejected, (state, action) => {
      state.loadingCurrencies = false;
      state.error = String(action.payload || action.error.message || 'Failed to fetch currencies');
    });
  },
});

export const companyActions = companySlice.actions;
export default companySlice.reducer;

/* ---------------- Selectors ---------------- */
export const selectCompaniesState = (state: RootState) => state.companies;
export const selectCompanyItems = (state: RootState) => state.companies.items;
export const selectCompanyPagination = (state: RootState) => state.companies.pagination;
export const selectCompanyLoadingList = (state: RootState) => state.companies.loadingList;
export const selectCompanyZones = (state: RootState) => state.companies.zones;
export const selectCompanyCurrencies = (state: RootState) => state.companies.currencies;
export const selectCompanyLoadingZones = (state: RootState) => state.companies.loadingZones;
export const selectCompanyLoadingCurrencies = (state: RootState) => state.companies.loadingCurrencies;

