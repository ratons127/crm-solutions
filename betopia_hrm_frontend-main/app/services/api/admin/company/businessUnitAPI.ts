import { BusinessUnit, BusinessUnitUpdate } from '@/lib/types/admin/company';
import { baseApi } from '../../baseAPI';

const tag = `business-unit`;

export const companyAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createBusinessUnit: builder.mutation<BusinessUnit, Partial<BusinessUnit>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteBusinessUnit: builder.mutation<BusinessUnit, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateBusinessUnit: builder.mutation<
      BusinessUnit,
      { id: number; data: BusinessUnitUpdate }
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

    // getLeaveTypeList
    getBusinessUnitList: builder.query<{ data: BusinessUnit[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${tag}`,
        };
      },
      providesTags: [tag],
    }),

    // End of API
  }),
});

export const {
  useCreateBusinessUnitMutation,
  useGetBusinessUnitListQuery,
  useDeleteBusinessUnitMutation,
  useUpdateBusinessUnitMutation,
} = companyAPI;
