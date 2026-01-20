package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeGroup;
import com.betopia.hrm.domain.employee.request.EmployeeGroupRequest;
import com.betopia.hrm.services.employee.EmployeeGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/employee-group")
@Tag(name = "Employee Management -> Employee groups", description = "Operations related to managing employee group")
public class EmployeeGroupController {

    private final EmployeeGroupService employeeGroupService;

    public EmployeeGroupController(EmployeeGroupService employeeGroupService) {
        this.employeeGroupService = employeeGroupService;
    }

    @GetMapping
    @Operation(summary = "1. Get all employee groups with pagination", description = "Retrieve all employee groups with pagination")
    @PreAuthorize("hasAuthority('employee-group-list')")
    public ResponseEntity<PaginationResponse<EmployeeGroup>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<EmployeeGroup> paginationResponse = employeeGroupService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all employee groups", description = "Retrieve all employee groups without pagination")
//    @PreAuthorize("hasAuthority('employee-group-list')")
    public ResponseEntity<GlobalResponse> getAllEmployeeGroups()
    {
        List<EmployeeGroup> employeeGroups = employeeGroupService.getAllEmployeeGroups();

        GlobalResponse response = GlobalResponse.success(
                employeeGroups,
                "All employee groups fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store user", description = "Creating a new user into the system")
//    @PreAuthorize("hasAuthority('employee-group-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmployeeGroupRequest request)
    {
        System.out.println("Request: " + request);
        EmployeeGroup createdEmployeeGroup = employeeGroupService.store(request);
        GlobalResponse response = GlobalResponse.success(
                createdEmployeeGroup,
                "Store successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Gey by id single employee group", description = "A single employee group retrieve from database by id")
//    @PreAuthorize("hasAuthority('employee-group-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        EmployeeGroup employeeGroup = employeeGroupService.show(id);
        GlobalResponse response = GlobalResponse.success(
                employeeGroup,
                "Employee group fetched successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update employee group", description = "Update single employee group into the system")
//    @PreAuthorize("hasAuthority('employee-group-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeGroupRequest request)
    {
        EmployeeGroup updatedEmployeeGroup = employeeGroupService.update(id, request);
        GlobalResponse response = GlobalResponse.success(
                updatedEmployeeGroup,
                "Updated successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete employee group", description = "Remove a single employee group into the system")
//    @PreAuthorize("hasAuthority('employee-group-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id)
    {
        employeeGroupService.destroy(id);
        GlobalResponse response = GlobalResponse.success(
                null,
                "User deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
