package com.betopia.hrm.services.users.company.impl;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.request.CompanyRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.company.CompanyService;
import com.betopia.hrm.webapp.util.S3Service;
import com.betopia.hrm.webapp.util.UploadFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
//import java.util.logging.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class.getName());

    private final CompanyRepository companyRepository;

    private final S3Service s3Service;

    private final UploadFiles uploadFiles;


    public CompanyServiceImpl(CompanyRepository companyRepository, S3Service s3Service, UploadFiles uploadFiles) {
        this.companyRepository = companyRepository;
        this.s3Service = s3Service;
        this.uploadFiles = uploadFiles;
    }

    @Override
    public PaginationResponse<Company> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Company> companyPage = companyRepository.findAll(pageable);

        List<Company> companies = companyPage.getContent();

        PaginationResponse<Company> response = new PaginationResponse<>();

        response.setData(companies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All companies fetch successful");

        Links links = Links.fromPage(companyPage, "/company");
        response.setLinks(links);

        Meta meta = Meta.fromPage(companyPage, "/company");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Company> getAllCompanies() {

        return companyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Company store(CompanyRequest request) {

        Company company = new Company();

        company.setName(request.name());
        company.setCountryId(request.countryId());
        company.setDivisionId(request.divisionId());
        company.setDistrictId(request.districtId());
        company.setThana(request.thana());
        company.setPostOffice(request.postOffice());
        company.setShortName(request.shortName());
        company.setCode(request.code());
        company.setPhone(request.phone());
        company.setEmail(request.email());
        company.setWebsiteUrl(request.websiteUrl());
        company.setDescription(request.description());
        company.setAddress(request.address());
        company.setImage(request.image());
        company.setImage(request.imageUrl());
        company.setStatus(request.status());
        company.setCurrency(Currency.getInstance(request.currency()));
        company.setTimeZone(ZoneId.of(request.timeZone()));

        companyRepository.save(company);

        return company;
    }

    @Override
    public Company show(Long companyId) {

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFound("Company not found " + companyId));

        return company;
    }

    @Override
    public Company update(Long companyId, CompanyRequest request) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFound("Company not found " + companyId));

        company.setName(request.name() != null ? request.name() : company.getName());
        company.setCountryId(request.countryId() != null ? request.countryId() : company.getCountryId());
        company.setDivisionId(request.divisionId() != null ? request.divisionId() : company.getDivisionId());
        company.setDistrictId(request.districtId() != null ? request.districtId() : company.getDistrictId());
        company.setThana(request.thana() != null ? request.thana() : company.getThana());
        company.setPostOffice(request.postOffice() != null ? request.postOffice() : company.getPostOffice());
        company.setShortName(request.shortName() != null ? request.shortName() : company.getShortName());
        company.setCode(request.code() != null ? request.code() : company.getCode());
        company.setPhone(request.phone() != null ? request.phone() : company.getPhone());
        company.setEmail(request.email() != null ? request.email() : company.getEmail());
        company.setWebsiteUrl(request.websiteUrl() != null ? request.websiteUrl() : company.getWebsiteUrl());
        company.setDescription(request.description() != null ? request.description() : company.getDescription());
        company.setAddress(request.address() != null ? request.address() : company.getAddress());
        company.setImage(request.image() != null ? request.image() : company.getImage());
        company.setImage(request.imageUrl() != null ? request.imageUrl() : company.getImageUrl());
        company.setStatus(request.status() != null ? request.status() : company.getStatus());
        company.setCurrency(request.currency() != null ? Currency.getInstance(request.currency()) : company.getCurrency());
        company.setTimeZone(request.timeZone() != null ? ZoneId.of(request.timeZone()) : company.getTimeZone());

        companyRepository.save(company);

        return company;
    }

    @Override
    public void destroy(Long companyId) {

        companyRepository.deleteById(companyId);
    }

    @Override
    public ResponseEntity<GlobalResponse> uploadCompanyImage(Long companyId, MultipartFile file) {
        return uploadFiles.upload(file, "companies", (imageUrl) -> {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new CompanyNotFound("Company not found " + companyId));

            company.setImage("companies/" + file.getOriginalFilename());
            company.setImageUrl(imageUrl);
            return companyRepository.save(company);
        });
    }

    @Override
    public ResponseEntity<GlobalResponse> deleteCompanyImage(Long companyId) {
        return uploadFiles.delete(
                companyId,
                () -> companyRepository.findById(companyId)
                        .orElseThrow(() -> new CompanyNotFound("Company not found " + companyId)),

                (entity) -> {
                    Company company = (Company) entity;

                    // delete from S3
                    if (company.getImage() != null) {
                        try {
                            uploadFiles.deleteFromS3(company.getImage());
                        } catch (Exception e) {
                            log.error("Failed to delete image from S3 for companyId={}, image={}",
                                    companyId, company.getImage(), e);
                        }
                    }

                    // clear DB refs
                    company.setImage(null);
                    company.setImageUrl(null);
                    companyRepository.save(company);

                    log.info("Image deleted successfully for companyId={}", companyId);
                }
        );
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return Currency.getAvailableCurrencies()
                .stream()
                .sorted(Comparator.comparing(Currency::getCurrencyCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<ZoneId> getAllTimeZone() {
        return ZoneId.getAvailableZoneIds()
                .stream()
                .sorted()
                .map(ZoneId::of)
                .collect(Collectors.toList());
    }

}
