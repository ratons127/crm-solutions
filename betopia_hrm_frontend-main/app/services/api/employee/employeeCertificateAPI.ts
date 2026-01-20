'use client';

import { ForeignKeyId } from '@/lib/types';
import { EmployeeCertificate } from '@/lib/types/employee/employeeCertificate';
import { getStringFromJSONWithBlob } from '@/lib/utils/helpers';
import { ApiResponse } from '@/types';
import { baseApi } from '../baseAPI';

const routePath = `employee-documents`;

export const employeeAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createEmployeeCertificate: builder.mutation<
      ApiResponse<EmployeeCertificate>,
      Partial<EmployeeCertificate>
    >({
      query: body => {
        const { file, ...data } = body;
        const formData = new FormData();
        if (file) {
          formData.append('file', file as Blob);
        }
        const dataPayload = {
          ...data,
          status: !!data.status,
          employeeId: Number(data.employeeId),
        };
        formData.append('data', getStringFromJSONWithBlob(dataPayload));
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body: formData,
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
    updateEmployeeCertificate: builder.mutation<
      ApiResponse<EmployeeCertificate>,
      { id: ForeignKeyId; data: Partial<EmployeeCertificate> }
    >({
      query: ({ id, data }) => {
        const formData = new FormData();
        if (data.file) {
          formData.append('file', data.file as Blob);
        }
        Object.entries(data).forEach(([key, value]) => {
          if (key !== 'file' && value !== undefined) {
            formData.append(key, String(value));
          }
        });
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: formData,
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
    getEmployeeCertificateList: builder.query<
      ApiResponse<EmployeeCertificate[]>,
      void
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [routePath],
    }),

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
  useCreateEmployeeCertificateMutation,
  useUpdateEmployeeCertificateMutation,
  useGetEmployeeCertificateListQuery,
} = employeeAPI;
