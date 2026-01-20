package com.betopia.hrm.domain.attendance.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

public record AttendanceDeviceCategoryRequest(
        Long id,

        @NotNull(message = "Name is required")
        @NotBlank(message = "Name field is not blank")
        String name,

        @NotNull(message = "Communication type is required")
        @NotBlank(message = "Communication type field is not blank")
        String communicationType,

        @NotNull(message = "Biometirc Mode is required")
        @NotBlank(message = "Biometirc Mode field is not blank")
        String biometricMode,

        String description,

        Boolean status
) {
}
