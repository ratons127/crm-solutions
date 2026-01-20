// lib/features/leave/leaveApply/leaveApplyApi.ts
import { notifications } from '@mantine/notifications';
import apiClient from '../../../../services/apiClient';
import { AppDispatch } from '../../../store';
import type {
  LeaveApplyRow,
  LeaveTypeRow,
  PageInfo,
  LeaveGroupAssignRow,
} from './leaveApplySlice';
import {
  clearEmpList,
  clearLeaveList,
  empFetchFailure,
  empFetchStart,
  empFetchSuccess,
  leaveCreateFailure,
  leaveCreateStart,
  leaveCreateSuccess,
  leaveDeleteFailure,
  leaveDeleteStart,
  leaveDeleteSuccess,
  leaveFetchFailure,
  leaveFetchStart,
  leaveFetchSuccess,
  leaveShowFailure,
  leaveShowStart,
  leaveShowSuccess,
  leaveUpdateFailure,
  leaveUpdateStart,
  leaveUpdateSuccess,
} from './leaveApplySlice';

/** Base path from Swagger */
const RESOURCE = '/leave-requests';

/** ---------- Mappers (API -> FE) ---------- */
function mapLeaveType(raw: any): LeaveTypeRow | null {
  if (!raw || typeof raw !== 'object') return null;
  return {
    id: raw.id,
    name: raw.name ?? '',
    code: raw.code ?? '',
    active: Boolean(raw.active),
    createdDate: raw.createdDate ?? null,
    lastModifiedDate: raw.lastModifiedDate ?? null,
    deletedAt: raw.deletedAt ?? null,
  };
}

function mapLeaveGroupAssign(raw: any): LeaveGroupAssignRow | null {
  if (!raw || typeof raw !== 'object') return null;
  return {
    id: raw.id,
    leaveGroup: raw.leaveGroup ?? null,
    name: raw.name ?? raw.leaveGroup?.name ?? null,
  };
}

function mapLeaveApply(raw: any): LeaveApplyRow {
  return {
    id: raw.id,
    employeeId: raw.employeeId,

    leaveType: mapLeaveType(raw.leaveType),

    // NEW: support both object and id-only from backend
    leaveGroupAssign: mapLeaveGroupAssign(raw.leaveGroupAssign),
    leaveGroupAssignId:
      raw.leaveGroupAssignId ??
      (raw.leaveGroupAssign && raw.leaveGroupAssign.id) ??
      null,

    startDate: raw.startDate,
    endDate: raw.endDate,
    daysRequested: Number(raw.daysRequested ?? 0),

    reason: raw.reason ?? null,
    proofDocumentPath: raw.proofDocumentPath ?? null,
    justification: raw.justification ?? null,

    status: raw.status ?? 'PENDING',

    requestedAt: raw.requestedAt ?? null,
    approvedBy: raw.approvedBy ?? null,
    approvedAt: raw.approvedAt ?? null,

    createdBy: raw.createdBy ?? null,
    lastModifiedBy: raw.lastModifiedBy ?? null,
    createdDate: raw.createdDate ?? null,
    lastModifiedDate: raw.lastModifiedDate ?? null,
    deletedAt: raw.deletedAt ?? null,
  };
}

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

/** ---------- Thunks ---------- */

