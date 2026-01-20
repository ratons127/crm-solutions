package com.betopia.hrm.domain.leave.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaveTypeRequest(
        Long id,

        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String name,

        @NotBlank(message = "Code cannot be blank")
        @NotNull(message = "Code cannot be null")
        String code,

        Boolean status,
        String description
        ) {
}