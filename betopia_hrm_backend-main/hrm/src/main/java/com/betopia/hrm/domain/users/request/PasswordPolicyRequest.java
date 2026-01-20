package com.betopia.hrm.domain.users.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PasswordPolicyRequest(
        @NotNull(message = "Minimum Length cannot be null")
        @Positive(message = "Minimum Length must be positive")
        String minLength,

        @NotNull(message = "Maximum Length cannot be null")
        @Positive(message = "Maximum Length must be positive")
        String maxLength,

        Integer expiration,
        Integer rotation,
        Integer gracePeriod,
        String passRecoveryParam,
        Integer tokenValidity,
        Integer loginAttemptAllowed,
        Integer passwordLockDuration,
        Integer bruteForceProtection,
        Integer throttleAttempt,
        @NotNull(message = "Cannot be null")
        @Positive(message = "Maximum Length must be positive")
        Integer year
) {
}
