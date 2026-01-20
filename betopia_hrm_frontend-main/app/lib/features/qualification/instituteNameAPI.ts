'use client';

import {
  InstituteNameData,
  InstituteNameRequest,
  InstituteNamePaginatedResponse,
} from '@/lib/types/qualification';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'instituteNames';

export const instituteNameAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Institute Name
    createInstituteName: builder.mutation<
      InstituteNameRequest,
      Partial<InstituteNameRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['institute-name'],
    }),
    // Update Institute Name

    updateInstituteName: builder.mutation<
      InstituteNameRequest,
      { id: number; data: InstituteNameRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['institute-name'],
    }),

    // Delete Institute Name
    deleteInstituteName: builder.mutation<InstituteNameData, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['institute-name'],
    }),
    // Get Institute Name (all)
    getInstituteName: builder.query<{ data: InstituteNameData[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['institute-name'],
    }),

    // Get Paginated Institute Names
    getPaginatedInstituteNames: builder.query<
      InstituteNamePaginatedResponse,
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
      providesTags: ['institute-name'],
    }),

    // End of API
  }),
});

export const {
  useCreateInstituteNameMutation,
  useDeleteInstituteNameMutation,
  useGetInstituteNameQuery,
  useGetPaginatedInstituteNamesQuery,
  useUpdateInstituteNameMutation,
} = instituteNameAPI;
