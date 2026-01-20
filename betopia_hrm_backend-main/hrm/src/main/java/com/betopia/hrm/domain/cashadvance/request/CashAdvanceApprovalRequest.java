package com.betopia.hrm.domain.cashadvance.request;

import com.betopia.hrm.domain.cashadvance.enums.CashAdvanceStatus;

import java.time.LocalDateTime;

public record CashAdvanceApprovalRequest(
        Long cashAdvanceApprovalId,
        Long approverId,
        Long employeeId,
        Integer level,
        String cashAdvanceStatus,
        String remarks,
        LocalDateTime actionDate,
        Long leaveApprovalId
) {
}
