// import SubmitSection from '@/components/common/form/SubmitSection';
// import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
// import { cn } from '@/lib/utils/utils';
// import { useGetLeaveGroupAssignListQuery } from '@/services/api/leave/leaveGroupAssignAPI';
// import {
//   useCreateLeavePolicyMutation,
//   useUpdateLeavePolicyMutation,
// } from '@/services/api/leave/leavePolicyAPI';
// import { useGetLeaveTypeListQuery } from '@/services/api/leave/leaveTypeAPI';
// import { NumberInput, Select, Switch } from '@mantine/core';
// import { useForm } from '@mantine/form';
// import { notifications } from '@mantine/notifications';
// import { useEffect } from 'react';
// import { LeavePolicy, LeavePolicyCreate } from '../../../lib/types/leave';

// type formProps = {
//   action: 'edit' | 'create' | null;
//   data: LeavePolicy | null;
//   onClose: () => void;
// };

// export default function LeavePolicyForm(props: formProps) {
//   const { data: leaveGroupAssign } = useGetLeaveGroupAssignListQuery({});
//   const { data: leaveTypeData } = useGetLeaveTypeListQuery({});
//   const { data: employmentTypesData } = useGetEmploymentTypesQuery(undefined);

//   const [createLeavePolicy, { isLoading: isCreating }] =
//     useCreateLeavePolicyMutation();
//   const [updateLeavePolicy, { isLoading: isUpdating }] =
//     useUpdateLeavePolicyMutation();

//   const leaveTypeList = leaveTypeData?.data ?? [];
//   const leaveTypeOptions = leaveTypeList
//     ?.filter(x => x.status)
//     .map(x => ({
//       label: `${x.name} (${x.code})`,
//       value: String(x.id),
//     }));

//   const form = useForm<LeavePolicyCreate>({
//     initialValues:
//       props.action === 'edit' && props.data
//         ? {
//             ...props.data,
//             leaveTypeId:
//               props?.data?.leaveType?.id?.toString() ||
//               props?.data?.leaveTypeId?.toString() ||
//               '',
//             leaveGroupAssignId:
//               (props?.data as any)?.leaveGroupAssign?.id?.toString() ||
//               props?.data?.leaveGroupAssignId?.toString() ||
//               '',
//             employeeTypeId: props?.data?.employeeTypeId?.toString() || '',
//           }
//         : {
//             leaveTypeId: '',
//             defaultQuota: 0,
//             minDays: 0,
//             maxDays: 0,
//             carryForwardAllowed: false,
//             carryForwardCap: 0,
//             encashable: false,
//             proofRequired: false,
//             proofThreshold: 0,
//             genderRestricted: false,
//             eligibleGender: '',
//             tenureRequiredDays: 0,
//             maxOccurrences: 0,
//             requiresApproval: false,
//             expeditedApproval: false,
//             linkedToOvertime: false,
//             accrualRatePerMonth: 0,
//             extraDaysAfterYears: 0,
//             restrictBridgeLeave: false,
//             maxBridgeDays: 0,
//             allowNegativeBalance: false,
//             maxAdvanceDays: 0,
//             status: true,
//             allowHalfDay: false,
//             earnedLeave: false,
//             earnedAfterDays: 0,
//             earnedLeaveDays: 0,
//             coveringEmployee: false,
//             prorata: false,
//             leaveGroupAssignId: '',
//             employeeTypeId: '',
//           },
//     validate: {
//       leaveTypeId: value => !value && 'Select Leave Type',
//       leaveGroupAssignId: value => !value && 'Select Leave GroupAssign',
//       maxDays: (value, values) => {
//         if (value < 1) return 'Day should be at least 1';
//         if (value <= values.minDays)
//           return 'Day should be greater than min days';
//         return undefined;
//       },
//       minDays: value => (value < 1 ? 'Day should be at least 1' : null),

//       // ✅ Conditional validation for Minimum Tenure
//       tenureRequiredDays: (value, values) => {
//         if (!values.employeeTypeId && (!value || value < 1)) {
//           return 'Minimum Tenure (Days) is required when Employee Type not selected';
//         }
//         return null;
//       },

//       // ✅ Conditional validation for Default Entitlement
//       defaultQuota: (value, values) => {
//         if (values.employeeTypeId && (!value || value < 1)) {
//           return 'Default Entitlement (Days) is required when Employee Type is selected';
//         }
//         return null;
//       },
//     },
//   });

