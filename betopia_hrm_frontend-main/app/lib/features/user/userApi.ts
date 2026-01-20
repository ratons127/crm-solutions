import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import {
  permissionDeleteFailure,
  permissionDeleteStart,
  permissionDeleteSuccess,
  // Permissions list/create/show/update/delete
  PermissionRow,
  permissionsCreateFailure,
  permissionsCreateStart,
  permissionsCreateSuccess,
  permissionsFetchFailure,
  permissionsFetchStart,
  permissionsFetchSuccess,
  permissionShowFailure,
  permissionShowStart,
  permissionShowSuccess,
  permissionUpdateFailure,
  permissionUpdateStart,
  permissionUpdateSuccess,
  // Users create
  userCreateFailure,
  userCreateStart,
  userCreateSuccess,
  // Users update/delete
  userDeleteFailure,
  userDeleteStart,
  userDeleteSuccess,
  // Types
  UserRow,
  // Users list
  usersFetchFailure,
  usersFetchStart,
  usersFetchSuccess,
  userUpdateFailure,
  userUpdateStart,
  userUpdateSuccess,
} from './userSlice';

/* ======================= Envelope Types ======================= */
type Meta = {
  currentPage?: number; // 1-based
  from?: number;
  lastPage?: number;
  path?: string;
  perPage?: number;
  to?: number;
  total?: number;
};

type Envelope<T> = {
  data?: T;
  meta?: Meta;
  message?: string;
  success?: boolean;
  statusCode?: number;
  links?: Record<string, unknown>;
};

/* ======================= Helpers ======================= */
const toPermissionRow = (p: any): PermissionRow => ({
  id: p?.id ?? p?.name,
  name: p?.name,
  guardName: p?.guardName ?? null,
  createdDate: p?.createdDate ?? null,
  lastModifiedDate: p?.lastModifiedDate ?? null,
  selfHref: null,
});

const toUserRow = (u: any): UserRow => ({
  id: u?.id,
  name: u?.name,
  email: u?.email,
  phone: u?.phone ?? u?.phoneNumber ?? u?.mobile ?? null,
  avatar: u?.avatar ?? null,
  role: u?.roles?.name ?? u?.role ?? null,
  lastLogin: u?.lastLogin ?? null,
  twoStep:
    u?.active === true || u?.isActive === true
      ? 'Enabled'
      : u?.active === false || u?.isActive === false
        ? 'Disabled'
        : (u?.twoStep ?? null),
  joinedDate: u?.createdDate ?? u?.created_at ?? null,
});

// strip undefined fields so we don't overwrite with undefined
const clean = <T extends Record<string, any>>(obj: T) =>
  Object.fromEntries(
    Object.entries(obj).filter(([, v]) => v !== undefined)
  ) as T;

