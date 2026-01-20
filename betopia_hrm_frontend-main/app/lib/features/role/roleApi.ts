import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import {
  rolesAllFetchStart,
  rolesAllFetchSuccess,
  rolesAllFetchFailure,
  roleCreateFailure,
  roleCreateStart,
  roleCreateSuccess,
  roleDeleteFailure,
  roleDeleteStart,
  roleDeleteSuccess,
  RoleRow,
  rolesFetchFailure,
  rolesFetchStart,
  rolesFetchSuccess,
  roleShowFailure,
  roleShowStart,
  roleShowSuccess,
  roleUpdateFailure,
  roleUpdateStart,
  roleUpdateSuccess,
} from './roleSlice';

/** GET /v1/roles (paginated) */
export const fetchRoles =
  (pageZeroBased = 0, perPage = 5) =>
  async (dispatch: AppDispatch) => {
    dispatch(rolesFetchStart());
    try {
      // ✅ backend is 1-based
      const res = await apiClient.get('/roles', {
        params: { page: pageZeroBased + 1, perPage, sortDirection: 'DESC' },
      });

      const body = res?.data ?? {};
      const items = Array.isArray(body?.data) ? body.data : [];
   
      const meta = body?.meta ?? {};
      
      const list: RoleRow[] = items.map(
        (r: any): RoleRow => ({
          id: r.id,
          name: r.name,
          level: r.level ?? null,
          guardName: r.guardName ?? null,
          createdDate: r.createdDate ?? null,
          lastModifiedDate: r.lastModifiedDate ?? null,
          permissions: Array.isArray(r.permissions)
            ? r.permissions.map((p: any) => ({
                id: p.id,
                name: p.name,
                level: p.level ?? null,
                guardName: p.guardName ?? null,
              }))
            : [],
        })
      );
      // ✅ Convert to 0-based for frontend
      const totalElements = Number(meta.total ?? list.length);
      const pageSize = Number(meta.perPage ?? perPage);
      const calculatedTotalPages = Math.ceil(totalElements / pageSize);

      const pageInfo = {
        currentPage:
          typeof meta.currentPage === 'number'
            ? Math.max(0, meta.currentPage - 1)
            : pageZeroBased,
        lastPage: Number(meta.lastPage ?? calculatedTotalPages),
        perPage: pageSize,
        total: totalElements,
        from: meta.from ?? null,
        to: meta.to ?? null,
      };

      dispatch(rolesFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to fetch roles.';
      dispatch(rolesFetchFailure(msg));
    }
  };

  /** ✅ GET /v1/roles/all (no pagination) */
export const fetchAllRoles = () => async (dispatch: AppDispatch) => {
  dispatch(rolesAllFetchStart());
  try {
    const res = await apiClient.get('/roles/all');
    // //  console.log('rolesss',res)
    const items = Array.isArray(res?.data?.data) ? res.data.data : [];
    //  console.log('items',items)
    const list: RoleRow[] = items.map((r: any) => ({
      id: r.id,
      level: r.level,
      name: r.name,
      guardName: r.guardName ?? null,
      createdDate: r.createdDate ?? null,
      lastModifiedDate: r.lastModifiedDate ?? null,
      permissions: Array.isArray(r.permissions)
        ? r.permissions.map((p: any) => ({
            id: p.id,
            name: p.name,
            level: p.level,
            guardName: p.guardName ?? null,
          }))
        : [],
    }));
    dispatch(rolesAllFetchSuccess(list));
  } catch (err: any) {
    const msg =
      err?.response?.data?.message ??
      err?.message ??
      'Failed to fetch all roles.';
    dispatch(rolesAllFetchFailure(msg));
  }
};


/** POST /v1/roles  (Swagger: { id, name, permissions:number[], level?:string }) */
export const createRole =
  (name: string, permissionIds: number[], level?: string) =>
  async (dispatch: AppDispatch) => {
    dispatch(roleCreateStart());
    try {
      const res = await apiClient.post('/roles', {
        id: 0,
        name,
        permissions: permissionIds,
        ...(level && { level }),
      });

      const body = res?.data ?? {};
      const r = body?.data ?? {};
      const created: RoleRow = {
        id: r.id ?? name,
        name: r.name ?? name,
        guardName: r.guardName ?? null,
        createdDate: r.createdDate ?? null,
        lastModifiedDate: r.lastModifiedDate ?? null,
        permissions: Array.isArray(r.permissions)
          ? r.permissions.map((p: any) => ({
              id: p.id,
              name: p.name,
              guardName: p.guardName ?? null,
            }))
          : [],
      };

      dispatch(roleCreateSuccess(created));

      // ✅ return message & success back to the component
      return {
        success: body?.success ?? true,
        message: body?.message ?? 'Role created successfully.',
      };
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create role.';
      dispatch(roleCreateFailure(msg));
      return { success: false, message: msg };
    }
  };


/** GET /v1/roles/{id} (optional used if you need to refetch one) */
export const getRoleById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(roleShowStart());
    try {
      const res = await apiClient.get(`/roles/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      const role: RoleRow = {
        id: r.id,
        name: r.name,
        guardName: r.guardName ?? null,
        createdDate: r.createdDate ?? null,
        lastModifiedDate: r.lastModifiedDate ?? null,
        permissions: Array.isArray(r.permissions)
          ? r.permissions.map((p: any) => ({
              id: p.id,
              name: p.name,
              guardName: p.guardName ?? null,
            }))
          : [],
      };
      dispatch(roleShowSuccess(role));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to fetch role.';
      dispatch(roleShowFailure(msg));
    }
  };

/** PUT /v1/roles/{id}  (body: { name, permissions, level? }) */
export const updateRoleById =
  (id: number | string, payload: { name: string; permissionIds: number[]; level?: string }) =>
  async (dispatch: AppDispatch) => {
    dispatch(roleUpdateStart());
    try {
      const res = await apiClient.put(`/roles/${id}`, {
        id,
        name: payload.name,
        permissions: payload.permissionIds,
        ...(payload.level && { level: payload.level }),
      });

      const body = res?.data ?? {};
      const r = body?.data ?? {};
      const updated: RoleRow = {
        id: r.id ?? id,
        name: r.name ?? payload.name,
        guardName: r.guardName ?? null,
        createdDate: r.createdDate ?? null,
        lastModifiedDate: r.lastModifiedDate ?? null,
        permissions: Array.isArray(r.permissions)
          ? r.permissions.map((p: any) => ({
              id: p.id,
              name: p.name,
              guardName: p.guardName ?? null,
            }))
          : [],
      };

      dispatch(roleUpdateSuccess(updated));

      return {
        success: body?.success ?? true,
        message: body?.message ?? 'Role updated successfully.',
      };
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update role.';
      dispatch(roleUpdateFailure(msg));
      return { success: false, message: msg };
    }
  };


/** DELETE /v1/roles/{id} */
export const deleteRoleById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(roleDeleteStart());
    try {
      const res = await apiClient.delete(`/roles/${id}`);
      const body = res?.data ?? {};

      dispatch(roleDeleteSuccess(String(id)));

      return {
        success: body?.success ?? true,
        message: body?.message ?? 'Role deleted successfully.',
      };
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete role.';
      dispatch(roleDeleteFailure(msg));
      return { success: false, message: msg };
    }
  };

