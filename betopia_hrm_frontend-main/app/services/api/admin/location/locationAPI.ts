import { PaginationMeta, PaginationParams } from '@/lib/types';
import { Location, LocationUpdate } from '@/lib/types/admin/location';
import { baseApi } from '../../baseAPI';

export const locationRoutePath = `locations`;

export interface LocationPaginatedResponse {
  data: Location[];
  meta: PaginationMeta;
}

export const locationAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createLocation: builder.mutation<Location, Partial<Location>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${locationRoutePath}`,
          body,
        };
      },
      invalidatesTags: [locationRoutePath],
    }),
    deleteLocation: builder.mutation<Location, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${locationRoutePath}/${id}`,
        };
      },
      invalidatesTags: [locationRoutePath],
    }),
    updateLocation: builder.mutation<
      Location,
      { id: number; data: LocationUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${locationRoutePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: [locationRoutePath],
    }),
    // Get all locations (no pagination)
    getLocationList: builder.query<{ data: Location[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${locationRoutePath}/all`,
        };
      },
      providesTags: [locationRoutePath],
    }),
    // Get paginated locations
    getPaginatedLocations: builder.query<
      LocationPaginatedResponse,
      PaginationParams & { type?: string }
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC', type }) => {
        return {
          method: 'GET',
          url: `/v1/${locationRoutePath}`,
          params: {
            sortDirection,
            page,
            perPage,
            ...(type && { type }),
          },
        };
      },
      providesTags: [locationRoutePath],
    }),
    getDivisionList: builder.query<{ data: Location[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${locationRoutePath}/divisions`,
        };
      },
      providesTags: [locationRoutePath],
    }),

    getDistrictListByDivisionId: builder.query<
      { data: Location[] },
      { divisionId: string | number }
    >({
      query({ divisionId }) {
        return {
          method: 'GET',
          url: `/v1/${locationRoutePath}/districts/${divisionId}`,
        };
      },
      providesTags: [locationRoutePath],
    }),
    getPoliceStationListByDistrictId: builder.query<
      { data: Location[] },
      { districtId: string | number }
    >({
      query({ districtId }) {
        return {
          method: 'GET',
          url: `/v1/${locationRoutePath}/policeStations/${districtId}`,
        };
      },
      providesTags: [locationRoutePath],
    }),
    getPostOfficeListByDistrictId: builder.query<
      { data: Location[] },
      { districtId: string | number }
    >({
      query({ districtId }) {
        return {
          method: 'GET',
          url: `/v1/${locationRoutePath}/postOffices/${districtId}`,
        };
      },
      providesTags: [locationRoutePath],
    }),

    // End of API
  }),
});

export const {
  useCreateLocationMutation,
  useGetDivisionListQuery,
  useGetDistrictListByDivisionIdQuery,
  useGetPoliceStationListByDistrictIdQuery,
  useGetPostOfficeListByDistrictIdQuery,
  useGetLocationListQuery,
  useGetPaginatedLocationsQuery,
  useDeleteLocationMutation,
  useUpdateLocationMutation,
} = locationAPI;
