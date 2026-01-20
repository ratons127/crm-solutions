package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeAssignmentHistory;
import com.betopia.hrm.domain.employee.request.EmployeeAssignmentHistoryRequest;
import com.betopia.hrm.services.employee.employeeassignmenthistory.EmployeeAssignmentHistoryService;
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
@RequestMapping("/v1/employee-assignments")
@Tag(
        name = "Employee Management -> Employee Assignment History",
        description = "APIs for configurable employee assignment history. Includes operations to create, read, update," +
                " and delete employee assignment history."
)
public class EmployeeAssignmentHistoryController {

    private final EmployeeAssignmentHistoryService employeeAssignmentHistoryService;

    public EmployeeAssignmentHistoryController(EmployeeAssignmentHistoryService employeeAssignmentHistoryService) {
        this.employeeAssignmentHistoryService = employeeAssignmentHistoryService;
    }

    @GetMapping
    @Operation(summary = "1. Get all employee assignment history with pagination", description = "Retrieve all employee assignment history " +
            "with pagination")
    public ResponseEntity<PaginationResponse<EmployeeAssignmentHistory>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<EmployeeAssignmentHistory> paginationResponse = employeeAssignmentHistoryService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all employee assignment history", description = "Retrieve all employee assignment history without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<EmployeeAssignmentHistory> assignments = employeeAssignmentHistoryService.getAll();
        return ResponseBuilder.ok(assignments, "All Employee Assignment History fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get employee assignment history by id", description = "Retrieve a single employee assignment history by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        EmployeeAssignmentHistory assignments = employeeAssignmentHistoryService.show(id);
        return ResponseBuilder.ok(assignments, "Employee Assignment History fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store employee assignment history", description = "Create/Save a new employee assignment history")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmployeeAssignmentHistoryRequest request) {
        EmployeeAssignmentHistory assignments = employeeAssignmentHistoryService.store(request);
        return ResponseBuilder.created(assignments, "Employee Assignment History created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update employee assignment history", description = "Update an existing employee assignment history")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeAssignmentHistoryRequest request) {
        EmployeeAssignmentHistory assignments = employeeAssignmentHistoryService.update(id, request);
        return ResponseBuilder.ok(assignments, "Employee Assignment History updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete employee assignment history", description = "Remove a employee assignment history from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        employeeAssignmentHistoryService.destroy(id);
        return ResponseBuilder.noContent("Employee Assignment History deleted successfully");
    }
}
