package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;

public record QualificationTypeRequest(
        @NotNull(message = "TypeName cannot be null")
        String typeName
) {
}
