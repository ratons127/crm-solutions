package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.request.DesignationRequest;
import com.betopia.hrm.services.employee.DesignationService;
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
@RequestMapping("/v1/designations")
@Tag(
        name = "Employee -> Designation",
        description = "APIs for managing designations. Includes operations to create, read, update, and delete designation information."
)
public class DesignationController {

    private final DesignationService designationService;
    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of designations",
            description = "Retrieves a paginated list of designations from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch designation records in a paginated format instead of retrieving all at once."
    )
    @PreAuthorize("hasAuthority('designation-list')")
    public ResponseEntity<PaginationResponse<Designation>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<Designation> paginationResponse = designationService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }


    @GetMapping("/all")
    @Operation(
            summary = "2. Get all designations",
            description = "Retrieves a list of all designations available in the system. "
                    + "This endpoint returns the complete designation collection without pagination. "
                    + "Use it when you need to fetch all designation at once."
    )
    @PreAuthorize("hasAuthority('designation-list')")
    public ResponseEntity<GlobalResponse> getAllDesignations()
    {
        List<Designation> designations = designationService.getAllDesignations();
        GlobalResponse response = GlobalResponse.success(
                designations,
                "All designations fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. Get designation by ID",
            description = "Retrieves the details of a specific designation using the provided ID. "
                    + "If the designation with the given ID does not exist, a 404 Not Found response will be returned."
    )
    @PreAuthorize("hasAuthority('designation-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long designationId)
    {
        Designation designation = designationService.show(designationId);
        GlobalResponse response = GlobalResponse.success(
                designation,
                "Designation fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(
            summary = "3. Create a new designation",
            description = "Creates a new designation in the system with the provided details. "
                    + "Required fields such as designation name, status, and description must be included in the request body. "
                    + "Returns the created designation information along with its unique ID."
    )
    @PreAuthorize("hasAuthority('designation-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody DesignationRequest request)
    {
        Designation designation = designationService.store(request);
        GlobalResponse response = GlobalResponse.success(
                designation,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("{id}")
    @Operation(
            summary = "5. Update designation by ID",
            description = "Updates the details of an existing designation using the provided ID. "
                    + "This endpoint allows modifying designation information such as name, description, or other attributes. "
                    + "If the designation with the given ID does not exist, a 404 Not Found response will be returned."
    )
    @PreAuthorize("hasAuthority('designation-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long designationId,
                                                 @Valid @RequestBody DesignationRequest request)
    {
        Designation designation = designationService.update(designationId, request);
        GlobalResponse response = GlobalResponse.success(
                designation,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete designation by ID",
            description = "Deletes a specific designation from the system using the provided ID. "
                    + "If the designation does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove designation records."
    )
    @PreAuthorize("hasAuthority('designation-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long designationId)
    {
        designationService.destroy(designationId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "designation deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
