package com.betopia.hrm.services.company.department;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.request.DepartmentRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DepartmentService {

    PaginationResponse<Department> index(Sort.Direction direction, int page, int perPage);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Department store(DepartmentRequest request);

    Department updateDepartment(Long id, DepartmentRequest request);

    void deleteDepartment(Long id);

}
