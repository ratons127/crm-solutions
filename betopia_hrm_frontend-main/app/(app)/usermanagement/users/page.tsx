'use client';

import Modal from '@/components/common/Modal';
import {
  DataTable,
  SelectFilterDef,
} from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { fetchAllRoles } from '@/lib/features/role/roleApi';
import {
  createUser,
  deleteUserById,
  fetchUsers,
  updateUserById,
} from '@/lib/features/user/userApi';
import { UserRow } from '@/lib/features/user/userSlice';
import { useAppDispatch, useAppSelector } from '@/lib/hooks';
import { RootState } from '@/lib/store';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';
import {
  Button,
  Grid,
  PasswordInput,
  Select,
  Switch,
  Text,
  TextInput,
} from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import Image from 'next/image';
import { useEffect, useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import { useSelector } from 'react-redux';

/* ---------------------------- Create User Modal ---------------------------- */
function CreateUserModal({
  open,
  onClose,
  onConfirm,
  busy,
  error,
  success,
  roles,
}: {
  open: boolean;
  onClose: () => void;
  onConfirm: (payload: {
    name: string;
    email: string;
    phone?: string;
    password: string;
    roleId?: number;
    isActive?: boolean;
    companyId?: number;
    branchId?: number;
    employeeSerialId?: number;
  }) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
  roles: { id: number; name: string; level: number }[];
}) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [phoneError, setPhoneError] = useState<string | null>(null);
  const [password, setPassword] = useState('');
  const [pwTouched, setPwTouched] = useState(false);
  const [roleId, setRoleId] = useState<number | undefined>(undefined);
  const [roleError, setRoleError] = useState<string | null>(null);
  const [isActive, setIsActive] = useState(true);

  const [companyId, setCompanyId] = useState<number | undefined>(undefined);
  const [branchId, setBranchId] = useState<number | undefined>(undefined);
  const [employeeSerialId, setEmployeeSerialId] = useState<number | undefined>(
    undefined
  );

  const phoneRegex = /^01[3-9]\d{8}$/;
  const validatePhone = (value: string) => {
    if (!value) return 'Phone is required';
    if (!phoneRegex.test(value))
      return 'Phone must start with 01 and be exactly 11 digits';
    return null;
  };

  const isValidEmail = (email: string) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const isValidPassword = (password: string) => password.length >= 8;

  const pwTooShort = password.length > 0 && !isValidPassword(password);
  const canSubmitCreate =
    !busy &&
    !success &&
    name.trim().length > 0 &&
    isValidEmail(email) &&
    isValidPassword(password) &&
    !validatePhone(phone) &&
    roleId !== undefined;

  const { user } = useAppSelector(s => s.auth);
  const levelStr = user?.roles?.level?.toString() || 'level-0';
  const myLevel = Number(levelStr.match(/\d+/)?.[0] || 0);
  const roleOptions = useMemo(() => {
    return roles
      .filter(r => myLevel < r.level) // ✅ only show roles below user's level
      .map(r => ({
        value: String(r.id),
        label: r.name,
        level: r.level,
      }));
  }, [roles, myLevel]);

  useEffect(() => {
    if (open) {
      setName('');
      setEmail('');
      setPhone('');
      setPassword('');
      setRoleId(undefined);
      setRoleError(null);
      setIsActive(true);
      setCompanyId(undefined);
      setBranchId(undefined);
      setEmployeeSerialId(undefined);
    }
  }, [open]);

  if (!open) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Create user">
      <div className="space-y-3">
        <Grid gutter="md">
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Name"
              placeholder="Name"
              value={name}
              onChange={e => setName(e.currentTarget.value)}
              disabled={busy || success}
              withAsterisk
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              type="email"
              label="Email"
              placeholder="Email address"
              value={email}
              onChange={e => setEmail(e.currentTarget.value)}
              disabled={busy || success}
              withAsterisk
              error={
                email && !isValidEmail(email)
                  ? 'Invalid email address'
                  : undefined
              }
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Phone"
              placeholder="017XXXXXXXX"
              value={phone}
              onChange={e => {
                const value = e.currentTarget.value;
                setPhone(value);
                setPhoneError(validatePhone(value));
              }}
              onBlur={() => setPhoneError(validatePhone(phone))}
              disabled={busy || success}
              error={phoneError}
              withAsterisk
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <PasswordInput
              size="xs"
              label="Password"
              placeholder="Password"
              value={password}
              onChange={e => setPassword(e.currentTarget.value)}
              onFocus={() => setPwTouched(true)}
              error={
                pwTouched && pwTooShort
                  ? 'Password must be at least 8 characters long'
                  : undefined
              }
              disabled={busy || success}
              withAsterisk
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12 }}>
            <Select
              label="Role"
              placeholder="Select role"
              data={roleOptions}
              value={roleId != null ? String(roleId) : null}
              onChange={v => {
                setRoleId(v ? Number(v) : undefined);
                setRoleError(v ? null : 'Role is required');
              }}
              onBlur={() => {
                if (!roleId) {
                  setRoleError('Role is required');
                }
              }}
              disabled={busy || success}
              withAsterisk
              clearable
              error={roleError}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Switch
              label="Active"
              checked={isActive}
              onChange={e => setIsActive(e.currentTarget.checked)}
              disabled={busy || success}
            />
          </Grid.Col>
        </Grid>

        {!!error && (
          <Text c="red" size="sm">
            {error}
          </Text>
        )}
        {success && !error && (
          <Text c="green" size="sm">
            User created. Closing…
          </Text>
        )}

        <div className="flex items-center gap-5 justify-end mt-8">
          <Button onClick={onClose} color="red" type="button" disabled={busy}>
            Cancel
          </Button>
          <Button
            type="submit"
            onClick={() => {
              // Validate role before submitting
              if (!roleId) {
                setRoleError('Role is required');
                notifications.show({
                  title: 'Validation Error',
                  message: 'Please select a role',
                  color: 'red',
                });
                return;
              }

              if (!canSubmitCreate) return; // hard guard
              onConfirm({
                name: name.trim(),
                email: email.trim(),
                phone: phone.trim() || undefined,
                password,
                roleId,
                isActive,
                companyId,
                branchId,
                employeeSerialId,
              });
            }}
            disabled={!canSubmitCreate}
          >
            {busy ? 'Creating…' : 'Create'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* ---------------------------- Edit User Modal ---------------------------- */
function EditUserModal({
  open,
  onClose,
  user,
  onConfirm,
  busy,
  error,
  success,
  roles,
}: {
  open: boolean;
  onClose: () => void;
  user: UserRow | null;
  onConfirm: (payload: {
    name: string;
    email: string;
    phone?: string;
    roleId?: number;
    isActive?: boolean;
    password?: string;
    companyId?: number;
    branchId?: number;
    employeeSerialId?: number;
  }) => void;
  busy: boolean;
  error: string | null;
  success: boolean;
  roles: { id: number; name: string }[];
}) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [phoneError, setPhoneError] = useState<string | null>(null);
  const [active, setActive] = useState<boolean>(true);
  const [roleId, setRoleId] = useState<number | undefined>(undefined);

  const [companyId, setCompanyId] = useState<number | undefined>(undefined);
  const [branchId, setBranchId] = useState<number | undefined>(undefined);
  const [employeeSerialId, setEmployeeSerialId] = useState<number | undefined>(
    undefined
  );
  const [password, setPassword] = useState('');

  const phoneRegex = /^01[3-9]\d{8}$/;
  const validatePhone = (value: string) => {
    if (!value) return 'Phone is required';
    if (!phoneRegex.test(value))
      return 'Phone must start with 01 and be exactly 11 digits';
    return null;
  };

  const isValidPassword = (password: string) => password.length >= 8;
  const [pwTouched, setPwTouched] = useState(false);

  // New password is optional; if present it must be >= 8
  const invalidNewPw = password.length > 0 && !isValidPassword(password);
  const canSubmitEdit =
    !busy &&
    !success &&
    name.trim().length > 0 &&
    email.trim().length > 0 &&
    !invalidNewPw &&
    !validatePhone(phone);

  const roleOptions = useMemo(
    () => roles.map(r => ({ value: String(r.id), label: r.name })),
    [roles]
  );

  useEffect(() => {
    if (open && user) {
      setName(user.name ?? '');
      setEmail(user.email ?? '');
      // setPhone((user as any).phone ?? '');
      setPhone(user.phone ?? '');
      setPhoneError(null);
      setActive((user.twoStep ?? 'Disabled') === 'Enabled');

      const r = roles.find(r => r.name === user.role);
      setRoleId(r?.id);

      setCompanyId((user as any).companyId ?? undefined);
      setBranchId((user as any).branchId ?? undefined);
      setEmployeeSerialId((user as any).employeeSerialId ?? undefined);

      setPassword('');
    }
  }, [open, user, roles]);

  if (!open || !user) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit user">
      <div className="space-y-3">
        <Grid gutter="md">
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Name"
              value={name}
              onChange={e => setName(e.currentTarget.value)}
              disabled={busy || success}
              withAsterisk
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              type="email"
              label="Email"
              value={email}
              onChange={e => setEmail(e.currentTarget.value)}
              // disabled={busy || success}
              withAsterisk
            />
          </Grid.Col>

          {/* <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Phone"
              placeholder="Phone number"
              value={phone}
              onChange={e => setPhone(e.currentTarget.value)}
              disabled={busy || success}
            />
          </Grid.Col> */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput
              label="Phone"
              placeholder="017XXXXXXXX"
              value={phone}
              onChange={e => {
                const value = e.currentTarget.value;
                setPhone(value);
                setPhoneError(validatePhone(value));
              }}
              onBlur={() => setPhoneError(validatePhone(phone))}
              disabled={busy || success}
              error={phoneError}
              withAsterisk
            />
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 6 }}>
            <Select
              label="Role"
              placeholder="Select role"
              data={roleOptions}
              value={roleId != null ? String(roleId) : null}
              onChange={v => setRoleId(v ? Number(v) : undefined)}
              disabled={busy || success}
              clearable
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12 }}>
            <PasswordInput
              size="xs"
              label="New Password (optional)"
              placeholder="New Password (optional)"
              value={password}
              onChange={e => setPassword(e.currentTarget.value)}
              onFocus={() => setPwTouched(true)}
              error={
                pwTouched && invalidNewPw
                  ? 'Password must be at least 8 characters long'
                  : undefined
              }
              disabled={busy || success}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Switch
              label="Active"
              checked={active}
              onChange={e => setActive(e.currentTarget.checked)}
              disabled={busy || success}
            />
          </Grid.Col>
        </Grid>

        {!!error && (
          <Text c="red" size="sm">
            {error}
          </Text>
        )}
        {success && !error && (
          <Text c="green" size="sm">
            User updated. Closing…
          </Text>
        )}

        <div className="flex items-center gap-5 justify-end mt-8">
          <Button onClick={onClose} color="red" type="button" disabled={busy}>
            Cancel
          </Button>
          <Button
            type="submit"
            onClick={() => {
              if (!canSubmitEdit) return; // hard guard
              onConfirm({
                name: name.trim(),
                email: email.trim(), // unchanged
                phone: phone.trim() || undefined,
                roleId,
                isActive: active,
                password: password.trim() ? password : undefined, // only send if set & valid
                companyId,
                branchId,
                employeeSerialId,
              });
            }}
            disabled={!canSubmitEdit} // 👈 block when short pw entered
          >
            {busy ? 'Saving…' : 'Save'}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

