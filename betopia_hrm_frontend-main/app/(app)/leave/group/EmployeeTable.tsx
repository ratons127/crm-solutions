import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import { Employee } from '@/lib/types/employee/index';
import { TrashIcon } from '@heroicons/react/24/outline';
import { Button, Modal, Text } from '@mantine/core';
import { modals } from '@mantine/modals';
import { ColumnDef } from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import EmployeeFilterTable from './EmployeeFilterTable';

export default function EmployeeTable({
  onDeleteOne,
}: {
  onDeleteOne: (id: number) => void;
}) {
  const [action, setAction] = useState<'add' | null>(null);
  const [employeeList] = useState<Employee[]>([]);
  const columns: ColumnDef<Employee, any>[] = useMemo(
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
      { accessorKey: 'id', header: 'ID', filterFn: 'equals' },
      {
        accessorKey: 'name',
        header: 'Name',
      },
      { accessorKey: 'description', header: 'Description ' },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }) => {
          return (
            <RowActionDropdown
              data={[
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
                      onCancel: () => {},
                      onConfirm: async () => {
                        try {
                          onDeleteOne(row.original.id);
                        } catch (error) {
                          console.error('Error message:', error);
                        }
                      },
                    });
                  },
                },
              ]}
            >
              ...
            </RowActionDropdown>
          );
        },
      },
    ],
    // eslint-disable-next-line react-hooks/exhaustive-deps
    []
  );
  return (
    <div>
      <DataTable<Employee>
        data={employeeList}
        columns={columns}
        selectFilters={[]}
        initialColumnVisibility={{}}
        searchPlaceholder="Search..."
        csvFileName="leave"
        enableRowSelection
        headerRight={
          <Button
            onClick={() => {
              setAction('add');
            }}
          >
            Add Employee
          </Button>
        }
      />

      <Modal
        size={'auto'}
        opened={action !== null}
        onClose={() => setAction(null)}
      >
        <EmployeeFilterTable />
      </Modal>
    </div>
  );
}
