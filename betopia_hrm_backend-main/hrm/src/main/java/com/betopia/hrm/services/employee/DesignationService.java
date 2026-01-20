package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.request.DesignationRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DesignationService {

    PaginationResponse<Designation> index(Sort.Direction direction, int page, int perPage);
    List<Designation> getAllDesignations();
    Designation store(DesignationRequest request);
    Designation show(Long designationId);
    Designation update(Long designationId, DesignationRequest request);
    void destroy(Long designationId);

}
