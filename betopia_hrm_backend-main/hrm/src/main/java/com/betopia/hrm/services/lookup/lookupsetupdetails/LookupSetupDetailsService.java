package com.betopia.hrm.services.lookup.lookupsetupdetails;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.betopia.hrm.domain.lookup.request.LookupSetupDetailsRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LookupSetupDetailsService {

    PaginationResponse<LookupSetupDetails> index(Sort.Direction direction, int page, int perPage, Long parentName);

    List<LookupSetupDetails> findAllLookupSetupDetails();

    LookupSetupDetails getLookupSetupDetailsById(Long id);

    LookupSetupDetails store(LookupSetupDetailsRequest request);

    LookupSetupDetails updateLookupSetupDetails(Long id, LookupSetupDetailsRequest request);

    void deleteLookupSetupDetails(Long id);
}
