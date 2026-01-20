package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.InstituteName;
import com.betopia.hrm.domain.employee.request.InstituteNameRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface InstituteNameService {

    PaginationResponse<InstituteName> index(Sort.Direction direction, int page, int perPage);

    List<InstituteName> getAllInstituteName();

    InstituteName store(InstituteNameRequest request);

    InstituteName show(Long instituteNameId);

    InstituteName update(Long instituteNameId, InstituteNameRequest request);

    void delete(Long instituteNameId);
}
