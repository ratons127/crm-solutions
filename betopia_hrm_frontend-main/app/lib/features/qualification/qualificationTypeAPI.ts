'use client';

import {
  EmployeeQualificationType,
  EmployeeQualificationTypeRequest,
  QualificationTypePaginatedResponse,
} from '@/lib/types/qualification';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'qualificationTypes';

export const qualificationTypeAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Employee Qualification Type
    createQualificationType: builder.mutation<
      EmployeeQualificationTypeRequest,
      Partial<EmployeeQualificationTypeRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['qualification-types'],
    }),
    // Update Employee Qualification Type

    updateQualificationType: builder.mutation<
      EmployeeQualificationTypeRequest,
      { id: number; data: EmployeeQualificationTypeRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['qualification-types'],
    }),

    // Delete Employee Qualification Type
    deleteQualificationType: builder.mutation<
      EmployeeQualificationType,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['qualification-types'],
    }),
    // Get Employee Qualification Type (all)
    getQualificationType: builder.query<
      { data: EmployeeQualificationType[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['qualification-types'],
    }),

    // Get Paginated Qualification Types
    getPaginatedQualificationTypes: builder.query<
      QualificationTypePaginatedResponse,
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
      providesTags: ['qualification-types'],
    }),

    // End of API
  }),
});

export const {
  useCreateQualificationTypeMutation,
  useDeleteQualificationTypeMutation,
  useGetQualificationTypeQuery,
  useGetPaginatedQualificationTypesQuery,
  useUpdateQualificationTypeMutation,
} = qualificationTypeAPI;
