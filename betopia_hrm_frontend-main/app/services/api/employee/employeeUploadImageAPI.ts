// src/services/api/employee/employeeUploadImageAPI.ts
import apiClient from '../../../services/apiClient';

export const uploadEmployeeImage = async (employeeId: number, file: File) => {
  const formData = new FormData();
  formData.append('file', file);

  const { data } = await apiClient.post<{ data: any }>(
    `/employees/${employeeId}/upload-image`,
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' },
    }
  );

  return data?.data;
};
