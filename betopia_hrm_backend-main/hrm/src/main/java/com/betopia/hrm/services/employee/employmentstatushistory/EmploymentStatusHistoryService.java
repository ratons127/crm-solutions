package com.betopia.hrm.services.employee.employmentstatushistory;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmploymentStatusHistory;
import com.betopia.hrm.domain.employee.request.EmploymentStatusHistoryRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmploymentStatusHistoryService {

    PaginationResponse<EmploymentStatusHistory> index(Sort.Direction direction, int page, int perPage);

    List<EmploymentStatusHistory> getAllEmploymentStatusHistories();

    EmploymentStatusHistory getEmploymentStatusHistoryById(Long id);

    EmploymentStatusHistory store(EmploymentStatusHistoryRequest request);

    EmploymentStatusHistory updateEmploymentStatusHistory(Long id, EmploymentStatusHistoryRequest request);

    void deleteEmploymentStatusHistory(Long id);
}
