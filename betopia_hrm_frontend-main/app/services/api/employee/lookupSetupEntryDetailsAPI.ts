'use client';
import {
  LookupSetupEntryDetails,
  LookupSetupEntryDetailsUpdate,
} from '../../../lib/types/employee/lookup';
import { baseApi } from '../baseAPI';

const routePath = `lookup-details`;

export const lookupSetupEntryDetailsAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLookupSetupEntryDetails: builder.mutation<
      LookupSetupEntryDetails,
      Partial<LookupSetupEntryDetails>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}/save`,
          body,
        };
      },
      invalidatesTags: [`lookup-details`],
    }),
    deleteLookupSetupEntryDetails: builder.mutation<
      LookupSetupEntryDetails,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/delete/${id}`,
        };
      },
      invalidatesTags: [`lookup-details`],
    }),
    updateLookupSetupEntryDetails: builder.mutation<
      LookupSetupEntryDetails,
      { id: number; data: LookupSetupEntryDetailsUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/update/${id}`,
          body: data,
        };
      },
      invalidatesTags: [`lookup-details`],
    }),

    // getLeaveTypeList
    getLookupSetupEntryDetailsList: builder.query<
      { data: LookupSetupEntryDetails[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: [`lookup-details`],
    }),

    // End of API
  }),
});

export const {
  useCreateLookupSetupEntryDetailsMutation,
  useGetLookupSetupEntryDetailsListQuery,
  useDeleteLookupSetupEntryDetailsMutation,
  useUpdateLookupSetupEntryDetailsMutation,
} = lookupSetupEntryDetailsAPI;
