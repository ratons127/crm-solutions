'use client';

import { 
  AttendanceDeviceCategory, 
  AttendanceDeviceCategoryRequest, 
  AttendanceDeviceCategoryPaginatedResponse 
} from '@/lib/types/attendanceDeviceCategory';
import { PaginationParams } from '@/lib/types';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'attendance-device-category';

export const attendanceDeviceCategoryAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Attendance Device Category
    createAttendanceDeviceCategory: builder.mutation<
      AttendanceDeviceCategoryRequest,
      Partial<AttendanceDeviceCategoryRequest>
    >({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['attendance-device-device-category'],
    }),

    // Update Attendance Device Category
    updateAttendanceDeviceCategory: builder.mutation<
      AttendanceDeviceCategoryRequest,
      { id: number; data: AttendanceDeviceCategoryRequest }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['attendance-device-device-category'],
    }),

    // Delete Attendance Device Category
    deleteAttendanceDeviceCategory: builder.mutation<
      AttendanceDeviceCategoryRequest,
      { id: number }
    >({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['attendance-device-device-category'],
    }),

    // Get Attendance Device Category List (all)
    getAttendanceDeviceCategoryList: builder.query<
      { data: AttendanceDeviceCategory[] },
      undefined
    >({
      query() {
        return {
          method: 'GET',
          url: `/v1/${routePath}/all`,
        };
      },
      providesTags: ['attendance-device-device-category'],
    }),

    // Get Paginated Attendance Device Categories
    getPaginatedAttendanceDeviceCategories: builder.query<
      AttendanceDeviceCategoryPaginatedResponse,
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
      providesTags: ['attendance-device-device-category'],
    }),

    // End of API
  }),
});

export const {
  useGetAttendanceDeviceCategoryListQuery,
  useGetPaginatedAttendanceDeviceCategoriesQuery,
  useCreateAttendanceDeviceCategoryMutation,
  useUpdateAttendanceDeviceCategoryMutation,
  useDeleteAttendanceDeviceCategoryMutation,
} = attendanceDeviceCategoryAPI;