//   useEffect(() => {
//     if (props.action === 'edit' && form.values.employeeTypeId) {
//       if (form.values.tenureRequiredDays !== 0) {
//         form.setFieldValue('tenureRequiredDays', 0);
//       }
//     }
//     // eslint-disable-next-line react-hooks/exhaustive-deps
//   }, [
//     props.action,
//     form.values.employeeTypeId,
//     form.values.tenureRequiredDays,
//   ]);

//   const handleSubmit = async (values: typeof form.values) => {
//     try {
//       const sanitizedData = { ...values };

//       if (props.action === 'create') {
//         await createLeavePolicy(sanitizedData).unwrap();
//         notifications.show({
//           title: 'Create',
//           message: 'Leave Policy created successfully',
//           color: 'green',
//         });
//       } else if (props.data) {
//         await updateLeavePolicy({
//           id: props.data.id,
//           data: sanitizedData,
//         }).unwrap();
//         notifications.show({
//           title: 'Update',
//           message: 'Leave Policy updated successfully',
//           color: 'blue',
//         });
//       }
//       props.onClose();
//     } catch (error: any) {
//       notifications.show({
//         title: 'Error',
//         message: error?.data?.message || 'Something went wrong',
//         color: 'red',
//       });
//     }
//   };

//   const DivideY = ({ strong = false }: { strong?: boolean }) => (
//     <div
//       className={cn(
//         `col-span-12 border-b`,
//         strong ? 'border-gray-400/50' : 'border-gray-400/20'
//       )}
//     ></div>
//   );

//   const heading = `${
//     props.action === 'create' ? 'Create' : 'Update'
//   } Leave Policy`;

//   return (
//     <form onSubmit={form.onSubmit(handleSubmit)}>
//       <div className="grid grid-cols-12 gap-3">
//         <div className="col-span-12 flex items-center justify-between">
//           <h1 className="font-semibold text-lg">{heading}</h1>
//           <Switch
//             labelPosition="left"
//             className="col-span-4"
//             classNames={{ label: 'font-semibold' }}
//             label="Policy Status"
//             {...form.getInputProps('status', { type: 'checkbox' })}
//           />
//         </div>

//         <DivideY strong />

//         {/* Leave Type */}
//         <Select
//           size="xs"
//           className="col-span-12"
//           required
//           label="Leave Type"
//           placeholder="Select Leave Type"
//           {...form.getInputProps('leaveTypeId')}
//           data={leaveTypeOptions}
//         />

//         {/* Leave Group */}
//         <Select
//           size="xs"
//           className="col-span-12"
//           required
//           label="Assign to Leave Group"
//           placeholder="Select Leave Group Assign"
//           {...form.getInputProps('leaveGroupAssignId')}
//           data={
//             leaveGroupAssign?.data?.map(c => ({
//               label: [
//                 c.leaveGroup?.name,
//                 c.leaveType?.name,
//                 (c as any)?.company?.name || c.company?.name,
//               ]
//                 .filter(Boolean)
//                 .join(' - '),
//               value: String(c.id),
//             })) ?? []
//           }
//         />

//         {/* Employee Type */}
//         <Select
//           withAsterisk
//           clearable
//           size="xs"
//           className="col-span-12"
//           label="Employee Type"
//           placeholder="Select Employee Type"
//           {...form.getInputProps('employeeTypeId')}
//           data={
//             employmentTypesData?.data?.map(c => ({
//               label: c.name,
//               value: String(c.id),
//             })) ?? []
//           }
//         />

//         {/* Entitlements */}
//         <NumberInput
//           label="Default Entitlement (Days)"
//           name="defaultQuota"
//           className="col-span-3"
//           required={!!form.values.employeeTypeId} // Show red asterisk when Employee Type selected
//           {...form.getInputProps('defaultQuota')}
//           min={0}
//         />

//         <NumberInput
//           withAsterisk
//           label="Min Days per Request"
//           name="minDays"
//           className="col-span-3"
//           {...form.getInputProps('minDays')}
//           min={1}
//         />
//         <NumberInput
//           withAsterisk
//           label="Max Days per Request"
//           name="maxDays"
//           className="col-span-3"
//           {...form.getInputProps('maxDays')}
//           min={0}
//         />

//         {/* ✅ Conditional Field */}
//         <NumberInput
//           label="Minimum Tenure (Days)"
//           name="tenureRequiredDays"
//           className="col-span-3"
//           disabled={!!form.values.employeeTypeId} // disable if selected
//           required={!form.values.employeeTypeId} // required if not selected
//           {...form.getInputProps('tenureRequiredDays')}
//           min={0}
//         />

//         <DivideY />

