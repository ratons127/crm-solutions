package com.betopia.hrm.domain.lookup.request;

public record LookupSetupDetailsRequest(
        Long id,
        Long lookupSetupEntryId,
        String name,
        String details,
        Boolean status

) {
}
