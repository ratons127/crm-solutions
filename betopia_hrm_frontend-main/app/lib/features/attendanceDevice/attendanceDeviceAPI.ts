'use client';

import { 
  AttendanceDevice, 
  AttendanceDeviceRequest, 
  AttendanceDevicePaginatedResponse 
} from '@/lib/types/attendanceDevice';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'attendance-device';

export const attendanceDeviceAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Attendance Device
    createAttendanceDevice: builder.mutation<
      AttendanceDeviceRequest,
      Partial<AttendanceDeviceRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['attendance-devices'],
    }),

    // Update Attendance Device
    updateAttendanceDevice: builder.mutation<
      AttendanceDeviceRequest,
      { id: number; data: AttendanceDeviceRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['attendance-devices'],
    }),

    // Delete Attendance Device
    deleteAttendanceDevice: builder.mutation<
      AttendanceDeviceRequest,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['attendance-devices'],
    }),

    // Get Attendance Device List (all)
    getAttendanceDeviceList: builder.query<
      { data: AttendanceDevice[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['attendance-devices'],
    }),

    // Get Paginated Attendance Devices
    getPaginatedAttendanceDevices: builder.query<
      AttendanceDevicePaginatedResponse,
      PaginationParams
    >({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['attendance-devices'],
    }),

    // End of API
  }),
});

export const {
  useGetAttendanceDeviceListQuery,
  useGetPaginatedAttendanceDevicesQuery,
  useCreateAttendanceDeviceMutation,
  useUpdateAttendanceDeviceMutation,
  useDeleteAttendanceDeviceMutation,
} = attendanceDeviceAPI;
