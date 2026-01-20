package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeEducationInfo;
import com.betopia.hrm.domain.employee.request.EmployeeEducationInfoRequest;
import com.betopia.hrm.services.employee.EmployeeEducationInfoService;
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
@RequestMapping("/v1/employeeEducationInfos")
@Tag(
        name = "Employee Management -> EmployeeEducationInfo setup",
        description = "APIs for configurable EmployeeEducationInfo. Includes operations to create, read, update, and delete grade."
)
public class EmployeeEducationInfoController {

    private final EmployeeEducationInfoService employeeEducationInfoService;

    public EmployeeEducationInfoController(EmployeeEducationInfoService employeeEducationInfoService) {
        this.employeeEducationInfoService = employeeEducationInfoService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of EmployeeEducationInfo types",
            description = "Retrieves a paginated list of EmployeeEducationInfo types from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch EmployeeEducationInfo types records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<EmployeeEducationInfo>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<EmployeeEducationInfo> paginationResponse = employeeEducationInfoService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all EmployeeEducationInfo types",
            description = "Retrieves a list of all EmployeeEducationInfo types available in the system. "
                    + "This endpoint returns the complete EmployeeEducationInfo types collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllEmployeeEducationInfo()
    {
        List<EmployeeEducationInfo> employeeEducationInfo = employeeEducationInfoService.getAllEmployeeEducationInfo();

        GlobalResponse response = GlobalResponse.success(
                employeeEducationInfo,
                "All employeeEducationInfo types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new EmployeeEducationInfo types",
            description = "Creates a new EmployeeEducationInfo types in the system with the provided details. "
                    + "Returns the created EmployeeEducationInfo with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmployeeEducationInfoRequest request)
    {
        EmployeeEducationInfo employeeEducationInfo = employeeEducationInfoService.store(request);

        GlobalResponse response = GlobalResponse.success(
                employeeEducationInfo,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get employeeEducationInfo types by ID",
            description = "Retrieves the details of a specific employee types using the provided ID. "
                    + "If the employeeEducationInfo with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long employeeEducationInfoId)
    {
        EmployeeEducationInfo employeeEducationInfo = employeeEducationInfoService.show(employeeEducationInfoId);

        GlobalResponse response = GlobalResponse.success(
                employeeEducationInfo,
                "EmployeeEducationInfo Types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update EmployeeEducationInfo types by ID",
            description = "Updates the details of an existing employee types using the provided ID. "
                    + "This endpoint allows modifying result,subject,passingYear attributes. "
                    + "If the EmployeeEducationInfo types with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long employeeEducationInfoId,
                                                 @Valid @RequestBody EmployeeEducationInfoRequest request)
    {
        EmployeeEducationInfo employeeEducationInfo = employeeEducationInfoService.update(employeeEducationInfoId, request);

        GlobalResponse response = GlobalResponse.success(
                employeeEducationInfo,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete EmployeeEducationInfo  by ID",
            description = "Deletes a specific EmployeeEducationInfo from the system using the provided ID. "
                    + "If the EmployeeEducationInfo does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove EmployeeEducationInfo records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long employeeEducationInfoId)
    {
        employeeEducationInfoService.delete(employeeEducationInfoId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "EmployeeEducationInfo deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
