'use client';

import React, { useEffect, useMemo, useState } from 'react';
import { useSelector } from 'react-redux';
import { ColumnDef } from '@tanstack/react-table';
import { ErrorMessage, Form, Formik } from 'formik';
import * as Yup from 'yup';
import { BsThreeDots } from 'react-icons/bs';
import ConfigPageLayout from '../ConfigPageLayout';
import Modal from '@/components/common/Modal';
import SelectInput from '@/components/common/SelectInput';
import { DataTable } from '@/components/common/table/DataTable';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';

import { notifications } from '@mantine/notifications';
import { TextInput, Textarea, Button, Switch, Group, Select, Loader } from '@mantine/core';
import { useGetCountryListQuery } from '@/services/api/admin/location/countryAPI';
import { useGetLocationListQuery } from '@/services/api/admin/location/locationAPI';

import { useAppDispatch, useAppSelector } from '@/lib/hooks';


/* ===================== COMPANY ===================== */
import {
  fetchCompanies,
  createCompanyThunk,
  updateCompanyThunk,
  deleteCompanyThunk,
  selectCompaniesState,
  fetchAllCurrenciesThunk,
  fetchAllZonesThunk,
  selectCompanyCurrencies,
  selectCompanyZones,
  selectCompanyLoadingCurrencies,
  selectCompanyLoadingZones,
} from '../../../lib/features/companyConfig/companyConfigSlice';
import type { CompanyRow } from '../../../lib/features/companyConfig/companyConfigApi';

/* ---------- Local option type ---------- */
type SelectOption = { label: string; value: string };

/* Helpers to read both old and new shapes safely */
const getCountryId = (x: any) => String(x?.country?.id ?? x?.countryId ?? '');
const getParentId  = (x: any) => String(x?.parent?.id ?? x?.parentId ?? '');
const getType      = (x: any) => String(x?.type ?? '').toUpperCase();
const byNameAsc    = (a: SelectOption, b: SelectOption) => a.label.localeCompare(b.label);

export function buildLocationOptions(
  locations: any[] = [],
  countryId?: string | number | '',
  divisionId?: string | number | '',
  districtId?: string | number | ''
) {
  const cid   = String(countryId ?? '');
  const did   = String(divisionId ?? '');
  const disId = String(districtId ?? '');

  const matchCountry = (x: any) => (cid ? getCountryId(x) === cid : true);

  const divisions: SelectOption[] = locations
    .filter((x) => matchCountry(x) && getType(x) === 'DIVISION')
    .map((x) => ({ label: String(x.name ?? ''), value: String(x.id) }))
    .sort(byNameAsc);

  const districts: SelectOption[] = locations
    .filter(
      (x) =>
        matchCountry(x) &&
        getType(x) === 'DISTRICT' &&
        getParentId(x) === did
    )
    .map((x) => ({ label: String(x.name ?? ''), value: String(x.id) }))
    .sort(byNameAsc);

  const policeStations: SelectOption[] = locations
    .filter(
      (x) =>
        matchCountry(x) &&
        getType(x) === 'POLICE_STATION' &&
        getParentId(x) === disId
    )
    .map((x) => ({ label: String(x.name ?? ''), value: String(x.id) }))
    .sort(byNameAsc);

  const postOffices: SelectOption[] = locations
    .filter(
      (x) =>
        matchCountry(x) &&
        getType(x) === 'POST_OFFICE' &&
        getParentId(x) === disId
    )
    .map((x) => ({ label: String(x.name ?? ''), value: String(x.id) }))
    .sort(byNameAsc);

  return { divisions, districts, policeStations, postOffices };
}


/* ===================== BUSINESS UNIT ===================== */
import {
  fetchBusinessUnits,
  createBusinessUnit,
  updateBusinessUnitById,
  deleteBusinessUnitById,
} from '../../../lib/features/companyConfig/businessUnitApi';
import type { BusinessUnitRow } from '../../../lib/features/companyConfig/businessUnitSlice';

/* ===================== WORKPLACE ===================== */
import {
  fetchWorkplaces,
  createWorkplace,
  updateWorkplaceById,
  deleteWorkplaceById,
} from '../../../lib/features/companyConfig/workplaceApi';
import type { WorkplaceRow } from '../../../lib/features/companyConfig/workplaceSlice';

/* ===================== WORKPLACE GROUP ===================== */
import {
  fetchWorkplaceGroups,
  createWorkplaceGroup,
  updateWorkplaceGroupById,
  deleteWorkplaceGroupById,
} from '../../../lib/features/companyConfig/workplaceGroupApi';
import type { WorkplaceGroupRow } from '../../../lib/features/companyConfig/workplaceGroupSlice';

/* ===================== DEPARTMENT ===================== */
import {
  fetchDepartments,
  createDepartment,
  updateDepartmentById,
  deleteDepartmentById,
} from '../../../lib/features/companyConfig/departmentApi';
import type { DepartmentRow } from '../../../lib/features/companyConfig/departmentSlice';

/* ===================== TEAM ===================== */
import {
  fetchTeams,
  createTeam,
  updateTeamById,
  deleteTeamById,
} from '../../../lib/features/companyConfig/teamApi';
import type { TeamRow } from '../../../lib/features/companyConfig/teamSlice';


import { RootState } from '@/lib/store';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/solid';

/* =======================================================
   COMPANY — form values + schema
======================================================= */
type CompanyFormValues = {
  companyLogo: File | null;
  companyName: string;
  companyShortName: string;
  companyCode: string;
  phone: string;
  companyEmail: string;
  companyWebsite: string;
  companyDescription: string;
  countryId: string | number | '';
  divisionId: string | number | '';
  districtId: string | number | '';
  policeStationId: string | number | '';
  postOfficeId: string | number | '';
  address: string;
  status: boolean;
  currency: string;
  timeZone: string;
};

const initialValues: CompanyFormValues = {
  companyLogo: null,
  companyName: '',
  companyShortName: '',
  companyCode: '',
  phone: '',
  companyEmail: '',
  companyWebsite: '',
  companyDescription: '',
  countryId: '',
  divisionId: '',
  districtId: '',
  policeStationId: '',
  postOfficeId: '',
  address: '',
  status: true,
  currency: '',
  timeZone: '',
};

const Schema = Yup.object({
  companyName: Yup.string().min(1).max(100, 'Max 100 chars').required('Company Name is required'),
  companyShortName: Yup.string().max(30, 'Max 30 chars').nullable(),
  companyCode: Yup.string().max(20, 'Max 20 chars').nullable(),
  phone: Yup.string()
    .matches(/^01[0-9]{9}$/, 'Phone must start with 01 and be exactly 11 digits')
    .required('Phone is required'),
  companyEmail: Yup.string().email('Invalid email').max(100).nullable(),
  companyWebsite: Yup.string().url('Invalid URL').max(100).nullable(),
  companyDescription: Yup.string().max(200, 'Max 200 chars').nullable(),
  countryId: Yup.string().required('Country is required'),

  divisionId: Yup.string().when('countryId', {
    is: (v: any) => !!v,
    then: (s) => s.required('Division is required'),
    otherwise: (s) => s.nullable(),
  }),
  districtId: Yup.string().when('divisionId', {
    is: (v: any) => !!v,
    then: (s) => s.required('District is required'),
    otherwise: (s) => s.nullable(),
  }),
  policeStationId: Yup.string().when('districtId', {
    is: (v: any) => !!v,
    then: (s) => s.required('Police Station is required'),
    otherwise: (s) => s.nullable(),
  }),
  postOfficeId: Yup.string().when('districtId', {
    is: (v: any) => !!v,
    then: (s) => s.required('Post Office is required'),
    otherwise: (s) => s.nullable(),
  }),

  address: Yup.string().max(100, 'Max 100 chars').nullable(),
  status: Yup.boolean().default(true),
  currency: Yup.string().required('Currency is required'),
  timeZone: Yup.string().required('Time zone is required'),
});

