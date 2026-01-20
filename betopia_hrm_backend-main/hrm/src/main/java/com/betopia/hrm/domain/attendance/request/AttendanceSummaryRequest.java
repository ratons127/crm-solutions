package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.enums.AttendanceType;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import com.betopia.hrm.domain.attendance.enums.DayStatus;

public record AttendanceSummaryRequest(

        @NotNull(message = "Company ID cannot be null")
        Long companyId,

        @NotNull(message = "Employee ID cannot be null")
        Long employeeId,

        @NotNull(message = "Shift ID cannot be null")
        Long shiftId,

        @NotNull(message = "Attendance Policy ID cannot be null")
        Long attendancePolicyId,

        LocalDate workDate,
        LocalDateTime inTime,
        LocalDateTime outTime,
        Integer totalWorkMinutes,
        Integer lateMinutes,
        Integer earlyLeaveMinutes,
        Integer overtimeMinutes,
        DayStatus dayStatus,
        AttendanceType attendanceType,

        @NotNull(message = "Shift ID cannot be null")
        Long manualRequestId,

        String remarks,
        LocalDateTime computedAt,
        Boolean isLocked

) {}