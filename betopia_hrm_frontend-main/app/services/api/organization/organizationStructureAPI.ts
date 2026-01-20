'use client';
import { ForeignKeyId } from '@/lib/types';
import {
  BusinessUnit,
  Company,
  Department,
  Team,
  Workplace,
  WorkplaceGroup,
} from '@/lib/types/admin/company';
import { getFilteredParams } from '@/lib/utils/utils';
import { ApiResponse } from '@/types';
import { baseApi } from '../baseAPI';

const routePath = `hierarchy-filter`;

export const organizationStructureAPI = baseApi.injectEndpoints({
  endpoints: builder => ({
    // getLeaveTypeList
    getOrganizationStructureList: builder.query<
      ApiResponse<{
        companies: Company[];
        businessUnits: BusinessUnit[];
        workplaceGroups: WorkplaceGroup[];
        workplaces: Workplace[];
        departments: Department[];
        teams: Team[];
      }>,
      {
        companyId?: ForeignKeyId | null;
        businessUnitId?: ForeignKeyId | null;
        workplaceGroupId?: ForeignKeyId | null;
        workplaceId?: ForeignKeyId | null;
        departmentId?: ForeignKeyId | null;
      }
    >({
      query(params) {
        return {
          method: 'GET',
          url: `/v1/${routePath}`,
          params: getFilteredParams(params),
        };
      },
      providesTags: [routePath],
    }),

    // End of API
  }),
});

export const { useGetOrganizationStructureListQuery } =
  organizationStructureAPI;
