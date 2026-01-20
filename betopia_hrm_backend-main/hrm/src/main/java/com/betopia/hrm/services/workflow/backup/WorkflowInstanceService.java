package com.betopia.hrm.services.workflow.backup;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowInstanceRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowInstanceService {

    PaginationResponse<WorkflowInstanceDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowInstanceDTO> getAll();

    WorkflowInstanceDTO getById(Long id);

    WorkflowInstanceDTO store(WorkflowInstanceRequest request);

    WorkflowInstanceDTO update(Long id, WorkflowInstanceRequest request);

    void delete(Long id);
}
