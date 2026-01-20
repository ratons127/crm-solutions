package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.AccessStatus;
import com.betopia.hrm.domain.employee.enums.SystemName;

import java.time.LocalDateTime;

public record AccessRevocationLogRequest(
        Long separationId,
        SystemName systemName,
        String accessType,
        LocalDateTime revocationDate,
        Long revokedById,
        AccessStatus accessStatus,
        String errorMessage,
        int retryCount
) {
}
