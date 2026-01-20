package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceStageDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowInstanceStageRequest;
import com.betopia.hrm.services.workflow.workflowinstancestage.WorkflowInstanceStageService;
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
@RequestMapping("/v1/workflow-instance-stages")
@Tag(
        name = "Workflow -> WorkflowInstanceStage",
        description = "APIs for managing workflow instance stage"
)
public class WorkflowInstanceStageController {

    private final WorkflowInstanceStageService service;

    public WorkflowInstanceStageController(WorkflowInstanceStageService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow instance stages with pagination", description = "Retrieve all workflow instance stages with pagination")
    public ResponseEntity<PaginationResponse<WorkflowInstanceStageDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowInstanceStageDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow instance stages", description = "Retrieve all workflow instance stages without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowInstanceStageDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow instance stages fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow instance stage by id", description = "Retrieve a single workflow instance stage by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowInstanceStageDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow instance stage fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow instance stage", description = "Create/Save a new workflow instance stage")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowInstanceStageRequest request) {
        WorkflowInstanceStageDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow instance stage created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow instance stage", description = "Update an existing workflow instance stage")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowInstanceStageRequest request
    ) {
        WorkflowInstanceStageDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow instance stage updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow instance stage", description = "Remove a workflow instance stage from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow instance stage deleted successfully");
    }
}
