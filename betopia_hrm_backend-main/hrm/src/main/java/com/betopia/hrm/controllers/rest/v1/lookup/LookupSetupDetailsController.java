package com.betopia.hrm.controllers.rest.v1.lookup;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.betopia.hrm.domain.lookup.entity.LookupSetupEntry;
import com.betopia.hrm.domain.lookup.request.LookupSetupDetailsRequest;
import com.betopia.hrm.domain.lookup.request.LookupSetupEntryRequest;
import com.betopia.hrm.services.lookup.lookupsetupdetails.LookupSetupDetailsService;
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
@RequestMapping("/v1/lookup-details")
@Tag(
        name = "Lookup Management -> Lookup Setup Details",
        description = "APIs for managing lookup setup details. Includes operations to create, read, update, and delete lookup setup details information."
)
public class LookupSetupDetailsController {

    private final LookupSetupDetailsService lookupSetupDetailsService;

    public LookupSetupDetailsController(LookupSetupDetailsService lookupSetupDetailsService) {
        this.lookupSetupDetailsService = lookupSetupDetailsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all lookup setup details with pagination", description = "Retrieve all lookup setup details with pagination")
    public ResponseEntity<PaginationResponse<LookupSetupDetails>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam Long setupId // Changed from String parentName to Long setupId// Add this parameter
    ) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LookupSetupDetails> paginationResponse = lookupSetupDetailsService.index(direction, page, perPage, setupId);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all lookup setup details", description = "Retrieve all lookup setup details without pagination")
    public ResponseEntity<GlobalResponse> findAllLookupSetupDetails() {
        List<LookupSetupDetails> lookupSetupDetails = lookupSetupDetailsService.findAllLookupSetupDetails();
        return ResponseBuilder.ok(lookupSetupDetails, "All lookup setup entry fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get lookup setup details by id", description = "Retrieve a single lookup setup details by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LookupSetupDetails lookupSetupDetails = lookupSetupDetailsService.getLookupSetupDetailsById(id);
        return ResponseBuilder.ok(lookupSetupDetails, "Lookup setup details fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store lookup setup details", description = "Create/Save a new lookup setup details")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LookupSetupDetailsRequest request) {
        LookupSetupDetails createdLookupSetupDetails = lookupSetupDetailsService.store(request);
        return ResponseBuilder.created(createdLookupSetupDetails, "Lookup setup details created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update lookup setup details", description = "Update an existing lookup setup details")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LookupSetupDetailsRequest request) {
        LookupSetupDetails updatedLookupSetupDetails = lookupSetupDetailsService.updateLookupSetupDetails(id, request);
        return ResponseBuilder.ok(updatedLookupSetupDetails, "Lookup setup details updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete lookup setup details", description = "Remove a lookup setup details from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        lookupSetupDetailsService.deleteLookupSetupDetails(id);
        return ResponseBuilder.noContent("Lookup setup details deleted successfully");
    }

}
