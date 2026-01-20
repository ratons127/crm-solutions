import { Team, TeamUpdate } from '@/lib/types/admin/company';
import { baseApi } from '../../baseAPI';

const tag = `teams`;

export const teamAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createTeam: builder.mutation<Team, Partial<Team>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteTeam: builder.mutation<Team, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateTeam: builder.mutation<Team, { id: number; data: TeamUpdate }>({
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
    getTeamList: builder.query<{ data: Team[] }, {}>({
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
  useCreateTeamMutation,
  useGetTeamListQuery,
  useDeleteTeamMutation,
  useUpdateTeamMutation,
} = teamAPI;
