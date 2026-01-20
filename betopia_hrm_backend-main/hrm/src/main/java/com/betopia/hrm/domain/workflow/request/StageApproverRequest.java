package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.enums.ApproverType;

public record StageApproverRequest(
        Long id,
        Long userId,
        Long roleId,
        Long departmentId,
        ApproverType approverType,
        Integer sequenceOrder,
        Boolean status,
        Long stageId
) {
}
