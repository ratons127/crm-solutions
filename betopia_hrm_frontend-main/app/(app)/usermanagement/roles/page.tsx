'use client';

import Modal from '@/components/common/Modal';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';

import {
  createRole,
  deleteRoleById,
  fetchRoles,
  updateRoleById,
} from '@/lib/features/role/roleApi';
import { RoleRow } from '@/lib/features/role/roleSlice';
import { fetchPermissions } from '@/lib/features/user/userApi';
import { useAppDispatch, useAppSelector } from '@/lib/hooks';
import { RootState } from '@/lib/store';
import { LockClosedIcon } from '@heroicons/react/24/outline';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { Button, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { useSelector } from 'react-redux';

const ACTION_ORDER = [
  'create',
  'edit',
  'update',
  'delete',
  'list',
  'read',
  'view',
] as const;

/* -------------------------------------------------------------------------- */
/*                              Create Role Modal                             */
/* -------------------------------------------------------------------------- */
function CreateRoleModal({
  open,
  onClose,
  onCreate,
  busy,
  error,
  success,
  availablePermissions,
}: {
  open: boolean;
  onClose: () => void;
  onCreate: (name: string, permissionIds: number[], level?: string) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
  availablePermissions: { id: number; name: string }[];
}) {
  const [name, setName] = useState('');
  const [level, setLevel] = useState<string>('');
  const [search, setSearch] = useState('');
  const [selected, setSelected] = useState<Set<number>>(new Set());

  // Get current user's level
  const { user } = useAppSelector(s => s.auth);
  const levelStr = user?.roles?.level?.toString();
  const myLevel = Number(levelStr.match(/\d+/)?.[0] || 0);

  type PermItem = { id: number; resource: string; action: string; raw: string };
  type Group = { resource: string; items: PermItem[] };

  const normalize = (s: string) =>
    s
      .toLowerCase()
      .replace(/[.:_\s]+/g, '-')
      .replace(/-+/g, '-');
  const parse = useCallback(
    (name: string): { resource: string; action: string } | null => {
      const n = normalize(name);
      const parts = n.split('-').filter(Boolean);
      if (parts.length < 2) return null;
      const action = parts[parts.length - 1];
      const resource = parts.slice(0, -1).join('-');
      return { resource, action };
    },
    []
  );
  const toIdx = (a: string) => {
    const i = ACTION_ORDER.indexOf(a as any);
    return i === -1 ? 999 : i;
  };

  const groups: Group[] = useMemo(() => {
    const lower = search.toLowerCase();
    const parsed: PermItem[] = (availablePermissions || [])
      .filter(p => p.name.toLowerCase().includes(lower))
      .map(p => {
        const pa = parse(p.name);
        if (!pa) return null;
        const canonical =
          pa.action === 'read'
            ? 'list'
            : pa.action === 'view'
              ? 'list'
              : pa.action;
        return {
          id: p.id,
          resource: pa.resource,
          action: canonical,
          raw: p.name,
        };
      })
      .filter(Boolean) as PermItem[];

    const byRes: Record<string, PermItem[]> = {};
    parsed.forEach(it => {
      byRes[it.resource] ??= [];
      byRes[it.resource].push(it);
    });
    return Object.keys(byRes)
      .sort()
      .map(res => ({
        resource: res,
        items: byRes[res]
          .slice()
          .sort(
            (a, b) =>
              toIdx(a.action) - toIdx(b.action) || a.raw.localeCompare(b.raw)
          ),
      }));
  }, [availablePermissions, parse, search]);

  const pretty = (s: string) =>
    s.replace(/[-_]/g, ' ').replace(/\b\w/g, m => m.toUpperCase());
  const allIds = useMemo(
    () => groups.flatMap(g => g.items.map(i => i.id)),
    [groups]
  );
  const allChecked = allIds.length > 0 && allIds.every(id => selected.has(id));
  const allIndeterminate =
    allIds.length > 0 && !allChecked && allIds.some(id => selected.has(id));

  const groupIds = (g: Group) => g.items.map(i => i.id);
  const groupChecked = (g: Group) => groupIds(g).every(id => selected.has(id));
  const groupIndeterminate = (g: Group) => {
    const ids = groupIds(g);
    const c = ids.filter(id => selected.has(id)).length;
    return c > 0 && c < ids.length;
  };

  const toggleId = (id: number) => {
    if (busy || success) return;
    setSelected(prev => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  };
  const setMany = (ids: number[], checked: boolean) => {
    setSelected(prev => {
      const next = new Set(prev);
      ids.forEach(id => (checked ? next.add(id) : next.delete(id)));
      return next;
    });
  };

  // Validate role name - only allow letters and numbers
  const handleNameChange = (value: string) => {
    // Only allow letters (a-z, A-Z) and numbers (0-9)
    const filteredValue = value.replace(/[^a-zA-Z0-9]/g, '');
    setName(filteredValue);
  };

  // auto-select all when SuperAdmin typed
  useEffect(() => {
    const normalized = name
      .trim()
      .toLowerCase()
      .replace(/[\s_-]+/g, '');
    if (normalized === 'superadmin' && availablePermissions.length > 0) {
      setSelected(new Set(availablePermissions.map(p => p.id)));
    }
  }, [name, availablePermissions]);

  if (!open) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Create role" size="4xl">
      <div className="space-y-4 max-h-[70vh] overflow-y-auto pr-1">
        {/* Role name */}
        <div>
          <label className="block text-sm text-gray-600 mb-1">
            Role name <span className="text-red-500">*</span>
          </label>
          <input
            className="w-full rounded-md border border-gray-200 px-3 py-2 text-sm"
            placeholder="e.g. Manager"
            value={name}
            onChange={e => handleNameChange(e.target.value)}
            disabled={busy || success}
          />
          <p className="text-xs text-gray-500 mt-1">
            Only letters and numbers are allowed
          </p>
        </div>

        {/* Level */}
        <div>
          <label className="block text-sm text-gray-600 mb-1">
            Level{' '}
            {myLevel >= 0 && (
              <span className="text-xs text-gray-400">
                (must be greater than {myLevel})
              </span>
            )}
          </label>
          <input
            type="number"
            className="w-full rounded-md border border-gray-200 px-3 py-2 text-sm"
            placeholder={`Enter level number (min: ${myLevel + 1})`}
            value={level.replace('level-', '')}
            onChange={e => {
              const num = e.target.value;
              const numValue = Number(num);

              // Validate: must be greater than myLevel
              if (num && numValue <= myLevel) {
                return; // Don't update if <= myLevel
              }

              setLevel(num ? `level-${num}` : '');
            }}
            disabled={busy || success}
            min={myLevel + 1}
          />
          {level &&
            level !== '' &&
            Number(level.replace('level-', '')) <= myLevel && (
              <p className="text-xs text-red-600 mt-1">
                Level must be greater than your level ({myLevel})
              </p>
            )}
        </div>

        {/* 🔍 Search */}
        <div className="flex justify-between items-center gap-3 py-2">
          <input
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search permissions..."
            className="flex-1 border border-gray-200 rounded-md px-3 py-2 text-sm"
            disabled={busy || success}
          />
          {search && (
            <button
              type="button"
              className="text-xs text-gray-500 hover:text-gray-700"
              onClick={() => setSearch('')}
            >
              Clear
            </button>
          )}
        </div>

        {/* Select all */}
        <div className="flex items-center justify-between py-2">
          <label className="inline-flex items-center gap-2 text-sm">
            <input
              type="checkbox"
              className="h-4 w-4 accent-blue-600"
              checked={allChecked}
              ref={el => {
                if (el) el.indeterminate = allIndeterminate;
              }}
              onChange={e => setMany(allIds, e.target.checked)}
              disabled={busy || success || allIds.length === 0}
            />
            Select all
          </label>
        </div>

        {/* Permissions */}
        {groups.length === 0 ? (
          <p className="text-sm text-gray-500">
            {search
              ? 'No matching permissions found.'
              : 'No permissions available.'}
          </p>
        ) : (
          groups.map(g => (
            <div key={g.resource} className="rounded-lg border border-gray-100">
              <div className="flex items-center justify-between px-3 py-2 bg-gray-50 rounded-t-lg">
                <div className="font-medium text-gray-800">
                  {pretty(g.resource)}
                </div>
                <label className="inline-flex items-center gap-2 text-sm">
                  <input
                    type="checkbox"
                    className="h-4 w-4 accent-blue-600"
                    checked={groupChecked(g)}
                    ref={el => {
                      if (el) el.indeterminate = groupIndeterminate(g);
                    }}
                    onChange={e => setMany(groupIds(g), e.target.checked)}
                    disabled={busy || success || g.items.length === 0}
                  />
                  Select all
                </label>
              </div>
              <div className="px-4 py-3 flex flex-wrap gap-4">
                {g.items.map(item => (
                  <label
                    key={item.id}
                    className="inline-flex items-center gap-2 text-sm text-gray-800"
                  >
                    <input
                      type="checkbox"
                      className="h-4 w-4 accent-blue-600"
                      checked={selected.has(item.id)}
                      onChange={() => toggleId(item.id)}
                      disabled={busy || success}
                    />
                    <span>
                      {pretty(`${item.action} ${item.resource}`)}
                      <span className="ml-1 text-xs text-gray-400">
                        ({item.raw})
                      </span>
                    </span>
                  </label>
                ))}
              </div>
            </div>
          ))
        )}

        {!!error && <p className="text-sm text-red-600">{error}</p>}

        <div className="border-t mt-4 pt-4 flex justify-end gap-4 bg-white border-gray-200 sticky bottom-0 py-3 px-2">
          <Button onClick={onClose} color="red" type="button" disabled={busy}>
            Cancel
          </Button>
          <Button
            onClick={() => {
              const trimmed = name.trim();

              const normalized = trimmed.toLowerCase().replace(/[\s_-]+/g, '');
              const totalPermissions = new Set(
                availablePermissions.map(p => p.id)
              );
              if (
                normalized === 'superadmin' &&
                selected.size < totalPermissions.size
              ) {
                notifications.show({
                  title: 'Missing permissions',
                  message:
                    'SuperAdmin must have all permissions selected before saving.',
                  color: 'red',
                });
                return;
              }

              onCreate(
                trimmed,
                Array.from(selected),
                level && level.trim() !== '' ? level : undefined
              );
            }}
            disabled={busy || !name.trim() || success}
          >
            {busy ? 'Creating…' : 'Create'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* ------------------------------- Edit Modal ------------------------------- */
function EditRoleModal({
  open,
  onClose,
  role,
  availablePermissions,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  role: RoleRow | null;
  availablePermissions: { id: number; name: string }[];
  onConfirm: (payload: {
    name: string;
    permissionIds: number[];
    level?: string;
  }) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  const [name, setName] = useState('');
  const [level, setLevel] = useState<string>('');
  const [search, setSearch] = useState('');
  const [selected, setSelected] = useState<Set<number>>(new Set());

  // Get current user's level
  const { user } = useAppSelector(s => s.auth);
  const levelStr = user?.roles?.level?.toString() || 'level-0';
  const myLevel = Number(levelStr.match(/\d+/)?.[0] || 0);

  const mergedPermissions = useMemo(() => {
    const map = new Map<number, { id: number; name: string }>();
    (availablePermissions || []).forEach(p =>
      map.set(Number(p.id), { id: Number(p.id), name: p.name })
    );
    (role?.permissions || []).forEach(p => {
      const id = Number(p.id);
      if (!map.has(id)) map.set(id, { id, name: p.name });
    });
    return Array.from(map.values());
  }, [availablePermissions, role]);

  type PermItem = { id: number; resource: string; action: string; raw: string };
  type Group = { resource: string; items: PermItem[] };

  const normalize = (s: string) =>
    s
      .toLowerCase()
      .replace(/[.:_\s]+/g, '-')
      .replace(/-+/g, '-');
  const parse = useCallback(
    (name: string): { resource: string; action: string } | null => {
      const n = normalize(name);
      const parts = n.split('-').filter(Boolean);
      if (parts.length < 2) return null;
      const action = parts[parts.length - 1];
      const resource = parts.slice(0, -1).join('-');
      return { resource, action };
    },
    []
  );
  const toIdx = (a: string) => {
    const i = ACTION_ORDER.indexOf(a as any);
    return i === -1 ? 999 : i;
  };

  const groups: Group[] = useMemo(() => {
    const lower = search.toLowerCase();
    const parsed: PermItem[] = (mergedPermissions || [])
      .filter(p => p.name.toLowerCase().includes(lower))
      .map(p => {
        const pa = parse(p.name);
        if (!pa) return null;
        const canonical =
          pa.action === 'read'
            ? 'list'
            : pa.action === 'view'
              ? 'list'
              : pa.action;
        return {
          id: Number(p.id),
          resource: pa.resource,
          action: canonical,
          raw: p.name,
        };
      })
      .filter(Boolean) as PermItem[];

    const byRes: Record<string, PermItem[]> = {};
    parsed.forEach(it => {
      byRes[it.resource] ??= [];
      byRes[it.resource].push(it);
    });
    return Object.keys(byRes)
      .sort()
      .map(res => ({
        resource: res,
        items: byRes[res]
          .slice()
          .sort(
            (a, b) =>
              toIdx(a.action) - toIdx(b.action) || a.raw.localeCompare(b.raw)
          ),
      }));
  }, [mergedPermissions, parse, search]);

  const pretty = (s: string) =>
    s.replace(/[-_]/g, ' ').replace(/\b\w/g, m => m.toUpperCase());
  const allIds = useMemo(
    () => groups.flatMap(g => g.items.map(i => i.id)),
    [groups]
  );
  const allChecked = allIds.length > 0 && allIds.every(id => selected.has(id));
  const allIndeterminate =
    allIds.length > 0 && !allChecked && allIds.some(id => selected.has(id));
  const groupIds = (g: Group) => g.items.map(i => i.id);
  const groupChecked = (g: Group) => groupIds(g).every(id => selected.has(id));
  const groupIndeterminate = (g: Group) => {
    const ids = groupIds(g);
    const c = ids.filter(id => selected.has(id)).length;
    return c > 0 && c < ids.length;
  };
  const toggleId = (id: number) => {
    if (busy || success) return;
    setSelected(prev => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  };
  const setMany = (ids: number[], checked: boolean) => {
    setSelected(prev => {
      const next = new Set(prev);
      ids.forEach(id => (checked ? next.add(id) : next.delete(id)));
      return next;
    });
  };

  // Validate role name - only allow letters and numbers
  const handleNameChange = (value: string) => {
    // Only allow letters (a-z, A-Z) and numbers (0-9)
    const filteredValue = value.replace(/[^a-zA-Z0-9]/g, '');
    setName(filteredValue);
  };

  useEffect(() => {
    if (open && role) {
      setName(role.name ?? '');
      setLevel(role.level != null ? String(role.level) : '');
      setSearch('');
      const pre = new Set<number>(
        (role.permissions || [])
          .map(p => Number(p.id))
          .filter(n => !Number.isNaN(n))
      );
      setSelected(pre);
    }
  }, [open, role]);

  if (!open || !role) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit role" size="4xl">
      <div className="space-y-4 max-h-[70vh] overflow-y-auto pr-1">
        <div>
          <label className="block text-sm text-gray-600 mb-1">Role name</label>
          <input
            className="w-full rounded-md border border-gray-200 px-3 py-2 text-sm"
            value={name}
            onChange={e => handleNameChange(e.target.value)}
            disabled={busy || success}
          />
        </div>

        {/* Level */}
        <div>
          <label className="block text-sm text-gray-600 mb-1">
            Level{' '}
            {myLevel >= 0 && (
              <span className="text-xs text-gray-400">
                (must be greater than {myLevel})
              </span>
            )}
          </label>
          <input
            type="number"
            className="w-full rounded-md border border-gray-200 px-3 py-2 text-sm"
            placeholder={`Enter level number (min: ${myLevel + 1})`}
            value={level.replace('level-', '')}
            onChange={e => {
              const num = e.target.value;
              const numValue = Number(num);

              if (num && numValue <= myLevel) {
                return;
              }
              setLevel(num ? `level-${num}` : '');
            }}
            disabled={busy || success}
            min={myLevel + 1}
          />
          {level &&
            level !== '' &&
            Number(level.replace('level-', '')) <= myLevel && (
              <p className="text-xs text-red-600 mt-1">
                Level must be greater than your level ({myLevel})
              </p>
            )}
        </div>

        {/* 🔍 Search */}
        <div className="flex justify-between items-center gap-3 py-2">
          <input
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search permissions..."
            className="flex-1 border border-gray-200 rounded-md px-3 py-2 text-sm"
            disabled={busy || success}
          />
          {search && (
            <button
              type="button"
              className="text-xs text-gray-500 hover:text-gray-700"
              onClick={() => setSearch('')}
            >
              Clear
            </button>
          )}
        </div>

        <div className="flex items-center justify-between py-2">
          <label className="inline-flex items-center gap-2 text-sm">
            <input
              type="checkbox"
              className="h-4 w-4 accent-blue-600"
              checked={allChecked}
              ref={el => {
                if (el) el.indeterminate = allIndeterminate;
              }}
              onChange={e => setMany(allIds, e.target.checked)}
              disabled={busy || success || allIds.length === 0}
            />
            Select all
          </label>
        </div>

        {groups.length === 0 ? (
          <p className="text-sm text-gray-500">
            {search
              ? 'No matching permissions found.'
              : 'No permissions available.'}
          </p>
        ) : (
          groups.map(g => (
            <div key={g.resource} className="rounded-lg border border-gray-100">
              <div className="flex items-center justify-between px-3 py-2 bg-gray-50 rounded-t-lg">
                <div className="font-medium text-gray-800">{`${pretty(g.resource)}`}</div>
                <label className="inline-flex items-center gap-2 text-sm">
                  <input
                    type="checkbox"
                    className="h-4 w-4 accent-blue-600"
                    checked={groupChecked(g)}
                    ref={el => {
                      if (el) el.indeterminate = groupIndeterminate(g);
                    }}
                    onChange={e => setMany(groupIds(g), e.target.checked)}
                    disabled={busy || success || g.items.length === 0}
                  />
                  Select all
                </label>
              </div>
              <div className="px-4 py-3 flex flex-wrap gap-4">
                {g.items.map(item => (
                  <label
                    key={item.id}
                    className="inline-flex items-center gap-2 text-sm text-gray-800"
                  >
                    <input
                      type="checkbox"
                      className="h-4 w-4 accent-blue-600"
                      checked={selected.has(item.id)}
                      onChange={() => toggleId(item.id)}
                      disabled={busy || success}
                    />
                    <span>
                      {pretty(`${item.action} ${item.resource}`)}
                      <span className="ml-1 text-xs text-gray-400">
                        ({item.raw})
                      </span>
                    </span>
                  </label>
                ))}
              </div>
            </div>
          ))
        )}

        {!!error && <p className="text-sm text-red-600">{error}</p>}
        <div className="border-t mt-4 pt-4 flex justify-end gap-4 bg-white border-gray-200 sticky bottom-0 py-3 px-2">
          <Button onClick={onClose} disabled={busy} color="red" type="button">
            {success ? 'Close now' : 'Cancel'}
          </Button>
          <Button
            onClick={() => {
              const trimmed = name.trim();
              const normalized = trimmed.toLowerCase().replace(/[\s_-]+/g, '');
              const totalPermissions = new Set(
                availablePermissions.map(p => p.id)
              );
              if (
                normalized === 'superadmin' &&
                selected.size < totalPermissions.size
              ) {
                notifications.show({
                  title: 'Missing permissions',
                  message:
                    'SuperAdmin must have all permissions selected before saving.',
                  color: 'red',
                });
                return;
              }

              onConfirm({
                name: trimmed,
                permissionIds: Array.from(selected),
                level: level && level.trim() !== '' ? level : undefined,
              });
            }}
            disabled={busy || !name.trim() || success}
          >
            {busy ? 'Saving…' : 'Save changes'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* ------------------------------ Delete Modal ------------------------------ */
function DeleteRoleModal({
  open,
  onClose,
  role,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  role: RoleRow | null;
  onConfirm: () => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  if (!open || !role) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete role">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete{' '}
        <span className="font-medium">{role.name}</span>?
      </p>

      {!!error && <p className="mt-2 text-sm text-red-600">{error}</p>}
      {success && !error && (
        <p className="mt-2 text-sm text-green-600">Role deleted. Closing…</p>
      )}

      <div className="mt-4 flex items-center justify-end gap-2">
        <button
          onClick={onClose}
          className="px-3 py-1.5 rounded-md border text-sm"
          disabled={busy}
        >
          {success ? 'Close now' : 'Cancel'}
        </button>
        <button
          onClick={onConfirm}
          className="px-3 py-1.5 rounded-md bg-red-600 text-white text-sm disabled:opacity-60"
          disabled={busy || success}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </button>
      </div>
    </Modal>
  );
}

/* ---------------------------------- Page ---------------------------------- */
export default function RolesPage() {
  const dispatch = useAppDispatch();

  const {
    list,
    loading,
    creating,
    createError,
    updating,
    updateError,
    deleting,
    deleteError,
    page,
  } = useSelector((s: RootState) => s.roles);
  // const { canCreate, canEdit, canDelete } = useResourcePermissions('role');
  const usersPermissions = useSelector((s: RootState) => s.users.permissions);

  const availablePermissions = useMemo(
    () =>
      usersPermissions?.map((p: any) => ({ id: Number(p.id), name: p.name })) ??
      [],
    [usersPermissions]
  );

  const [openCreate, setOpenCreate] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [editing, setEditing] = useState<RoleRow | null>(null);
  const [deletingTarget, setDeletingTarget] = useState<RoleRow | null>(null);

  useEffect(() => {
    dispatch(fetchRoles(0, 10)); // Start with page 0, size 5
    if (!availablePermissions.length) dispatch<any>(fetchPermissions(0, 1000));
  }, [dispatch]);

  // Create Handler
  const handleCreate = async (
    name: string,
    permissionIds: number[],
    level?: string
  ) => {
    const result = await dispatch<any>(createRole(name, permissionIds, level));

    if (!result?.success) {
      notifications.show({
        title: 'Failed',
        message: result?.message || 'Failed to create role.',
        color: 'red',
      });
      return;
    }
    notifications.show({
      title: 'Success',
      message: result?.message || 'Role created successfully.',
      color: 'green',
    });
    setOpenCreate(false);
    dispatch(fetchRoles(0, 10));
  };

  // Delete Handler
  const handleDeleteConfirm = async () => {
    if (!deletingTarget) return;
    const result = await dispatch<any>(deleteRoleById(deletingTarget.id));
    if (!result?.success) {
      notifications.show({
        title: 'Failed',
        message: result?.message || 'Failed to delete role.',
        color: 'red',
      });
      return;
    }
    notifications.show({
      title: 'Deleted',
      message: result?.message || 'Role deleted successfully.',
      color: 'green',
    });
    setOpenDelete(false);
    dispatch(fetchRoles(0, 10));
  };

  // Edit Handler
  const handleEditConfirm = async (payload: {
    name: string;
    permissionIds: number[];
    level?: string;
  }) => {
    if (!editing) return;
    try {
      const result = await dispatch<any>(updateRoleById(editing.id, payload));
      if (!result?.success) {
        notifications.show({
          title: 'Failed',
          message: result?.message || 'Failed to update role.',
          color: 'red',
        });
        return;
      }
      notifications.show({
        title: 'Success',
        message: result?.message || 'Role updated successfully.',
        color: 'green',
      });
      setOpenEdit(false);
      dispatch(fetchRoles(0, 10));
    } catch (err: any) {
      notifications.show({
        title: 'Error',
        message: err?.message || 'Unexpected error occurred.',
        color: 'red',
      });
    }
  };

  // simple success flags expected by the modals; adjust logic if you track successes differently
  const editSuccess = false;
  const deleteSuccess = false;

  const columns: ColumnDef<RoleRow, any>[] = useMemo(
    () => [
      {
        id: 'select',
        enableSorting: false,
        enableHiding: false,
        header: ({ table }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={table.getIsAllPageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        ),
        cell: ({ row }) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={row.getIsSelected()}
            disabled={!row.getCanSelect()}
            onChange={row.getToggleSelectedHandler()}
          />
        ),
      },
      { accessorKey: 'id', header: 'ID' },
      { accessorKey: 'name', header: 'Role' },
      {
        id: 'permissions',
        header: 'Permissions',
        cell: ({ row }) => {
          const perms = (row.original.permissions || []).map(p => p.name);
          if (perms.length === 0)
            return <span className="text-gray-400">—</span>;
          const first = perms.slice(0, 3).join(', ');
          const more = perms.length - 3;
          return (
            <span className="text-sm text-gray-700">
              {first}
              {more > 0 ? ` +${more} more` : ''}
            </span>
          );
        },
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          const normalized = row.original.name
            .trim()
            .toLowerCase()
            .replace(/[\s_-]+/g, '');
          const isProtected = ['superadmin', 'employee'].includes(normalized);
          const actions: any[] = [];

          // Always allow Edit
          actions.push({
            label: 'Edit',
            icon: <PencilSquareIcon height={16} />,
            action: () => {
              dispatch<any>(fetchPermissions(0, 1000));
              setEditing(row.original);
              setOpenEdit(true);
            },
          });

          // Only allow Delete if NOT protected
          if (!isProtected) {
            actions.push({
              label: 'Delete',
              icon: <TrashIcon height={16} />,
              action: () => {
                modals.openConfirmModal({
                  title: 'Delete confirmation',
                  centered: true,
                  children: (
                    <Text size="sm">
                      Are you sure you want to delete this role?
                    </Text>
                  ),
                  labels: { confirm: 'Delete', cancel: 'Cancel' },
                  confirmProps: { color: 'red' },
                  onConfirm: () => {
                    setDeletingTarget(row.original);
                    setOpenDelete(true);
                  },
                });
              },
            });
          }

          // Optional: visually mark protected roles
          if (isProtected) {
            actions.push({
              label: 'Protected Role',
              icon: <LockClosedIcon height={16} className="text-gray-400" />,
              disabled: true,
            });
          }

          return (
            <RowActionDropdown data={actions}>
              <BsThreeDots />
            </RowActionDropdown>
          );
        },
      },
    ],
    [dispatch]
  );

  // const handleCreate = async (name: string, permissionIds: number[]) => {
  //   setJustSubmittedCreate(true);
  //   await dispatch<any>(createRole(name, permissionIds));
  // };

  // const handleEditConfirm = async (payload: { name: string; permissionIds: number[] }) => {
  //   if (!editing) return;
  //   setJustSubmittedEdit(true);
  //   await dispatch<any>(updateRoleById(editing.id, payload));
  // };

  // const handleDeleteConfirm = async () => {
  //   if (!deletingTarget) return;
  //   setJustSubmittedDelete(true);
  //   await dispatch<any>(deleteRoleById(deletingTarget.id));
  // };

  return (
    <>
      <div className="p-6 bg-white rounded-lg shadow max-w-full">
        <DataTable<RoleRow>
          data={list}
          columns={columns}
          searchPlaceholder="Search roles..."
          csvFileName="roles"
          onPressAdd={() => {
            dispatch<any>(fetchPermissions(0, 1000));
            setOpenCreate(true);
          }}
          manualPagination
          pagination={{
            pageIndex: page?.currentPage ?? 0,
            pageSize: page?.perPage ?? 5,
          }}
          onPaginationChange={next =>
            dispatch(fetchRoles(next.pageIndex, next.pageSize))
          }
          pageCount={page?.lastPage ?? 1}
          totalRows={page?.total ?? list.length}
          loading={loading}
        />

        {/* Modals */}
        <CreateRoleModal
          open={openCreate}
          onClose={() => setOpenCreate(false)}
          onCreate={handleCreate}
          busy={creating}
          error={createError}
          success={false}
          availablePermissions={availablePermissions}
        />
        {/* Edit */}
        <EditRoleModal
          open={openEdit}
          onClose={() => setOpenEdit(false)}
          role={editing}
          availablePermissions={availablePermissions}
          onConfirm={handleEditConfirm}
          busy={updating}
          error={updateError}
          success={editSuccess}
        />
        {/* Delete */}
        <DeleteRoleModal
          open={openDelete}
          onClose={() => setOpenDelete(false)}
          role={deletingTarget}
          onConfirm={handleDeleteConfirm}
          busy={deleting}
          error={deleteError}
          success={deleteSuccess}
        />
      </div>
    </>
  );
}
