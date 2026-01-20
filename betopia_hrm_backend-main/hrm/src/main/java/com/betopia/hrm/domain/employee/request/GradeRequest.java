package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;

public record GradeRequest(
        @NotNull(message = "Code cannot be null")
        String code,
        String name,
        String description,
        Boolean status
) {
}
