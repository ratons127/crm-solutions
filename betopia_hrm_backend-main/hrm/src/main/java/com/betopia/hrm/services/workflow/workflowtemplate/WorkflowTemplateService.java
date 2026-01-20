package com.betopia.hrm.services.workflow.workflowtemplate;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowTemplateDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowTemplateRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowTemplateService {

    PaginationResponse<WorkflowTemplateDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowTemplateDTO> getAll();

    WorkflowTemplateDTO getById(Long id);

    WorkflowTemplateDTO store(WorkflowTemplateRequest request);

    WorkflowTemplateDTO update(Long id, WorkflowTemplateRequest request);

    void delete(Long id);
}
