package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.FieldStudy;
import com.betopia.hrm.domain.employee.request.FieldStudyRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface FieldStudyService {

    PaginationResponse<FieldStudy> index(Sort.Direction direction, int page, int perPage);

    List<FieldStudy> getAllFieldStudy();

    FieldStudy store(FieldStudyRequest request);

    FieldStudy show(Long fieldStudyId);

    FieldStudy update(Long fieldStudyId, FieldStudyRequest request);

    void delete(Long fieldStudyId);
}
