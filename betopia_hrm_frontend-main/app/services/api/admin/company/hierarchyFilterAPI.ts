import { baseApi } from '../../baseAPI';

export const hierarchyFilterAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getHierarchyFilter: builder.query<
      {
        data: {
          companies: { id: number; name: string }[];
          businessUnits: { id: number; name: string }[];
          workplaceGroups: { id: number; name: string }[];
          workplaces: { id: number; name: string }[];
          departments: { id: number; name: string }[];
          teams: { id: number; name: string }[];
        };
        message: string;
        success: boolean;
        status: number;
      },
      {
        companyId?: number;
        businessUnitId?: number;
        workplaceGroupId?: number;
        workplaceId?: number;
        departmentId?: number;
        teamId?: number;
      }
    >({
      query: (params) => ({
        url: '/v1/hierarchy-filter',
        method: 'GET',
        params,
      }),
    }),
  }),
});

export const { useLazyGetHierarchyFilterQuery } = hierarchyFilterAPI;
