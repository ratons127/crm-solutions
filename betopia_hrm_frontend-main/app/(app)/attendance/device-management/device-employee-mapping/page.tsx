'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import {
  useDeleteAttendanceDeviceAssignMutation,
  useGetAttendanceDeviceAssignListQuery,
  useUpdateAttendanceDeviceAssignMutation,
} from '@/lib/features/attendanceDeviceAssign/attendanceDeviceAssignAPI';
import { AttendanceDeviceAssign } from '@/lib/types/attendanceDeviceAssign';
import { Employee } from '@/lib/types/employee/index';
import { useGetEmployeeListQuery } from '@/services/api/employee/employeeProfileAPI';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import {
  ActionIcon,
  Badge,
  Button,
  Loader,
  Modal,
  Paper,
  Select,
  Switch,
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { useState } from 'react';
import { BiTrash } from 'react-icons/bi';
import { FiActivity } from 'react-icons/fi';
import AssignDeviceForm from './AssignDeviceForm';

export default function DeviceEmployeeMapping() {
  const [selectedEmployee, setSelectedEmployee] = useState<Employee | null>(
    null
  );
  const [action, setAction] = useState<null | 'create' | 'update'>(null);
  const [selectedMapping, setSelectedMapping] =
    useState<AttendanceDeviceAssign | null>(null);

  // Fetch employees
  const { data: employeeListData, isLoading: isLoadingEmployees } =
    useGetEmployeeListQuery(undefined);

  // Fetch all device mappings
  const { data: mappingsData, isLoading: isLoadingMappings } =
    useGetAttendanceDeviceAssignListQuery(undefined);

  // Delete mutation
  const [deleteMapping] = useDeleteAttendanceDeviceAssignMutation();

  // Update mutation for status toggle
  const [updateMapping] = useUpdateAttendanceDeviceAssignMutation();

  // Handle delete
  const handleDelete = async (id: number) => {
    try {
      await deleteMapping({ id }).unwrap();
      notifications.show({
        title: 'Success',
        message: 'Device mapping deleted successfully',
        color: 'green',
      });
    } catch (error) {
      notifications.show({
        title: 'Error',
        message: 'Failed to delete device mapping',
        color: 'red',
      });
    }
  };

  // Handle status toggle
  const handleStatusToggle = async (mapping: AttendanceDeviceAssign) => {
    try {
      await updateMapping({
        id: mapping.id,
        data: {
          attendanceDeviceId:
            mapping.attendanceDevice?.id || mapping.attendanceDeviceId || 0,
          employeeId: mapping.employee?.id || mapping.employeeId || 0,
          deviceUserId: mapping.deviceUserId,
          assignedBy: mapping.assignedBy,
          assignedAt: mapping.assignedAt,
          status: !mapping.status,
          notes: mapping.notes || '',
        },
      }).unwrap();
      notifications.show({
        title: 'Success',
        message: `Device mapping ${!mapping.status ? 'activated' : 'deactivated'} successfully`,
        color: 'green',
      });
    } catch (error) {
      notifications.show({
        title: 'Error',
        message: 'Failed to update status',
        color: 'red',
      });
    }
  };

  // Filter mappings by selected employee
  const allMappings = mappingsData?.data || [];
  const deviceMappings = selectedEmployee
    ? allMappings.filter(
        mapping => mapping.employee?.id === selectedEmployee.id
      )
    : [];

  return (
    <div className="space-y-4">
      <Paper p={20} radius={5} shadow="sm">
        <Breadcrumbs />
      </Paper>

      {/* Single Column Layout */}
      <Paper
        p={24}
        radius={15}
        shadow="lg"
        className="min-h-[calc(100vh-200px)]"
      >
        <div className="space-y-6">
          {/* Employee Selection Section */}
          <div className="space-y-4">
            <div className="flex items-start gap-2">
              <MagnifyingGlassIcon className="h-5 w-5 text-gray-600 mt-1" />
              <div>
                <h2 className="text-lg font-semibold">
                  Device Employee Mapping
                </h2>
                <p className="text-sm text-[#64748B]">
                  Select an employee to view and manage device assignments
                </p>
              </div>
            </div>

            {/* Employee Dropdown */}
            {isLoadingEmployees ? (
              <div className="flex justify-center py-8">
                <LoadingSpinner />
              </div>
            ) : (
              <div className="grid grid-cols-11 gap-2">
                <Select
                  radius={8}
                  className="col-span-10"
                  size="sm"
                  placeholder="Select Employee"
                  searchable
                  clearable
                  data={
                    employeeListData?.data?.map(e => ({
                      label: `${e.firstName} ${e.lastName} (ID: ${e.id})`,
                      value: String(e.id),
                    })) ?? []
                  }
                  value={selectedEmployee ? String(selectedEmployee.id) : null}
                  onChange={value => {
                    const employee = employeeListData?.data?.find(
                      e => String(e.id) === value
                    );
                    setSelectedEmployee(employee || null);
                  }}
                  name="employeeId"
                />
                <Button
                  radius={8}
                  size="sm"
                  className="bg-orange-500 hover:bg-orange-600 col-span-1"
                  onClick={() => setAction('create')}
                >
                  Assign Device
                </Button>
              </div>
            )}
          </div>

          {/* Device Mappings Section */}
          {selectedEmployee ? (
            <div className="space-y-4 pt-6">
              {/* Header */}
              <div className="flex items-start justify-between pt-6">
                <div className="flex items-start gap-2">
                  <FiActivity className="h-5 w-5 text-blue-600 mt-1" />
                  <div>
                    <h2 className="text-lg font-semibold text-gray-800">
                      Device Mappings for {selectedEmployee.firstName}{' '}
                      {selectedEmployee.lastName}
                    </h2>
                    <p className="text-sm text-gray-500">
                      Employee ID: {selectedEmployee.id} •{' '}
                      {selectedEmployee.email || 'No email'}
                    </p>
                  </div>
                </div>
                {/* <div className="flex gap-2">
                  <Button
                    variant="transparent"
                    leftSection={<RiHistoryLine className="h-4 w-4" />}
                    size="sm"
                    radius={8}
                    className="border border-gray-200"
                  >
                    History
                  </Button>
                  <Button
                    radius={8}
                    leftSection={<span className="text-lg">+</span>}
                    size="sm"
                    className="bg-orange-500 hover:bg-orange-600"
                    onClick={() => setAction('create')}
                  >
                    Assign Device
                  </Button>
                </div> */}
              </div>

              {/* Device Mappings List */}
              <div className="space-y-4">
                {isLoadingMappings ? (
                  <div className="flex justify-center py-8">
                    <Loader size="md" />
                  </div>
                ) : deviceMappings.length > 0 ? (
                  deviceMappings.map(mapping => (
                    <div
                      key={mapping.id}
                      className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
                    >
                      <div className="flex items-start justify-between mb-3">
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-2">
                            <h3 className="font-semibold text-gray-800">
                              {mapping.attendanceDevice?.deviceName ||
                                `Device #${mapping.attendanceDeviceId}`}
                            </h3>
                            <Badge
                              color={mapping.status ? 'green' : 'red'}
                              variant="light"
                              size="sm"
                              className="font-medium"
                            >
                              {mapping.status ? 'Active' : 'Inactive'}
                            </Badge>
                          </div>
                          <div className="grid grid-cols-2 gap-4 text-sm">
                            <div>
                              <p className="text-gray-500">Device User ID:</p>
                              <p className="text-gray-800 font-medium">
                                {mapping.deviceUserId}
                              </p>
                            </div>
                            <div>
                              <p className="text-gray-500">Assigned At:</p>
                              <p className="text-gray-800 font-medium">
                                {new Date(
                                  mapping.assignedAt
                                ).toLocaleDateString()}
                              </p>
                            </div>
                          </div>
                          <div className="mt-2 text-sm">
                            <p className="text-gray-500">
                              Created:{' '}
                              {mapping.createdAt
                                ? new Date(
                                    mapping.createdAt
                                  ).toLocaleDateString()
                                : 'N/A'}
                            </p>
                            {mapping.notes && (
                              <p className="text-gray-600 mt-1">
                                Notes: {mapping.notes}
                              </p>
                            )}
                          </div>
                        </div>
                        <div className="flex items-center gap-3 ml-4">
                          <Switch
                            checked={mapping.status}
                            color="orange"
                            size="md"
                            onChange={() => handleStatusToggle(mapping)}
                          />
                          <ActionIcon
                            variant="subtle"
                            color="red"
                            size="lg"
                            onClick={() => handleDelete(mapping.id)}
                          >
                            <BiTrash className="h-5 w-5" />
                          </ActionIcon>
                        </div>
                      </div>
                    </div>
                  ))
                ) : (
                  <div className="flex flex-col items-center justify-center py-20">
                    {/* Pulse Icon */}
                    <div className="mb-6">
                      <svg
                        width="120"
                        height="120"
                        viewBox="0 0 120 120"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                        className="text-gray-300"
                      >
                        <path
                          d="M20 60 L35 60 L45 40 L55 80 L65 50 L75 60 L100 60"
                          stroke="currentColor"
                          strokeWidth="4"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          fill="none"
                        />
                      </svg>
                    </div>

                    {/* Message */}
                    <h3 className="text-xl font-semibold text-gray-800 mb-2">
                      No Device Mappings
                    </h3>
                    <p className="text-gray-500 mb-6">
                      This employee has no device assignments yet.
                    </p>

                    {/* Assign First Device Button */}
                    <Button
                      leftSection={<span className="text-lg">+</span>}
                      size="md"
                      className="bg-orange-500 hover:bg-orange-600"
                      onClick={() => setAction('create')}
                    >
                      Assign First Device
                    </Button>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className="flex flex-col items-center justify-center py-20 mt-6">
              {/* Illustration */}
              <div className="mb-6">
                <svg
                  width="200"
                  height="200"
                  viewBox="0 0 200 200"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                  className="text-gray-300"
                >
                  {/* Device Icon */}
                  <rect
                    x="60"
                    y="50"
                    width="80"
                    height="100"
                    rx="8"
                    stroke="currentColor"
                    strokeWidth="3"
                    fill="none"
                  />
                  <circle
                    cx="100"
                    cy="100"
                    r="20"
                    fill="currentColor"
                    opacity="0.2"
                  />
                  <circle
                    cx="100"
                    cy="100"
                    r="12"
                    fill="currentColor"
                    opacity="0.3"
                  />

                  {/* User Icon */}
                  <circle
                    cx="100"
                    cy="85"
                    r="8"
                    stroke="currentColor"
                    strokeWidth="2"
                    fill="none"
                  />
                  <path
                    d="M85 110 Q85 100 100 100 Q115 100 115 110"
                    stroke="currentColor"
                    strokeWidth="2"
                    fill="none"
                  />
                </svg>
              </div>

              {/* Message */}
              <h3 className="text-2xl font-semibold text-gray-700 mb-3">
                No Employee Selected
              </h3>
              <p className="text-gray-500 text-center max-w-md mb-2">
                Please select an employee from the dropdown above to view and
                manage their device assignments.
              </p>
              <p className="text-sm text-gray-400">
                You can also assign a device without selecting an employee
                first.
              </p>
            </div>
          )}
        </div>
      </Paper>

      {/* Modal for Assign Device Form */}
      <Modal
        opened={action !== null}
        onClose={() => {
          setAction(null);
          setSelectedMapping(null);
        }}
        size="lg"
        title=""
      >
        <AssignDeviceForm
          action={action}
          data={selectedMapping}
          onClose={() => {
            setAction(null);
            setSelectedMapping(null);
          }}
          selectedEmployeeId={
            selectedEmployee?.id ? String(selectedEmployee.id) : undefined
          }
        />
      </Modal>
    </div>
  );
}
