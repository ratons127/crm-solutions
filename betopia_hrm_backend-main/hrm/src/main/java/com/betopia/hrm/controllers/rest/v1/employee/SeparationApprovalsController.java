package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.SeparationApprovals;
import com.betopia.hrm.domain.employee.request.SeparationApprovalsRequest;
import com.betopia.hrm.services.employee.separationapprovals.SeparationApprovalsService;
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
@RequestMapping("/v1/separation-approval")
@Tag(
        name = "Employee Management -> Separations Approval",
        description = "APIs for configurable employee separations. Includes operations to create, read, update, and delete employee separations."
)

public class SeparationApprovalsController {

    private final SeparationApprovalsService separationApprovalsService;
    public SeparationApprovalsController(SeparationApprovalsService separationApprovalsService) {
        this.separationApprovalsService = separationApprovalsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all separations approval with pagination", description = "Retrieve all separations approval with pagination")
    public ResponseEntity<PaginationResponse<SeparationApprovals>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<SeparationApprovals> paginationResponse = separationApprovalsService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all separations approval", description = "Retrieve all separations approval without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<SeparationApprovals> separationApprovals = separationApprovalsService.getAll();
        return ResponseBuilder.ok(separationApprovals, "All separations approval fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get separations approval by id", description = "Retrieve a single separations approval by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        SeparationApprovals separationApprovals = separationApprovalsService.show(id);
        return ResponseBuilder.ok(separationApprovals, "Separations Approval fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store separations approval", description = "Create/Save a new separations approval")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody SeparationApprovalsRequest request) {
        SeparationApprovals separationApprovals = separationApprovalsService.store(request);
        return ResponseBuilder.created(separationApprovals, "Separations Approval created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update separations approval", description = "Update an existing separations approval")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SeparationApprovalsRequest request) {
        SeparationApprovals separationApprovals = separationApprovalsService.update(id, request);
        return ResponseBuilder.ok(separationApprovals, "Separations Approval updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete separations approval", description = "Remove a separations approvalt from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        separationApprovalsService.destroy(id);
        return ResponseBuilder.noContent("Separations Approval deleted successfully");
    }
}
