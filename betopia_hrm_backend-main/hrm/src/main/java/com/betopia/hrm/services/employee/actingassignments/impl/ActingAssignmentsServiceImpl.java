package com.betopia.hrm.services.employee.actingassignments.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.employee.entity.ActingAssignments;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.exception.actingassignments.ActingAssignmentsNotFoundException;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.repository.ActingAssignmentsRepository;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.request.ActingAssignmentsRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.base.BaseService;
import com.betopia.hrm.services.employee.actingassignments.ActingAssignmentsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActingAssignmentsServiceImpl implements ActingAssignmentsService {

    private final ActingAssignmentsRepository actingAssignmentsRepository;
    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public ActingAssignmentsServiceImpl(ActingAssignmentsRepository actingAssignmentsRepository, EmployeeRepository employeeRepository,
                                       DesignationRepository designationRepository, DepartmentRepository departmentRepository,
                                        UserRepository userRepository) {
        this.actingAssignmentsRepository = actingAssignmentsRepository;
        this.employeeRepository = employeeRepository;
        this.designationRepository = designationRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;

    }

    @Override
    public PaginationResponse<ActingAssignments> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<ActingAssignments> actingAssignmentsPage = actingAssignmentsRepository.findAll(pageable);
        List<ActingAssignments> assignments = actingAssignmentsPage.getContent();

        PaginationResponse<ActingAssignments> response = new PaginationResponse<>();
        response.setData(assignments);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Acting Assignments fetched successfully");

        Links links = Links.fromPage(actingAssignmentsPage, "/acting-assignments");
        response.setLinks(links);

        Meta meta = Meta.fromPage(actingAssignmentsPage, "/acting-assignments");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ActingAssignments> getAll() {
        return actingAssignmentsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public ActingAssignments store(ActingAssignmentsRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));

        Department department = departmentRepository.findById(request.fromDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.fromDepartmentId()));

        Designation actingRole = designationRepository.findById(request.actingRoleId())
                .orElseThrow(() -> new RuntimeException("Acting Role not found with id: " + request.actingRoleId()));

        User approver = userRepository.findById(request.approvedById())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + request.approvedById()));

        ActingAssignments assignment = new ActingAssignments();
        assignment.setEmployee(employee);
        assignment.setActingRole(actingRole);
        assignment.setFromDepartment(department);
        assignment.setToDepartment(department);
        assignment.setActingSupervisor(employee);
        assignment.setAssignmentType(request.assignmentType());
        assignment.setStartDate(request.startDate());
        assignment.setEndDate(request.endDate());
        assignment.setRemarks(request.remarks());
        assignment.setApprovalStatus(request.approvalStatus());
        assignment.setApprovedBy(approver);
        assignment.setApprovedAt(request.approvedAt());
        assignment.setStatus(request.status());

        return actingAssignmentsRepository.save(assignment);
    }

    @Override
    public ActingAssignments show(Long id) {
        return actingAssignmentsRepository.findById(id)
                .orElseThrow(() -> new ActingAssignmentsNotFoundException("Acting Assignments not found with id: " + id));
    }

    @Override
    public ActingAssignments update(Long id, ActingAssignmentsRequest request) {
        ActingAssignments assignment = actingAssignmentsRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Acting Assignments not found with id: " + id));

        assignment.setEmployee(request.employeeId() != null ? employeeRepository.findById(request.employeeId()).orElse(null) :
                assignment.getEmployee());

        assignment.setActingRole(request.actingRoleId() != null ? designationRepository.findById(request.actingRoleId()).orElse(null) :
                assignment.getActingRole());

        assignment.setFromDepartment(request.fromDepartmentId() != null ? departmentRepository.
                findById(request.fromDepartmentId()).orElse(null) : assignment.getFromDepartment());

        assignment.setToDepartment(request.toDepartmentId() != null ? departmentRepository.
                findById(request.toDepartmentId()).orElse(null) : assignment.getToDepartment());

        assignment.setActingSupervisor(request.actingSupervisorId()!= null ? employeeRepository.
                findById(request.actingSupervisorId()).orElse(null) : assignment.getActingSupervisor());

        assignment.setApprovedBy(request.approvedById()!= null ? userRepository.
                findById(request.approvedById()).orElse(null) : assignment.getApprovedBy());

        assignment.setAssignmentType(request.assignmentType() != null ? request.assignmentType() : assignment.getAssignmentType());
        assignment.setStartDate(request.startDate() != null ? request.startDate() : assignment.getStartDate());
        assignment.setEndDate(request.endDate() != null ? request.endDate() : assignment.getEndDate());
        assignment.setApprovalStatus(request.approvalStatus() != null ? request.approvalStatus() : assignment.getApprovalStatus());
        assignment.setApprovedAt(request.approvedAt() != null ? request.approvedAt() : assignment.getApprovedAt());
        assignment.setStatus(request.status() != null ? request.status() : assignment.getStatus());
        assignment.setLastModifiedDate(LocalDateTime.now());

        return actingAssignmentsRepository.save(assignment);
    }

    @Override
    public void destroy(Long id) {
        ActingAssignments assignment = actingAssignmentsRepository.findById(id)
                .orElseThrow(() -> new ActingAssignmentsNotFoundException("Acting Assignments not found with id: " + id));
        actingAssignmentsRepository.delete(assignment);
    }
}
