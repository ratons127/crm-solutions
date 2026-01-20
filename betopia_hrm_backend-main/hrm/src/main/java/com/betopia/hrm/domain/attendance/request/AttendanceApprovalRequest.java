package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceApprovalRequest(

        @NotNull(message = "Employee ID is required")
        Long employeeId,

        @NotBlank(message = "Employee name is required")
        String employeeName,

        Long employeeSerialId,

        @NotNull(message = "Manual Attendance ID is required")
        Long manualAttendanceId,

        LocalDate date,
        LocalDateTime inTime,
        LocalDateTime outTime,

        @NotNull(message = "Manual approval status is required")
        AttendanceStatus manualApprovalStatus,

        AdjustmentType adjustmentType,
        String reason,
        Long approverId,
        Source submittedBy,
        LocalDateTime submittedAt

) {}