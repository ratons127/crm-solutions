package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.request.EmployeeTypeRequest;
import com.betopia.hrm.services.employee.EmployeeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/v1/employeeTypes")
@Tag(
        name = "Employee Management -> Employee types setup",
        description = "APIs for configurable employee types. Includes operations to create, read, update, and delete employee types."
)
public class EmployeeTypeController {

    private final EmployeeTypeService employeeTypeService;

    public EmployeeTypeController(EmployeeTypeService employeeTypeService) {
        this.employeeTypeService = employeeTypeService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of employee types",
            description = "Retrieves a paginated list of employee types from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch employee types records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<EmployeeType>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<EmployeeType> paginationResponse = employeeTypeService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all employee types",
            description = "Retrieves a list of all employee types available in the system. "
                    + "This endpoint returns the complete employee types collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllEmployeeTypes()
    {
        List<EmployeeType> employeeTypes = employeeTypeService.getAllEmployeeTypes();

        GlobalResponse response = GlobalResponse.success(
                employeeTypes,
                "All employee types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new employee types",
            description = "Creates a new employee types in the system with the provided details. "
                    + "Required fields such as minLength,maxLength must be included in the request body. "
                    + "Returns the created password policy along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmployeeTypeRequest request)
    {
        EmployeeType employeeType = employeeTypeService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                employeeType,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get employee types by ID",
            description = "Retrieves the details of a specific employee types using the provided ID. "
                    + "If the employee types with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long employeeTypesId)
    {
        EmployeeType employeeType = employeeTypeService.show(employeeTypesId);

        GlobalResponse response = GlobalResponse.success(
                employeeType,
                "Employee Types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update employee types by ID",
            description = "Updates the details of an existing employee types using the provided ID. "
                    + "This endpoint allows modifying employee types name,description or other attributes. "
                    + "If the employee types with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long employeeTypesId,
                                                 @Valid @RequestBody EmployeeTypeRequest request)
    {
        EmployeeType employeeType = employeeTypeService.update(employeeTypesId, request);

        GlobalResponse response = GlobalResponse.success(
                employeeType,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete employee types by ID",
            description = "Deletes a specific employee types from the system using the provided ID. "
                    + "If the employee types does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove employee types records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long employeeTypesId)
    {
        employeeTypeService.delete(employeeTypesId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Employee types deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
