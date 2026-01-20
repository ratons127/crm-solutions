package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.request.PermissionRequest;
import com.betopia.hrm.services.users.permission.PermissionService;
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
@RequestMapping("/v1/permissions")
@Tag(
        name = "User Management -> Permission",
        description = "APIs for managing Permission. Includes operations to create, read, update, and delete Permission information."
)
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of permission",
            description = "Retrieves a paginated list of permission from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch permission records in a paginated format instead of retrieving all at once."
    )
//    @PreAuthorize("hasAuthority('permission-list')")
    public ResponseEntity<PaginationResponse<Permission>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Permission> paginationResponse = permissionService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all permission",
            description = "Retrieves a list of all permission available in the system. "
                    + "This endpoint returns the complete permission collection without pagination. "
                    + "Use it when you need to fetch all permission at once."
    )
//    @PreAuthorize("hasAuthority('permission-list')")
    public ResponseEntity<GlobalResponse> getAllPermissions()
    {
        List<Permission> permissions = permissionService.getAllPermissions();

        GlobalResponse response = GlobalResponse.success(
                permissions,
                "All permissions fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new permission",
            description = "Creates a new permission in the system with the provided details. "
                    + "Required fields such as permission name, address, and contact information must be included in the request body. "
                    + "Returns the created permission information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('permission-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody PermissionRequest request)
    {
        Permission permission = permissionService.store(request);

        GlobalResponse response = GlobalResponse.success(
                permission,
                "Permission created successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get permission by ID",
            description = "Retrieves the details of a specific permission using the provided ID. "
                    + "If the permission with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('permission-edit')")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Long permissionId)
    {
        Permission permission = permissionService.edit(permissionId);

        GlobalResponse response = GlobalResponse.success(
                permission,
                "Permission fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update permission by ID",
            description = "Updates the details of an existing permission identified by the provided ID. "
                    + "The request body should contain the fields to be updated. "
                    + "If the permission with the given ID does not exist, a 404 Not Found response will be returned. "
                    + "Returns the updated permission information."
    )
//    @PreAuthorize("hasAuthority('permission-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long permissionId,
                                                 @Valid @RequestBody PermissionRequest request)
    {
        Permission permission = permissionService.update(permissionId, request);

        GlobalResponse response = GlobalResponse.success(
                permission,
                "Permission updated successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete permission by ID",
            description = "Deletes the permission identified by the provided ID from the system. "
                    + "If the permission with the given ID does not exist, a 404 Not Found"
                    + " response will be returned. "
                    + "Returns a success message upon successful deletion."
    )
//    @PreAuthorize("hasAuthority('permission-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long permissionId)
    {
        permissionService.destroy(permissionId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Permission deleted successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }
}
