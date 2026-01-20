
'use client';

import {
  ActionIcon,
  Badge,
  Button,
  NumberInput,
  Select,
  Switch,
  Table,
  Textarea,
  TextInput,
  Title,
  Tooltip,
} from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { IconPlus, IconTrash } from '@tabler/icons-react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import dayjs from 'dayjs';
import { notifications } from '@mantine/notifications';
import { useMemo, useState } from 'react';
import {
  useUpdateCashAdvanceSlabConfigMutation,
} from '@/lib/features/cashAdvanceSlabConfig/cashAdvanceSlabConfigAPI';
import {
  useGetCompanyListQuery,
} from '@/lib/features/employment/employmentGroupAPI';
import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
import { useGetOrganizationStructureListQuery } from '@/services/api/organization/organizationStructureAPI';

// ✅ Define TypeScript props type
type EditCashAdvanceSlabConfigModalProps = {
  row: {
    id: number;
    setupName: string;
    companyId: number;
    businessUnitId?: number | null;
    workplaceGroupId?: number | null;
    workplaceId?: number | null;
    employeeTypeId: number;
    serviceChargeType: string;
    serviceChargeAmount: number;
    advanceRequestDay: number;
    advancePercent: number;
    effectiveFrom: string;
    effectiveTo: string;
    remark?: string;
    isApprovedAmountChange?: boolean;
    status: boolean;
    cashAdvanceSlabConfigDetails?: {
      fromAmount: number;
      toAmount: number;
      serviceChargeAmount: number;
      serviceChargeType: string;
    }[];
  };
  onClose: () => void;
  onSuccess?: () => void;
};

