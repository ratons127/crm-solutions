'use client';

import { Bank, BankRequest, BankPaginatedResponse } from '@/lib/types/banks';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'bank';

export const banksAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Bank
    createBank: builder.mutation<BankRequest, Partial<BankRequest>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['banks'],
    }),
    // Update Bank

    updateBank: builder.mutation<
      BankRequest,
      { id: number; data: BankRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['banks'],
    }),

    // Delete Bank
    deleteBank: builder.mutation<BankRequest, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['banks'],
    }),
    // Get Bank List (all)
    getBankList: builder.query<{ data: Bank[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['banks'],
    }),

    // Get Paginated Banks
    getPaginatedBanks: builder.query<BankPaginatedResponse, PaginationParams>({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['banks'],
    }),

    // End of API
  }),
});

export const {
  useGetBankListQuery,
  useGetPaginatedBanksQuery,
  useCreateBankMutation,
  useUpdateBankMutation,
  useDeleteBankMutation,
} = banksAPI;
