'use client';

import { DataTable } from '@/components/common/table/DataTable';
import { useCreateShiftAssignMutation } from '@/lib/features/shiftAssignment/shiftAssignmentAPI';
import { useGetShiftDefinitionListQuery } from '@/lib/features/shiftDefinition/shiftDefinitionAPI';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetCompanyAllListQuery } from '@/services/api/admin/company/companyAPI';
import { useFetchShiftAssignableEmployeesMutation } from '@/services/api/attendance/shiftAssignAPI';
import { Button, Checkbox, Select } from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { ColumnDef } from '@tanstack/react-table';
import { useFormik } from 'formik';
import Link from 'next/link';
import { useMemo, useState } from 'react';
import { CiFilter } from 'react-icons/ci';
import { FiFilter } from 'react-icons/fi';
import { IoPersonOutline } from 'react-icons/io5';
import { RiGroupLine } from 'react-icons/ri';
import * as Yup from 'yup';

const validationSchema = Yup.object().shape({
  companyId: Yup.string().required('Company is required'),
});

export default function ShiftAssignPage() {
  const [selectedRows, setSelectedRows] = useState<number[]>([]);
  const [employees, setEmployees] = useState<any[] | null>(null);
  const [effectiveFrom, setEffectiveFrom] = useState<null | string>(null);
  const [effectiveTo, setEffectiveTo] = useState<null | string>(null);
  const [shift, setShift] = useState<null | string>(null);

  const { data: companyList } = useGetCompanyAllListQuery({});
  const { data: shiftList } = useGetShiftDefinitionListQuery(undefined);

  const [fetchEmployees, { isLoading: fetchingEmployees }] =
    useFetchShiftAssignableEmployeesMutation();

  const handleSubmit = async (values: any) => {
    try {
      const res = await fetchEmployees({
        companyId: values.companyId,
      }).unwrap();
      setEmployees(res?.data ?? []);
      setSelectedRows([]);
    } catch (e: any) {
      notifications.show({
        title: 'Shift Assign',
        message: e?.data?.message,
      });
      setEmployees([]);
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
        id: 'select',
        header: () => {
          const allIds = (employees ?? [])
            .map(
              (r: any) =>
                r.id ?? r.employeeId ?? (r.employee ? r.employee.id : undefined)
            )
            .filter((v: any) => v != null) as number[];
          const total = allIds.length;
          const selectedCount = selectedRows.filter(id =>
            allIds.includes(id)
          ).length;
          const checked = total > 0 && selectedCount === total;
          const indeterminate = selectedCount > 0 && selectedCount < total;

          return (
            <Checkbox
              checked={checked}
              indeterminate={indeterminate}
              onChange={e => {
                const nextChecked = e.currentTarget.checked;
                setSelectedRows(prev => {
                  if (nextChecked) {
                    const set = new Set(prev);
                    allIds.forEach(id => set.add(id));
                    return Array.from(set);
                  }
                  const remove = new Set(allIds);
                  return prev.filter(id => !remove.has(id));
                });
              }}
            />
          );
        },
        cell: ({ row }) => (
          <Checkbox
            checked={selectedRows.includes(row.original.id ?? -1)}
            onChange={e => {
              const checked = e.currentTarget.checked;
              const rid = row.original.id;
              if (rid == null) return;
              setSelectedRows(prev =>
                checked ? [...prev, rid] : prev.filter(id => id !== rid)
              );
            }}
          />
        ),
      },
      {
        header: 'Employee ID',
        accessorKey: 'employeeSerialId',
      },
      {
        accessorKey: 'firstName',
        header: 'Employee Name',
        cell: ({ row }) => (
          <div className="flex items-center gap-3">
            <IoPersonOutline size={18} className="text-slate-700" />
            <span className="font-medium text-gray-800">
              {`${row.original.firstName ?? ''} ${row.original.lastName ?? ''}`.trim() ||
                '-'}
            </span>
          </div>
        ),
      },
      // {
      //   accessorKey: 'businessUnit?.name',
      //   header: 'Business Unit',
      //   cell: ({ row }) => (
      //     <div className="flex items-center gap-2 text-purple-700">
      //       <IoBusinessOutline size={16} />
      //       <span>
      //         {row.original.businessUnit?.name ||
      //           row.original.businessUnit ||
      //           row.original.businessUnitName ||
      //           '-'}
      //       </span>
      //     </div>
      //   ),
      // },
      // {
      //   accessorKey: 'workplaceGroup.name',
      //   header: 'Workplace Group',
      //   cell: ({ row }) => (
      //     <div className="flex items-center gap-2 text-blue-700">
      //       <CiLocationOn size={16} />
      //       <span>
      //         {row.original.workplaceGroup?.name ||
      //           row.original.workplaceGroup ||
      //           row.original.workplaceGroupName ||
      //           '-'}
      //       </span>
      //     </div>
      //   ),
      // },
      // {
      //   accessorKey: 'workplace.name',
      //   header: 'Workplace',
      //   cell: ({ row }) => (
      //     <div className="flex items-center gap-2 text-green-700">
      //       <IoBriefcaseOutline size={16} />
      //       <span>
      //         {row.original.workplace?.name ||
      //           row.original.workplace ||
      //           row.original.workplaceName ||
      //           '-'}
      //       </span>
      //     </div>
      //   ),
      // },
      // {
      //   accessorKey: 'department.name',
      //   header: 'Department',
      //   cell: ({ row }) => (
      //     <div className="flex items-center gap-2 text-orange-700">
      //       <MdOutlineGroups2 size={16} />
      //       <span>
      //         {row.original.department?.name ||
      //           row.original.department ||
      //           row.original.departmentName ||
      //           '-'}
      //       </span>
      //     </div>
      //   ),
      // },
      // {
      //   accessorKey: 'team.name',
      //   header: 'Team',
      //   cell: ({ row }) => (
      //     <div className="flex items-center gap-2 text-sky-700">
      //       <RiGroupLine size={16} />
      //       <span>
      //         {row.original.team?.name ||
      //           row.original.team ||
      //           row.original.teamName ||
      //           '-'}
      //       </span>
      //     </div>
      //   ),
      // },
      // Current Shift column removed for API-driven data
    ],
    [selectedRows, employees]
  );

  const [createShiftAssign, { isLoading }] = useCreateShiftAssignMutation();

  const handleSubmitShiftAssign = async () => {
    const payload = {
      employeeIds: selectedRows,
      shiftId: Number(shift),
      effectiveFrom: effectiveFrom,
      effectiveTo: effectiveTo,
      status: true,
      assignmentSource: 'manual',
      assignedBy: 1,
      assignedAt: new Date().toISOString(),
    };

    try {
      if (effectiveFrom === null && effectiveTo === null && shift === null) {
        notifications.show({
          color: 'red',
          message: 'Effective From, To and Shift must be selected',
        });
      } else {
        const result = await createShiftAssign(payload);
        // Show Create Notification
        if (result?.data) {
          showApiNotification({
            status: result?.data?.status,
            message: result?.data?.message,
            title: 'Shift Assign',
          });
        }

        if (result?.error) {
          notifications.show({
            color: 'red',
            message: 'Duplicate Entry',
          });
        }
      }
    } catch (e: any) {
      notifications.show({
        color: 'red',
        message: e?.data?.message || 'Failed to shift assign',
      });
    }
  };

  return (
    <div className="space-y-8">
      {/* ===== Header ===== */}
      <div className="flex items-center gap-3">
        <div className="w-10 h-10 bg-orange-500 flex items-center justify-center rounded-xl">
          <RiGroupLine size={20} color="white" />
        </div>
        <div>
          <h1 className="text-3xl font-bold text-[#1D293D]">Shift Assign</h1>
          <p className="text-base text-[#45556C]">
            View and manage employee shift assignments
          </p>
        </div>
      </div>

      {/* ===== Filter Section ===== */}
      <div className="bg-white px-6 py-6 rounded-2xl shadow-md border border-gray-100">
        {/* ===== Header Row ===== */}
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
            <Link href="/attendance/shift-configuration/shift-history">
              <Button
                variant="gradient"
                radius="md"
                className="self-end md:self-auto"
                // loading={fetchingEmployees}
                // onClick={onApplyFilters}
              >
                Shift History
              </Button>
            </Link>
          </div>
        </div>
      </div>

      {/* ===== Employee Table ===== */}
      {employees === null ? (
        // Case 1: no filter applied yet
        <div className="flex justify-center items-center text-orange-500 font-bold text-xl">
          Apply Filter to get Shift Assign List
        </div>
      ) : employees.length === 0 ? (
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
                <RiGroupLine size={18} className="text-[#F54900]" />
                <span className="text-base font-semibold text-[#1E293B]">
                  Employee List
                </span>
              </div>
              <span className="text-sm text-[#64748B]">
                {employees.length} employee(s) • {selectedRows.length} selected
              </span>
            </div>

            <div className="flex justify-end w-full gap-x-4">
              <DatePickerInput
                label="Effective From"
                placeholder="Effective From"
                withAsterisk
                onChange={date => setEffectiveFrom(date)}
                size="sm"
                radius="md"
                className="min-w-60"
              />
              <DatePickerInput
                label="Effective To"
                placeholder="Effective To"
                withAsterisk
                onChange={date => setEffectiveTo(date)}
                size="sm"
                radius="md"
                className="min-w-60"
              />
              <Select
                label="Shift"
                size="sm"
                radius="md"
                withAsterisk
                data={
                  shiftList?.data?.map(c => ({
                    label: `${c.shiftName} - ${c.shiftCode}`,
                    value: String(c.id),
                  })) ?? []
                }
                onChange={s => setShift(s)}
                placeholder="Select Shift"
                searchable
                clearable
                className="min-w-60"
              />
              <div className="flex flex-col justify-center items-center">
                <p>Action</p>
                <Button
                  onClick={handleSubmitShiftAssign}
                  color="orange"
                  radius="md"
                  size="sm"
                  fullWidth
                  className="px-10 font-medium min-w-40"
                  loading={isLoading}
                  disabled={isLoading}
                >
                  Submit
                </Button>
              </div>
            </div>
          </div>

          <DataTable
            data={employees}
            columns={columns}
            hideExport
            hideColumnVisibility
            hideSearch
            pageSizeOptions={[100]}
          />
        </div>
      )}
    </div>
  );
}
