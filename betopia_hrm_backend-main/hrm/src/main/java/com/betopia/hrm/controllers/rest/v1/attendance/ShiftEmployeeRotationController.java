package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.ShiftEmployeeRotationRequest;
import com.betopia.hrm.domain.attendance.request.ShiftRotationPatternsRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftEmployeeRotationDTO;
import com.betopia.hrm.domain.dto.attendance.ShiftRotationPatternDTO;
import com.betopia.hrm.services.attendance.shiftEmployeeRotation.ShiftEmployeeRotationService;
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
@RequestMapping("/v1/shift-employee-rotation")
@Tag(
        name = "Attendance -> Shift Employee Rotation ",
        description = "APIs for managing shift Employee Rotation "
)
public class ShiftEmployeeRotationController {

    private final ShiftEmployeeRotationService shiftEmployeeRotationService;

    public ShiftEmployeeRotationController(ShiftEmployeeRotationService shiftEmployeeRotationService) {
        this.shiftEmployeeRotationService = shiftEmployeeRotationService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all shift Employee Rotation Pattern with pagination",
            description = "Retrieve a list of all shift Employee Rotation Pattern with pagination."
    )
    // @PreAuthorize("hasAuthority('shift-employee-rotation-list')")
    public ResponseEntity<PaginationResponse<ShiftEmployeeRotationDTO>> index(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ShiftEmployeeRotationDTO> response = shiftEmployeeRotationService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all shift Employee Rotation without pagination",
            description = "Retrieve a list of all shift Employee Rotation without pagination"
    )
    // @PreAuthorize("hasAuthority('shift-employee-rotation-list')")
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ShiftEmployeeRotationDTO> data = shiftEmployeeRotationService.getAllShiftEmployeeRotation();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store Shift Employee Rotation Pattern",
            description = "Creating a new shift Employee Rotation Pattern"
    )
    // @PreAuthorize("hasAuthority('shift-employee-rotation-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ShiftEmployeeRotationRequest request)
    {
        var data = shiftEmployeeRotationService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show Shift employee Rotation Pattern by id",
            description = "Creating a new shift employee Rotation Pattern"
    )
    // @PreAuthorize("hasAuthority('shift-employee-rotation-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long shiftEmployeeRotationId)
    {
        var data = shiftEmployeeRotationService.show(shiftEmployeeRotationId);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "3. Update Shift Employee Rotation",
            description = "Creating a new shift Employee Rotation"
    )
    // @PreAuthorize("hasAuthority('shift-employee-rotation-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long shiftEmployeeRotationId, @Valid @RequestBody ShiftEmployeeRotationRequest request)
    {
        var data = shiftEmployeeRotationService.update(shiftEmployeeRotationId, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete Shift Employee Rotation",
            description = "Creating a new shift Employee Rotation"
    )
    // @PreAuthorize("hasAuthority('shift-employee-rotation-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftEmployeeRotationId)
    {
        shiftEmployeeRotationService.destroy(shiftEmployeeRotationId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
