package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.HandoverStatus;

import java.time.LocalDateTime;

public record HandoverChecklistRequest(
        Long separationId,
        String itemDescription,
        Long handoverToId,
        HandoverStatus status,
        LocalDateTime completedDate,
        String remarks,
        String evidencePath
) {
}
