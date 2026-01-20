package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.enums.AttendanceStatus;

public record ApproveAttendanceStatusRequest(
        Long id,
        AttendanceStatus manualApprovalStatus,
        String reason
) {
}
