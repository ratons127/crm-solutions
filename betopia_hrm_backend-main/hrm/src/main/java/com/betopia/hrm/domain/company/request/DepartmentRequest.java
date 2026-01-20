package com.betopia.hrm.domain.company.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
        @NotNull(message = "Workplace ID cannot be null")
        Long WorkplaceId,

        @NotBlank(message = "Department name cannot be blank")
        @Size(max = 255)
        String name,

        @Size(max = 50)
        @NotBlank(message = "Code cannot be blank")
        @NotNull(message = "Code cannot be null")
        String code,

        String description,

        Boolean status
) {
}