//         {/* Other switches */}
//         <Switch
//           className="col-span-4"
//           label="Requires Approval"
//           {...form.getInputProps('requiresApproval', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Encashment Allowed"
//           {...form.getInputProps('encashable', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Half-Day Allowed"
//           {...form.getInputProps('allowHalfDay', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Covering Person Required"
//           {...form.getInputProps('coveringEmployee', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Pro-Rata Entitlement"
//           {...form.getInputProps('prorata', { type: 'checkbox' })}
//         />

//         <DivideY />

//         {/* Gender Restriction */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Gender Restriction"
//           {...form.getInputProps('genderRestricted', { type: 'checkbox' })}
//         />
//         <Select
//           className="col-span-3"
//           label="Eligible Gender"
//           disabled={!form.values.genderRestricted}
//           data={[
//             { label: 'Male', value: 'MALE' },
//             { label: 'Female', value: 'FEMALE' },
//           ]}
//           placeholder="Select Gender"
//           {...form.getInputProps('eligibleGender')}
//         />

//         <DivideY />

//         {/* Carry Forward */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Carry Forward Allowed"
//           {...form.getInputProps('carryForwardAllowed', { type: 'checkbox' })}
//         />
//         <NumberInput
//           disabled={!form.values.carryForwardAllowed}
//           className="col-span-3"
//           label="Carry Forward Cap (Days)"
//           {...form.getInputProps('carryForwardCap')}
//           min={0}
//         />

//         <DivideY />

//         {/* Proof */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Supporting Document Required"
//           {...form.getInputProps('proofRequired', { type: 'checkbox' })}
//         />
//         <NumberInput
//           disabled={!form.values.proofRequired}
//           label="Min Documents"
//           className="col-span-3"
//           {...form.getInputProps('proofThreshold')}
//           min={0}
//         />

//         <DivideY />

//         {/* Earned Leave */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Earned-Type Leave"
//           {...form.getInputProps('earnedLeave', { type: 'checkbox' })}
//         />
//         <NumberInput
//           disabled={!form.values.earnedLeave}
//           label="Accrual Frequency (Days)"
//           className="col-span-3"
//           {...form.getInputProps('earnedAfterDays')}
//           min={0}
//         />
//         <NumberInput
//           disabled={!form.values.earnedLeave}
//           label="Accrual Rate (Days)"
//           className="col-span-3"
//           {...form.getInputProps('earnedLeaveDays')}
//           min={0}
//         />

//         <DivideY />

//         <div className="col-span-12">
//           <SubmitSection
//             isLoading={isCreating || isUpdating}
//             onCancel={props.onClose}
//             cancelText="Cancel"
//             confirmText={props.action === 'create' ? 'Create' : 'Update'}
//           />
//         </div>
//       </div>
//     </form>
//   );
// }




// 'use client';

// import SubmitSection from '@/components/common/form/SubmitSection';
// import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
// import { cn } from '@/lib/utils/utils';
// import {
//   useCreateLeavePolicyMutation,
//   useUpdateLeavePolicyMutation,
// } from '@/services/api/leave/leavePolicyAPI';
// import { useGetLeaveTypeListQuery } from '@/services/api/leave/leaveTypeAPI';
// import {
//   useGetLeaveGroupAssignsByTypeQuery,
// } from '@/services/api/leave/leaveGroupAssignAPI';
// import { NumberInput, Select, Switch } from '@mantine/core';
// import { useForm } from '@mantine/form';
// import { notifications } from '@mantine/notifications';
// import { useEffect } from 'react';
// import { LeavePolicy, LeavePolicyCreate } from '../../../lib/types/leave';

// type formProps = {
//   action: 'edit' | 'create' | null;
//   data: LeavePolicy | null;
//   onClose: () => void;
// };

// export default function LeavePolicyForm(props: formProps) {
//   const { data: leaveTypeData } = useGetLeaveTypeListQuery({});
//   const { data: employmentTypesData } = useGetEmploymentTypesQuery(undefined);

//   const [createLeavePolicy, { isLoading: isCreating }] =
//     useCreateLeavePolicyMutation();
//   const [updateLeavePolicy, { isLoading: isUpdating }] =
//     useUpdateLeavePolicyMutation();

//   const leaveTypeList = leaveTypeData?.data ?? [];
//   const leaveTypeOptions = leaveTypeList
//     ?.filter(x => x.status)
//     .map(x => ({
//       label: `${x.name} (${x.code})`,
//       value: String(x.id),
//     }));

