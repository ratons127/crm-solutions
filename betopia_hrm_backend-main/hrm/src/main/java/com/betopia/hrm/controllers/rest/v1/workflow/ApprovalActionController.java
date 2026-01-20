package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ApprovalActionDTO;
import com.betopia.hrm.domain.workflow.request.ApprovalActionRequest;
import com.betopia.hrm.services.workflow.backup.ApprovalActionService;
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
@RequestMapping("/v1/approval-actions")
@Tag(
        name = "Workflow -> ApprovalAction",
        description = "APIs for managing approval action"
)
public class ApprovalActionController {

    private final ApprovalActionService service;

    public ApprovalActionController(ApprovalActionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all approval actions with pagination", description = "Retrieve all approval actions with pagination")
    public ResponseEntity<PaginationResponse<ApprovalActionDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<ApprovalActionDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all approval actions", description = "Retrieve all approval actions without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<ApprovalActionDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All approval actions fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get approval action by id", description = "Retrieve a single approval action by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        ApprovalActionDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Approval action fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store approval action", description = "Create/Save a new approval action")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ApprovalActionRequest request) {
        ApprovalActionDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Approval action created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update approval action", description = "Update an existing approval action")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ApprovalActionRequest request
    ) {
        ApprovalActionDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Approval action updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete approval action", description = "Remove a approval action from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Approval action deleted successfully");
    }
}
