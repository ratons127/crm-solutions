package com.betopia.hrm.services.employee.separationaudittrail;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationAuditTrailDTO;
import com.betopia.hrm.domain.employee.request.SeparationAuditTrailRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SeparationAuditTrailService {


    PaginationResponse<SeparationAuditTrailDTO> index(Sort.Direction direction, int page, int perPage);

    List<SeparationAuditTrailDTO> getAll();

    SeparationAuditTrailDTO store(SeparationAuditTrailRequest request);

    SeparationAuditTrailDTO show(Long id);

    SeparationAuditTrailDTO update(Long id,  SeparationAuditTrailRequest request);

    void destroy(Long id);
}
