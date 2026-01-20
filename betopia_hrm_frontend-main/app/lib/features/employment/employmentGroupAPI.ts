'use client';

import { PaginationParams } from '@/lib/types';
import { Company } from '@/lib/types/admin/company';
import {
  EmploymentGroupData,
  EmploymentGroupPaginatedResponse,
  EmploymentGroupRequest,
} from '@/lib/types/employment';
import { baseApi } from '@/services/api/baseAPI';
import { ApiResponse } from '@/types';

const routePath = 'employee-group';
const routePath2 = 'companies';
const routePath3 = 'workplaces';

export const employmentGroupAPI = baseApi.injectEndpoints({
  overrideExisting: true,
  endpoints: builder => ({
    // Create Employment Group
    createEmploymentGroup: builder.mutation<
      EmploymentGroupRequest,
      Partial<EmploymentGroupRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['employee-group'],
    }),
    // Update Employee Qualification Type

    updateEmploymentGroup: builder.mutation<
      EmploymentGroupRequest,
      { id: number; data: EmploymentGroupRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['employee-group'],
    }),

    // Delete Employee Qualification Type
    deleteEmploymentGroup: builder.mutation<
      EmploymentGroupData,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['employee-group'],
    }),
    // Get Employment Group (all)
    getEmploymentGroup: builder.query<
      { data: EmploymentGroupData[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['employee-group'],
    }),

    // Get Paginated Employment Groups
    getPaginatedEmploymentGroups: builder.query<
      EmploymentGroupPaginatedResponse,
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
      providesTags: ['employee-group'],
    }),

    // Get Company List
    getCompanyList: builder.query<ApiResponse<Company[]>, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath2}/all`,
        };
      },
    }),

    // Get Workplaces List
    getWorkPlacesList: builder.query<{ data: any[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath3}/all`,
        };
      },
    }),

    // End of API
  }),
});

export const {
  useCreateEmploymentGroupMutation,
  useDeleteEmploymentGroupMutation,
  useGetEmploymentGroupQuery,
  useGetPaginatedEmploymentGroupsQuery,
  useUpdateEmploymentGroupMutation,
  useGetCompanyListQuery,
  useGetWorkPlacesListQuery,
} = employmentGroupAPI;
