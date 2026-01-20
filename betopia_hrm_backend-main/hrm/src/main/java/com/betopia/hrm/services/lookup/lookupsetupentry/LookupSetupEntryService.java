package com.betopia.hrm.services.lookup.lookupsetupentry;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.lookup.entity.LookupSetupEntry;
import com.betopia.hrm.domain.lookup.request.LookupSetupEntryRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LookupSetupEntryService {

    PaginationResponse<LookupSetupEntry> index(Sort.Direction direction, int page, int perPage);

    List<LookupSetupEntry> findAllLookupSetupEntries(); // renamed to follow Spring Data convention

    LookupSetupEntry getLookupSetupEntryById(Long id);

    LookupSetupEntry store(LookupSetupEntryRequest request);

    LookupSetupEntry updateLookupSetupEntry(Long id, LookupSetupEntryRequest request);

    void deleteLookupSetupEntry(Long id);
}