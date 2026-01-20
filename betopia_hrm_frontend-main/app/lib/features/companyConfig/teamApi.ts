// lib/features/team/teamApi.ts
import apiClient from '@/services/apiClient';
import { AppDispatch } from '../../store';
import { TeamRow, PageInfo, teamActions } from './teamSlice';

export type ListParams = {
  page?: number;
  size?: number;
  sort?: string;
  q?: string;
};

const routeBase = 'teams';

function toQuery(params: Record<string, any>) {
  const s = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== '') s.append(k, String(v));
  });
  return s.toString();
}

/* ---------------- READ: Paginated list ---------------- */
export const fetchTeams =
  (page = 1, size = 10, extra: Omit<ListParams, 'page' | 'size'> = {}) =>
  async (dispatch: AppDispatch) => {
    const { setLoading, setError, setList, setPage } = teamActions;
    dispatch(setLoading(true));
    try {
      const query = toQuery({ page, perPage: size, ...extra });
      const { data } = await apiClient.get<any>(`/${routeBase}?${query}`);

      const list: TeamRow[] = Array.isArray(data)
        ? data
        : (data?.data as TeamRow[]) ?? [];

      const meta = data?.meta ?? null;
      const pageInfo: PageInfo = meta
        ? {
            currentPage: Number(meta.currentPage ?? page),
            lastPage: Number(meta.lastPage ?? 1),
            total: Number(meta.total ?? list.length),
          }
        : {
            currentPage: page,
            lastPage: Math.max(1, Math.ceil((list?.length ?? 0) / size)),
            total: list?.length ?? 0,
          };

      dispatch(setList(list));
      dispatch(setPage(pageInfo));
    } catch (e: any) {
      const msg =
        e?.response?.data?.message || e?.message || 'Failed to load teams';
      dispatch(setError(msg));
    } finally {
      dispatch(setLoading(false));
    }
  };

/* ---------------- READ: Get one ---------------- */
export const getTeamById = async (id: number | string) => {
  const { data } = await apiClient.get<{ data: TeamRow }>(`/${routeBase}/${id}`);
  return (data?.data ?? data) as TeamRow;
};

/* ---------------- CREATE ---------------- */
export type CreateTeamPayload = {
  id?: number | null;
  departmentId: number;
  name: string;
  code?: string | null;
  description?: string | null;
  status: boolean;
};

export const createTeam =
  (payload: CreateTeamPayload) => async (dispatch: AppDispatch) => {
    const { setCreating, setCreateError, addItemInList } = teamActions;
    dispatch(setCreating(true));
    try {
      const body = {
        ...payload,
        status: !!payload.status,
      };

      const { data } = await apiClient.post<{ data: TeamRow }>(
        `/${routeBase}`,
        body
      );
      const created = (data?.data ?? data) as TeamRow;
      dispatch(addItemInList(created));
      return created;
    } catch (e: any) {
      const msg =
        e?.response?.data?.message || e?.message || 'Failed to create team';
      dispatch(setCreateError(msg));
      throw e;
    } finally {
      dispatch(setCreating(false));
    }
  };

/* ---------------- UPDATE ---------------- */
export type UpdateTeamPayload = Partial<CreateTeamPayload>;

export const updateTeamById =
  (id: number | string, payload: UpdateTeamPayload) =>
  async (dispatch: AppDispatch) => {
    const { setUpdating, setUpdateError, updateItemInList } = teamActions;
    dispatch(setUpdating(true));
    try {
      const body = {
        ...payload,
        status: !!payload.status,
      };

      const { data } = await apiClient.put<{ data: TeamRow }>(
        `/${routeBase}/${id}`,
        body
      );
      const updated = (data?.data ?? data) as TeamRow;
      dispatch(updateItemInList(updated));
      return updated;
    } catch (e: any) {
      const msg =
        e?.response?.data?.message || e?.message || 'Failed to update team';
      dispatch(setUpdateError(msg));
      throw e;
    } finally {
      dispatch(setUpdating(false));
    }
  };

/* ---------------- DELETE ---------------- */
export const deleteTeamById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    const { setDeleting, setDeleteError, removeItemFromList } = teamActions;
    dispatch(setDeleting(true));
    try {
      await apiClient.delete(`/${routeBase}/${id}`);
      dispatch(removeItemFromList(Number(id)));
      return true;
    } catch (e: any) {
      const msg =
        e?.response?.data?.message || e?.message || 'Failed to delete team';
      dispatch(setDeleteError(msg));
      throw e;
    } finally {
      dispatch(setDeleting(false));
    }
  };