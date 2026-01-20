'use client';
import Breadcrumbs from '@/components/common/Breadcrumbs';
import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteEmployeeDocumentMutation,
  useGetPaginatedDocumentListQuery,
} from '@/lib/features/document/documentAPI';
import type { EmployeeDocument } from '@/lib/types/document';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import Image from 'next/image';
import { useMemo, useState } from 'react';
import DocumentForm from './DocumentForm';

export default function EmployeeDocumentPage() {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<EmployeeDocument | null>(null);
  const [deleteItem] = useDeleteEmployeeDocumentMutation();

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedDocumentListQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<EmployeeDocument, EmployeeDocument>[] = useMemo(
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
        header: 'Employee ID',
        accessorKey: 'employee.id',
      },
      {
        accessorKey: 'documentType.name',
        header: 'Document Name',
      },
      {
        accessorKey: 'documentType.category',
        header: 'Document Category',
      },
      {
        header: 'File',
        cell: ({ row }) => {
          const filePath = row.original.filePath;
          if (!filePath) return <span className="text-gray-400">No file</span>;

          // Check if it's an image
          const isImage = /\.(jpg|jpeg|png|gif|webp|svg)$/i.test(filePath);

          if (isImage) {
            return (
              <Image
                src={filePath}
                alt="Document"
                width={40}
                height={40}
                className="object-cover rounded"
              />
            );
          }

          // For PDFs and other files, show a link
          return (
            <a
              href={filePath}
              target="_blank"
              rel="noopener noreferrer"
              className="text-blue-600 hover:underline"
            >
              View File
            </a>
          );
        },
      },
      {
        accessorKey: 'issueDate',
        header: 'Issue Date',
      },
      {
        accessorKey: 'expiryDate',
        header: 'Expiry Date',
      },
      {
        header: 'Status',
        cell: ({ row }) => <StatusField status={row.original.status} />,
      },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setAction('update');
                  setItem(row.original);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  modals.openConfirmModal({
                    title: 'Delete confirmation',
                    centered: true,
                    children: (
                      <Text size="sm">
                        Are you sure you want to delete ? This action is
                        destructive and you will have to contact support to
                        restore your data.
                      </Text>
                    ),
                    labels: {
                      confirm: 'Delete',
                      cancel: 'Cancel',
                    },
                    size: 'lg',
                    confirmProps: { color: 'red' },
                    onCancel: () => {
                      setAction(null);
                      setItem(null);
                    },
                    onConfirm: async () => {
                      try {
                        await deleteItem({ id: row.original.id });
                        notifications.show({
                          title: `Document Deleted successfully`,
                          message: '',
                          color: 'red',
                        });
                      } catch (error) {
                        notifications.show({
                          title: 'Failed to delete',
                          message: 'Something went wrong',
                        });
                      }
                    },
                  });
                },
              },
            ]}
          >
            ...
          </RowActionDropdown>
        ),
      },
    ],
    [deleteItem]
  );
  return (
    <div>
      <Paper p={20} radius={5} shadow="sm">
        <Breadcrumbs />
        <DataTable<EmployeeDocument>
          data={data?.data ?? []}
          columns={columns}
          selectFilters={[]}
          searchPlaceholder="Search..."
          csvFileName="Employee Document"
          enableRowSelection
          onPressAdd={() => {
            setAction('create');
          }}
          manualPagination
          pageCount={data?.meta?.lastPage ?? 0}
          pagination={{ pageIndex, pageSize }}
          onPaginationChange={({ pageIndex, pageSize }) => {
            setPageIndex(pageIndex);
            setPageSize(pageSize);
          }}
          totalRows={data?.meta?.total}
          loading={isLoading}
        />
      </Paper>
      {/* Modal */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
        }}
      >
        <DocumentForm
          action={action}
          onClose={() => {
            setAction(null);
            setItem(null);
          }}
          data={item}
        />
      </Modal>
    </div>
  );
}
