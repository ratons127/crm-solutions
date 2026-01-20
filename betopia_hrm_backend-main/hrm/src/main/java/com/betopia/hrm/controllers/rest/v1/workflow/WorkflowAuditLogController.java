package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowAuditLogDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowAuditLogRequest;
import com.betopia.hrm.services.workflow.workflowauditlog.WorkflowAuditLogService;
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
@RequestMapping("/v1/workflow-audit-logs")
@Tag(
        name = "Workflow -> WorkflowAuditLog",
        description = "APIs for managing workflow audit log"
)
public class WorkflowAuditLogController {

    private final WorkflowAuditLogService service;

    public WorkflowAuditLogController(WorkflowAuditLogService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow audit logs with pagination", description = "Retrieve all workflow audit logs with pagination")
    public ResponseEntity<PaginationResponse<WorkflowAuditLogDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowAuditLogDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow audit logs", description = "Retrieve all workflow audit logs without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowAuditLogDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow audit logs fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow audit log by id", description = "Retrieve a single workflow audit log by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowAuditLogDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow audit log fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow audit log", description = "Create/Save a new workflow audit log")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowAuditLogRequest request) {
        WorkflowAuditLogDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow audit log created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow audit log", description = "Update an existing workflow audit log")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowAuditLogRequest request
    ) {
        WorkflowAuditLogDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow audit log updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow audit log", description = "Remove a workflow audit log from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow audit log deleted successfully");
    }
}
