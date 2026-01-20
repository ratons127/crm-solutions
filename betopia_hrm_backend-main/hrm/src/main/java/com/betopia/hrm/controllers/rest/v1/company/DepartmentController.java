package com.betopia.hrm.controllers.rest.v1.company;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.request.DepartmentRequest;
import com.betopia.hrm.services.company.department.DepartmentService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/v1/department")
@Tag(
        name = "Company -> Department",
        description = "APIs for managing Department. Includes operations to create, read, update, and delete Department information."
)
public class DepartmentController {

    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @Operation(summary = "1. Get all departments with pagination", description = "Retrieve departments with pagination")
    @PreAuthorize("hasAuthority('department-list')")
    public ResponseEntity<PaginationResponse<Department>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<Department> paginationResponse = departmentService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get departments", description = "Retrieve departments without pagination")
    @PreAuthorize("hasAuthority('department-list')")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseBuilder.ok(departments, "All departments fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get departments by id", description = "Retrieve a single departments by its ID")
    @PreAuthorize("hasAuthority('department-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        Department departments = departmentService.getDepartmentById(id);
        return ResponseBuilder.ok(departments, "Departments fetched successfully");
    }


    @PostMapping("/save")
    @Operation(summary = "4. Save/Store departments", description = "Create/Save a new departments")
//    @PreAuthorize("hasAuthority('department-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody DepartmentRequest request) {
        Department createdDepartment = departmentService.store(request);
        return ResponseBuilder.created(createdDepartment, "Departments created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update Departments", description = "Update an existing departments")
    @PreAuthorize("hasAuthority('department-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody DepartmentRequest request) {
        Department updatedDepartment = departmentService.updateDepartment(id, request);
        return ResponseBuilder.ok(updatedDepartment, "Departments updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('department-delete')")
    @Operation(summary = "6. Delete departments", description = "Remove a departments from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return ResponseBuilder.noContent("Departments deleted successfully");
    }

}
