'use client';

import { PaginationParams } from '@/lib/types';
import type {
  CreateUpdateDocumentTypeResponse,
  DeleteDocumentTypeResponse,
  DocumentType,
  DocumentTypeFormValues,
  PaginatedDocumentTypeResponse,
} from '@/lib/types/document';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'document-type';

export const documentTypeAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Document Type
    createDocumentType: builder.mutation<
      CreateUpdateDocumentTypeResponse,
      DocumentTypeFormValues
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['document-type'],
    }),

    // Update Document Type
    updateDocumentType: builder.mutation<
      CreateUpdateDocumentTypeResponse,
      { id: number; data: DocumentTypeFormValues }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['document-type'],
    }),

    // Delete Document Type
    deleteDocumentType: builder.mutation<
      DeleteDocumentTypeResponse,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['document-type'],
    }),

    // Get Paginated Document Type List
    getPaginatedDocumentTypeList: builder.query<
      PaginatedDocumentTypeResponse,
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
      providesTags: ['document-type'],
    }),

    // Get All Document Types
    getAllDocumentTypes: builder.query<
      {
        data: DocumentType[];
        message: string;
        success: boolean;
        status: number;
      },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['document-type'],
    }),
  }),
});

export const {
  useCreateDocumentTypeMutation,
  useUpdateDocumentTypeMutation,
  useDeleteDocumentTypeMutation,
  useGetPaginatedDocumentTypeListQuery,
  useGetAllDocumentTypesQuery,
} = documentTypeAPI;
