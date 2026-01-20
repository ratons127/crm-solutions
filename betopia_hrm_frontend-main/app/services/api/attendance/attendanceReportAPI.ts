'use client';

import { baseApi } from '@/services/api/baseAPI';

export const attendanceReportAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    getAttendanceDailyReport: builder.mutation<{ data: any[] }, any>({
      query: ({
        employeeId,
        fromDate,
        toDate,
        shiftId,
        businessUnitId,
        workplaceGroupId,
        workplaceId,
        departmentId,
        teamId,
      }) => {
        const params = new URLSearchParams();

        if (employeeId) params.append('employeeId', employeeId);
        if (fromDate) params.append('fromDate', fromDate);
        if (toDate) params.append('toDate', toDate);
        if (shiftId) params.append('shiftId', shiftId);
        if (businessUnitId) params.append('businessUnitId', businessUnitId);
        if (workplaceGroupId)
          params.append('workplaceGroupId', workplaceGroupId);
        if (workplaceId) params.append('workplaceId', workplaceId);
        if (departmentId) params.append('departmentId', departmentId);
        if (teamId) params.append('teamId', teamId);

        return {
          method: 'GET',
          url: `/v1/attendance-report/employee-report?${params.toString()}`,
        };
      },
    }),
    exportAttendanceDailyReport: builder.mutation<Blob, any>({
      query: ({
        employeeId,
        fromDate,
        toDate,
        format,
        shiftId,
        businessUnitId,
        workplaceGroupId,
        workplaceId,
        departmentId,
        teamId,
      }) => {
        const params = new URLSearchParams();
        if (employeeId) params.append('employeeId', String(employeeId));
        if (fromDate) params.append('fromDate', String(fromDate));
        if (toDate) params.append('toDate', String(toDate));
        if (format) params.append('format', String(format));
        if (shiftId) params.append('shiftId', String(shiftId));
        if (businessUnitId) params.append('businessUnitId', String(businessUnitId));
        if (workplaceGroupId)
          params.append('workplaceGroupId', String(workplaceGroupId));
        if (workplaceId) params.append('workplaceId', String(workplaceId));
        if (departmentId) params.append('departmentId', String(departmentId));
        if (teamId) params.append('teamId', String(teamId));

        return {
          method: 'GET',
          url: `/v1/attendance-report/export/employee-report?${params.toString()}`,
          // Force binary response for file downloads
          responseHandler: (response: Response) => response.blob(),
        } as any;
      },
    }),
    // Monthly report (summary over a date range, filtered by org hierarchy)
    getAttendanceMonthlyReport: builder.mutation<{ data: any[] }, any>({
      query: ({
        fromDate,
        toDate,
        companyId,
        businessUnitId,
        workplaceGroupId,
        workplaceId,
        departmentId,
        teamId,
      }) => {
        const params = new URLSearchParams();
        if (fromDate) params.append('fromDate', String(fromDate));
        if (toDate) params.append('toDate', String(toDate));
        if (companyId) params.append('companyId', String(companyId));
        if (businessUnitId) params.append('businessUnitId', String(businessUnitId));
        if (workplaceGroupId)
          params.append('workplaceGroupId', String(workplaceGroupId));
        if (workplaceId) params.append('workplaceId', String(workplaceId));
        if (departmentId) params.append('departmentId', String(departmentId));
        if (teamId) params.append('teamId', String(teamId));

        return {
          method: 'GET',
          url: `/v1/attendance-report/monthly-report?${params.toString()}`,
        };
      },
    }),
    exportAttendanceMonthlyReport: builder.mutation<Blob, any>({
      query: ({
        fromDate,
        toDate,
        companyId,
        businessUnitId,
        workplaceGroupId,
        workplaceId,
        departmentId,
        teamId,
        format,
      }) => {
        const params = new URLSearchParams();
        if (fromDate) params.append('fromDate', String(fromDate));
        if (toDate) params.append('toDate', String(toDate));
        if (companyId) params.append('companyId', String(companyId));
        if (businessUnitId) params.append('businessUnitId', String(businessUnitId));
        if (workplaceGroupId)
          params.append('workplaceGroupId', String(workplaceGroupId));
        if (workplaceId) params.append('workplaceId', String(workplaceId));
        if (departmentId) params.append('departmentId', String(departmentId));
        if (teamId) params.append('teamId', String(teamId));
        if (format) params.append('format', String(format));

        return {
          method: 'GET',
          url: `/v1/attendance-report/export/monthly-report?${params.toString()}`,
          responseHandler: (response: Response) => response.blob(),
        } as any;
      },
    }),
  }),
});

export const {
  useGetAttendanceDailyReportMutation,
  useExportAttendanceDailyReportMutation,
  useGetAttendanceMonthlyReportMutation,
  useExportAttendanceMonthlyReportMutation,
} = attendanceReportAPI;
