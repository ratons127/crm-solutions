package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Menu;
import com.betopia.hrm.domain.users.request.MenuRequest;
import com.betopia.hrm.services.users.menu.MenuService;
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
@RequestMapping("/v1/menus")
@Tag(name = "User Management -> Menu", description = "Operations related to managing menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @Operation(summary = "1. Get all menu with pagination", description = "Retrieve a list of all menu with pagination.")
//    @PreAuthorize("hasAuthority('menu-list')")
    public ResponseEntity<PaginationResponse<Menu>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Menu> paginationResponse = menuService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all menus without pagination", description = "Retrieve a list of all menus without pagination")
//    @PreAuthorize("hasAuthority('menu-list')")
    public ResponseEntity<GlobalResponse> getAllMenus()
    {
        List<Menu> menus = menuService.getAllMenus();

        GlobalResponse response = GlobalResponse.success(
                menus,
                "All menus fetch successful",
                200
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store menu", description = "Creating a new menu")
//    @PreAuthorize("hasAuthority('menu-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody MenuRequest request)
    {
        Menu menu = menuService.store(request);

        GlobalResponse response = GlobalResponse.success(
                menu,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Menu get by id", description = "Return a single menu by id")
//    @PreAuthorize("hasAuthority('menu-edit')")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Long menuId)
    {
        Menu menu = menuService.edit(menuId);

        GlobalResponse response = GlobalResponse.success(
                menu,
                "Menu fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update menu", description = "Update a menu by id")
//    @PreAuthorize("hasAuthority('menu-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long menuId, @Valid @RequestBody MenuRequest request)
    {
        Menu menu = menuService.update(menuId, request);

        GlobalResponse response = GlobalResponse.success(
                menu,
                "Menu update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete menu", description = "Delete a menu by id")
//    @PreAuthorize("hasAuthority('menu-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long menuId)
    {
        menuService.destroy(menuId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Menu delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

}
