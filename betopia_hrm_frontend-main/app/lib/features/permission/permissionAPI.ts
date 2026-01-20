'use client';

import { baseApi } from '@/services/api/baseAPI';

const routePath = 'permissions';

export interface Permission {
  id: number;
  name: string;
  guardName: string;
  createdDate?: string;
  lastModifiedDate?: string;
  createdBy?: number;
  lastModifiedBy?: number;
}

export interface PermissionsResponse {
  data: Permission[];
  meta?: {
    currentPage?: number;
    from?: number;
    lastPage?: number;
    path?: string;
    perPage?: number;
    to?: number;
    total?: number;
  };
  message?: string;
  success?: boolean;
}

export const permissionAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Get all permissions (no pagination)
    getAllPermissions: builder.query<Permission[], void>({
      query: () => ({
        method: 'GET',
        url: `/v1/${routePath}/all`,
      }),
      transformResponse: (response: PermissionsResponse | Permission[]) => {
        if (Array.isArray(response)) return response;
        return response?.data ?? [];
      },
      providesTags: ['permissions'],
    }),

    // Get paginated permissions
    getPaginatedPermissions: builder.query<
      PermissionsResponse,
      { page?: number; perPage?: number; sortDirection?: 'ASC' | 'DESC' }
    >({
      query: ({ page = 1, perPage = 20, sortDirection = 'DESC' }) => ({
        method: 'GET',
        url: `/v1/${routePath}`,
        params: {
          page,
          perPage,
          sortDirection,
        },
      }),
      providesTags: ['permissions'],
    }),

    // Get single permission by ID
    getPermissionById: builder.query<Permission, number>({
      query: id => ({
        method: 'GET',
        url: `/v1/${routePath}/${id}`,
      }),
      transformResponse: (
        response: { data?: Permission } | Permission
      ): Permission => {
        if ('data' in response && response.data) return response.data;
        return response as Permission;
      },
      providesTags: (_result, _error, id) => [{ type: 'permissions', id }],
    }),

    // Create permission
    createPermission: builder.mutation<
      Permission,
      { name: string; guardName?: string }
    >({
      query: body => ({
        method: 'POST',
        url: `/v1/${routePath}`,
        body,
      }),
      invalidatesTags: ['permissions'],
    }),

    // Update permission
    updatePermission: builder.mutation<
      Permission,
      { id: number; data: { name: string; guardName?: string } }
    >({
      query: ({ id, data }) => ({
        method: 'PUT',
        url: `/v1/${routePath}/${id}`,
        body: data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        'permissions',
        { type: 'permissions', id },
      ],
    }),

    // Delete permission
    deletePermission: builder.mutation<any, { id: number }>({
      query: ({ id }) => ({
        method: 'DELETE',
        url: `/v1/${routePath}/${id}`,
      }),
      invalidatesTags: ['permissions'],
    }),
  }),
});

export const {
  useGetAllPermissionsQuery,
  useGetPaginatedPermissionsQuery,
  useGetPermissionByIdQuery,
  useCreatePermissionMutation,
  useUpdatePermissionMutation,
  useDeletePermissionMutation,
} = permissionAPI;
