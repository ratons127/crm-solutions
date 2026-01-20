import { DBModel, PaginatedResponse } from '.';

export interface ShiftCategory extends DBModel {
  name: string;
  type: string;
  description?: string;
  status: boolean;
}

export interface ShiftCategoryRequest {
  name: string;
  type: string;
  description?: string;
  status: boolean;
  message?: string;
}

// Pagination type alias
export type ShiftCategoryPaginatedResponse = PaginatedResponse<ShiftCategory>;
