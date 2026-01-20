package com.betopia.hrm.domain.auth.login;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestBody(
        @NotBlank(message = "Token is required")
        String token,

        @NotBlank(message = "New password is required")
        String newPassword,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
}
