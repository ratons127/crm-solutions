import { notifications } from '@mantine/notifications';
import apiClient from '../../../../services/apiClient';
import { AppDispatch } from '../../../store';
import { LeaveYearType } from '../../../types/leave';

import {
  failureCreatingleaveYear,
  failureDeletingleaveYear,
  failureUpdatingleaveYear,
  fetchleaveYearFailure,
  fetchleaveYearStart,
  fetchleaveYearSuccess,
  startCreatingleaveYear,
  startDeletingleaveYear,
  startUpdatingleaveYear,
  successCreatingleaveYear,
  successDeletingleaveYear,
  successUpdatingleaveYear,
} from './leaveYearSlice';

//

export const fetchLeaveYear = () => async (dispatch: AppDispatch) => {
  dispatch(fetchleaveYearStart());
  try {
    const { data } = await apiClient.get<{ data: LeaveYearType[] }>(
      '/leave-years/all'
    );

    dispatch(fetchleaveYearSuccess(data.data));
  } catch (err: any) {
    dispatch(
      fetchleaveYearFailure(
        err.response?.data?.message || 'Failed fetching leave types'
      )
    );
  }
};

export const createLeaveYear =
  (createData: any) => async (dispatch: AppDispatch) => {
    dispatch(startCreatingleaveYear());
    try {
      const { data } = await apiClient.post<{ data: LeaveYearType }>(
        '/leave-years/save',
        createData
      );
      // //  console.log(data);

      notifications.show({
        title: 'Create',
        message: `Leave year created successfully`,
      });

      dispatch(successCreatingleaveYear(data.data));
    } catch (err: any) {
      // //  console.log(err);
      notifications.show({
        title: 'Create',
        message: `Leave year created failed`,
        color: 'red',
      });
      dispatch(
        failureCreatingleaveYear(
          err.response?.data?.message || 'Failed creating leave type'
        )
      );
    }
  };
export const updateLeaveYear =
  (id: LeaveYearType['id'], updatedData: Partial<LeaveYearType>) =>
  async (dispatch: AppDispatch) => {
    dispatch(startUpdatingleaveYear());
    try {
      const { data } = await apiClient.put<{ data: LeaveYearType }>(
        `/leave-years/update/${id}`,
        updatedData
      );
      notifications.show({
        title: 'Update',
        message: `Leave year updated successfully`,
      });
      dispatch(successUpdatingleaveYear(data.data));
    } catch (err: any) {
      // //  console.log(err);
      notifications.show({
        title: 'Update',
        message: `Leave year update failed`,
      });
      dispatch(
        failureUpdatingleaveYear(
          err.response?.data?.message || 'Failed updating leave year'
        )
      );
    }
  };

export const deleteLeaveYear =
  (id: LeaveYearType['id']) => async (dispatch: AppDispatch) => {
    dispatch(startDeletingleaveYear());
    try {
      await apiClient.delete(`/leave-years/delete/${id}`);
      notifications.show({
        title: 'Deleted',
        message: 'Leave year deleted.',
        color: 'red',
      });
      dispatch(successDeletingleaveYear(id));
    } catch (err: any) {
      // //  console.log('GOT_DELETING_ERROR');
      notifications.show({
        title: 'Delete',
        message: `Leave year delete failed`,
        color: 'red',
      });
      dispatch(
        failureDeletingleaveYear(
          err.response?.data?.message || 'Failed creating leave type'
        )
      );
    }
  };
