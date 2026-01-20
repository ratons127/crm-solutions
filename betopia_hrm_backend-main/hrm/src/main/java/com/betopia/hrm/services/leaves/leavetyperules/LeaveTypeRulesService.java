package com.betopia.hrm.services.leaves.leavetyperules;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveTypeRules;
import com.betopia.hrm.domain.leave.request.LeaveTypeRulesRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveTypeRulesService {

    PaginationResponse<LeaveTypeRules> index(Sort.Direction direction, int page, int perPage);

    List<LeaveTypeRules> getAllLeaveTypeRules();

    LeaveTypeRules getLeaveTypeRulesById(Long id);

    LeaveTypeRules store(LeaveTypeRulesRequest request);

    LeaveTypeRules updateLeaveTypeRules(Long id, LeaveTypeRulesRequest request);

    void deleteLeaveTypeRules(Long id);


}
