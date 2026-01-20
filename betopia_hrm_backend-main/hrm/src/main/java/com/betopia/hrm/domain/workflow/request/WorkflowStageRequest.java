package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.enums.ApprovalType;
import com.betopia.hrm.domain.workflow.enums.StageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WorkflowStageRequest(
        Long id,

        @NotNull(message = "Stage name cannot be null")
        @NotBlank(message = "Stage name cannot be blank")
        String stageName,

        Integer stageOrder,
        StageType stageType,
        Boolean isMandatory,
        Boolean canSkip,
        Integer minApprovers,
        ApprovalType approvalType,
        Integer escalationHours,

        @NotNull(message = "Template ID cannot be null")
        Long templateId,

        List<StageApprover> approvers,
        List<WorkflowInstanceStage> instanceStages,
        List<ApprovalAction> actions
) {
}
