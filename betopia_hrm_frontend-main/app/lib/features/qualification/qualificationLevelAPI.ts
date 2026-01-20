'use client';

import {
  EmployeeQualificationLevel,
  EmployeeQualificationLevelRequest,
  QualificationLevelPaginatedResponse,
} from '@/lib/types/qualification';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'qualificationLevels';

export const qualificationLevelAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Employee Qualification Level
    createQualificationLevel: builder.mutation<
      EmployeeQualificationLevelRequest,
      Partial<EmployeeQualificationLevelRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['qualification-level'],
    }),
    // Update Employee Qualification Type

    updateQualificationLevel: builder.mutation<
      EmployeeQualificationLevelRequest,
      { id: number; data: EmployeeQualificationLevelRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['qualification-level'],
    }),

    // Delete Employee Qualification Type
    deleteQualificationLevel: builder.mutation<
      EmployeeQualificationLevel,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['qualification-level'],
    }),
    // Get Employee Qualification Level (all)
    getQualificationLevel: builder.query<
      { data: EmployeeQualificationLevel[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['qualification-level'],
    }),

    // Get Paginated Qualification Levels
    getPaginatedQualificationLevels: builder.query<
      QualificationLevelPaginatedResponse,
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
      providesTags: ['qualification-level'],
    }),

    // End of API
  }),
});

export const {
  useCreateQualificationLevelMutation,
  useDeleteQualificationLevelMutation,
  useGetQualificationLevelQuery,
  useGetPaginatedQualificationLevelsQuery,
  useUpdateQualificationLevelMutation,
} = qualificationLevelAPI;