/* ======================= USERS ======================= */
export const fetchUsers =
  (pageZeroBased = 0, perPage = 5, keyword: string = '') =>
  async (dispatch: AppDispatch) => {
    dispatch(usersFetchStart());
    try {
      const res = await apiClient.get<Envelope<any>>('/users', {
        params: {
          page: pageZeroBased + 1,
          perPage,
          sortDirection: 'DESC',
          keyword: keyword.trim() || undefined, // 👈 added
        },
      });

      const payload = res.data ?? {};
      const rows = Array.isArray(payload.data) ? payload.data : [];
      const meta = payload.meta ?? {};

      const list: UserRow[] = rows.map(toUserRow);

      const totalElements = meta.total ?? list.length;
      const pageSize = meta.perPage ?? perPage;
      const calculatedTotalPages = Math.ceil(totalElements / pageSize);

      const page = {
        size: pageSize,
        totalElements,
        totalPages: meta.lastPage ?? calculatedTotalPages,
        number:
          typeof meta.currentPage === 'number'
            ? Math.max(0, meta.currentPage - 1) // make frontend 0-based
            : pageZeroBased,
      };

      dispatch(usersFetchSuccess({ list, page }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch users.';
      dispatch(usersFetchFailure(msg));
    }
  };

/* -------- CREATE USER (POST /v1/users) -------- */
export const createUser =
  (payload: {
    name: string;
    email: string;
    phone?: string;
    password: string;
    roleId?: number;
    isActive?: boolean;
    companyId?: number;
    branchId?: number;
    employeeSerialId?: number;
  }) =>
  async (dispatch: AppDispatch) => {
    dispatch(userCreateStart());
    try {
      const res = await apiClient.post<Envelope<any>>('/users', clean(payload));
      const u = res.data?.data ?? res.data;
      dispatch(userCreateSuccess(toUserRow(u)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create user.';
      dispatch(userCreateFailure(msg));
    }
  };

/* -------- UPDATE USER (PUT /v1/users/{id}) -------- */
export const updateUserById =
  (
    id: number | string,
    body: Partial<{
      name: string;
      email: string;
      active: boolean;
      avatar: string | null;
    }>
  ) =>
  async (dispatch: AppDispatch) => {
    dispatch(userUpdateStart());
    try {
      const res = await apiClient.put<Envelope<any>>(
        `/users/${id}`,
        clean(body)
      );
      const u = res.data?.data ?? res.data;
      dispatch(userUpdateSuccess(toUserRow(u)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update user.';
      dispatch(userUpdateFailure(msg));
    }
  };

/* -------- DELETE USER (DELETE /v1/users/{id}) -------- */
export const deleteUserById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(userDeleteStart());
    try {
      await apiClient.delete(`/users/${id}`);
      dispatch(userDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete user.';
      dispatch(userDeleteFailure(msg));
    }
  };

/* ======================= PERMISSIONS ======================= */
export const fetchPermissions =
  (pageZeroBased = 0, perPage = 10) =>
  async (dispatch: AppDispatch) => {
    dispatch(permissionsFetchStart());
    try {
      // API is 1-based -> UI is 0-based
      const res = await apiClient.get<Envelope<any[]>>('/permissions/all', {
        params: { page: pageZeroBased + 1, perPage, sortDirection: 'DESC' },
      });

      const payload = res.data ?? {};
      const rows = Array.isArray(payload.data) ? payload.data : [];
      const meta = payload.meta ?? {};

      const list: PermissionRow[] = rows.map(toPermissionRow);

      const totalElements = meta.total ?? list.length;
      const pageSize = meta.perPage ?? perPage;
      const calculatedTotalPages = Math.ceil(totalElements / pageSize);

      const page = {
        size: pageSize,
        totalElements: totalElements,
        totalPages: meta.lastPage ?? calculatedTotalPages,
        number:
          typeof meta.currentPage === 'number'
            ? Math.max(0, meta.currentPage - 1)
            : pageZeroBased,
      };

      dispatch(permissionsFetchSuccess({ list, page }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Failed to fetch permissions.';
      dispatch(permissionsFetchFailure(msg));
    }
  };

export const createPermission =
  (name: string, guardName = 'api') =>
  async (dispatch: AppDispatch) => {
    dispatch(permissionsCreateStart());
    try {
      const res = await apiClient.post<Envelope<any>>('/permissions', {
        name,
        guardName,
      });
      const p = res.data?.data ?? res.data;
      dispatch(permissionsCreateSuccess(toPermissionRow(p)));
    } catch (err: any) {
      dispatch(
        permissionsCreateFailure(
          err?.response?.data?.message ||
            err?.message ||
            'Failed to create permission.'
        )
      );
    }
  };

export const getPermissionById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(permissionShowStart());
    try {
      const res = await apiClient.get<Envelope<any>>(`/permissions/${id}`);
      const p = res.data?.data ?? res.data;
      dispatch(permissionShowSuccess(toPermissionRow(p)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Failed to fetch permission.';
      dispatch(permissionShowFailure(msg));
    }
  };

export const updatePermissionById =
  (id: number | string, body: { name: string; guardName?: string }) =>
  async (dispatch: AppDispatch) => {
    dispatch(permissionUpdateStart());
    try {
      const res = await apiClient.put<Envelope<any>>(
        `/permissions/${id}`,
        body
      );
      const p = res.data?.data ?? res.data;
      dispatch(permissionUpdateSuccess(toPermissionRow(p)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Failed to update permission.';
      dispatch(permissionUpdateFailure(msg));
    }
  };

export const deletePermissionById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(permissionDeleteStart());
    try {
      await apiClient.delete(`/permissions/${id}`);
      dispatch(permissionDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Failed to delete permission.';
      dispatch(permissionDeleteFailure(msg));
    }
  };
