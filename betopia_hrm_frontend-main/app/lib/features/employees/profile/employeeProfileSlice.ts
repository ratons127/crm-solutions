import { useAppSelector } from '@/lib/hooks';
import { EmployeeUpdate } from '@/lib/types/employee/index';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

const initialState: { personal: EmployeeUpdate } = {
  personal: {
    firstName: '',
    lastName: '',
    dob: '',
    gender: '',
    maritalStatus: '',
    religionId: '',
    nationalityId: '',
    bloodGroupId: '',
    businessUnitId: '',
    departmentId: '',
    designationId: '',
    dateOfJoining: '',
    employeeTypeId: '',
    photo: '',
    email: '',
    phone: '',
    nationalId: '',
    companyId: '',
    workPlaceId: '',
    supervisorId: '',
    roleId: '',
    workplaceGroupId: '',
    teamId: '',
    presentAddress: '',
    permanentAddress: '',
    lineManagerId: '',
    jobTitle: '',
    gradeId: '',
  },
  // familyInformation: {
  //   spouseName: '',
  //   numberOfChildren: 0,
  //   members: [
  //     {
  //       name: '',
  //       numberOfChildren: 0,
  //       relationship: '',
  //     },
  //   ],
  // },
  // hobbies: [] as string[],
  // contactInformation: {
  //   personalEmail: '',
  //   officeEmail: '',
  //   mobileNo: '',
  //   officePhoneNo: '',
  // },
  // socialMediaInformation: {
  //   facebook: '',
  //   twitter: '',
  //   linkedin: '',
  //   instagram: '',
  //   whatsapp: '',
  // },
  // presentAddress: {
  //   country: '',
  //   division: '',
  //   district: '',
  //   policeStation: '',
  //   postOffice: '',
  //   postCode: '',
  //   address: '',
  // },
  // permanentAddress: {
  //   country: '',
  //   division: '',
  //   district: '',
  //   policeStation: '',
  //   postOffice: '',
  //   postCode: '',
  //   address: '',
  // },
  // employeeDetails: {
  //   employeeId: '',
  //   employeeType: '',
  //   dateOfJoining: null,
  //   probationDuration: '',
  //   estimatedConfirmationDate: null,
  //   actualConfirmationDate: null,
  //   jobGrade: '',
  //   designation: '',
  //   paymentType: '',
  // },
  // bankingInformation: {
  //   bankName: '',
  //   bankBranch: '',
  //   accountNumber: '',
  //   routingNumber: '',
  // },
  // organizationalStructure: {
  //   companyName: '',
  //   businessUnit: '',
  //   workplaceGroup: '',
  //   workplace: '',
  //   department: '',
  //   team: '',
  // },
  // // promotionHistory: [
  // //   {
  // //     companyName: '',
  // //     businessUnit: '',
  // //     workplaceGroup: '',
  // //     workplace: '',
  // //     department: '',
  // //     team: '',
  // //   },
  // // ],
  // // transferHistory: [
  // //   {
  // //     companyName: '',
  // //     businessUnit: '',
  // //     workplaceGroup: '',
  // //     workplace: '',
  // //     department: '',
  // //     team: '',
  // //   },
  // // ],
  // // officialDocuments: {
  // //   nationalId: '',
  // //   birthCertificate: '',
  // //   passportNumber: '',
  // //   taxIdentificationNumber: '',
  // //   drivingLicense: '',
  // // },
  // workExperience: [
  //   {
  //     companyName: '',
  //     jobTitle: '',
  //     location: '',
  //     fromDate: null,
  //     toDate: null,
  //     tenure: '',
  //     jobDescription: '',
  //     files: [] as string[],
  //   },
  // ],
  // educationQualifications: [
  //   {
  //     qualificationType: '',
  //     qualification: '',
  //     instituteName: '',
  //     fieldOfStudy: '',
  //     subject: '',
  //     ratingMethod: '',
  //     result: '',
  //     passingYear: '',
  //     duration: '',
  //     remarks: '',
  //     isLastDegree: false,
  //   },
  // ],
  // professionalCertifications: [
  //   {
  //     certificationName: '',
  //     issuingOrganization: '',
  //     issueDate: null,
  //     expirationDate: null,
  //     credentialId: '',
  //     credentialUrl: '',
  //     description: '',
  //     noExpiration: false,
  //   },
  // ],
};
const slice = createSlice({
  name: 'employeeProfile',
  initialState,
  reducers: {
    setState: (state, action: PayloadAction<Partial<typeof initialState>>) => {
      return { ...state, ...action.payload };
    },
  },
});

export const { setState } = slice.actions;
export const useEmployeeProfile = () =>
  useAppSelector(state => state.employeeProfile);
export const employeeProfileReducer = slice.reducer;
