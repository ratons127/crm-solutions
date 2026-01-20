package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.repository.BusinessUnitRepository;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.company.repository.TeamRepository;
import com.betopia.hrm.domain.company.repository.WorkplaceGroupRepository;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeShiftAssignDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeUploadDTO;
import com.betopia.hrm.domain.dto.employee.mapper.EmployeeDTOMapper;
import com.betopia.hrm.domain.dto.employee.mapper.EmployeeShiftAssignDTOMapper;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.entity.Grade;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.mapper.EmployeeMapper;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import com.betopia.hrm.domain.employee.repository.GradeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeRequest;
import com.betopia.hrm.domain.employee.request.EmployeeShiftAssignRequest;
import com.betopia.hrm.domain.employee.specification.EmployeeSpecification;
import com.betopia.hrm.domain.leave.repository.LeaveYearRepository;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.betopia.hrm.domain.lookup.repository.LookupSetupDetailsRepository;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.exception.role.RoleNotFoundException;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.RoleRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.employee.EmployeeService;
import com.betopia.hrm.services.leaves.leavebalanceemployee.LeaveBalanceEmployeeService;
import com.betopia.hrm.webapp.util.AuthUtils;
import com.betopia.hrm.webapp.util.S3Service;
import com.betopia.hrm.webapp.util.UploadFiles;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final WorkplaceRepository workplaceRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final GradeRepository gradeRepository;
    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final DesignationRepository designationRepository;
    private final TeamRepository teamRepository;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final EmployeeShiftAssignDTOMapper employeeShiftAssignDTOMapper;
    private final LeaveBalanceEmployeeService leaveBalanceEmployeeService;
    private final LeaveYearRepository leaveYearRepository;
    private final ExcelHelper excelHelper;
