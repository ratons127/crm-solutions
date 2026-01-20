package com.betopia.hrm.domain.employee.mapper;

import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.repository.BusinessUnitRepository;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.company.repository.TeamRepository;
import com.betopia.hrm.domain.company.repository.WorkplaceGroupRepository;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.entity.Grade;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import com.betopia.hrm.domain.employee.repository.GradeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeRequest;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.betopia.hrm.domain.lookup.repository.LookupSetupDetailsRepository;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final WorkplaceRepository workplaceRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final GradeRepository gradeRepository;
    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final DesignationRepository designationRepository;
    private final TeamRepository teamRepository;
    private final LookupSetupDetailsRepository lookupSetupDetailsRepository;

    public EmployeeMapper(CompanyRepository companyRepository, DepartmentRepository departmentRepository, WorkplaceRepository workplaceRepository, EmployeeTypeRepository employeeTypeRepository, GradeRepository gradeRepository, WorkplaceGroupRepository workplaceGroupRepository, BusinessUnitRepository businessUnitRepository, DesignationRepository designationRepository, TeamRepository teamRepository, LookupSetupDetailsRepository lookupSetupDetailsRepository) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.workplaceRepository = workplaceRepository;
        this.employeeTypeRepository = employeeTypeRepository;
        this.gradeRepository = gradeRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.designationRepository = designationRepository;
        this.teamRepository = teamRepository;
        this.lookupSetupDetailsRepository = lookupSetupDetailsRepository;
    }


    /**
     * Map CreateRequest DTO -> Entity
     */
    public Employee toEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        entityDto(employee, request);
        return employee;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void entityDto(Employee employee, EmployeeRequest request) {
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setPresentAddress(request.presentAddress());
        employee.setPermanentAddress(request.permanentAddress());
        employee.setMaritalStatus(request.maritalStatus());
        employee.setEmergencyContactName(request.emergencyContactName());
        employee.setEmergencyContactRelation(request.emergencyContactRelation());
        employee.setPhone(request.phone());
        employee.setPhoto(request.photo());
        employee.setImageUrl(request.imageUrl());
        employee.setGender(request.gender());
        employee.setDateOfJoining(request.dateOfJoining());
        employee.setSupervisorId(request.supervisorId());
        employee.setJobTitle(request.jobTitle());
        employee.setLineManagerId(request.lineManagerId());
        employee.setDeviceUserId(request.deviceUserId());
        employee.setGrossSalary(request.grossSalary());
        employee.setDob(request.dob());
        employee.setEmergencyContactPhone(request.emergencyContactPhone());
        employee.setNationalId(request.nationalId());


        employee.setBirthCertificateNumber(request.birthCertificateNumber());
        employee.setPassportNumber(request.passportNumber());
        employee.setDrivingLicenseNumber(request.drivingLicenseNumber());
        employee.setTinNumber(request.tinNumber());

        employee.setOfficeEmail(request.officeEmail());
        employee.setOfficePhone(request.officePhone());

        employee.setEstimatedConfirmationDate(request.estimatedConfirmationDate());
        employee.setActualConfirmationDate(request.actualConfirmationDate());

        /////////////
        if (request.religionId() != null) {
            // This line fetches a LookupSetupDetails object
            LookupSetupDetails religionDetail = lookupSetupDetailsRepository.findById(request.religionId())
                    .orElseThrow(() -> new RuntimeException("Religion lookup detail not found for id: " + request.religionId()));
            employee.setReligion(religionDetail);
        } else {
            employee.setReligion(null);
        }

        if (request.nationalityId() != null) {
            LookupSetupDetails nationalityDetail = lookupSetupDetailsRepository.findById(request.nationalityId())
                    .orElseThrow(() -> new RuntimeException("Nationality lookup detail not found for id: " + request.nationalityId()));
            employee.setNationality(nationalityDetail);
        } else {
            employee.setNationality(null);
        }

        if (request.bloodGroupId() != null) {
            LookupSetupDetails bloodGroupDetail = lookupSetupDetailsRepository.findById(request.bloodGroupId())
                    .orElseThrow(() -> new RuntimeException("Blood Group lookup detail not found for id: " + request.bloodGroupId()));
            employee.setBloodGroup(bloodGroupDetail);
        } else {
            employee.setBloodGroup(null);
        }

        if (request.paymentTypeId() != null) {
            LookupSetupDetails paymentTypeDetail = lookupSetupDetailsRepository.findById(request.paymentTypeId())
                    .orElseThrow(() -> new RuntimeException("Payment Type lookup detail not found for id: " + request.paymentTypeId()));
            employee.setPaymentType(paymentTypeDetail);
        } else {
            employee.setPaymentType(null);
        }

        if (request.probationDurationId() != null) {
            LookupSetupDetails probationDurationDetail = lookupSetupDetailsRepository.findById(request.probationDurationId())
                    .orElseThrow(() -> new RuntimeException("Payment Type lookup detail not found for id: " + request.probationDurationId()));
            employee.setProbationDuration(probationDurationDetail);
        } else {
            employee.setProbationDuration(null);
        }

        /////////////

        if(request.employeeTypeId()!=null) {
            EmployeeType employeeType = employeeTypeRepository.findById(request.employeeTypeId())
                    .orElseThrow(() -> new RuntimeException("EmployeeTypes not found: " + request.employeeTypeId()));
            employee.setEmployeeType(employeeType);
        }
        else
            employee.setEmployeeType(null);


        if(request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found: " + request.departmentId()));
            employee.setDepartment(department);
        }
        else
            employee.setDepartment(null);


        if(request.designationId() != null) {
            Designation designation = designationRepository.findById(request.designationId())
                    .orElseThrow(() -> new RuntimeException("Designation not found: " + request.designationId()));
            employee.setDesignation(designation);
        }
        else
            employee.setDesignation(null);

        if(request.gradeId() != null) {
            Grade grade = gradeRepository.findById(request.gradeId())
                    .orElseThrow(() -> new RuntimeException("Grade not found: " + request.gradeId()));
            employee.setGrade(grade);
        }
        else
            employee.setGrade(null);

        if(request.companyId() != null){
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new RuntimeException("Company not found: " + request.companyId()));
            employee.setCompany(company);
        }
        else
            employee.setCompany(null);

        if(request.workPlaceId() != null) {
            Workplace workplace = workplaceRepository.findById(request.workPlaceId())
                    .orElseThrow(() -> new RuntimeException("Workplace not found: " + request.workPlaceId()));
            employee.setWorkplace(workplace);
        }
        else
            employee.setWorkplace(null);

        if(request.workPlaceGroupId() != null) {
            WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workPlaceGroupId())
                    .orElseThrow(() -> new RuntimeException("WorkplaceGroup not found: " + request.workPlaceGroupId()));
            employee.setWorkplaceGroup(workplaceGroup);
        }
        else
            employee.setWorkplaceGroup(null);

        if(request.businessUnitId() != null) {
            BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                    .orElseThrow(() -> new RuntimeException("BusinessUnit not found: " + request.businessUnitId()));
            employee.setBusinessUnit(businessUnit);
        }
        else
            employee.setBusinessUnit(null);

        if(request.teamId() != null) {
            Team team = teamRepository.findById(request.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found: " + request.teamId()));
            employee.setTeam(team);
        }
        else
            employee.setTeam(null);

    }
}
