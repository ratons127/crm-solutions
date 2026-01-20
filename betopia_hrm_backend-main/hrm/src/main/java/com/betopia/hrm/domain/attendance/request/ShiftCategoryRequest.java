package com.betopia.hrm.domain.attendance.request;

import jakarta.validation.constraints.NotNull;

public record ShiftCategoryRequest(
        Long id,

        @NotNull(message = "Name cannot be null") String name,

        @NotNull(message = "Type cannot be null") String type,

        String description,

        Boolean status
) {
}
