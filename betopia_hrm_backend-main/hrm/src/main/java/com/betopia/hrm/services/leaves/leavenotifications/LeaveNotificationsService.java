package com.betopia.hrm.services.leaves.leavenotifications;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveNotificationsDTO;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.request.LeaveNotificationsRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveNotificationsService {

    PaginationResponse<LeaveNotificationsDTO> index(Sort.Direction direction, int page, int perPage);

    List<LeaveNotificationsDTO> getAll();

    LeaveNotificationsDTO store(LeaveNotificationsRequest request);

    LeaveNotificationsDTO show(Long id);

    LeaveNotificationsDTO update(Long id, LeaveNotificationsRequest request);

    void destroy(Long id);

    LeaveNotificationsDTO createInitialNotification(LeaveApprovals approval);

    String sendNotificationMessageByLeaveRequest(Long id);

    String sendNotificationAfterApproval(Long leaveRequestId);

}
