package com.betopia.hrm.services.employee.accessrevocationlog;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.AccessRevocationLogDTO;
import com.betopia.hrm.domain.employee.request.AccessRevocationLogRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AccessRevocationLogService {

    PaginationResponse<AccessRevocationLogDTO> index(Sort.Direction direction, int page, int perPage);

    List<AccessRevocationLogDTO> getAll();

    AccessRevocationLogDTO store(AccessRevocationLogRequest request);

    AccessRevocationLogDTO show(Long id);

    AccessRevocationLogDTO update(Long id, AccessRevocationLogRequest request);

    void destroy(Long id);
}
