package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LeaveYearRequest(
        Long id,

        @NotNull(message = "Leave Type ID cannot be null")
        LocalDate startDate,

        @NotNull(message = "Leave Type ID cannot be null")
        LocalDate endDate,

        Boolean status
) {
}
