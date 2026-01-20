import { DBModel, ForeignKeyId, Simplify } from '..';

// company > business unit > workplace_group > workplace > department > team

export interface CompanyCreate {
  name: string;
  countryId: ForeignKeyId;
  divisionId: ForeignKeyId;
  districtId: ForeignKeyId;
  Integer: number;
  thana: number;
  postOffice: number;
  shortName: string;
  code: string;
  phone: string;
  email: string;
  websiteUrl: string;
  description: string;
  address: string;
  image: string;
  imageUrl: string;
  status: string;
}

export type CompanyUpdate = Partial<CompanyCreate>;
export type Company = Simplify<DBModel & CompanyCreate>;

export interface BusinessUnitCreate {
  company: number;
  name: string;
  code: string;
  description: string;
  status: string;
}

export type BusinessUnitUpdate = Partial<BusinessUnitCreate>;
export type BusinessUnit = Simplify<DBModel & BusinessUnitCreate>;

export interface WorkplaceGroupCreate {
  businessUnitId: number;
  name: string;
  code: string;
  description: string;
  status: string;
}

export type WorkplaceGroupUpdate = Partial<WorkplaceGroupCreate>;
export type WorkplaceGroup = Simplify<DBModel & WorkplaceGroupCreate>;

export interface WorkplaceCreate {
  workplaceGroupId: number;
  name: string;
  code: string;
  address: string;
  description: string;
  status: string;
}

export type WorkplaceUpdate = Partial<WorkplaceCreate>;
export type Workplace = Simplify<DBModel & WorkplaceCreate>;

export interface DepartmentCreate {
  WorkplaceId: number;
  name: string;
  code: string;
  description: string;
  status: string;
}

export type DepartmentUpdate = Partial<DepartmentCreate>;
export type Department = Simplify<DBModel & DepartmentCreate>;

export interface TeamCreate {
  departmentId: number;
  name: string;
  code: string;
  description: string;
  status: string;
}

export type TeamUpdate = Partial<TeamCreate>;
export type Team = Simplify<DBModel & TeamCreate>;
