package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaveCategoryRequest(
        Long id,

        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        Long parentId,

        Boolean status
) {
}
