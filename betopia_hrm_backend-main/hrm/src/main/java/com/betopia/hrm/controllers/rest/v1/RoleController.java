package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.domain.users.request.RoleRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/roles")
@Tag(name = "User Management -> Role", description = "Operations related to managing role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @Operation(summary = "1. Get all roles with pagination", description = "Retrieve a list of all role with pagination.")
    @PreAuthorize("hasAuthority('role-list')")
    public ResponseEntity<PaginationResponse<Role>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Role> paginationResponse = roleService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all roles without pagination", description = "Retrieve a list of all roles without pagination")
    @PreAuthorize("hasAuthority('role-list')")
    public ResponseEntity<GlobalResponse> getAllRoles()
    {
        List<Role> roles = roleService.getAllRoles();

        GlobalResponse response = GlobalResponse.success(
                roles,
                "All roles fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store Role", description = "Creating a new role")
    @PreAuthorize("hasAuthority('role-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody RoleRequest request)
    {
        Role role = roleService.store(request);

        GlobalResponse response = GlobalResponse.success(
                role,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Role get by id", description = "Return a single role by id")
    @PreAuthorize("hasAuthority('role-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long roleId)
    {
        Map<String, Object> role = roleService.show(roleId);

        GlobalResponse response = GlobalResponse.success(
                role,
                "Role fetch get by roleId",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update role", description = "Update a role by id")
    @PreAuthorize("hasAuthority('role-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long roleId, @Valid @RequestBody RoleRequest request)
    {
        Role role = roleService.update(roleId, request);

        GlobalResponse response = GlobalResponse.success(
                role,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete role", description = "delete role by id")
    @PreAuthorize("hasAuthority('role-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long roleId)
    {
        roleService.destroy(roleId);

        GlobalResponse response = GlobalResponse.success(
                "",
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }
}
