package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.Action;
import com.betopia.hrm.domain.employee.enums.ApprovalLevel;

import java.time.LocalDateTime;

public record SeparationApprovalsRequest(
        Long id,
        Long separationId,
        ApprovalLevel approvalLevel,
        Long approverId,
        Action action,
        String remarks,
        LocalDateTime actionDate,
        LocalDateTime slaDeadline,
        Boolean isOverdue,
        Integer sequenceOrder
) {
}
