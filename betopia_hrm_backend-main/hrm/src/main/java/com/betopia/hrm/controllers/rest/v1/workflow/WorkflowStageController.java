package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowStageDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowStageRequest;
import com.betopia.hrm.services.workflow.workflowstage.WorkflowStageService;
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
@RequestMapping("/v1/workflow-stages")
@Tag(
        name = "Workflow -> WorkflowStage",
        description = "APIs for managing workflow stages"
)
public class WorkflowStageController {

    private final WorkflowStageService service;

    public WorkflowStageController(WorkflowStageService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow stages with pagination", description = "Retrieve all workflow stages with pagination")
    public ResponseEntity<PaginationResponse<WorkflowStageDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowStageDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow stages", description = "Retrieve all workflow stages without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowStageDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow stages fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow stage by id", description = "Retrieve a single workflow stage by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowStageDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow stage fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow stage", description = "Create/Save a new workflow stage")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowStageRequest request) {
        WorkflowStageDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow stage created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow stage", description = "Update an existing workflow stage")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowStageRequest request
    ) {
        WorkflowStageDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow stage updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow stage", description = "Remove a workflow stage from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow stage deleted successfully");
    }
}
