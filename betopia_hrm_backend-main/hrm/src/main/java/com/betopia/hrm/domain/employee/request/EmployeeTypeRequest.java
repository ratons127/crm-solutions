package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;

public record EmployeeTypeRequest(
         @NotNull(message = "Name cannot be null")
         String name,
         String description,
         Boolean status
) {
}
