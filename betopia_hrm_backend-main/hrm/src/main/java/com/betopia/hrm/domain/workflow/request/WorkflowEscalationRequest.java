package com.betopia.hrm.domain.workflow.request;

import java.time.Instant;

public record WorkflowEscalationRequest(
        Long id,
        Long escalatedFrom,
        Long escalatedTo,
        String escalationReason,
        Instant escalatedAt,
        Instant resolvedAt,
        Long instanceId,
        Long instanceStageId
) {
}
