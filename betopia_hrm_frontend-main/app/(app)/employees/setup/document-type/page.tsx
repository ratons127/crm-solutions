'use client';
import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteDocumentTypeMutation,
  useGetPaginatedDocumentTypeListQuery,
} from '@/lib/features/document/documentTypeAPI';
import type { DocumentType } from '@/lib/types/document';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { Modal, Paper, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import DocumentTypeForm from './DocumentTypeForm';

export default function EmployeeDocumentTypePage() {
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [item, setItem] = useState<DocumentType | null>(null);
  const [deleteItem, { isLoading: isDeleting }] =
    useDeleteDocumentTypeMutation();

  // Permission checks
  // const { canCreate, canEdit, canDelete } =
  //   useResourcePermissions('document-type');

  // Server-side pagination state
  const [pageIndex, setPageIndex] = useState(0); // 0-based for DataTable
  const [pageSize, setPageSize] = useState(10);

  // Fetch paginated data
  const { data, isLoading } = useGetPaginatedDocumentTypeListQuery({
    page: pageIndex + 1, // API uses 1-based page numbers
    perPage: pageSize,
    sortDirection: 'DESC',
  });

  const columns: ColumnDef<DocumentType, DocumentType>[] = useMemo(
    () => [
      {
        accessorKey: 'id',
        header: 'ID',
      },
      {
        accessorKey: 'name',
        header: 'Name',
      },
      {
        accessorKey: 'category',
        header: 'Category',
      },
      {
        header: 'Description',
        cell: ({ row }) => <span>{row.original.description || 'N/A'}</span>,
      },
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
              setAction('update');
              setItem(row.original);
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
                    Are you sure you want to delete this document type? This
                    action is destructive and you will have to contact support
                    to restore your data.
                  </Text>
                ),
                labels: {
                  confirm: 'Delete',
                  cancel: 'Cancel',
                },
                size: 'lg',
                confirmProps: {
                  color: 'red',
                  loading: isDeleting,
                  disabled: isDeleting,
                },
                onCancel: () => {
                  setAction(null);
                  setItem(null);
                },
                onConfirm: async () => {
                  try {
                    await deleteItem({ id: row.original.id });
                    notifications.show({
                      title: `Document Type Deleted successfully`,
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
          });

          return (
            <RowActionDropdown data={actions}>
              <BsThreeDots />
            </RowActionDropdown>
          );
        },
      },
    ],
    [deleteItem, isDeleting]
  );
  return (
    <>
      <div>
        <Paper p={20} radius={5} shadow="sm">
          <Breadcrumbs />
          <DataTable<DocumentType>
            data={data?.data ?? []}
            columns={columns}
            selectFilters={[]}
            searchPlaceholder="Search document types..."
            csvFileName="Document Types"
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
            hideColumnVisibility
            hideExport
          />
        </Paper>
        {/* Modal */}
        <Modal
          opened={action !== null}
          onClose={() => {
            setAction(null);
          }}
        >
          <DocumentTypeForm
            action={action}
            onClose={() => {
              setAction(null);
              setItem(null);
            }}
            data={item}
          />
        </Modal>
      </div>
    </>
  );
}
