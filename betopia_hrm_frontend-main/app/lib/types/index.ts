export interface DBModel {
  id: DBModelId;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: number;
  lastModifiedBy: number;
}

export type DBModelId = number;

export type ForeignKeyId = DBModel['id'] | string;

export type Simplify<T> = {
  [K in keyof T]: T[K] extends object
    ? T[K] extends Function
      ? T[K] // keep functions as-is
      : Simplify<T[K]> | null // recurse for nested objects
    : T[K];
};

export type ErrorResponse = {
  status: number;
  data: {
    message: string;
  };
};

export type StatusString = 'active' | 'inactive';

// Generic Pagination Types
export interface PaginationParams {
  page?: number;
  perPage?: number;
  sortDirection?: 'ASC' | 'DESC';
}

export interface PaginationMeta {
  currentPage: number;
  from: number;
  lastPage: number;
  path: string;
  perPage: number;
  to: number;
  total: number;
}

export interface PaginatedResponse<T> {
  data: T[];
  meta: PaginationMeta;
}

export type LookupData = {
  id: number;
  name: string;
  details: string;
};
