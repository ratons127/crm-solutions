package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.ShiftRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.services.attendance.shift.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/shift")
@Tag(
        name = "Attendance -> Shift",
        description = "APIs for managing shift"
)
public class ShiftController {

    private final ShiftService shiftService;

    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all shift with pagination",
            description = "Retrieve a list of all shift with pagination."
    )
    // @PreAuthorize("hasAuthority('shift-list')")
    public ResponseEntity<PaginationResponse<ShiftDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ShiftDTO> response = shiftService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all shift without pagination",
            description = "Retrieve a list of all shift without pagination"
    )
    // @PreAuthorize("hasAuthority('shift-list')")
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ShiftDTO> data = shiftService.getAllShift();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store Shift",
            description = "Creating a new shift"
    )
    // @PreAuthorize("hasAuthority('shift-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ShiftRequest request)
    {
        var data = shiftService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show Shift by id",
            description = "Creating a new shift"
    )
    // @PreAuthorize("hasAuthority('shift-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long shiftId)
    {
        var data = shiftService.show(shiftId);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "3. Update Shift",
            description = "Creating a new shift"
    )
    // @PreAuthorize("hasAuthority('shift-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long shiftId, @Valid @RequestBody ShiftRequest request)
    {
        var data = shiftService.update(shiftId, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete Shift",
            description = "Creating a new shift"
    )
    // @PreAuthorize("hasAuthority('shift-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        shiftService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
