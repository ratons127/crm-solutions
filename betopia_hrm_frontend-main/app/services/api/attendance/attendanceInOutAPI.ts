
import { baseApi } from '@/services/api/baseAPI';

/* ---------- Types ---------- */
export interface AttendanceSummary {
  workDate: string;
  inTime: string;
  outTime: string;
  totalWorkDuration: string;
}

/**
 * Response: List of summaries
 */
export type AttendanceSummaryResponse = AttendanceSummary[];

/* ---------- API ---------- */
const routePath = 'attendance-summary';

export const attendanceSummaryAPI = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    /* ✅ Get attendance summary by employeeId */
    getAttendanceSummaryByEmployee: builder.query<
      AttendanceSummaryResponse,
      { employeeId: number | string; limit?: number }
    >({
      query: ({ employeeId, limit }) => ({
        method: 'GET',
        url: `/v1/${routePath}/show/${employeeId}`,
        params: limit ? { limit } : undefined,
      }),
      providesTags: ['attendance-summary'],
    }),
  }),
});

/* ---------- Exports ---------- */
export const { useGetAttendanceSummaryByEmployeeQuery } = attendanceSummaryAPI;
