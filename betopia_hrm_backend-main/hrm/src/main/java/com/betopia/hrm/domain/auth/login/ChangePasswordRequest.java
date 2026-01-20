package com.betopia.hrm.domain.auth.login;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Old password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        String newPassword,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
}
