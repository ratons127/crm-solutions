import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  QualificationRatingMethod,
  QualificationRatingMethodDetail,
} from '@/lib/types/qualification';
import { PencilSquareIcon } from '@heroicons/react/24/outline';
import { Modal, Table } from '@mantine/core';
import {
  createColumnHelper,
  flexRender,
  getCoreRowModel,
  useReactTable,
} from '@tanstack/react-table';
import { useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import QualificationRatingForm from './QualificationRatingForm';

type DetailsTableProps = {
  details: QualificationRatingMethod;
};

type EditItemRow = QualificationRatingMethod & {
  singleRatingDetails: QualificationRatingMethodDetail;
};

const columnHelper = createColumnHelper<QualificationRatingMethodDetail>();

export default function RatingDetailsTable({ details }: DetailsTableProps) {
  const [actionRow, setActionRow] = useState<'update' | null>(null);
  const [itemRow, setItemRow] = useState<EditItemRow | null>(null);

  const handleCloseModal = () => {
    setActionRow(null);
    setItemRow(null);
  };

  const columns = useMemo(() => {
    const handleEdit = (detail: QualificationRatingMethodDetail) => {
      setActionRow('update');
      setItemRow({
        ...details,
        singleRatingDetails: detail,
      });
    };

    return [
      columnHelper.accessor('grade', {
        header: 'Grade',
        cell: info => info.getValue(),
      }),
      columnHelper.accessor('minimumMark', {
        header: 'Minimum Marks',
        cell: info => info.getValue().toFixed(2),
      }),
      columnHelper.accessor('maximumMark', {
        header: 'Maximum Marks',
        cell: info => info.getValue().toFixed(2),
      }),
      columnHelper.accessor('averageMark', {
        header: 'Average Marks',
        cell: info => info?.getValue()?.toFixed(2),
      }),
      columnHelper.display({
        id: 'actions',
        header: () => <div style={{ textAlign: 'center' }}>Actions</div>,
        cell: ({ row }) => (
          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <RowActionDropdown
              data={[
                {
                  label: 'Edit',
                  icon: <PencilSquareIcon height={16} />,
                  action: () => handleEdit(row.original),
                },
              ]}
            >
              <BsThreeDots />
            </RowActionDropdown>
          </div>
        ),
      }),
    ];
  }, [details]);

  const table = useReactTable({
    data: details?.qualificationRatingMethodDetails || [],
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  return (
    <>
      {/* Desktop Table */}
      <div className="hidden sm:block overflow-x-auto">
        <Table withColumnBorders highlightOnHover striped>
          <Table.Thead>
            {table.getHeaderGroups().map(headerGroup => (
              <Table.Tr key={headerGroup.id}>
                {headerGroup.headers.map(header => (
                  <Table.Th key={header.id}>
                    {flexRender(
                      header.column.columnDef.header,
                      header.getContext()
                    )}
                  </Table.Th>
                ))}
              </Table.Tr>
            ))}
          </Table.Thead>

          <Table.Tbody>
            {table.getRowModel().rows.length === 0 ? (
              <Table.Tr>
                <Table.Td colSpan={5} className="text-center text-gray-500">
                  No rating details found.
                </Table.Td>
              </Table.Tr>
            ) : (
              table.getRowModel().rows.map(row => (
                <Table.Tr key={row.id}>
                  {row.getVisibleCells().map(cell => (
                    <Table.Td key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </Table.Td>
                  ))}
                </Table.Tr>
              ))
            )}
          </Table.Tbody>
        </Table>
      </div>

      {/* Mobile Cards */}
      <div className="sm:hidden space-y-4">
        {table.getRowModel().rows.length === 0 ? (
          <div className="text-center p-4 text-gray-500">
            No rating details found.
          </div>
        ) : (
          table.getRowModel().rows.map(row => (
            <div
              key={row.id}
              className="border rounded-lg p-4 shadow-sm bg-white"
            >
              <div className="space-y-2">
                <div className="flex justify-between items-center">
                  <span className="font-medium text-gray-900">Grade:</span>
                  <span className="text-gray-700">{row.original.grade}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="font-medium text-gray-900">Minimum Marks:</span>
                  <span className="text-gray-700">{row.original.minimumMark.toFixed(2)}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="font-medium text-gray-900">Maximum Marks:</span>
                  <span className="text-gray-700">{row.original.maximumMark.toFixed(2)}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="font-medium text-gray-900">Average Marks:</span>
                  <span className="text-gray-700">{row.original.averageMark?.toFixed(2)}</span>
                </div>
                <div className="flex justify-center pt-2 border-t">
                  <RowActionDropdown
                    data={[
                      {
                        label: 'Edit',
                        icon: <PencilSquareIcon height={16} />,
                        action: () => {
                          setActionRow('update');
                          setItemRow({
                            ...details,
                            singleRatingDetails: row.original,
                          });
                        },
                      },
                    ]}
                  >
                    <div className="flex items-center gap-2 px-4 py-2 text-sm text-gray-700 rounded-md min-h-[44px]">
                      <BsThreeDots />
                      <span>Actions</span>
                    </div>
                  </RowActionDropdown>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Modal */}
      <Modal
        title="Update Qualification Rating Detail"
        opened={!!actionRow}
        onClose={handleCloseModal}
      >
        <QualificationRatingForm
          action={actionRow}
          onClose={handleCloseModal}
          data={itemRow}
        />
      </Modal>
    </>
  );
}
