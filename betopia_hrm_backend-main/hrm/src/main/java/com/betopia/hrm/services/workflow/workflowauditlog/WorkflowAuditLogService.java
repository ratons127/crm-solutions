package com.betopia.hrm.services.workflow.workflowauditlog;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowAuditLogDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowAuditLogRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowAuditLogService {

    PaginationResponse<WorkflowAuditLogDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowAuditLogDTO> getAll();

    WorkflowAuditLogDTO getById(Long id);

    WorkflowAuditLogDTO store(WorkflowAuditLogRequest request);

    WorkflowAuditLogDTO update(Long id, WorkflowAuditLogRequest request);

    void delete(Long id);
}
