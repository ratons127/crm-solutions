import { DBModel, ForeignKeyId, PaginatedResponse } from '.';

export interface Country extends DBModel {
  code: string;
  name: string;
  region: string;
}

export interface Bank extends DBModel {
  name: string;
  shortName: string;
  bankCode: string;
  swiftCode: string;
  country: Country;
  website: string;
  supportEmail: string;
  status: boolean;
}

export interface BankRequest {
  name: string;
  shortName: string;
  bankCode: string;
  swiftCode: string;
  countryId: ForeignKeyId;
  website: string;
  supportEmail: string;
  status: boolean | number;
  message?: string;
}

// Bank Branch related Types
export interface Location extends DBModel {
  countryId: ForeignKeyId;
  parentId: number;
  name: string;
  type: 'DIVISION' | 'DISTRICT' | 'CITY' | string;
  geoHash: string;
  status: boolean;
}

export interface BankBranch extends DBModel {
  bank: Bank;
  location: Location;
  branchName: string;
  branchCode: string;
  routingNo: string;
  swiftCode: string;
  email: string;
  addressLine1: string;
  addressLine2: string;
  district: string;
  status: boolean;
}

export interface BankBranchRequest {
  branchName: string;
  bankId: ForeignKeyId;
  locationId: ForeignKeyId;
  branchCode: string;
  routingNo: string;
  swiftCode: string;
  email: string;
  addressLine1: string;
  addressLine2: string;
  district: string;
  status: boolean | number;
  message?: string;
}

// Pagination type aliases
export type BankPaginatedResponse = PaginatedResponse<Bank>;
export type BankBranchPaginatedResponse = PaginatedResponse<BankBranch>;
