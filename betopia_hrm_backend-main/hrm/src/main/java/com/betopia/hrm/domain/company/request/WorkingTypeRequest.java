package com.betopia.hrm.domain.company.request;

import jakarta.validation.constraints.NotNull;

public record WorkingTypeRequest(
        @NotNull(message = "Name cannot be null")
        String name,
        Boolean status
) {
}
