package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LeaveTypeRulesRequest(
        Long id,

        @NotNull(message = "Leave Type ID cannot be null")
        @Positive(message = "Leave Type ID must be positive")
        Long leaveTypeId,

        @NotBlank(message = "ruleKey cannot be blank")
        @NotNull(message = "ruleKey cannot be null")
        String ruleKey,

        @NotBlank(message = "ruleValue cannot be blank")
        @NotNull(message = "ruleValue cannot be null")
        String ruleValue,

        String description,
        Boolean status
) {
}
