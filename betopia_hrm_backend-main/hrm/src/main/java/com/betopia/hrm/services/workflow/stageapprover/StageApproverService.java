package com.betopia.hrm.services.workflow.stageapprover;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.StageApproverDTO;
import com.betopia.hrm.domain.workflow.request.StageApproverRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface StageApproverService {

    PaginationResponse<StageApproverDTO> index(Sort.Direction direction, int page, int perPage);

    List<StageApproverDTO> getAll();

    StageApproverDTO getById(Long id);

    StageApproverDTO store(StageApproverRequest request);

    StageApproverDTO update(Long id, StageApproverRequest request);

    void delete(Long id);
}
