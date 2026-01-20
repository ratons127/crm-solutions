package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.request.EmployeeTypeRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmployeeTypeService {

    PaginationResponse<EmployeeType> index(Sort.Direction direction, int page, int perPage);

    List<EmployeeType> getAllEmployeeTypes();

    EmployeeType insert(EmployeeTypeRequest request);

    EmployeeType show(Long employeeTypesId);

    EmployeeType update(Long employeeTypesId, EmployeeTypeRequest request);

    void delete(Long employeeTypesId);
}