//   const form = useForm<LeavePolicyCreate>({
//     initialValues:
//       props.action === 'edit' && props.data
//         ? {
//             ...props.data,
//             leaveTypeId:
//               props?.data?.leaveType?.id?.toString() ||
//               props?.data?.leaveTypeId?.toString() ||
//               '',
//             leaveGroupAssignId:
//               (props?.data as any)?.leaveGroupAssign?.id?.toString() ||
//               props?.data?.leaveGroupAssignId?.toString() ||
//               '',
//             employeeTypeId: props?.data?.employeeTypeId?.toString() || '',
//           }
//         : {
//             leaveTypeId: '',
//             defaultQuota: 0,
//             minDays: 0,
//             maxDays: 0,
//             carryForwardAllowed: false,
//             carryForwardCap: 0,
//             encashable: false,
//             proofRequired: false,
//             proofThreshold: 0,
//             genderRestricted: false,
//             eligibleGender: '',
//             tenureRequiredDays: 0,
//             maxOccurrences: 0,
//             requiresApproval: false,
//             expeditedApproval: false,
//             linkedToOvertime: false,
//             accrualRatePerMonth: 0,
//             extraDaysAfterYears: 0,
//             restrictBridgeLeave: false,
//             maxBridgeDays: 0,
//             allowNegativeBalance: false,
//             maxAdvanceDays: 0,
//             status: true,
//             allowHalfDay: false,
//             earnedLeave: false,
//             earnedAfterDays: 0,
//             earnedLeaveDays: 0,
//             coveringEmployee: false,
//             prorata: false,
//             leaveGroupAssignId: '',
//             employeeTypeId: '',
//           },
//     validate: {
//       leaveTypeId: value => !value && 'Select Leave Type',
//       leaveGroupAssignId: value => !value && 'Select Leave GroupAssign',
//       maxDays: (value, values) => {
//         if (value < 1) return 'Day should be at least 1';
//         if (value <= values.minDays)
//           return 'Day should be greater than min days';
//         return undefined;
//       },
//       minDays: value => (value < 1 ? 'Day should be at least 1' : null),

//       tenureRequiredDays: (value, values) => {
//         if (!values.employeeTypeId && (!value || value < 1)) {
//           return 'Minimum Tenure (Days) is required when Employee Type not selected';
//         }
//         return null;
//       },

//       defaultQuota: (value, values) => {
//         if (values.employeeTypeId && (!value || value < 1)) {
//           return 'Default Entitlement (Days) is required when Employee Type is selected';
//         }
//         return null;
//       },
//     },
//   });

//   // reset leave group assign if leave type changes
//   useEffect(() => {
//     form.setFieldValue('leaveGroupAssignId', '');
//   }, [form.values.leaveTypeId]);

//   // conditional reset for tenure days
//   useEffect(() => {
//     if (props.action === 'edit' && form.values.employeeTypeId) {
//       if (form.values.tenureRequiredDays !== 0) {
//         form.setFieldValue('tenureRequiredDays', 0);
//       }
//     }
//   }, [props.action, form.values.employeeTypeId, form.values.tenureRequiredDays]);

//   // fetch group assign by leave type
//   const leaveTypeId = form.values.leaveTypeId;
//   const { data: leaveGroupAssign, isFetching: isGroupFetching } =
//     useGetLeaveGroupAssignsByTypeQuery(
//       { leaveTypeId: Number(leaveTypeId) || 0 },
//       { skip: !leaveTypeId }
//     );

//   const handleSubmit = async (values: typeof form.values) => {
//     try {
//       const sanitizedData = { ...values };

//       if (props.action === 'create') {
//         await createLeavePolicy(sanitizedData).unwrap();
//         notifications.show({
//           title: 'Create',
//           message: 'Leave Policy created successfully',
//           color: 'green',
//         });
//       } else if (props.data) {
//         await updateLeavePolicy({
//           id: props.data.id,
//           data: sanitizedData,
//         }).unwrap();
//         notifications.show({
//           title: 'Update',
//           message: 'Leave Policy updated successfully',
//           color: 'blue',
//         });
//       }
//       props.onClose();
//     } catch (error: any) {
//       notifications.show({
//         title: 'Error',
//         message: error?.data?.message || 'Something went wrong',
//         color: 'red',
//       });
//     }
//   };

//   const DivideY = ({ strong = false }: { strong?: boolean }) => (
//     <div
//       className={cn(
//         `col-span-12 border-b`,
//         strong ? 'border-gray-400/50' : 'border-gray-400/20'
//       )}
//     ></div>
//   );

//   const heading = `${
//     props.action === 'create' ? 'Create' : 'Update'
//   } Leave Policy`;

//   return (
//     <form onSubmit={form.onSubmit(handleSubmit)}>
//       <div className="grid grid-cols-12 gap-3">
//         <div className="col-span-12 flex items-center justify-between">
//           <h1 className="font-semibold text-lg">{heading}</h1>
//           <Switch
//             labelPosition="left"
//             className="col-span-4"
//             classNames={{ label: 'font-semibold' }}
//             label="Policy Status"
//             {...form.getInputProps('status', { type: 'checkbox' })}
//           />
//         </div>

