import {
  LeaveGroupAssign,
  LeaveGroupAssignPaginatedResponse,
  LeaveGroupAssignUpdate,
} from '@/lib/types/leave';
import { PaginationParams } from '../../../lib/types';
import { baseApi } from '../baseAPI';

const tag = `leave-group-assigns`;

export const leaveGroupAssignAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLeaveGroupAssign: builder.mutation<
      LeaveGroupAssign,
      Partial<LeaveGroupAssign>
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
    deleteLeaveGroupAssign: builder.mutation<LeaveGroupAssign, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateLeaveGroupAssign: builder.mutation<
      LeaveGroupAssign,
      { id: number; data: LeaveGroupAssignUpdate }
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

    // getLeaveGroupAssignList
    getLeaveGroupAssignList: builder.query<{ data: LeaveGroupAssign[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${tag}`,
        };
      },
      providesTags: [tag],
    }),

    // Get All Leave Group Assigns by LeaveTypeId
    getLeaveGroupAssignsByType: builder.query<
      { data: LeaveGroupAssign[] },
      { leaveTypeId: number | string }
    >({
      query: ({ leaveTypeId }) => ({
        method: 'GET',
        url: `/v1/${tag}/all`,
        params: { leaveTypeId },
      }),
      providesTags: [tag],
    }),

    // Get Paginated Leave Group Assigns
    getPaginatedLeaveGroupAssigns: builder.query<
      LeaveGroupAssignPaginatedResponse,
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
  useCreateLeaveGroupAssignMutation,
  useGetLeaveGroupAssignListQuery,
  useGetPaginatedLeaveGroupAssignsQuery,
  useDeleteLeaveGroupAssignMutation,
  useUpdateLeaveGroupAssignMutation,
  useGetLeaveGroupAssignsByTypeQuery,
} = leaveGroupAssignAPI;
