package com.betopia.hrm.domain.lookup.request;

public record LookupSetupEntryRequest(
        Long id,
        String name,
        Boolean status
) {
}
