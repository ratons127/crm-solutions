package com.betopia.hrm.services.company.workplaceGroup;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;

import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.request.WorkplaceGroupRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkplaceGroupService {

    PaginationResponse<WorkplaceGroup> index(Sort.Direction direction, int page, int perPage);

    List<WorkplaceGroup> getAllWorkplaceGroups();

    WorkplaceGroup store(WorkplaceGroupRequest request);

    WorkplaceGroup show(Long workplaceGroupId);

    WorkplaceGroup update(Long workplaceGroupId, WorkplaceGroupRequest request);

    void destroy(Long workplaceGroupId);
}
