package com.betopia.hrm.domain.company.request;

import com.betopia.hrm.domain.company.enums.Channel;

public record NotificationProvidersRequest(
        Long id,
        Long companyId,
        Channel channel,
        String providerKey,
        Boolean status
) {
}
