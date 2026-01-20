package com.betopia.hrm.services.company.department.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.company.request.DepartmentRequest;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeNotFoundException;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.company.department.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final WorkplaceRepository WorkplaceRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, WorkplaceRepository WorkplaceRepository) {
        this.departmentRepository = departmentRepository;
        this.WorkplaceRepository = WorkplaceRepository;
    }

    @Override
    public PaginationResponse<Department> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        List<Department> departments = departmentPage.getContent();

        PaginationResponse<Department> response = new PaginationResponse<>();
        response.setData(departments);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All departments fetched successfully");

        Links links = Links.fromPage(departmentPage, "/leave-department");
        response.setLinks(links);

        Meta meta = Meta.fromPage(departmentPage, "/leave-department");
        response.setMeta(meta);

        return response;

    }


    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departments not found with id: " + id));
    }

    @Override
    public Department store(DepartmentRequest request) {
        // Create new department
        Department department = new Department();
        department.setName(request.name());

        department.setWorkplace(WorkplaceRepository.findById(request.WorkplaceId())
                .orElseThrow(() -> new LeaveTypeNotFoundException("Workplace group not found with id: " + request.WorkplaceId())));

        department.setCode(request.code());
        department.setDescription(request.description());
        department.setStatus(request.status());
        return departmentRepository.save(department);

    }

    @Override
    public Department updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setName(request.name() != null ? request.name() : department.getName());
        department.setWorkplace(request.WorkplaceId() != null ? WorkplaceRepository.findById(request.WorkplaceId()).orElse(null) : department.getWorkplace());

        department.setCode(request.code() != null ? request.code() : department.getCode());
        department.setDescription(request.description() != null ? request.description() : department.getDescription());
        department.setStatus(request.status() != null ? request.status() : department.getStatus());

        department.setLastModifiedDate(LocalDateTime.now());

        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setDeletedAt(LocalDateTime.now());

        departmentRepository.delete(department);
    }
}