export function EditCashAdvanceSlabConfigModal({ row, onClose, onSuccess }: EditCashAdvanceSlabConfigModalProps) {
  const [updateConfig, { isLoading }] = useUpdateCashAdvanceSlabConfigMutation();

  // ─── Fetch master data ───────────────────────────────────────────────────────
  const { data: companyList } = useGetCompanyListQuery(undefined);
  const { data: empTypesData } = useGetEmploymentTypesQuery(undefined);

  const [selectedCompanyId, setSelectedCompanyId] = useState<number | null>(
    Number(row.companyId)
  );
  const [selectedBusinessUnitId, setSelectedBusinessUnitId] =
    useState<number | null>(Number(row.businessUnitId));

  const { data: organizationStructure } = useGetOrganizationStructureListQuery({
    companyId: selectedCompanyId || undefined,
    businessUnitId: selectedBusinessUnitId || undefined,
  });

  // ─── Dropdown options ───────────────────────────────────────────────────────
  const companyOptions = useMemo(
    () =>
      (companyList?.data || []).map((c: any) => ({
        label: c.name,
        value: String(c.id),
      })),
    [companyList]
  );

  const employeeTypeOptions = useMemo(
    () =>
      (empTypesData?.data || []).map((t: any) => ({
        label: t.name,
        value: String(t.id),
      })),
    [empTypesData]
  );

  const businessUnitOptions = useMemo(
    () =>
      (organizationStructure?.data?.businessUnits || []).map((b: any) => ({
        label: b.name,
        value: String(b.id),
      })),
    [organizationStructure]
  );

  const workplaceGroupOptions = useMemo(
    () =>
      (organizationStructure?.data?.workplaceGroups || []).map((wg: any) => ({
        label: wg.name,
        value: String(wg.id),
      })),
    [organizationStructure]
  );

  const workplaceOptions = useMemo(
    () =>
      (organizationStructure?.data?.workplaces || []).map((w: any) => ({
        label: w.name,
        value: String(w.id),
      })),
    [organizationStructure]
  );

  // ─── Detect Range Mode ──────────────────────────────────────────────────────
  const isInitialRange =
    row.serviceChargeType === 'RANGE' || row.serviceChargeType === 'RANGE_WISE';
  const [showRangeConfig, setShowRangeConfig] = useState(isInitialRange);

  const initialRangeList = row.cashAdvanceSlabConfigDetails?.length
    ? row.cashAdvanceSlabConfigDetails.map((r: any) => ({
        fromAmount: r.fromAmount || '',
        toAmount: r.toAmount || '',
        serviceChargeAmount: r.serviceChargeAmount || '',
      }))
    : [];

  // ─── Formik setup ──────────────────────────────────────────────────────────
  const formik = useFormik({
    initialValues: {
      setupName: row.setupName || '',
      employeeTypeId: String(row.employeeTypeId || ''),
      companyId: String(row.companyId || ''),
      businessUnitId: String(row.businessUnitId || ''),
      workplaceGroupId: row.workplaceGroupId ? String(row.workplaceGroupId) : '',
      workplaceId: row.workplaceId ? String(row.workplaceId) : '',
      effectiveFrom: new Date(row.effectiveFrom),
      effectiveTo: new Date(row.effectiveTo),
      advanceRequestDay: row.advanceRequestDay || 0,
      advancePercent: row.advancePercent || 0,
      serviceChargeType: row.serviceChargeType || 'PERCENTAGE',
      serviceChargeAmount: row.serviceChargeAmount || 0,
      remarks: row.remark || '',
      approvedAmountChange: row.isApprovedAmountChange || false,
      status: row.status ?? true,
      rangeWiseLimitType:
        row.cashAdvanceSlabConfigDetails?.[0]?.serviceChargeType === 'FIXED'
          ? 'Fixed'
          : 'Percent%',
      cashAdvanceSlabConfigDetails: initialRangeList,
    },
    validationSchema: Yup.object({
      setupName: Yup.string().required('Setup name is required'),
      companyId: Yup.string().required('Company is required'),
      businessUnitId: Yup.string().required('Business unit is required'),
      employeeTypeId: Yup.string().required('Employee type is required'),
      serviceChargeType: Yup.string().required('Service charge type is required'),
    }),
    onSubmit: async (values) => {
      try {
        const topLevelType =
          values.serviceChargeType === 'PERCENTAGE'
            ? 'PERCENTAGE'
            : values.serviceChargeType === 'FIXED'
            ? 'FIXED'
            : 'RANGE';

        const rangeRowType =
          values.rangeWiseLimitType === 'Percent%' ? 'PERCENTAGE' : 'FIXED';

        // ✅ Define payload type locally
        type CashAdvanceSlabConfigPayload = {
          id: number;
          setupName: string;
          advanceRequestDay: number;
          employeeTypeId: number;
          companyId: number;
          businessUnitId?: number;
          workplaceGroupId?: number;
          workplaceId?: number;
          effectiveFrom: string;
          effectiveTo: string;
          advancePercent: number;
          serviceChargeType: string;
          serviceChargeAmount: number;
          remark?: string;
          isApprovedAmountChange: boolean;
          status: boolean;
          cashAdvanceSlabConfigDetails: {
            serviceChargeType: string;
            serviceChargeAmount: number;
            fromAmount: number;
            toAmount: number;
          }[];
        };

        // ✅ Construct payload with safe optional handling
        const rawPayload: CashAdvanceSlabConfigPayload = {
          id: row.id,
          setupName: values.setupName,
          advanceRequestDay: Number(values.advanceRequestDay),
          employeeTypeId: Number(values.employeeTypeId),
          companyId: Number(values.companyId),
          businessUnitId: values.businessUnitId
            ? Number(values.businessUnitId)
            : undefined,
          workplaceGroupId: values.workplaceGroupId
            ? Number(values.workplaceGroupId)
            : undefined,
          workplaceId: values.workplaceId
            ? Number(values.workplaceId)
            : undefined,

          effectiveFrom: dayjs(values.effectiveFrom).format('YYYY-MM-DD'),
          effectiveTo: dayjs(values.effectiveTo).format('YYYY-MM-DD'),
          advancePercent: Number(values.advancePercent),
          serviceChargeType: topLevelType,
          serviceChargeAmount:
            topLevelType === 'RANGE' ? 0 : Number(values.serviceChargeAmount),
          remark: values.remarks,
          isApprovedAmountChange: values.approvedAmountChange,
          status: values.status,
          cashAdvanceSlabConfigDetails:
            topLevelType === 'RANGE'
              ? values.cashAdvanceSlabConfigDetails.map((r) => ({
                  serviceChargeType: rangeRowType,
                  serviceChargeAmount: Number(r.serviceChargeAmount),
                  fromAmount: Number(r.fromAmount),
                  toAmount: Number(r.toAmount),
                }))
              : [],
        };

        if (values.businessUnitId)
          rawPayload.businessUnitId = Number(values.businessUnitId);
        if (values.workplaceGroupId)
          rawPayload.workplaceGroupId = Number(values.workplaceGroupId);
        if (values.workplaceId)
          rawPayload.workplaceId = Number(values.workplaceId);

        // ✅ Strip undefined safely before sending
        const payload = Object.fromEntries(
          Object.entries(rawPayload).filter(([_, v]) => v !== undefined)
        ) as CashAdvanceSlabConfigPayload;

        const res = await updateConfig(payload as any).unwrap();

        notifications.show({
          title: res?.success ? '✅ Success' : 'ℹ️ Info',
          message:
            res?.message || 'Cash Advance Configuration updated successfully!',
          color: res?.success ? 'green' : 'blue',
        });

        // ✅ redirect after 2s
        // setTimeout(() => {
        //   window.location.href = '/payroll/advance-salary';
        // }, 2000);

        onSuccess?.();
        onClose();
      } catch (err: any) {
        notifications.show({
          title: '❌ Update Failed',
          message: err?.data?.message || 'Failed to update configuration.',
          color: 'red',
        });
      }
    },

  });

  // ─── Range row handlers ─────────────────────────────────────────────────────
  const handleServiceChargeTypeChange = (value: string | null) => {
    const mapped =
      value === 'Percent'
        ? 'PERCENTAGE'
        : value === 'Fixed'
        ? 'FIXED'
        : 'RANGE';
    formik.setFieldValue('serviceChargeType', mapped);
    setShowRangeConfig(mapped === 'RANGE');
  };

  const addRangeRow = () => {
    const list = [...formik.values.cashAdvanceSlabConfigDetails];
    list.push({ fromAmount: '', toAmount: '', serviceChargeAmount: '' });
    formik.setFieldValue('cashAdvanceSlabConfigDetails', list);
  };

  const removeRangeRow = (idx: number) => {
    const list = [...formik.values.cashAdvanceSlabConfigDetails];
    list.splice(idx, 1);
    formik.setFieldValue('cashAdvanceSlabConfigDetails', list);
  };

  // ─── JSX ────────────────────────────────────────────────────────────────────
  return (
    <form onSubmit={formik.handleSubmit} className="space-y-6">
      {/* Eligibility Setup */}
      <div className="bg-white rounded-2xl shadow border border-gray-100">
        <div className="mb-6 bg-[#F4F7FA] py-6 px-6">
          <div className="flex items-center space-x-2">
            <span className="text-orange-600 text-xl">👥</span>
            <Title order={4} className="text-orange-700">
              Eligibility Setup
            </Title>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 px-6">
          <TextInput label="Setup Name *" {...formik.getFieldProps('setupName')} />

          <Select
            label="Employee Type *"
            data={employeeTypeOptions}
            value={formik.values.employeeTypeId}
            onChange={(v) => formik.setFieldValue('employeeTypeId', v)}
          />

          <DatePickerInput
            label="Effective From *"
            value={formik.values.effectiveFrom}
            onChange={(v) => formik.setFieldValue('effectiveFrom', v)}
          />
          <DatePickerInput
            label="Effective To *"
            value={formik.values.effectiveTo}
            onChange={(v) => formik.setFieldValue('effectiveTo', v)}
          />

                    <Select
            label="Company *"
            data={companyOptions}
            value={formik.values.companyId}
            onChange={(v) => {
              formik.setFieldValue('companyId', v);
              setSelectedCompanyId(Number(v));
              formik.setFieldValue('businessUnitId', '');
              formik.setFieldValue('workplaceGroupId', '');
              formik.setFieldValue('workplaceId', '');
            }}
            error={formik.errors.companyId}
          />

          <Select
            label="Business Unit *"
            data={businessUnitOptions}
            value={formik.values.businessUnitId}
            onChange={(v) => {
              formik.setFieldValue('businessUnitId', v);
              setSelectedBusinessUnitId(Number(v));
            }}
            error={formik.errors.businessUnitId}
            disabled={!formik.values.companyId}
          />

          <Select
            label="Workplace Group"
            data={workplaceGroupOptions}
            value={formik.values.workplaceGroupId}
            onChange={(v) => formik.setFieldValue('workplaceGroupId', v)}
            disabled={!formik.values.businessUnitId}
          />

          <Select
            label="Workplace"
            data={workplaceOptions}
            value={formik.values.workplaceId}
            onChange={(v) => formik.setFieldValue('workplaceId', v)}
            disabled={!formik.values.workplaceGroupId}
          />


          <NumberInput
            label="Request Applicable Day"
            value={formik.values.advanceRequestDay}
            onChange={(v) => formik.setFieldValue('advanceRequestDay', v ?? 0)}
          />
          <NumberInput
            label="Advance Percent %"
            value={formik.values.advancePercent}
            onChange={(v) => formik.setFieldValue('advancePercent', v ?? 0)}
          />
        </div>

        <div className="mt-6 px-6 mb-6">
          <Textarea label="Remarks" {...formik.getFieldProps('remarks')} />
        </div>
      </div>

      {/* Service Charge Config */}
      <div className="bg-white rounded-2xl shadow border border-gray-100">
        <div className="mb-6 bg-[#F5F3FF] py-6 px-6 flex items-center space-x-2">
          <span className="text-purple-600 text-xl">💲</span>
          <Title order={4} className="text-purple-700">
            Service Charge Configuration
          </Title>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 px-6 mb-6">
          <Select
            label="Service Charge Type *"
            data={['Percent', 'Fixed', 'Range-wise Limit']}
            value={
              formik.values.serviceChargeType === 'PERCENTAGE'
                ? 'Percent'
                : formik.values.serviceChargeType === 'FIXED'
                ? 'Fixed'
                : 'Range-wise Limit'
            }
            onChange={handleServiceChargeTypeChange}
          />

          {!showRangeConfig && (
            <NumberInput
              label="Service Charge Amount"
              value={formik.values.serviceChargeAmount}
              onChange={(v) => formik.setFieldValue('serviceChargeAmount', v ?? 0)}
            />
          )}
        </div>
      </div>

      {/* Range Config */}
      {showRangeConfig && (
        <div className="p-6 bg-white rounded-2xl shadow border border-gray-100">
          <div className="flex justify-between items-center mb-4">
            <Title order={4} className="text-blue-700 flex items-center gap-2">
              <span>📊</span> Range Wise Limit Configuration
            </Title>
            <Tooltip label="Add new range">
              <ActionIcon color="blue" onClick={addRangeRow}>
                <IconPlus size={16} />
              </ActionIcon>
            </Tooltip>
          </div>

          {/* Range type toggle */}
          <div className="mb-4 flex gap-6">
            <label className="flex items-center gap-2">
              <input
                type="radio"
                name="rangeWiseLimitType"
                value="Percent%"
                checked={formik.values.rangeWiseLimitType === 'Percent%'}
                onChange={() => formik.setFieldValue('rangeWiseLimitType', 'Percent%')}
                className="accent-blue-600"
              />
              <span className="text-blue-600 font-medium text-sm">% Percent</span>
            </label>
            <label className="flex items-center gap-2">
              <input
                type="radio"
                name="rangeWiseLimitType"
                value="Fixed"
                checked={formik.values.rangeWiseLimitType === 'Fixed'}
                onChange={() => formik.setFieldValue('rangeWiseLimitType', 'Fixed')}
                className="accent-green-600"
              />
              <span className="text-green-600 font-medium text-sm">৳ Fixed</span>
            </label>
          </div>

          {/* Table */}
          <Table striped withTableBorder>
            <thead className="bg-gray-50 text-gray-600 text-sm">
              <tr>
                <th>SL</th>
                <th>From</th>
                <th>To</th>
                <th>
                  Charge {formik.values.rangeWiseLimitType === 'Percent%' ? '(%)' : '(৳)'}
                </th>
                <th>Del</th>
              </tr>
            </thead>
            <tbody>
              {formik.values.cashAdvanceSlabConfigDetails.map((r, i) => (
                <tr key={i}>
                  <td>{i + 1}</td>
                  <td>
                    <NumberInput
                      value={r.fromAmount as any}
                      onChange={(v) =>
                        formik.setFieldValue(
                          `cashAdvanceSlabConfigDetails[${i}].fromAmount`,
                          v ?? ''
                        )
                      }
                    />
                  </td>
                  <td>
                    <NumberInput
                      value={r.toAmount as any}
                      onChange={(v) =>
                        formik.setFieldValue(
                          `cashAdvanceSlabConfigDetails[${i}].toAmount`,
                          v ?? ''
                        )
                      }
                    />
                  </td>
                  <td>
                    <NumberInput
                      value={r.serviceChargeAmount as any}
                      onChange={(v) =>
                        formik.setFieldValue(
                          `cashAdvanceSlabConfigDetails[${i}].serviceChargeAmount`,
                          v ?? ''
                        )
                      }
                    />
                  </td>
                  <td className="text-center">
                    <ActionIcon color="red" variant="subtle" onClick={() => removeRangeRow(i)}>
                      <IconTrash size={16} />
                    </ActionIcon>
                  </td>
                </tr>
              ))}
              {formik.values.cashAdvanceSlabConfigDetails.length === 0 && (
                <tr>
                  <td colSpan={5} className="text-center text-gray-500 py-3">
                    No range data. Click ➕ to add one.
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </div>
      )}

      {/* Additional Settings */}
      <div className="p-6 bg-gradient-to-r from-amber-50 to-yellow-50 rounded-2xl shadow">
        <Title order={4} className="text-amber-700 mb-2">
          Additional Settings
        </Title>

        <div className="border border-amber-200 bg-white p-4 rounded-xl flex justify-between mb-4">
          <div>
            <p className="font-medium text-gray-800">Allow Request Amount Change</p>
            <p className="text-gray-500 text-sm">
              Allows user to modify the approved amount
            </p>
          </div>
          <Switch
            checked={formik.values.approvedAmountChange}
            onChange={(e) =>
              formik.setFieldValue('approvedAmountChange', e.currentTarget.checked)
            }
            color="orange"
          />
        </div>

        <div className="border border-green-300 bg-green-50 p-4 rounded-xl flex justify-between">
          <div>
            <p className="font-medium text-gray-800">Status</p>
            <p className="text-gray-500 text-sm">
              Active or Inactive configuration
            </p>
          </div>
          <div className="flex items-center space-x-3">
            <Badge
              color={formik.values.status ? 'green' : 'red'}
              variant="outline"
              radius="lg"
              className="px-3 py-1 text-sm"
            >
              {formik.values.status ? 'Active' : 'Inactive'}
            </Badge>
            <Switch
              checked={formik.values.status}
              onChange={(e) =>
                formik.setFieldValue('status', e.currentTarget.checked)
              }
              color="green"
            />
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="flex justify-end gap-3 pt-3">
        <Button variant="default" onClick={onClose}>
          Cancel
        </Button>
        <Button type="submit" color="orange" loading={isLoading}>
          Save Changes
        </Button>
      </div>
    </form>
  );
}

