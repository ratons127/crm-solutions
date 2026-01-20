package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.entity.ShiftRotationPatterns;
import com.betopia.hrm.domain.employee.entity.Employee;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ShiftEmployeeRotationRequest(
        @NotNull(message = "Employee ID is required")
        Long employeeId,

        @NotNull(message = "Pattern ID is required")
        Long patternId,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        LocalDate endDate,

        @Min(value = 1, message = "Cycle start day must be at least 1")
        Integer cycleStartDay,

        Boolean status
) {
}
