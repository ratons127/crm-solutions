package com.betopia.hrm.domain.workflow.request;

public record WorkflowAuditLogRequest(
        Long id,
        String actionType,
        Long performedBy,
        String oldValue,
        String newValue,
        String fieldChanged,
        String ipAddress,
        Long instanceId
) {
}
