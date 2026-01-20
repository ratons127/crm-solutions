import { ForeignKeyId } from '..';

export interface EmployeeEducationalQualification {
  employeeId: ForeignKeyId;
  qualificationTypeId: ForeignKeyId;
  qualificationLevelId: ForeignKeyId;
  instituteNameId: ForeignKeyId;
  fieldStudyId: ForeignKeyId;
  qualificationRatingMethodId: ForeignKeyId;
  subject: string;
  result: string;
  passingYear: number;
}