/* =========================
   Create Company Modal
========================= */
function CreateCompanyModal({
  open,
  onClose,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [, setLogoPreview] = useState<string | null>(null);

  const currencies = useAppSelector(selectCompanyCurrencies);
  const zones = useAppSelector(selectCompanyZones);
  const loadingCurrencies = useAppSelector(selectCompanyLoadingCurrencies);
  const loadingZones = useAppSelector(selectCompanyLoadingZones);

  const { data: countryList } = useGetCountryListQuery({});
  const { data: locationRes } = useGetLocationListQuery({});
  const locations = locationRes?.data ?? [];

  useEffect(() => {
    if (!open) {
      dispatch(fetchAllCurrenciesThunk());
      dispatch(fetchAllZonesThunk());
      setLogoPreview(null);
    }
  }, [open, dispatch]);

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Company" size="4xl">
      <Formik<CompanyFormValues>
        initialValues={initialValues}
        validationSchema={Schema}
        validateOnChange
        validateOnBlur
        enableReinitialize
        onSubmit={async (values, { resetForm, setSubmitting  }) => {
          try {
            await dispatch(createCompanyThunk(values)).unwrap();
            notifications.show({ color: 'green', title: 'Company created', message: values.companyName });
            resetForm();
            setLogoPreview(null);
            onClose();
            onDone();
          } catch (e: any) {
            const msg = e?.response?.data?.message || e?.message || 'Failed to create company';
            notifications.show({ color: 'red', title: 'Create failed', message: msg });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, setFieldTouched, isSubmitting, errors, touched }) => {
          // derive options for current values (pure function, no hooks)
          const { divisions, districts, policeStations, postOffices } = buildLocationOptions(
            locations,
            values.countryId,
            values.divisionId,
            values.districtId
          );

          return (
            <Form className="space-y-5">
              <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                {/* <div className="flex flex-col">
                  <FileInput
                    label="Company Logo"
                    placeholder="Choose file"
                    accept="image/png,image/jpeg,image/jpg,image/gif"
                    value={values.companyLogo}
                    onChange={(file) => {
                      if (!file) {
                        setFieldValue('companyLogo', null);
                        setLogoPreview(null);
                        return;
                      }
                      const valid = ['image/png', 'image/jpeg', 'image/jpg', 'image/gif'];
                      if (!valid.includes(file.type)) {
                        notifications.show({ color: 'red', title: 'Invalid file', message: 'Use jpg, jpeg, gif, or png.' });
                        return;
                      }
                      if (file.size > 1024 * 1024) {
                        notifications.show({ color: 'red', title: 'File too large', message: 'Max 1MB allowed.' });
                        return;
                      }
                      setFieldValue('companyLogo', file);
                      setLogoPreview(URL.createObjectURL(file));
                    }}
                    clearable
                  />
                  {logoPreview && (
                    <div className="mt-2">
                      <img src={logoPreview} alt="Logo preview" className="h-12 w-12 rounded object-cover border" />
                    </div>
                  )}
                  <ErrorMessage name="companyLogo" component="div" className="mt-1 text-xs text-red-600" />
                </div> */}

                <TextInput
                  label="Company Name"
                  required
                  name="companyName"
                  value={values.companyName}
                  onChange={(e) => setFieldValue('companyName', e.currentTarget.value)}
                  error={touched.companyName && errors.companyName ? (errors.companyName as string) : undefined}
                />
                <TextInput
                  label="Company Short Name"
                  name="companyShortName"
                  value={values.companyShortName}
                  onChange={(e) => setFieldValue('companyShortName', e.currentTarget.value)}
                />
                <TextInput
                  label="Company Code"
                  required
                  name="companyCode"
                  value={values.companyCode}
                  onChange={(e) => setFieldValue('companyCode', e.currentTarget.value)}
                />
                <TextInput
                  label="Phone"
                  name="phone"
                  placeholder="Enter phone number"
                  value={values.phone}
                  onChange={(e) => setFieldValue('phone', e.currentTarget.value)}
                  onBlur={() => setFieldTouched('phone', true)}
                  error={touched.phone && errors.phone ? (errors.phone as string) : undefined}
                />

                <TextInput
                  label="Company Email"
                  name="companyEmail"
                  value={values.companyEmail}
                  onChange={(e) => setFieldValue('companyEmail', e.currentTarget.value)}
                  error={touched.companyEmail && errors.companyEmail ? (errors.companyEmail as string) : undefined}
                />
                <TextInput
                  label="Company Website"
                  name="companyWebsite"
                  placeholder="https://example.com"
                  value={values.companyWebsite}
                  onChange={(e) => setFieldValue('companyWebsite', e.currentTarget.value)}
                  error={touched.companyWebsite && errors.companyWebsite ? (errors.companyWebsite as string) : undefined}
                />

                <Group justify="space-between" mt="md">
                  <Switch
                    checked={values.status}
                    label="Status"
                    onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
                  />
                </Group>
                <TextInput
                  label="Address"
                  name="address"
                  value={values.address}
                  placeholder="Address"
                  onChange={(e) => setFieldValue('address', e.currentTarget.value)}
                  error={touched.address && errors.address ? (errors.address as string) : undefined}
                />
                <div className="sm:col-span-2">
                  <Textarea
                    label="Description"
                    name="companyDescription"
                    minRows={2}
                    autosize
                    value={values.companyDescription}
                    onChange={(e) => setFieldValue('companyDescription', e.currentTarget.value)}
                  />
                </div>

                {/* Currency & Time zone */}
                <Select
                  name="currency"
                  required
                  label="Currency"
                  data={currencies}
                  value={String(values.currency ?? '') || null}
                  onChange={(v) => setFieldValue('currency', v ?? '')}
                  placeholder={loadingCurrencies ? 'Loading...' : 'Select Currency'}
                  searchable
                  clearable={false}
                  disabled={loadingCurrencies}
                  rightSection={loadingCurrencies ? <Loader size="xs" /> : null}
                  maxDropdownHeight={300}
                  nothingFoundMessage="No matches"
                  comboboxProps={{ withinPortal: true }}
                  error={touched.currency && errors.currency ? String(errors.currency) : undefined}
                />
                <Select
                  name="timeZone"
                  required
                  label="Time Zone"
                  data={zones}
                  value={String(values.timeZone ?? '') || null}
                  onChange={(v) => setFieldValue('timeZone', v ?? '')}
                  placeholder={loadingZones ? 'Loading...' : 'Select Time Zone'}
                  searchable
                  clearable={false}
                  disabled={loadingZones}
                  rightSection={loadingZones ? <Loader size="xs" /> : null}
                  maxDropdownHeight={300}
                  nothingFoundMessage="No matches"
                  comboboxProps={{ withinPortal: true }}
                  error={touched.timeZone && errors.timeZone ? String(errors.timeZone) : undefined}
                />

                {/* Country */}
                <Select
                  placeholder="Select Country"
                  label="Country"
                  withAsterisk
                  searchable
                  clearable
                  data={
                    countryList?.data?.map((c) => ({
                      label: c.name,
                      value: String(c.id),
                    })) ?? []
                  }
                  name="countryId"
                  value={String(values.countryId ?? '') || null}
                  onChange={(value) => {
                    setFieldValue('countryId', value ?? '');
                    setFieldValue('divisionId', '');
                    setFieldValue('districtId', '');
                    setFieldValue('policeStationId', '');
                    setFieldValue('postOfficeId', '');
                  }}
                />

                {/* Division */}
                <Select
                  label="Division"
                  withAsterisk
                  placeholder="Select Division"
                  searchable
                  clearable
                  data={divisions}
                  name="divisionId"
                  disabled={!values.countryId}
                  value={String(values.divisionId ?? '') || null}
                  onChange={(v) => {
                    setFieldValue('divisionId', v ?? '');
                    setFieldValue('districtId', '');
                    setFieldValue('policeStationId', '');
                    setFieldValue('postOfficeId', '');
                  }}
                />

                {/* District */}
                <Select
                  label="District"
                  withAsterisk
                  placeholder="Select District"
                  searchable
                  clearable
                  data={districts}
                  name="districtId"
                  disabled={!values.countryId || !values.divisionId}
                  value={String(values.districtId ?? '') || null}
                  onChange={(v) => {
                    setFieldValue('districtId', v ?? '');
                    setFieldValue('policeStationId', '');
                    setFieldValue('postOfficeId', '');
                  }}
                />

                {/* Police Station */}
                <Select
                  label="Police Station"
                  withAsterisk
                  placeholder="Select Police Station"
                  searchable
                  clearable
                  data={policeStations}
                  name="policeStationId"
                  disabled={!values.countryId || !values.divisionId || !values.districtId}
                  value={String(values.policeStationId ?? '') || null}
                  onChange={(v) => {
                    setFieldValue('policeStationId', v ?? '');
                  }}
                />

                {/* Post Office */}
                <Select
                  label="Post Office"
                  withAsterisk
                  placeholder="Select Post Office"
                  searchable
                  clearable
                  data={postOffices}
                  name="postOfficeId"
                  disabled={!values.countryId || !values.divisionId || !values.districtId}
                  value={String(values.postOfficeId ?? '') || null}
                  onChange={(v) => setFieldValue('postOfficeId', v ?? '')}
                />
              </div>

              <div className="w-full flex justify-end gap-2 pt-2">
                <Button variant="outline" type="button" color="red" onClick={onClose} disabled={isSubmitting}>
                  Cancel
                </Button>
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? 'Saving…' : 'Save'}
                </Button>
              </div>
            </Form>
          );
        }}
      </Formik>
    </Modal>
  );
}

