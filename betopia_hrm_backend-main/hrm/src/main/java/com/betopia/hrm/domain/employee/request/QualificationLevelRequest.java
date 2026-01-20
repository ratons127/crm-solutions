package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;

public record QualificationLevelRequest(
        @NotNull(message = "QualificationTypeId cannot be null")
        Long qualificationTypeId,
        @NotNull(message = "Level name cannot be null")
        String levelName
) {
}
