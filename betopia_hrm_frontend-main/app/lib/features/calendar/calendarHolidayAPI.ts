'use client';

import { baseApi } from '@/services/api/baseAPI';

/* ---------- Types ---------- */
export interface CalendarHoliday {
  id: number;
  holidayDate: string; // YYYY-MM-DD
  isHoliday: boolean;
  description?: string;
  colorCode?: string;
  name?: string;
  workingType?: number;
  weekendType?: string;
  status?: boolean;
}

export interface UpdateIsHolidayRequest {
  id: number;
  isHoliday: boolean;
}

/**
 * Single holiday update structure.
 * Used for both single and bulk updates.
 */
export interface UpdateHolidayRequest {
  id?: number;
  holidayDate: string;
  isHoliday: boolean;
  description?: string;
  colorCode?: string;
  weekendType?: string;
}

const routePath = 'calendar-holidays';

/* ---------- API Endpoints ---------- */
export const calendarHolidayAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    /* ✅ Get current-year holidays */
    getCurrentYearHolidays: builder.query<{ data: CalendarHoliday[] }, void>({
      query: () => ({
        url: `/v1/${routePath}/current-year`,
        method: 'GET',
      }),
      providesTags: ['calendar-holidays'],
    }),

    /* ✅ Update only isHoliday flag */
    updateIsHoliday: builder.mutation<
      CalendarHoliday[],
      UpdateIsHolidayRequest[] // ✅ accepts array
    >({
      query: (body) => ({
        url: `/v1/${routePath}/update-isholiday`,
        method: 'PATCH',
        body,
      }),
      invalidatesTags: ['calendar-holidays'],
    }),


    /* ✅ Create (save) new holidays — single or bulk */
    createHoliday: builder.mutation<
      CalendarHoliday[],
      UpdateHolidayRequest | UpdateHolidayRequest[]
    >({
      query: (body) => ({
        url: `/v1/${routePath}/save`,
        method: 'POST',
        body: Array.isArray(body) ? body : [body],
      }),
      invalidatesTags: ['calendar-holidays'],
    }),

    /* ✅ Update full holiday details (single or bulk) */
    updateHoliday: builder.mutation<
      CalendarHoliday[],
      UpdateHolidayRequest | UpdateHolidayRequest[]
    >({
      query: (body) => ({
        url: `/v1/${routePath}/update`,
        method: 'PUT',
        body: Array.isArray(body) ? body : [body],
      }),
      invalidatesTags: ['calendar-holidays'],
    }),
  }),
});

export const {
  useGetCurrentYearHolidaysQuery,
  useUpdateIsHolidayMutation,
  useUpdateHolidayMutation,
  useCreateHolidayMutation,
} = calendarHolidayAPI;
