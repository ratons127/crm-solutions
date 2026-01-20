package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.company.BusinessUnitDTO;
import com.betopia.hrm.domain.dto.company.CompanyDTO;
import com.betopia.hrm.domain.dto.company.DepartmentDTO;
import com.betopia.hrm.domain.dto.company.TeamDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceGroupDTO;
import com.betopia.hrm.domain.dto.lookup.LookupDetailsDTO;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class EmployeeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfJoining;
    private LocalDate dob;
    private String photo;
    private String imageUrl;
    private String nationalId;
    private String phone;
    private String email;
    private String presentAddress;
    private String permanentAddress;
    private String maritalStatus;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
    private Long employeeTypeId;
    private DepartmentDTO department;
    private DesignationDTO designation;
    private Long supervisorId;
    private WorkplaceDTO workplace;
    private Long employeeSerialId;
    private BusinessUnitDTO businessUnit;
    private WorkplaceGroupDTO workplaceGroup;
    private Long gradeId;
    private Long lineManagerId;
    private CompanyDTO company;
    private String jobTitle;
    private TeamDTO team;
    private String deviceUserId;
    private BigDecimal grossSalary;

    // Lookup references
    private LookupDetailsDTO religionId;
    private LookupDetailsDTO nationalityId;
    private LookupDetailsDTO bloodGroupId;
    private LookupDetailsDTO paymentTypeId;
    private LookupDetailsDTO probationDurationId;

    // Official document numbers
    private String birthCertificateNumber;
    private String passportNumber;
    private String drivingLicenseNumber;
    private String tinNumber;
    private String officePhone;
    private String officeEmail;
    private String estimatedConfirmationDate;
    private String actualConfirmationDate;
    private String displayName;

    private String designationName;

    public LookupDetailsDTO getProbationDurationId() {
        return probationDurationId;
    }

    public void setProbationDurationId(LookupDetailsDTO probationDurationId) {
        this.probationDurationId = probationDurationId;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public String getEstimatedConfirmationDate() {
        return estimatedConfirmationDate;
    }

    public void setEstimatedConfirmationDate(String estimatedConfirmationDate) {
        this.estimatedConfirmationDate = estimatedConfirmationDate;
    }

    public String getActualConfirmationDate() {
        return actualConfirmationDate;
    }

    public void setActualConfirmationDate(String actualConfirmationDate) {
        this.actualConfirmationDate = actualConfirmationDate;
    }


    // New getter/setter for displayName
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LookupDetailsDTO getReligionId() {
        return religionId;
    }

    public void setReligionId(LookupDetailsDTO religionId) {
        this.religionId = religionId;
    }

    public LookupDetailsDTO getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(LookupDetailsDTO nationalityId) {
        this.nationalityId = nationalityId;
    }

    public LookupDetailsDTO getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(LookupDetailsDTO bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public LookupDetailsDTO getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(LookupDetailsDTO paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(String birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public Long getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Long employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }


    public Long getEmployeeSerialId() {
        return employeeSerialId;
    }

    public void setEmployeeSerialId(Long employeeSerialId) {
        this.employeeSerialId = employeeSerialId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getLineManagerId() {
        return lineManagerId;
    }

    public void setLineManagerId(Long lineManagerId) {
        this.lineManagerId = lineManagerId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDeviceUserId() {
        return deviceUserId;
    }

    public void setDeviceUserId(String deviceUserId) {
        this.deviceUserId = deviceUserId;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public DesignationDTO getDesignation() {
        return designation;
    }

    public void setDesignation(DesignationDTO designation) {
        this.designation = designation;
    }

    public WorkplaceDTO getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceDTO workplace) {
        this.workplace = workplace;
    }

    public BusinessUnitDTO getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnitDTO businessUnit) {
        this.businessUnit = businessUnit;
    }

    public WorkplaceGroupDTO getWorkplaceGroup() {
        return workplaceGroup;
    }

    public void setWorkplaceGroup(WorkplaceGroupDTO workplaceGroup) {
        this.workplaceGroup = workplaceGroup;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }
}
