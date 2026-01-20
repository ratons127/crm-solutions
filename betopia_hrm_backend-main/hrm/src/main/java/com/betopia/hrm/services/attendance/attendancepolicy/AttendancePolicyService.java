package com.betopia.hrm.services.attendance.attendancepolicy;

import com.betopia.hrm.domain.attendance.request.AttendancePolicyRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendancePolicyDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AttendancePolicyService {

    PaginationResponse<AttendancePolicyDTO> index(Sort.Direction direction, int page, int perPage);

    List<AttendancePolicyDTO> getAll();

    AttendancePolicyDTO store(AttendancePolicyRequest request);

    AttendancePolicyDTO show(Long Id);

    AttendancePolicyDTO update(Long Id, AttendancePolicyRequest request);

    void destroy(Long Id);
}