/* =========================
   Edit Company Modal
========================= */
function EditCompanyModal({
  open,
  onClose,
  company,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  company: CompanyRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [, setLogoPreview] = useState<string | null>(null);

  const init: CompanyFormValues = useMemo(() => {
    if (!company) return initialValues;
    return {
      companyLogo: null,
      companyName: company.name ?? '',
      companyShortName: company.shortName ?? '',
      companyCode: company.code ?? '',
      phone: company.phone ?? '',
      companyEmail: company.email ?? '',
      companyWebsite: company.websiteUrl ?? '',
      companyDescription: company.description ?? '',
      countryId: (company.countryId as any) ?? '',
      divisionId: (company.divisionId as any) ?? '',
      districtId: (company.districtId as any) ?? '',
      policeStationId: (company.thana as any) ?? '',
      postOfficeId: (company.postOffice as any) ?? '',
      address: company.address ?? '',
      status: !!company.status,
      currency: (company.currency as any) ?? '',
      timeZone: (company.timeZone as any) ?? '',
    };
  }, [company]);

  const currencies = useAppSelector(selectCompanyCurrencies);
  const zones = useAppSelector(selectCompanyZones);
  const loadingCurrencies = useAppSelector(selectCompanyLoadingCurrencies);
  const loadingZones = useAppSelector(selectCompanyLoadingZones);

  const { data: countryList } = useGetCountryListQuery({});
  const { data: locationRes } = useGetLocationListQuery({});
  const locations = locationRes?.data ?? [];

  useEffect(() => {
    if (!open) {
      dispatch(fetchAllCurrenciesThunk());
      dispatch(fetchAllZonesThunk());
      setLogoPreview(null);
    }
  }, [open, dispatch]);

  if (!open || !company) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Company" size="4xl">
      <Formik<CompanyFormValues>
        initialValues={init}
        validationSchema={Schema}
        enableReinitialize
        onSubmit={async (values, { resetForm, setSubmitting }) => {
          try {
            await dispatch(updateCompanyThunk({ id: company.id, ...values })).unwrap();
            notifications.show({ color: 'green', title: 'Company updated', message: values.companyName });
            resetForm();
            setLogoPreview(null);
            onClose();
            onDone();
          } catch (e: any) {
            const msg = e?.response?.data?.message || e?.message || 'Failed to update company';
            notifications.show({ color: 'red', title: 'Update failed', message: msg });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, setFieldTouched, isSubmitting, errors, touched }) => {

          return (
            <Form className="space-y-5">
              <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                {/* <div className="flex flex-col">
                  <FileInput
                    label="Company Logo"
                    placeholder="Choose file"
                    accept="image/png,image/jpeg,image/jpg,image/gif"
                    value={values.companyLogo}
                    onChange={(file) => {
                      if (!file) {
                        setFieldValue('companyLogo', null);
                        setLogoPreview(null);
                        return;
                      }
                      const valid = ['image/png', 'image/jpeg', 'image/jpg', 'image/gif'];
                      if (!valid.includes(file.type)) {
                        notifications.show({ color: 'red', title: 'Invalid file', message: 'Use jpg, jpeg, gif, or png.' });
                        return;
                      }
                      if (file.size > 1024 * 1024) {
                        notifications.show({ color: 'red', title: 'File too large', message: 'Max 1MB allowed.' });
                        return;
                      }
                      setFieldValue('companyLogo', file);
                      setLogoPreview(URL.createObjectURL(file));
                    }}
                    clearable
                  />
                  {(logoPreview || company.logoUrl) && (
                    <div className="mt-2">
                      <img
                        src={logoPreview || (company.logoUrl as string)}
                        alt="Logo preview"
                        className="h-12 w-12 rounded object-cover border"
                      />
                    </div>
                  )}
                  <ErrorMessage name="companyLogo" component="div" className="mt-1 text-xs text-red-600" />
                </div> */}

                <TextInput
                  label="Company Name"
                  required
                  name="companyName"
                  value={values.companyName}
                  onChange={(e) => setFieldValue('companyName', e.currentTarget.value)}
                  error={touched.companyName && errors.companyName ? (errors.companyName as string) : undefined}
                />
                <TextInput
                  label="Company Short Name"
                  name="companyShortName"
                  value={values.companyShortName}
                  onChange={(e) => setFieldValue('companyShortName', e.currentTarget.value)}
                />
                <TextInput
                  label="Company Code"
                  required
                  name="companyCode"
                  value={values.companyCode}
                  onChange={(e) => setFieldValue('companyCode', e.currentTarget.value)}
                />
                <TextInput
                  label="Phone"
                  name="phone"
                  placeholder="Enter phone number"
                  value={values.phone}
                  onChange={(e) => setFieldValue('phone', e.currentTarget.value)}
                  onBlur={() => setFieldTouched('phone', true)}
                  error={touched.phone && errors.phone ? (errors.phone as string) : undefined}
                />
                <TextInput
                  label="Company Email"
                  name="companyEmail"
                  value={values.companyEmail}
                  onChange={(e) => setFieldValue('companyEmail', e.currentTarget.value)}
                  error={touched.companyEmail && errors.companyEmail ? (errors.companyEmail as string) : undefined}
                />
                <TextInput
                  label="Company Website"
                  name="companyWebsite"
                  placeholder="https://example.com"
                  value={values.companyWebsite}
                  onChange={(e) => setFieldValue('companyWebsite', e.currentTarget.value)}
                  error={touched.companyWebsite && errors.companyWebsite ? (errors.companyWebsite as string) : undefined}
                />

                <Group justify="space-between" mt="md">
                  <Switch
                    checked={values.status}
                    label="Status"
                    onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
                  />
                </Group>
                <TextInput
                  label="Address"
                  name="address"
                  value={values.address}
                  placeholder="Address"
                  onChange={(e) => setFieldValue('address', e.currentTarget.value)}
                  error={touched.address && errors.address ? (errors.address as string) : undefined}
                />
                <div className="sm:col-span-2">
                  <Textarea
                    label="Description"
                    name="companyDescription"
                    minRows={2}
                    autosize
                    value={values.companyDescription}
                    onChange={(e) => setFieldValue('companyDescription', e.currentTarget.value)}
                  />
                </div>

                <Select
                  name="currency"
                  withAsterisk
                  label="Currency"
                  data={currencies}
                  value={String(values.currency ?? '') || null}
                  onChange={(v) => setFieldValue('currency', v ?? '')}
                  placeholder={loadingCurrencies ? 'Loading...' : 'Select Currency'}
                  searchable
                  clearable={false}
                  disabled={loadingCurrencies}
                  rightSection={loadingCurrencies ? <Loader size="xs" /> : null}
                  maxDropdownHeight={300}
                  nothingFoundMessage="No matches"
                  comboboxProps={{ withinPortal: true }}
                  error={touched.currency && errors.currency ? String(errors.currency) : undefined}
                />
                <Select
                  name="timeZone"
                  withAsterisk
                  label="Time Zone"
                  data={zones}
                  value={String(values.timeZone ?? '') || null}
                  onChange={(v) => setFieldValue('timeZone', v ?? '')}
                  placeholder={loadingZones ? 'Loading...' : 'Select Time Zone'}
                  searchable
                  clearable={false}
                  disabled={loadingZones}
                  rightSection={loadingZones ? <Loader size="xs" /> : null}
                  maxDropdownHeight={300}
                  nothingFoundMessage="No matches"
                  comboboxProps={{ withinPortal: true }}
                  error={touched.timeZone && errors.timeZone ? String(errors.timeZone) : undefined}
                />

                {/* Country */}
                <Select
                  placeholder="Select Country"
                  label="Country"
                  withAsterisk
                  searchable
                  clearable
                  data={
                    countryList?.data?.map((c) => ({
                      label: c.name,
                      value: String(c.id),
                    })) ?? []
                  }
                  name="countryId"
                  value={String(values.countryId ?? '') || null}
                  onChange={(value) => {
                    setFieldValue('countryId', value ?? '');
                    setFieldValue('divisionId', '');
                    setFieldValue('districtId', '');
                    setFieldValue('policeStationId', '');
                    setFieldValue('postOfficeId', '');
                  }}
                />

                {/* Division */}
                <Select
                  label="Division"
                  withAsterisk
                  placeholder="Select Division"
                  searchable
                  clearable
                  data={buildLocationOptions(
                    locations,
                    values.countryId,
                    values.divisionId,
                    values.districtId
                  ).divisions}
                  name="divisionId"
                  disabled={!values.countryId}
                  value={String(values.divisionId ?? '') || null}
                  onChange={(v) => {
                    setFieldValue('divisionId', v ?? '');
                    setFieldValue('districtId', '');
                    setFieldValue('policeStationId', '');
                    setFieldValue('postOfficeId', '');
                  }}
                />

                {/* District */}
                <Select
                  label="District"
                  withAsterisk
                  placeholder="Select District"
                  searchable
                  clearable
                  data={buildLocationOptions(
                    locations,
                    values.countryId,
                    values.divisionId,
                    values.districtId
                  ).districts}
                  name="districtId"
                  disabled={!values.countryId || !values.divisionId}
                  value={String(values.districtId ?? '') || null}
                  onChange={(v) => {
                    setFieldValue('districtId', v ?? '');
                    setFieldValue('policeStationId', '');
                    setFieldValue('postOfficeId', '');
                  }}
                />

                {/* Police Station */}
                <Select
                  label="Police Station"
                  withAsterisk
                  placeholder="Select Police Station"
                  searchable
                  clearable
                  data={buildLocationOptions(
                    locations,
                    values.countryId,
                    values.divisionId,
                    values.districtId
                  ).policeStations}
                  name="policeStationId"
                  disabled={!values.countryId || !values.divisionId || !values.districtId}
                  value={String(values.policeStationId ?? '') || null}
                  onChange={(v) => {
                    setFieldValue('policeStationId', v ?? '');
                  }}
                />

                {/* Post Office */}
                <Select
                  label="Post Office"
                  withAsterisk
                  placeholder="Select Post Office"
                  searchable
                  clearable
                  data={buildLocationOptions(
                    locations,
                    values.countryId,
                    values.divisionId,
                    values.districtId
                  ).postOffices}
                  name="postOfficeId"
                  disabled={!values.countryId || !values.divisionId || !values.districtId}
                  value={String(values.postOfficeId ?? '') || null}
                  onChange={(v) => setFieldValue('postOfficeId', v ?? '')}
                />

                {/* Currency & Timezone */}
              </div>

              <div className="w-full flex justify-end gap-2 pt-2">
                <Button variant="outline" type="button" color="red" onClick={onClose} disabled={isSubmitting}>
                  Cancel
                </Button>
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? 'Saving…' : 'Save changes'}
                </Button>
              </div>
            </Form>
          );
        }}
      </Formik>
    </Modal>
  );
}

/* =========================
   Delete Company Modal
========================= */
function DeleteCompanyModal({
  open,
  onClose,
  company,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  company: CompanyRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!open) setErr(null);
  }, [open]);

  if (!open || !company) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete company">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete <span className="font-medium">{company.name}</span>?
      </p>
      {!!err && <p className="mt-2 text-sm text-red-600 break-words">{err}</p>}
      <div className="mt-4 flex items-center justify-end gap-2">
        <Button onClick={onClose} variant="outline" color="gray" disabled={busy}>
          Cancel
        </Button>
        <Button
        color='red'
          onClick={async () => {
            try {
              setBusy(true);
              await dispatch(deleteCompanyThunk(company.id)).unwrap();
              notifications.show({ color: 'green', title: 'Company deleted', message: company.name });
              onClose();
              onDone();
            } catch (e: any) {
              const msg = e?.response?.data?.message || e?.message || 'Failed to delete';
              setErr(msg);
              notifications.show({ color: 'red', title: 'Delete failed', message: msg });
            } finally {
              setBusy(false);
            }
          }}
          disabled={busy}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}

/* =========================
   Business Unit Modals (with Mantine Select)
========================= */
const BuSchema = Yup.object({
  company: Yup.string().required('Company is required'),
  name: Yup.string().min(1).max(100).required('Name is required'),
  code: Yup.string().max(30).nullable(),
  description: Yup.string().max(200).nullable(),
  status: Yup.boolean().required('Status is required'),
});

