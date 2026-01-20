package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowTemplateDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowTemplateRequest;
import com.betopia.hrm.services.workflow.workflowtemplate.WorkflowTemplateService;
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
@RequestMapping("/v1/workflow-templates")
@Tag(
        name = "Workflow -> Workflow Template",
        description = "APIs for managing workflow template"
)
public class WorkflowTemplateController {

    private final WorkflowTemplateService service;

    public WorkflowTemplateController(WorkflowTemplateService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow templates with pagination", description = "Retrieve all workflow templates with pagination")
    public ResponseEntity<PaginationResponse<WorkflowTemplateDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowTemplateDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow templates", description = "Retrieve all workflow templates without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowTemplateDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow templates fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow template by ID", description = "Retrieve a single workflow template by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowTemplateDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow template fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow template", description = "Create/Save a new workflow template")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowTemplateRequest request) {
        WorkflowTemplateDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow templates created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow template", description = "Update an existing workflow template")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowTemplateRequest request
    ) {
        WorkflowTemplateDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow template updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow template", description = "Remove a workflow template from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow template deleted successfully");
    }
}
