'use client';
import { PaginationParams } from '@/lib/types';
import { ApiResponse } from '@/types';
import { Employee, EmployeeUpdate } from '../../../lib/types/employee/index';
import { baseApi } from '../baseAPI';

const routePath = `employees`;

export const employeeAPI = baseApi.injectEndpoints({
  overrideExisting: true,
  endpoints: builder => ({
    // create
    createEmployee: builder.mutation<ApiResponse<Employee>, Partial<Employee>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: [routePath],
    }),

    //
    getEmployeeById: builder.query<ApiResponse<Employee>, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}/${id}`,
        };
      },
      providesTags: [routePath],
    }),
    deleteEmployee: builder.mutation<ApiResponse<Employee>, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: [routePath],
    }),
    updateEmployee: builder.mutation<
      ApiResponse<Employee>,
      { id: number; data: EmployeeUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: [routePath],
    }),

    // getLeaveTypeList
    getEmployeeList: builder.query<{ data: Employee[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [routePath],
    }),

    // infinite
    getPaginatedEmployeeList: builder.infiniteQuery({
      infiniteQueryOptions: {
        initialPageParam: 1,

        getNextPageParam: lastPage => {
          if (lastPage.data.length === 0) return undefined;
          return lastPage.meta.total >= lastPage.meta.currentPage
            ? lastPage.meta.currentPage + 1
            : undefined;
        },
        getPreviousPageParam: (_firstPage, _allPages, firstPageParam) => {
          return firstPageParam > 0 ? firstPageParam - 1 : undefined;
        },
      },
      query: ({
        pageParam = 1,
        perPage = 2,
        sortDirection = 'DESC',
      }: {
        pageParam: number;
        perPage?: number;
        sortDirection?: 'DESC' | 'ASC';
      }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}?page=${pageParam}&perPage=${perPage}&sortDirection=${sortDirection}`,
        };
      },
    }),

    // Get Paginated Employee
    getEmployeeListPaginated: builder.query<
      any,
      PaginationParams & { keyword?: string }
    >({
      query: ({
        page = 1,
        perPage = 10,
        sortDirection = 'DESC',
        keyword,
      }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: {
            sortDirection,
            page,
            perPage,
            keyword,
          },
        };
      },
      providesTags: [routePath],
    }),

    // End of API
  }),
});

export const {
  useCreateEmployeeMutation,
  useGetEmployeeListQuery,
  useDeleteEmployeeMutation,
  useUpdateEmployeeMutation,
  useGetEmployeeByIdQuery,
  useGetPaginatedEmployeeListInfiniteQuery,
  useGetEmployeeListPaginatedQuery,
} = employeeAPI;
