package com.betopia.hrm.domain.attendance.request;

import java.time.LocalDateTime;

public record AttendanceDeviceAssignRequest(
        Long id,
        Long attendanceDeviceId,
        Long employeeId,
        Long deviceUserId,
        Long assignedBy,
        LocalDateTime assignedAt,
        String notes,
        Boolean status
) {
}
