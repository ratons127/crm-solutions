import { Department, DepartmentUpdate } from '@/lib/types/admin/company';
import { baseApi } from '../../baseAPI';

const tag = `department`;

export const departmentAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // create
    createDepartment: builder.mutation<Department, Partial<Department>>({
      query: body => {
        return {
          method: 'POST',
          url: `/v1/${tag}`,
          body,
        };
      },
      invalidatesTags: [tag],
    }),
    deleteDepartment: builder.mutation<Department, { id: number }>({
      query: ({ id }) => {
        return {
          method: 'DELETE',
          url: `/v1/${tag}/${id}`,
        };
      },
      invalidatesTags: [tag],
    }),
    updateDepartment: builder.mutation<
      Department,
      { id: number; data: DepartmentUpdate }
    >({
      query: ({ id, data }) => {
        return {
          method: 'PUT',
          url: `/v1/${tag}/${id}`,
          body: data,
        };
      },
      invalidatesTags: [tag],
    }),

    // getDepartmentList
    getDepartmentList: builder.query<{ data: Department[] }, {}>({
      query() {
        return {
          method: 'GET',
          url: `/v1/${tag}/all`,
        };
      },
      providesTags: [tag],
    }),

    // End of API
  }),
});

export const {
  useCreateDepartmentMutation,
  useGetDepartmentListQuery,
  useDeleteDepartmentMutation,
  useUpdateDepartmentMutation,
} = departmentAPI;
