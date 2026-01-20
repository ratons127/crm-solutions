package com.betopia.hrm.services.employee.employmentstatus;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmploymentStatus;
import com.betopia.hrm.domain.employee.request.EmploymentStatusRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmploymentStatusService {

    PaginationResponse<EmploymentStatus> index(Sort.Direction direction, int page, int perPage);

    List<EmploymentStatus> getAllEmploymentStatuses();

    EmploymentStatus getEmploymentStatusById(Long id);

    EmploymentStatus store(EmploymentStatusRequest request);

    EmploymentStatus updateEmploymentStatus(Long id, EmploymentStatusRequest request);

    void deleteEmploymentStatus(Long id);
}
