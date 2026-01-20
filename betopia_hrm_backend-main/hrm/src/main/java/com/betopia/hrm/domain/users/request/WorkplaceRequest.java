package com.betopia.hrm.domain.users.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WorkplaceRequest(

    @NotNull(message = "Workplace Group ID cannot be null")
    @Positive(message = "Workplace Group ID must be positive")
    Long workplaceGroupId,

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    String name,

    @NotBlank(message = "Code cannot be blank")
    @NotNull(message = "Code cannot be null")
    String code,

    @NotBlank(message = "Address cannot be blank")
    @NotNull(message = "Address cannot be null")
    String address,

    String description,
    Boolean status
) {
}
