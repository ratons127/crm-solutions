'use client';

import { EllipsisHorizontalIcon } from '@heroicons/react/24/solid';
import { ColumnDef } from '@tanstack/react-table';
import Image from 'next/image';
import React, { useEffect, useMemo } from 'react';
import { useSelector } from 'react-redux';
import Breadcrumbs from '../../../../components/common/Breadcrumbs';
import {
  DataTable,
  SelectFilterDef,
} from '../../../../components/common/table/DataTable';
import { fetchUsers } from '../../../../lib/features/user/userApi';
import { UserRow } from '../../../../lib/features/user/userSlice';
import { useAppDispatch } from '../../../../lib/hooks';
import { RootState } from '../../../../lib/store';

/** Dummy fallback OUTSIDE component to avoid hook deps warnings */
const dummyUsers: UserRow[] = [
  {
    id: 1,
    name: 'Emma Smithh',
    email: 'smith@kpmg.com',
    avatar: 'https://randomuser.me/api/portraits/women/1.jpg',
    role: 'Administrator',
    lastLogin: 'Yesterday',
    twoStep: 'Enabled',
    joinedDate: '20 Jun 2025, 2:40 pm',
  },
  {
    id: 2,
    name: 'Melody Macy',
    email: 'melody@altbox.com',
    avatar: '',
    role: 'Analyst',
    lastLogin: '20 mins ago',
    twoStep: 'Enabled',
    joinedDate: '21 Feb 2025, 2:40 pm',
  },
  {
    id: 3,
    name: 'Max Smith',
    email: 'max@kt.com',
    avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
    role: 'Developer',
    lastLogin: '3 days ago',
    twoStep: 'Disabled',
    joinedDate: '20 Jun 2025, 5:20 pm',
  },
  {
    id: 4,
    name: 'John Smith',
    email: 'john@kt.com',
    avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
    role: 'Developer',
    lastLogin: '3 days ago',
    twoStep: 'Disabled',
    joinedDate: '20 Jun 2025, 5:20 pm',
  },
];

function ActionsDropdown({ row }: { row: any }) {
  const [open, setOpen] = React.useState(false);
  return (
    <div className="relative">
      <button
        onClick={() => setOpen(p => !p)}
        className="flex items-center gap-1 px-2 py-1 text-gray-400 hover:bg-gray-100"
      >
        <EllipsisHorizontalIcon className="h-4 w-4" />
      </button>
      {open && (
        <div className="absolute right-0 mt-1 w-32 bg-white border border-gray-200 rounded-md shadow-lg z-10">
          <button
            onClick={() => {
              alert(`Edit user: ${row.original.name}`);
              setOpen(false);
            }}
            className="block w-full text-left px-3 py-2 text-sm hover:bg-gray-100"
          >
            Edit
          </button>
          <button
            onClick={() => {
              alert(`Delete user: ${row.original.name}`);
              setOpen(false);
            }}
            className="block w-full text-left px-3 py-2 text-sm hover:bg-red-50 text-red-600"
          >
            Delete
          </button>
        </div>
      )}
    </div>
  );
}

export default function UserInfoPage() {
  const dispatch = useAppDispatch();
  const {
    list: users,
    loading,
    error,
  } = useSelector((s: RootState) => s.users);

  useEffect(() => {
    dispatch(fetchUsers());
  }, [dispatch]);

  const data = useMemo<UserRow[]>(
    () => (users && users.length > 0 ? users : dummyUsers),
    [users]
  );

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
                  {e.name?.charAt(0)}
                </div>
              )}
              <div>
                <div className="font-medium text-gray-700">{e.name}</div>
                <div className="text-xs text-gray-400">{e.email}</div>
              </div>
            </div>
          );
        },
      },
      { accessorKey: 'role', header: 'Role', filterFn: 'equals' },
      { accessorKey: 'lastLogin', header: 'Last Login' },
      {
        accessorKey: 'twoStep',
        header: 'Two-Step',
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
      { accessorKey: 'joinedDate', header: 'Joined Date' },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => <ActionsDropdown row={row} />,
      },
    ],
    []
  );

  const selectFilters: SelectFilterDef<UserRow>[] = [
    {
      id: 'role',
      label: 'Role',
      options: [
        { label: 'Administrator', value: 'Administrator' },
        { label: 'Analyst', value: 'Analyst' },
        { label: 'Developer', value: 'Developer' },
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

  return (
    <div className="p-6 bg-white rounded-lg shadow max-w-full">
      <DataTable<UserRow>
        data={data}
        columns={columns}
        selectFilters={selectFilters}
        initialColumnVisibility={{}}
        searchPlaceholder="Search..."
        csvFileName="users"
        enableRowSelection
        headerLeft={<Breadcrumbs />}
      />

      {loading && <p className="mt-3 text-sm text-gray-500">Loading users…</p>}
      {error && <p className="mt-3 text-sm text-red-600">{error}</p>}
    </div>
  );
}
