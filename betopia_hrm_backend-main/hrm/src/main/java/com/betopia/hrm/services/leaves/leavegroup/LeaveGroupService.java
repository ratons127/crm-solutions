package com.betopia.hrm.services.leaves.leavegroup;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import com.betopia.hrm.domain.leave.request.LeaveGroupRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveGroupService {

    PaginationResponse<LeaveGroup> index(Sort.Direction direction, int page, int perPage);

    List<LeaveGroup> getAllLeaveGroups();

    LeaveGroup getLeaveGroupById(Long id);

    LeaveGroup store(LeaveGroupRequest request);

    LeaveGroup updateLeaveGroup(Long id, LeaveGroupRequest request);

    void deleteLeaveGroup(Long id);

}
