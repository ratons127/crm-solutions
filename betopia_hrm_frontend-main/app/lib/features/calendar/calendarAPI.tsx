'use client';

import { baseApi } from '@/services/api/baseAPI';

/* ---------- Types ---------- */
export interface Calendar {
  id: number;
  name: string;
  description?: string;
  type?: string; // "HOLIDAY" or others
  year: number;
  isDefault?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateCalendarRequest {
  name: string;
  description?: string;
  type?: string;
  year: number;
  isDefault?: boolean;
}

export interface UpdateCalendarRequest extends CreateCalendarRequest {
  id: number;
}

/* ---------- Route Path ---------- */
const routePath = 'calendars';

/* ---------- API Definition ---------- */
export const calendarAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    // ✅ Get all calendars (no pagination)
    getCalendarList: builder.query<{ data: Calendar[] }, void>({
      query: () => ({
        url: `/v1/${routePath}/all`,
        method: 'GET',
      }),
      providesTags: ['calendar'],
    }),

    // ✅ Get calendar by ID
    getCalendarById: builder.query<{ data: Calendar }, number>({
      query: (id) => ({
        url: `/v1/${routePath}/${id}`,
        method: 'GET',
      }),
      providesTags: ['calendar'],
    }),

    // ✅ Create / Save new calendar
    createCalendar: builder.mutation<Calendar, CreateCalendarRequest>({
      query: (body) => ({
        url: `/v1/${routePath}/save`,
        method: 'POST',
        body,
      }),
      invalidatesTags: ['calendar'],
    }),

    // ✅ Update calendar by ID
    updateCalendar: builder.mutation<Calendar, UpdateCalendarRequest>({
      query: ({ id, ...body }) => ({
        url: `/v1/${routePath}/update/${id}`,
        method: 'PUT',
        body,
      }),
      invalidatesTags: ['calendar'],
    }),
  }),
});

export const {
  useGetCalendarListQuery,
  useGetCalendarByIdQuery,
  useCreateCalendarMutation,
  useUpdateCalendarMutation,
} = calendarAPI;
