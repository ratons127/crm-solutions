'use client';

import { PaginationParams } from '@/lib/types';
import type {
  CreateUpdateDocumentResponse,
  DeleteDocumentResponse,
  DocumentTypeListType,
  EmployeeDocumentListResponse,
  PaginatedEmployeeDocumentResponse,
} from '@/lib/types/document';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'employee-documents';

export const documentAPI = baseApi.injectEndpoints({
  overrideExisting: true,
  endpoints: builder => ({
    // Create Employee Document
    createEmployeeDocument: builder.mutation<
      CreateUpdateDocumentResponse,
      FormData
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body,
        };
      },
      invalidatesTags: [routePath],
    }),
    // Update Employee Document
    updateEmployeeDocument: builder.mutation<
      CreateUpdateDocumentResponse,
      { id: number; data: FormData }
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

    // // Delete Employee Document
    deleteEmployeeDocument: builder.mutation<
      DeleteDocumentResponse,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['document'],
    }),
    // Get Employee Document
    getEmployeeDocument: builder.query<EmployeeDocumentListResponse, undefined>(
      {
        query() {
          return {
            method: 'GET',
            url: `/v1/${routePath}/all`,
          };
        },
        providesTags: ['document'],
      }
    ),

    // Get Paginated Document List
    getPaginatedDocumentList: builder.query<
      PaginatedEmployeeDocumentResponse,
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
      providesTags: ['document'],
    }),

    // // Get Employee List
    // getEmployeeList: builder.query<{ data: Employee[] }, undefined>({
    //   query() {
    //     return {
    //       method: 'GET',
    //       url: '/v1/employees/all',
    //     };
    //   },
    // }),

    // Get Document Type List
    getDocumentTypeList: builder.query<
      {
        data: DocumentTypeListType[];
        message: string;
        success: boolean;
        status: number;
      },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: '/v1/document-type/all',
        };
      },
    }),

    // End of API
  }),
});

export const {
  useCreateEmployeeDocumentMutation,
  useDeleteEmployeeDocumentMutation,
  useGetEmployeeDocumentQuery,
  useUpdateEmployeeDocumentMutation,

  useGetDocumentTypeListQuery,
  useGetPaginatedDocumentListQuery,
} = documentAPI;

// Re-export types for convenience
export type {
  CreateUpdateDocumentResponse,
  DeleteDocumentResponse,
  DocumentFormValues,
  DocumentPayloadData,
  DocumentType,
  EmployeeDocument,
  EmployeeDocumentVersion,
} from '@/lib/types/document';
