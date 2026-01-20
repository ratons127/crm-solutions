'use client';

import { QualificationRatingMethod } from '@/lib/types/qualification';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'qualificationRatingMethods';

export const qualificationRatingAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Employee Qualification Rating
    createQualificationRating: builder.mutation<
      QualificationRatingMethod,
      Partial<QualificationRatingMethod>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['qualification-rating'],
    }),
    // Update Employee Qualification Rating

    updateQualificationRating: builder.mutation<
      QualificationRatingMethod,
      { id: number; data: Partial<QualificationRatingMethod> }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['qualification-rating'],
    }),

    // Delete Employee Qualification Rating
    deleteQualificationRating: builder.mutation<
      QualificationRatingMethod,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['qualification-rating'],
    }),
    // Get Employee Qualification Rating
    getQualificationRating: builder.query<
      { data: QualificationRatingMethod[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['qualification-rating'],
    }),

    // End of API
  }),
});

export const {
  useCreateQualificationRatingMutation,
  useDeleteQualificationRatingMutation,
  useGetQualificationRatingQuery,
  useUpdateQualificationRatingMutation,
} = qualificationRatingAPI;
