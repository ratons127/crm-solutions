'use client';
import { lookupItemIds } from '@/lib/utils/helpers';
import { LookupSetupEntryDetails } from '../../../lib/types/employee/lookup';
import { baseApi } from '../baseAPI';

const routePath = `lookup-details`;

export const lookupSetupEntryDetailsAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    getLookupItemsByCategory: builder.query<
      { data: LookupSetupEntryDetails[] },
      { type: keyof typeof lookupItemIds }
    >({
      query({ type }) {
        return {
          method: 'GET',
          url: `/v1/${routePath}?setupId=${lookupItemIds[type]}&perPage=100&page=1`,
        };
      },
      providesTags: [`lookup-details`],
    }),

    // End of API
  }),
});

export const { useGetLookupItemsByCategoryQuery } = lookupSetupEntryDetailsAPI;
