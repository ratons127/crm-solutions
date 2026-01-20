package com.betopia.hrm.services.lookup.lookupsetupentry.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.lookup.entity.LookupSetupEntry;
import com.betopia.hrm.domain.lookup.repository.LookupSetupEntryRepository;
import com.betopia.hrm.domain.lookup.request.LookupSetupEntryRequest;
import com.betopia.hrm.services.lookup.lookupsetupentry.LookupSetupEntryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LookupSetupEntryServiceImpl implements LookupSetupEntryService {

    private final LookupSetupEntryRepository lookupSetupEntryRepository;

    public LookupSetupEntryServiceImpl(LookupSetupEntryRepository lookupSetupEntryRepository) {
        this.lookupSetupEntryRepository = lookupSetupEntryRepository;
    }

    @Override
    public PaginationResponse<LookupSetupEntry> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LookupSetupEntry> lookupSetupEntryPage = lookupSetupEntryRepository.findAll(pageable);
        List<LookupSetupEntry> lookupSetupEntryRules = lookupSetupEntryPage.getContent();

        PaginationResponse<LookupSetupEntry> response = new PaginationResponse<>();
        response.setData(lookupSetupEntryRules);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All lookup setup entry fetched successfully");

        Links links = Links.fromPage(lookupSetupEntryPage, "/lookup-setup-entry");
        response.setLinks(links);

        Meta meta = Meta.fromPage(lookupSetupEntryPage, "/lookup-setup-entry");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LookupSetupEntry> findAllLookupSetupEntries() {
        return lookupSetupEntryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    }

    @Override
    public LookupSetupEntry getLookupSetupEntryById(Long id) {
        return lookupSetupEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lookup setup entry not found with id: " + id));
    }

    @Override
    public LookupSetupEntry store(LookupSetupEntryRequest request) {
        LookupSetupEntry lookupSetupEntry = new LookupSetupEntry();
        lookupSetupEntry.setName(request.name());
        lookupSetupEntry.setStatus(request.status());
        return lookupSetupEntryRepository.save(lookupSetupEntry);
    }

    @Override
    public LookupSetupEntry updateLookupSetupEntry(Long id, LookupSetupEntryRequest request) {
        LookupSetupEntry lookupSetupEntry = lookupSetupEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lookup setup entry not found with id: " + id));

        lookupSetupEntry.setName(request.name() != null ? request.name() : lookupSetupEntry.getName());
        lookupSetupEntry.setStatus(request.status() != null ? request.status() : lookupSetupEntry.getStatus());

        return lookupSetupEntryRepository.save(lookupSetupEntry);
    }

    @Override
    public void deleteLookupSetupEntry(Long id) {
        LookupSetupEntry lookupSetupEntry = lookupSetupEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lookup setup entry not found with id: " + id));

        lookupSetupEntry.setDeletedAt(LocalDateTime.now());
        lookupSetupEntryRepository.delete(lookupSetupEntry);

    }


}
