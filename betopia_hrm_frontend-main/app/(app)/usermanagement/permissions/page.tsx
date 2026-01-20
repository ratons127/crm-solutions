'use client';

import Modal from '@/components/common/Modal';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  createPermission,
  deletePermissionById,
  fetchPermissions,
  updatePermissionById,
} from '@/lib/features/user/userApi';
import { PermissionRow } from '@/lib/features/user/userSlice';
import { useAppDispatch } from '@/lib/hooks';
import { RootState } from '@/lib/store';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import { Button, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useEffect, useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { useSelector } from 'react-redux';

/* ---------------------------- Create Modal ---------------------------- */
function CreatePermissionModal({
  open,
  onClose,
  onCreate,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  onCreate: (name: string) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  const [name, setName] = useState('');
  useEffect(() => {
    if (open) setName('');
  }, [open]);

  if (!open) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Create permission">
      <label className="block text-sm text-gray-600 mb-1">Name</label>
      <input
        className="w-full rounded-md border px-3 py-2 text-sm"
        placeholder="Permission name"
        value={name}
        onChange={e => setName(e.target.value)}
        disabled={busy || success}
      />

      {!!error && (
        <p className="mt-2 text-sm text-red-600 break-words">{error}</p>
      )}
      {success && !error && (
        <p className="mt-2 text-sm text-green-600">
          Permission created successfully. Closing…
        </p>
      )}

      <div className="flex items-center gap-5 justify-end mt-10">
        <Button onClick={onClose} disabled={busy} color="red" type="button">
          {success ? 'Close now' : 'Cancel'}
        </Button>
        <Button
          onClick={() => onCreate(name.trim())}
          disabled={busy || !name.trim() || success}
          type="submit"
        >
          {busy ? 'Creating…' : 'Create'}
        </Button>
      </div>
    </Modal>
  );
}

/* ----------------------------- Edit Modal ----------------------------- */
function EditPermissionModal({
  open,
  onClose,
  initial,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  initial: PermissionRow | null;
  onConfirm: (payload: { name: string; guardName?: string }) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  const [name, setName] = useState('');
  const [guardName, setGuardName] = useState<string>('api');

  useEffect(() => {
    if (open && initial) {
      setName(initial.name ?? '');
      setGuardName(initial.guardName ?? 'api');
    }
  }, [open, initial]);

  if (!open || !initial) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit permission">
      <div className="space-y-3">
        <div>
          <label className="block text-sm text-gray-600 mb-1">Name</label>
          <input
            className="w-full rounded-md border px-3 py-2 text-sm"
            value={name}
            onChange={e => setName(e.target.value)}
            disabled={busy || success}
          />
        </div>

        {!!error && <p className="text-sm text-red-600 break-words">{error}</p>}
        {success && !error && (
          <p className="text-sm text-green-600">Permission updated. Closing…</p>
        )}

        <div className="flex items-center gap-5 justify-end mt-10">
          <Button onClick={onClose} disabled={busy} color="red" type="button">
            {success ? 'Close now' : 'Cancel'}
          </Button>
          <Button
            onClick={() =>
              onConfirm({
                name: name.trim(),
                guardName: guardName?.trim() || undefined,
              })
            }
            disabled={busy || !name.trim() || success}
            type="submit"
          >
            {busy ? 'Saving…' : 'Save'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* ---------------------------- Delete Modal ---------------------------- */
function DeletePermissionModal({
  open,
  onClose,
  target,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  target: PermissionRow | null;
  onConfirm: () => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  if (!open || !target) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete permission">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete{' '}
        <span className="font-medium">{target.name}</span>?
      </p>

      {!!error && (
        <p className="mt-2 text-sm text-red-600 break-words">{error}</p>
      )}
      {success && !error && (
        <p className="mt-2 text-sm text-green-600">
          Permission deleted. Closing…
        </p>
      )}

      <div className="flex items-center gap-5 justify-end mt-10">
        <Button onClick={onClose} disabled={busy} color="red" type="button">
          {success ? 'Close now' : 'Cancel'}
        </Button>
        <Button onClick={onConfirm} disabled={busy || success} type="submit">
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}

/* ---------------------------------- Page ---------------------------------- */
export default function PermissionsPage() {
  const dispatch = useAppDispatch();

  const {
    permissions,
    permissionsLoading,
    permissionsError,
    permissionsPage,
    creatingPermission,
    createPermissionError,
    updatingPermission,
    updatePermissionError,
    deletingPermission,
    deletePermissionError,
  } = useSelector((s: RootState) => s.users);

  // Controlled (server-side) pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based
  const [pageSize, setPageSize] = useState(10);

  // Fetch when page or size changes
  useEffect(() => {
    dispatch(fetchPermissions(pageIndex, pageSize));
  }, [dispatch, pageIndex, pageSize]);

  // Columns
  const columns: ColumnDef<PermissionRow, any>[] = useMemo(
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
      { accessorKey: 'name', header: 'Permission' },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          const actions = [];
          actions.push({
            label: 'Edit',
            icon: <PencilSquareIcon height={16} />,
            action: () => {
              setEditing(row.original as PermissionRow);
              setOpenEdit(true);
            },
          });
          actions.push({
            label: 'Delete',
            icon: <TrashIcon height={16} />,
            action: () => {
              modals.openConfirmModal({
                title: 'Delete confirmation',
                centered: true,
                children: (
                  <Text size="sm">
                    Are you sure you want to delete? This action is destructive
                    and you will have to contact support to restore your data.
                  </Text>
                ),
                labels: { confirm: 'Delete', cancel: 'Cancel' },
                confirmProps: { color: 'red' },
                onConfirm: async () => {
                  try {
                    setDeletingTarget(row.original as PermissionRow);
                    setOpenDelete(true);
                  } catch (error) {
                    console.error('Error message:', error);
                    notifications.show({
                      title: 'Failed to delete',
                      message: 'Something went wrong',
                    });
                  }
                },
              });
            },
          });

          return (
            <RowActionDropdown data={actions}>
              <BsThreeDots />
            </RowActionDropdown>
          );
        },
      },
    ],
    []
  );

  // Create state/handlers
  const [openCreate, setOpenCreate] = useState(false);
  const [justSubmittedCreate, setJustSubmittedCreate] = useState(false);
  const creationSuccess =
    justSubmittedCreate && !creatingPermission && !createPermissionError;
  const handleCreate = async (name: string) => {
    setJustSubmittedCreate(true);
    await dispatch<any>(createPermission(name));
  };
  useEffect(() => {
    if (!(creationSuccess && openCreate)) return;
    const t = setTimeout(() => {
      setOpenCreate(false);
      setJustSubmittedCreate(false);
      dispatch(fetchPermissions(pageIndex, pageSize));
    }, 1200);
    return () => clearTimeout(t);
  }, [creationSuccess, openCreate, dispatch, pageIndex, pageSize]);

  // Edit state/handlers
  const [openEdit, setOpenEdit] = useState(false);
  const [editing, setEditing] = useState<PermissionRow | null>(null);
  const [justSubmittedEdit, setJustSubmittedEdit] = useState(false);
  const editSuccess =
    justSubmittedEdit && !updatingPermission && !updatePermissionError;
  const handleEditConfirm = async (payload: {
    name: string;
    guardName?: string;
  }) => {
    if (!editing) return;
    setJustSubmittedEdit(true);
    await dispatch<any>(updatePermissionById(editing.id, payload));
  };
  useEffect(() => {
    if (!(editSuccess && openEdit)) return;
    const t = setTimeout(() => {
      setOpenEdit(false);
      setJustSubmittedEdit(false);
      dispatch(fetchPermissions(pageIndex, pageSize));
    }, 800);
    return () => clearTimeout(t);
  }, [editSuccess, openEdit, dispatch, pageIndex, pageSize]);

  // Delete state/handlers
  const [openDelete, setOpenDelete] = useState(false);
  const [deletingTarget, setDeletingTarget] = useState<PermissionRow | null>(
    null
  );
  const [justSubmittedDelete, setJustSubmittedDelete] = useState(false);
  const deleteSuccess =
    justSubmittedDelete && !deletingPermission && !deletePermissionError;
  const handleDeleteConfirm = async () => {
    if (!deletingTarget) return;
    setJustSubmittedDelete(true);
    await dispatch<any>(deletePermissionById(deletingTarget.id));
  };
  useEffect(() => {
    if (!(deleteSuccess && openDelete)) return;
    const t = setTimeout(() => {
      setOpenDelete(false);
      setJustSubmittedDelete(false);
      setDeletingTarget(null);
      dispatch(fetchPermissions(pageIndex, pageSize));
    }, 800);
    return () => clearTimeout(t);
  }, [deleteSuccess, openDelete, dispatch, pageIndex, pageSize]);

  return (
    <>
      <div className="p-6 bg-white rounded-lg shadow max-w-full">
        <DataTable<PermissionRow>
          data={permissions}
          columns={columns}
          searchPlaceholder="Search permissions..."
          csvFileName="permissions"
          initialColumnVisibility={{}}
          // headerLeft={<Breadcrumbs />}
          onPressAdd={() => {
            setOpenCreate(true);
            setJustSubmittedCreate(false);
          }}
          manualPagination
          pageCount={permissionsPage?.totalPages ?? 0}
          pagination={{ pageIndex, pageSize }}
          onPaginationChange={({ pageIndex, pageSize }) => {
            setPageIndex(pageIndex);
            setPageSize(pageSize);
          }}
          totalRows={permissionsPage?.totalElements}
          loading={permissionsLoading}
        />

        {permissionsError && (
          <p className="mt-3 text-sm text-red-600">{permissionsError}</p>
        )}

        {/* Create */}
        <CreatePermissionModal
          open={openCreate}
          onClose={() => setOpenCreate(false)}
          onCreate={handleCreate}
          busy={creatingPermission}
          error={createPermissionError || null}
          success={creationSuccess}
        />

        {/* Edit */}
        <EditPermissionModal
          open={openEdit}
          onClose={() => setOpenEdit(false)}
          initial={editing}
          onConfirm={handleEditConfirm}
          busy={updatingPermission}
          error={updatePermissionError || null}
          success={editSuccess}
        />

        {/* Delete */}
        <DeletePermissionModal
          open={openDelete}
          onClose={() => setOpenDelete(false)}
          target={deletingTarget}
          onConfirm={handleDeleteConfirm}
          busy={deletingPermission}
          error={deletePermissionError || null}
          success={deleteSuccess}
        />
      </div>
    </>
  );
}
