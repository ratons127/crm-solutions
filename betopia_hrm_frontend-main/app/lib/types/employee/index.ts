import { DesignationRow } from '@/lib/features/employees/workstructure/designationSlice';
import { GradeRow } from '@/lib/features/employees/workstructure/gradeSlice';
import { RoleRow } from '@/lib/features/role/roleSlice';
import {
  DBModel,
  ForeignKeyId,
  LookupData,
  PaginatedResponse,
  Simplify,
} from '..';
import {
  BusinessUnit,
  Company,
  Department,
  Team,
  Workplace,
  WorkplaceGroup,
} from '../admin/company';

// Relation mapping with proper types for read operations
type RelationMap = {
  departmentId: Department | null;
  designationId: DesignationRow | null;
  workPlaceId: Workplace | null;
  businessUnitId: BusinessUnit | null;
  workplaceGroupId: WorkplaceGroup | null;
  gradeId: GradeRow | null;
  companyId: Company | null;
  roleId: RoleRow | null;
  teamId: Team | null;
  religionId: LookupData | null;
  nationalityId: LookupData | null;
  bloodGroupId: LookupData | null;
  paymentTypeId: LookupData | null;
  employeeTypeId: LookupData | null;
  supervisorId: Employee | null;
  probationDurationId: LookupData | null;
};

// Base structure with all scalar fields
interface EmployeeBase {
  firstName: string;
  lastName: string;
  gender: string;
  dateOfJoining: string;
  dob: string;
  photo: string;
  nationalId: string;
  phone: string;
  email: string;
  presentAddress: string;
  permanentAddress: string;
  maritalStatus: string;
  emergencyContactName: string;
  emergencyContactRelation: string;
  emergencyContactPhone: string;

  lineManagerId: ForeignKeyId;
  jobTitle: string;
  employeeSerialId: string;
  passportNumber: string;
  drivingLicenseNumber: string;
  tinNumber: string;
  officePhone: string;
  officeEmail: string;
  estimatedConfirmationDate: string;
  actualConfirmationDate: string;
  displayName: string;
  deviceUserId: string;
  grossSalary: number;
  birthCertificateNumber: number | null;
}

// Create type: uses ForeignKeyId, preserves nullability from RelationMap
export type EmployeeCreate = EmployeeBase & {
  [K in keyof RelationMap]:
    | ForeignKeyId
    | (null extends RelationMap[K] ? null : never);
};

// Update type: partial of create
export type EmployeeUpdate = Partial<EmployeeCreate>;

// Read type: uses populated objects from RelationMap
export type Employee = Simplify<EmployeeBase & RelationMap & DBModel>;
// Pagination type aliases
export type EmployeePaginatedResponse = PaginatedResponse<Employee>;
