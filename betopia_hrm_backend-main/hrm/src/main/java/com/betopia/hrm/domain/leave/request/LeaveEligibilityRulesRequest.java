package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LeaveEligibilityRulesRequest(

        Long id,

        @NotNull(message = "Leave Type ID cannot be null")
        @Positive(message = "Leave Type ID must be positive")
        Long leaveTypeId,

        @NotNull(message = "Leave group ID cannot be null")
        @Positive(message = "Leave group ID must be positive")
        Long leaveGroupId,

        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String gender,

        @Positive(message = "Leave group ID must be positive")
        Integer minTenureMonths,

        @Positive(message = "Leave group ID must be positive")
        Integer maxTenureMonths,

        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String employmentStatus,

        Boolean status
) {
}
