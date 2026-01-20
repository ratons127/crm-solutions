package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.request.WorkplaceRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.workplace.WorkplaceService;
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
@RequestMapping("/v1/workplaces")
@Tag(
        name = "User Management -> Workplace",
        description = "APIs for managing workplaces. Includes operations to create, read, update, and delete workplace information."
)
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    public WorkplaceController(WorkplaceService workplaceService) {
        this.workplaceService = workplaceService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of workplacees",
            description = "Retrieves a paginated list of workplaces from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch workplace records in a paginated format instead of retrieving all at once."
    )
    @PreAuthorize("hasAuthority('work-place-list')")
    public ResponseEntity<PaginationResponse<Workplace>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Workplace> paginationResponse = workplaceService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all workplaces",
            description = "Retrieves a list of all workplaces available in the system. "
                    + "This endpoint returns the complete workplace collection without pagination. "
                    + "Use it when you need to fetch all workplaces at once."
    )
    @PreAuthorize("hasAuthority('work-place-list')")
    public ResponseEntity<GlobalResponse> getAllWorkplaces()
    {
        List<Workplace> workplaces = workplaceService.getAllWorkplaces();

        GlobalResponse response = GlobalResponse.success(
                workplaces,
                "All workplaces fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new workplace",
            description = "Creates a new workplace in the system with the provided details. "
                    + "Required fields such as workplace name, address, and contact information must be included in the request body. "
                    + "Returns the created workplace information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('work-place-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkplaceRequest request)
    {
        Workplace workplace = workplaceService.store(request);

        GlobalResponse response = GlobalResponse.success(
                workplace,
                "Store successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get workplace by ID",
            description = "Retrieves the details of a specific workplace using the provided ID. "
                    + "If the workplace with the given ID does not exist, a 404 Not Found response will be returned."
    )
    @PreAuthorize("hasAuthority('work-place-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long workplaceId)
    {
        Workplace workplace = workplaceService.show(workplaceId);

        GlobalResponse response = GlobalResponse.success(
                workplace,
                "workplace fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update workplace by ID",
            description = "Updates the details of an existing workplace using the provided ID. "
                    + "This endpoint allows modifying workplace information such as name, location, or other attributes. "
                    + "If the workplace with the given ID does not exist, a 404 Not Found response will be returned."
    )
    @PreAuthorize("hasAuthority('work-place-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long workplaceId,
                                                 @Valid @RequestBody WorkplaceRequest request)
    {
        Workplace workplace = workplaceService.update(workplaceId, request);

        GlobalResponse response = GlobalResponse.success(
                workplace,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete workplace by ID",
            description = "Deletes a specific workplace from the system using the provided ID. "
                    + "If the workplace does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove workplace records."
    )
    @PreAuthorize("hasAuthority('work-place-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long workplaceId)
    {
        workplaceService.destroy(workplaceId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "workplace deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
