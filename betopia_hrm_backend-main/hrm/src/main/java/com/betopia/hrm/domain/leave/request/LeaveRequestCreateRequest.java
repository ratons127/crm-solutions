package com.betopia.hrm.domain.leave.request;

import com.betopia.hrm.domain.leave.entity.LeaveRequestDocument;
import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record LeaveRequestCreateRequest(
        Long id,

        @NotNull(message = "Employee ID cannot be null")
        @Positive(message = "Employee ID must be positive")
        Long employeeId,

        @NotNull(message = "Leave Type ID cannot be null")
        @Positive(message = "Leave Type ID must be positive")
        Long leaveTypeId,
        Long leaveGroupAssignId,

        LocalDate startDate,
        LocalDate endDate,

        @Positive(message = "Employee ID must be positive")
        BigDecimal daysRequested,

        String reason,
        List<LeaveRequestDocument> proofDocumentPath,
        String justification,
        boolean hasProof,

        LeaveStatus status
) {}