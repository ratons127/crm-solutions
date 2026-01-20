package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QualificationRatingMethodRequestDetails(
        @NotBlank(message = "Grade cannot be blank")
        String grade,

        @NotNull(message = "Maximum mark is required")
        @Min(value = 0, message = "Maximum mark cannot be negative")
        @Max(value = 100, message = "Maximum mark cannot exceed 100")
        Integer maximumMark,

        @NotNull(message = "Minimum mark is required")
        @Min(value = 0, message = "Minimum mark cannot be negative")
        @Max(value = 100, message = "Minimum mark cannot exceed 100")
        Integer minimumMark,

        @NotNull(message = "Average mark is required")
        @Min(value = 0, message = "Average mark cannot be negative")
        @Max(value = 100, message = "Average mark cannot exceed 100")
        Integer averageMark
) {
}
