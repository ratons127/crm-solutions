import SubmitSection from '@/components/common/form/SubmitSection';
import { useGetCompanyListQuery } from '@/lib/features/employment/employmentGroupAPI';
import { LeaveGroupAssign, LeaveGroupAssignCreate } from '@/lib/types/leave';
import { showApiNotification } from '@/lib/utils/showNotification';
import { useGetLeaveGroupListQuery } from '@/services/api/leave/leaveGroupAPI';
import {
  useCreateLeaveGroupAssignMutation,
  useUpdateLeaveGroupAssignMutation,
} from '@/services/api/leave/leaveGroupAssignAPI';
import { useGetLeaveTypeListQuery } from '@/services/api/leave/leaveTypeAPI';
import { useGetOrganizationStructureListQuery } from '@/services/api/organization/organizationStructureAPI';

import { Select, Switch, Textarea } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useFormik } from 'formik';
import * as Yup from 'yup';

type Props = {
  action: 'create' | 'update' | null;
  data: LeaveGroupAssign | null;
  onClose: () => void;
};

const validationSchema = Yup.object().shape({
  leaveTypeId: Yup.mixed<string | number>().required('Leave type is required'),
  leaveGroupId: Yup.mixed<string | number>().required(
    'Leave group is required'
  ),
  companyId: Yup.mixed<string | number>().required('Company is required'),
  // businessUnitId: Yup.mixed<string | number>().required(
  //   'Business unit is required'
  // ),
  // workPlaceGroupId: Yup.mixed<string | number>().required(
  //   'Workplace group is required'
  // ),
  // workPlaceId: Yup.mixed<string | number>().required('Workplace is required'),
  // departmentId: Yup.mixed<string | number>(),
  // teamId: Yup.mixed<string | number>(),
  description: Yup.string()
    // .required('Description is required')
    .min(3, 'Description must be at least 3 characters')
    .max(500, 'Description must not exceed 500 characters')
    .trim(),
  status: Yup.boolean().required('Status is required'),
});

