package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitClearanceItemDTO;
import com.betopia.hrm.domain.employee.request.ExitClearanceItemRequest;
import com.betopia.hrm.services.employee.exitclearanceitem.ExitClearanceItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/exit-item")
@Tag(
        name = "Employee Management -> Exit Clearance Item",
        description = "APIs for configurable Exit Clearance item. Includes operations to create, read, update, and delete exit clearance item."
)
public class ExitClearanceItemController {

    private final ExitClearanceItemService exitClearanceItemService;
    public ExitClearanceItemController(ExitClearanceItemService exitClearanceItemService) {
        this.exitClearanceItemService = exitClearanceItemService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all  exit clearance item with pagination",
            description = "Retrieve a list of all  exit clearance item with pagination."
    )
    public ResponseEntity<PaginationResponse<ExitClearanceItemDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ExitClearanceItemDTO> response = exitClearanceItemService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all  exit clearance item without pagination",
            description = "Retrieve a list of all  exit clearance item without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ExitClearanceItemDTO> data = exitClearanceItemService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store  exit clearance item",
            description = "Creating a new  exit clearance item"
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ExitClearanceItemRequest request)
    {
        var data = exitClearanceItemService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show  exit clearance item by id",
            description = "Show a new  exit clearance item"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = exitClearanceItemService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "4. Update  exit clearance item",
            description = "Updating a new  exit clearance item"
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ExitClearanceItemRequest request)
    {
        var data = exitClearanceItemService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "5. Delete exit clearance item",
            description = "Deleting a new exit clearance item"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        exitClearanceItemService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

}
