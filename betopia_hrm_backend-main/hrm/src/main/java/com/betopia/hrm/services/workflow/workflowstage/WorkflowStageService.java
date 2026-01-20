package com.betopia.hrm.services.workflow.workflowstage;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ModuleDTO;
import com.betopia.hrm.domain.dto.workflow.WorkflowStageDTO;
import com.betopia.hrm.domain.workflow.request.ModuleRequest;
import com.betopia.hrm.domain.workflow.request.WorkflowStageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowStageService {

    PaginationResponse<WorkflowStageDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowStageDTO> getAll();

    WorkflowStageDTO getById(Long id);

    WorkflowStageDTO store(WorkflowStageRequest request);

    WorkflowStageDTO update(Long id, WorkflowStageRequest request);

    void delete(Long id);
}
