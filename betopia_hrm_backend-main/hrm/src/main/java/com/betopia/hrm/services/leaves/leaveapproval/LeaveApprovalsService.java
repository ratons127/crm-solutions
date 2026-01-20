package com.betopia.hrm.services.leaves.leaveapproval;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveApprovalsDTO;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.request.LeaveApprovalsRequest;
import com.betopia.hrm.domain.leave.request.StatusApproveRequest;
import org.springframework.data.domain.Sort;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface LeaveApprovalsService {

    PaginationResponse<LeaveApprovalsDTO> index(Sort.Direction direction, int page, int perPage, String keyword);

    List<LeaveApprovalsDTO> getAll();

    LeaveApprovalsDTO store(LeaveApprovalsRequest request);

    LeaveApprovalsDTO show(Long id);

    LeaveApprovalsDTO update(Long id, LeaveApprovalsRequest request);

    void destroy(Long id);

    LeaveApprovalsDTO createInitialApproval(LeaveRequest leaveRequest);

    List<LeaveApprovalsDTO> updateLeaveStatus(List<StatusApproveRequest> requests);

    List<LeaveApprovalsDTO> getApprovalsForSupervisor();
}
