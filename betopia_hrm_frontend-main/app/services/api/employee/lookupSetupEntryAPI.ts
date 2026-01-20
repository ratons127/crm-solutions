'use client';
import {
  LookupSetupEntry,
  LookupSetupEntryUpdate,
} from '../../../lib/types/employee/lookup';
import { baseApi } from '../baseAPI';

const routePath = `lookup-entry`;

export const lookupSetupEntryAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLookupSetupEntry: builder.mutation<
      LookupSetupEntry,
      Partial<LookupSetupEntry>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body,
        };
      },
      invalidatesTags: [`lookup-setup-entry`],
    }),
    deleteLookupSetupEntry: builder.mutation<LookupSetupEntry, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/delete/${id}`,
        };
      },
      invalidatesTags: [`lookup-setup-entry`],
    }),
    updateLookupSetupEntry: builder.mutation<
      LookupSetupEntry,
      { id: number; data: LookupSetupEntryUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: [`lookup-setup-entry`],
    }),

    // getLeaveTypeList
    getLookupSetupEntryList: builder.query<
      { data: LookupSetupEntry[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [`lookup-setup-entry`],
    }),

    // End of API
  }),
});

export const {
  useCreateLookupSetupEntryMutation,
  useGetLookupSetupEntryListQuery,
  useDeleteLookupSetupEntryMutation,
  useUpdateLookupSetupEntryMutation,
} = lookupSetupEntryAPI;
