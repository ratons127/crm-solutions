package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.enums.ActionType;

import java.time.Instant;

public record ApprovalActionRequest(
        Long id,
        Long actionBy,
        ActionType actionType,
        Instant actionDate,
        String comments,
        String ipAddress,
        String userAgent,
        Long instanceId,
        Long instanceStageId,
        Long stageId
) {
}
