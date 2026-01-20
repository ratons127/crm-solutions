package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmployeeGroupRequest(
    Long id,

    @NotNull(message = "Company Id cannot be null")
    @Positive(message = "Company Id must be positive")
    Long companyId,

    @NotNull(message = "workplace Id cannot be null")
    @Positive(message = "workplace Id must be positive")
    Long workplaceId,

    @NotNull(message = "Name cannot be null")
    String name,

    String code,
    String description,
    Boolean status
) {
}
