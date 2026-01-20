import { PaginationMeta, PaginationParams } from '@/lib/types';
import { Company, CompanyUpdate } from '@/lib/types/admin/company';
import { baseApi } from '../../baseAPI';

const tag = `companies`;

export interface CompanyPaginatedResponse {
  data: Company[];
  meta: PaginationMeta;
}

export const companyAPI = baseApi.injectEndpoints({
  overrideExisting: true,
  endpoints: builder => ({
    // create
    createCompany: builder.mutation<Company, Partial<Company>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteCompany: builder.mutation<Company, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateCompany: builder.mutation<
      Company,
      { id: number; data: CompanyUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${tag}/${id}`,
          body: data,
        };
      },
      invalidatesTags: [tag],
    }),

    // Get all companies (no pagination)
    getCompanyList: builder.query<{ data: Company[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${tag}`,
        };
      },
      providesTags: [tag],
    }),

    // Get all companies (no pagination)
    getCompanyAllList: builder.query<{ data: Company[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${tag}/all`,
        };
      },
      providesTags: [tag],
    }),

    // Get paginated companies
    getPaginatedCompanies: builder.query<
      CompanyPaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: `/v1/${tag}`,
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: [tag],
    }),

    // End of API
  }),
});

export const {
  useCreateCompanyMutation,
  useGetCompanyListQuery,
  useGetPaginatedCompaniesQuery,
  useDeleteCompanyMutation,
  useUpdateCompanyMutation,
  useGetCompanyAllListQuery,
} = companyAPI;
