package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.enums.StageStatus;

import java.time.Instant;
import java.util.List;

public record WorkflowInstanceStageRequest(
        Long id,
        StageStatus stageStatus,
        Long assignedTo,
        Instant assignedAt,
        Instant startedAt,
        Instant completedAt,
        String remarks,
        Long instanceId,
        Long stageId,
        List<ApprovalAction> actions
) {
}
