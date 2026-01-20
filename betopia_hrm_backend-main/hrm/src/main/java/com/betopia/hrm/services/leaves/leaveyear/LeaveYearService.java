package com.betopia.hrm.services.leaves.leaveyear;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveYear;
import com.betopia.hrm.domain.leave.request.LeaveYearRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveYearService {

    PaginationResponse<LeaveYear> index(Sort.Direction direction, int page, int perPage);

    List<LeaveYear> getAllLeaveYears();

    LeaveYear getLeaveYearById(Long id);

    LeaveYear store(LeaveYearRequest request);

    LeaveYear updateLeaveYear(Long id, LeaveYearRequest request);

    void deleteLeaveYear(Long id);

}
