package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Grade;
import com.betopia.hrm.domain.employee.request.GradeRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GradeService {

    PaginationResponse<Grade> index(Sort.Direction direction, int page, int perPage);

    List<Grade> getAllGrades();

    Grade insert(GradeRequest request);

    Grade show(Long gradeId);

    Grade update(Long gradeId, GradeRequest request);

    void delete(Long gradeId);
}
