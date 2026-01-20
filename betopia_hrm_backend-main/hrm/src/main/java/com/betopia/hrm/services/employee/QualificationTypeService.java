package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationType;
import com.betopia.hrm.domain.employee.request.QualificationTypeRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface QualificationTypeService {

    PaginationResponse<QualificationType> index(Sort.Direction direction, int page, int perPage);

    List<QualificationType> getAllQualificationType();

    QualificationType store(QualificationTypeRequest request);

    QualificationType show(Long qualificationTypeId);

    QualificationType update(Long qualificationTypeId, QualificationTypeRequest request);

    void delete(Long qualificationTypeId);
}
