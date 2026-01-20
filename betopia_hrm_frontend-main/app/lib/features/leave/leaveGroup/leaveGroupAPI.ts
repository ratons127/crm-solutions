import { notifications } from '@mantine/notifications';
import apiClient from '../../../../services/apiClient';
import { AppDispatch } from '../../../store';
import { LeaveGroupInput, LeaveGroupType } from '../../../types/leave';
import {
  failureCreatingleaveGroup,
  failureDeletingleaveGroup,
  failureUpdatingleaveGroup,
  fetchleaveGroupFailure,
  fetchleaveGroupStart,
  fetchleaveGroupSuccess,
  startCreatingleaveGroup,
  startDeletingleaveGroup,
  startUpdatingleaveGroup,
  successCreatingleaveGroup,
  successDeletingleaveGroup,
  successUpdatingleaveGroup,
} from './leaveGroupeSlice';

//

export const fetchLeaveGroup = () => async (dispatch: AppDispatch) => {
  dispatch(fetchleaveGroupStart());
  try {
    const { data } = await apiClient.get<{ data: LeaveGroupType[] }>(
      '/leave-groups/all'
    );

    dispatch(fetchleaveGroupSuccess(data.data));
  } catch (err: any) {
    dispatch(
      fetchleaveGroupFailure(
        err.response?.data?.message || 'Failed fetching leave types'
      )
    );
  }
};

export const getLeaveGroupList = async () => {
  const { data } = await apiClient.get<{ data: LeaveGroupType[] }>(
    '/leave-groups/all'
  );
  return data.data;
};

export const createLeaveGroup =
  (createData: LeaveGroupInput) => async (dispatch: AppDispatch) => {
    dispatch(startCreatingleaveGroup());
    try {
      const { data } = await apiClient.post<{ data: LeaveGroupType }>(
        '/leave-groups/save',
        createData
      );
      // //  console.log(data);
      notifications.show({
        title: `Create`,
        message: `Leave group created successfully`,
      });

      dispatch(successCreatingleaveGroup(data.data));
    } catch (err: any) {
      // //  console.log(err);

      notifications.show({
        title: `Create`,
        message: `Leave group creation failed`,
      });

      dispatch(
        failureCreatingleaveGroup(
          err.response?.data?.message || 'Failed creating leave type'
        )
      );
    }
  };
export const updateLeaveGroup =
  (id: LeaveGroupType['id'], updatedData: Partial<LeaveGroupType>) =>
  async (dispatch: AppDispatch) => {
    dispatch(startUpdatingleaveGroup());
    try {
      const { data } = await apiClient.put<{ data: LeaveGroupType }>(
        `/leave-groups/update/${id}`,
        updatedData
      );
      notifications.show({
        title: 'Update',
        message: `Leave year updated successfully`,
      });
      dispatch(successUpdatingleaveGroup(data.data));
    } catch (err: any) {
      notifications.show({
        title: 'Update failed ',
        message: `Leave year update failed`,
        color: 'red',
      });
      // //  console.log(err);
      dispatch(
        failureUpdatingleaveGroup(
          err.response?.data?.message || 'Failed updating leave type'
        )
      );
    }
  };

export const deleteLeaveGroup =
  (id: LeaveGroupType['id']) => async (dispatch: AppDispatch) => {
    dispatch(startDeletingleaveGroup());
    try {
      await apiClient.delete(`/leave-groups/delete/${id}`);
      notifications.show({
        title: 'Deleted',
        message: 'Leave group deleted.',
        color: 'red',
      });
      dispatch(successDeletingleaveGroup(id));
    } catch (err: any) {
      notifications.show({
        title: 'Delete',
        message: `Leave year delete failed`,
      });
      // //  console.log('GOT_DELETING_ERROR');
      dispatch(
        failureDeletingleaveGroup(
          err.response?.data?.message || 'Failed creating leave type'
        )
      );
    }
  };
