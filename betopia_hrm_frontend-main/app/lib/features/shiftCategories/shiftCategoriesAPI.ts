'use client';

import { PaginationParams } from '@/lib/types';
import {
  ShiftCategory,
  ShiftCategoryPaginatedResponse,
  ShiftCategoryRequest,
} from '@/lib/types/shiftCategories';
import { baseApi } from '@/services/api/baseAPI';

// const routePath = 'shift-device-category';
const routePath2 = 'shift-categories';

export const shiftCategoriesAPI = baseApi.injectEndpoints({
  // endpoints: builder => ({
  //   // Create Shift Category
  //   createShiftCategory: builder.mutation<
  //     ShiftCategoryRequest,
  //     Partial<ShiftCategoryRequest>
  //   >({
  //     query: body => {
  //       return {
  //         method: 'POST',
  //         url: `/v1/${routePath}`,
  //         body,
  //       };
  //     },
  //     invalidatesTags: ['shift-device-category'],
  //   }),

  //   // Update Shift Category
  //   updateShiftCategory: builder.mutation<
  //     ShiftCategoryRequest,
  //     { id: number; data: ShiftCategoryRequest }
  //   >({
  //     query: ({ id, data }) => {
  //       return {
  //         method: 'PUT',
  //         url: `/v1/${routePath}/${id}`,
  //         body: data,
  //       };
  //     },
  //     invalidatesTags: ['shift-device-category'],
  //   }),

  //   // Delete Shift Category
  //   deleteShiftCategory: builder.mutation<
  //     ShiftCategoryRequest,
  //     { id: number }
  //   >({
  //     query: ({ id }) => {
  //       return {
  //         method: 'DELETE',
  //         url: `/v1/${routePath}/${id}`,
  //       };
  //     },
  //     invalidatesTags: ['shift-device-category'],
  //   }),

  //   // Get Shift Category List (all)
  //   getShiftCategoryList: builder.query<
  //     { data: ShiftCategory[] },
  //     undefined
  //   >({
  //     query() {
  //       return {
  //         method: 'GET',
  //         url: `/v1/${routePath}/all`,
  //       };
  //     },
  //     providesTags: ['shift-device-category'],
  //   }),

  //   // Get Paginated Shift Categories
  //   getPaginatedShiftCategories: builder.query<
  //     ShiftCategoryPaginatedResponse,
  //     PaginationParams
  //   >({
  //     query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
  //       return {
  //         method: 'GET',
  //         url: `/v1/${routePath}`,
  //         params: {
  //           sortDirection,
  //           page,
  //           perPage,
  //         },
  //       };
  //     },
  //     providesTags: ['shift-device-category'],
  //   }),

  //   // End of API
  // }),

  endpoints: builder => ({
    // Create Shift Category
    createShiftCategory: builder.mutation<
      ShiftCategoryRequest,
      Partial<ShiftCategoryRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath2}`,
          body,
        };
      },
      invalidatesTags: [routePath2],
    }),

    // Update Shift Category
    updateShiftCategory: builder.mutation<
      ShiftCategoryRequest,
      { id: number; data: ShiftCategoryRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath2}/${id}`,
          body: data,
        };
      },
      invalidatesTags: [routePath2],
    }),

    // Delete Shift Category
    deleteShiftCategory: builder.mutation<ShiftCategoryRequest, { id: number }>(
      {
        query: ({ id }) => {
          return {
            method: 'DELETE',
            url: `/v1/${routePath2}/${id}`,
          };
        },
        invalidatesTags: [routePath2],
      }
    ),

    // Get Shift Category List (all)
    getShiftCategoryList: builder.query<{ data: ShiftCategory[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath2}/all`,
        };
      },
      providesTags: [routePath2],
    }),

    // Get Paginated Shift Categories
    getPaginatedShiftCategories: builder.query<
      ShiftCategoryPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath2}`,
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: [routePath2],
    }),

    // End of API
  }),
});

export const {
  useGetShiftCategoryListQuery,
  useGetPaginatedShiftCategoriesQuery,
  useCreateShiftCategoryMutation,
  useUpdateShiftCategoryMutation,
  useDeleteShiftCategoryMutation,
} = shiftCategoriesAPI;
