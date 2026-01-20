package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowInstanceRequest;
import com.betopia.hrm.services.workflow.backup.WorkflowInstanceService;
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
@RequestMapping("/v1/workflow-instances")
@Tag(
        name = "Workflow -> WorkflowInstance",
        description = "APIs for managing workflow instances"
)
public class WorkflowInstanceController {

    private final WorkflowInstanceService service;

    public WorkflowInstanceController(WorkflowInstanceService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow instances with pagination", description = "Retrieve all workflow instances with pagination")
    public ResponseEntity<PaginationResponse<WorkflowInstanceDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowInstanceDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow instances", description = "Retrieve all workflow instances without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowInstanceDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow instances fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow instance by id", description = "Retrieve a single workflow instance by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowInstanceDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow instances fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow instance", description = "Create/Save a new workflow instance")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowInstanceRequest request) {
        WorkflowInstanceDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow instances created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow instance", description = "Update an existing workflow instance")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowInstanceRequest request
    ) {
        WorkflowInstanceDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow instance updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow instance", description = "Remove a workflow instance from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow instance deleted successfully");
    }
}
