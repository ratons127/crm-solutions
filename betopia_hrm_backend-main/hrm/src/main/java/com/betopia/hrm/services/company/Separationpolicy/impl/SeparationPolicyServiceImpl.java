package com.betopia.hrm.services.company.Separationpolicy.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.SeparationPolicy;
import com.betopia.hrm.domain.company.entity.SeparationPolicyVersion;
import com.betopia.hrm.domain.company.enums.SeparationPolicyStatus;
import com.betopia.hrm.domain.company.exception.SeparationPolicyNotFound;
import com.betopia.hrm.domain.company.repository.SeparationPolicyRepository;
import com.betopia.hrm.domain.company.repository.SeparationPolicyVersionRepository;
import com.betopia.hrm.domain.company.request.SeparationPolicyRequest;
import com.betopia.hrm.domain.dto.company.SeparationPolicyDTO;
import com.betopia.hrm.domain.dto.company.mapper.SeparationPolicyMapper;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.company.Separationpolicy.SeparationPolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class SeparationPolicyServiceImpl implements SeparationPolicyService {

    private final SeparationPolicyRepository separationPolicyRepository;
    private final SeparationPolicyVersionRepository separationPolicyVersionRepository;
    private final CompanyRepository companyRepository;
    private final WorkplaceRepository workplaceRepository;
    private final SeparationPolicyMapper separationPolicyMapper;

    public SeparationPolicyServiceImpl(SeparationPolicyRepository separationPolicyRepository,
                                       SeparationPolicyVersionRepository separationPolicyVersionRepository,
                                       CompanyRepository companyRepository,
                                       WorkplaceRepository workplaceRepository,
                                       SeparationPolicyMapper separationPolicyMapper) {
        this.separationPolicyRepository = separationPolicyRepository;
        this.separationPolicyVersionRepository = separationPolicyVersionRepository;
        this.companyRepository = companyRepository;
        this.workplaceRepository = workplaceRepository;
        this.separationPolicyMapper = separationPolicyMapper;
    }

    @Override
    public PaginationResponse<SeparationPolicy> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<SeparationPolicy> separationPolicyPage = separationPolicyRepository.findAll(pageable);
        List<SeparationPolicy> separationPolicies = separationPolicyPage.getContent();
        PaginationResponse<SeparationPolicy> response = new PaginationResponse<>();

        response.setData(separationPolicies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Separation Policies fetch successful");

        Links links = Links.fromPage(separationPolicyPage, "/SeparationPolicies");
        response.setLinks(links);
        Meta meta = Meta.fromPage(separationPolicyPage, "/SeparationPolicies");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<SeparationPolicyDTO> getAllSeparationPolicies() {
        return separationPolicyMapper.toDTOList(separationPolicyRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public List<SeparationPolicyDTO> getSeparationPoliciesByCompanyId(Long companyId) {
        if (companyId == null) {
            throw new IllegalArgumentException("companyId is required");
        }
        List<SeparationPolicy> separationPolicies = separationPolicyRepository.findAllByCompanyId(companyId);
        if (separationPolicies == null || separationPolicies.isEmpty()) {
            throw new SeparationPolicyNotFound("No separation policy found for companyId: " + companyId);
        }

        return separationPolicyMapper.toDTOList(separationPolicies);
    }


    @Override
    public SeparationPolicyDTO show(Long separationPolicyId) {
        SeparationPolicy separationPolicy = separationPolicyRepository.findById(separationPolicyId)
                .orElseThrow(() -> new SeparationPolicyNotFound("Separation policy not found with id: " + separationPolicyId));

        return separationPolicyMapper.toDTO(separationPolicy);
    }
    @Override
    public SeparationPolicyDTO store(SeparationPolicyRequest request) {

        SeparationPolicy separationPolicy = new SeparationPolicy();

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found with id: " + request.companyId()));

        if (request.workplaceId() != null) {
            Workplace workplace = workplaceRepository.findById(request.workplaceId())
                    .orElseThrow(() -> new WorkplaceNotFound("Workplace not found with id: " + request.workplaceId()));
            separationPolicy.setWorkplace(workplace);
        } else {
            separationPolicy.setWorkplace(null);
        }

        if (separationPolicyRepository.existsByCode(request.code())) {
            throw new RuntimeException("Code already exists: " + request.code());
        }

        separationPolicy.setName(request.name());
        separationPolicy.setCompany(company);
        separationPolicy.setCode(request.code());
        separationPolicy.setDescription(request.description());
        separationPolicy.setEffectiveFrom(request.effectiveFrom());
        separationPolicy.setEffectiveTo(request.effectiveTo());
        separationPolicy.setPreviousVersion(null);
        separationPolicy.setSeparationPolicyStatus(request.separationPolicyStatus() == null ? SeparationPolicyStatus.DRAFT : request.separationPolicyStatus());
        separationPolicy.setStatus(request.status());

        return separationPolicyMapper.toDTO( separationPolicyRepository.save(separationPolicy));

    }

    @Override
    public SeparationPolicyDTO update(Long id, SeparationPolicyRequest request) {

        SeparationPolicy separationPolicy = separationPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Separation policy not found with id: " + id));

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found with id: " + request.companyId()));

        Workplace workplace = request.workplaceId() != null
                ? workplaceRepository.findById(request.workplaceId())
                .orElseThrow(() -> new WorkplaceNotFound("Workplace not found with id: " + request.workplaceId()))
                : null;

        if (!hasChanges(separationPolicy, request, company, workplace)) {
            throw new RuntimeException("No changes found in update request.");
        }

        savePreviousVersion(separationPolicy);

        separationPolicy.setCompany(company != null ? company : separationPolicy.getCompany());
        separationPolicy.setWorkplace(workplace != null ? workplace : separationPolicy.getWorkplace());
        separationPolicy.setName(request.name() != null ? request.name() : separationPolicy.getName());

        if (request.code() != null && !request.code().equals(separationPolicy.getCode())) {
            if (separationPolicyRepository.existsByCode(request.code())) {
                throw new RuntimeException("Code already exists: " + request.code());
            }
            separationPolicy.setCode(request.code());
        } else {
            separationPolicy.setCode(separationPolicy.getCode());
        }

        separationPolicy.setDescription(request.description() != null ? request.description() : separationPolicy.getDescription());

        separationPolicy.setEffectiveFrom(request.effectiveFrom() != null ? request.effectiveFrom() : separationPolicy.getEffectiveFrom());

        separationPolicy.setEffectiveTo(request.effectiveTo() != null ? request.effectiveTo() : separationPolicy.getEffectiveTo());

        separationPolicy.setSeparationPolicyStatus(
                request.separationPolicyStatus() != null ? request.separationPolicyStatus() : separationPolicy.getSeparationPolicyStatus());

        separationPolicy.setStatus(request.status() != null ? request.status() : separationPolicy.getStatus());

        return separationPolicyMapper.toDTO(separationPolicyRepository.save(separationPolicy));
    }

   /* @Override
    public void destroy(Long separationPolicyId) {
        separationPolicyRepository.deleteById(separationPolicyId);
    }*/

    public void destroy(Long separationPolicyId) {
        SeparationPolicy separationPolicy = separationPolicyRepository.findById(separationPolicyId)
                .orElseThrow(() -> new RuntimeException("Separation policy not found with id: " + separationPolicyId));

        separationPolicy.setStatus(false);
        separationPolicy.setSeparationPolicyStatus(SeparationPolicyStatus.ARCHIVED);

        separationPolicyRepository.save(separationPolicy);
    }

    @Override
    public SeparationPolicyDTO updateSeparationPolicyStatus(Long separationPolicyId, SeparationPolicyRequest request) {
        SeparationPolicy separationPolicy = separationPolicyRepository.findById(separationPolicyId)
                .orElseThrow(() -> new RuntimeException("Separation Policy not found with id: " + separationPolicyId));

        SeparationPolicyStatus previousStatus = separationPolicy.getSeparationPolicyStatus();
        SeparationPolicyStatus newStatus = request.separationPolicyStatus();

        if (newStatus == null) {
            throw new RuntimeException("New status cannot be null");
        }

        separationPolicy.setSeparationPolicyStatus(newStatus);

        savePreviousVersion(separationPolicy);

        SeparationPolicy saved = separationPolicyRepository.save(separationPolicy);

        return separationPolicyMapper.toDTO(saved);
    }

    private void savePreviousVersion(SeparationPolicy policy) {
        SeparationPolicyVersion version = new SeparationPolicyVersion();

        version.setCompany(policy.getCompany().getId());
        version.setWorkplace(policy.getWorkplace() != null ? policy.getWorkplace().getId() : 0L);
        version.setName(policy.getName());
        version.setCode(policy.getCode());
        version.setDescription(policy.getDescription());
        version.setEffectiveFrom(policy.getEffectiveFrom());
        version.setEffectiveTo(policy.getEffectiveTo());
        version.setSeparationPolicyStatus(policy.getSeparationPolicyStatus());
        version.setApprovedBy(policy.getApprovedBy());
        version.setSeparationPolicy(policy);

        separationPolicyVersionRepository.findBySeparationPolicyOrderByIdDesc(policy)
                .stream()
                .findFirst()
                .ifPresent(last -> version.setPreviousVersionId(last.getId()));

        separationPolicyVersionRepository.save(version);
    }

    private boolean hasChanges(SeparationPolicy policy, SeparationPolicyRequest request, Company company, Workplace workplace) {

        if (!Objects.equals(policy.getCompany().getId(), company.getId())) return true;

        if (!Objects.equals(
                policy.getWorkplace() != null ? policy.getWorkplace().getId() : null,
                workplace != null ? workplace.getId() : null)) return true;

        if (!Objects.equals(policy.getName(), request.name())) return true;
        if (!Objects.equals(policy.getCode(), request.code())) return true;
        if (!Objects.equals(policy.getDescription(), request.description())) return true;
        if (!Objects.equals(policy.getEffectiveFrom(), request.effectiveFrom())) return true;
        if (!Objects.equals(policy.getEffectiveTo(), request.effectiveTo())) return true;
        if (!Objects.equals(policy.getSeparationPolicyStatus(),
                request.separationPolicyStatus() != null ? request.separationPolicyStatus() : SeparationPolicyStatus.DRAFT)) return true;
        if (!Objects.equals(policy.getStatus(), request.status())) return true;

        return false;
    }

}

