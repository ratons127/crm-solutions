package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.WorkingType;
import com.betopia.hrm.domain.company.request.WorkingTypeRequest;
import com.betopia.hrm.services.company.WorkingTypeService;
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
@RequestMapping("/v1/workingTypes")
@Tag(
        name = "Companies Config -> Working types setup",
        description = "APIs for configurable Working types. Includes operations to create, read, update, and delete working types."
)

public class WorkingTypeController {

    private final WorkingTypeService workingTypeService;

    public WorkingTypeController(WorkingTypeService workingTypeService) {
        this.workingTypeService = workingTypeService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of working types",
            description = "Retrieves a paginated list of working types from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch working types records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<WorkingType>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<WorkingType> paginationResponse = workingTypeService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all working types",
            description = "Retrieves a list of all working types available in the system. "
                    + "This endpoint returns the complete working types collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllWorkingType()
    {
        List<WorkingType> workingTypes = workingTypeService.getAllWorkingType();

        GlobalResponse response = GlobalResponse.success(
                workingTypes,
                "All working types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new working types",
            description = "Creates a new working types in the system with the provided details. "
                    + "Required fields such as name must be included in the request body. "
                    + "Returns the created working type along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkingTypeRequest request)
    {
        WorkingType workingType = workingTypeService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                workingType,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get working types by ID",
            description = "Retrieves the details of a specific working types using the provided ID. "
                    + "If the working types with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long workingTypeId)
    {
        WorkingType workingType = workingTypeService.show(workingTypeId);

        GlobalResponse response = GlobalResponse.success(
                workingType,
                "Working Types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update working types by ID",
            description = "Updates the details of an existing working types using the provided ID. "
                    + "This endpoint allows modifying working types name or other attributes. "
                    + "If the working types with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long workingTypeId,
                                                 @Valid @RequestBody WorkingTypeRequest request)
    {
        WorkingType workingType = workingTypeService.update(workingTypeId, request);

        GlobalResponse response = GlobalResponse.success(
                workingType,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete working types by ID",
            description = "Deletes a specific working types from the system using the provided ID. "
                    + "If the working types does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove working types records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long workingTypeId)
    {
        workingTypeService.delete(workingTypeId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Working types deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
