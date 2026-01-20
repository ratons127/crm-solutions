package com.betopia.hrm.services.workflow.workflowinstancestage;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceStageDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowInstanceStageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowInstanceStageService {

    PaginationResponse<WorkflowInstanceStageDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowInstanceStageDTO> getAll();

    WorkflowInstanceStageDTO getById(Long id);

    WorkflowInstanceStageDTO store(WorkflowInstanceStageRequest request);

    WorkflowInstanceStageDTO update(Long id, WorkflowInstanceStageRequest request);

    void delete(Long id);
}
