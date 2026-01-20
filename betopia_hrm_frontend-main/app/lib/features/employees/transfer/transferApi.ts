import apiClient from '@/services/apiClient';
import { AppDispatch } from '@/lib/store';
import {
  TransferRow,
  TransferCreatePayload,
  TransferUpdatePayload,
  transferCreateFailure,
  transferCreateStart,
  transferCreateSuccess,
  transferDeleteFailure,
  transferDeleteStart,
  transferDeleteSuccess,
  transferFetchFailure,
  transferFetchStart,
  transferFetchSuccess,
  transferShowFailure,
  transferShowStart,
  transferShowSuccess,
  transferUpdateFailure,
  transferUpdateStart,
  transferUpdateSuccess,
} from './transferSlice';

/* ======================= Envelope Types ======================= */
type Meta = {
  currentPage?: number; // 1-based from API
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

// Remove `undefined` so backend fields aren't overwritten with undefined
const clean = <T extends Record<string, any>>(obj: T) =>
  Object.fromEntries(Object.entries(obj).filter(([, v]) => v !== undefined)) as T;

/* ======================= Mapper ======================= */
const toTransferRow = (r: any): TransferRow => ({
  id: r?.id,
  employeeId: r?.employeeId,

  requestType: r?.requestType,

  fromCompanyId: r?.fromCompanyId ?? null,
  toCompanyId: r?.toCompanyId ?? null,

  fromWorkplaceId: r?.fromWorkplaceId ?? null,
  toWorkplaceId: r?.toWorkplaceId ?? null,

  fromDepartmentId: r?.fromDepartmentId ?? null,
  toDepartmentId: r?.toDepartmentId ?? null,

  fromDesignationId: r?.fromDesignationId ?? null,
  toDesignationId: r?.toDesignationId ?? null,

  reason: r?.reason ?? null,
  effectiveDate: r?.effectiveDate ?? null,
  approvalStatus: r?.approvalStatus ?? null,
  approvedById: r?.approvedById ?? null,
  approvedAt: r?.approvedAt ?? null,
  status: r?.status === 1 || r?.status === true,

  createdDate: r?.createdDate ?? null,
  lastModifiedDate: r?.lastModifiedDate ?? null,
});

/* ======================= API Thunks ======================= */

/** LIST (GET /v1/transfer-request?page=&size=) */
export const fetchTransfers =
  (pageZeroBased = 0, size = 10, extraParams: Record<string, any> = {}) =>
  async (dispatch: AppDispatch) => {
    dispatch(transferFetchStart());
    try {
      const res = await apiClient.get<Envelope<any[]>>('/transfer-request', {
        params: { page: pageZeroBased + 1, size, ...extraParams },
      });

      const payload = res.data ?? {};
      const rows = Array.isArray(payload.data) ? payload.data : [];
      const meta = payload.meta ?? {};

      const list: TransferRow[] = rows.map(toTransferRow);

      const page = {
        size: meta.perPage ?? size,
        totalElements: meta.total ?? list.length,
        totalPages: meta.lastPage ?? 1,
        number:
          typeof meta.currentPage === 'number'
            ? Math.max(0, (meta.currentPage as number) - 1)
            : pageZeroBased,
      };

      dispatch(transferFetchSuccess({ list, page }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch transfer requests.';
      dispatch(transferFetchFailure(msg));
    }
  };

/** SHOW (GET /v1/transfer-request/{id}) */
export const getTransferById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(transferShowStart());
    try {
      const res = await apiClient.get<Envelope<any>>(`/transfer-request/${id}`);
      const r = res.data?.data ?? res.data;
      dispatch(transferShowSuccess(toTransferRow(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch transfer request.';
      dispatch(transferShowFailure(msg));
    }
  };

/** CREATE (POST /v1/transfer-request/save) */
export const createTransfer =
  (body: TransferCreatePayload) =>
  async (dispatch: AppDispatch) => {
    dispatch(transferCreateStart());
    try {
      // convert boolean status to number if backend expects 0/1
      const payload = {
        ...body,
        status:
          typeof body.status === 'boolean'
            ? (body.status ? 1 : 0)
            : body.status,
      };

      const res = await apiClient.post<Envelope<any>>(
        '/transfer-request/save',
        clean(payload)
      );
      const r = res.data?.data ?? res.data;
      dispatch(transferCreateSuccess(toTransferRow(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create transfer request.';
      dispatch(transferCreateFailure(msg));
    }
  };

/** UPDATE (PUT /v1/transfer-request/update/{id}) */
export const updateTransferById =
  (id: number | string, body: TransferUpdatePayload) =>
  async (dispatch: AppDispatch) => {
    dispatch(transferUpdateStart());
    try {
      const payload = {
        ...body,
        status:
          body.status === undefined
            ? undefined
            : typeof body.status === 'boolean'
              ? (body.status ? 1 : 0)
              : body.status,
      };

      const res = await apiClient.put<Envelope<any>>(
        `/transfer-request/update/${id}`,
        clean(payload)
      );
      const r = res.data?.data ?? res.data;
      dispatch(transferUpdateSuccess(toTransferRow(r)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update transfer request.';
      dispatch(transferUpdateFailure(msg));
    }
  };

/** DELETE (DELETE /v1/transfer-request/delete/{id}) */
export const deleteTransferById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(transferDeleteStart());
    try {
      await apiClient.delete(`/transfer-request/delete/${id}`);
      dispatch(transferDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete transfer request.';
      dispatch(transferDeleteFailure(msg));
    }
  };
