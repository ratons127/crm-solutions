package com.betopia.hrm.domain.company.request;

import java.time.LocalDateTime;

public record CalendarsRequest(
        Long id,
        String name,
        String description,
        String type,
        Integer year,
        Boolean isDefault,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
