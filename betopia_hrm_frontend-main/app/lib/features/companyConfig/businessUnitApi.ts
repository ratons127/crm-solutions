// lib/features/businessUnit/businessUnitApi.ts
import apiClient from '../../../services/apiClient';
import { AppDispatch } from '../../store';
import {
  BusinessUnitRow,
  PageInfo,
  unitsFetchFailure,
  unitsFetchStart,
  unitsFetchSuccess,
  unitCreateFailure,
  unitCreateStart,
  unitCreateSuccess,
  unitDeleteFailure,
  unitDeleteStart,
  unitDeleteSuccess,
  unitShowFailure,
  unitShowStart,
  unitShowSuccess,
  unitUpdateFailure,
  unitUpdateStart,
  unitUpdateSuccess,
} from './businessUnitSlice';

const RESOURCE = '/business-unit';

/* ------------ helpers ------------ */
function mapPageInfo(meta: any, fallbackCount: number): PageInfo | null {
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

/** Normalize server payload into your slice shape (status as 'ACTIVE' | 'INACTIVE' | null) */
function mapBusinessUnit(raw: any): BusinessUnitRow {
  const companyObj = raw?.company;

  const companyId =
    raw?.companyId ??
    (companyObj && (companyObj.id ?? companyObj.companyId)) ??
    null;

  const companyName =
    raw?.companyName ??
    (companyObj && (companyObj.name ?? companyObj.companyName)) ??
    null;

  const companyShortName =
    raw?.companyShortName ??
    (companyObj && (companyObj.shortName ?? companyObj.companyShortName)) ??
    null;

  const companyCode =
    raw?.companyCode ??
    (companyObj && (companyObj.code ?? companyObj.companyCode)) ??
    null;

  // Convert any boolean to 'ACTIVE'/'INACTIVE' to match your slice type
  let status: 'ACTIVE' | 'INACTIVE' | null = null;
  if (typeof raw?.status === 'boolean') {
    status = raw.status ? 'ACTIVE' : 'INACTIVE';
  } else if (typeof raw?.status === 'string') {
    const s = raw.status.trim().toUpperCase();
    if (s === 'ACTIVE') status = 'ACTIVE';
    else if (s === 'INACTIVE') status = 'INACTIVE';
  }

  return {
    id: raw.id ?? raw.unitId ?? raw.businessUnitId,
    companyId: companyId ?? null,
    companyName: companyName ?? null,
    companyShortName: companyShortName ?? null,
    companyCode: companyCode ?? null,

    name: raw.name ?? '',
    code: raw.code ?? raw.unitCode ?? null,
    description: raw.description ?? null,
    status,

    createdDate: raw.createdDate ?? null,
    lastModifiedDate: raw.lastModifiedDate ?? null,
    deletedAt: raw.deletedAt ?? null,
  };
}

/* -------------------- Thunks (CRUD) -------------------- */

/** GET /business-unit (paginated) */
export const fetchBusinessUnits =
  (page?: number, size?: number) => async (dispatch: AppDispatch) => {
    dispatch(unitsFetchStart());
    try {
      const res = await apiClient.get(RESOURCE, {
        params: page != null && size != null ? { page, perPage: size } : undefined,
      });

      const body = res?.data ?? {};
      const items = Array.isArray(body?.data) ? body.data : [];
      const list: BusinessUnitRow[] = items.map(mapBusinessUnit);
      const pageInfo = mapPageInfo(body?.meta, items.length);

      dispatch(unitsFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch business units.';
      dispatch(unitsFetchFailure(msg));
    }
  };

/** GET /business-unit/all (no pagination) */
export const fetchAllBusinessUnits = () => async (dispatch: AppDispatch) => {
  dispatch(unitsFetchStart());
  try {
    const res = await apiClient.get(`${RESOURCE}/all`);
    const body = res?.data ?? {};
    const items = Array.isArray(body?.data) ? body.data : [];
    const list: BusinessUnitRow[] = items.map(mapBusinessUnit);
    dispatch(unitsFetchSuccess({ list, page: null }));
  } catch (err: any) {
    const msg =
      err?.response?.data?.message ??
      err?.message ??
      'Failed to fetch all business units.';
    dispatch(unitsFetchFailure(msg));
  }
};

/** GET /business-unit/{id} */
export const getBusinessUnitById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(unitShowStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(unitShowSuccess(mapBusinessUnit(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch business unit.';
      dispatch(unitShowFailure(msg));
    }
  };

/** POST /business-unit */
export const createBusinessUnit =
  (payload: {
    company: number | string;
    name: string;
    code?: string | null;
    description?: string | null;
    /** boolean in request body */
    status?: boolean;
  }) =>
  async (dispatch: AppDispatch) => {
    dispatch(unitCreateStart());
    try {
      const res = await apiClient.post(RESOURCE, {
        company: Number(payload.company),
        name: payload.name,
        code: payload.code ?? null,
        description: payload.description ?? null,
        // send boolean; default true if not provided
        status: typeof payload.status === 'boolean' ? payload.status : true,
      });
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(unitCreateSuccess(mapBusinessUnit(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create business unit.';
      dispatch(unitCreateFailure(msg));
    }
  };

/** PUT /business-unit/{id} */
export const updateBusinessUnitById =
  (
    id: number | string,
    payload: Partial<{
      company: number | string;
      name: string;
      code: string | null;
      description: string | null;
      /** boolean in request body */
      status: boolean;
    }>
  ) =>
  async (dispatch: AppDispatch) => {
    dispatch(unitUpdateStart());
    try {
      const body: any = {
        ...(payload.company != null ? { company: Number(payload.company) } : {}),
        ...(payload.name != null ? { name: payload.name } : {}),
        ...(payload.code !== undefined ? { code: payload.code } : {}),
        ...(payload.description !== undefined
          ? { description: payload.description }
          : {}),
      };
      // include status even if false
      if (payload.status !== undefined) body.status = payload.status;

      const res = await apiClient.put(`${RESOURCE}/${id}`, body);
      const r = res?.data?.data ?? res?.data ?? {};
      dispatch(unitUpdateSuccess(mapBusinessUnit(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update business unit.';
      dispatch(unitUpdateFailure(msg));
    }
  };

/** DELETE /business-unit/{id} */
export const deleteBusinessUnitById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(unitDeleteStart());
    try {
      await apiClient.delete(`${RESOURCE}/${id}`);
      dispatch(unitDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete business unit.';
      dispatch(unitDeleteFailure(msg));
    }
  };