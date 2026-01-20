package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmploymentStatus;
import com.betopia.hrm.domain.employee.request.EmploymentStatusRequest;
import com.betopia.hrm.services.employee.employmentstatus.EmploymentStatusService;
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
@RequestMapping("/v1/employment-status")
@Tag(
        name = "Employee Management -> Employee status",
        description = "APIs for configurable employee status. Includes operations to create, read, update, and delete employee status."
)
public class EmploymentStatusController {

    private final EmploymentStatusService employmentStatusService;

    public EmploymentStatusController(EmploymentStatusService employmentStatusService) {
        this.employmentStatusService = employmentStatusService;
    }

    @GetMapping
    @Operation(summary = "1. Get all employee status with pagination", description = "Retrieve all employee status with pagination")
    public ResponseEntity<PaginationResponse<EmploymentStatus>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<EmploymentStatus> paginationResponse = employmentStatusService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all employee status", description = "Retrieve all employee status without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<EmploymentStatus> employmentStatus = employmentStatusService.getAllEmploymentStatuses();
        return ResponseBuilder.ok(employmentStatus, "All employee status fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get employee status by id", description = "Retrieve a single employee status by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        EmploymentStatus employmentStatus = employmentStatusService.getEmploymentStatusById(id);
        return ResponseBuilder.ok(employmentStatus, "Employee status fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store employee status", description = "Create/Save a new employee status")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmploymentStatusRequest request) {
        EmploymentStatus createdEmploymentStatus = employmentStatusService.store(request);
        return ResponseBuilder.created(createdEmploymentStatus, "Employee status created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update employee status", description = "Update an existing employee status")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody EmploymentStatusRequest request) {
        EmploymentStatus updatedEmploymentStatus = employmentStatusService.updateEmploymentStatus(id, request);
        return ResponseBuilder.ok(updatedEmploymentStatus, "Employee status updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete employee status", description = "Remove a employee status from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        employmentStatusService.deleteEmploymentStatus(id);
        return ResponseBuilder.noContent("Employee status deleted successfully");
    }
}
