package com.betopia.hrm.services.company.Separationpolicy;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.SeparationPolicy;
import com.betopia.hrm.domain.company.request.SeparationPolicyRequest;
import com.betopia.hrm.domain.dto.company.SeparationPolicyDTO;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface SeparationPolicyService {

    PaginationResponse<SeparationPolicy> index(Sort.Direction direction, int page, int perPage);

    List<SeparationPolicyDTO> getAllSeparationPolicies();

    List<SeparationPolicyDTO> getSeparationPoliciesByCompanyId(Long companyId);

    SeparationPolicyDTO store(SeparationPolicyRequest request);

    SeparationPolicyDTO show(Long SeparationPolicyId);

    SeparationPolicyDTO update(Long SeparationPolicyId, SeparationPolicyRequest request);

    void destroy(Long SeparationPolicyId);

    SeparationPolicyDTO updateSeparationPolicyStatus(Long SeparationPolicyId, SeparationPolicyRequest request);
}
