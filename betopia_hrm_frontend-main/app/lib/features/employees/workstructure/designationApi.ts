import apiClient from '../../../../services/apiClient';
import { AppDispatch } from '../../../store';
import {
  PageInfo,
  DesignationRow,
  designationActions,
} from './designationSlice';

const RESOURCE = '/designations';

/* ------------ helpers (mappers) ------------ */

function mapPageInfo(meta: any, fallbackCount: number): PageInfo {
  if (!meta) return null;
  return {
    currentPage: Number(meta.currentPage ?? meta.page ?? 1),
    lastPage: Number(meta.lastPage ?? meta.totalPages ?? 1),
    perPage: Number(meta.perPage ?? meta.size ?? fallbackCount ?? 10),
    total: Number(meta.total ?? fallbackCount ?? 0),
    from: meta.from ?? null,
    to: meta.to ?? null,
  };
}

/** Coerce any legacy status value into a strict boolean */
function normalizeStatus(v: unknown): boolean {
  if (typeof v === 'boolean') return v;
  if (typeof v === 'number') return v !== 0;
  const s = String(v ?? '').trim().toLowerCase();
  if (['active', 'true', '1', 'yes'].includes(s)) return true;
  if (['inactive', 'false', '0', 'no'].includes(s)) return false;
  return Boolean(v);
}

function mapDesignation(raw: any): DesignationRow {
  return {
    id: raw.id ?? raw.designationId ?? raw.uuid ?? '',
    name: raw.name ?? '',
    description: raw.description ?? null,
    status: normalizeStatus(raw.status),        // boolean in state
    createdDate: raw.createdDate ?? raw.created_at ?? null,
    lastModifiedDate: raw.lastModifiedDate ?? raw.updated_at ?? null,
    deletedAt: raw.deletedAt ?? null,
  };
}

/* -------------------- Thunks (CRUD) -------------------- */

/** GET /designations (paginated) */
export const fetchDesignations =
  (page?: number, perPage?: number) =>
  async (dispatch: AppDispatch) => {
    const {
      designationsFetchStart,
      designationsFetchSuccess,
      designationsFetchFailure,
    } = designationActions;

    dispatch(designationsFetchStart());
    try {
      const res = await apiClient.get(RESOURCE, {
        params: page != null && perPage != null ? { page, perPage, sortDirection: 'DESC' } : undefined,
      });

      const body = res?.data ?? {};
      const items = Array.isArray(body?.data) ? body.data : Array.isArray(body) ? body : [];
      const list: DesignationRow[] = items.map(mapDesignation);
      const pageInfo = mapPageInfo(body?.meta, items.length);

      dispatch(designationsFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch designations.';
      dispatch(designationsFetchFailure(msg));
    }
  };

/** GET /designations/{id} */
export const getDesignationById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    const { designationShowStart, designationShowSuccess, designationShowFailure } =
      designationActions;

    dispatch(designationShowStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(designationShowSuccess(mapDesignation(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch designation.';
      dispatch(designationShowFailure(msg));
    }
  };

/** POST /designations */
export const createDesignation =
  (payload: { name: string; description?: string | null; status?: boolean }) =>  // ← boolean
  async (dispatch: AppDispatch) => {
    const { designationCreateStart, designationCreateSuccess, designationCreateFailure } =
      designationActions;

    dispatch(designationCreateStart());
    try {
      const res = await apiClient.post(RESOURCE, {
        name: payload.name,
        description: payload.description ?? null,
        status: payload.status ?? true,   // default true if omitted
      });
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(designationCreateSuccess(mapDesignation(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create designation.';
      dispatch(designationCreateFailure(msg));
    }
  };

/** PUT /designations/{id} */
export const updateDesignationById =
  (
    id: number | string,
    payload: Partial<{ name: string; description: string | null; status: boolean }> // ← boolean
  ) =>
  async (dispatch: AppDispatch) => {
    const { designationUpdateStart, designationUpdateSuccess, designationUpdateFailure } =
      designationActions;

    dispatch(designationUpdateStart());
    try {
      // build body explicitly to avoid sending undefined
      const body: any = {};
      if (payload.name !== undefined) body.name = payload.name;
      if (payload.description !== undefined) body.description = payload.description ?? null;
      if (payload.status !== undefined) body.status = !!payload.status;

      const res = await apiClient.put(`${RESOURCE}/${id}`, body);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(designationUpdateSuccess(mapDesignation(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update designation.';
      dispatch(designationUpdateFailure(msg));
    }
  };

/** DELETE /designations/{id} */
export const deleteDesignationById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    const { designationDeleteStart, designationDeleteSuccess, designationDeleteFailure } =
      designationActions;

    dispatch(designationDeleteStart());
    try {
      await apiClient.delete(`${RESOURCE}/${id}`);
      dispatch(designationDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete designation.';
      dispatch(designationDeleteFailure(msg));
    }
  };
