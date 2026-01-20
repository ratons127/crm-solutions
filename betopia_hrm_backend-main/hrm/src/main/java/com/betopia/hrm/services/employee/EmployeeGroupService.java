package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeGroup;
import com.betopia.hrm.domain.employee.request.EmployeeGroupRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmployeeGroupService {

    PaginationResponse<EmployeeGroup> index(Sort.Direction direction, int page, int perPage);

    List<EmployeeGroup> getAllEmployeeGroups();

    EmployeeGroup store(EmployeeGroupRequest request);

    EmployeeGroup show(Long employeeGroupId);

    EmployeeGroup update(Long userId, EmployeeGroupRequest request);

    void destroy(Long employeeGroupId);
}
