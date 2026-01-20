package com.betopia.hrm.domain.company.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamRequest(
        Long id,

        @NotNull(message = "Department ID cannot be null")
        Long departmentId,

        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Code cannot be blank")
        @NotNull(message = "Code cannot be null")
        String code,
        String description,

        @NotNull(message = "Status cannot be null")
        Boolean status
) {
}
