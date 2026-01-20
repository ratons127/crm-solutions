package com.betopia.hrm.domain.users.request;

import java.time.LocalDateTime;

public record UserRequest(
    Long id,
    Integer employeeSerialId,
    String name,
    String email,
    String phone,
    String password,
    LocalDateTime emailVerifiedAt,
    String rememberToken,
    boolean isActive,
    Long roleId,

    String userType
) {
}