//         <DivideY strong />

//         {/* Leave Type */}
//         <Select
//           size="xs"
//           className="col-span-12"
//           required
//           label="Leave Type"
//           placeholder="Select Leave Type"
//           {...form.getInputProps('leaveTypeId')}
//           data={leaveTypeOptions}
//         />

//         {/* Leave Group (filtered by leave type) */}
//         <Select
//           size="xs"
//           className="col-span-12"
//           required
//           label="Assign to Leave Group"
//           placeholder={
//             isGroupFetching
//               ? 'Loading...'
//               : leaveTypeId
//               ? 'Select Leave Group Assign'
//               : 'Select Leave Type first'
//           }
//           disabled={!leaveTypeId || isGroupFetching}
//           {...form.getInputProps('leaveGroupAssignId')}
//           data={
//             leaveGroupAssign?.data?.map(c => ({
//               label: [
//                 c.leaveGroup?.name,
//                 c.leaveType?.name,
//                 (c as any)?.company?.name || c.company?.name,
//               ]
//                 .filter(Boolean)
//                 .join(' - '),
//               value: String(c.id),
//             })) ?? []
//           }
//         />

//         {/* Employee Type */}
//         <Select
//           withAsterisk
//           clearable
//           size="xs"
//           className="col-span-12"
//           label="Employee Type"
//           placeholder="Select Employee Type"
//           {...form.getInputProps('employeeTypeId')}
//           data={
//             employmentTypesData?.data?.map(c => ({
//               label: c.name,
//               value: String(c.id),
//             })) ?? []
//           }
//         />

//         {/* Entitlements */}
//         <NumberInput
//           label="Default Entitlement (Days)"
//           name="defaultQuota"
//           className="col-span-3"
//           required={!!form.values.employeeTypeId}
//           {...form.getInputProps('defaultQuota')}
//           min={0}
//         />

//         <NumberInput
//           withAsterisk
//           label="Min Days per Request"
//           name="minDays"
//           className="col-span-3"
//           {...form.getInputProps('minDays')}
//           min={1}
//         />
//         <NumberInput
//           withAsterisk
//           label="Max Days per Request"
//           name="maxDays"
//           className="col-span-3"
//           {...form.getInputProps('maxDays')}
//           min={0}
//         />

//         <NumberInput
//           label="Minimum Tenure (Days)"
//           name="tenureRequiredDays"
//           className="col-span-3"
//           disabled={!!form.values.employeeTypeId}
//           required={!form.values.employeeTypeId}
//           {...form.getInputProps('tenureRequiredDays')}
//           min={0}
//         />

//         <DivideY />

//         {/* Other switches */}
//         <Switch
//           className="col-span-4"
//           label="Requires Approval"
//           {...form.getInputProps('requiresApproval', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Encashment Allowed"
//           {...form.getInputProps('encashable', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Half-Day Allowed"
//           {...form.getInputProps('allowHalfDay', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Covering Person Required"
//           {...form.getInputProps('coveringEmployee', { type: 'checkbox' })}
//         />
//         <Switch
//           className="col-span-4"
//           label="Pro-Rata Entitlement"
//           {...form.getInputProps('prorata', { type: 'checkbox' })}
//         />

//         <DivideY />

//         {/* Gender Restriction */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Gender Restriction"
//           {...form.getInputProps('genderRestricted', { type: 'checkbox' })}
//         />
//         <Select
//           className="col-span-3"
//           label="Eligible Gender"
//           disabled={!form.values.genderRestricted}
//           data={[
//             { label: 'Male', value: 'MALE' },
//             { label: 'Female', value: 'FEMALE' },
//           ]}
//           placeholder="Select Gender"
//           {...form.getInputProps('eligibleGender')}
//         />

//         <DivideY />

//         {/* Carry Forward */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Carry Forward Allowed"
//           {...form.getInputProps('carryForwardAllowed', { type: 'checkbox' })}
//         />
//         <NumberInput
//           disabled={!form.values.carryForwardAllowed}
//           className="col-span-3"
//           label="Carry Forward Cap (Days)"
//           {...form.getInputProps('carryForwardCap')}
//           min={0}
//         />

//         <DivideY />

//         {/* Proof */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Supporting Document Required"
//           {...form.getInputProps('proofRequired', { type: 'checkbox' })}
//         />
//         <NumberInput
//           disabled={!form.values.proofRequired}
//           label="Min Documents"
//           className="col-span-3"
//           {...form.getInputProps('proofThreshold')}
//           min={0}
//         />

