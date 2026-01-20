'use client';
import { Grade, GradeUpdate } from '@/lib/types/workstructure/grade';
import { ApiResponse } from '@/types';
import { baseApi } from '../baseAPI';

const routePath = `grades`;

export const gradeAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createGrade: builder.mutation<ApiResponse<Grade>, Partial<Grade>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/`,
          body,
        };
      },
      invalidatesTags: [routePath],
    }),
    getGradeById: builder.query<ApiResponse<Grade>, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}/${id}`,
        };
      },
      providesTags: [routePath],
    }),
    deleteGrade: builder.mutation<ApiResponse<Grade>, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: [routePath],
    }),
    updateGrade: builder.mutation<
      ApiResponse<Grade>,
      { id: number; data: GradeUpdate }
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
    getGradeList: builder.query<{ data: Grade[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [routePath],
    }),

    getPaginatedGradeList: builder.infiniteQuery({
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
  useCreateGradeMutation,
  useGetGradeListQuery,
  useDeleteGradeMutation,
  useUpdateGradeMutation,
  useGetGradeByIdQuery,
  useGetPaginatedGradeListInfiniteQuery,
} = gradeAPI;
