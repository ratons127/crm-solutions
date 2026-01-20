'use client';

import {
  EmploymentTypesData,
  EmploymentTypesRequest,
  EmploymentTypesPaginatedResponse,
} from '@/lib/types/employment';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'employeeTypes';

export const employmentTypesAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Employment types
    createEmploymentTypes: builder.mutation<
      EmploymentTypesRequest,
      Partial<EmploymentTypesRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['employee-types'],
    }),
    // Update Employee Qualification Type

    updateEmploymentTypes: builder.mutation<
      EmploymentTypesRequest,
      { id: number; data: EmploymentTypesRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['employee-types'],
    }),

    // Delete Employee Qualification Type
    deleteEmploymentTypes: builder.mutation<
      EmploymentTypesData,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['employee-types'],
    }),
    // Get Employment Types (all)
    getEmploymentTypes: builder.query<
      { data: EmploymentTypesData[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['employee-types'],
    }),

    // Get Paginated Employment Types
    getPaginatedEmploymentTypes: builder.query<
      EmploymentTypesPaginatedResponse,
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
      providesTags: ['employee-types'],
    }),

    // End of API
  }),
});

export const {
  useCreateEmploymentTypesMutation,
  useDeleteEmploymentTypesMutation,
  useGetEmploymentTypesQuery,
  useGetPaginatedEmploymentTypesQuery,
  useUpdateEmploymentTypesMutation,
} = employmentTypesAPI;
