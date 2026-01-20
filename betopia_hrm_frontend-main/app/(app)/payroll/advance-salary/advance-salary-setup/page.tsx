'use client';

import { useState, useMemo } from 'react';
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
import { useRouter } from "next/navigation";

/* ---------- Import APIs ---------- */
import { useCreateCashAdvanceSlabConfigMutation } from '@/lib/features/cashAdvanceSlabConfig/cashAdvanceSlabConfigAPI';
import { useGetCompanyListQuery } from '@/lib/features/employment/employmentGroupAPI';
import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
import { useGetOrganizationStructureListQuery } from '@/services/api/organization/organizationStructureAPI';

/* ---------- Types ---------- */
type RangeRow = {
  fromAmount: number | '';
  toAmount: number | '';
  serviceChargeAmount: number | '';
};

export default function AdvanceSalaryPage() {
  const router = useRouter();
  const [showRangeConfig, setShowRangeConfig] = useState(false);
  const [createConfig, { isLoading }] = useCreateCashAdvanceSlabConfigMutation();

  /* ---------- HRM Data ---------- */
  const { data: companyList } = useGetCompanyListQuery(undefined);
  const { data: empTypesData } = useGetEmploymentTypesQuery(undefined);
  const [selectedCompanyId, setSelectedCompanyId] = useState<number | null>(null);
  const [selectedBusinessUnitId, setSelectedBusinessUnitId] = useState<number | null>(null);

  const { data: organizationStructure } = useGetOrganizationStructureListQuery({
    companyId: selectedCompanyId || undefined,
    businessUnitId: selectedBusinessUnitId || undefined,
  });

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

  /* ---------- Formik ---------- */
  const formik = useFormik({
    initialValues: {
      setupName: '',
      eligibleEmployeeType: '',
      effectiveFrom: null as Date | null,
      effectiveTo: null as Date | null,
      companyId: '',
      businessUnitId: '',
      workplaceGroupId: '',
      workplaceId: '',
      requestApplicableDay: '',
      cashAdvancePercent: '',
      remarks: '',
      serviceChargeType: '', // 'Percent' | 'Fixed' | 'Range-wise Limit'
      serviceChargeAmount: '',
      rangeWiseLimitType: 'Percent%', // 'Percent%' | 'Fixed'
      allowRequestChange: false,
      status: true,
      cashAdvanceSlabConfigDetails: [] as RangeRow[],
    },
    validationSchema: Yup.object({
      setupName: Yup.string().required('Setup name is required'),
      eligibleEmployeeType: Yup.string().required('Employee type required'),
      effectiveFrom: Yup.date().required('Effective From required'),
      effectiveTo: Yup.date().required('Effective To required'),
      companyId: Yup.string().required('Company required'),
      businessUnitId: Yup.string().required('Business Unit required'),
      serviceChargeType: Yup.string().required('Service charge type required'),
      cashAdvanceSlabConfigDetails: Yup.array(
      Yup.object({
        fromAmount: Yup.number()
          .typeError('From is required')
          .min(0, 'From must be ≥ 0')
          .required('From is required'),
        toAmount: Yup.number()
          .typeError('To is required')
          .moreThan(Yup.ref('fromAmount'), 'To must be greater than From')
          .required('To is required'),
        serviceChargeAmount: Yup.number()
          .typeError('Charge is required')
          .moreThan(0, 'Charge must be > 0')
          .required('Charge is required'),
      })
    )
    .when('serviceChargeType', {
      is: 'Range-wise Limit',
      then: (schema) => schema.min(1, 'At least one range is required'),
      otherwise: (schema) => schema.notRequired(),
    })
    .default([]),

    }),
    onSubmit: async (values, { resetForm }) => {
      try {
        const topLevelType =
          values.serviceChargeType === 'Percent'
            ? 'PERCENTAGE'
            : values.serviceChargeType === 'Fixed'
            ? 'FIXED'
            : 'RANGE'; // ← expected by backend

        const rangeRowType =
          values.rangeWiseLimitType === 'Percent%' ? 'PERCENTAGE' : 'FIXED';

        const rawPayload = {
        setupName: values.setupName,
        advanceRequestDay: Number(values.requestApplicableDay || 0),
        employeeTypeId: Number(values.eligibleEmployeeType),
        companyId: Number(values.companyId),
        businessUnitId: values.businessUnitId
          ? Number(values.businessUnitId)
          : undefined,
        workplaceGroupId: values.workplaceGroupId
          ? Number(values.workplaceGroupId)
          : undefined,
        workplaceId: values.workplaceId ? Number(values.workplaceId) : undefined,
        effectiveFrom: dayjs(values.effectiveFrom).format("YYYY-MM-DD"),
        effectiveTo: dayjs(values.effectiveTo).format("YYYY-MM-DD"),
        advancePercent: Number(values.cashAdvancePercent || 0),
        serviceChargeType: topLevelType,
        serviceChargeAmount:
          topLevelType === "RANGE" ? 0 : Number(values.serviceChargeAmount || 0),
        remark: values.remarks,
        isApprovedAmountChange: values.allowRequestChange,
        status: values.status,
        cashAdvanceSlabConfigDetails:
          topLevelType === "RANGE"
            ? values.cashAdvanceSlabConfigDetails.map((r) => ({
                serviceChargeType: rangeRowType,
                serviceChargeAmount: Number(r.serviceChargeAmount),
                fromAmount: Number(r.fromAmount),
                toAmount: Number(r.toAmount),
              }))
            : [],
      };

        // Remove all undefined keys
        const payload = Object.fromEntries(
          Object.entries(rawPayload).filter(([_, v]) => v !== undefined)
        );

        const res = await createConfig(payload).unwrap();

        notifications.show({
          title: res?.success ? '✅ Success' : 'ℹ️ Info',
          message: res?.message || 'Cash Advance Configuration saved successfully!',
          color: res?.success ? 'green' : 'blue',
        });
        // Redirect after 2 seconds
        setTimeout(() => {
          router.push("/payroll/advance-salary");
        }, 2000);
        resetForm();
      } catch (error: any) {
        console.error('❌ Error:', error);
        notifications.show({
          title: 'Error',
          message:
            error?.data?.message ||
            error?.message ||
            'Failed to save Cash Advance Configuration',
          color: 'red',
        });
      }
    },
  });

  /* ---------- Handlers ---------- */
  const handleServiceChargeTypeChange = (value: string | null) => {
    formik.setFieldValue('serviceChargeType', value);
    const isRange = value === 'Range-wise Limit';
    setShowRangeConfig(isRange);

    if (isRange && formik.values.cashAdvanceSlabConfigDetails.length === 0) {
      // Seed one empty row
      formik.setFieldValue('cashAdvanceSlabConfigDetails', [
        { fromAmount: '', toAmount: '', serviceChargeAmount: '' },
      ]);
    }
    if (!isRange) {
      // Clear rows if switching away
      formik.setFieldValue('cashAdvanceSlabConfigDetails', []);
    }
  };

  const addRangeRow = () => {
    const rows = [...formik.values.cashAdvanceSlabConfigDetails];
    rows.push({ fromAmount: '', toAmount: '', serviceChargeAmount: '' });
    formik.setFieldValue('cashAdvanceSlabConfigDetails', rows);
  };

  const removeRangeRow = (idx: number) => {
    const rows = [...formik.values.cashAdvanceSlabConfigDetails];
    rows.splice(idx, 1);
    formik.setFieldValue('cashAdvanceSlabConfigDetails', rows);
  };

  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-orange-600">Advance Salary Configuration Setup</h1>
        <p className="text-gray-600">Define business rules for advance salary eligibility and deductions</p>
      </div>

      {/* Form */}
      <form onSubmit={formik.handleSubmit} className="space-y-8">
        {/* ---------------------- Eligibility Setup ---------------------- */}
        <div className="bg-white rounded-2xl shadow border border-gray-100">
          <div className="mb-6 bg-[#F4F7FA] py-6 px-6">
            <div className="flex items-center space-x-2">
              <span className="text-orange-600 text-xl">👥</span>
              <Title order={4} className="text-orange-700">
                Eligibility Setup
              </Title>
            </div>
            <p className="text-gray-600 text-sm mt-1">
              Configure employee eligibility criteria and applicable period
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6 px-6">
            <TextInput
              label="Setup Name *"
              placeholder="e.g., Standard Advance Policy"
              {...formik.getFieldProps('setupName')}
              error={formik.touched.setupName && formik.errors.setupName}
            />

            <Select
              label="Eligible Employee Type *"
              placeholder="Select employee type"
              data={employeeTypeOptions}
              value={formik.values.eligibleEmployeeType}
              onChange={(v) => formik.setFieldValue('eligibleEmployeeType', v)}
              error={formik.touched.eligibleEmployeeType && formik.errors.eligibleEmployeeType}
            />

            <DatePickerInput
              label="Effective From *"
              value={formik.values.effectiveFrom}
              onChange={(v) => formik.setFieldValue('effectiveFrom', v)}
              error={formik.touched.effectiveFrom && formik.errors.effectiveFrom}
            />
            <DatePickerInput
              label="Effective To *"
              value={formik.values.effectiveTo}
              onChange={(v) => formik.setFieldValue('effectiveTo', v)}
              error={formik.touched.effectiveTo && formik.errors.effectiveTo}
            />

            <Select
              label="Company *"
              placeholder="Select company"
              data={companyOptions}
              value={formik.values.companyId}
              onChange={(v) => {
                formik.setFieldValue('companyId', v);
                setSelectedCompanyId(Number(v));
                formik.setFieldValue('businessUnitId', '');
                formik.setFieldValue('workplaceGroupId', '');
                formik.setFieldValue('workplaceId', '');
              }}
              error={formik.touched.companyId && formik.errors.companyId}
            />

            <Select
              label="Business Unit *"
              placeholder="Select business unit"
              data={businessUnitOptions}
              value={formik.values.businessUnitId}
              onChange={(v) => {
                formik.setFieldValue('businessUnitId', v);
                setSelectedBusinessUnitId(Number(v));
                formik.setFieldValue('workplaceGroupId', '');
                formik.setFieldValue('workplaceId', '');
              }}
              disabled={!formik.values.companyId}
              error={formik.touched.businessUnitId && formik.errors.businessUnitId}
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
              placeholder="e.g., 15"
              value={formik.values.requestApplicableDay as any}
              onChange={(v) => formik.setFieldValue('requestApplicableDay', v ?? '')}
            />
            <NumberInput
              label="Cash Advance Percent %"
              placeholder="e.g., 50"
              value={formik.values.cashAdvancePercent as any}
              onChange={(v) => formik.setFieldValue('cashAdvancePercent', v ?? '')}
            />
          </div>

          <div className="mt-6 px-6 mb-6">
            <Textarea
              label="Remarks"
              placeholder="Add any additional notes..."
              {...formik.getFieldProps('remarks')}
            />
          </div>
        </div>

        {/* ---------------------- Service Charge Configuration ---------------------- */}
        <div className=" bg-white rounded-2xl shadow border border-gray-100">
          {/* Header */}
          <div className="mb-6 bg-[#F5F3FF] py-6">
            <div className="flex items-center space-x-2 px-6">
              <span className="text-purple-600 text-xl">💲</span>
              <Title order={4} className="text-purple-700">
                Service Charge Configuration
              </Title>
            </div>
            <p className="text-gray-600 text-sm mt-1 px-6">
              Configure service charge type and amount
            </p>
          </div>

          {/* Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 px-6 mb-6">
            {/* Service Charge Type */}
            <div>
              <Select
                label={
                  <span className="flex items-center gap-1">
                    <span className="text-purple-600">📈</span> Service Charge Type *
                  </span>
                }
                placeholder="Select type"
                data={['Percent', 'Fixed', 'Range-wise Limit']}
                value={formik.values.serviceChargeType}
                onChange={handleServiceChargeTypeChange}
                error={formik.touched.serviceChargeType && formik.errors.serviceChargeType}
              />
              <p className="text-xs text-gray-500 mt-1">Static Value (Percent/Fixed)</p>
            </div>

            {/* Service Charge Amount (hidden for range mode) */}
            {!showRangeConfig && (
              <div>
                <NumberInput
                  label="Service Charge Amount *"
                  placeholder="e.g., 5 or 1000"
                  value={formik.values.serviceChargeAmount as any}
                  onChange={(val) => formik.setFieldValue('serviceChargeAmount', val ?? 0)}
                  error={
                    !showRangeConfig &&
                    !formik.values.serviceChargeAmount &&
                    'Service Charge Amount is required'
                  }
                />
                <p className="text-xs text-gray-500 mt-1">
                  This field shows based on service charge type selection
                </p>
              </div>
            )}
          </div>
        </div>

        {/* ---------------------- Range-wise Limit Configuration ---------------------- */}
        {showRangeConfig && (
          <div className="p-6 bg-white rounded-2xl shadow border border-gray-100">
            {/* Header */}
            <div className="mb-6">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span className="text-blue-600 text-xl">📊</span>
                  <Title order={4} className="text-blue-700">
                    Range Wise Limit Configuration
                  </Title>
                </div>
                <Tooltip label="Add range">
                  <ActionIcon variant="filled" color="blue" onClick={addRangeRow}>
                    <IconPlus size={18} />
                  </ActionIcon>
                </Tooltip>
              </div>
              <p className="text-gray-600 text-sm mt-1">
                Add one or more ranges. The charge is interpreted as{' '}
                {formik.values.rangeWiseLimitType === 'Percent%' ? 'percent (%)' : 'fixed amount'}.
              </p>
            </div>

            {/* Range Type Selection */}
            <div className="p-4 rounded-xl bg-blue-50 border border-blue-100 mb-6">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Range Wise Limit Type *
              </label>

              <div className="flex items-center gap-6">
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
            </div>

            {/* Editable table */}
            <Table
              withTableBorder
              striped
              verticalSpacing="sm"
              horizontalSpacing="md"
              className="rounded-lg overflow-hidden"
            >
              <thead className="bg-gray-50 text-gray-600 text-sm">
                <tr>
                  <th className="w-14">SL</th>
                  <th>From</th>
                  <th>To</th>
                  <th>
                    Charge {formik.values.rangeWiseLimitType === 'Percent%' ? '(%)' : '(৳)'}
                  </th>
                  <th className="w-14 text-center">Del</th>
                </tr>
              </thead>
              <tbody>
                {formik.values.cashAdvanceSlabConfigDetails.map((row, idx) => (
                  <tr key={idx} className="text-sm">
                    <td>{idx + 1}</td>
                    <td>
                      <NumberInput
                        placeholder="From"
                        value={row.fromAmount as any}
                        onChange={(v) =>
                          formik.setFieldValue(
                            `cashAdvanceSlabConfigDetails[${idx}].fromAmount`,
                            v ?? ''
                          )
                        }
                        min={0}
                        error={
                          (formik.errors.cashAdvanceSlabConfigDetails as any)?.[idx]?.fromAmount
                        }
                      />
                    </td>
                    <td>
                      <NumberInput
                        placeholder="To"
                        value={row.toAmount as any}
                        onChange={(v) =>
                          formik.setFieldValue(
                            `cashAdvanceSlabConfigDetails[${idx}].toAmount`,
                            v ?? ''
                          )
                        }
                        min={0}
                        error={
                          (formik.errors.cashAdvanceSlabConfigDetails as any)?.[idx]?.toAmount
                        }
                      />
                    </td>
                    <td>
                      <NumberInput
                        placeholder={
                          formik.values.rangeWiseLimitType === 'Percent%' ? 'e.g. 5' : 'e.g. 1000'
                        }
                        value={row.serviceChargeAmount as any}
                        onChange={(v) =>
                          formik.setFieldValue(
                            `cashAdvanceSlabConfigDetails[${idx}].serviceChargeAmount`,
                            v ?? ''
                          )
                        }
                        min={0}
                        max={formik.values.rangeWiseLimitType === 'Percent%' ? 100 : undefined}
                        error={
                          (formik.errors.cashAdvanceSlabConfigDetails as any)?.[idx]
                            ?.serviceChargeAmount
                        }
                      />
                    </td>
                    <td className="text-center">
                      <ActionIcon color="red" variant="subtle" onClick={() => removeRangeRow(idx)}>
                        <IconTrash size={16} />
                      </ActionIcon>
                    </td>
                  </tr>
                ))}

                {formik.values.cashAdvanceSlabConfigDetails.length === 0 && (
                  <tr>
                    <td colSpan={5} className="text-center text-gray-500 py-4">
                      No ranges added yet. Click the ➕ to add one.
                    </td>
                  </tr>
                )}
              </tbody>
            </Table>

            <div className="mt-4 bg-blue-50 border border-blue-100 text-blue-700 text-xs rounded-lg p-3 flex items-start gap-2">
              <span>ℹ️</span>
              <p>
                Ensure ranges don’t overlap and that <b>To</b> is greater than <b>From</b>. Example:
                1–1000 → 5%, 1001–10000 → 10%.
              </p>
            </div>
          </div>
        )}

        {/* ---------------------- Additional Settings ---------------------- */}
        <div className="p-6 bg-gradient-to-r from-amber-50 to-yellow-50 rounded-2xl shadow">
          <Title order={4} className="text-amber-700 mb-2">
            Additional Settings
          </Title>
          <p className="text-gray-600 mb-4 text-sm">Configure approval and status settings</p>

          {/* Allow Request Amount Change */}
          <div className="border border-amber-200 rounded-xl bg-white/40 p-4 flex items-center justify-between mb-4">
            <div>
              <p className="font-medium text-gray-800">Allow Request Amount Change</p>
              <p className="text-gray-500 text-sm">Allows users to change the amount for the final approver</p>
            </div>
            <Switch
              checked={formik.values.allowRequestChange}
              onChange={(e) => formik.setFieldValue('allowRequestChange', e.currentTarget.checked)}
              color="orange"
            />
          </div>

          {/* Status */}
          <div className="border border-green-300 rounded-xl bg-green-50 p-4 flex items-center justify-between">
            <div>
              <p className="font-medium text-gray-800">Status</p>
              <p className="text-gray-500 text-sm">Configuration is currently active</p>
            </div>

            <div className="flex items-center space-x-3">
              <Badge
                color="green"
                variant="outline"
                radius="lg"
                className="text-green-700 border-green-400 bg-green-100 px-3 py-1 text-sm font-medium"
              >
                {formik.values.status ? 'Active' : 'Inactive'}
              </Badge>

              <Switch
                checked={formik.values.status}
                onChange={(e) => formik.setFieldValue('status', e.currentTarget.checked)}
                color="green"
              />
            </div>
          </div>
        </div>

        {/* ---------------------- Configuration Guidelines ---------------------- */}
        <div className="p-6 bg-green-50 rounded-2xl text-sm text-gray-700">
          <Title order={5} className="text-green-700 mb-2">
            Configuration Guidelines:
          </Title>
          <ul className="list-disc pl-5 space-y-1">
            <li>All fields marked with * are mandatory.</li>
            <li>Employee types support multi-selection for flexibility.</li>
            <li>Business Unit, Workplace Group, and Workplace are cascading dropdowns.</li>
            <li>Service charge field visibility depends on the selected type.</li>
            <li>Range Wise Limit options appear only when this type is enabled.</li>
            <li>Cash Advance Percent applies to advance salary request forms.</li>
          </ul>
        </div>

        {/* ---------------------- Footer Buttons ---------------------- */}
        <div className="mt-6 w-full">
          <div className="grid w-full grid-cols-2 gap-3">
            <Button
              variant="default"
              onClick={() => formik.resetForm()}
              disabled={isLoading}
              fullWidth
            >
              Clear
            </Button>

            <Button
              type="submit"
              color="orange"
              loading={isLoading}
              loaderProps={{ type: 'dots' }}
              fullWidth
            >
              Save
            </Button>
          </div>
        </div>
      </form>
    </div>
  );
}
