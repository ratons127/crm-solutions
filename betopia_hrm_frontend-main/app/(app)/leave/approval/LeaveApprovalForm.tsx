'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useGetAllLeaveApprovalsQuery,
  useUpdateLeaveApprovalStatusMutation,
} from '@/lib/features/leave/leaveApproval/leaveApprovalAPI';
import { LeaveStatus } from '@/lib/types/leaveApproval';
import { Button, Stack, Text, Textarea } from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import { useMemo } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import * as Yup from 'yup';

export default function LeaveApprovalPage() {
  const { data, isLoading } = useGetAllLeaveApprovalsQuery(undefined, {
    refetchOnMountOrArgChange: true,
  });

  const [updateApprovalStatus] = useUpdateLeaveApprovalStatusMutation();

  const handleStatusChange = async (
    id: number,
    newStatus: LeaveStatus,
    remarks: string
  ) => {
    try {
      await updateApprovalStatus({
        id,
        data: { leaveStatus: newStatus, remarks },
      }).unwrap();

      notifications.show({
        title: 'Status updated successfully',
        message: `Leave approval has been marked as ${newStatus}.`,
        color: 'blue',
      });
    } catch (error) {
      console.error(error);
      notifications.show({
        title: 'Update failed',
        message: 'Something went wrong while updating the leave status.',
        color: 'red',
      });
    }
  };

  const openRemarksModal = (id: number, status: LeaveStatus) => {
    const isRemarksRequired = status === 'REJECTED' || status === 'CANCELLED';

    const formik = useFormik({
      initialValues: {
        remarks: '',
      },
      validationSchema: Yup.object({
        remarks: isRemarksRequired
          ? Yup.string().trim().required('Remarks are required for this status')
          : Yup.string().trim(),
      }),
      onSubmit: async values => {
        modals.closeAll();
        await handleStatusChange(id, status, values.remarks);
      },
    });

    modals.open({
      title: `Change Status to ${status}`,
      centered: true,
      children: (
        <form onSubmit={formik.handleSubmit}>
          <Stack gap="sm">
            <Text size="sm">
              Are you sure you want to mark this leave as <b>{status}</b>?
            </Text>

            <Textarea
              label="Remarks"
              withAsterisk={isRemarksRequired}
              placeholder={
                isRemarksRequired
                  ? 'Remarks are required for rejected or cancelled leaves'
                  : 'Enter remarks (optional)'
              }
              minRows={2}
              value={formik.values.remarks}
              onChange={e =>
                formik.setFieldValue('remarks', e.currentTarget.value)
              }
              onBlur={formik.handleBlur}
              error={formik.touched.remarks ? formik.errors.remarks : undefined}
            />

            <div className="flex justify-end gap-2 pt-2">
              <Button
                variant="default"
                onClick={() => {
                  modals.closeAll();
                }}
                type="button"
              >
                Cancel
              </Button>

              <Button type="submit">Confirm</Button>
            </div>
          </Stack>
        </form>
      ),
    });
  };

  const columns = useMemo(
    () => [
      {
        id: 'select',
        enableSorting: false,
        enableHiding: false,
        header: ({ table }: any) => (
          <input
            type="checkbox"
            className="h-4 w-4 accent-blue-600 cursor-pointer"
            checked={table.getIsAllPageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        ),
        cell: ({ row }: any) => (
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
      {
        header: 'Employee Info',
        cell: ({ row }: any) => (
          <span>
            {row.original.leaveRequest.id} - {row.original?.employee?.firstName}{' '}
            {row.original?.employee?.lastName} -{' '}
            {row.original?.employee?.designationName}
          </span>
        ),
      },
      { accessorKey: 'leaveRequest.leaveType.name', header: 'Leave Type' },
      { accessorKey: 'leaveRequest.startDate', header: 'Start Date' },
      { accessorKey: 'leaveRequest.endDate', header: 'End Date' },
      { accessorKey: 'leaveRequest.daysRequested', header: 'Days Request' },
      { accessorKey: 'approverId', header: 'Approver' },
      { accessorKey: 'level', header: 'Level' },
      {
        accessorKey: 'leaveStatus',
        header: 'Status',
        cell: ({ row }: any) => {
          const s = row.original.leaveStatus;
          const cls =
            s === 'APPROVED'
              ? 'text-green-700 bg-green-50'
              : s === 'REJECTED'
                ? 'text-red-700 bg-red-50'
                : s === 'CANCELLED'
                  ? 'text-gray-700 bg-gray-50'
                  : 'text-amber-700 bg-amber-50';
          return (
            <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>
              {s}
            </span>
          );
        },
      },
      { accessorKey: 'leaveRequest.reason', header: 'Reason' },
      { accessorKey: 'remarks', header: 'Remarks' },
      {
        id: 'actions',
        header: 'Actions',
        cell: ({ row }: any) => (
          <RowActionDropdown
            data={[
              {
                label: 'Pending',
                action: () => openRemarksModal(row.original.id, 'PENDING'),
              },
              {
                label: 'Approve',
                action: () => openRemarksModal(row.original.id, 'APPROVED'),
              },
              {
                label: 'Reject',
                action: () => openRemarksModal(row.original.id, 'REJECTED'),
              },
              {
                label: 'Cancel',
                action: () => openRemarksModal(row.original.id, 'CANCELLED'),
              },
            ]}
          >
            <BsThreeDots />
          </RowActionDropdown>
        ),
      },
    ],
    []
  );

  return (
    <div className="p-6 bg-white rounded-lg shadow max-w-full">
      <DataTable<any>
        data={data?.data ?? []}
        columns={columns as any}
        searchPlaceholder="Search leave approvals..."
        csvFileName="Leave Approvals"
        loading={isLoading}
        headerLeft={<Breadcrumbs />}
      />
    </div>
  );
}
