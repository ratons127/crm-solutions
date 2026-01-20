package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.ShiftRotationPatternsRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftRotationPatternDTO;
import com.betopia.hrm.services.attendance.shiftRotationPattern.ShiftRotationPatternService;
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
@RequestMapping("/v1/shift-rotation-pattern")
@Tag(
        name = "Attendance -> Shift Rotation Pattern",
        description = "APIs for managing shift Rotation Pattern"
)
public class ShiftRotationPatternController {

    private final ShiftRotationPatternService shiftRotationPatternService;

    public ShiftRotationPatternController(ShiftRotationPatternService shiftRotationPatternService) {
        this.shiftRotationPatternService = shiftRotationPatternService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all shift Rotation Pattern with pagination",
            description = "Retrieve a list of all shift Rotation Pattern with pagination."
    )
    // @PreAuthorize("hasAuthority('shift-rotation-pattern-list')")
    public ResponseEntity<PaginationResponse<ShiftRotationPatternDTO>> index(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ShiftRotationPatternDTO> response = shiftRotationPatternService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all shift Rotation Pattern without pagination",
            description = "Retrieve a list of all shift Rotation Pattern without pagination"
    )
    // @PreAuthorize("hasAuthority('shift-list')")
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ShiftRotationPatternDTO> data = shiftRotationPatternService.getAllShiftRotationPattern();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store Shift Rotation Pattern",
            description = "Creating a new shift Rotation Pattern"
    )
    // @PreAuthorize("hasAuthority('shift-rotation-pattern-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ShiftRotationPatternsRequest request)
    {
        var data = shiftRotationPatternService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show Shift Rotation Pattern by id",
            description = "Creating a new shift Rotation Pattern"
    )
    // @PreAuthorize("hasAuthority('shift-rotation-pattern-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long shiftRotationPatternId)
    {
        var data = shiftRotationPatternService.show(shiftRotationPatternId);

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
    // @PreAuthorize("hasAuthority('shift-rotation-pattern-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long shiftRotationPatternId, @Valid @RequestBody ShiftRotationPatternsRequest request)
    {
        var data = shiftRotationPatternService.update(shiftRotationPatternId, request);

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
    // @PreAuthorize("hasAuthority('shift-rotation-pattern-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        shiftRotationPatternService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
