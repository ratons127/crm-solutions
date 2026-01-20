package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.entity.WorkflowAuditLog;
import com.betopia.hrm.domain.workflow.entity.WorkflowEscalation;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import com.betopia.hrm.domain.workflow.enums.WorkflowPriority;
import com.betopia.hrm.domain.workflow.enums.WorkflowStatus;

import java.time.Instant;
import java.util.List;

public record WorkflowInstanceRequest(
        Long id,
        Long referenceId,
        String referenceNumber,
        Long initiatedBy,
        Instant initiatedAt,
        WorkflowStatus currentStatus,
        WorkflowPriority priority,
        Instant completedAt,
        String remarks,
        Long moduleId,
        Long templateId,
        Long currentStageId,
        List<WorkflowInstanceStage> stages,
        List<ApprovalAction> actions,
        List<WorkflowNotification> notifications,
        List<WorkflowEscalation> escalations,
        List<WorkflowAuditLog> auditLogs
) {
}