export default function LeaveGroupAssignForm({ action, data, onClose }: Props) {
  const resourceName = 'Leave Category';

  const { data: leaveTypeList } = useGetLeaveTypeListQuery({});
  const { data: leaveGroupList } = useGetLeaveGroupListQuery({});
  const { data: companyList } = useGetCompanyListQuery(undefined);

  const [create, { isLoading }] = useCreateLeaveGroupAssignMutation();
  const [update, { isLoading: isLoadingUpdate }] =
    useUpdateLeaveGroupAssignMutation();

  const handleSubmit = async (values: LeaveGroupAssignCreate) => {
    try {
      if (action === 'create') {
        const result = await create(values).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Group Assign',
          onSuccess: onClose,
        });
      } else if (data) {
        const result = await update({
          id: data.id,
          data: values,
        }).unwrap();
        showApiNotification({
          status: result?.status,
          message: result?.message,
          title: 'Leave Group Assign',
          onSuccess: onClose,
        });
      }

      onClose();
    } catch (error: any) {
      notifications.show({
        title: `Error while ${action} ${resourceName}`,
        message: String(error?.message) ?? 'Something went wrong',
        color: 'red',
      });
    }
  };

  const formik = useFormik<LeaveGroupAssignCreate>({
    initialValues:
      action === 'update' && data
        ? {
            leaveTypeId: data?.leaveType?.id?.toString(),
            leaveGroupId: data?.leaveGroup?.id?.toString(),
            companyId: data?.company?.id?.toString(),
            businessUnitId: data?.businessUnit?.id?.toString(),
            workPlaceGroupId: data?.workplaceGroup?.id?.toString(),
            workPlaceId: data?.workplace?.id?.toString(),
            departmentId: data?.department?.id?.toString(),
            teamId: data?.team?.id?.toString(),
            description: data?.description,
            status: data?.status,
          }
        : {
            leaveTypeId: '',
            leaveGroupId: '',
            companyId: '',
            businessUnitId: '',
            workPlaceGroupId: '',
            workPlaceId: '',
            departmentId: '',
            teamId: '',
            description: '',
            status: true,
          },
    validationSchema,
    validateOnChange: true,
    validateOnBlur: true,
    onSubmit: handleSubmit,
  });

  const { data: organizationalStructuredData } =
    useGetOrganizationStructureListQuery({
      companyId: formik.values.companyId,
      businessUnitId: formik.values.businessUnitId,
      departmentId: formik.values.departmentId ?? '',
      workplaceGroupId: formik.values.workPlaceGroupId,
      workplaceId: formik.values.workPlaceId,
    });

  return (
    <form onSubmit={formik.handleSubmit}>
      <div className="grid grid-cols-12 gap-3">
        <Select
          className="col-span-6"
          label="Leave Type"
          placeholder="Select leave type"
          clearable
          searchable
          withAsterisk
          data={leaveTypeList?.data?.map(x => ({
            label: x.name,
            value: x.id.toString(),
          }))}
          name="leaveTypeId"
          value={formik.values.leaveTypeId?.toString() || ''}
          onChange={value => formik.setFieldValue('leaveTypeId', value)}
          onBlur={() => formik.setFieldTouched('leaveTypeId', true)}
          error={
            formik.touched.leaveTypeId && formik.errors.leaveTypeId
              ? formik.errors.leaveTypeId
              : undefined
          }
        />
        <Select
          className="col-span-6"
          label="Leave Group"
          placeholder="Select leave group"
          clearable
          searchable
          withAsterisk
          data={leaveGroupList?.data?.map(x => ({
            label: x.name,
            value: x.id.toString(),
          }))}
          name="leaveGroupId"
          value={formik.values.leaveGroupId?.toString() || ''}
          onChange={value => formik.setFieldValue('leaveGroupId', value)}
          onBlur={() => formik.setFieldTouched('leaveGroupId', true)}
          error={
            formik.touched.leaveGroupId && formik.errors.leaveGroupId
              ? formik.errors.leaveGroupId
              : undefined
          }
        />
        <Select
          className="col-span-6"
          label="Company"
          placeholder="Select company"
          clearable
          searchable
          withAsterisk
          data={companyList?.data?.map(x => ({
            label: x.name,
            value: x.id.toString(),
          }))}
          name="companyId"
          value={
            formik.values.companyId ? formik.values.companyId.toString() : null
          }
          onChange={value => {
            if (!value) {
              formik.setFieldValue('companyId', '');
              formik.setFieldValue('businessUnitId', '');
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
              return;
            }
            formik.setFieldValue('companyId', value);
            // Reset dependents when switching company
            formik.setFieldValue('businessUnitId', '');
            formik.setFieldValue('workPlaceGroupId', '');
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onClear={() => {
            formik.setFieldValue('companyId', '');
            formik.setFieldValue('businessUnitId', '');
            formik.setFieldValue('workPlaceGroupId', '');
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onBlur={() => formik.setFieldTouched('companyId', true)}
          error={
            formik.touched.companyId && formik.errors.companyId
              ? formik.errors.companyId
              : undefined
          }
        />
        <Select
          className="col-span-6"
          label="Business Unit"
          placeholder="Select business unit"
          clearable
          searchable
          key={`bu-${formik.values.companyId || 'none'}`}
          disabled={!formik.values.companyId}
          data={
            formik.values.companyId
              ? (organizationalStructuredData?.data?.businessUnits?.map(x => ({
                  label: x.name,
                  value: x.id.toString(),
                })) ?? [])
              : []
          }
          name="businessUnitId"
          value={
            formik.values.businessUnitId
              ? formik.values.businessUnitId.toString()
              : null
          }
          onChange={value => {
            if (!value) {
              formik.setFieldValue('businessUnitId', '');
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
              return;
            }
            formik.setFieldValue('businessUnitId', value);
            formik.setFieldValue('workPlaceGroupId', '');
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onClear={() => {
            formik.setFieldValue('businessUnitId', '');
            formik.setFieldValue('workPlaceGroupId', '');
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onBlur={() => formik.setFieldTouched('businessUnitId', true)}
          // error={
          //   formik.touched.businessUnitId && formik.errors.businessUnitId
          //     ? formik.errors.businessUnitId
          //     : undefined
          // }
        />
        <Select
          className="col-span-6"
          label="Workplace Group"
          placeholder="Select workplace group"
          clearable
          searchable
          key={`wg-${formik.values.companyId || 'none'}-${formik.values.businessUnitId || 'none'}`}
          disabled={!formik.values.companyId || !formik.values.businessUnitId}
          data={
            formik.values.companyId && formik.values.businessUnitId
              ? (organizationalStructuredData?.data?.workplaceGroups?.map(
                  x => ({
                    label: x.name,
                    value: x.id.toString(),
                  })
                ) ?? [])
              : []
          }
          name="workPlaceGroupId"
          value={
            formik.values.workPlaceGroupId
              ? formik.values.workPlaceGroupId.toString()
              : null
          }
          onChange={value => {
            if (!value) {
              formik.setFieldValue('workPlaceGroupId', '');
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
              return;
            }
            formik.setFieldValue('workPlaceGroupId', value);
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onClear={() => {
            formik.setFieldValue('workPlaceGroupId', '');
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onBlur={() => formik.setFieldTouched('workPlaceGroupId', true)}
          // error={
          //   formik.touched.workPlaceGroupId && formik.errors.workPlaceGroupId
          //     ? formik.errors.workPlaceGroupId
          //     : undefined
          // }
        />
        <Select
          className="col-span-6"
          label="Workplace"
          placeholder="Select workplace"
          clearable
          searchable
          key={`wp-${formik.values.workPlaceGroupId || 'none'}`}
          disabled={!formik.values.workPlaceGroupId}
          data={
            formik.values.workPlaceGroupId
              ? (organizationalStructuredData?.data?.workplaces?.map(x => ({
                  label: x.name,
                  value: x.id.toString(),
                })) ?? [])
              : []
          }
          name="workPlaceId"
          value={
            formik.values.workPlaceId
              ? formik.values.workPlaceId.toString()
              : null
          }
          onChange={value => {
            if (!value) {
              formik.setFieldValue('workPlaceId', '');
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
              return;
            }
            formik.setFieldValue('workPlaceId', value);
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onClear={() => {
            formik.setFieldValue('workPlaceId', '');
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onBlur={() => formik.setFieldTouched('workPlaceId', true)}
          // error={
          //   formik.touched.workPlaceId && formik.errors.workPlaceId
          //     ? formik.errors.workPlaceId
          //     : undefined
          // }
        />
        <Select
          className="col-span-6"
          label="Department"
          placeholder="Select department"
          clearable
          searchable
          // withAsterisk
          key={`dept-${formik.values.workPlaceId || 'none'}`}
          disabled={!formik.values.workPlaceId}
          data={
            formik.values.workPlaceId
              ? (organizationalStructuredData?.data?.departments?.map(x => ({
                  label: x.name,
                  value: x.id.toString(),
                })) ?? [])
              : []
          }
          name="departmentId"
          value={
            formik.values.departmentId
              ? formik.values.departmentId.toString()
              : null
          }
          onChange={value => {
            if (!value) {
              formik.setFieldValue('departmentId', '');
              formik.setFieldValue('teamId', '');
              return;
            }
            formik.setFieldValue('departmentId', value);
            formik.setFieldValue('teamId', '');
          }}
          onClear={() => {
            formik.setFieldValue('departmentId', '');
            formik.setFieldValue('teamId', '');
          }}
          onBlur={() => formik.setFieldTouched('departmentId', true)}
          error={
            formik.touched.departmentId && formik.errors.departmentId
              ? formik.errors.departmentId
              : undefined
          }
        />
        <Select
          className="col-span-6"
          label="Team"
          placeholder="Select team"
          clearable
          searchable
          // withAsterisk
          key={`team-${formik.values.departmentId || 'none'}`}
          disabled={!formik.values.departmentId}
          data={
            formik.values.departmentId
              ? (organizationalStructuredData?.data?.teams?.map(x => ({
                  label: x.name,
                  value: x.id.toString(),
                })) ?? [])
              : []
          }
          name="teamId"
          value={formik.values.teamId ? formik.values.teamId.toString() : null}
          onChange={value => formik.setFieldValue('teamId', value)}
          onClear={() => formik.setFieldValue('teamId', '')}
          onBlur={() => formik.setFieldTouched('teamId', true)}
          error={
            formik.touched.teamId && formik.errors.teamId
              ? formik.errors.teamId
              : undefined
          }
        />
        <Textarea
          label="Description"
          placeholder="Type description"
          rows={2}
          // withAsterisk
          className=" col-span-12"
          name="description"
          value={formik.values.description}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={
            formik.touched.description && formik.errors.description
              ? formik.errors.description
              : undefined
          }
        />
        <Switch
          labelPosition="left"
          className="col-span-4"
          classNames={{ label: 'font-semibold' }}
          label="Status"
          checked={Boolean(formik.values.status)}
          onChange={event =>
            formik.setFieldValue('status', event.currentTarget.checked)
          }
        />
        <div className="col-span-12">
          <SubmitSection
            onCancel={onClose}
            confirmText={action === 'create' ? 'Create' : 'Update'}
            isLoading={isLoading || isLoadingUpdate}
          />
        </div>
      </div>
    </form>
  );
}
