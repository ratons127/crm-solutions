package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeEducationInfo;
import com.betopia.hrm.domain.employee.request.EmployeeEducationInfoRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmployeeEducationInfoService {

    PaginationResponse<EmployeeEducationInfo> index(Sort.Direction direction, int page, int perPage);

    List<EmployeeEducationInfo> getAllEmployeeEducationInfo();

    EmployeeEducationInfo store(EmployeeEducationInfoRequest request);

    EmployeeEducationInfo show(Long employeeEducationInfoId);

    EmployeeEducationInfo update(Long employeeEducationInfoId, EmployeeEducationInfoRequest request);

    void delete(Long employeeEducationInfoId);
}
