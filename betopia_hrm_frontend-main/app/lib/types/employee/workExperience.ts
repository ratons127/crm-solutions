import { DBModel } from '..';

export interface EmployeeWorkExperienceCreate {
  id?: number;
  employeeId: number;
  companyName: string;
  jobTitle: string;
  location: string;
  jobDescription: string;
  fromDate: string;
  toDate: string;
  tenure: number;
}

export type EmployeeWorkExperienceUpdate =
  Partial<EmployeeWorkExperienceCreate>;

export type EmployeeWorkExperience = EmployeeWorkExperienceCreate & DBModel;
