package com.betopia.hrm.services.users.user;

import com.betopia.hrm.domain.users.entity.PasswordPolicy;
import com.betopia.hrm.domain.users.request.PasswordPolicyRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PasswordPolicyService {

    PaginationResponse<PasswordPolicy> index(Sort.Direction direction, int page, int perPage);

    List<PasswordPolicy> getAllPasswordPolicies();

    PasswordPolicy insert(PasswordPolicyRequest request);

    PasswordPolicy show(Long passwordPolicyId);

    PasswordPolicy update(Long passwordPolicyId, PasswordPolicyRequest request);

    void delete(Long passwordPolicyId);
}