//         <DivideY />

//         {/* Earned Leave */}
//         <Switch
//           className="col-span-4 self-center"
//           label="Earned-Type Leave"
//           {...form.getInputProps('earnedLeave', { type: 'checkbox' })}
//         />
//         <NumberInput
//           disabled={!form.values.earnedLeave}
//           label="Accrual Frequency (Days)"
//           className="col-span-3"
//           {...form.getInputProps('earnedAfterDays')}
//           min={0}
//         />
//         <NumberInput
//           disabled={!form.values.earnedLeave}
//           label="Accrual Rate (Days)"
//           className="col-span-3"
//           {...form.getInputProps('earnedLeaveDays')}
//           min={0}
//         />

//         <DivideY />

//         <div className="col-span-12">
//           <SubmitSection
//             isLoading={isCreating || isUpdating}
//             onCancel={props.onClose}
//             cancelText="Cancel"
//             confirmText={props.action === 'create' ? 'Create' : 'Update'}
//           />
//         </div>
//       </div>
//     </form>
//   );
// }


'use client';

import SubmitSection from '@/components/common/form/SubmitSection';
import { useGetEmploymentTypesQuery } from '@/lib/features/employment/employmentTypesAPI';
import { cn } from '@/lib/utils/utils';
import {
  useCreateLeavePolicyMutation,
  useUpdateLeavePolicyMutation,
} from '@/services/api/leave/leavePolicyAPI';
import { useGetLeaveTypeListQuery } from '@/services/api/leave/leaveTypeAPI';
import {
  useGetLeaveGroupAssignsByTypeQuery,
} from '@/services/api/leave/leaveGroupAssignAPI';
import { NumberInput, Select, Switch } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { useEffect, useState } from 'react';
import { LeavePolicy, LeavePolicyCreate } from '../../../lib/types/leave';

type formProps = {
  action: 'edit' | 'create' | null;
  data: LeavePolicy | null;
  onClose: () => void;
};

