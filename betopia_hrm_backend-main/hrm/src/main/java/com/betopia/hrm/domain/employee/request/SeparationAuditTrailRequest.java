package com.betopia.hrm.domain.employee.request;

import java.util.Map;

public record SeparationAuditTrailRequest(
        Long separationId,
        String action,
        Long performedById,
        Map<String, Object> actionDetails,
        Map<String, Object> oldValues,
        Map<String, Object> newValues,
        String ipAddress,
        String userAgent
) {
}
