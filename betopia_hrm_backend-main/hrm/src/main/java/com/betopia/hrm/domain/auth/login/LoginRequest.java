package com.betopia.hrm.domain.auth.login;

import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank(message = "Identifier must not be blank")
        String identifier,

        @NotNull(message = "Password cannot be null")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)"
        )
         String password
) {
}
