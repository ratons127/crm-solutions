import { Country, CountryUpdate } from '@/lib/types/admin/location';
import { PaginationParams, PaginationMeta } from '@/lib/types';
import { baseApi } from '../../baseAPI';

const tag = `countries`;

export interface CountryPaginatedResponse {
  data: Country[];
  meta: PaginationMeta;
}

export const countryAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createCountry: builder.mutation<Country, Partial<Country>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteCountry: builder.mutation<Country, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateCountry: builder.mutation<
      Country,
      { id: number; data: CountryUpdate }
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

    // Get all countries (no pagination)
    getCountryList: builder.query<{ data: Country[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${tag}`,
        };
      },
      providesTags: [tag],
    }),

    // Get paginated countries
    getPaginatedCountries: builder.query<CountryPaginatedResponse, PaginationParams>({
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
  useCreateCountryMutation,
  useGetCountryListQuery,
  useGetPaginatedCountriesQuery,
  useDeleteCountryMutation,
  useUpdateCountryMutation,
} = countryAPI;
