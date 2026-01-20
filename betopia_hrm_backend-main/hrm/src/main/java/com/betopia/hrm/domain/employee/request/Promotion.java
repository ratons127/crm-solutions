package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.ApprovalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Promotion(
        Long id,
        Long employeeId,
        Long currentGradeId,
        Long newGradeId,
        Long currentDesignationId,
        Long newDesignationId,
        Integer appraisalId,
        String justification,
        String promotionType,
        LocalDate effectiveDate,
        BigDecimal salaryChange,
        ApprovalStatus approvalStatus,
        Long approvedById,
        LocalDateTime approvedAt,
        Boolean status
) {
}
