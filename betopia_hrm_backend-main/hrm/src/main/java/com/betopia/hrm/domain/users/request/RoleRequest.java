package com.betopia.hrm.domain.users.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RoleRequest(
    long id,

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    String name,

    String level,

    List<Long> permissions
) {
}
