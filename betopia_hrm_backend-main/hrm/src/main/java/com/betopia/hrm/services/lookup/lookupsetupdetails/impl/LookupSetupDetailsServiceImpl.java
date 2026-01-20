package com.betopia.hrm.services.lookup.lookupsetupdetails.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeNotFoundException;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.betopia.hrm.domain.lookup.repository.LookupSetupDetailsRepository;
import com.betopia.hrm.domain.lookup.repository.LookupSetupEntryRepository;
import com.betopia.hrm.domain.lookup.request.LookupSetupDetailsRequest;
import com.betopia.hrm.services.lookup.lookupsetupdetails.LookupSetupDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LookupSetupDetailsServiceImpl implements LookupSetupDetailsService {

    private final LookupSetupDetailsRepository lookupSetupDetailsRepository;

    private final LookupSetupEntryRepository lookupSetupEntryRepository;

    public LookupSetupDetailsServiceImpl(LookupSetupDetailsRepository lookupSetupDetailsRepository, LookupSetupEntryRepository lookupSetupEntryRepository) {
        this.lookupSetupDetailsRepository = lookupSetupDetailsRepository;
        this.lookupSetupEntryRepository = lookupSetupEntryRepository;
    }

    @Override
    public PaginationResponse<LookupSetupDetails> index(Sort.Direction direction, int page, int perPage,             Long setupId // Changed from String parentName to Long setupId
    ) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));


        Page<LookupSetupDetails> lookupSetupDetailsPage;

        if (setupId != null) { // Check if setupId is provided
            // If setupId is provided, filter by it
            lookupSetupDetailsPage = lookupSetupDetailsRepository.findBySetup_Id(setupId, pageable);
        } else {
            // Otherwise, fetch all with pagination
            lookupSetupDetailsPage = lookupSetupDetailsRepository.findAll(pageable);
        }


        List<LookupSetupDetails> lookupSetupDetailsRules = lookupSetupDetailsPage.getContent();

        PaginationResponse<LookupSetupDetails> response = new PaginationResponse<>();
        response.setData(lookupSetupDetailsRules);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All lookup setup details fetched successfully");

        Links links = Links.fromPage(lookupSetupDetailsPage, "/lookup-setup-details");
        response.setLinks(links);

        Meta meta = Meta.fromPage(lookupSetupDetailsPage, "/lookup-setup-details");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LookupSetupDetails> findAllLookupSetupDetails() {
        return lookupSetupDetailsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public LookupSetupDetails getLookupSetupDetailsById(Long id) {
        return lookupSetupDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lookup setup details not found with id: " + id));
    }

    @Override
    public LookupSetupDetails store(LookupSetupDetailsRequest request) {
        LookupSetupDetails lookupSetupDetails = new LookupSetupDetails();
        lookupSetupDetails.setName(request.name());
        lookupSetupDetails.setDetails(request.details());
        lookupSetupDetails.setStatus(request.status());

        lookupSetupDetails.setSetup(lookupSetupEntryRepository.findById(request.lookupSetupEntryId())
                .orElseThrow(() -> new LeaveTypeNotFoundException("Lookup setup details not found with id: " + request.lookupSetupEntryId())));

        return lookupSetupDetailsRepository.save(lookupSetupDetails);
    }

    @Override
    public LookupSetupDetails updateLookupSetupDetails(Long id, LookupSetupDetailsRequest request) {

        LookupSetupDetails lookupSetupDetails = lookupSetupDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lookup setup details not found with id: " + id));

        lookupSetupDetails.setName(request.name() != null ? request.name() : lookupSetupDetails.getName());
        lookupSetupDetails.setDetails(request.details() != null ? request.details(): lookupSetupDetails.getDetails());
        lookupSetupDetails.setStatus(request.status() != null ? request.status() : lookupSetupDetails.getStatus());

        lookupSetupDetails.setSetup(request.lookupSetupEntryId() != null ? lookupSetupEntryRepository.findById(request.lookupSetupEntryId()).orElse(null) : lookupSetupDetails.getSetup());
        return lookupSetupDetailsRepository.save(lookupSetupDetails);
    }

    @Override
    public void deleteLookupSetupDetails(Long id) {
        LookupSetupDetails lookupSetupDetails = lookupSetupDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lookup setup details not found with id: " + id));

        lookupSetupDetails.setDeletedAt(LocalDateTime.now());
        lookupSetupDetailsRepository.delete(lookupSetupDetails);
    }
}
