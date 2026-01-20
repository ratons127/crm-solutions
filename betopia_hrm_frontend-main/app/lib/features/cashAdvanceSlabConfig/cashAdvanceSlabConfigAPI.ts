'use client';

import { baseApi } from '@/services/api/baseAPI';

/* ---------- Types ---------- */
export interface CashAdvanceSlabConfigDetail {
  serviceChargeType: 'PERCENTAGE' | 'FIXED';
  serviceChargeAmount: number;
  fromAmount: number;
  toAmount: number;
}

export interface CashAdvanceSlabConfig {
  id: number;
  setupName: string;
  advanceRequestDay: number;
  employeeTypeId: number;
  companyId: number;
  businessUnitId: number;
  workplaceGroupId?: number;
  workplaceId?: number;
  effectiveFromDate: string;
  effectiveToDate: string;
  advancePercent: number;
  serviceChargeType: 'PERCENTAGE' | 'FIXED' | 'RANGE';
  serviceChargeAmount?: number;
  remarks?: string;
  approvedAmountChange: boolean;
  status: boolean;
  cashAdvanceSlabConfigDetails?: CashAdvanceSlabConfigDetail[];
}

/* ---------- Paginated Response ---------- */
export interface PaginatedResponse<T> {
  data: T[];
  meta: {
    currentPage: number;
    from: number;
    lastPage: number;
    perPage: number;
    total: number;
  };
  message: string;
  success: boolean;
  statusCode: number;
}

/* ---------- Route Path ---------- */
const routePath = 'cashAdvanceSlabConfigs';

/* ---------- API Definition ---------- */
export const cashAdvanceSlabConfigAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    // ✅ Get paginated list
    getCashAdvanceSlabConfigList: builder.query<
      PaginatedResponse<CashAdvanceSlabConfig>,
      { page?: number; limit?: number }
    >({
      query: ({ page = 1, limit = 10 }) => ({
        url: `/v1/${routePath}?page=${page}&limit=${limit}`,
        method: 'GET',
      }),
      providesTags: ['cashAdvanceSlabConfig'],
    }),

    // ✅ Get by ID
    getCashAdvanceSlabConfigById: builder.query<
      { data: CashAdvanceSlabConfig },
      number
    >({
      query: (id) => ({
        url: `/v1/${routePath}/${id}`,
        method: 'GET',
      }),
      providesTags: ['cashAdvanceSlabConfig'],
    }),

    // ✅ Create
    createCashAdvanceSlabConfig: builder.mutation<
      { data: CashAdvanceSlabConfig; message: string; success: boolean; status: number },
      Partial<CashAdvanceSlabConfig>
    >({
      query: (body) => ({
        url: `/v1/${routePath}`,
        method: 'POST',
        body,
      }),
      invalidatesTags: ['cashAdvanceSlabConfig'],
    }),

    // ✅ Update
    updateCashAdvanceSlabConfig: builder.mutation<
      { data: CashAdvanceSlabConfig; message: string; success: boolean; status: number },
      Partial<CashAdvanceSlabConfig> & { id: number }
    >({
      query: ({ id, ...body }) => ({
        url: `/v1/${routePath}/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: ['cashAdvanceSlabConfig'],
    }),

    // ✅ Delete
    deleteCashAdvanceSlabConfig: builder.mutation<
      { message: string; success: boolean },
      number
    >({
      query: (id) => ({
        url: `/v1/${routePath}/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['cashAdvanceSlabConfig'],
    }),
  }),
});

/* ---------- Export Hooks ---------- */
export const {
  useGetCashAdvanceSlabConfigListQuery,
  useGetCashAdvanceSlabConfigByIdQuery,
  useCreateCashAdvanceSlabConfigMutation,
  useUpdateCashAdvanceSlabConfigMutation,
  useDeleteCashAdvanceSlabConfigMutation,
} = cashAdvanceSlabConfigAPI;
