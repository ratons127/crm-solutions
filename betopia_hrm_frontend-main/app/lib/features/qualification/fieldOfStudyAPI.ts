'use client';

import {
  EmployeeFieldOfStudy,
  EmployeeFieldOfStudyRequest,
  FieldOfStudyPaginatedResponse,
} from '@/lib/types/qualification';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'fieldStudies';

export const fieldOfStudyAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Employee field of Studies
    createFieldOfStudy: builder.mutation<
      EmployeeFieldOfStudyRequest,
      Partial<EmployeeFieldOfStudyRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['field-of-study'],
    }),
    // Update Employee field of Studies

    updateFieldOfStudy: builder.mutation<
      EmployeeFieldOfStudyRequest,
      { id: number; data: EmployeeFieldOfStudyRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['field-of-study'],
    }),

    // Delete Employee field of Studies
    deleteFieldOfStudy: builder.mutation<EmployeeFieldOfStudy, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['field-of-study'],
    }),
    // Get Employee field of Studies (all)
    getFieldOfStudy: builder.query<{ data: EmployeeFieldOfStudy[] }, undefined>(
      {
        query() {
          return {
            method: 'GET',
            url: `/v1/${routePath}/all`,
          };
        },
        providesTags: ['field-of-study'],
      }
    ),

    // Get Paginated Field of Studies
    getPaginatedFieldOfStudies: builder.query<
      FieldOfStudyPaginatedResponse,
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
      providesTags: ['field-of-study'],
    }),

    // End of API
  }),
});

export const {
  useCreateFieldOfStudyMutation,
  useDeleteFieldOfStudyMutation,
  useGetFieldOfStudyQuery,
  useGetPaginatedFieldOfStudiesQuery,
  useUpdateFieldOfStudyMutation,
} = fieldOfStudyAPI;
