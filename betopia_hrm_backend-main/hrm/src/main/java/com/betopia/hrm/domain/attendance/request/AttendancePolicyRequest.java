package com.betopia.hrm.domain.attendance.request;

import java.time.LocalDate;

public record AttendancePolicyRequest(
        Long companyId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        Integer graceInMinutes,
        Integer graceOutMinutes,
        Integer lateThresholdMinutes,
        Integer earlyLeaveThresholdMinutes,
        Integer minWorkMinutes,
        Integer halfDayThresholdMinutes,
        Integer absenceThresholdMinutes,
        Boolean movementEnabled,
        Integer movementAllowMinutes,
        String notes,
        Boolean status,
        String companyName
) {
}
