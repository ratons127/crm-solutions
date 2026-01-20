package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DesignationRequest(

        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String name,
        String description,
        Boolean status
) {

}