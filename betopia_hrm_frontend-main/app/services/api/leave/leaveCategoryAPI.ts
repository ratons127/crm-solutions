'use client';
import {
  LeaveCategory,
  LeaveCategoryPaginatedResponse,
  LeaveCategoryUpdate,
} from '../../../lib/types/leave';
import { PaginationParams } from '../../../lib/types';
import { baseApi } from '../baseAPI';

export const leaveCategoryAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeaveCategory: builder.mutation<
      LeaveCategory,
      Partial<LeaveCategory>
    >({
      query: body => {
        return {
          method: 'POST',
          url: '/v1/leave-categories',
          body,
        };
      },
      invalidatesTags: ['LeaveCategory'],
    }),
    deleteLeaveCategory: builder.mutation<LeaveCategory, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/leave-categories/${id}`,
        };
      },
      invalidatesTags: ['LeaveCategory'],
    }),
    updateLeaveCategory: builder.mutation<
      LeaveCategory,
      { id: number; data: LeaveCategoryUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/leave-categories/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['LeaveCategory'],
    }),

    // getLeaveCategoryList
    getLeaveCategoryList: builder.query<{ data: LeaveCategory[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: '/v1/leave-categories/all',
        };
      },
      providesTags: ['LeaveCategory'],
    }),

    // Get Paginated Leave Categories
    getPaginatedLeaveCategories: builder.query<
      LeaveCategoryPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: '/v1/leave-categories',
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['LeaveCategory'],
    }),

    // End of API
  }),
});

export const {
  useCreateLeaveCategoryMutation,
  useGetLeaveCategoryListQuery,
  useGetPaginatedLeaveCategoriesQuery,
  useDeleteLeaveCategoryMutation,
  useUpdateLeaveCategoryMutation,
} = leaveCategoryAPI;
