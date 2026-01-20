package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmploymentStatusRequest(
        Long id,

        @NotNull(message = "Company Id cannot be null")
        @Positive(message = "Company Id must be positive")
        Long companyId,

        String statusCode,
        String statusName,
        String description,
        Boolean isSystem,
        Boolean status
) {
}
