package com.betopia.hrm.services.employee.employeeassignmenthistory.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeAssignmentHistory;
import com.betopia.hrm.domain.employee.exception.employeeassignmenthistory.EmployeeAssignmentHistoryNotFound;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeAssignmentHistoryRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeAssignmentHistoryRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.employeeassignmenthistory.EmployeeAssignmentHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeAssignmentHistoryServiceImpl implements EmployeeAssignmentHistoryService {

    private final EmployeeAssignmentHistoryRepository employeeAssignmentHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public EmployeeAssignmentHistoryServiceImpl(EmployeeAssignmentHistoryRepository employeeAssignmentHistoryRepository,
                                                EmployeeRepository employeeRepository, DesignationRepository designationRepository,
                                                DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.employeeAssignmentHistoryRepository = employeeAssignmentHistoryRepository;
        this.employeeRepository = employeeRepository;
        this.designationRepository = designationRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<EmployeeAssignmentHistory> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<EmployeeAssignmentHistory> employeeAssignmentHistoryPage = employeeAssignmentHistoryRepository.findAll(pageable);
        List<EmployeeAssignmentHistory> history = employeeAssignmentHistoryPage.getContent();

        PaginationResponse<EmployeeAssignmentHistory> response = new PaginationResponse<>();
        response.setData(history);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Employee AssignmentHistory fetched successfully");

        Links links = Links.fromPage(employeeAssignmentHistoryPage, "/employee-assignments");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeAssignmentHistoryPage, "/employee-assignments");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeAssignmentHistory> getAll() {
        return employeeAssignmentHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmployeeAssignmentHistory store(EmployeeAssignmentHistoryRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));

        Department department = departmentRepository.findById(request.toDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("To Department not found with id: " + request.toDepartmentId()));

        Designation designation = designationRepository.findById(request.fromDesignationId())
                .orElseThrow(() -> new RuntimeException("From Designation not found with id: " + request.fromDesignationId()));

        User approver = userRepository.findById(request.approvedById())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + request.approvedById()));

        EmployeeAssignmentHistory history = new EmployeeAssignmentHistory();
        history.setEmployee(employee);
        history.setChangeType(request.changeType());
        history.setFromDepartment(department);
        history.setToDepartment(department);
        history.setFromDesignation(designation);
        history.setToDesignation(designation);
        history.setEffectiveDate(request.effectiveDate());
        history.setApprovedBy(approver);
        history.setReferenceId(request.referenceId());
        history.setRemarks(request.remarks());
        history.setStatus(request.status());

        return employeeAssignmentHistoryRepository.save(history);
    }

    @Override
    public EmployeeAssignmentHistory show(Long id) {
        return employeeAssignmentHistoryRepository.findById(id)
                .orElseThrow(() -> new EmployeeAssignmentHistoryNotFound("Employee Assignment History not found with id: " + id));
    }

    @Override
    public EmployeeAssignmentHistory update(Long id, EmployeeAssignmentHistoryRequest request) {
        EmployeeAssignmentHistory history = employeeAssignmentHistoryRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Employee Assignment History not found with id: " + id));

        history.setEmployee(request.employeeId() != null ? employeeRepository.findById(request.employeeId()).orElse(null) :
                history.getEmployee());

        history.setFromDepartment(request.fromDepartmentId() != null ? departmentRepository.
                findById(request.fromDepartmentId()).orElse(null) : history.getFromDepartment());

        history.setToDepartment(request.toDepartmentId() != null ? departmentRepository.
                findById(request.toDepartmentId()).orElse(null) : history.getToDepartment());

        history.setFromDesignation(request.fromDesignationId()!= null ? designationRepository.
                findById(request.fromDesignationId()).orElse(null) : history.getFromDesignation());

        history.setToDesignation(request.employeeId() != null ? designationRepository.findById(request.toDesignationId()).orElse(null) :
                history.getToDesignation());

        history.setApprovedBy(request.approvedById()!= null ? userRepository.
                findById(request.approvedById()).orElse(null) : history.getApprovedBy());

        history.setChangeType(request.changeType() != null ? request.changeType() : history.getChangeType());
        history.setEffectiveDate(request.effectiveDate() != null ? request.effectiveDate() : history.getEffectiveDate());
        history.setReferenceId(request.referenceId() != null ? request.referenceId() : history.getReferenceId());
        history.setRemarks(request.remarks() != null ? request.remarks() : history.getRemarks());
        history.setStatus(request.status() != null ? request.status() : history.getStatus());
        history.setLastModifiedDate(LocalDateTime.now());

        return employeeAssignmentHistoryRepository.save(history);
    }

    @Override
    public void destroy(Long id) {
        EmployeeAssignmentHistory history = employeeAssignmentHistoryRepository.findById(id)
                .orElseThrow(() -> new EmployeeAssignmentHistoryNotFound("Employee Assignment History not found with id: " + id));
        employeeAssignmentHistoryRepository.delete(history);
    }
}
