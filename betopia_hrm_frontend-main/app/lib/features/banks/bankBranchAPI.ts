'use client';

import {
  BankBranch,
  BankBranchRequest,
  BankBranchPaginatedResponse,
} from '@/lib/types/banks';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'bank-branches';

export const bankBranchAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Bank Branch
    createBankBranch: builder.mutation<
      BankBranchRequest,
      Partial<BankBranchRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['bank-branches'],
    }),
    // Update Bank

    updateBankBranch: builder.mutation<
      BankBranchRequest,
      { id: number; data: BankBranchRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['bank-branches'],
    }),

    // Delete Bank
    deleteBankBranch: builder.mutation<BankBranchRequest, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['bank-branches'],
    }),
    // Get Bank Branch List (all)
    getBankBranchList: builder.query<{ data: BankBranch[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['bank-branches'],
    }),

    // Get Paginated Bank Branches
    getPaginatedBankBranches: builder.query<
      BankBranchPaginatedResponse,
      PaginationParams
    >({
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
      providesTags: ['bank-branches'],
    }),

    // End of API
  }),
});

export const {
  useCreateBankBranchMutation,
  useDeleteBankBranchMutation,
  useGetBankBranchListQuery,
  useGetPaginatedBankBranchesQuery,
  useUpdateBankBranchMutation,
} = bankBranchAPI;
