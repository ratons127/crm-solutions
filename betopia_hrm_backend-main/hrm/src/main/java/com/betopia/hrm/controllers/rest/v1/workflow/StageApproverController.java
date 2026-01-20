package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.StageApproverDTO;
import com.betopia.hrm.domain.workflow.request.StageApproverRequest;
import com.betopia.hrm.services.workflow.stageapprover.StageApproverService;
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
@RequestMapping("/v1/stage-approvers")
@Tag(
        name = "Workflow -> StageApprover",
        description = "APIs for managing stage approvers"
)
public class StageApproverController {

    private final StageApproverService service;

    public StageApproverController(StageApproverService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "1. Get all stage approvers with pagination", description = "Retrieve all stage approvers with pagination")
    public ResponseEntity<PaginationResponse<StageApproverDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<StageApproverDTO> paginationResponse = service.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all stage approvers", description = "Retrieve all stage approvers without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<StageApproverDTO> dtos = service.getAll();
        return ResponseBuilder.ok(dtos, "All stage approvers fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get stage approver by id", description = "Retrieve a single stage approver by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        StageApproverDTO dto = service.getById(id);
        return ResponseBuilder.ok(dto, "Stage approvers fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store stage approver", description = "Create/Save a new stage approver")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody StageApproverRequest request) {
        StageApproverDTO stored = service.store(request);
        return ResponseBuilder.created(stored, "Stage approver created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update stage approver", description = "Update an existing stage approver")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody StageApproverRequest request
    ) {
        StageApproverDTO dto = service.update(id, request);
        return ResponseBuilder.ok(dto, "Stage approver updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete stage approver", description = "Remove a stage approver from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseBuilder.noContent("Stage approver deleted successfully");
    }
}
