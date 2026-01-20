package com.betopia.hrm.domain.auth.login;

import java.time.LocalDateTime;

public record ForgetPasswordRequest(
        Long id,
        String email,
        String token,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime expiresAt
) {
}
