import { RootState } from '@/lib/store';
import {
  BaseQueryFn,
  createApi,
  FetchArgs,
  fetchBaseQuery,
  FetchBaseQueryError,
} from '@reduxjs/toolkit/query/react';
import { clearAuth, setAuth } from '../../lib/features/app/appSlice';

const API_BASE = process.env.NEXT_PUBLIC_API_URL;

const rawBaseQuery = fetchBaseQuery({
  baseUrl: API_BASE,
  prepareHeaders: (headers, { getState }) => {
    const token = (getState() as RootState).app.auth.accessToken;
    if (token) headers.set('Authorization', `Bearer ${token}`);
    // Do NOT force Content-Type here; it breaks FormData uploads.
    return headers;
  },
});

const isString = (v: unknown): v is string => typeof v === 'string';

const shouldSkipRefresh = (args: string | FetchArgs) => {
  const url = isString(args) ? args : args.url || '';
  // Never try to refresh while hitting auth endpoints
  return /\/(refresh-token|logout|login)\b/.test(url);
};

// Strongly-typed baseQuery with reauth
const baseQueryWithReauth: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extra) => {
  let result = await rawBaseQuery(args, api, extra);

  const status = (result.error as FetchBaseQueryError | undefined)?.status;

  // Only refresh on 401 (expired/invalid token), NOT on 403 (forbidden)
  if (status === 401 && !shouldSkipRefresh(args)) {
    const state = api.getState() as RootState;
    const rt = state.app.auth.refreshToken;

    if (!rt) {
      // No refresh token: just clear auth and return the error once.
      api.dispatch(clearAuth());
      return result;
    }

    // Attempt token refresh
    const refreshResult = await rawBaseQuery(
      { url: '/v1/refresh-token', method: 'POST', body: { refreshToken: rt } },
      api,
      extra
    );

    if (refreshResult.data) {
      const data = refreshResult.data as any;
      api.dispatch(
        setAuth({
          user: data?.user ?? null,
          accessToken: data?.token ?? data?.accessToken ?? null,
          refreshToken: data?.refreshToken ?? rt,
        })
      );

      // Retry original request ONCE with the new token
      result = await rawBaseQuery(args, api, extra);
    } else {
      // Refresh failed: clear auth and return the original error
      api.dispatch(clearAuth());
      return result;
    }
  }

  // Do not throw here; RTKQ will surface `result.error`, and `unwrap()` will reject appropriately.
  return result;
};

export const baseApi = createApi({
  reducerPath: 'baseApi',
  baseQuery: baseQueryWithReauth,
  tagTypes: [
    'LeaveType',
    'LeaveGroup',
    'LeaveCategory',
    'LeaveTypeRule',
    'LeaveEligibility',
    'LeavePolicy',
    'leave-years',
    'leave-requests',
    'lookup-setup-entry',
    'lookup-details',
    'countries',
    'locations',
    'companies',
    'business-unit',
    'workplaces',
    'department',
    'teams',
    'workplace-group',
    'leave-group-assigns',
    'banks',
    'bank-branches',
    'qualification-types',
    'qualification-level',
    'field-of-study',
    'qualification-rating',
    'employee-group',
    'employee-types',
    'institute-name',
    'document',
    'document-type',
    'shift-device-category',
    'shift-definitions',
    'menu',
    'shift-rotation-patterns',
    'leave-approval',
    'permissions',
    `employees`,
    `grades`,
    'designations',
    `hierarchy-filter`,
    'attendance-device-device-category',
    'attendance-devices',
    'attendance-device-assigns',
    'employees',
    'calendar-holidays',
    'calendar',
    'leave-balance',
    'attendance-policy',
    'employeeWorkExperiences',
    'employeesRTK',
    `employeeEducationInfos`,
    'employee-documents',
    'shift-assign',
    'manual-attendance',
    'attendance-summary',
    'attendance-status',
    'attendance-approval',
    'shift-categories',
    'cashAdvanceSlabConfig',
  ],
  endpoints: () => ({}),
});
