package com.betopia.hrm.services.company;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.WorkingType;
import com.betopia.hrm.domain.company.request.WorkingTypeRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface WorkingTypeService {

    PaginationResponse<WorkingType> index(Sort.Direction direction, int page, int perPage);

    List<WorkingType> getAllWorkingType();

    WorkingType insert(WorkingTypeRequest request);

    WorkingType show(Long workingTypeId);

    WorkingType update(Long workingTypeId, WorkingTypeRequest request);

    void delete(Long workingTypeId);
}
