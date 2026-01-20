package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.enums.AssignmentSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ShiftAssignmentsRequest(
        Long id,
        List<Long> employeeIds,
        Long shiftId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        Boolean status,
        AssignmentSource assignmentSource,
        LocalDateTime assignedAt
) {
}
