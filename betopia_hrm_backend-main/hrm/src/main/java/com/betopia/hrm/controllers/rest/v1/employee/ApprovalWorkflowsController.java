package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.ApprovalWorkflows;
import com.betopia.hrm.domain.employee.request.ApprovalWorkflowsRequest;
import com.betopia.hrm.services.employee.approvalworkflows.ApprovalWorkflowsService;
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
@RequestMapping("/v1/approval-workflows")
@Tag(
        name = "Employee Management -> Approval Workflows",
        description = "APIs for configurable approval workflows. Includes operations to create, read, update, and delete approval workflows."
)
public class ApprovalWorkflowsController {

    private final ApprovalWorkflowsService approvalWorkflowsService;

    public ApprovalWorkflowsController(ApprovalWorkflowsService approvalWorkflowsService) {
        this.approvalWorkflowsService = approvalWorkflowsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all approval workflows with pagination", description = "Retrieve all approval workflows with pagination")
    public ResponseEntity<PaginationResponse<ApprovalWorkflows>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<ApprovalWorkflows> paginationResponse = approvalWorkflowsService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all approval workflows", description = "Retrieve all approval workflows without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<ApprovalWorkflows> approvalWorkflows = approvalWorkflowsService.getAll();
        return ResponseBuilder.ok(approvalWorkflows, "All approval workflows fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get approval workflows by id", description = "Retrieve a single approval workflows by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        ApprovalWorkflows approvalWorkflows = approvalWorkflowsService.show(id);
        return ResponseBuilder.ok(approvalWorkflows, "Approval Workflows fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store approval workflows", description = "Create/Save a new approval workflows")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ApprovalWorkflowsRequest request) {
        ApprovalWorkflows approvalWorkflows = approvalWorkflowsService.store(request);
        return ResponseBuilder.created(approvalWorkflows, "Approval Workflows created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update approval workflows", description = "Update an existing approval workflows")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ApprovalWorkflowsRequest request) {
        ApprovalWorkflows approvalWorkflows = approvalWorkflowsService.update(id, request);
        return ResponseBuilder.ok(approvalWorkflows, "Approval Workflows updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete tacting-assignment", description = "Remove a approval workflows from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        approvalWorkflowsService.destroy(id);
        return ResponseBuilder.noContent("Approval Workflows deleted successfully");
    }
}
