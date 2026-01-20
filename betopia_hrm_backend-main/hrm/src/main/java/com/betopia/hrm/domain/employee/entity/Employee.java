package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "employees")
public class Employee  extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;      //'male', 'female', 'other'

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;
    @Column(name = "dob")
    private LocalDate dob;
    @Column(name = "photo")
    String photo;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "national_id")
    String nationalId;
    @Column(name = "phone")
    String phone;
    @Column(name = "email")
    String email;
    @Column(name = "present_address")
    String presentAddress;
    @Column(name = "permanent_address")
    String permanentAddress;
    @Column(name = "marital_status")
    String maritalStatus;
    @Column(name = "emergency_contact_name")
    String emergencyContactName;
    @Column(name = "emergency_contact_relation")
    String emergencyContactRelation;
    @Column(name = "emergency_contact_phone")
    String emergencyContactPhone;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employment_type_id", updatable = false)
    private EmployeeType employeeType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "department_id", updatable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "designation_id", updatable = false)
    private Designation designation;

    @Column(name = "supervisor_id")
    private Long supervisorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "workplace_id", updatable = false)
    private Workplace workplace;


    @Column(name = "employee_serial_id",updatable = false, nullable = false, unique = true)
    private Long employeeSerialId; //System generated employee id

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "business_unit_id", updatable = false)
    private BusinessUnit businessUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "work_place_group_id", updatable = false)
    private WorkplaceGroup workplaceGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "grade_id", updatable = false)
    private Grade grade;

    @Column(name = "line_manager_id")
    private Long lineManagerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;

    @Column(name = "job_title")
    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "team_id", updatable = false)
    private Team team;

    @Column(name="device_user_id")
    private String deviceUserId;

    @Column(name="gross_salary")
    private BigDecimal grossSalary;

    // Lookup references
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "religion_id", updatable = false)
    private LookupSetupDetails religion;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "nationality_id")
    private LookupSetupDetails nationality;
//
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "blood_group_id", updatable = false)
    private LookupSetupDetails bloodGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "payment_type_id", updatable = false)
    private LookupSetupDetails paymentType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "probation_duration_id", updatable = false)
    private LookupSetupDetails probationDuration;

    // -----
    @Column(name = "birth_certificate_number")
    private String birthCertificateNumber;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "driving_license_number")
    private String drivingLicenseNumber;

    @Column(name = "tin_number")
    private String tinNumber;

    @Column(name = "office_phone")
    String officePhone;
    @Column(name = "office_email")
    String officeEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "shift_id")
    private Shift shift;


    @Column(name = "estimated_confirmation_date")
    private LocalDateTime estimatedConfirmationDate;

    @Column(name = "actual_confirmation_date")
    private LocalDateTime actualConfirmationDate;


    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public LookupSetupDetails getProbationDuration() {
        return probationDuration;
    }

    public void setProbationDuration(LookupSetupDetails probationDuration) {
        this.probationDuration = probationDuration;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public LocalDateTime getEstimatedConfirmationDate() {
        return estimatedConfirmationDate;
    }

    public void setEstimatedConfirmationDate(LocalDateTime estimatedConfirmationDate) {
        this.estimatedConfirmationDate = estimatedConfirmationDate;
    }

    public LocalDateTime getActualConfirmationDate() {
        return actualConfirmationDate;
    }

    public void setActualConfirmationDate(LocalDateTime actualConfirmationDate) {
        this.actualConfirmationDate = actualConfirmationDate;
    }

    public LookupSetupDetails getReligion() {
        return religion;
    }

    public void setReligion(LookupSetupDetails religion) {
        this.religion = religion;
    }


    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(String birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }
    public LookupSetupDetails getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(LookupSetupDetails paymentType) {
        this.paymentType = paymentType;
    }

    public LookupSetupDetails getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(LookupSetupDetails bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public LookupSetupDetails getNationality() {
        return nationality;
    }

    public void setNationality(LookupSetupDetails nationality) {
        this.nationality = nationality;
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

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public WorkplaceGroup getWorkplaceGroup() {
        return workplaceGroup;
    }

    public void setWorkplaceGroup(WorkplaceGroup workplaceGroup) {
        this.workplaceGroup = workplaceGroup;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

}