/* =========================
   Create Business Unit Modal
========================= */
export function CreateBusinessUnitModal({
  open,
  onClose,
  onDone,
  companyOptions,
}: {
  open: boolean;
  onClose: () => void;
  onDone: () => void;
  companyOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  const normalizedCompanyOptions = useMemo(
    () => (companyOptions ?? []).map((o) => ({ ...o, value: String(o.value) })),
    [companyOptions]
  );

  const init = {
    company: '',
    name: '',
    code: '',
    description: '',
    status: true, // default ACTIVE
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Business Unit" size="lg">
      <Formik
        initialValues={init}
        validationSchema={BuSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting, resetForm }) => {
          try {
            await dispatch<any>(
              createBusinessUnit({
                company: Number(values.company),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status, // boolean to API
              })
            );

            notifications.show({
              color: 'green',
              title: 'Business Unit created',
              message: values.name,
            });

            resetForm();
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Create failed',
              message:
                e?.response?.data?.message ??
                e?.message ??
                'Failed to create business unit',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            {/* <SelectInput
              name="company"
              label="Company"
              value={String(values.company) || undefined}
              options={normalizedCompanyOptions}
              placeholder="Select Company"
              isClearable={false}
              onValueChange={(opt: any) =>
                setFieldValue('company', opt ? String(opt.value ?? opt) : '')
              }
            /> */}

            <Select
              name="company"
              withAsterisk
              label="Company"
              placeholder="Select Company"
              data={normalizedCompanyOptions} // [{ value: '1', label: 'Betopia' }, ...]
              value={values.company ? String(values.company) : ''}
              searchable
              clearable={false}
              onChange={(val) => setFieldValue('company', val || '')}
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Mantine Switch for status (boolean) */}
            <Switch
              label={values.status ? 'Active' : 'Inactive'}
              checked={!!values.status}
              onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
            />
            {touched.status && errors.status ? (
              <div className="text-red-600 text-sm">{String(errors.status)}</div>
            ) : null}

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

/* =========================
   Edit Business Unit Modal
========================= */
export function EditBusinessUnitModal({
  open,
  onClose,
  unit,
  onDone,
  companyOptions,
}: {
  open: boolean;
  onClose: () => void;
  unit: BusinessUnitRow | null;
  onDone: () => void;
  companyOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  // Hooks must run before any early return
  const normalizedCompanyOptions = useMemo(
    () => (companyOptions ?? []).map((o) => ({ ...o, value: String(o.value) })),
    [companyOptions]
  );

  const init = useMemo(
    () => ({
      company: String(unit?.companyId ?? ''),
      name: unit?.name ?? '',
      code: unit?.code ?? '',
      description: unit?.description ?? '',
      // slice returns 'ACTIVE' | 'INACTIVE' | null → convert to boolean for the form
      status: (unit?.status ?? 'ACTIVE') === 'ACTIVE',
    }),
    [unit]
  );

  if (!open || !unit) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Business Unit" size="lg">
      <Formik
        key={String(unit.id)}
        initialValues={init}
        validationSchema={BuSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await dispatch<any>(
              updateBusinessUnitById(unit.id, {
                company: Number(values.company),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status, // boolean to API
              })
            );

            notifications.show({
              color: 'green',
              title: 'Business Unit updated',
              message: values.name,
            });

            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Update failed',
              message:
                e?.response?.data?.message ??
                e?.message ??
                'Failed to update business unit',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            {/* <SelectInput
              name="company"
              label="Company"
              value={
                normalizedCompanyOptions.find(
                  (o) => o.value === String(values.company)
                )?.value ?? ''
              }
              options={normalizedCompanyOptions}
              placeholder="Select Company"
              isClearable={false}
              onValueChange={(opt: any) =>
                setFieldValue('company', opt ? String(opt.value ?? opt) : '')
              }
            /> */}

            <Select
              name="company"
              withAsterisk
              label="Company"
              placeholder="Select Company"
              data={normalizedCompanyOptions} // [{ value: '1', label: 'Betopia' }, ...]
              value={
                normalizedCompanyOptions.find(
                  (o) => o.value === String(values.company)
                )?.value ?? ''
              }
              searchable
              clearable={false}
              onChange={(val) => setFieldValue('company', val || '')}
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Mantine Switch for status (boolean) */}
            <Switch
              label={values.status ? 'Active' : 'Inactive'}
              checked={!!values.status}
              onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
            />
            {touched.status && errors.status ? (
              <div className="text-red-600 text-sm">{String(errors.status)}</div>
            ) : null}

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save changes'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}


function DeleteBusinessUnitModal({
  open,
  onClose,
  unit,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  unit: BusinessUnitRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!open) setErr(null);
  }, [open]);

  if (!open || !unit) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete business unit">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete <span className="font-medium">{unit.name}</span>?
      </p>
      {!!err && <p className="mt-2 text-sm text-red-600 break-words">{err}</p>}
      <div className="mt-4 flex items-center justify-end gap-2">
        <Button onClick={onClose} variant="outline" color="gray" disabled={busy}>
          Cancel
        </Button>
        <Button
          color="red"
          onClick={async () => {
            try {
              setBusy(true);
              await dispatch<any>(deleteBusinessUnitById(unit.id));
              notifications.show({ color: 'green', title: 'Business Unit deleted', message: unit.name });
              onClose();
              onDone();
            } catch (e: any) {
              const msg = e?.response?.data?.message || e?.message || 'Failed to delete';
              setErr(msg);
              notifications.show({ color: 'red', title: 'Delete failed', message: msg });
            } finally {
              setBusy(false);
            }
          }}
          disabled={busy}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}

/* =======================================================
   WORKPLACE — Modals & schema
======================================================= */
const WpSchema = Yup.object({
  workplaceGroupId: Yup.number()
    .typeError('Workplace Group is required')
    .positive()
    .required('Workplace Group is required'),
  name: Yup.string().min(1).max(100, 'Max 100 chars').required('Name is required'),
  code: Yup.string().max(30).nullable(),
  address: Yup.string().max(120).nullable(),
  description: Yup.string().max(200).nullable(),
  status: Yup.boolean().required('Status is required'), // ← boolean now
});

/* =========================
   Create Workplace
========================= */
export function CreateWorkplaceModal({
  open,
  onClose,
  onDone,
  wpgOptions,
}: {
  open: boolean;
  onClose: () => void;
  onDone: () => void;
  wpgOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  const init = {
    workplaceGroupId: '' as any,
    name: '',
    code: '',
    address: '',
    description: '',
    status: true, // default ACTIVE
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Workplace" size="lg">
      <Formik
        initialValues={init}
        validationSchema={WpSchema}
        onSubmit={async (values, { setSubmitting, resetForm }) => {
          try {
            await dispatch<any>(
              createWorkplace({
                workplaceGroupId: Number(values.workplaceGroupId),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                address: values.address?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status, // ← boolean to API
              })
            );
            notifications.show({ color: 'green', title: 'Workplace created', message: values.name });
            resetForm();
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Create failed',
              message: e?.response?.data?.message ?? e?.message ?? 'Failed to create workplace',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            <SelectInput
              name="workplaceGroupId"
              required
              label="Workplace Group"
              value={values.workplaceGroupId}
              options={wpgOptions}
              placeholder="Select Workplace Group"
              isClearable={false}
              onValueChange={(v) => setFieldValue('workplaceGroupId', v)}
            />
            <ErrorMessage name="workplaceGroupId" component="div" className="text-xs text-red-600" />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <TextInput
              label="Address"
              value={values.address}
              onChange={(e) => setFieldValue('address', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Mantine Switch for status (boolean) */}
            <Switch
              label={values.status ? 'Active' : 'Inactive'}
              checked={!!values.status}
              onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
            />
            {touched.status && errors.status ? (
              <div className="text-red-600 text-sm">{String(errors.status)}</div>
            ) : null}

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

/* =========================
   Edit Workplace
========================= */
export function EditWorkplaceModal({
  open,
  onClose,
  workplace,
  onDone,
  wpgOptions,
}: {
  open: boolean;
  onClose: () => void;
  workplace: WorkplaceRow | null;
  onDone: () => void;
  wpgOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();
  if (!open || !workplace) return null;

  const init = {
    workplaceGroupId: String(workplace.workplaceGroupId ?? ''),
    name: workplace.name ?? '',
    code: workplace.code ?? '',
    address: workplace.address ?? '',
    description: workplace.description ?? '',
    // slice holds 'ACTIVE' | 'INACTIVE' | null → convert to boolean for the form
    status: (workplace.status ?? 'ACTIVE') === 'ACTIVE',
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Workplace" size="lg">
      <Formik
        initialValues={init}
        validationSchema={WpSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await dispatch<any>(
              updateWorkplaceById(workplace.id, {
                workplaceGroupId: Number(values.workplaceGroupId),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                address: values.address?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status, // ← boolean to API
              })
            );
            notifications.show({ color: 'green', title: 'Workplace updated', message: values.name });
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Update failed',
              message: e?.response?.data?.message ?? e?.message ?? 'Failed to update workplace',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            <SelectInput
              name="workplaceGroupId"
              required
              label="Workplace Group"
              value={values.workplaceGroupId}
              options={wpgOptions}
              placeholder="Select Workplace Group"
              isClearable={false}
              onValueChange={(v) => setFieldValue('workplaceGroupId', v)}
            />
            <ErrorMessage name="workplaceGroupId" component="div" className="text-xs text-red-600" />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <TextInput
              label="Address"
              value={values.address}
              onChange={(e) => setFieldValue('address', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Mantine Switch for status (boolean) */}
            <Switch
              label={values.status ? 'Active' : 'Inactive'}
              checked={!!values.status}
              onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
            />
            {touched.status && errors.status ? (
              <div className="text-red-600 text-sm">{String(errors.status)}</div>
            ) : null}

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

function DeleteWorkplaceModal({
  open,
  onClose,
  workplace,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  workplace: WorkplaceRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!open) setErr(null);
  }, [open]);

  if (!open || !workplace) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete workplace">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete <span className="font-medium">{workplace.name}</span>?
      </p>
      {!!err && <p className="mt-2 text-sm text-red-600 break-words">{err}</p>}
      <div className="mt-4 flex items-center justify-end gap-2">
        <Button onClick={onClose} variant="outline" color="gray" disabled={busy}>
          Cancel
        </Button>
        <Button
          color='red'
          onClick={async () => {
            try {
              setBusy(true);
              await dispatch<any>(deleteWorkplaceById(workplace.id));
              notifications.show({ color: 'green', title: 'Workplace deleted', message: workplace.name });
              onClose();
              onDone();
            } catch (e: any) {
              const msg = e?.response?.data?.message || e?.message || 'Failed to delete';
              setErr(msg);
              notifications.show({ color: 'red', title: 'Delete failed', message: msg });
            } finally {
              setBusy(false);
            }
          }}
          disabled={busy}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}

/* =======================================================
   WORKPLACE GROUP — Modals & schema
======================================================= */
const WpgSchema = Yup.object({
  businessUnitId: Yup.number()
    .typeError('Business Unit must be a number')
    .positive('Must be positive')
    .required('Business Unit is required'),
  name: Yup.string().min(1).max(100, 'Max 100 chars').required('Name is required'),
  code: Yup.string().max(30).nullable(),
  description: Yup.string().max(200).nullable(),
  status: Yup.boolean().required('Status is required'), // ← boolean now
});

/* =========================
   Create Workplace Group
========================= */
export function CreateWorkplaceGroupModal({
  open,
  onClose,
  onDone,
  buOptions,
}: {
  open: boolean;
  onClose: () => void;
  onDone: () => void;
  buOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  const init = {
    businessUnitId: '' as any,
    name: '',
    code: '',
    description: '',
    status: true, // default ACTIVE
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Workplace Group" size="lg">
      <Formik
        initialValues={init}
        validationSchema={WpgSchema}
        onSubmit={async (values, { setSubmitting, resetForm }) => {
          try {
            await dispatch<any>(
              createWorkplaceGroup({
                businessUnitId: Number(values.businessUnitId),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status, // boolean to API
              })
            );
            notifications.show({
              color: 'green',
              title: 'Workplace Group created',
              message: values.name,
            });
            resetForm();
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Create failed',
              message:
                e?.response?.data?.message ??
                e?.message ??
                'Failed to create workplace group',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            <SelectInput
              name="businessUnitId"
              required
              label="Business Unit"
              value={values.businessUnitId}
              options={buOptions}
              placeholder="Select Business Unit"
              isClearable={false}
              onValueChange={(v) => setFieldValue('businessUnitId', v)}
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Mantine Switch for status (boolean) */}
            <Switch
              label={values.status ? 'Active' : 'Inactive'}
              checked={!!values.status}
              onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
            />
            {touched.status && errors.status ? (
              <div className="text-red-600 text-sm">{String(errors.status)}</div>
            ) : null}

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

/* =========================
   Edit Workplace Group
========================= */
export function EditWorkplaceGroupModal({
  open,
  onClose,
  group,
  onDone,
  buOptions,
}: {
  open: boolean;
  onClose: () => void;
  group: WorkplaceGroupRow | null;
  onDone: () => void;
  buOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();
  if (!open || !group) return null;

  const init = {
    businessUnitId: String(group.businessUnitId ?? ''),
    name: group.name ?? '',
    code: group.code ?? '',
    description: group.description ?? '',
    // your slice holds 'ACTIVE' | 'INACTIVE' | null → convert to boolean for form
    status: (group.status ?? 'ACTIVE') === 'ACTIVE',
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Workplace Group" size="lg">
      <Formik
        initialValues={init}
        validationSchema={WpgSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await dispatch<any>(
              updateWorkplaceGroupById(group.id, {
                businessUnitId: Number(values.businessUnitId),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status, // boolean to API
              })
            );
            notifications.show({
              color: 'green',
              title: 'Workplace Group updated',
              message: values.name,
            });
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Update failed',
              message:
                e?.response?.data?.message ??
                e?.message ??
                'Failed to update workplace group',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            <SelectInput
              name="businessUnitId"
              required
              label="Business Unit"
              value={values.businessUnitId}
              options={buOptions}
              placeholder="Select Business Unit"
              isClearable={false}
              onValueChange={(v) => setFieldValue('businessUnitId', v)}
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Mantine Switch for status (boolean) */}
            <Switch
              label={values.status ? 'Active' : 'Inactive'}
              checked={!!values.status}
              onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
            />
            {touched.status && errors.status ? (
              <div className="text-red-600 text-sm">{String(errors.status)}</div>
            ) : null}

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save changes'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

function DeleteWorkplaceGroupModal({
  open,
  onClose,
  group,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  group: WorkplaceGroupRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!open) setErr(null);
  }, [open]);

  if (!open || !group) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete workplace group">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete <span className="font-medium">{group.name}</span>?
      </p>
      {!!err && <p className="mt-2 text-sm text-red-600 break-words">{err}</p>}
      <div className="mt-4 flex items-center justify-end gap-2">
        <Button onClick={onClose} variant="outline" color="gray" disabled={busy}>
          Cancel
        </Button>
        <Button
          color="red"
          onClick={async () => {
            try {
              setBusy(true);
              await dispatch<any>(deleteWorkplaceGroupById(group.id));
              notifications.show({ color: 'green', title: 'Workplace Group deleted', message: group.name });
              onClose();
              onDone();
            } catch (e: any) {
              const msg = e?.response?.data?.message || e?.message || 'Failed to delete';
              setErr(msg);
              notifications.show({ color: 'red', title: 'Delete failed', message: msg });
            } finally {
              setBusy(false);
            }
          }}
          disabled={busy}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}

/* =======================================================
   DEPARTMENT — Modals & schema
======================================================= */
/* ---------------- Schema ---------------- */
/** Keep workplaceId as a string in the form (UI), convert to Number() in submit */
const DeptSchema = Yup.object({
  workplaceId: Yup.string().required('Workplace is required'),
  name: Yup.string().min(1).max(100).required('Name is required'),
  code: Yup.string().max(30).nullable(),
  description: Yup.string().max(200).nullable(),
  /** boolean status as per your API */
  status: Yup.boolean().default(true),
});

/* ---------------- Create Modal ---------------- */
export function CreateDepartmentModal({
  open,
  onClose,
  onDone,
  workplaceOptions,
}: {
  open: boolean;
  onClose: () => void;
  onDone: () => void;
  workplaceOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  // normalize options to string values so they match the form’s string workplaceId
  const normalizedWorkplaceOptions = useMemo(
    () => (workplaceOptions ?? []).map((o) => ({ ...o, value: String(o.value) })),
    [workplaceOptions]
  );

  const init = {
    workplaceId: '', // string id in the form
    name: '',
    code: '',
    description: '',
    status: true,
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Department" size="lg">
      <Formik
        initialValues={init}
        validationSchema={DeptSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting, resetForm }) => {
          try {
            await dispatch<any>(
              createDepartment({
                WorkplaceId: Number(values.workplaceId), // convert to number for API
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status,
              })
            );
            notifications.show({
              color: 'green',
              title: 'Department created',
              message: values.name,
            });
            resetForm();
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Create failed',
              message:
                e?.response?.data?.message ?? e?.message ?? 'Failed to create department',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            <SelectInput
              name="workplaceId"
              required
              label="Workplace"
              value={
                (
                  normalizedWorkplaceOptions.find(
                    (o) => o.value === String(values.workplaceId)
                  )?.value ?? ''
                )
              }
              options={normalizedWorkplaceOptions}
              placeholder="Select Workplace"
              isClearable={false}
              onValueChange={(opt: any) =>
                setFieldValue('workplaceId', opt ? String(opt.value ?? opt) : '')
              }
            />
            <ErrorMessage name="workplaceId" component="div" className="text-xs text-red-600" />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            {/* Boolean status with Mantine Switch */}
            <div className="pt-2">
              <Switch
                label="Active"
                checked={values.status}
                onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
              />
            </div>

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button variant="outline" color="red" onClick={onClose} disabled={isSubmitting}>
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

/* -------- Edit Department -------- */
export function EditDepartmentModal({
  open,
  onClose,
  dept,
  onDone,
  workplaceOptions,
}: {
  open: boolean;
  onClose: () => void;
  dept: DepartmentRow | null;
  onDone: () => void;
  workplaceOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  // ✅ normalize options before early return
  const normalizedWorkplaceOptions = useMemo(
    () => (workplaceOptions ?? []).map((o) => ({ ...o, value: String(o.value) })),
    [workplaceOptions]
  );

  // ✅ prepare init safely (optional chaining avoids crashes)
  const init = useMemo(
    () => ({
      workplaceId: String(
        dept?.workplaceId ??
          (dept as any)?.WorkplaceId ??
          (dept as any)?.workplace?.id ??
          ''
      ),
      name: dept?.name ?? '',
      code: dept?.code ?? '',
      description: dept?.description ?? '',
      status: !!dept?.status,
    }),
    [dept]
  );

  // ✅ return after hooks
  if (!open || !dept) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Department" size="lg">
      <Formik
        key={String(dept.id)} // remount on row change
        initialValues={init}
        validationSchema={DeptSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await dispatch<any>(
              updateDepartmentById(dept.id, {
                WorkplaceId: Number(values.workplaceId),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status,
              })
            );

            notifications.show({
              color: 'green',
              title: 'Department updated',
              message: values.name,
            });

            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Update failed',
              message:
                e?.response?.data?.message ??
                e?.message ??
                'Failed to update department',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            {/* ✅ proper select binding */}
            <SelectInput
              name="workplaceId"
              required
              label="Workplace"
              value={
                (
                  normalizedWorkplaceOptions.find(
                    (o) => o.value === String(values.workplaceId)
                  )?.value ?? ''
                )
              }
              options={normalizedWorkplaceOptions}
              placeholder="Select Workplace"
              isClearable={false}
              onValueChange={(opt: any) =>
                setFieldValue(
                  'workplaceId',
                  opt ? String(opt.value ?? opt) : ''
                )
              }
            />
            <ErrorMessage
              name="workplaceId"
              component="div"
              className="text-xs text-red-600"
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={
                touched.name && errors.name ? String(errors.name) : undefined
              }
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) =>
                setFieldValue('description', e.currentTarget.value)
              }
            />

            {/* ✅ Mantine Switch for boolean status */}
            <div className="pt-2">
              <Switch
                label="Active"
                checked={values.status}
                onChange={(e) =>
                  setFieldValue('status', e.currentTarget.checked)
                }
              />
            </div>

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save changes'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}



/* ---------------- Delete Modal (unchanged) ---------------- */
function DeleteDepartmentModal({
  open,
  onClose,
  dept,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  dept: DepartmentRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!open) setErr(null);
  }, [open]);

  if (!open || !dept) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete department">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete{' '}
        <span className="font-medium">{dept.name}</span>?
      </p>
      {!!err && (
        <p className="mt-2 text-sm text-red-600 break-words">{err}</p>
      )}
      <div className="mt-4 flex items-center justify-end gap-2">
        <Button onClick={onClose} variant="outline" color="gray" disabled={busy}>
          Cancel
        </Button>
        <Button
          color="red"
          onClick={async () => {
            try {
              setBusy(true);
              await dispatch<any>(deleteDepartmentById((dept as any).id));
              notifications.show({
                color: 'green',
                title: 'Department deleted',
                message: dept.name,
              });
              onClose();
              onDone();
            } catch (e: any) {
              const msg =
                e?.response?.data?.message ||
                e?.message ||
                'Failed to delete';
              setErr(msg);
              notifications.show({
                color: 'red',
                title: 'Delete failed',
                message: msg,
              });
            } finally {
              setBusy(false);
            }
          }}
          disabled={busy}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}

/* =======================================================
   TEAM — Modals & schema
======================================================= */
/* ---------------- Schema ---------------- */
const TeamSchema = Yup.object({
  departmentId: Yup.string().required('Department is required'),
  name: Yup.string().min(1).max(100).required('Name is required'),
  code: Yup.string().max(30).nullable(),
  description: Yup.string().max(200).nullable(),
  status: Yup.boolean().required(),
});

/* ---------------- Create Modal ---------------- */
export function CreateTeamModal({
  open,
  onClose,
  onDone,
  departmentOptions,
}: {
  open: boolean;
  onClose: () => void;
  onDone: () => void;
  departmentOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  // normalize option values to strings (to match form state)
  const normalizedDeptOptions = useMemo(
    () => (departmentOptions ?? []).map(o => ({ ...o, value: String(o.value) })),
    [departmentOptions]
  );

  const init = {
    departmentId: '', // string id in form
    name: '',
    code: '',
    description: '',
    status: true,
  };

  return (
    <Modal isOpen={open} onClose={onClose} title="Create Team" size="lg">
      <Formik
        initialValues={init}
        validationSchema={TeamSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting, resetForm }) => {
          try {
            await dispatch<any>(
              createTeam({
                departmentId: Number(values.departmentId), // convert for API
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status,
              })
            );
            notifications.show({ color: 'green', title: 'Team created', message: values.name });
            resetForm();
            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Create failed',
              message: e?.response?.data?.message ?? e?.message ?? 'Failed to create team',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            <SelectInput
              name="departmentId"
              required
              label="Department"
              value={String(values.departmentId) || ''}
              options={normalizedDeptOptions}
              placeholder="Select Department"
              isClearable={false}
              onValueChange={(opt: any) =>
                setFieldValue('departmentId', opt ? String(opt.value ?? opt) : '')
              }
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={touched.name && errors.name ? String(errors.name) : undefined}
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) => setFieldValue('description', e.currentTarget.value)}
            />

            <Group justify="space-between" mt="md">
              <Switch
                label="Status"
                checked={values.status}
                onChange={(e) => setFieldValue('status', e.currentTarget.checked)}
              />
            </Group>

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button variant="outline" type="button" color="red" onClick={onClose} disabled={isSubmitting}>
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}

/* ---------------- Edit Modal ---------------- */
/* -------- Edit Team -------- */
export function EditTeamModal({
  open,
  onClose,
  team,
  onDone,
  departmentOptions,
}: {
  open: boolean;
  onClose: () => void;
  team: any; // or TeamRow type
  onDone: () => void;
  departmentOptions: SelectOption[];
}) {
  const dispatch = useAppDispatch();

  // ✅ Hooks must come before conditional return
  const normalizedDeptOptions = useMemo(
    () => (departmentOptions ?? []).map((o) => ({ ...o, value: String(o.value) })),
    [departmentOptions]
  );

  // ✅ useMemo for init (safe even if team = null)
  const init = useMemo(
    () => ({
      departmentId: String(
        team?.departmentId ??
          (team as any)?.DepartmentId ??
          (team as any)?.department?.id ??
          ''
      ),
      name: team?.name ?? '',
      code: team?.code ?? '',
      description: team?.description ?? '',
      status: !!team?.status,
    }),
    [team]
  );

  // ✅ return after hooks
  if (!open || !team) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Edit Team" size="lg">
      <Formik
        key={String(team.id)} // remount when switching rows
        initialValues={init}
        validationSchema={TeamSchema}
        enableReinitialize
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await dispatch<any>(
              updateTeamById(team.id, {
                departmentId: Number(values.departmentId),
                name: values.name.trim(),
                code: values.code?.trim() || null,
                description: values.description?.trim() || null,
                status: !!values.status,
              })
            );

            notifications.show({
              color: 'green',
              title: 'Team updated',
              message: values.name,
            });

            onClose();
            onDone();
          } catch (e: any) {
            notifications.show({
              color: 'red',
              title: 'Update failed',
              message:
                e?.response?.data?.message ??
                e?.message ??
                'Failed to update team',
            });
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ values, setFieldValue, isSubmitting, errors, touched }) => (
          <Form className="space-y-4">
            {/* ✅ proper controlled SelectInput */}
            <SelectInput
              name="departmentId"
              required
              label="Department"
              value={
                (
                  normalizedDeptOptions.find(
                    (o) => o.value === String(values.departmentId)
                  )?.value ?? ''
                )
              }
              options={normalizedDeptOptions}
              placeholder="Select Department"
              isClearable={false}
              onValueChange={(opt: any) =>
                setFieldValue(
                  'departmentId',
                  opt ? String(opt.value ?? opt) : ''
                )
              }
            />

            <TextInput
              label="Name"
              required
              value={values.name}
              onChange={(e) => setFieldValue('name', e.currentTarget.value)}
              error={
                touched.name && errors.name ? String(errors.name) : undefined
              }
            />

            <TextInput
              label="Code"
              value={values.code}
              onChange={(e) => setFieldValue('code', e.currentTarget.value)}
            />

            <Textarea
              label="Description"
              minRows={2}
              value={values.description}
              onChange={(e) =>
                setFieldValue('description', e.currentTarget.value)
              }
            />

            <Group justify="space-between" mt="md">
              <Switch
                label="Status"
                checked={values.status}
                onChange={(e) =>
                  setFieldValue('status', e.currentTarget.checked)
                }
              />
            </Group>

            <div className="w-full flex justify-end gap-2 pt-2">
              <Button
                variant="outline"
                type="button"
                color="red"
                onClick={onClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>

              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving…' : 'Save changes'}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </Modal>
  );
}



/* ---------------- Delete Modal (unchanged) ---------------- */
function DeleteTeamModal({
  open,
  onClose,
  team,
  onDone,
}: {
  open: boolean;
  onClose: () => void;
  team: TeamRow | null;
  onDone: () => void;
}) {
  const dispatch = useAppDispatch();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!open) setErr(null);
  }, [open]);

  if (!open || !team) return null;

  return (
    <Modal isOpen={open} onClose={onClose} title="Delete team">
      <p className="text-sm text-gray-700">
        Are you sure you want to delete{' '}
        <span className="font-medium">{team.name}</span>?
      </p>
      {!!err && (
        <p className="mt-2 text-sm text-red-600 break-words">{err}</p>
      )}
      <div className="mt-4 flex items-center justify-end gap-2">
        <Button onClick={onClose} variant="outline" color="gray" disabled={busy}>
          Cancel
        </Button>
        <Button
          color="red"
          onClick={async () => {
            try {
              setBusy(true);
              await dispatch<any>(deleteTeamById((team as any).id));
              notifications.show({
                color: 'green',
                title: 'Team deleted',
                message: team.name,
              });
              onClose();
              onDone();
            } catch (e: any) {
              const msg =
                e?.response?.data?.message ||
                e?.message ||
                'Failed to delete';
              setErr(msg);
              notifications.show({
                color: 'red',
                title: 'Delete failed',
                message: msg,
              });
            } finally {
              setBusy(false);
            }
          }}
          disabled={busy}
        >
          {busy ? 'Deleting…' : 'Delete'}
        </Button>
      </div>
    </Modal>
  );
}
/* ---------------------------------- Page ---------------------------------- */
export default function CompanyConfigPage() {
  const dispatch = useAppDispatch();

  const {  pagination, loadingList } = useSelector(selectCompaniesState);
  const { lastPage } = pagination;

  /* Companies */
  const { items, error } = useSelector(selectCompaniesState);

  /* Business Units */
  const buState = useSelector((s: RootState) => s.businessUnit);
  const { list: buItems, error: buError } = buState;

  /* Workplaces */
  const wpState = useSelector((s: RootState) => s.workplaces);
  const wpItems = useMemo(()=>(wpState?.list ?? []) as WorkplaceRow[],[wpState]);
  const wpError = wpState?.error ?? null;

  /* Workplace Groups */
  const wpgState = useSelector((s: RootState) => s.workplaceGroup);
  const wpgItems = useMemo(()=>(wpgState?.list ?? []) as WorkplaceGroupRow[],[wpgState]);
  const wpgError = wpgState?.error ?? null;

  /* Departments */
  const deptState = useSelector((s: RootState) => s.departments);
  const deptItems = useMemo(()=>(deptState?.list ?? []) as DepartmentRow[],[deptState]);
  const deptError = deptState?.error ?? null;

  /* Teams */
  const teamState = useSelector((s: RootState) => (s as any).teams || (s as any).team);
  const teamItems = (teamState?.list ?? []) as TeamRow[];
  const teamError = teamState?.error ?? null;

  // server pagination (company)
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);

  // simple pagers
  const [buPage] = useState(1);
  const [buSize] = useState(10);
  const [wpPage] = useState(1);
  const [wpSize] = useState(10);
  const [wpgPage] = useState(1);
  const [wpgSize] = useState(10);
  const [deptPage] = useState(1);
  const [deptSize] = useState(10);
  const [teamPage] = useState(1);
  const [teamSize] = useState(10);

  // company modals
  const [openCreate, setOpenCreate] = useState(false);
  const [openEdit, setOpenEdit] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [current, setCurrent] = useState<CompanyRow | null>(null);

  // business unit modals
  const [openBuCreate, setOpenBuCreate] = useState(false);
  const [openBuEdit, setOpenBuEdit] = useState(false);
  const [openBuDelete, setOpenBuDelete] = useState(false);
  const [currentBu, setCurrentBu] = useState<BusinessUnitRow | null>(null);

  // workplace modals
  const [openWpCreate, setOpenWpCreate] = useState(false);
  const [openWpEdit, setOpenWpEdit] = useState(false);
  const [openWpDelete, setOpenWpDelete] = useState(false);
  const [currentWp, setCurrentWp] = useState<WorkplaceRow | null>(null);

  // workplace group modals
  const [openWpgCreate, setOpenWpgCreate] = useState(false);
  const [openWpgEdit, setOpenWpgEdit] = useState(false);
  const [openWpgDelete, setOpenWpgDelete] = useState(false);
  const [currentWpg, setCurrentWpg] = useState<WorkplaceGroupRow | null>(null);

  // department modals
  const [openDeptCreate, setOpenDeptCreate] = useState(false);
  const [openDeptEdit, setOpenDeptEdit] = useState(false);
  const [openDeptDelete, setOpenDeptDelete] = useState(false);
  const [currentDept, setCurrentDept] = useState<DepartmentRow | null>(null);

  // team modals
  const [openTeamCreate, setOpenTeamCreate] = useState(false);
  const [openTeamEdit, setOpenTeamEdit] = useState(false);
  const [openTeamDelete, setOpenTeamDelete] = useState(false);
  const [currentTeam, setCurrentTeam] = useState<TeamRow | null>(null);

  const refetchCompanies = (p = page, s = size) => dispatch(fetchCompanies({ page: p, size: s }));
  const refetchBusinessUnits = (p = buPage, s = buSize) => dispatch<any>(fetchBusinessUnits(p, s));
  const refetchWorkplaces = (p = wpPage, s = wpSize) => dispatch<any>(fetchWorkplaces(p, s));
  const refetchWorkplaceGroups = (p = wpgPage, s = wpgSize) => dispatch<any>(fetchWorkplaceGroups(p, s));
  const refetchDepartments = (p = deptPage, s = deptSize) => dispatch<any>(fetchDepartments(p, s));
  const refetchTeams = (p = teamPage, s = teamSize) => dispatch<any>(fetchTeams(p, s));


  useEffect(() => {
    refetchCompanies(1, size);
    refetchBusinessUnits(1, buSize);
    refetchWorkplaces(1, wpSize);
    refetchWorkplaceGroups(1, wpgSize);
    refetchDepartments(1, deptSize);
    refetchTeams(1, teamSize);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    refetchCompanies(page, size);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, size]);

  const companyOptionsForBU: SelectOption[] = useMemo(
    () => (items || []).map((c) => ({ value: c.id as any, label: c.name || `#${c.id}` })),
    [items]
  );

  const buOptionsForGroup: SelectOption[] = useMemo(
    () =>
      (buItems || []).map((b) => ({
        value: b.id as any,
        label: b.name ? `${b.name}${b.companyName ? ` — ${b.companyName}` : ''}` : `#${b.id}`,
      })),
    [buItems]
  );

  const workplaceOptionsForDept: SelectOption[] = useMemo(
    () =>
      (wpItems || []).map((w) => ({
        value: w.id as any,
        label: w.name ? `${w.name}${w.code ? ` (${w.code})` : ''}` : `#${w.id}`,
      })),
    [wpItems]
  );

  // ★ NEW: Workplace Group options for Workplace modals (with BU name)
  const wpgOptionsForWp: SelectOption[] = useMemo(
    () =>
      (wpgItems || []).map((g) => {
        const bu = buItems.find((b: { id: any; }) => String(b.id) === String(g.businessUnitId));
        return {
          value: g.id as any,
          label: g.name ? `${g.name}${bu?.name ? ` — ${bu.name}` : ''}` : `#${g.id}`,
        };
      }),
    [wpgItems, buItems]
  );

  // Team Part
  const departmentOptionsForTeam: SelectOption[] = useMemo(
    () =>
      (deptItems || []).map((d) => {
        const wid = (d as any).workplaceId ?? (d as any).WorkplaceId;
        const w = wpItems.find((x) => String(x.id) === String(wid));
        return {
          value: d.id as any,
          label: d.name ? `${d.name}${w?.name ? ` — ${w.name}` : ''}` : `#${d.id}`,
        };
      }),
    [deptItems, wpItems]
  );


  /* --------------------- Companies table --------------------- */
  const companyColumns: ColumnDef<CompanyRow, any>[] = useMemo(
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
      // { accessorKey: 'id', header: 'ID' },
      {
        accessorKey: 'name',
        header: 'Company',
        cell: ({ row }) => {
          const c = row.original;
          return (
            <div className="flex items-center gap-3">
              {c.logoUrl ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={c.logoUrl} alt={c.name} className="h-8 w-8 rounded-full object-cover border" />
              ) : (
                <div className="h-8 w-8 flex items-center justify-center rounded-full bg-gray-200 text-gray-600 font-semibold">
                  {c.name?.charAt(0) ?? '—'}
                </div>
              )}
              <div className="flex flex-col">
                <span className="font-medium text-gray-800">{c.name || '—'}</span>
              </div>
            </div>
          );
        },
      },
      {
        accessorKey: 'shortName',
        header: 'Short Name',
        cell: ({ row }) => {
          const c = row.original;
          return (
            <div className="flex items-center gap-3">
              <div className="flex flex-col">
                {c.shortName ? <span className="text-xs text-gray-500">{c.shortName}</span> : null}
              </div>
            </div>
          );
        },
      },
      { accessorKey: 'code', header: 'Code' },
      { accessorKey: 'phone', header: 'Phone' },
      { accessorKey: 'email', header: 'Email' },
      {
        accessorKey: 'websiteUrl',
        header: 'Website',
        cell: ({ getValue }) => {
          const url = getValue<string>();
          if (!url) return '—';
          return (
            <a href={url} target="_blank" rel="noreferrer" className="text-blue-600 hover:underline">
              {url}
            </a>
          );
        },
      },
      { accessorKey: 'currency', header: 'Currency' },
      { accessorKey: 'timeZone', header: 'TimeZone' },
      { accessorKey: 'address', header: 'Address' },
      {
        id: 'status',
        header: 'Status',
        accessorFn: (row) => (row.status ? 'ACTIVE' : 'INACTIVE'),
        cell: ({ getValue }) => {
          const s = getValue() as string;
          const cls =
            s === 'ACTIVE'
              ? 'text-green-700 bg-green-50'
              : 'text-red-700 bg-red-50';
          return (
            <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>
              {s}
            </span>
          );
        },
      },

      {
        id: 'actions',
        header: 'Actions',
        enableSorting: false,
        enableHiding: false,
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setCurrent(row.original as CompanyRow);
                  setOpenEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  setCurrent(row.original as CompanyRow);
                  setOpenDelete(true);
                },
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
  
  const companySection = {
    id: 'company',
    title: 'Company',
    content: (
      <div className="bg-white rounded-lg">
        <DataTable<CompanyRow>
          data={items}
          columns={companyColumns}
          searchPlaceholder="Search companies..."
          csvFileName="companies"
          initialColumnVisibility={{}}
          onPressAdd={() => setOpenCreate(true)}
          // headerLeft={<Breadcrumbs />}
          manualPagination
          pagination={{
            pageIndex: page - 1,
            pageSize: size,
          }}
          totalRows={pagination?.total ?? items.length}
          pageCount={lastPage} // if your DataTable supports pageCount
          onPaginationChange={({ pageIndex, pageSize }) => {
            setPage(pageIndex + 1);
            setSize(pageSize);
          }}
          loading={loadingList}
        />
        {error && <p className="mt-3 text-sm text-red-600">{error}</p>}
      </div>
    ),
  };

  /* --------------------- Business Units table --------------------- */
  const buColumns: ColumnDef<BusinessUnitRow, any>[] = useMemo(
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
      // { accessorKey: 'id', header: 'ID' },
      {
        accessorKey: 'companyName',
        header: 'Company',
        cell: ({ row }) => row.original.companyName ?? `#${row.original.companyId ?? '—'}`,
      },
      { accessorKey: 'name', header: 'Business Unit' },
      { accessorKey: 'code', header: 'Code' },
      {
        accessorKey: 'description',
        header: 'Description',
        cell: ({ getValue }) => {
          const value = String(getValue() ?? '—');
          return value.length > 25 ? value.slice(0, 25) + '…' : value;
        },
      },
      {
        id: 'status',
        header: 'Status',
        accessorFn: (row) => String(row.status ?? 'ACTIVE').toUpperCase(),
        cell: ({ getValue }) => {
          const s = String(getValue());
          const cls = s === 'ACTIVE' ? 'text-green-700 bg-green-50' : 'text-red-700 bg-red-50';
          return <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>{s}</span>;
        },
      },
      {
        id: 'actions',
        header: 'Actions',
        enableSorting: false,
        enableHiding: false,
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setCurrentBu(row.original as BusinessUnitRow);
                  setOpenBuEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  setCurrentBu(row.original as BusinessUnitRow);
                  setOpenBuDelete(true);
                },
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

  const businessUnitSection = {
    id: 'businessUnit',
    title: 'Business Unit',
    content: (
      <div className="bg-white rounded-lg">
        <DataTable<BusinessUnitRow>
          data={buItems}
          columns={buColumns}
          searchPlaceholder="Search business units..."
          csvFileName="business-units"
          initialColumnVisibility={{}}
          onPressAdd={() => setOpenBuCreate(true)}
          // headerLeft={<Breadcrumbs />}
          manualPagination
          pagination={{
            pageIndex: (buState.page?.currentPage ?? 1) - 1,
            pageSize: buState.page?.perPage ?? 10,
          }}
          pageCount={buState.page?.lastPage ?? 1}
          totalRows={buState.page?.total ?? buItems.length}
          onPaginationChange={({ pageIndex, pageSize }) => {
            const newPage = pageIndex + 1;
            const newSize = pageSize;
            dispatch<any>(fetchBusinessUnits(newPage, newSize));
          }}
          loading={buState.loading}
        />
        {buError && <p className="mt-3 text-sm text-red-600">{buError}</p>}
      </div>
    ),
  };

  /* --------------------- Workplace Groups table --------------------- */
  const wpgColumns: ColumnDef<WorkplaceGroupRow, any>[] = useMemo(
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
      // { accessorKey: 'id', header: 'ID' },
      {
        id: 'businessUnitName',
        header: 'Business Unit',
        accessorFn: (row) => {
          const bu = buItems.find((b) => String(b.id) === String(row.businessUnitId));
          return bu?.name ?? `#${row.businessUnitId ?? '—'}`;
        },
        cell: ({ getValue }) => String(getValue() ?? '—'),
      },
      { accessorKey: 'name', header: 'Group Name' },
      { accessorKey: 'code', header: 'Code' },
      {
        id: 'status',
        header: 'Status',
        accessorFn: (row) => String(row.status ?? 'ACTIVE').toUpperCase(),
        cell: ({ getValue }) => {
          const s = String(getValue());
          const cls = s === 'ACTIVE' ? 'text-green-700 bg-green-50' : 'text-red-700 bg-red-50';
          return <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>{s}</span>;
        },
      },
      { accessorKey: 'description', header: 'Description' },
      {
        id: 'actions',
        header: 'Actions',
        enableSorting: false,
        enableHiding: false,
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setCurrentWpg(row.original as WorkplaceGroupRow);
                  setOpenWpgEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  setCurrentWpg(row.original as WorkplaceGroupRow);
                  setOpenWpgDelete(true);
                },
              },
            ]}
          >
            <BsThreeDots />
          </RowActionDropdown>
        ),
      },
    ],
    [buItems]
  );

  const workplaceGroupSection = {
    id: 'workplaceGroup',
    title: 'Workplace Group',
    content: (
      <div className="bg-white rounded-lg">
        <DataTable<WorkplaceGroupRow>
          data={wpgItems}
          columns={wpgColumns}
          searchPlaceholder="Search workplace groups..."
          csvFileName="workplace-groups"
          initialColumnVisibility={{}}
          onPressAdd={() => setOpenWpgCreate(true)}
          // headerLeft={<Breadcrumbs />}
          manualPagination
          pagination={{
            pageIndex: (wpgState.page?.currentPage ?? 1) - 1,
            pageSize: wpgState.page?.perPage ?? 10, // ✅ using backend’s perPage
          }}
          pageCount={wpgState.page?.lastPage ?? 1}
          totalRows={wpgState.page?.total ?? wpgItems.length}
          onPaginationChange={({ pageIndex, pageSize }) => {
            const newPage = pageIndex + 1;
            dispatch<any>(fetchWorkplaceGroups(newPage, pageSize));
          }}
          loading={wpgState.loading}
        />
        {wpgError && <p className="mt-3 text-sm text-red-600">{wpgError}</p>}
      </div>
    ),
  };

  /* --------------------- Workplaces table --------------------- */
  const wpColumns: ColumnDef<WorkplaceRow, any>[] = useMemo(
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
      // { accessorKey: 'id', header: 'ID' },
      {
        id: 'workplaceGroup',
        header: 'Workplace Group',
        accessorFn: (row: any) => row.workplaceGroup?.name ?? '—',
        cell: ({ getValue }) => {
          const value = String(getValue() ?? '—');
          return value.length > 40 ? value.slice(0, 25) + '…' : value;
        },
      },
      // { accessorKey: 'name', header: 'Workplace' },
      {
        accessorKey: 'name',
        header: 'Workplace',
        cell: ({ getValue }) => {
          const value = String(getValue() ?? '—');
          return value.length > 25 ? value.slice(0, 25) + '…' : value;
        },
      },
      { accessorKey: 'code', header: 'Code' },
      { accessorKey: 'address', header: 'Address' },
      {
        id: 'status',
        header: 'Status',
        accessorFn: (row) => String(row.status ?? 'ACTIVE').toUpperCase(),
        cell: ({ getValue }) => {
          const s = String(getValue());
          const cls = s === 'ACTIVE' ? 'text-green-700 bg-green-50' : 'text-red-700 bg-red-50';
          return <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>{s}</span>;
        },
      },
      {
        id: 'actions',
        header: 'Actions',
        enableSorting: false,
        enableHiding: false,
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setCurrentWp(row.original as WorkplaceRow);
                  setOpenWpEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  setCurrentWp(row.original as WorkplaceRow);
                  setOpenWpDelete(true);
                },
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

  const workplaceSection = {
    id: 'workplace',
    title: 'Workplace',
    content: (
      <div className="bg-white rounded-lg">
        <DataTable<WorkplaceRow>
          data={wpItems}
          columns={wpColumns}
          searchPlaceholder="Search workplaces..."
          csvFileName="workplaces"
          initialColumnVisibility={{}}
          enableRowSelection
          onPressAdd={() => setOpenWpCreate(true)}
          // headerLeft={<Breadcrumbs />}
          manualPagination
          pagination={{
            pageIndex: (wpState.page?.currentPage ?? 1) - 1,
            pageSize: wpState.page?.perPage ?? 10,
          }}
          pageCount={wpState.page?.lastPage ?? 1}
          totalRows={wpState.page?.total ?? wpItems.length}
          onPaginationChange={({ pageIndex, pageSize }) => {
            const newPage = pageIndex + 1;
            const newSize = pageSize;
            dispatch<any>(fetchWorkplaces(newPage, newSize));
          }}
          loading={wpState.loading}
        />
        {wpError && <p className="mt-3 text-sm text-red-600">{wpError}</p>}
      </div>
    ),
  };

  /* --------------------- Departments table --------------------- */
  const deptColumns: ColumnDef<DepartmentRow, any>[] = useMemo(
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
      // { accessorKey: 'id', header: 'ID' },
      {
        id: 'workplaceName',
        header: 'Workplace',
        accessorFn: (row: any) => row.workplace?.name ?? '—',
        cell: ({ getValue }) => String(getValue() ?? '—'),
      },
      { accessorKey: 'name', header: 'Department' },
      { accessorKey: 'code', header: 'Code' },
      // {
      //   id: 'status',
      //   header: 'Status',
      //   accessorFn: (row) => String((row as any).status ?? 'ACTIVE').toUpperCase(),
      //   cell: ({ getValue }) => {
      //     const s = String(getValue());
      //     const cls = s === 'ACTIVE' ? 'text-green-700 bg-green-50' : 'text-red-700 bg-red-50';
      //     return <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>{s}</span>;
      //   },
      // },
      {
        id: 'status',
        header: 'Status',
        accessorFn: (row) => (row.status ? 'ACTIVE' : 'INACTIVE'),
        cell: ({ getValue }) => {
          const s = getValue() as string;
          const cls =
            s === 'ACTIVE'
              ? 'text-green-700 bg-green-50'
              : 'text-red-700 bg-red-50';
          return (
            <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>
              {s}
            </span>
          );
        },
      },
      { accessorKey: 'description', header: 'Description' },
      {
        id: 'actions',
        header: 'Actions',
        enableSorting: false,
        enableHiding: false,
        cell: ({ row }) => (
          <RowActionDropdown
            data={[
              {
                label: 'Edit',
                icon: <PencilSquareIcon height={16} />,
                action: () => {
                  setCurrentDept(row.original as DepartmentRow);
                  setOpenDeptEdit(true);
                },
              },
              {
                label: 'Delete',
                icon: <TrashIcon height={16} />,
                action: () => {
                  setCurrentDept(row.original as DepartmentRow);
                  setOpenDeptDelete(true);
                },
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

  const departmentSection = {
    id: 'department',
    title: 'Department',
    content: (
      <div className="bg-white rounded-lg">
        <DataTable<DepartmentRow>
          data={deptItems}
          columns={deptColumns}
          searchPlaceholder="Search departments..."
          csvFileName="departments"
          initialColumnVisibility={{}}
          enableRowSelection
          onPressAdd={() => setOpenDeptCreate(true)}
          // headerLeft={<Breadcrumbs />}
          manualPagination
          pagination={{
            pageIndex: (deptState.page?.currentPage ?? 1) - 1,
            pageSize: deptState.page?.perPage ?? 10,
          }}
          pageCount={deptState.page?.lastPage ?? 1}
          totalRows={deptState.page?.total ?? deptItems.length}
          onPaginationChange={({ pageIndex, pageSize }) => {
            const newPage = pageIndex + 1;
            const newSize = pageSize;
            dispatch<any>(fetchDepartments(newPage, newSize));
          }}
          loading={deptState.loading}
        />
        {deptError && <p className="mt-3 text-sm text-red-600">{deptError}</p>}
      </div>
    ),
  };

  // -------------------- Team Table --------------------
  const teamColumns: ColumnDef<TeamRow, any>[] = useMemo(
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
    // { accessorKey: 'id', header: 'ID' },
    {
      id: 'departmentName',
      header: 'Department',
      accessorFn: (row: any) => row.department?.name ?? '—',
      cell: ({ getValue }) => String(getValue() ?? '—'),
    },
    { accessorKey: 'name', header: 'Team' },
    { accessorKey: 'code', header: 'Code' },
    // {
    //   id: 'status',
    //   header: 'Status',
    //   accessorFn: (row) => String((row as any).status ?? 'ACTIVE').toUpperCase(),
    //   cell: ({ getValue }) => {
    //     const s = String(getValue());
    //     const cls = s === 'ACTIVE' ? 'text-green-700 bg-green-50' : 'text-red-700 bg-red-50';
    //     return <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>{s}</span>;
    //   },
    // },
    {
      id: 'status',
      header: 'Status',
      accessorFn: (row) => (row.status ? 'ACTIVE' : 'INACTIVE'),
      cell: ({ getValue }) => {
        const s = getValue() as string;
        const cls =
          s === 'ACTIVE'
            ? 'text-green-700 bg-green-50'
            : 'text-red-700 bg-red-50';
        return (
          <span className={`px-2 py-0.5 rounded text-xs font-medium ${cls}`}>
            {s}
          </span>
        );
      },
    },
    { accessorKey: 'description', header: 'Description' },
    {
      id: 'actions',
      header: 'Actions',
      enableSorting: false,
      enableHiding: false,
      cell: ({ row }) => (
        <RowActionDropdown
          data={[
            {
              label: 'Edit',
              icon: <PencilSquareIcon height={16} />,
              action: () => {
                setCurrentTeam(row.original as TeamRow);
                setOpenTeamEdit(true);
              },
            },
            {
              label: 'Delete',
              icon: <TrashIcon height={16} />,
              action: () => {
                setCurrentTeam(row.original as TeamRow);
                setOpenTeamDelete(true);
              },
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

const teamSection = {
  id: 'team',
  title: 'Team',
  content: (
    <div className="bg-white rounded-lg">
      <DataTable<TeamRow>
        data={teamItems}
        columns={teamColumns}
        searchPlaceholder="Search teams..."
        csvFileName="teams"
        initialColumnVisibility={{}}
        enableRowSelection
        onPressAdd={() => setOpenTeamCreate(true)}
        // headerLeft={<Breadcrumbs />}
        manualPagination
        pagination={{
          pageIndex: (teamState.page?.currentPage ?? 1) - 1,
          pageSize: teamState.page?.perPage ?? 10,
        }}
        pageCount={teamState.page?.lastPage ?? 1}
        totalRows={teamState.page?.total ?? teamItems.length}
        onPaginationChange={({ pageIndex, pageSize }) => {
          const newPage = pageIndex + 1;
          const newSize = pageSize;
          dispatch<any>(fetchTeams(newPage, newSize));
        }}
        loading={teamState.loading}
      />
      {teamError && <p className="mt-3 text-sm text-red-600">{teamError}</p>}
    </div>
  ),
};


  return (
    <>
      <ConfigPageLayout
        title="Company Configuration"
        sections={[companySection, businessUnitSection, workplaceGroupSection, workplaceSection, departmentSection, teamSection]}
      />

      {/* Company Create / Edit / Delete */}
      <CreateCompanyModal open={openCreate} onClose={() => setOpenCreate(false)} onDone={() => refetchCompanies(page, size)} />
      <EditCompanyModal open={openEdit} onClose={() => setOpenEdit(false)} company={current} onDone={() => refetchCompanies(page, size)} />
      <DeleteCompanyModal open={openDelete} onClose={() => setOpenDelete(false)} company={current} onDone={() => refetchCompanies(page, size)} />

      {/* Business Unit Create / Edit / Delete */}
      <CreateBusinessUnitModal
        open={openBuCreate}
        onClose={() => setOpenBuCreate(false)}
        onDone={() => refetchBusinessUnits(buPage, buSize)}
        companyOptions={companyOptionsForBU}
      />
      <EditBusinessUnitModal
        open={openBuEdit}
        onClose={() => setOpenBuEdit(false)}
        unit={currentBu}
        onDone={() => refetchBusinessUnits(buPage, buSize)}
        companyOptions={companyOptionsForBU}
      />
      <DeleteBusinessUnitModal open={openBuDelete} onClose={() => setOpenBuDelete(false)} unit={currentBu} onDone={() => refetchBusinessUnits(buPage, buSize)} />

      {/* Workplace Group Create / Edit / Delete */}
      <CreateWorkplaceGroupModal
        open={openWpgCreate}
        onClose={() => setOpenWpgCreate(false)}
        onDone={() => refetchWorkplaceGroups(wpgPage, wpgSize)}
        buOptions={buOptionsForGroup}
      />
      <EditWorkplaceGroupModal
        open={openWpgEdit}
        onClose={() => setOpenWpgEdit(false)}
        group={currentWpg}
        onDone={() => refetchWorkplaceGroups(wpgPage, wpgSize)}
        buOptions={buOptionsForGroup}
      />
      <DeleteWorkplaceGroupModal open={openWpgDelete} onClose={() => setOpenWpgDelete(false)} group={currentWpg} onDone={() => refetchWorkplaceGroups(wpgPage, wpgSize)} />

      {/* Workplace Create / Edit / Delete */}
      {/* ★ UPDATED: pass wpgOptionsForWp into both modals */}
      <CreateWorkplaceModal
        open={openWpCreate}
        onClose={() => setOpenWpCreate(false)}
        onDone={() => refetchWorkplaces(wpPage, wpSize)}
        wpgOptions={wpgOptionsForWp}
      />
      <EditWorkplaceModal
        open={openWpEdit}
        onClose={() => setOpenWpEdit(false)}
        workplace={currentWp}
        onDone={() => refetchWorkplaces(wpPage, wpSize)}
        wpgOptions={wpgOptionsForWp}
      />
      <DeleteWorkplaceModal open={openWpDelete} onClose={() => setOpenWpDelete(false)} workplace={currentWp} onDone={() => refetchWorkplaces(wpPage, wpSize)} />

      {/* Department Create / Edit / Delete */}
      <CreateDepartmentModal
        open={openDeptCreate}
        onClose={() => setOpenDeptCreate(false)}
        onDone={() => refetchDepartments(deptPage, deptSize)}
        workplaceOptions={workplaceOptionsForDept}
      />
      <EditDepartmentModal
        open={openDeptEdit}
        onClose={() => setOpenDeptEdit(false)}
        dept={currentDept}
        onDone={() => refetchDepartments(deptPage, deptSize)}
        workplaceOptions={workplaceOptionsForDept}
      />
      <DeleteDepartmentModal
        open={openDeptDelete}
        onClose={() => setOpenDeptDelete(false)}
        dept={currentDept}
        onDone={() => refetchDepartments(deptPage, deptSize)}
      />

      {/* Team Create / Edit / Delete */}
      <CreateTeamModal
        open={openTeamCreate}
        onClose={() => setOpenTeamCreate(false)}
        onDone={() => refetchTeams(teamPage, teamSize)}
        departmentOptions={departmentOptionsForTeam}
      />
      <EditTeamModal
        open={openTeamEdit}
        onClose={() => setOpenTeamEdit(false)}
        team={currentTeam}
        onDone={() => refetchTeams(teamPage, teamSize)}
        departmentOptions={departmentOptionsForTeam}
      />
      <DeleteTeamModal
        open={openTeamDelete}
        onClose={() => setOpenTeamDelete(false)}
        team={currentTeam}
        onDone={() => refetchTeams(teamPage, teamSize)}
      />

    </>
  );
}