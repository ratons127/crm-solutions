'use client';
import {
  Designation,
  DesignationUpdate,
} from '@/lib/types/workstructure/designation';
import { ApiResponse } from '@/types';
import { baseApi } from '../baseAPI';

const routePath = `designations`;

export const designationAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createDesignation: builder.mutation<
      ApiResponse<Designation>,
      Partial<Designation>
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
    getDesignationById: builder.query<ApiResponse<Designation>, { id: number }>(
      {
        query: ({ id }) => {
          return {
            method: 'GET',
            url: `/v1/${routePath}/${id}`,
          };
        },
        providesTags: [routePath],
      }
    ),
    deleteDesignation: builder.mutation<
      ApiResponse<Designation>,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: [routePath],
    }),
    updateDesignation: builder.mutation<
      ApiResponse<Designation>,
      { id: number; data: DesignationUpdate }
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
    getDesignationList: builder.query<{ data: Designation[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [routePath],
    }),

    getPaginatedDesignationList: builder.infiniteQuery({
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

    // End of API
  }),
});

export const {
  useCreateDesignationMutation,
  useGetDesignationListQuery,
  useDeleteDesignationMutation,
  useUpdateDesignationMutation,
  useGetDesignationByIdQuery,
  useGetPaginatedDesignationListInfiniteQuery,
} = designationAPI;
