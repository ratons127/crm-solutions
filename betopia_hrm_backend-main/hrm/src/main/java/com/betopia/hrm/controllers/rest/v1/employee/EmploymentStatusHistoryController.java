package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmploymentStatusHistory;
import com.betopia.hrm.domain.employee.request.EmploymentStatusHistoryRequest;
import com.betopia.hrm.services.employee.employmentstatushistory.EmploymentStatusHistoryService;
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
@RequestMapping("/v1/employment-status-history")
@Tag(
        name = "Employee Management -> Employee status history",
        description = "APIs for configurable employee status. Includes operations to create, read, update, and delete employee status history."
)
public class EmploymentStatusHistoryController {

    private final EmploymentStatusHistoryService employmentStatusHistoryService;

    public EmploymentStatusHistoryController(EmploymentStatusHistoryService employmentStatusHistoryService) {
        this.employmentStatusHistoryService = employmentStatusHistoryService;
    }

    @GetMapping
    @Operation(summary = "1. Get all employee status history with pagination", description = "Retrieve all employee status history with pagination")
    public ResponseEntity<PaginationResponse<EmploymentStatusHistory>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<EmploymentStatusHistory> paginationResponse = employmentStatusHistoryService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all employee status history", description = "Retrieve all employee status history without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<EmploymentStatusHistory> employmentStatus = employmentStatusHistoryService.getAllEmploymentStatusHistories();
        return ResponseBuilder.ok(employmentStatus, "All employee status history fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get employee status history by id", description = "Retrieve a single employee status history by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        EmploymentStatusHistory employmentStatusHistory = employmentStatusHistoryService.getEmploymentStatusHistoryById(id);
        return ResponseBuilder.ok(employmentStatusHistory, "Employee status history fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store employee status history", description = "Create/Save a new employee status history")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmploymentStatusHistoryRequest request) {
        EmploymentStatusHistory createdEmploymentStatusHistory = employmentStatusHistoryService.store(request);
        return ResponseBuilder.created(createdEmploymentStatusHistory, "Employee status history created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update employee status history", description = "Update an existing employee status history")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody EmploymentStatusHistoryRequest request) {
        EmploymentStatusHistory updatedEmploymentStatusHistory = employmentStatusHistoryService.updateEmploymentStatusHistory(id, request);
        return ResponseBuilder.ok(updatedEmploymentStatusHistory, "Employee status history updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete employee status history", description = "Remove a employee status history from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        employmentStatusHistoryService.deleteEmploymentStatusHistory(id);
        return ResponseBuilder.noContent("Employee status history deleted successfully");
    }
}