export default function LeavePolicyForm(props: formProps) {
  const { data: leaveTypeData } = useGetLeaveTypeListQuery({});
  const { data: employmentTypesData } = useGetEmploymentTypesQuery(undefined);

  const [createLeavePolicy, { isLoading: isCreating }] =
    useCreateLeavePolicyMutation();
  const [updateLeavePolicy, { isLoading: isUpdating }] =
    useUpdateLeavePolicyMutation();

  const leaveTypeList = leaveTypeData?.data ?? [];
  const leaveTypeOptions = leaveTypeList
    ?.filter(x => x.status)
    .map(x => ({
      label: `${x.name} (${x.code})`,
      value: String(x.id),
    }));

  const form = useForm<LeavePolicyCreate>({
    initialValues:
      props.action === 'edit' && props.data
        ? {
            ...props.data,
            leaveTypeId:
              props?.data?.leaveType?.id?.toString() ||
              props?.data?.leaveTypeId?.toString() ||
              '',
            leaveGroupAssignId:
              (props?.data as any)?.leaveGroupAssign?.id?.toString() ||
              props?.data?.leaveGroupAssignId?.toString() ||
              '',
            employeeTypeId: props?.data?.employeeTypeId?.toString() || '',
          }
        : {
            leaveTypeId: '',
            defaultQuota: 0,
            minDays: 0,
            maxDays: 0,
            carryForwardAllowed: false,
            carryForwardCap: 0,
            encashable: false,
            proofRequired: false,
            proofThreshold: 0,
            genderRestricted: false,
            eligibleGender: '',
            tenureRequiredDays: 0,
            maxOccurrences: 0,
            requiresApproval: false,
            expeditedApproval: false,
            linkedToOvertime: false,
            accrualRatePerMonth: 0,
            extraDaysAfterYears: 0,
            restrictBridgeLeave: false,
            maxBridgeDays: 0,
            allowNegativeBalance: false,
            maxAdvanceDays: 0,
            status: true,
            allowHalfDay: false,
            earnedLeave: false,
            earnedAfterDays: 0,
            earnedLeaveDays: 0,
            coveringEmployee: false,
            prorata: false,
            leaveGroupAssignId: '',
            employeeTypeId: '',
          },
    validate: {
      leaveTypeId: value => !value && 'Select Leave Type',
      leaveGroupAssignId: value => !value && 'Select Leave Group Assign',
      maxDays: (value, values) => {
        if (value < 1) return 'Day should be at least 1';
        if (value <= values.minDays)
          return 'Day should be greater than min days';
        return undefined;
      },
      minDays: value => (value < 1 ? 'Day should be at least 1' : null),

      tenureRequiredDays: (value, values) => {
        if (!values.employeeTypeId && (!value || value < 1)) {
          return 'Minimum Tenure (Days) is required when Employee Type not selected';
        }
        return null;
      },

      defaultQuota: (value, values) => {
        if (values.employeeTypeId && (!value || value < 1)) {
          return 'Default Entitlement (Days) is required when Employee Type is selected';
        }
        return null;
      },
    },
  });

  // 🔄 Refresh key for refetch trigger
  const [, setRefreshKey] = useState(0);

  // Trigger refetch when leaveType changes
  useEffect(() => {
    if (form.values.leaveTypeId) {
      setRefreshKey(k => k + 1);
    }
  }, [form.values.leaveTypeId]);

  // ✅ Fetch group assign by leave type
  const leaveTypeId = form.values.leaveTypeId;
  const { data: leaveGroupAssign, isFetching: isGroupFetching } =
    useGetLeaveGroupAssignsByTypeQuery(
      { leaveTypeId: Number(leaveTypeId) || 0 },
      {
        skip: !leaveTypeId,
        refetchOnMountOrArgChange: true,
        refetchOnReconnect: true,
        refetchOnFocus: true,
      }
    );

  // ✅ If existing group assign doesn't belong to new leave type → clear it
  useEffect(() => {
    if (!leaveGroupAssign?.data?.length) return;

    const current = form.values.leaveGroupAssignId;
    const validIds = leaveGroupAssign.data.map(x => String(x.id));

    if (current && !validIds.includes(String(current))) {
      form.setFieldValue('leaveGroupAssignId', '');
    }
  }, [leaveGroupAssign?.data]);


  // Conditional reset for tenure days when editing
  useEffect(() => {
    if (props.action === 'edit' && form.values.employeeTypeId) {
      if (form.values.tenureRequiredDays !== 0) {
        form.setFieldValue('tenureRequiredDays', 0);
      }
    }
  }, [props.action, form.values.employeeTypeId, form.values.tenureRequiredDays]);

  // ✅ Handle Submit
  const handleSubmit = async (values: typeof form.values) => {
    try {
      const sanitizedData = { ...values };

      if (props.action === 'create') {
        await createLeavePolicy(sanitizedData).unwrap();
        notifications.show({
          title: 'Create',
          message: 'Leave Policy created successfully',
          color: 'green',
        });
      } else if (props.data) {
        await updateLeavePolicy({
          id: props.data.id,
          data: sanitizedData,
        }).unwrap();
        notifications.show({
          title: 'Update',
          message: 'Leave Policy updated successfully',
          color: 'blue',
        });
      }
      props.onClose();
    } catch (error: any) {
      notifications.show({
        title: 'Error',
        message: error?.data?.message || 'Something went wrong',
        color: 'red',
      });
    }
  };

  const DivideY = ({ strong = false }: { strong?: boolean }) => (
    <div
      className={cn(
        `col-span-12 border-b`,
        strong ? 'border-gray-400/50' : 'border-gray-400/20'
      )}
    ></div>
  );

  const heading = `${
    props.action === 'create' ? 'Create' : 'Update'
  } Leave Policy`;

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <div className="grid grid-cols-12 gap-3">
        <div className="col-span-12 flex items-center justify-between">
          <h1 className="font-semibold text-lg">{heading}</h1>
          <Switch
            labelPosition="left"
            className="col-span-4"
            classNames={{ label: 'font-semibold' }}
            label="Policy Status"
            {...form.getInputProps('status', { type: 'checkbox' })}
          />
        </div>

        <DivideY strong />

        {/* Leave Type */}
        <Select
          size="xs"
          className="col-span-12"
          required
          label="Leave Type"
          placeholder="Select Leave Type"
          {...form.getInputProps('leaveTypeId')}
          data={leaveTypeOptions}
        />

        {/* Leave Group (filtered by leave type) */}
        <Select
          key={form.values.leaveTypeId} // ensures UI reset when leave type changes
          size="xs"
          className="col-span-12"
          required
          label="Assign to Leave Group"
          placeholder={
            !form.values.leaveTypeId
              ? 'Select Leave Type first'
              : isGroupFetching
              ? 'Loading groups...'
              : 'Select Leave Group Assign'
          }
          disabled={!form.values.leaveTypeId || isGroupFetching}
          {...form.getInputProps('leaveGroupAssignId')}
          data={
            leaveGroupAssign?.data?.map(c => ({
              label: [
                c.leaveGroup?.name,
                c.leaveType?.name,
                c.company?.name,
              ]
                .filter(Boolean)
                .join(' - '),
              value: String(c.id),
            })) ?? []
          }
        />

        {/* Employee Type */}
        <Select
          withAsterisk
          clearable
          size="xs"
          className="col-span-12"
          label="Employee Type"
          placeholder="Select Employee Type"
          {...form.getInputProps('employeeTypeId')}
          data={
            employmentTypesData?.data?.map(c => ({
              label: c.name,
              value: String(c.id),
            })) ?? []
          }
        />

        {/* Entitlements */}
        <NumberInput
          label="Default Entitlement (Days)"
          name="defaultQuota"
          className="col-span-3"
          required={!!form.values.employeeTypeId}
          {...form.getInputProps('defaultQuota')}
          min={0}
        />

        <NumberInput
          withAsterisk
          label="Min Days per Request"
          name="minDays"
          className="col-span-3"
          {...form.getInputProps('minDays')}
          min={1}
        />
        <NumberInput
          withAsterisk
          label="Max Days per Request"
          name="maxDays"
          className="col-span-3"
          {...form.getInputProps('maxDays')}
          min={0}
        />

        <NumberInput
          label="Minimum Tenure (Days)"
          name="tenureRequiredDays"
          className="col-span-3"
          disabled={!!form.values.employeeTypeId}
          required={!form.values.employeeTypeId}
          {...form.getInputProps('tenureRequiredDays')}
          min={0}
        />

        <DivideY />

        {/* Other switches */}
        <Switch
          className="col-span-4"
          label="Requires Approval"
          {...form.getInputProps('requiresApproval', { type: 'checkbox' })}
        />
        <Switch
          className="col-span-4"
          label="Encashment Allowed"
          {...form.getInputProps('encashable', { type: 'checkbox' })}
        />
        <Switch
          className="col-span-4"
          label="Half-Day Allowed"
          {...form.getInputProps('allowHalfDay', { type: 'checkbox' })}
        />
        <Switch
          className="col-span-4"
          label="Covering Person Required"
          {...form.getInputProps('coveringEmployee', { type: 'checkbox' })}
        />
        <Switch
          className="col-span-4"
          label="Pro-Rata Entitlement"
          {...form.getInputProps('prorata', { type: 'checkbox' })}
        />

        <DivideY />

        {/* Gender Restriction */}
        <Switch
          className="col-span-4 self-center"
          label="Gender Restriction"
          {...form.getInputProps('genderRestricted', { type: 'checkbox' })}
        />
        <Select
          className="col-span-3"
          label="Eligible Gender"
          disabled={!form.values.genderRestricted}
          data={[
            { label: 'Male', value: 'MALE' },
            { label: 'Female', value: 'FEMALE' },
          ]}
          placeholder="Select Gender"
          {...form.getInputProps('eligibleGender')}
        />

        <DivideY />

        {/* Carry Forward */}
        <Switch
          className="col-span-4 self-center"
          label="Carry Forward Allowed"
          {...form.getInputProps('carryForwardAllowed', { type: 'checkbox' })}
        />
        <NumberInput
          disabled={!form.values.carryForwardAllowed}
          className="col-span-3"
          label="Carry Forward Cap (Days)"
          {...form.getInputProps('carryForwardCap')}
          min={0}
        />

        <DivideY />

        {/* Proof */}
        <Switch
          className="col-span-4 self-center"
          label="Supporting Document Required"
          {...form.getInputProps('proofRequired', { type: 'checkbox' })}
        />
        <NumberInput
          disabled={!form.values.proofRequired}
          label="Min Documents"
          className="col-span-3"
          {...form.getInputProps('proofThreshold')}
          min={0}
        />

        <DivideY />

        {/* Earned Leave */}
        <Switch
          className="col-span-4 self-center"
          label="Earned-Type Leave"
          {...form.getInputProps('earnedLeave', { type: 'checkbox' })}
        />
        <NumberInput
          disabled={!form.values.earnedLeave}
          label="Accrual Frequency (Days)"
          className="col-span-3"
          {...form.getInputProps('earnedAfterDays')}
          min={0}
        />
        <NumberInput
          disabled={!form.values.earnedLeave}
          label="Accrual Rate (Days)"
          className="col-span-3"
          {...form.getInputProps('earnedLeaveDays')}
          min={0}
        />

        <DivideY />

        <div className="col-span-12">
          <SubmitSection
            isLoading={isCreating || isUpdating}
            onCancel={props.onClose}
            cancelText="Cancel"
            confirmText={props.action === 'create' ? 'Create' : 'Update'}
          />
        </div>
      </div>
    </form>
  );
}
