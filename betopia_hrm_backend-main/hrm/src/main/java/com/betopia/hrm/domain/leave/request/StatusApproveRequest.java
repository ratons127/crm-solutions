package com.betopia.hrm.domain.leave.request;

import com.betopia.hrm.domain.leave.enums.LeaveStatus;
public record StatusApproveRequest(
        Long id,
        LeaveStatus leaveStatus,
        String remarks
) {
}
