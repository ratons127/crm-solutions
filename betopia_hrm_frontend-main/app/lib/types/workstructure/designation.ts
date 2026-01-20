import { DBModel, Simplify } from '..';

export type DesignationCreate = {
  name: string;
  description: string | null;
  status: boolean;
};

export type DesignationUpdate = Partial<DesignationCreate>;
export type Designation = Simplify<DesignationCreate & DBModel>;
