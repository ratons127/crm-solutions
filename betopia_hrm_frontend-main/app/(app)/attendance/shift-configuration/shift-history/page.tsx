'use client';

import StatusField from '@/components/common/StatusField';
import { DataTable } from '@/components/common/table/DataTable';
import { useGetShiftHistoryMutation } from '@/lib/features/shiftAssignment/shiftAssignmentAPI';
import { useGetCompanyAllListQuery } from '@/services/api/admin/company/companyAPI';
import { Badge, Button, Select } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useFormik } from 'formik';
import { useMemo, useState } from 'react';
import { CiFilter } from 'react-icons/ci';
import { FiFilter } from 'react-icons/fi';
import { GoStack } from 'react-icons/go';
import { IoPersonOutline } from 'react-icons/io5';
import * as Yup from 'yup';

const validationSchema = Yup.object().shape({
  companyId: Yup.string().required('Company is required'),
});

export default function ShiftHistoryPage() {
  const [shiftHistory, setShiftHistory] = useState<any[] | null>(null);

  const { data: companyList } = useGetCompanyAllListQuery({});
  const [fetchShiftHistory, { isLoading: fetchingEmployees }] =
    useGetShiftHistoryMutation();

  const handleSubmit = async (values: any) => {
    try {
      const res = await fetchShiftHistory({
        companyId: values.companyId,
      }).unwrap();
      setShiftHistory(res?.data ?? []);
      notifications.show({
        title: 'Shift History',
        message: res?.message,
      });
    } catch (e: any) {
      notifications.show({
        title: 'Shift History',
        message: e?.data?.message,
      });
      setShiftHistory([]);
    }
  };

  const formik = useFormik<any>({
    initialValues: {
      companyId: '',
    },
    validationSchema,
    onSubmit: handleSubmit,
  });

  const columns: ColumnDef<any, any>[] = useMemo(
    () => [
      {
        header: 'Employee ID',
        accessorKey: 'employee.employeeSerialId',
      },
      {
        header: 'Employee Name',
        accessorKey: 'employee.firstName',
        cell: ({ row }) => (
          <div className="flex items-center gap-3">
            <IoPersonOutline size={18} className="text-slate-700" />
            <span className="font-medium text-gray-800">
              {`${row.original.employee.firstName ?? ''} ${row.original.employee.lastName ?? ''}`.trim() ||
                '-'}
            </span>
          </div>
        ),
      },
      {
        header: 'Shift Name',
        accessorKey: 'shift.shiftName',
        cell: ({ row }) => {
          const shiftName = row.original.shift?.shiftName || '-';

          // Define colors per shift type
          const getBadgeProps = (name: string) => {
            switch (name.toLowerCase()) {
              case 'regular':
                return { color: 'blue', variant: 'light' };
              case 'night':
                return { color: 'violet', variant: 'light' };
              case 'rotational':
                return { color: 'orange', variant: 'light' };
              default:
                return { color: 'green', variant: 'light' };
            }
          };

          const badgeProps = getBadgeProps(shiftName);

          return (
            <div className="flex items-center gap-2">
              <Badge
                radius="md"
                size="sm"
                {...badgeProps}
                className="font-medium"
              >
                {shiftName}
              </Badge>
            </div>
          );
        },
      },
      {
        header: 'From Date',
        accessorKey: 'effectiveFrom',
      },
      {
        header: 'To Date',
        accessorKey: 'effectiveTo',
      },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ row }) => <StatusField status={row.original.status} />,
      },
    ],
    []
  );
  return (
    <div className="space-y-8">
      {/* ===== Header ===== */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        <div className="flex items-center justify-between">
          <div className="flex flex-col gap-1">
            <div className="flex items-center  gap-3">
              <FiFilter className="text-blue-600" size={19} />
              <span className="text-base font-semibold text-[#1E293B]">
                Filter Employees
              </span>
            </div>
          </div>

          <div className="flex justify-end gap-x-2">
            <form
              onSubmit={formik.handleSubmit}
              className="flex items-center gap-x-2"
            >
              <Select
                placeholder="Select Company"
                withAsterisk
                searchable
                clearable
                className="w-96"
                size="sm"
                radius="md"
                data={
                  companyList?.data?.map(c => ({
                    label: c.name,
                    value: String(c.id),
                  })) ?? []
                }
                name="companyId"
                value={formik.values.companyId}
                onChange={value =>
                  formik.setFieldValue('companyId', value || '')
                }
                onBlur={() => formik.setFieldTouched('companyId', true)}
                error={
                  formik.touched.companyId
                    ? typeof formik.errors.companyId === 'string'
                      ? formik.errors.companyId
                      : undefined
                    : undefined
                }
              />
              <Button
                leftSection={<CiFilter size={18} />}
                radius="md"
                className="self-end md:self-auto"
                type="submit"
                loading={fetchingEmployees}
              >
                Apply Filters
              </Button>
            </form>
          </div>
        </div>
      </div>

      {/* ===== Employee Table ===== */}
      {shiftHistory === null ? (
        // Case 1: no filter applied yet (null or undefined)
        <div className="flex justify-center items-center text-orange-500 font-bold text-xl">
          Apply Filter to get Shift History List
        </div>
      ) : shiftHistory.length === 0 ? (
        // Case 2: filter applied but no data found
        <div className="flex justify-center items-center text-red-600 font-semibold text-lg">
          Data not found
        </div>
      ) : (
        // Case 3: data exists
        <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
          <div className="grid grid-cols-2 items-center mb-5">
            <div>
              <div className="flex items-center gap-3">
                <GoStack size={18} className="text-[#F54900]" />
                <span className="text-base font-semibold text-[#1E293B]">
                  Shift Categories
                </span>
              </div>
            </div>
          </div>

          <DataTable
            data={shiftHistory}
            columns={columns}
            hideExport
            hideColumnVisibility
            pageSizeOptions={[10]}
          />
        </div>
      )}
    </div>
  );
}
