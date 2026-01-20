package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.ApprovalLevel;
import com.betopia.hrm.domain.employee.enums.SeparationStatus;
import com.betopia.hrm.domain.employee.enums.SeparationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeSeparationsRequest(
        Long id,
        Long employeeId,
        SeparationType separationType,
        LocalDate submissionDate,
        LocalDate requestedLwd,
        LocalDate actualLwd,
        LocalDate effectiveSeparationDate,
        String reason,
        String resignationLetterPath,
        SeparationStatus separationsStatus,
        Long currentApproverId,
        ApprovalLevel currentApprovalLevel,
        Integer noticePeriodDays,
        Boolean noticeWaived,
        String noticeWaiverReason,
        BigDecimal noticeBuyoutAmount,
        Boolean isBuyoutRecovered
) {
}
