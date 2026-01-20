import { Workplace, WorkplaceUpdate } from '@/lib/types/admin/company';
import { baseApi } from '../../baseAPI';

const tag = `workplaces`;

export const workplaceAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createWorkplace: builder.mutation<Workplace, Partial<Workplace>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteWorkplace: builder.mutation<Workplace, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateWorkplace: builder.mutation<
      Workplace,
      { id: number; data: WorkplaceUpdate }
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
    getWorkplaceList: builder.query<{ data: Workplace[] }, {}>({
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
  useCreateWorkplaceMutation,
  useGetWorkplaceListQuery,
  useDeleteWorkplaceMutation,
  useUpdateWorkplaceMutation,
} = workplaceAPI;
