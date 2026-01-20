package com.betopia.hrm.controllers.rest.v1.attendance;


import com.betopia.hrm.domain.attendance.request.ManualAttendanceRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ManualAttendanceDTO;
import com.betopia.hrm.services.attendance.manualattendance.ManualAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/manual-attendance")
@Tag(
        name = "Attendance -> Manual Attendance",
        description = "APIs for managing manual attendance"
)
public class ManualAttendanceController {

    private final ManualAttendanceService manualAttendanceService;

    private ManualAttendanceController(ManualAttendanceService manualAttendanceService){
        this.manualAttendanceService = manualAttendanceService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all manual attendance with pagination",
            description = "Retrieve a list of all manual attendance with pagination."
    )
    // @PreAuthorize("hasAuthority('manual-attendance-list')")
    public ResponseEntity<PaginationResponse<ManualAttendanceDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long userId
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ManualAttendanceDTO> response =
                manualAttendanceService.index(direction, page, perPage,keyword,userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all manual attendance without pagination",
            description = "Retrieve a list of all manual attendance pagination"
    )
    // @PreAuthorize("hasAuthority('manual-attendance-list')")
    public ResponseEntity<GlobalResponse> getAllAttendanceDeviceAssigns(
            @RequestParam(value = "userId", required = false) Long userId
    )
    {
        List<ManualAttendanceDTO> attendanceDeviceAssignDTOS = manualAttendanceService.getAll(userId);

        GlobalResponse response = GlobalResponse.success(
                attendanceDeviceAssignDTOS,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store manual attendance",
            description = "Creating a new manual attendance"
    )
    // @PreAuthorize("hasAuthority('manual-attendance-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ManualAttendanceRequest request)
    {
        var data = manualAttendanceService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show manual attendance by id",
            description = "Showing a new manual attendance"
    )
    // @PreAuthorize("hasAuthority('manual-attendance-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = manualAttendanceService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "3. Update manual attendance",
            description = "Updating a new manual attendance"
    )
    // @PreAuthorize("hasAuthority('manual-attendance-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ManualAttendanceRequest request)
    {
        var data = manualAttendanceService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete manual attendance",
            description = "Deleting a manual attendance"
    )
    // @PreAuthorize("hasAuthority('manual-attendance-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id)
    {
        manualAttendanceService.destroy(id);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