/* --------------------------- Delete User Modal --------------------------- */
function DeleteUserModal({
  open,
  onClose,
  user,
  onConfirm,
  busy,
  error,
  success,
}: {
  open: boolean;
  onClose: () => void;
  user: UserRow | null;
  onConfirm: () => void;
  busy: boolean;
  error: string | null;
  success: boolean;
}) {
  if (!open || !user) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete user">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete{' '}
        <span className="font-medium">{user.name}</span>?
      </p>

      {!!error && <p className="mt-2 text-sm text-red-600">{error}</p>}
      {success && !error && (
        <p className="mt-2 text-sm text-green-600">User deleted. Closing…</p>
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
export default function UserInfoPage() {
  const dispatch = useAppDispatch();
  const [keyword, setKeyword] = useState('');

  const {
    list: users,
    page,
    loading,
    error,
    creatingUser,
    createUserError,
    updatingUser,
    updateUserError,
    deletingUser,
    deleteUserError,
  } = useSelector((s: RootState) => s.users);

  const rolesForDropdown =
    useSelector((s: RootState) => s.roles.allRoles)?.map(r => ({
      id: Number(r.id),
      name: r.name,
      level: Number(r?.level?.toString()?.split('-')[1]) || 0,
    })) ?? [];

  useEffect(() => {
    dispatch(fetchUsers(0, 10, keyword)); // Start with page 0, size 10
    if (!rolesForDropdown?.length) {
      dispatch(fetchAllRoles());
    }
  }, [dispatch, keyword]);

  const data = useMemo<UserRow[]>(() => users ?? [], [users]);

  // Create modal state
  const [openCreate, setOpenCreate] = useState(false);
  const [justSubmittedCreate, setJustSubmittedCreate] = useState(false);
  const creationSuccess =
    justSubmittedCreate && !creatingUser && !createUserError;

  // Edit modal state
  const [openEdit, setOpenEdit] = useState(false);
  const [editing, setEditing] = useState<UserRow | null>(null);
  const [justSubmittedEdit, setJustSubmittedEdit] = useState(false);
  const editSuccess = justSubmittedEdit && !updatingUser && !updateUserError;

  // Delete modal state
  const [openDelete, setOpenDelete] = useState(false);
  const [deletingTarget, setDeletingTarget] = useState<UserRow | null>(null);
  const [justSubmittedDelete, setJustSubmittedDelete] = useState(false);
  const deleteSuccess =
    justSubmittedDelete && !deletingUser && !deleteUserError;

  // Auto-close
  useEffect(() => {
    if (!(creationSuccess && openCreate)) return undefined;
    const t = setTimeout(() => {
      setOpenCreate(false);
      setJustSubmittedCreate(false);
    }, 1200);
    return () => clearTimeout(t);
  }, [creationSuccess, openCreate]);

  useEffect(() => {
    if (editSuccess && openEdit) {
      const t = setTimeout(() => {
        setOpenEdit(false);
        setJustSubmittedEdit(false);
      }, 800);
      return () => clearTimeout(t);
    }
    return undefined;
  }, [editSuccess, openEdit]);

  useEffect(() => {
    if (!(deleteSuccess && openDelete)) return undefined;
    const t = setTimeout(() => {
      setOpenDelete(false);
      setJustSubmittedDelete(false);
      setDeletingTarget(null);
    }, 800);
    return () => clearTimeout(t);
  }, [deleteSuccess, openDelete]);

  // ✅ Notifications for CREATE
  useEffect(() => {
    if (justSubmittedCreate && !creatingUser) {
      if (createUserError) {
        notifications.show({
          title: 'User creation failed',
          message: createUserError,
          color: 'red',
          withBorder: true,
        });
      } else {
        notifications.show({
          title: 'User created successfully',
          message: 'The user has been added to the system.',
          color: 'green',
          withBorder: true,
        });
      }
    }
  }, [justSubmittedCreate, creatingUser, createUserError]);

  // ✅ Notifications for EDIT
  useEffect(() => {
    if (justSubmittedEdit && !updatingUser) {
      if (updateUserError) {
        notifications.show({
          title: 'User update failed',
          message: updateUserError,
          color: 'red',
          withBorder: true,
        });
      } else {
        notifications.show({
          title: 'User updated successfully',
          message: 'Changes have been saved.',
          color: 'green',
          withBorder: true,
        });
      }
    }
  }, [justSubmittedEdit, updatingUser, updateUserError]);

  // ✅ Notifications for DELETE
  useEffect(() => {
    if (justSubmittedDelete && !deletingUser) {
      if (deleteUserError) {
        notifications.show({
          title: 'User deletion failed',
          message: deleteUserError,
          color: 'red',
          withBorder: true,
        });
      } else {
        notifications.show({
          title: 'User deleted successfully',
          message: 'The user record has been removed.',
          color: 'green',
          withBorder: true,
        });
      }
    }
  }, [justSubmittedDelete, deletingUser, deleteUserError]);

  const columns: ColumnDef<UserRow, any>[] = useMemo(
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
      {
        accessorKey: 'name',
        header: 'User',
        cell: ({ row }) => {
          const e = row.original as UserRow;
          const hasAvatar =
            typeof e.avatar === 'string' && e.avatar.trim().length > 0;
          return (
            <div className="flex items-center gap-3">
              {hasAvatar ? (
                <Image
                  src={e.avatar!}
                  alt={e.name}
                  width={32}
                  height={32}
                  className="h-8 w-8 rounded-full object-cover"
                  sizes="32px"
                />
              ) : (
                <div className="h-8 w-8 flex items-center justify-center rounded-full bg-gray-200 text-gray-600 font-semibold">
                  {(e.name || '?').charAt(0)}
                </div>
              )}
              <div>
                <div className="font-medium text-gray-700">{e.name || '—'}</div>
                <div className="text-xs text-gray-400">{e.email || '—'}</div>
              </div>
            </div>
          );
        },
      },
      { accessorKey: 'role', header: 'Role', filterFn: 'equals' },
      { accessorKey: 'phone', header: 'Phone' },
      {
        accessorKey: 'twoStep',
        header: 'Active',
        filterFn: 'equals',
        cell: ({ getValue }) =>
          getValue() === 'Enabled' ? (
            <span className="px-2 py-0.5 text-xs font-medium rounded bg-green-100 text-green-700">
              Enabled
            </span>
          ) : (
            <span className="px-2 py-0.5 text-xs font-medium rounded bg-gray-100 text-gray-600">
              Disabled
            </span>
          ),
      },

      // {
      //   accessorKey: 'joinedDate',
      //   header: 'Joined Date',
      //   cell: ({ getValue }) => {
      //     const value = getValue() as string | null;
      //     if (!value) return '—';
      //     const date = new Date(value);
      //     return date.toLocaleDateString('en-US', {
      //       year: 'numeric',
      //       month: 'short',
      //       day: '2-digit',
      //     });
      //   },
      // },

      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          const actions = [];

          // Only show edit if user has permission

          actions.push({
            label: 'Edit',
            icon: <PencilSquareIcon height={16} />,
            action: () => {
              setEditing(row.original as UserRow);
              setOpenEdit(true);
            },
          });

          // Only show delete if user has permission

          actions.push({
            label: 'Delete',
            icon: <TrashIcon height={16} />,
            action: () => {
              modals.openConfirmModal({
                title: 'Delete confirmation',
                centered: true,
                children: (
                  <Text size="sm">
                    Are you sure you want to delete ? This action is destructive
                    and you will have to contact support to restore your data.
                  </Text>
                ),
                labels: { confirm: 'Delete', cancel: 'Cancel' },
                confirmProps: { color: 'red' },
                onConfirm: async () => {
                  try {
                    setDeletingTarget(row.original as UserRow);
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

  const selectFilters: SelectFilterDef<UserRow>[] = [
    {
      id: 'role',
      label: 'Role',
      options: [
        { label: 'Super Admin', value: 'SuperAdmin' },
        { label: 'Employee', value: 'Employee' },
        { label: 'Supervisor', value: 'Supervisor' },
        { label: 'Manager', value: 'Manager' },
        { label: 'Admin', value: 'Admin' },
      ],
    },
    {
      id: 'twoStep',
      label: 'Two-Step',
      options: [
        { label: 'Enabled', value: 'Enabled' },
        { label: 'Disabled', value: 'Disabled' },
      ],
    },
  ];

  const handleCreateConfirm = async (payload: {
    name: string;
    email: string;
    phone?: string;
    password: string;
    roleId?: number;
    isActive?: boolean;
    companyId?: number;
    branchId?: number;
    employeeSerialId?: number;
  }) => {
    setJustSubmittedCreate(true);
    await dispatch<any>(createUser(payload));
  };

  const handleEditConfirm = async (payload: {
    name: string;
    email: string;
    phone?: string;
    roleId?: number;
    active?: boolean;
    password?: string;
    companyId?: number;
    branchId?: number;
    employeeSerialId?: number;
  }) => {
    if (!editing) return;
    setJustSubmittedEdit(true);
    await dispatch<any>(updateUserById(editing.id, payload as any));
  };

  const handleDeleteConfirm = async () => {
    if (!deletingTarget) return;
    setJustSubmittedDelete(true);
    await dispatch<any>(deleteUserById(deletingTarget.id));
  };

  return (
    <>
      <div className="p-6 bg-white rounded-lg shadow max-w-full">
        <DataTable<UserRow>
          data={data}
          columns={columns}
          selectFilters={selectFilters}
          initialColumnVisibility={{}}
          searchPlaceholder="Search..."
          csvFileName="users"
          enableRowSelection
          onPressAdd={() => {
            setOpenCreate(true);
            setJustSubmittedCreate(false);
          }}
          manualPagination
          pagination={{
            pageIndex: page?.number ?? 0,
            pageSize: page?.size ?? 10,
          }}
          onPaginationChange={next => {
            dispatch(fetchUsers(next.pageIndex, next.pageSize));
          }}
          pageCount={page?.totalPages ?? 1}
          totalRows={page?.totalElements ?? data.length}
          /* ✅ Optional loading indicator */
          loading={loading}
          onSearchChange={setKeyword}
        />

        {loading && (
          <p className="mt-3 text-sm text-gray-500">Loading users…</p>
        )}
        {error && <p className="mt-3 text-sm text-red-600">{error}</p>}
        {!loading && !error && data.length === 0 && (
          <p className="mt-3 text-sm text-gray-500">No data found</p>
        )}

        {/* Create */}
        <CreateUserModal
          open={openCreate}
          onClose={() => setOpenCreate(false)}
          onConfirm={handleCreateConfirm}
          busy={creatingUser}
          error={createUserError}
          success={creationSuccess}
          roles={rolesForDropdown}
        />

        {/* Edit */}
        <EditUserModal
          open={openEdit}
          onClose={() => setOpenEdit(false)}
          user={editing}
          onConfirm={handleEditConfirm}
          busy={updatingUser}
          error={updateUserError}
          success={editSuccess}
          roles={rolesForDropdown}
        />

        {/* Delete */}
        <DeleteUserModal
          open={openDelete}
          onClose={() => setOpenDelete(false)}
          user={deletingTarget}
          onConfirm={handleDeleteConfirm}
          busy={deletingUser}
          error={deleteUserError}
          success={deleteSuccess}
        />
      </div>
    </>
  );
}