//    private final LookupSetupDetailsRepository lookupSetupDetailsRepository;
    private final LookupSetupDetailsRepository lookupRepository;
    @Value("${user.default.password}")
    private String userDefaultPassword;
    private final UploadFiles uploadFiles;
    private final S3Service s3Service;
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class.getName());


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                               UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                               CompanyRepository companyRepository, DepartmentRepository departmentRepository,
                               WorkplaceRepository workplaceRepository, EmployeeTypeRepository employeeTypeRepository,
                               GradeRepository gradeRepository, WorkplaceGroupRepository workplaceGroupRepository,
                               BusinessUnitRepository businessUnitRepository, DesignationRepository designationRepository,
                               TeamRepository teamRepository, EmployeeDTOMapper employeeDTOMapper,
                               EmployeeShiftAssignDTOMapper employeeShiftAssignDTOMapper,
                               LeaveBalanceEmployeeService leaveBalanceEmployeeService,
                               LeaveYearRepository leaveYearRepository, LookupSetupDetailsRepository lookupSetupDetailsRepository,
                               ExcelHelper excelHelper, LookupSetupDetailsRepository lookupRepository,UploadFiles uploadFiles,S3Service s3Service) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.workplaceRepository = workplaceRepository;
        this.employeeTypeRepository = employeeTypeRepository;
        this.gradeRepository = gradeRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.designationRepository = designationRepository;
        this.teamRepository = teamRepository;
        this.employeeDTOMapper = employeeDTOMapper;
        this.employeeShiftAssignDTOMapper = employeeShiftAssignDTOMapper;
        this.leaveBalanceEmployeeService = leaveBalanceEmployeeService;
        this.leaveYearRepository = leaveYearRepository;
        this.excelHelper = excelHelper;
        this.lookupRepository = lookupRepository;
        this.uploadFiles = uploadFiles;
        this.s3Service = s3Service;
    }

    @Override
    public PaginationResponse<EmployeeDTO> index(Sort.Direction direction, int page, int perPage, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Specification<Employee> spec =
                EmployeeSpecification.globalSearch(keyword);

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        List<Employee> employees = employeePage.getContent();
        List<EmployeeDTO> advanceCashRequestDTOs = employeeDTOMapper.toDTOList(employees);

        PaginationResponse<EmployeeDTO> response = new PaginationResponse<>();

        response.setData(advanceCashRequestDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All employee fetch successful");

        Links links = Links.fromPage(employeePage, "/employees");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeePage, "/employees");
        response.setMeta(meta);

        return response;
    }

    @Override
    public PaginationResponse<EmployeeDTO> searchEmployees(Long departmentId, Long designationId, Long workplaceId, Long workPlaceGroupId, Long companyId, Long gradeId, Long employeeTypeId, String employeeName, Pageable pageable) {

        Specification<Employee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (departmentId != null)
                predicates.add(cb.equal(root.get("department").get("id"), departmentId));

            if (designationId != null)
                predicates.add(cb.equal(root.get("designation").get("id"), designationId));

            if (workplaceId != null)
                predicates.add(cb.equal(root.get("workplace").get("id"), workplaceId));

            if (workPlaceGroupId != null)
                predicates.add(cb.equal(root.get("workPlaceGroup").get("id"), workPlaceGroupId));

            if (companyId != null)
                predicates.add(cb.equal(root.get("company").get("id"), companyId));

            if (gradeId != null)
                predicates.add(cb.equal(root.get("grade").get("id"), gradeId));

            if (employeeTypeId != null)
                predicates.add(cb.equal(root.get("employeeType").get("id"), employeeTypeId));

            if (employeeName != null && !employeeName.trim().isEmpty())
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + employeeName.toLowerCase() + "%"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        List<Employee> employees = employeePage.getContent();

        List<EmployeeDTO> advanceCashRequestDTOs = employeeDTOMapper.toDTOList(employees);
        PaginationResponse<EmployeeDTO> response = new PaginationResponse<>();

        response.setData(advanceCashRequestDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Employee fetch successful");

        Links links = Links.fromPage(employeePage, "/employees");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeePage, "/employees");
        response.setMeta(meta);

        return response;
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {

        return employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream().map(employeeDTOMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public EmployeeDTO store(EmployeeRequest request) {
        Long empSerialId=null;
        Employee employee = employeeMapper.toEntity(request);
        //empSerialId = employSerialStringToLongConvert(request.employeeSerialId());
        empSerialId = employeeRepository.nextEmployeeSerial();
        employee.setEmployeeSerialId(empSerialId);
        Employee savedEmployee = employeeRepository.save(employee);
        insertUser(employee, request,empSerialId);

//        leaveAssign(employee);

        return employeeDTOMapper.toDTO(savedEmployee);
    }

    private void leaveAssign(Employee employee) {
        Integer leaveYear = leaveYearRepository.findCurrentLeaveYear(LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No Leave Year found for current date"));

        leaveBalanceEmployeeService.initializeYearlyBalances(employee.getId(), leaveYear);
    }

    private void insertUser(Employee employee, EmployeeRequest request, Long empSerialId) {

        User user = new User();
        user.setEmail(employee.getEmail());
        user.setActive(true);
        user.setUserType("Employee");
        user.setEmployeeSerialId(Math.toIntExact(empSerialId));
        user.setPhone(employee.getPhone());
        user.setName(employee.getFirstName());
        user.setPassword(passwordEncoder.encode(userDefaultPassword));

        user.setRoles(roleRepository.findById(request.roleId()).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + request.roleId())));

        userRepository.save(user);
    }

    @Override
    public EmployeeDTO show(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with id: " + employeeId));

        return employeeDTOMapper.toDTO(employee);
    }

    @Override
    public EmployeeDTO update(Long employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with id: " + employeeId));

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


        if (request.religionId() != null) {
            LookupSetupDetails lookupSetupDetails = lookupRepository.findById(request.religionId())
                    .orElseThrow(() -> new RuntimeException("LookupSetupDetails Religion not found: " + request.religionId()));
            employee.setReligion(lookupSetupDetails);
        }

        if (request.nationalityId() != null) {
            LookupSetupDetails lookupSetupDetails = lookupRepository.findById(request.nationalityId())
                    .orElseThrow(() -> new RuntimeException("LookupSetupDetails nationality not found: " + request.nationalityId()));
            employee.setNationality(lookupSetupDetails);
        }

        if (request.bloodGroupId() != null) {
            LookupSetupDetails lookupSetupDetails = lookupRepository.findById(request.bloodGroupId())
                    .orElseThrow(() -> new RuntimeException("LookupSetupDetails not found: " + request.bloodGroupId()));
            employee.setBloodGroup(lookupSetupDetails);
        }

        if (request.probationDurationId() != null) {
            LookupSetupDetails lookupSetupDetails = lookupRepository.findById(request.probationDurationId())
                    .orElseThrow(() -> new RuntimeException("LookupSetupDetails ProbationDurationId not found: " + request.probationDurationId()));
            employee.setReligion(lookupSetupDetails);
        }


        if (request.employeeTypeId() != null) {
            EmployeeType employeeTypes = employeeTypeRepository.findById(request.employeeTypeId())
                    .orElseThrow(() -> new RuntimeException("EmployeeTypes not found: " + request.employeeTypeId()));
            employee.setEmployeeType(employeeTypes);
        }


        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found: " + request.departmentId()));
            employee.setDepartment(department);
        }


        if (request.designationId() != null) {
            Designation designation = designationRepository.findById(request.designationId())
                    .orElseThrow(() -> new RuntimeException("Designation not found: " + request.designationId()));
            employee.setDesignation(designation);
        }

        if (request.gradeId() != null) {
            Grade grade = gradeRepository.findById(request.gradeId())
                    .orElseThrow(() -> new RuntimeException("Grade not found: " + request.gradeId()));
            employee.setGrade(grade);
        }

        if (request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new RuntimeException("Company not found: " + request.companyId()));
            employee.setCompany(company);
        }

        if (request.workPlaceId() != null) {
            Workplace workplace = workplaceRepository.findById(request.workPlaceId())
                    .orElseThrow(() -> new RuntimeException("Workplace not found: " + request.workPlaceId()));
            employee.setWorkplace(workplace);
        }

        if (request.workPlaceGroupId() != null) {
            WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workPlaceGroupId())
                    .orElseThrow(() -> new RuntimeException("WorkplaceGroup not found: " + request.workPlaceGroupId()));
            employee.setWorkplaceGroup(workplaceGroup);
        }

        if (request.businessUnitId() != null) {
            BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                    .orElseThrow(() -> new RuntimeException("BusinessUnit not found: " + request.businessUnitId()));
            employee.setBusinessUnit(businessUnit);
        }

        if (request.teamId() != null) {
            Team team = teamRepository.findById(request.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found: " + request.teamId()));
            employee.setTeam(team);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeDTOMapper.toDTO(updatedEmployee);
    }

    @Override
    public void delete(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with id: " + employeeId));

        employeeRepository.deleteById(employeeId);
    }

    public Long findReportingManagerId(Long employeeId) {
        return employeeRepository.findSupervisorIdByEmployeeId(employeeId)
                .map(Employee::getId)
                .orElseThrow(() -> new EntityNotFoundException("Supervisor not found for employee ID: " + employeeId));
    }

//    @Override
//    public List<EmployeeShiftAssignDTO> getAllEmployeeForShiftAssignments(EmployeeShiftAssignRequest employee) {
//
//        Specification<Employee> spec =
//                EmployeeSpecification.applicableForEmployee(employee);
//
//        return employeeRepository.findAll(spec).stream()
//                .map(employeeShiftAssignDTOMapper::toDTO)
//                .toList();
//    }

    @Override
    public List<EmployeeShiftAssignDTO> getAllEmployeeForShiftAssignments(EmployeeShiftAssignRequest request) {
        // Step 1: fetch logged-in supervisor
        User users = null;
        String identifier = AuthUtils.getCurrentUsername();

        if (identifier != null && !identifier.isEmpty()) {
            users = userRepository.findByEmailOrPhone(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));
        } else {
            throw new EntityNotFoundException("No authenticated user found for submission.");
        }

        Integer supervisorEmpSerialId = users.getEmployeeSerialId();

        // Step 3: Find the supervisor in Employee table
        Optional<Employee> optionalSupervisor = employeeRepository.findByEmployeeSerialId(supervisorEmpSerialId);

        // Step 4: fetch employees based on existing filters (Specification)
        Specification<Employee> spec = EmployeeSpecification.applicableForEmployee(request);
        List<Employee> employees = employeeRepository.findAll(spec);

        List<Employee> employeesToReturn;

        if (optionalSupervisor.isPresent()) {
            Employee supervisor = optionalSupervisor.get();
            System.err.println("Supervisor found: " + supervisor.getId());

            // Step 5: filter only employees under this supervisor
            employeesToReturn = employees.stream()
                    .filter(emp -> emp.getSupervisorId() != null &&
                            emp.getSupervisorId().equals(supervisor.getId()))
                    .toList();
        } else {
            // Step 6: no supervisor found → return all employees
            System.err.println("No supervisor found, returning all employees");
            employeesToReturn = employees;
        }

        // Step 7: map to DTO
        return employeesToReturn.stream()
                .map(employeeShiftAssignDTOMapper::toDTO)
                .toList();
    }


    @Override
    public Map<String, String> getSupervisorName() {
        Map<String, String> map = new HashMap<>();
        if (AuthUtils.getCurrentUsername() == null)
            throw new UsernameNotFoundException("User not found");

        User user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(p -> p.getEmail().equals(AuthUtils.getCurrentUsername()) || p.getPhone().equals(AuthUtils.getCurrentUsername()))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(user.getEmployeeSerialId()==null || user.getEmployeeSerialId()==0)
            throw new UsernameNotFoundException("Employee serial not found");

        Employee employee = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(p -> p.getEmployeeSerialId().equals(user.getEmployeeSerialId().longValue())).findFirst().orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        Long supervisorId = employee.getSupervisorId();
        if (supervisorId == null)
            throw new EmployeeNotFound("Supervisor not found");

        Employee empSupervisor = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(p -> p.getId().equals(supervisorId)).findFirst().orElseThrow(() -> new EmployeeNotFound("Supervisor employee not found"));

        map.put("firstName", empSupervisor.getFirstName());
        map.put("lastName", empSupervisor.getLastName());
        map.put("employeeSerialId", empSupervisor.getEmployeeSerialId().toString());
        map.put("imageUrl",empSupervisor.getImageUrl());
        return map;
    }

    private Long employSerialStringToLongConvert(String empSerial) {
        try {
          return Long.parseLong(empSerial);

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid employee serial id:"+e.getMessage());
        }
    }

    @Transactional
    @Override
    public void importEmployees(MultipartFile file) {
        try {
            List<EmployeeUploadDTO> dtos = excelHelper.excelToEmployeeDTOs(file.getInputStream());

            List<Employee> employees = dtos.stream()
                    .map(dto -> {
                        Employee emp = new Employee();
                        emp.setEmployeeSerialId(dto.getEmployeeId());
                        emp.setFirstName(dto.getEmployeeName());
                        emp.setDateOfJoining(dto.getDateOfJoining());
                        emp.setSupervisorId(dto.getSupervisorId());
                        emp.setActualConfirmationDate(getLocalDateTime(dto.getDateOfPermanent()));
                        emp.setPresentAddress(dto.getPresentAddress());
                        emp.setPermanentAddress(dto.getPermanentAddress());
                        emp.setEmail(dto.getEmployeeEmail());
                        emp.setOfficeEmail(dto.getOfficeEmail());
                        emp.setDob(dto.getDateOfBirth());
                        emp.setGender(dto.getGender());
                        emp.setMaritalStatus(dto.getMaritalStatus());
                        emp.setPhone(dto.getPersonalMobile());
                        emp.setNationalId(dto.getEmployeeNid());

                        emp.setEmployeeType(employeeTypeRepository.findFirstByNameIgnoreCase(dto.getEmploymentType())
                                .orElseThrow(() -> new RuntimeException("Employee type not found: " + dto.getEmploymentType())));

                        emp.setDesignation(designationRepository.findFirstByNameIgnoreCase(dto.getDesignation())
                                .orElseThrow(() -> new RuntimeException("Designation not found: " + dto.getDesignation())));

                        emp.setCompany(companyRepository.findFirstByNameIgnoreCase(dto.getCompany())
                                .orElseThrow(() -> new RuntimeException("Company not found: " + dto.getCompany())));

                        emp.setBusinessUnit(businessUnitRepository.findFirstByNameIgnoreCase(dto.getBusinessUnit())
                                .orElseThrow(() -> new RuntimeException("Business Unit not found: " + dto.getBusinessUnit())));

                        emp.setWorkplaceGroup(workplaceGroupRepository.findFirstByNameIgnoreCase(dto.getWorkplaceGroup())
                                .orElseThrow(() -> new RuntimeException("Workplace Group not found: " + dto.getWorkplaceGroup())));

                        emp.setWorkplace(workplaceRepository.findFirstByNameIgnoreCase(dto.getWorkplace())
                                .orElseThrow(() -> new RuntimeException("Workplace not found: " + dto.getWorkplace())));

                        emp.setDepartment(departmentRepository.findFirstByNameIgnoreCase(dto.getDepartment())
                                .orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDepartment())));

                        //Lookup
                        LookupSetupDetails religion = null;
                        if (dto.getReligion() != null && !dto.getReligion().trim().isEmpty()) {
                            religion = lookupRepository.findFirstByNameIgnoreCase(dto.getReligion())
                                    .orElseThrow(() -> new RuntimeException("Religion not found: " + dto.getReligion()));
                        }
                        emp.setReligion(religion);

                        LookupSetupDetails bloodGroup = null;
                        if (dto.getBloodGroup() != null && !dto.getBloodGroup().trim().isEmpty()) {
                            bloodGroup = lookupRepository.findFirstByNameIgnoreCase(dto.getBloodGroup())
                                    .orElseThrow(() -> new RuntimeException("Blood group not found: " + dto.getBloodGroup() + ", " + dto.getEmployeeId()));
                        }
                        emp.setBloodGroup(bloodGroup);

                        return emp;
                    })
                    .toList();

            employeeRepository.saveAll(employees);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store excel data: " + e.getMessage());
        }
    }

    private LocalDateTime getLocalDateTime(LocalDate date) {
        if (date == null) return null;
        return date.atStartOfDay(); // adds time = 00:00:00
    }

    @Override
    public ResponseEntity<GlobalResponse> uploadEmployeeImage(Long employeeId, MultipartFile file) {
        return uploadFiles.upload(file, "employees", (imageUrl) -> {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFound("employee not found " + employeeId));

            employee.setPhoto("employees/" + file.getOriginalFilename());
            employee.setImageUrl(imageUrl);
            return employeeDTOMapper.toDTO(employeeRepository.save(employee));
        });
    }

    @Override
    public ResponseEntity<GlobalResponse> deleteEmployeeImage(Long employeeId) {
        return uploadFiles.delete(
                employeeId,
                () -> employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new EmployeeNotFound("Employee not found " + employeeId)),

                (entity) -> {
                    Employee employee = (Employee) entity;

                    // delete from S3
                    if (employee.getPhoto() != null) {
                        try {
                            uploadFiles.deleteFromS3(employee.getPhoto());
                        } catch (Exception e) {
                            log.error("Failed to delete image from S3 for employee={}, image={}",
                                    employeeId, employee.getPhoto(), e);
                        }
                    }

                    // clear DB refs
                    employee.setPhoto(null);
                    employee.setImageUrl(null);
                    employeeRepository.save(employee);

                    log.info("Image deleted successfully for employeeId={}", employeeId);
                }
        );
    }
}
