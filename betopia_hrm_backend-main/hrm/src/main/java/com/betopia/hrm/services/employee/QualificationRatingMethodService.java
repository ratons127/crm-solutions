package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationRatingMethod;
import com.betopia.hrm.domain.employee.request.MethodUpdateRequest;
import com.betopia.hrm.domain.employee.request.QualificationRatingMethodRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface QualificationRatingMethodService {

    PaginationResponse<QualificationRatingMethod> index(Sort.Direction direction, int page, int perPage);

    List<QualificationRatingMethod> getAllQualificationRatingMethods();

    QualificationRatingMethod insert(QualificationRatingMethodRequest request);

    QualificationRatingMethod show(Long qualificationRatingMethodRequestId);

    QualificationRatingMethod update(Long qualificationRatingMethodRequestId, MethodUpdateRequest request);

    void delete(Long qualificationRatingMethodRequestId);
}
