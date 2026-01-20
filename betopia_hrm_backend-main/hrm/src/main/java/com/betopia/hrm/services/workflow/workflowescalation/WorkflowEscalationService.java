package com.betopia.hrm.services.workflow.workflowescalation;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowEscalationDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowEscalationRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowEscalationService {

    PaginationResponse<WorkflowEscalationDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowEscalationDTO> getAll();

    WorkflowEscalationDTO getById(Long id);

    WorkflowEscalationDTO store(WorkflowEscalationRequest request);

    WorkflowEscalationDTO update(Long id, WorkflowEscalationRequest request);

    void delete(Long id);
}
