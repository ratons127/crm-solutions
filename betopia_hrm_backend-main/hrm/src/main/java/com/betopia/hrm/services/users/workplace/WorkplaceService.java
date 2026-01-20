package com.betopia.hrm.services.users.workplace;

import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.request.WorkplaceRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkplaceService {

    PaginationResponse<Workplace> index(Sort.Direction direction, int page, int perPage);

    List<Workplace> getAllWorkplaces();

    Workplace store(WorkplaceRequest request);

    Workplace show(Long workplaceId);

    Workplace update(Long workplaceId, WorkplaceRequest request);

    void destroy(Long workplaceId);
}
