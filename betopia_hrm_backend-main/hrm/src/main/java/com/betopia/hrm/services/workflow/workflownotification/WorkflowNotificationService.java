package com.betopia.hrm.services.workflow.workflownotification;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ApprovalActionDTO;
import com.betopia.hrm.domain.dto.workflow.WorkflowNotificationDTO;
import com.betopia.hrm.domain.workflow.request.ApprovalActionRequest;
import com.betopia.hrm.domain.workflow.request.WorkflowNotificationRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkflowNotificationService {

    PaginationResponse<WorkflowNotificationDTO> index(Sort.Direction direction, int page, int perPage);

    List<WorkflowNotificationDTO> getAll();

    WorkflowNotificationDTO getById(Long id);

    WorkflowNotificationDTO store(WorkflowNotificationRequest request);

    WorkflowNotificationDTO update(Long id, WorkflowNotificationRequest request);

    void delete(Long id);
}