/** GET /v1/leave-requests (paginated) */
export const fetchLeaveApplies =
  (page?: number, size?: number) => async (dispatch: AppDispatch) => {
    dispatch(leaveFetchStart());
    try {
      const res = await apiClient.get(RESOURCE, {
        params: page != null && size != null ? { page, size } : undefined,
      });

      const body = res?.data ?? {};
      const items = Array.isArray(body?.data) ? body.data : [];
      const list: LeaveApplyRow[] = items.map(mapLeaveApply);

      const pageInfo = mapPageInfo(body?.meta, items.length);

      dispatch(leaveFetchSuccess({ list, page: pageInfo }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch leave requests.';
      dispatch(leaveFetchFailure(msg));
    }
  };

/** GET /v1/leave-requests/all (no pagination) */
export const fetchAllLeaveApplies = () => async (dispatch: AppDispatch) => {
  dispatch(leaveFetchStart());
  try {
    const res = await apiClient.get(`${RESOURCE}/all`);
    const body = res?.data ?? {};
    const items = Array.isArray(body?.data) ? body.data : [];
    const list: LeaveApplyRow[] = items.map(mapLeaveApply);

    dispatch(leaveFetchSuccess({ list, page: null }));
  } catch (err: any) {
    const msg =
      err?.response?.data?.message ??
      err?.message ??
      'Failed to fetch all leave requests.';
    dispatch(leaveFetchFailure(msg));
  }
};

/** GET /v1/leave-requests/{id} */
export const getLeaveApplyById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(leaveShowStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/${id}`);
      const r = res?.data?.data ?? res?.data ?? {};
      const row = mapLeaveApply(r);
      dispatch(leaveShowSuccess(row));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch leave request.';
      dispatch(leaveShowFailure(msg));
    }
  };

/** GET /v1/leave-requests/employee/{employeeId} */
export const getLeaveApplyByEmployee =
  (employeeId: number | string) => async (dispatch: AppDispatch) => {
    dispatch(empFetchStart());
    try {
      const res = await apiClient.get(`${RESOURCE}/employee/${employeeId}`);
      const body = res?.data ?? {};
      const items = Array.isArray(body?.data) ? body.data : [];
      const list: LeaveApplyRow[] = items.map(mapLeaveApply);

      dispatch(empFetchSuccess({ list, page: null }));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to fetch leave requests by employee.';
      dispatch(empFetchFailure(msg));
    }
  };

/** POST /v1/leave-requests/save */
/** POST /v1/leave-requests/save */
export const createLeaveApply =
  (payload: {
    employeeId: number | string;
    leaveTypeId: number | string;
    leaveGroupAssignId: number | string;

    startDate: string;
    endDate: string;
    daysRequested?: number;
    reason?: string;
    proofDocument?: File | null; // 👈 actual file
    justification?: string | null;
    hasProof?: boolean;
  }) =>
  async (dispatch: AppDispatch) => {
    dispatch(leaveCreateStart());
    try {
      const formData = new FormData();

      formData.append('employeeId', String(payload.employeeId));
      formData.append('leaveTypeId', String(payload.leaveTypeId));
      formData.append('leaveGroupAssignId', String(payload.leaveGroupAssignId));

      formData.append('startDate', payload.startDate);
      formData.append('endDate', payload.endDate);
      if (payload.daysRequested != null)
        formData.append('daysRequested', String(payload.daysRequested));
      if (payload.reason) formData.append('reason', payload.reason);
      if (payload.justification)
        formData.append('justification', payload.justification);
      formData.append('hasProof', String(payload.hasProof ?? !!payload.proofDocument));

      if (payload.proofDocument) {
        formData.append('proofDocument', payload.proofDocument); // 👈 real file here
      }

      const res = await apiClient.post(`${RESOURCE}/save`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      const r = res?.data?.data ?? res?.data ?? {};
      const created = mapLeaveApply(r);
      dispatch(leaveCreateSuccess(created));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to create leave request.';
      dispatch(leaveCreateFailure(msg));
    }
  };

/** PUT /v1/leave-requests/update/{id} */
export const updateLeaveApplyById =
  (
    id: number | string,
    payload: Partial<{
      employeeId: number | string;
      leaveTypeId: number | string;
      // NEW (optional on update)
      leaveGroupAssignId: number | string;

      startDate: string;
      endDate: string;
      daysRequested: number;
      reason: string;
      proofDocumentPath: string | null;
      justification: string | null;
      status: string;
    }>
  ) =>
  async (dispatch: AppDispatch) => {
    dispatch(leaveUpdateStart());
    try {
      const res = await apiClient.put(`${RESOURCE}/update/${id}`, payload);
      const r = res?.data?.data ?? res?.data ?? {};
      const updated = mapLeaveApply(r);
      dispatch(leaveUpdateSuccess(updated));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to update leave request.';
      dispatch(leaveUpdateFailure(msg));
    }
  };

/** DELETE /v1/leave-requests/delete/{id} */
export const deleteLeaveApplyById =
  (id: number | string) => async (dispatch: AppDispatch) => {
    dispatch(leaveDeleteStart());
    try {
      await apiClient.delete(`${RESOURCE}/delete/${id}`);
      notifications.show({
        title: 'Deleted',
        message: 'Leave apply deleted.',
        color: 'red',
      });
      dispatch(leaveDeleteSuccess(String(id)));
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.message ??
        'Failed to delete leave request.';
      notifications.show({
        title: 'Delete',
        message: `Leave deletion failed`,
      });
      dispatch(leaveDeleteFailure(msg));
    }
  };

/** ---------- Optional helpers to clear lists ---------- */
export const clearLeaveRequests = () => (dispatch: AppDispatch) => {
  dispatch(clearLeaveList());
};
export const clearEmployeeLeaveRequests = () => (dispatch: AppDispatch) => {
  dispatch(clearEmpList());
};
