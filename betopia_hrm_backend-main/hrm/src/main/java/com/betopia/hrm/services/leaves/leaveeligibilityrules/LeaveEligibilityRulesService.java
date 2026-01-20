package com.betopia.hrm.services.leaves.leaveeligibilityrules;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveEligibilityRules;
import com.betopia.hrm.domain.leave.request.LeaveEligibilityRulesRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveEligibilityRulesService {

    PaginationResponse<LeaveEligibilityRules> index(Sort.Direction direction, int page, int perPage);

    List<LeaveEligibilityRules> getAllLeaveEligibilityRules();

    LeaveEligibilityRules getLeaveEligibilityRulesById(Long id);

    LeaveEligibilityRules store(LeaveEligibilityRulesRequest request);

    LeaveEligibilityRules updateLeaveEligibilityRules(Long id, LeaveEligibilityRulesRequest request);

    void deleteLeaveEligibilityRules(Long id);

}
