'use client';

import {
  ShiftDefinition,
  ShiftDefinitionRequest,
  ShiftDefinitionPaginatedResponse,
} from '@/lib/types/shiftDefinition';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'shift';

export const shiftDefinitionAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Shift Definition
    createShiftDefinition: builder.mutation<
      any,
      Partial<ShiftDefinitionRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['shift-definitions'],
    }),

    // Update Shift Definition
    updateShiftDefinition: builder.mutation<
      any,
      { id: number; data: ShiftDefinitionRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['shift-definitions'],
    }),

    // Delete Shift Definition
    deleteShiftDefinition: builder.mutation<
      any,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['shift-definitions'],
    }),

    // Get Shift Definition List (all)
    getShiftDefinitionList: builder.query<
      { data: ShiftDefinition[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['shift-definitions'],
    }),

    // Get Paginated Shift Definitions
    getPaginatedShiftDefinitions: builder.query<
      ShiftDefinitionPaginatedResponse,
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
      providesTags: ['shift-definitions'],
    }),

    // End of API
  }),
});

export const {
  useGetShiftDefinitionListQuery,
  useGetPaginatedShiftDefinitionsQuery,
  useCreateShiftDefinitionMutation,
  useUpdateShiftDefinitionMutation,
  useDeleteShiftDefinitionMutation,
} = shiftDefinitionAPI;
