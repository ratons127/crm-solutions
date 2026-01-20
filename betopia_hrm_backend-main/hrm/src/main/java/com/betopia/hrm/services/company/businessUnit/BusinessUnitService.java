package com.betopia.hrm.services.company.businessUnit;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.request.BusinessUnitRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BusinessUnitService {

    PaginationResponse<BusinessUnit> index(Sort.Direction direction, int page, int perPage);

    List<BusinessUnit> getAllBusinessUnits();

    BusinessUnit store(BusinessUnitRequest request);

    BusinessUnit show(Long businessUnitId);

    BusinessUnit update(Long businessUnitId, BusinessUnitRequest request);

    void destroy(Long businessUnitId);
}
