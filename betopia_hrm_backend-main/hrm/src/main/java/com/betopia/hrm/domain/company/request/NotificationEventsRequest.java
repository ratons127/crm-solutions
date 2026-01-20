package com.betopia.hrm.domain.company.request;

import com.betopia.hrm.domain.company.enums.Category;

public record NotificationEventsRequest(
        Long id,
        Long companyId,
        String eventKey,
        String displayName,
        String description,
        Category category,
        Boolean isSystem,
        Boolean status
) {
}
