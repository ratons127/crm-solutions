package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.entity.ShiftCategory;
import com.betopia.hrm.domain.attendance.request.ShiftCategoryRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.services.attendance.shiftCategory.ShiftCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/shift-categories")
@Tag(
        name = "Attendance -> Shift Category",
        description = "APIs for managing shift categories"
)
public class ShiftCategoryController {

    private final ShiftCategoryService shiftCategoryService;

    public ShiftCategoryController(ShiftCategoryService shiftCategoryService) {
        this.shiftCategoryService = shiftCategoryService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all shift categories with pagination",
            description = "Retrieve a list of all shift categories with pagination."
    )
    // @PreAuthorize("hasAuthority('shift-category-list')")
    public ResponseEntity<PaginationResponse<ShiftCategory>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<ShiftCategory> paginationResponse = shiftCategoryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all shift categories without pagination",
            description = "Retrieve a list of all shift categories without pagination"
    )
    // @PreAuthorize("hasAuthority('shift-category-list')")
    public ResponseEntity<GlobalResponse> getAllShiftCategories(){

        List<ShiftCategory> shiftCategories = shiftCategoryService.getAll();

        GlobalResponse response = GlobalResponse.success(
                shiftCategories,
                "All shift categories fetch successful",
                200
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store Shift Category",
            description = "Creating a new shift category"
    )
    // @PreAuthorize("hasAuthority('shift-category-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ShiftCategoryRequest request){
        ShiftCategory shiftCategory = shiftCategoryService.store(request);

        GlobalResponse response = GlobalResponse.success(
                shiftCategory,
                "Store successful",
                201
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Shift Category get by id",
            description = "Return a single shift category by id"
    )
    // @PreAuthorize("hasAuthority('shift-category-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long shiftCategoryId) {
        ShiftCategory shiftCategory = shiftCategoryService.show(shiftCategoryId);
        GlobalResponse response = GlobalResponse.success(
                shiftCategory,
                "Shift category fetch successful",
                200
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update shift category",
            description = "Update a shift category by id"
    )
    // @PreAuthorize("hasAuthority('shift-category-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long shiftCategoryId, @Valid @RequestBody ShiftCategoryRequest request) {
        ShiftCategory shiftCategory = shiftCategoryService.update(shiftCategoryId, request);
        GlobalResponse response = GlobalResponse.success(
                shiftCategory,
                "Update successful",
                200
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete shift category",
            description = "Delete shift category by id"
    )
    // @PreAuthorize("hasAuthority('shift-category-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long shiftCategoryId) {
        shiftCategoryService.destroy(shiftCategoryId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                200
        );
        return ResponseEntity.ok(response);
    }
}
