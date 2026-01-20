package com.betopia.hrm.controllers.rest.v1.lookup;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.lookup.entity.LookupSetupEntry;
import com.betopia.hrm.domain.lookup.request.LookupSetupEntryRequest;
import com.betopia.hrm.services.lookup.lookupsetupentry.LookupSetupEntryService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/lookup-entry")
@Tag(
        name = "Lookup Management -> Lookup Setup Entry",
        description = "APIs for managing lookup setup entries. Includes operations to create, read, update, and delete lookup setup entry information."
)
public class LookupSetupEntryController {

    private final LookupSetupEntryService lookupSetupEntryService;

    public LookupSetupEntryController(LookupSetupEntryService lookupSetupEntryService) {
        this.lookupSetupEntryService = lookupSetupEntryService;
    }

    @GetMapping
    @Operation(summary = "1. Get all lookup setup entry with pagination", description = "Retrieve all lookup setup entry with pagination")
    public ResponseEntity<PaginationResponse<LookupSetupEntry>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LookupSetupEntry> paginationResponse = lookupSetupEntryService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all lookup setup entry", description = "Retrieve all lookup setup entry without pagination")
    public ResponseEntity<GlobalResponse> findAllLookupSetupEntries() {
        List<LookupSetupEntry> lookupSetupEntries = lookupSetupEntryService.findAllLookupSetupEntries();
        return ResponseBuilder.ok(lookupSetupEntries, "All lookup setup entry fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get lookup setup entry by id", description = "Retrieve a single lookup setup entry by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LookupSetupEntry lookupSetupEntry = lookupSetupEntryService.getLookupSetupEntryById(id);
        return ResponseBuilder.ok(lookupSetupEntry, "Lookup setup entry fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store lookup setup entry", description = "Create/Save a new lookup setup entry")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LookupSetupEntryRequest request) {
        LookupSetupEntry createdLookupSetupEntry = lookupSetupEntryService.store(request);
        return ResponseBuilder.created(createdLookupSetupEntry, "Lookup setup entry created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update lookup setup entry", description = "Update an existing lookup setup entry")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LookupSetupEntryRequest request) {
        LookupSetupEntry updatedLookupSetupEntry = lookupSetupEntryService.updateLookupSetupEntry(id, request);
        return ResponseBuilder.ok(updatedLookupSetupEntry, "Lookup setup entry updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete lookup setup entry", description = "Remove a lookup setup entry from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        lookupSetupEntryService.deleteLookupSetupEntry(id);
        return ResponseBuilder.noContent("Lookup setup entry deleted successfully");
    }
}
