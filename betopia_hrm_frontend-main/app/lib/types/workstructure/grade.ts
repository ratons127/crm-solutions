import { DBModel, Simplify } from '..';

export type GradeCreate = {
  code: string;
  name: string;
  description: string | null;
  status: boolean;
};

export type GradeUpdate = Partial<GradeCreate>;
export type Grade = Simplify<GradeCreate & DBModel>;
