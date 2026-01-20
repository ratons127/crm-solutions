package com.betopia.hrm.domain.company.request;

import com.betopia.hrm.domain.company.enums.AudienceType;

import java.util.UUID;

public record NotificationBindingsRequest(
        Long companyId,
        Long eventId,
        AudienceType audienceType,
        UUID audienceRefId,
        Integer priority,
        Boolean status
) {
}
