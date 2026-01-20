package com.betopia.hrm.services.leaves.leavepolicy;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeavePolicyDTO;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.request.LeavePolicyRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeavePolicyService {

    PaginationResponse<LeavePolicyDTO> index(Sort.Direction direction, int page, int perPage);

    List<LeavePolicyDTO> getAllLeavePolicies();

    LeavePolicyDTO getLeavePolicyById(Long id);

    LeavePolicyDTO store(LeavePolicyRequest request);

    LeavePolicyDTO updateLeavePolicy(Long id, LeavePolicyRequest request);

    void deleteLeavePolicy(Long id);
}
