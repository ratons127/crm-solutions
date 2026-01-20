package com.betopia.hrm.services.employee.handoverchecklist;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.HandoverChecklistDTO;
import com.betopia.hrm.domain.employee.request.HandoverChecklistRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface HandoverChecklistService {

    PaginationResponse<HandoverChecklistDTO> index(Sort.Direction direction, int page, int perPage);

    List<HandoverChecklistDTO> getAll();

    HandoverChecklistDTO show(Long id);

    HandoverChecklistDTO update(Long id, HandoverChecklistRequest request);

    void destroy(Long id);
    HandoverChecklistDTO store(HandoverChecklistRequest request);

}
