package com.betopia.hrm.domain.company.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WorkplaceGroupRequest(

    @NotNull(message = "Business Unit ID cannot be null")
    @Positive(message = "Business Unit ID must be positive")
    Long businessUnitId,

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    String name,
    String code,
    String description,
    Boolean status

) {
}
