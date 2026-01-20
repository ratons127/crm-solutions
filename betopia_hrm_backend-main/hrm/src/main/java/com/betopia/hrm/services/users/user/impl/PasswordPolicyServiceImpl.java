package com.betopia.hrm.services.users.user.impl;

import com.betopia.hrm.domain.users.entity.PasswordPolicy;
import com.betopia.hrm.domain.users.exception.user.PasswordPolicyNotFound;
import com.betopia.hrm.domain.users.repository.PasswordPolicyRepository;
import com.betopia.hrm.domain.users.request.PasswordPolicyRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.user.PasswordPolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PasswordPolicyServiceImpl implements PasswordPolicyService {

    private final PasswordPolicyRepository passwordPolicyRepository;

    public PasswordPolicyServiceImpl(PasswordPolicyRepository passwordPolicyRepository) {
        this.passwordPolicyRepository = passwordPolicyRepository;
    }

    @Override
    public PaginationResponse<PasswordPolicy> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<PasswordPolicy> passwordPolicyPage = passwordPolicyRepository.findAll(pageable);

        List<PasswordPolicy> passwordPolicies = passwordPolicyPage.getContent();

        PaginationResponse<PasswordPolicy> response = new PaginationResponse<>();

        response.setData(passwordPolicies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All password policy fetch successful");

        Links links = Links.fromPage(passwordPolicyPage, "/passwordPolicy");
        response.setLinks(links);

        Meta meta = Meta.fromPage(passwordPolicyPage, "/passwordPolicy");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<PasswordPolicy> getAllPasswordPolicies() {

        return passwordPolicyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public PasswordPolicy insert(PasswordPolicyRequest request) {

        PasswordPolicy passwordPolicy = new PasswordPolicy();
        passwordPolicy.setMaxLength(request.maxLength());
        passwordPolicy.setMinLength(request.minLength());
        passwordPolicy.setExpiration(request.expiration());
        passwordPolicy.setPasswordLockDuration(request.passwordLockDuration());
        passwordPolicy.setGracePeriod(request.gracePeriod());
        passwordPolicy.setRotation(request.rotation());
        passwordPolicy.setBruteForceProtection(request.bruteForceProtection());
        passwordPolicy.setThrottleAttempt(request.throttleAttempt());
        passwordPolicy.setLoginAttemptAllowed(request.loginAttemptAllowed());
        passwordPolicy.setPassRecoveryParam(request.passRecoveryParam());
        passwordPolicy.setTokenValidity(request.tokenValidity());
        passwordPolicy.setYear(request.year());
        passwordPolicyRepository.save(passwordPolicy);

        return passwordPolicy;
    }

    @Override
    public PasswordPolicy show(Long passwordPolicyId) {
        PasswordPolicy passwordPolicy = passwordPolicyRepository.findById(passwordPolicyId)
                .orElseThrow(() -> new PasswordPolicyNotFound("Password policy not found with id: " + passwordPolicyId));

        return passwordPolicy;
    }

    @Override
    public PasswordPolicy update(Long PasswordPolicyId, PasswordPolicyRequest request) {
        PasswordPolicy passwordPolicy = passwordPolicyRepository.findById(PasswordPolicyId)
                .orElseThrow(() -> new PasswordPolicyNotFound("Password policy not found with id: " + PasswordPolicyId));

        passwordPolicy.setMaxLength(request.maxLength());
        passwordPolicy.setMinLength(request.minLength());
        passwordPolicy.setExpiration(request.expiration());
        passwordPolicy.setPasswordLockDuration(request.passwordLockDuration());
        passwordPolicy.setGracePeriod(request.gracePeriod());
        passwordPolicy.setRotation(request.rotation());
        passwordPolicy.setBruteForceProtection(request.bruteForceProtection());
        passwordPolicy.setThrottleAttempt(request.throttleAttempt());
        passwordPolicy.setLoginAttemptAllowed(request.loginAttemptAllowed());
        passwordPolicy.setPassRecoveryParam(request.passRecoveryParam());
        passwordPolicy.setTokenValidity(request.tokenValidity());
        passwordPolicy.setYear(request.year());
        passwordPolicyRepository.save(passwordPolicy);
        return passwordPolicy;
    }

    @Override
    public void delete(Long passwordPolicyId) {
        passwordPolicyRepository.deleteById(passwordPolicyId);
    }
}
