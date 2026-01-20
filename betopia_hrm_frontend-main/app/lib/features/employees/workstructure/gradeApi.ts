import apiClient from '../../../../services/apiClient';
import { AppDispatch } from '../../../store';
import { PageInfo, GradeRow, gradeActions } from './gradeSlice';

const RESOURCE = '/grades';

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
  if (s === 'active' || s === 'true' || s === '1' || s === 'yes') return true;
  if (s === 'inactive' || s === 'false' || s === '0' || s === 'no') return false;
  // Fallback: truthy -> true
  return Boolean(v);
}

function mapGrade(raw: any): GradeRow {
  return {
    id: raw.id ?? raw.gradeId ?? raw.uuid ?? '',
    code: raw.code ?? '',
    name: raw.name ?? '',
    description: raw.description ?? null,
    status: normalizeStatus(raw.status), // <-- boolean in state
    createdDate: raw.createdDate ?? raw.created_at ?? null,
    lastModifiedDate: raw.lastModifiedDate ?? raw.updated_at ?? null,
    deletedAt: raw.deletedAt ?? null,
  };
}

/* -------------------- Thunks (CRUD) -------------------- */

/** GET /grades (paginated) */
export const fetchGrades =
  (page?: number, perPage?: number) =>
  async (dispatch: AppDispatch) => {
    const { gradesFetchStart, gradesFetchSuccess, gradesFetchFailure } =
      gradeActions;

    dispatch(gradesFetchStart());
    try {
      const res = await apiClient.get(RESOURCE, {
        params: page != null && perPage != null ? { page, perPage, sortDirection: 'DESC' } : undefined,
      });

      const body = res?.data ?? {};
      const items = Array.isArray(body?.data)
        ? body.data
        : Array.isArray(body)
        ? body
        : [];
      const list: GradeRow[] = items.map(mapGrade);
      const pageInfo = mapPageInfo(body?.meta, items.length);

      dispatch(gradesFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to fetch grades.';
      dispatch(gradesFetchFailure(msg));
    }
  };

/** GET /grades/{id} */
export const getGradeById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    const { gradeShowStart, gradeShowSuccess, gradeShowFailure } = gradeActions;

    dispatch(gradeShowStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(gradeShowSuccess(mapGrade(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to fetch grade.';
      dispatch(gradeShowFailure(msg));
    }
  };

/** POST /grades */
export const createGrade =
  (payload: {
    code: string;
    name: string;
    description?: string | null;
    status?: boolean;                      // <-- boolean now
  }) =>
  async (dispatch: AppDispatch) => {
    const { gradeCreateStart, gradeCreateSuccess, gradeCreateFailure } =
      gradeActions;

    dispatch(gradeCreateStart());
    try {
      const res = await apiClient.post(RESOURCE, {
        code: payload.code,
        name: payload.name,
        description: payload.description ?? null,
        status: payload.status ?? true,    // <-- default true if omitted
      });
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(gradeCreateSuccess(mapGrade(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to create grade.';
      dispatch(gradeCreateFailure(msg));
    }
  };

/** PUT /grades/{id} */
export const updateGradeById =
  (
    id: number | string,
    payload: Partial<{
      code: string;
      name: string;
      description: string | null;
      status: boolean;                     // <-- boolean now
    }>
  ) =>
  async (dispatch: AppDispatch) => {
    const { gradeUpdateStart, gradeUpdateSuccess, gradeUpdateFailure } =
      gradeActions;

    dispatch(gradeUpdateStart());
    try {
      // build body carefully so we don't send undefined
      const body: any = {};
      if (payload.code !== undefined) body.code = payload.code;
      if (payload.name !== undefined) body.name = payload.name;
      if (payload.description !== undefined) body.description = payload.description ?? null;
      if (payload.status !== undefined) body.status = !!payload.status; // ensure boolean

      const res = await apiClient.put(`${RESOURCE}/${id}`, body);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(gradeUpdateSuccess(mapGrade(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to update grade.';
      dispatch(gradeUpdateFailure(msg));
    }
  };

/** DELETE /grades/{id} */
export const deleteGradeById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    const { gradeDeleteStart, gradeDeleteSuccess, gradeDeleteFailure } =
      gradeActions;

    dispatch(gradeDeleteStart());
    try {
      await apiClient.delete(`${RESOURCE}/${id}`);
      dispatch(gradeDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ?? err?.message ?? 'Failed to delete grade.';
      dispatch(gradeDeleteFailure(msg));
    }
  };
