package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.request.BusinessUnitRequest;
import com.betopia.hrm.services.company.businessUnit.BusinessUnitService;
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
@RequestMapping("/v1/business-unit")
@Tag(
        name = "Company -> Business Unit",
        description = "APIs for managing Business Unit. Includes operations to create, read, update, and delete businessUnit information."
)
public class BusinessUnitController {

    private final BusinessUnitService businessUnitService;
    public BusinessUnitController(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of business units",
            description = "Retrieves a paginated list of business units from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch business unit records in a paginated format instead of retrieving all at once."
    )
    @PreAuthorize("hasAuthority('business-unit-list')")
    public ResponseEntity<PaginationResponse<BusinessUnit>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<BusinessUnit> paginationResponse = businessUnitService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all Business Units",
            description = "Retrieves a list of all Business Units available in the system. "
                    + "This endpoint returns the complete Business Unit collection without pagination. "
                    + "Use it when you need to fetch all Business Units at once."
    )
//    @PreAuthorize("hasAuthority('business-unit-list')")
    public ResponseEntity<GlobalResponse> getAllBusinessUnits()
    {
        List<BusinessUnit> businessUnits = businessUnitService.getAllBusinessUnits();
        GlobalResponse response = GlobalResponse.success(
                businessUnits,
                "All Business Unit fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new business unit",
            description = "Creates a new business unit in the system with the provided details. "
                    + "Required fields such as business unit name, address, and contact information must be included in the request body. "
                    + "Returns the created business unit information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('business-unit-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody BusinessUnitRequest request)
    {
        BusinessUnit businessUnit = businessUnitService.store(request);
        GlobalResponse response = GlobalResponse.success(
                businessUnit,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get Business Unit by ID",
            description = "Retrieves the details of a specific Business Unit using the provided ID. "
                    + "If the Business Unit with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('business-unit-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long businessUnitId)
    {
        BusinessUnit businessUnit = businessUnitService.show(businessUnitId);
        GlobalResponse response = GlobalResponse.success(
                businessUnit,
                "Business Unit fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update Business Unit by ID",
            description = "Updates the details of an existing Business Unit using the provided ID. "
                    + "This endpoint allows modifying Business Unit information such as name, location, or other attributes. "
                    + "If the Business Unit with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('business-unit-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long businessUnitId,
                                                 @Valid @RequestBody BusinessUnitRequest request)
    {
        BusinessUnit businessUnit = businessUnitService.update(businessUnitId, request);
        GlobalResponse response = GlobalResponse.success(
                businessUnit,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete Business Unit by ID",
            description = "Deletes a specific Business Unit from the system using the provided ID. "
                    + "If the Business Unit does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove Business Unit records."
    )
//    @PreAuthorize("hasAuthority('business-unit-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long businessUnitId)
    {
        businessUnitService.destroy(businessUnitId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "Business Unit deleted successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
