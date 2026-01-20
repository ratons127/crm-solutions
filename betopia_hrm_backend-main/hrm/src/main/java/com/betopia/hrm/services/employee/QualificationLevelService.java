package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationLevel;
import com.betopia.hrm.domain.employee.request.QualificationLevelRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface QualificationLevelService {

    PaginationResponse<QualificationLevel> index(Sort.Direction direction, int page, int perPage);

    List<QualificationLevel> getAllQualificationLevel();

    QualificationLevel store(QualificationLevelRequest request);

    QualificationLevel show(Long qualificationLevelId);

    QualificationLevel update(Long qualificationLevelId, QualificationLevelRequest request);

    void delete(Long qualificationLevelId);
}
