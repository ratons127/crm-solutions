package com.betopia.hrm.services.company.businessUnit.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.exception.BusinessUnitNotFound;
import com.betopia.hrm.domain.company.repository.BusinessUnitRepository;
import com.betopia.hrm.domain.company.request.BusinessUnitRequest;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.company.businessUnit.BusinessUnitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private final BusinessUnitRepository businessUnitRepository;

    private final CompanyRepository companyRepository;

    public BusinessUnitServiceImpl(BusinessUnitRepository businessUnitRepository, CompanyRepository companyRepository) {
        this.businessUnitRepository = businessUnitRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaginationResponse<BusinessUnit> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<BusinessUnit> businessUnitPage = businessUnitRepository.findAll(pageable);
        List<BusinessUnit> businessUnits = businessUnitPage.getContent();
        PaginationResponse<BusinessUnit> response = new PaginationResponse<>();

        response.setData(businessUnits);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All business Units fetch successful");

        Links links = Links.fromPage(businessUnitPage, "/business-units");
        response.setLinks(links);
        Meta meta = Meta.fromPage(businessUnitPage, "/business-units");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<BusinessUnit> getAllBusinessUnits() {
        return businessUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public BusinessUnit store(BusinessUnitRequest request) {
        Company company = companyRepository.findById(request.company())
                .orElseThrow(() -> new CompanyNotFound("Company not found"));

        BusinessUnit businessUnit = new BusinessUnit();

        businessUnit.setCompany(company);
        businessUnit.setName(request.name());
        businessUnit.setCode(request.code());
        businessUnit.setDescription(request.description());
        businessUnit.setStatus(request.status());

        businessUnitRepository.save(businessUnit);

        return businessUnit;
    }

    @Override
    public BusinessUnit show(Long businessUnitId) {
        BusinessUnit businessUnit = businessUnitRepository.findById(businessUnitId)
                .orElseThrow(() -> new BusinessUnitNotFound("Business Unit not found with id: " + businessUnitId));

        return businessUnit;
    }

    @Override
    public BusinessUnit update(Long businessUnitId, BusinessUnitRequest request) {
        BusinessUnit businessUnit = businessUnitRepository.findById(businessUnitId)
                .orElseThrow(() -> new BusinessUnitNotFound("Business Unit not found with id: " + businessUnitId));

        Company company = companyRepository.findById(request.company())
                .orElseThrow(() -> new CompanyNotFound("Company not found"));

        businessUnit.setCompany(company != null ? company : businessUnit.getCompany());
        businessUnit.setName(request.name() != null ? request.name() : businessUnit.getName());
        businessUnit.setCode(request.code() != null ? request.code() : businessUnit.getCode());
        businessUnit.setDescription(request.description() != null ? request.description() : businessUnit.getDescription());
        businessUnit.setStatus(request.status() != null ? request.status() : businessUnit.getStatus());

        businessUnitRepository.save(businessUnit);

        return businessUnit;
    }

    @Override
    public void destroy(Long businessUnitId) {
        businessUnitRepository.deleteById(businessUnitId);
    }
}
