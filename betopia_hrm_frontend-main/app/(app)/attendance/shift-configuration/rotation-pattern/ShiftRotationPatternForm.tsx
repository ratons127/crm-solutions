'use client';

import {
  ActionIcon,
  Checkbox,
  NumberInput,
  Select,
  Switch,
  Table,
  Textarea,
  TextInput,
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { TrashIcon } from '@heroicons/react/24/outline';
import SubmitSection from '@/components/common/form/SubmitSection';

import {
  useCreateShiftRotationPatternMutation,
  useUpdateShiftRotationPatternMutation,
} from '@/lib/features/shiftRotationPattern/shiftRotationPatternAPI';
import { useGetPaginatedShiftDefinitionsQuery } from '@/lib/features/shiftDefinition/shiftDefinitionAPI';
import {
  ShiftRotationPattern,
  ShiftRotationPatternRequest,
} from '@/lib/types/shiftRotationPattern';

import { useGetCompanyListQuery } from '@/services/api/admin/company/companyAPI';
import { useGetBusinessUnitListQuery } from '@/services/api/admin/company/businessUnitAPI';
import { useGetWorkplaceGroupListQuery } from '@/services/api/admin/company/workplaceGroupAPI';
import { useGetWorkplaceListQuery } from '@/services/api/admin/company/workplaceAPI';
import { useGetDepartmentListQuery } from '@/services/api/admin/company/departmentAPI';
import { useGetTeamListQuery } from '@/services/api/admin/company/teamAPI';
import { useEffect } from 'react';

type Props = {
  action: 'create' | 'update' | null;
  data: ShiftRotationPattern | null;
  onClose: () => void;
};

/* ✅ Validation Schema */
const validationSchema = Yup.object({
  patternName: Yup.string().required('Pattern name is required'),
  rotationDays: Yup.number()
    .required('Rotation days required')
    .min(1, 'Must be at least 1'),
  companyId: Yup.number().required().min(1),
  businessUnitId: Yup.number().required().min(1),
  workPlaceGroupId: Yup.number().required().min(1),
  workPlaceId: Yup.number().required().min(1),
  departmentId: Yup.number().required().min(1),
  teamId: Yup.number().required().min(1),
  status: Yup.boolean().optional(),
  shiftRotationPatternDetails: Yup.array()
    .of(
      Yup.object({
        pattern: Yup.string().required(),
        dayNumber: Yup.number().required(),
        shiftId: Yup.number().required().min(1, 'Shift is required'),
        offDay: Yup.boolean().required(),
      })
    )
    .min(1, 'At least one pattern detail is required'),
});

export default function ShiftRotationPatternForm({
  action,
  data,
  onClose,
}: Props) {
  const [createItem, { isLoading }] = useCreateShiftRotationPatternMutation();
  const [updateItem, { isLoading: isLoadingUpdate }] =
    useUpdateShiftRotationPatternMutation();

  const { data: companies } = useGetCompanyListQuery({});
  const { data: businessUnits } = useGetBusinessUnitListQuery({});
  const { data: workplaceGroups } = useGetWorkplaceGroupListQuery({});
  const { data: workplaces } = useGetWorkplaceListQuery({});
  const { data: departments } = useGetDepartmentListQuery({});
  const { data: teams } = useGetTeamListQuery({});
  const { data: paginatedShifts } = useGetPaginatedShiftDefinitionsQuery({
    page: 1,
    perPage: 100,
    sortDirection: 'ASC',
  });

  /* ✅ Formik setup */
  const formik = useFormik<ShiftRotationPatternRequest>({
    initialValues:
      action === 'update' && data
        ? {
            patternName: data.patternName,
            description: data.description ?? '',
            rotationDays: data.rotationDays ?? 0,
            companyId: data.companyId,
            businessUnitId: data.businessUnitId,
            workPlaceGroupId: data.workPlaceGroupId,
            workPlaceId: data.workPlaceId,
            departmentId: data.departmentId,
            teamId: data.teamId,
            status: data.status,
            shiftRotationPatternDetails:
              data.shiftRotationPatternDetails || [],
          }
        : {
            patternName: '',
            description: '',
            rotationDays: 0,
            companyId: 0,
            businessUnitId: 0,
            workPlaceGroupId: 0,
            workPlaceId: 0,
            departmentId: 0,
            teamId: 0,
            status: true,
            shiftRotationPatternDetails: [],
          },
    validationSchema,
    onSubmit: async (values) => {
      try {
        if (action === 'create') {
          await createItem(values).unwrap();
        //   notifications.show({
        //     title: 'Shift Rotation Pattern Created',
        //     color: 'green',
        //   });
          onClose();
        } else if (action === 'update' && data?.id) {
          await updateItem({ id: data.id, data: values }).unwrap();
        //   notifications.show({
        //     title: 'Shift Rotation Pattern Updated',
        //     color: 'blue',
        //   });
          onClose();
        }
      } catch (error: any) {
        notifications.show({
          title: 'Error',
          message: error?.data?.message || 'Something went wrong',
          color: 'red',
        });
      }
    },
  });

  /* ✅ Auto-generate pattern details when rotationDays changes */
  useEffect(() => {
    const days = formik.values.rotationDays || 0;
    const existing = formik.values.shiftRotationPatternDetails;
    if (days > 0 && existing.length !== days) {
      const newDetails = Array.from({ length: days }, (_, i) => ({
        createdDate: new Date().toISOString(),
        lastModifiedDate: new Date().toISOString(),
        createdBy: 0,
        lastModifiedBy: 0,
        id: 0,
        pattern: String.fromCharCode(65 + i), // A, B, C ...
        dayNumber: i + 1,
        shiftId: 0,
        offDay: false,
      }));
      formik.setFieldValue('shiftRotationPatternDetails', newDetails);
    }
  }, [formik.values.rotationDays]);

  /* ✅ Handle row removal */
  const removeDetail = (index: number) => {
    const updated = formik.values.shiftRotationPatternDetails.filter(
      (_, i) => i !== index
    );
    formik.setFieldValue('shiftRotationPatternDetails', updated);
  };

  return (
    <form onSubmit={formik.handleSubmit}>
      <h1 className="text-lg font-semibold mb-4">
        {action === 'create'
          ? 'Create Shift Rotation Pattern'
          : 'Update Shift Rotation Pattern'}
      </h1>

      <div className="grid grid-cols-12 gap-3">
        <TextInput
          label="Pattern Name"
          placeholder="Enter pattern name"
          withAsterisk
          className="col-span-6"
          name="patternName"
          value={formik.values.patternName}
          onChange={formik.handleChange}
          error={formik.errors.patternName}
        />
        <NumberInput
          label="Rotation Days"
          placeholder="Enter number of days"
          className="col-span-6"
          value={formik.values.rotationDays}
          onChange={(v) => formik.setFieldValue('rotationDays', v || 0)}
          error={formik.errors.rotationDays}
        />

        <Textarea
          label="Description"
          className="col-span-12"
          name="description"
          placeholder="Enter pattern description"
          value={formik.values.description}
          onChange={formik.handleChange}
        />

        {/* Hierarchy Selects */}
        <Select
          label="Company"
          className="col-span-6"
          withAsterisk
          data={
            companies?.data?.map((c: any) => ({
              label: c.name,
              value: c.id.toString(),
            })) || []
          }
          value={formik.values.companyId?.toString()}
          onChange={(v) =>
            formik.setFieldValue('companyId', v ? Number(v) : 0)
          }
        />
        <Select
          label="Business Unit"
          className="col-span-6"
          withAsterisk
          data={
            businessUnits?.data?.map((b: any) => ({
              label: b.name,
              value: b.id.toString(),
            })) || []
          }
          value={formik.values.businessUnitId?.toString()}
          onChange={(v) =>
            formik.setFieldValue('businessUnitId', v ? Number(v) : 0)
          }
        />
        <Select
          label="Workplace Group"
          className="col-span-6"
          withAsterisk
          data={
            workplaceGroups?.data?.map((g: any) => ({
              label: g.name,
              value: g.id.toString(),
            })) || []
          }
          value={formik.values.workPlaceGroupId?.toString()}
          onChange={(v) =>
            formik.setFieldValue('workPlaceGroupId', v ? Number(v) : 0)
          }
        />
        <Select
          label="Workplace"
          className="col-span-6"
          withAsterisk
          data={
            workplaces?.data?.map((w: any) => ({
              label: w.name,
              value: w.id.toString(),
            })) || []
          }
          value={formik.values.workPlaceId?.toString()}
          onChange={(v) =>
            formik.setFieldValue('workPlaceId', v ? Number(v) : 0)
          }
        />
        <Select
          label="Department"
          className="col-span-6"
          withAsterisk
          data={
            departments?.data?.map((d: any) => ({
              label: d.name,
              value: d.id.toString(),
            })) || []
          }
          value={formik.values.departmentId?.toString()}
          onChange={(v) =>
            formik.setFieldValue('departmentId', v ? Number(v) : 0)
          }
        />
        <Select
          label="Team"
          className="col-span-6"
          withAsterisk
          data={
            teams?.data?.map((t: any) => ({
              label: t.name,
              value: t.id.toString(),
            })) || []
          }
          value={formik.values.teamId?.toString()}
          onChange={(v) =>
            formik.setFieldValue('teamId', v ? Number(v) : 0)
          }
        />

        <Switch
          labelPosition="left"
          className="col-span-6 mt-2"
          label="Active Pattern"
          checked={formik.values.status}
          onChange={(e) =>
            formik.setFieldValue('status', e.currentTarget.checked)
          }
        />
      </div>

      {/* Pattern Details Table */}
      <div className="mt-6">
        <h2 className="font-semibold text-base mb-2">Pattern Details</h2>
        <Table withColumnBorders striped highlightOnHover>
          <thead>
            <tr>
              <th>Day</th>
              <th>Pattern</th>
              <th>Shift</th>
              <th>Off Day</th>
              <th className="text-center w-[50px]">Action</th>
            </tr>
          </thead>
          <tbody>
            {formik.values.shiftRotationPatternDetails.map((d, i) => (
              <tr key={i}>
                <td>{d.dayNumber}</td>
                <td>
                  <TextInput
                    value={d.pattern}
                    onChange={(e) =>
                      formik.setFieldValue(
                        `shiftRotationPatternDetails[${i}].pattern`,
                        e.target.value
                      )
                    }
                  />
                </td>
                <td>
                  <Select
                    placeholder="Select shift"
                    data={
                      paginatedShifts?.data?.map((s: any) => ({
                        label: `${s.shiftName} (${s.shiftCode})`,
                        value: s.id.toString(),
                      })) || []
                    }
                    value={d.shiftId ? d.shiftId.toString() : ''}
                    onChange={(v) =>
                      formik.setFieldValue(
                        `shiftRotationPatternDetails[${i}].shiftId`,
                        v ? Number(v) : 0
                      )
                    }
                  />
                </td>
                <td className="text-center">
                  <Checkbox
                    checked={d.offDay}
                    onChange={(e) =>
                      formik.setFieldValue(
                        `shiftRotationPatternDetails[${i}].offDay`,
                        e.currentTarget.checked
                      )
                    }
                  />
                </td>
                <td className="text-center">
                  <ActionIcon
                    color="red"
                    variant="subtle"
                    onClick={() => removeDetail(i)}
                  >
                    <TrashIcon className="h-4 w-4" />
                  </ActionIcon>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      <div className="col-span-12 mt-5">
        <SubmitSection
          isLoading={isLoading || isLoadingUpdate}
          onCancel={onClose}
          confirmText={action === 'create' ? 'Create' : 'Update'}
        />
      </div>
    </form>
  );
}
