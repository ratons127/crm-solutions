package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.request.WorkplaceGroupRequest;
import com.betopia.hrm.services.company.workplaceGroup.WorkplaceGroupService;
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
@RequestMapping("/v1/workplace-group")
@Tag(
        name = "Company -> Workplace-group",
        description = "APIs for managing workplace-group. Includes operations to create, read, update, and delete workplace-group information."
)
public class WorkplaceGroupController {

    private final WorkplaceGroupService workplaceGroupService;
    public WorkplaceGroupController(WorkplaceGroupService workplaceGroupService) {
        this.workplaceGroupService = workplaceGroupService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of workplace groups",
            description = "Retrieves a paginated list of workplace groups from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch work[lace group records in a paginated format instead of retrieving all at once."
    )
    @PreAuthorize("hasAuthority('work-place-group-list')")
    public ResponseEntity<PaginationResponse<WorkplaceGroup>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<WorkplaceGroup> paginationResponse = workplaceGroupService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all workplace-group",
            description = "Retrieves a list of all workplace group available in the system. "
                    + "This endpoint returns the complete workplace group collection without pagination. "
                    + "Use it when you need to fetch all workplace at once."
    )
    @PreAuthorize("hasAuthority('work-place-group-list')")
    public ResponseEntity<GlobalResponse> getAllWorkplaceGroups()
    {
        List<WorkplaceGroup> workplaceGroups = workplaceGroupService.getAllWorkplaceGroups();
        GlobalResponse response = GlobalResponse.success(
                workplaceGroups,
                "All workplace groups fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new workplace group",
            description = "Creates a new workplace group in the system with the provided details. "
                    + "Required fields such as workplace group name, company, and business unit must be included in the request body. "
                    + "Returns the created workplace group information along with its unique ID."
    )
    @PreAuthorize("hasAuthority('work-place-group-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody WorkplaceGroupRequest request)
    {
        WorkplaceGroup workplaceGroup = workplaceGroupService.store(request);
        GlobalResponse response = GlobalResponse.success(
                workplaceGroup,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get workplace group by ID",
            description = "Retrieves the details of a specific workplace group using the provided ID. "
                    + "If the workplace group with the given ID does not exist, a 404 Not Found response will be returned."
    )
    @PreAuthorize("hasAuthority('work-place-group-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long workplaceGroupId)
    {
        WorkplaceGroup workplaceGroup = workplaceGroupService.show(workplaceGroupId);
        GlobalResponse response = GlobalResponse.success(
                workplaceGroup,
                "Workplace group fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update workplace group by ID",
            description = "Updates the details of an existing workplace group using the provided ID. "
                    + "This endpoint allows modifying workplace group information such as name, location, or other attributes. "
                    + "If the workplace group with the given ID does not exist, a 404 Not Found response will be returned."
    )
    @PreAuthorize("hasAuthority('work-place-group-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long workplaceGroupId,
                                                 @Valid @RequestBody WorkplaceGroupRequest request)
    {
        WorkplaceGroup workplaceGroup = workplaceGroupService.update(workplaceGroupId, request);
        GlobalResponse response = GlobalResponse.success(
                workplaceGroup,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete workplace group by ID",
            description = "Deletes a specific workplace group from the system using the provided ID. "
                    + "If the workplace group does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove workplace group records."
    )
    @PreAuthorize("hasAuthority('work-place-group-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long workplaceGroupId)
    {
        workplaceGroupService.destroy(workplaceGroupId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "workplace group deleted successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
