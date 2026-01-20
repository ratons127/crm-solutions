'use client';

import { PaginationParams } from '@/lib/types';
import { MenuItem } from '@/lib/types/menu';
import { baseApi } from '@/services/api/baseAPI';

const routePath = 'menus';

export const menuAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // Create Menu
    createMenu: builder.mutation<MenuItem, Partial<MenuItem>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${routePath}`,
          body,
        };
      },
      invalidatesTags: ['menu'],
    }),

    // Update Menu

    updateMenu: builder.mutation<MenuItem, { id: number; data: MenuItem }>({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${routePath}/${id}`,
          body: data,
        };
      },
      invalidatesTags: ['menu'],
    }),
    getSidebarMenus: builder.query<MenuItem[], void>({
      query: () => ({
        method: 'GET',
        url: `/v1/${routePath}/all`,
      }),
      transformResponse: (response: { data?: MenuItem[] } | MenuItem[]) => {
        if (Array.isArray(response)) return response;
        return response?.data ?? [];
      },
      providesTags: ['menu'],
    }),

    // Get Paginated Menu
    getPaginatedMenu: builder.query<any, PaginationParams>({
      query: ({ page = 1, perPage = 10, sortDirection = 'DESC' }) => {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: {
            sortDirection,
            page,
            perPage,
          },
        };
      },
      providesTags: ['menu'],
    }),

    // Delete Menu
    deleteMenu: builder.mutation<any, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${routePath}/${id}`,
        };
      },
      invalidatesTags: ['menu'],
    }),
  }),
});

export const {
  useGetSidebarMenusQuery,
  useCreateMenuMutation,
  useUpdateMenuMutation,
  useGetPaginatedMenuQuery,
  useDeleteMenuMutation,
} = menuAPI;
