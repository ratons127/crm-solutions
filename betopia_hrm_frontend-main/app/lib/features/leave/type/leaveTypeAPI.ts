import { notifications } from '@mantine/notifications';
import apiClient from '../../../../services/apiClient';
import { AppDispatch } from '../../../store';
import { LeaveType, LeaveTypeCreate } from '../../../types/leave';
import {
  failureCreatingLeaveType,
  failureDeletingLeaveType,
  failureUpdatingLeaveType,
  fetchLeaveTypeFailure,
  fetchLeaveTypeStart,
  fetchLeaveTypeSuccess,
  startCreatingLeaveType,
  startDeletingLeaveType,
  startUpdatingLeaveType,
  successCreatingLeaveType,
  successDeletingLeaveType,
  successUpdatingLeaveType,
} from './leaveTypeSlice';

// Dummy data fallback (unique ids)
// const leaveTypeList = (length: number = 20) =>
//     Array.from({ length }).map((_, idx) => {
//         const randomValue = Math.random();
//         return {
//             id: idx + 1,
//             name: `Leave name ${idx + 1}`,
//             active: randomValue > 0.5 ? true : false,
//             code: randomValue > 0.3 ? "AL" : "CL",
//             created_at: faker.date
//                 .between({
//                     from: new Date("09-09-25"),
//                     to: new Date("09-20-2025"),
//                 })
//                 .toISOString(),
//             updated_at: faker.date.anytime().toISOString(),
//             deleted_at: null,
//         };
//     });

export const fetchLeaveTypes = () => async (dispatch: AppDispatch) => {
  dispatch(fetchLeaveTypeStart());
  try {
    const { data } = await apiClient.get<{ data: LeaveType[] }>(
      '/leave-types/all'
    );

    dispatch(fetchLeaveTypeSuccess(data.data));
  } catch (err: any) {
    dispatch(
      fetchLeaveTypeFailure(
        err.response?.data?.message || 'Failed fetching leave types'
      )
    );
  }
};
export const getLeaveTypeList = async () => {
  const { data } = await apiClient.get<{ data: LeaveType[] }>(
    '/leave-types/all'
  );
  return data.data;
};

export const createLeaveType =
  (createData: LeaveTypeCreate) => async (dispatch: AppDispatch) => {
    dispatch(startCreatingLeaveType());
    try {
      const { data } = await apiClient.post<{ data: LeaveType }>(
        '/leave-types/save',
        createData
      );
      dispatch(successCreatingLeaveType(data.data));
    } catch (err: any) {
      notifications.show({
        title: `Failed`,
        message: `Leave type creation failed`,
        color: 'red',
      });
      dispatch(
        failureCreatingLeaveType(
          err.response?.data?.message || 'Failed creating leave type'
        )
      );
    }
  };
export const updateLeaveType =
  (id: LeaveType['id'], updatedData: Partial<LeaveType>) =>
  async (dispatch: AppDispatch) => {
    dispatch(startUpdatingLeaveType());
    try {
      const { data } = await apiClient.put<{ data: LeaveType }>(
        `/leave-types/update/${id}`,
        updatedData
      );
      dispatch(successUpdatingLeaveType(data?.data));
    } catch (err: any) {
      // //  console.log(err);
      notifications.show({
        title: `Failed`,
        message: `Leave Type failed`,
        color: 'red',
      });
      dispatch(
        failureUpdatingLeaveType(
          err.response?.data?.message || 'Failed updating leave type'
        )
      );
    }
  };

export const deleteLeaveType =
  (id: LeaveType['id']) => async (dispatch: AppDispatch) => {
    dispatch(startDeletingLeaveType());
    try {
      await apiClient.delete(`/leave-types/delete/${id}`);
      notifications.show({
        title: 'Deleted',
        message: 'Leave Type deleted.',
        color: 'red',
      });
      dispatch(successDeletingLeaveType(id));
    } catch (err: any) {
      // //  console.log('GOT_DELETING_ERROR');
      dispatch(
        failureDeletingLeaveType(
          err.response?.data?.message || 'Failed creating leave type'
        )
      );
    }
  };
