package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.enums.AdjustmentType;
import com.betopia.hrm.domain.attendance.enums.AttendanceStatus;
import com.betopia.hrm.domain.attendance.enums.Source;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ManualAttendanceRequest(
        Long employeeId,
        LocalDate attendanceDate,
        LocalDateTime inTime,
        LocalDateTime outTime,
        AdjustmentType adjustmentType, // ENUM: MISSING_BIOMETRIC, DEVICE_OFFLINE, FORGOT_TO_PUNCH, OTHER
        String reason,
        Long submittedById,
        LocalDateTime submittedAt,
        Source sourceChannel,  // ENUM: HR_ADMIN, EMPLOYEE, SUPERVISOR, API
        Boolean isLocked,
        AttendanceStatus manualAttendanceStatus
) {
}
