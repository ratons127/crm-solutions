// lib/features/department/departmentApi.ts
import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import {
  DepartmentRow,
  PageInfo,
  departmentActions,
} from './departmentSlice';

export type ListParams = {
  page?: number;
  size?: number;
  sort?: string;
  q?: string;
};

const routeBase = 'department'; // keep consistent with backend

function toQuery(params: Record<string, any>) {
  const s = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== '') s.append(k, String(v));
  });
  return s.toString();
}

/* ---------------- READ: Paginated list ---------------- */
export const fetchDepartments =
  (page = 1, size = 10, extra: Omit<ListParams, 'page' | 'size'> = {}) =>
  async (dispatch: AppDispatch) => {
    const { setLoading, setError, setList, setPage } = departmentActions;
    dispatch(setLoading(true));
    try {
      const query = toQuery({ page, perPage: size, ...extra });
      const { data } = await apiClient.get<any>(`/${routeBase}?${query}`);

      const list: DepartmentRow[] = Array.isArray(data)
        ? data
        : (data?.data as DepartmentRow[]) ?? [];

      const meta = data?.meta ?? null;
      const pageInfo: PageInfo = meta
        ? {
            currentPage: Number(meta.currentPage ?? page),
            lastPage: Number(meta.lastPage ?? 1),
            total: Number(meta.total ?? list.length),
            perPage: Number(meta.perPage ?? size),
          }
        : {
            currentPage: page,
            lastPage: Math.max(1, Math.ceil((list?.length ?? 0) / size)),
            total: list?.length ?? 0,
            perPage: size,
          };

      dispatch(setList(list));
      dispatch(setPage(pageInfo));
    } catch (e: any) {
      const msg =
        e?.response?.data?.message ||
        e?.message ||
        'Failed to load departments';
      dispatch(setError(msg));
    } finally {
      dispatch(setLoading(false));
    }
  };

/* ---------------- READ: single department ---------------- */
export const getDepartmentById = async (id: number | string) => {
  const { data } = await apiClient.get<{ data: DepartmentRow }>(
    `/${routeBase}/${id}`
  );
  return (data?.data ?? data) as DepartmentRow;
};

/* ---------------- READ: all departments (no pagination) ---------------- */
export const getAllDepartments = async () => {
  const { data } = await apiClient.get<{ data: DepartmentRow[] }>(
    `/${routeBase}/all`
  );
  return (data?.data ?? data) as DepartmentRow[];
};

/* ---------------- CREATE ---------------- */
export type CreateDepartmentPayload = {
  WorkplaceId: number;
  name: string;
  code?: string | null;
  description?: string | null;
  /** boolean now */
  status?: boolean;
};

export const createDepartment =
  (payload: CreateDepartmentPayload) => async (dispatch: AppDispatch) => {
    const { setCreating, setCreateError, addItemInList } = departmentActions;
    dispatch(setCreating(true));
    try {
      // Default status to true if not specified
      const body = {
        ...payload,
        status: typeof payload.status === 'boolean' ? payload.status : true,
      };

      const { data } = await apiClient.post<{ data: DepartmentRow }>(
        `/${routeBase}/save`,
        body
      );
      const created = (data?.data ?? data) as DepartmentRow;
      dispatch(addItemInList(created));
      return created;
    } catch (e: any) {
      const msg =
        e?.response?.data?.message ||
        e?.message ||
        'Failed to create department';
      dispatch(setCreateError(msg));
      throw e;
    } finally {
      dispatch(setCreating(false));
    }
  };

/* ---------------- UPDATE ---------------- */
export type UpdateDepartmentPayload = Partial<CreateDepartmentPayload>;

export const updateDepartmentById =
  (id: number | string, payload: UpdateDepartmentPayload) =>
  async (dispatch: AppDispatch) => {
    const { setUpdating, setUpdateError, updateItemInList } = departmentActions;
    dispatch(setUpdating(true));
    try {
      const body = {
        ...payload,
        // send boolean, defaulting to true if missing
        status:
          typeof payload.status === 'boolean' ? payload.status : true,
      };

      const { data } = await apiClient.put<{ data: DepartmentRow }>(
        `/${routeBase}/update/${id}`,
        body
      );
      const updated = (data?.data ?? data) as DepartmentRow;
      dispatch(updateItemInList(updated));
      return updated;
    } catch (e: any) {
      const msg =
        e?.response?.data?.message ||
        e?.message ||
        'Failed to update department';
      dispatch(setUpdateError(msg));
      throw e;
    } finally {
      dispatch(setUpdating(false));
    }
  };

/* ---------------- DELETE ---------------- */
export const deleteDepartmentById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    const { setDeleting, setDeleteError, removeItemFromList } =
      departmentActions;
    dispatch(setDeleting(true));
    try {
      await apiClient.delete(`/${routeBase}/delete/${id}`);
      dispatch(removeItemFromList(Number(id)));
      return true;
    } catch (e: any) {
      const msg =
        e?.response?.data?.message ||
        e?.message ||
        'Failed to delete department';
      dispatch(setDeleteError(msg));
      throw e;
    } finally {
      dispatch(setDeleting(false));
    }
  };