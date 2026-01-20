// lib/features/employeesProfile/employeeProfileApi.ts
import apiClient from '../../../services/apiClient';
import {
  employeesFetchStart,
  employeesFetchSuccess,
  employeesFetchFailure,
  employeeCreateStart,
  employeeCreateSuccess,
  employeeCreateFailure,
  employeeShowStart,
  employeeShowSuccess,
  employeeShowFailure,
  employeeUpdateStart,
  employeeUpdateSuccess,
  employeeUpdateFailure,
  employeeDeleteStart,
  employeeDeleteSuccess,
  employeeDeleteFailure,
  type PageInfo,
  type EmployeeRow,
} from './employeeProfileSlice';

/* ------------ helpers ------------ */
const asNumOrNull = (v: unknown) =>
  v === '' || v === undefined || v === null ? null : (Number(v) || null);

const messageOf = (e: any) =>
  e?.response?.data?.message || e?.message || 'Something went wrong';

function pageFromMeta(meta: any | undefined): PageInfo | null {
  if (!meta || typeof meta !== 'object') return null;
  const currentPage = Number(meta.current_page ?? meta.currentPage ?? meta.page ?? 1);
  const perPage     = Number(meta.per_page    ?? meta.perPage    ?? meta.size ?? 10);
  const lastPage    = Number(meta.last_page   ?? meta.lastPage   ?? 1);
  const total       = Number(meta.total ?? 0);
  const from        = meta.from ?? null;
  const to          = meta.to ?? null;
  return { currentPage, perPage, lastPage, total, from, to };
}

/* ------------ payloads (match your backend exactly) ------------ */
export type CreateEmployeePayload = {
  firstName: string;
  lastName: string;
  gender: string;            // 'male' | 'female' | 'other' | string
  dateOfJoining: string;     // YYYY-MM-DD
  phone: string;

  dob?: string | null;
  photo?: string | null;
  nationalId?: string | null;
  email?: string | null;

  presentAddress?: string | null;
  permanentAddress?: string | null;
  maritalStatus?: string | null;

  emergencyContactName?: string | null;
  emergencyContactRelation?: string | null;
  emergencyContactPhone?: string | null;

  employeeTypeId?: number | string | null;
  departmentId?: number | string | null;
  designationId?: number | string | null;
  supervisorId?: number | string | null;
  workPlaceId?: number | string | null;

  companyId?: number | string | null;
  businessUnitId?: number | string | null;
  workPlaceGroupId?: number | string | null;
  teamId?: number | string | null;
  roleId?: number | string | null;
};

export type UpdateEmployeePayload = CreateEmployeePayload & {
  id: number | string;
};

function toApiBody(p: CreateEmployeePayload) {
  return {
    firstName: p.firstName,
    lastName: p.lastName,
    gender: p.gender ?? null,

    dateOfJoining: p.dateOfJoining ?? null,
    dob: p.dob ?? null,

    photo: p.photo ?? null,
    nationalId: p.nationalId ?? null,
    phone: p.phone ?? null,
    email: p.email ?? null,

    presentAddress: p.presentAddress ?? null,
    permanentAddress: p.permanentAddress ?? null,
    maritalStatus: p.maritalStatus ?? null,

    emergencyContactName: p.emergencyContactName ?? null,
    emergencyContactRelation: p.emergencyContactRelation ?? null,
    emergencyContactPhone: p.emergencyContactPhone ?? null,

    employeeTypeId: asNumOrNull(p.employeeTypeId),
    departmentId: asNumOrNull(p.departmentId),
    designationId: asNumOrNull(p.designationId),
    supervisorId: asNumOrNull(p.supervisorId),
    workPlaceId: asNumOrNull(p.workPlaceId),

    companyId: asNumOrNull(p.companyId),
    businessUnitId: asNumOrNull(p.businessUnitId),
    workPlaceGroupId: asNumOrNull(p.workPlaceGroupId),
    teamId: asNumOrNull(p.teamId),
    roleId: asNumOrNull(p.roleId),
  };
}

/* ====== Lists for dropdowns (employee types & designations) ====== */
export type SimpleIdName = { id: number | string; name: string };

export async function listEmployeeTypesAll(): Promise<SimpleIdName[]> {
  const { data } = await apiClient.get(`/v1/employeeTypes/all`);
  return (data?.data ?? data) as SimpleIdName[];
}

export async function listDesignationsAll(): Promise<SimpleIdName[]> {
  const { data } = await apiClient.get(`/v1/designations/all`);
  return (data?.data ?? data) as SimpleIdName[];
}

/* ============ API calls (dispatching slice actions) ============ */

export const fetchEmployees =
  (page = 1, perPage = 10, sort?: string, q?: string) =>
  async (dispatch: any) => {
    try {
      dispatch(employeesFetchStart());
      const params = new URLSearchParams();
      params.append('page', String(page));
      params.append('perPage', String(perPage)); // 👈 matches your backend meta
      if (sort) params.append('sort', sort);
      if (q) params.append('q', q);

      const { data } = await apiClient.get(`/employees?${params.toString()}`);

      const listRaw = Array.isArray(data?.data) ? data.data : [];
      const list: EmployeeRow[] = listRaw.map((e: any) => ({
        ...e,
        fullName: [e?.firstName, e?.lastName].filter(Boolean).join(' ') || null,
      }));

      dispatch(employeesFetchSuccess({ list, page: pageFromMeta(data?.meta) }));
    } catch (e: any) {
      dispatch(employeesFetchFailure(messageOf(e)));
    }
  };

export const fetchEmployeeById =
  (id: number | string) =>
  async (dispatch: any) => {
    try {
      dispatch(employeeShowStart());
      const { data } = await apiClient.get(`/employees/${id}`);
      dispatch(employeeShowSuccess((data?.data ?? data) as EmployeeRow));
    } catch (e: any) {
      dispatch(employeeShowFailure(messageOf(e)));
    }
  };

export const createEmployee =
  (payload: CreateEmployeePayload) =>
  async (dispatch: any) => {
    try {
      dispatch(employeeCreateStart());
      const body = toApiBody(payload);
      const { data } = await apiClient.post(`/employees`, body);
      dispatch(employeeCreateSuccess((data?.data ?? data) as EmployeeRow));
    } catch (e: any) {
      dispatch(employeeCreateFailure(messageOf(e)));
    }
  };

export const updateEmployeeById =
  (id: number | string, payload: CreateEmployeePayload) =>
  async (dispatch: any) => {
    try {
      dispatch(employeeUpdateStart());
      const body = toApiBody(payload);
      const { data } = await apiClient.put(`/employees/${id}`, body);
      dispatch(employeeUpdateSuccess((data?.data ?? data) as EmployeeRow));
    } catch (e: any) {
      dispatch(employeeUpdateFailure(messageOf(e)));
    }
  };

export const deleteEmployeeById =
  (id: number | string) =>
  async (dispatch: any) => {
    try {
      dispatch(employeeDeleteStart());
      await apiClient.delete(`/employees/${id}`);
      dispatch(employeeDeleteSuccess(id));
    } catch (e: any) {
      dispatch(employeeDeleteFailure(messageOf(e)));
    }
  };

/* ====== handy selectors (optional) ====== */
export const selectEmployees   = (s: any) => s.employeesProfile.list as EmployeeRow[];
export const selectEmployeesPage = (s: any) => s.employeesProfile.page as PageInfo | null;
export const selectEmployeesLoading = (s: any) => s.employeesProfile.loading as boolean;
