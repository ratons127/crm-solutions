'use client';

import { ForeignKeyId } from '@/lib/types';
import { EmployeeEducationalQualification } from '@/lib/types/employee/employeeEducationalQualification';
import { ApiResponse } from '@/types';
import { baseApi } from '../baseAPI';

const routePath = `employeeEducationInfos`;

export const employeeAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createEmployeeEducationalQualification: builder.mutation<
      ApiResponse<EmployeeEducationalQualification>,
      Partial<EmployeeEducationalQualification>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/`,
          body,
        };
      },
      invalidatesTags: [routePath],
    }),

    // //
    // getEmployeeEducationListById: builder.query<
    //   ApiResponse<EmployeeEducationalQualification[]>,
    //   { id: ForeignKeyId }
    // >({
    //   query: ({ id }) => {
    //     return {
    //       method: 'GET',
    //       url: `/v1/${routePath}/${id}`,
    //     };
    //   },
    //   providesTags: [routePath],
    // }),
    // deleteEmployee: builder.mutation<ApiResponse<Employee>, { id: number }>({
    //   query: ({ id }) => {
    //     return {
    //       method: 'DELETE',
    //       url: `/v1/${routePath}/${id}`,
    //     };
    //   },
    //   invalidatesTags: [routePath],
    // }),
    updateEmployeeQualification: builder.mutation<
      ApiResponse<EmployeeEducationalQualification>,
      { id: ForeignKeyId; data: Partial<EmployeeEducationalQualification> }
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
    // uploadEmployeeEducationDocument: builder.mutation<
    //   ApiResponse<EmployeeEducationalQualification>,
    //   { id: ForeignKeyId; file: File }
    // >({
    //   query: ({ id, file }) => {
    //     return {
    //       method: 'PUT',
    //       url: `/v1/${routePath}/${id}/upload-file`,
    //       body: new FormData().append('file', file),
    //     };
    //   },
    //   invalidatesTags: [routePath],
    // }),

    // // getLeaveTypeList
    // getEmployeeList: builder.query<{ data: Employee[] }, undefined>({
    //   query() {
    //     return {
    //       method: 'GET',
    //       url: `/v1/${routePath}/all`,
    //     };
    //   },
    //   providesTags: [routePath],
    // }),

    // getPaginatedEmployeeList: builder.infiniteQuery({
    //   infiniteQueryOptions: {
    //     initialPageParam: 1,

    //     getNextPageParam: lastPage => {
    //       if (lastPage.data.length === 0) return undefined;
    //       return lastPage.meta.total >= lastPage.meta.currentPage
    //         ? lastPage.meta.currentPage + 1
    //         : undefined;
    //     },
    //     getPreviousPageParam: (_firstPage, _allPages, firstPageParam) => {
    //       return firstPageParam > 0 ? firstPageParam - 1 : undefined;
    //     },
    //   },
    //   query: ({
    //     pageParam = 1,
    //     perPage = 2,
    //     sortDirection = 'DESC',
    //   }: {
    //     pageParam: number;
    //     perPage?: number;
    //     sortDirection?: 'DESC' | 'ASC';
    //   }) => {
    //     return {
    //       method: 'GET',
    //       url: `/v1/${routePath}?page=${pageParam}&perPage=${perPage}&sortDirection=${sortDirection}`,
    //     };
    //   },
    // }),

    // End of API
  }),
});

export const {
  useCreateEmployeeEducationalQualificationMutation,
  useUpdateEmployeeQualificationMutation,
} = employeeAPI;
