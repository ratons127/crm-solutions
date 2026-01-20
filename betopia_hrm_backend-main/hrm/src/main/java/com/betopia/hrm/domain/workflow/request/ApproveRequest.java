package com.betopia.hrm.domain.workflow.request;

public record ApproveRequest(
        Long instanceId,
        Long instanceStageId,
        Long actionBy,
        Long approverId,
        String actionType,
        String comments
) {
}
