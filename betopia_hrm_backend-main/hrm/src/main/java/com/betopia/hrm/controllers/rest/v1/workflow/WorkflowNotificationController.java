package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowNotificationDTO;
import com.betopia.hrm.domain.workflow.request.WorkflowNotificationRequest;
import com.betopia.hrm.services.workflow.workflownotification.WorkflowNotificationService;
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
@RequestMapping("/v1/workflow-notifications")
@Tag(
        name = "Workflow -> WorkflowNotification",
        description = "APIs for managing workflow notification"
)
public class WorkflowNotificationController {

    private final WorkflowNotificationService service;

    public WorkflowNotificationController(WorkflowNotificationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all workflow notifications with pagination", description = "Retrieve all workflow notifications with pagination")
    public ResponseEntity<PaginationResponse<WorkflowNotificationDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkflowNotificationDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all workflow notifications", description = "Retrieve all workflow notifications without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<WorkflowNotificationDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All workflow notifications fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get workflow notification by id", description = "Retrieve a single workflow notification by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        WorkflowNotificationDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Workflow notification fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store workflow notification", description = "Create/Save a new workflow notification")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkflowNotificationRequest request) {
        WorkflowNotificationDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Workflow notification created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update workflow notification", description = "Update an existing workflow notification")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody WorkflowNotificationRequest request
    ) {
        WorkflowNotificationDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Workflow notification updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete workflow notification", description = "Remove a workflow notification from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Workflow notification deleted successfully");
    }
}
