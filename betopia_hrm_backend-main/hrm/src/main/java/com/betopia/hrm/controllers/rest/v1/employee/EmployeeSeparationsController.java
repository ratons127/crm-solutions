package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeSeparations;
import com.betopia.hrm.domain.employee.request.EmployeeSeparationsRequest;
import com.betopia.hrm.services.employee.employeeseparations.EmployeeSeparationsService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/employee-separations")
@Tag(
        name = "Employee Management -> Employee Separations",
        description = "APIs for configurable employee separations. Includes operations to create, read, update, and delete employee separations."
)
public class EmployeeSeparationsController {

    private final EmployeeSeparationsService employeeSeparationsService;

    public EmployeeSeparationsController(EmployeeSeparationsService employeeSeparationsService) {
        this.employeeSeparationsService = employeeSeparationsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all employee separations with pagination", description = "Retrieve all employee separations with pagination")
    public ResponseEntity<PaginationResponse<EmployeeSeparations>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<EmployeeSeparations> paginationResponse = employeeSeparationsService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all employee separations", description = "Retrieve all employee separations without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<EmployeeSeparations> separations = employeeSeparationsService.getAll();
        return ResponseBuilder.ok(separations, "All employee separations fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get employee separations by id", description = "Retrieve a single employee separations by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        EmployeeSeparations separations = employeeSeparationsService.show(id);
        return ResponseBuilder.ok(separations, "Employee Separations fetched successfully");
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalResponse> store(
            @RequestPart("data") @Valid EmployeeSeparationsRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        EmployeeSeparations separation = employeeSeparationsService.saveSeparation(request, files);
        return ResponseBuilder.created(separation, "Employee separation created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update employee separations", description = "Update an existing employee separation with optional files")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @RequestPart("data") @Valid EmployeeSeparationsRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        EmployeeSeparations separation = employeeSeparationsService.updateSeparation(id, request, files);
        return ResponseBuilder.ok(separation, "Employee Separation updated successfully");
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete employee separations", description = "Remove a employee separations from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        employeeSeparationsService.destroy(id);
        return ResponseBuilder.noContent("Employee Separations deleted successfully");
    }

    @GetMapping("/separation")
    @Operation(summary = "6. Get employee separation", description = " Get employee separations by status")
    public ResponseEntity<List<EmployeeSeparations>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(employeeSeparationsService.getByStatus(status));
    }

}
