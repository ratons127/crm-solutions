package com.betopia.hrm.services.workflow.backup;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ApprovalActionDTO;
import com.betopia.hrm.domain.workflow.request.ApprovalActionRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ApprovalActionService {

    PaginationResponse<ApprovalActionDTO> index(Sort.Direction direction, int page, int perPage);

    List<ApprovalActionDTO> getAll();

    ApprovalActionDTO getById(Long id);

    ApprovalActionDTO store(ApprovalActionRequest request);

    ApprovalActionDTO update(Long id, ApprovalActionRequest request);

    void delete(Long id);
}
