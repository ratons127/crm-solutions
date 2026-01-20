package com.betopia.hrm.domain.leave.request;

import com.betopia.hrm.domain.leave.enums.LeaveStatus;

import java.time.LocalDateTime;

public record LeaveApprovalsRequest(
        Long leaveRequestId,
        Long approverId,
        Long employeeId,
        Integer level,
        LeaveStatus leaveStatus,
        String remarks,
        LocalDateTime actionDate,
        Long leaveApprovalId,
        String employeeName,

        Long employeeSerialId
) {
}
