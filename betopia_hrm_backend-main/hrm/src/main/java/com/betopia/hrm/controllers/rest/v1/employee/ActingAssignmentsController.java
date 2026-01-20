package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.ActingAssignments;
import com.betopia.hrm.domain.employee.request.ActingAssignmentsRequest;
import com.betopia.hrm.services.employee.actingassignments.ActingAssignmentsService;
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
@RequestMapping("/v1/acting-assignment")
@Tag(
        name = "Employee Management -> Acting Assignment",
        description = "APIs for configurable acting-assignment. Includes operations to create, read, update, and delete acting-assignment."
)
public class ActingAssignmentsController {

    private final ActingAssignmentsService actingAssignmentsService;

    public ActingAssignmentsController(ActingAssignmentsService actingAssignmentsService) {
        this.actingAssignmentsService = actingAssignmentsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all acting-assignment with pagination", description = "Retrieve all acting-assignment with pagination")
    public ResponseEntity<PaginationResponse<ActingAssignments>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<ActingAssignments> paginationResponse = actingAssignmentsService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all acting-assignment", description = "Retrieve all acting-assignment without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<ActingAssignments> assignments = actingAssignmentsService.getAll();
        return ResponseBuilder.ok(assignments, "All promotion request fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get acting-assignment by id", description = "Retrieve a single acting-assignment by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        ActingAssignments assignments = actingAssignmentsService.show(id);
        return ResponseBuilder.ok(assignments, "Acting Assignment fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store acting-assignment", description = "Create/Save a new acting-assignment")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ActingAssignmentsRequest request) {
        ActingAssignments assignments = actingAssignmentsService.store(request);
        return ResponseBuilder.created(assignments, "Acting Assignment created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update acting-assignment", description = "Update an existing acting-assignment")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ActingAssignmentsRequest request) {
        ActingAssignments assignments = actingAssignmentsService.update(id, request);
        return ResponseBuilder.ok(assignments, "Acting Assignment updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete acting-assignment", description = "Remove a acting-assignment from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        actingAssignmentsService.destroy(id);
        return ResponseBuilder.noContent("Acting Assignment deleted successfully");
    }
}
