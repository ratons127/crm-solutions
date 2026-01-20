package com.betopia.hrm.services.employee.exitclearanceitem;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitClearanceItemDTO;
import com.betopia.hrm.domain.employee.request.ExitClearanceItemRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExitClearanceItemService {

    PaginationResponse<ExitClearanceItemDTO> index(Sort.Direction direction, int page, int perPage);

    List<ExitClearanceItemDTO> getAll();

    ExitClearanceItemDTO store(ExitClearanceItemRequest request);

    ExitClearanceItemDTO show(Long id);

    ExitClearanceItemDTO update(Long id,  ExitClearanceItemRequest request);

    void destroy(Long id);
}
