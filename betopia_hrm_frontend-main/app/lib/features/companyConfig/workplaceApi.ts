import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import {
  // list
  workplaceFetchFailure,
  workplaceFetchStart,
  workplaceFetchSuccess,
  clearWorkplaceList,
  // create
  workplaceCreateFailure,
  workplaceCreateStart,
  workplaceCreateSuccess,
  // show
  workplaceShowFailure,
  workplaceShowStart,
  workplaceShowSuccess,
  // update
  workplaceUpdateFailure,
  workplaceUpdateStart,
  workplaceUpdateSuccess,
  // delete
  workplaceDeleteFailure,
  workplaceDeleteStart,
  workplaceDeleteSuccess,
} from './workplaceSlice';
import type {
  WorkplaceRow,
  WorkplaceStatus,
  PageInfo,
  WorkplaceGroupRef,
} from './workplaceSlice';

/* ---------- Base path ---------- */
const RESOURCE = '/workplaces';

/* ---------- Helpers ---------- */
function normalizeStatus(s: unknown): WorkplaceStatus {
  if (typeof s === 'boolean') return s ? 'ACTIVE' : 'INACTIVE';
  if (typeof s === 'string') {
    const u = s.trim().toUpperCase();
    if (u === 'ACTIVE') return 'ACTIVE';
    if (u === 'INACTIVE') return 'INACTIVE';
  }
  return 'ACTIVE'; // default instead of null
}

function mapGroup(raw: any): WorkplaceGroupRef | null {
  if (!raw || typeof raw !== 'object') return null;
  return {
    id: raw.id,
    name: raw.name ?? null,
  };
}

/* ---------- Mapper (API -> FE) ---------- */
function mapRow(raw: any): WorkplaceRow {
  const id =
    Number(raw?.id ?? raw?.workplaceId ?? raw?.wpId ?? 0) || 0;

  return {
    id,
    workplaceGroupId:
      Number(
        raw?.workplaceGroupId ??
          raw?.groupId ??
          raw?.workplaceGroup?.id ??
          0
      ) || null,
    workplaceGroup: mapGroup(raw?.workplaceGroup) ?? null,

    name: raw?.name ?? '',
    code: raw?.code ?? null,
    address: raw?.address ?? null,
    description: raw?.description ?? null,
    status: normalizeStatus(raw?.status),

    createdDate: raw?.createdDate ?? null,
    lastModifiedDate: raw?.lastModifiedDate ?? null,
    deletedAt: raw?.deletedAt ?? null,
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

/* ---------- Thunks ---------- */

/** GET /workplaces (paginated) */
export const fetchWorkplaces =
  (page?: number, size?: number) =>
  async (dispatch: AppDispatch) => {
    dispatch(workplaceFetchStart());
    try {
      const res = await apiClient.get(RESOURCE, {
        params: page != null && size != null ? { page, perPage: size } : undefined,
      });
      const body = res?.data ?? {};
      const items = Array.isArray(body?.data)
        ? body.data
        : Array.isArray(body)
        ? body
        : [];
      const list: WorkplaceRow[] = items.map(mapRow);
      const pageInfo = mapPage(body?.meta, items.length);

      dispatch(workplaceFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch workplaces.';
      dispatch(workplaceFetchFailure(msg));
    }
  };

/** Clear local list/page/error */
export const clearWorkplaces = () => (dispatch: AppDispatch) => {
  dispatch(clearWorkplaceList());
};

/** GET /workplaces/{id} */
export const getWorkplaceById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(workplaceShowStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(workplaceShowSuccess(mapRow(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch workplace.';
      dispatch(workplaceShowFailure(msg));
    }
  };

/** POST /workplaces
 * Request body now expects boolean status (default true).
 */
export const createWorkplace =
  (payload: {
    workplaceGroupId: number | string;
    name: string;
    code?: string | null;
    address?: string | null;
    description?: string | null;
    status?: boolean; // boolean in request
  }) =>
  async (dispatch: AppDispatch) => {
    dispatch(workplaceCreateStart());
    try {
      const body = {
        workplaceGroupId: Number(payload.workplaceGroupId),
        name: payload.name,
        code: payload.code ?? null,
        address: payload.address ?? null,
        description: payload.description ?? null,
        status:
          typeof payload.status === 'boolean' ? payload.status : true,
      };

      const res = await apiClient.post(RESOURCE, body);
      const raw = res?.data?.data ?? res?.data ?? {};
      const mapped = mapRow(raw);

      // If API didn't return an id, do a list refresh to stay consistent
      if (!mapped.id) {
        await dispatch<any>(fetchWorkplaces(1, 10));
      } else {
        dispatch(workplaceCreateSuccess(mapped));
      }
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create workplace.';
      dispatch(workplaceCreateFailure(msg));
    }
  };

/** PUT /workplaces/{id}
 * Send boolean status; include even if false.
 */
export const updateWorkplaceById =
  (
    id: number | string,
    payload: Partial<{
      workplaceGroupId: number | string;
      name: string;
      code: string | null;
      address: string | null;
      description: string | null;
      status: boolean; // boolean in request
    }>
  ) =>
  async (dispatch: AppDispatch) => {
    dispatch(workplaceUpdateStart());
    try {
      const body: any = {
        ...(payload.workplaceGroupId != null
          ? { workplaceGroupId: Number(payload.workplaceGroupId) }
          : {}),
        ...(payload.name != null ? { name: payload.name } : {}),
        ...(payload.code !== undefined ? { code: payload.code } : {}),
        ...(payload.address !== undefined ? { address: payload.address } : {}),
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

      dispatch(workplaceUpdateSuccess(safe));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update workplace.';
      dispatch(workplaceUpdateFailure(msg));
    }
  };

/** DELETE /workplaces/{id} */
export const deleteWorkplaceById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(workplaceDeleteStart());
    try {
      await apiClient.delete(`${RESOURCE}/${id}`);
      dispatch(workplaceDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete workplace.';
      dispatch(workplaceDeleteFailure(msg));
    }
  };