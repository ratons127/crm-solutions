import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import {
  wgFetchStart,
  wgFetchSuccess,
  wgFetchFailure,
  wgClearList,
  wgShowStart,
  wgShowSuccess,
  wgShowFailure,
  wgCreateStart,
  wgCreateSuccess,
  wgCreateFailure,
  wgUpdateStart,
  wgUpdateSuccess,
  wgUpdateFailure,
  wgDeleteStart,
  wgDeleteSuccess,
  wgDeleteFailure,
} from './workplaceGroupSlice';
import type { WorkplaceGroupRow, PageInfo } from './workplaceGroupSlice';

const RESOURCE = '/workplace-group';

/* ------------ Helpers ------------ */
function normalizeStatus(raw: unknown): 'ACTIVE' | 'INACTIVE' | null {
  if (typeof raw === 'boolean') return raw ? 'ACTIVE' : 'INACTIVE';
  if (typeof raw === 'string') {
    const s = raw.trim().toUpperCase();
    if (s === 'ACTIVE') return 'ACTIVE';
    if (s === 'INACTIVE') return 'INACTIVE';
  }
  return null;
}

function mapRow(raw: any): WorkplaceGroupRow {
  // Your API may omit `id` and only return fields shown in the example.
  // Try common keys; fall back to 0.
  const id =
    Number(
      raw?.id ??
        raw?.workplaceGroupId ??
        raw?.groupId ??
        raw?.wgId ??
        0
    ) || 0;

  return {
    id,
    businessUnitId: Number(
      raw?.businessUnitId ?? raw?.businessUnit?.id ?? 0
    ),
    name: raw?.name ?? '',
    code: raw?.code ?? null,
    description: raw?.description ?? null,
    status: normalizeStatus(raw?.status),

    createdDate: raw?.createdDate ?? null,
    lastModifiedDate: raw?.lastModifiedDate ?? null,
    deletedAt: raw?.deletedAt ?? null,
    createdBy: raw?.createdBy ?? null,
    lastModifiedBy: raw?.lastModifiedBy ?? null,
  };
}

function mapPage(meta: any, fallbackCount: number): PageInfo | null {
  if (!meta) return null;

  return {
    currentPage: Number(meta.currentPage ?? 1),
    lastPage: Number(meta.lastPage ?? 1),
    perPage: Number(meta.perPage ?? fallbackCount ?? 10),
    total: Number(meta.total ?? fallbackCount ?? 0),
    from: meta.from ?? null,
    to: meta.to ?? null,
  };
}


/* ------------ Thunks ------------ */

/** GET /workplace-group (paginated) */
export const fetchWorkplaceGroups =
  (page?: number, size?: number) => async (dispatch: AppDispatch) => {
    dispatch(wgFetchStart());
    try {
      const res = await apiClient.get(RESOURCE, {
        params: page != null && size != null ? { page, perPage: size } : undefined,
      });
      const body = res?.data ?? {};
      const items = Array.isArray(body?.data) ? body.data : [];
      const list: WorkplaceGroupRow[] = items.map(mapRow);
      const pageInfo = mapPage(body?.meta, items.length);
      dispatch(wgFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch workplace groups.';
      dispatch(wgFetchFailure(msg));
    }
  };

/** Clear local list/page/error */
export const clearWorkplaceGroups = () => (dispatch: AppDispatch) => {
  dispatch(wgClearList());
};

/** GET /workplace-group/{id} */
export const getWorkplaceGroupById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(wgShowStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(wgShowSuccess(mapRow(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch workplace group.';
      dispatch(wgShowFailure(msg));
    }
  };

/** POST /workplace-group
 *  Send boolean status (default true if omitted)
 */
export const createWorkplaceGroup =
  (payload: {
    businessUnitId: number | string;
    name: string;
    code?: string | null;
    description?: string | null;
    status?: boolean; // boolean in request
  }) =>
  async (dispatch: AppDispatch) => {
    dispatch(wgCreateStart());
    try {
      const res = await apiClient.post(RESOURCE, {
        businessUnitId: Number(payload.businessUnitId),
        name: payload.name,
        code: payload.code ?? null,
        description: payload.description ?? null,
        status:
          typeof payload.status === 'boolean' ? payload.status : true,
      });

      const raw = res?.data?.data ?? res?.data ?? {};
      const mapped = mapRow(raw);

      // If API didn't return an id, do a list refresh to stay consistent
      if (!mapped.id) {
        // fire-and-forget refresh of first page (or choose your current page/size)
        // you can lift `page/size` from component state if needed
        await dispatch<any>(fetchWorkplaceGroups(1, 10));
      } else {
        dispatch(wgCreateSuccess(mapped));
      }
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create workplace group.';
      dispatch(wgCreateFailure(msg));
    }
  };

/** PUT /workplace-group/{id}
 *  Send boolean status; include even if false
 */
export const updateWorkplaceGroupById =
  (
    id: number | string,
    payload: Partial<{
      businessUnitId: number | string;
      name: string;
      code: string | null;
      description: string | null;
      status: boolean; // boolean in request
    }>
  ) =>
  async (dispatch: AppDispatch) => {
    dispatch(wgUpdateStart());
    try {
      const body: any = {
        ...(payload.businessUnitId != null
          ? { businessUnitId: Number(payload.businessUnitId) }
          : {}),
        ...(payload.name != null ? { name: payload.name } : {}),
        ...(payload.code !== undefined ? { code: payload.code } : {}),
        ...(payload.description !== undefined
          ? { description: payload.description }
          : {}),
      };
      if (payload.status !== undefined) body.status = payload.status;

      const res = await apiClient.put(`${RESOURCE}/${id}`, body);
      const raw = res?.data?.data ?? res?.data ?? {};
      const mapped = mapRow(raw);

      // If backend didn’t echo id, ensure we carry the correct one
      const safe = mapped.id ? mapped : { ...mapped, id: Number(id) };

      dispatch(wgUpdateSuccess(safe));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update workplace group.';
      dispatch(wgUpdateFailure(msg));
    }
  };

/** DELETE /workplace-group/{id} */
export const deleteWorkplaceGroupById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(wgDeleteStart());
    try {
      await apiClient.delete(`${RESOURCE}/${id}`);
      dispatch(wgDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete workplace group.';
      dispatch(wgDeleteFailure(msg));
    }
  };