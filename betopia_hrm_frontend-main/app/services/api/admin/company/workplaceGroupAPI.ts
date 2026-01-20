import {
  WorkplaceGroup,
  WorkplaceGroupUpdate,
} from '@/lib/types/admin/company';
import { baseApi } from '../../baseAPI';

const tag = `workplace-group`;

export const workplaceAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createWorkplaceGroup: builder.mutation<
      WorkplaceGroup,
      Partial<WorkplaceGroup>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteWorkplaceGroup: builder.mutation<WorkplaceGroup, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateWorkplaceGroup: builder.mutation<
      WorkplaceGroup,
      { id: number; data: WorkplaceGroupUpdate }
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
    getWorkplaceGroupList: builder.query<{ data: WorkplaceGroup[] }, {}>({
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
  useCreateWorkplaceGroupMutation,
  useGetWorkplaceGroupListQuery,
  useDeleteWorkplaceGroupMutation,
  useUpdateWorkplaceGroupMutation,
} = workplaceAPI;
