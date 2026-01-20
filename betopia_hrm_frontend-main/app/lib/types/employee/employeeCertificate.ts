import { ForeignKeyId } from '..';

export interface EmployeeCertificate {
  id?: ForeignKeyId;
  employeeId: ForeignKeyId;
  documentTypeId: ForeignKeyId;
  issueDate: string;
  expiryDate: string;
  documentStatus: string;
  status: boolean | string;
  file?: File | null;
}
