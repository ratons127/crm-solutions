package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LeaveGroupRequest(
        Long id,

        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String name,

        String description,

        Boolean status
) {

}
