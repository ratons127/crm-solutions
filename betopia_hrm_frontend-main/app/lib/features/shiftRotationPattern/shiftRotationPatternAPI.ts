'use client';

import { baseApi } from '@/services/api/baseAPI';
import { PaginationParams } from '@/lib/types';
import {
  ShiftRotationPattern,
  ShiftRotationPatternRequest,
  ShiftRotationPatternPaginatedResponse,
} from '@/lib/types/shiftRotationPattern';

const routePath = 'shift-rotation-pattern';

export const shiftRotationPatternAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    /* ------------ Create Shift Rotation Pattern ------------ */
    createShiftRotationPattern: builder.mutation<
      any,
      Partial<ShiftRotationPatternRequest>
    >({
      query: (body) => ({
        method: 'POST',
        url: `/v1/${routePath}`,
        body,
      }),
      invalidatesTags: ['shift-rotation-patterns'],
    }),

    /* ------------ Update Shift Rotation Pattern ------------ */
    updateShiftRotationPattern: builder.mutation<
      any,
      { id: number; data: ShiftRotationPatternRequest }
    >({
      query: ({ id, data }) => ({
        method: 'PUT',
        url: `/v1/${routePath}/${id}`,
        body: data,
      }),
      invalidatesTags: ['shift-rotation-patterns'],
    }),

    /* ------------ Delete Shift Rotation Pattern ------------ */
    deleteShiftRotationPattern: builder.mutation<any, { id: number }>({
      query: ({ id }) => ({
        method: 'DELETE',
        url: `/v1/${routePath}/${id}`,
      }),
      invalidatesTags: ['shift-rotation-patterns'],
    }),

    /* ------------ Get Single Shift Rotation Pattern ------------ */
    getShiftRotationPatternById: builder.query<ShiftRotationPattern, number>({
      query: (id) => ({
        method: 'GET',
        url: `/v1/${routePath}/${id}`,
      }),
      providesTags: ['shift-rotation-patterns'],
    }),

    /* ------------ Get Paginated Shift Rotation Pattern List ------------ */
    getPaginatedShiftRotationPatterns: builder.query<
      ShiftRotationPatternPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => ({
        method: 'GET',
        url: `/v1/${routePath}`,
        params: { page, perPage, sortDirection },
      }),
      providesTags: ['shift-rotation-patterns'],
    }),
  }),
});

export const {
  useCreateShiftRotationPatternMutation,
  useUpdateShiftRotationPatternMutation,
  useDeleteShiftRotationPatternMutation,
  useGetShiftRotationPatternByIdQuery,
  useGetPaginatedShiftRotationPatternsQuery,
} = shiftRotationPatternAPI;
