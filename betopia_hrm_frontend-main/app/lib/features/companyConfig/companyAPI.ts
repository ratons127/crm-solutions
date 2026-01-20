'use client';

import { baseApi } from '@/services/api/baseAPI';

const routePath = 'companies';

type CompanyType = {
  id: number;
  name: string;
};

export const companyAPI = baseApi.injectEndpoints({
  overrideExisting: true,
  endpoints: builder => ({
    // Get Company List (all)
    getCompanyList: builder.query<{ data: CompanyType[] }, undefined>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
    }),

    // End of API
  }),
});

export const { useGetCompanyListQuery } = companyAPI;
