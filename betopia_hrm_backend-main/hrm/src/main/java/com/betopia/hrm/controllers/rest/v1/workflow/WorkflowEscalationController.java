package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowEscalationDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowEscalationRequest;
import com.betopia.hrm.services.workflow.workflowescalation.WorkflowEscalationService;
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
@RequestMapping("/v1/workflow-escalations")
@Tag(
        name = "Workflow -> WorkflowEscalation",
        description = "APIs for managing workflow escalation"
)
public class WorkflowEscalationController {

    private final WorkflowEscalationService service;

    public WorkflowEscalationController(WorkflowEscalationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow escalations with pagination", description = "Retrieve all workflow escalations with pagination")
    public ResponseEntity<PaginationResponse<WorkflowEscalationDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowEscalationDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow escalations", description = "Retrieve all workflow escalations without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowEscalationDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow escalations fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow escalation by id", description = "Retrieve a single workflow escalation by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowEscalationDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow escalation fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow escalation", description = "Create/Save a new workflow escalation")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowEscalationRequest request) {
        WorkflowEscalationDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow escalation created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow escalation", description = "Update an existing workflow escalation")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowEscalationRequest request
    ) {
        WorkflowEscalationDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow escalation updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow escalation", description = "Remove a workflow escalation from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow escalation deleted successfully");
    }
}
