'use client';

import { PaginationParams } from '@/lib/types';
import {
  LeaveApproval,
  LeaveApprovalPaginatedResponse,
  LeaveApprovalRequest,
  LeaveApprovalStatusUpdateRequest,
  LeaveApprovalStatusBulkRequest,
} from '@/lib/types/leaveApproval';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'leave-approvals';

export const leaveApprovalAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    /* ---------- CREATE ---------- */
    createLeaveApproval: builder.mutation<any, LeaveApprovalRequest>({
      query: body => ({
        method: 'POST',
        url: `/v1/${routePath}/save`,
        body,
      }),
      invalidatesTags: ['leave-approval'],
    }),

    /* ---------- UPDATE (PUT /update/{id}) ---------- */
    updateLeaveApproval: builder.mutation<
      any,
      { id: number; data: LeaveApprovalRequest }
    >({
      query: ({ id, data }) => ({
        method: 'PUT',
        url: `/v1/${routePath}/update/${id}`,
        body: data,
      }),
      invalidatesTags: ['leave-approval'],
    }),

    /* ---------- UPDATE STATUS (PATCH /{id}) ---------- */
    updateLeaveApprovalStatus: builder.mutation<
      any,
      { id: number; data: LeaveApprovalStatusUpdateRequest }
    >({
      query: ({ id, data }) => ({
        method: 'PATCH',
        url: `/v1/${routePath}/${id}`,
        body: data,
      }),
      invalidatesTags: ['leave-approval'],
    }),

    /* ---------- BULK UPDATE STATUS (PATCH /status) ---------- */
    bulkUpdateLeaveApprovalStatus: builder.mutation<
      any,
      LeaveApprovalStatusBulkRequest
    >({
      query: body => ({
        method: 'PATCH',
        url: `/v1/${routePath}/status`,
        body,
      }),
      invalidatesTags: ['leave-approval'],
    }),

    /* ---------- GET BY ID ---------- */
    getLeaveApprovalById: builder.query<LeaveApproval, number>({
      query: id => ({
        method: 'GET',
        url: `/v1/${routePath}/${id}`,
      }),
      providesTags: ['leave-approval'],
    }),

    /* ---------- GET ALL PAGINATED ---------- */

    getPaginatedLeaveApprovals: builder.query<
      LeaveApprovalPaginatedResponse,
      PaginationParams & { keyword?: string } // 👈 extend type
    >({
      query: ({
        page = 1,
        perPage = 10,
        sortDirection = 'DESC',
        keyword = '',
      }) => ({
        method: 'GET',
        url: `/v1/${routePath}`,
        params: {
          sortDirection,
          page,
          perPage,
          keyword: keyword.trim() || undefined, // 👈 only include if non-empty
        },
      }),
      providesTags: ['leave-approval'],
    }),

    /* ---------- GET ALL (no pagination) ---------- */
    getAllLeaveApprovals: builder.query<any, void>({
      query: () => ({
        method: 'GET',
        url: `/v1/${routePath}/all`,
      }),
      providesTags: ['leave-approval'],
    }),

    /* ---------- DELETE ---------- */
    deleteLeaveApproval: builder.mutation<any, { id: number }>({
      query: ({ id }) => ({
        method: 'DELETE',
        url: `/v1/${routePath}/delete/${id}`,
      }),
      invalidatesTags: ['leave-approval'],
    }),
  }),
});

export const {
  useCreateLeaveApprovalMutation,
  useUpdateLeaveApprovalMutation,
  useUpdateLeaveApprovalStatusMutation,
  useBulkUpdateLeaveApprovalStatusMutation,
  useGetLeaveApprovalByIdQuery,
  useGetPaginatedLeaveApprovalsQuery,
  useGetAllLeaveApprovalsQuery,
  useDeleteLeaveApprovalMutation,
} = leaveApprovalAPI;
