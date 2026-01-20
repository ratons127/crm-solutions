package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.AttendanceDeviceAssignRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceAssignDTO;
import com.betopia.hrm.services.attendance.attendanceDeviceAssign.AttendanceDeviceAssignService;
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
@RequestMapping("/v1/attendance-device-assign")
@Tag(
        name = "Attendance -> Attendance device assign",
        description = "APIs for managing Attendance device assign"
)
public class AttendanceDeviceAssignController {

    private final AttendanceDeviceAssignService attendanceDeviceAssignService;

    public AttendanceDeviceAssignController(AttendanceDeviceAssignService attendanceDeviceAssignService) {
        this.attendanceDeviceAssignService = attendanceDeviceAssignService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all attendance device assign with pagination",
            description = "Retrieve a list of all attendance device assign with pagination."
    )
    // @PreAuthorize("hasAuthority('attendance-device-assign-list')")
    public ResponseEntity<PaginationResponse<AttendanceDeviceAssignDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<AttendanceDeviceAssignDTO> response = attendanceDeviceAssignService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all attendance device assign without pagination",
            description = "Retrieve a list of all shift attendance device assign pagination"
    )
    // @PreAuthorize("hasAuthority('attendance-device-assign-list')")
    public ResponseEntity<GlobalResponse> getAllAttendanceDeviceAssigns()
    {
        List<AttendanceDeviceAssignDTO> attendanceDeviceAssignDTOS = attendanceDeviceAssignService.getAllAttendanceDeviceAssigns();

        GlobalResponse response = GlobalResponse.success(
                attendanceDeviceAssignDTOS,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store attendance device assign",
            description = "Creating a new attendance device assign"
    )
    // @PreAuthorize("hasAuthority('attendance-device-assign-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AttendanceDeviceAssignRequest request)
    {
        var data = attendanceDeviceAssignService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show attendance device assign by id",
            description = "Creating a new attendance device assign"
    )
    // @PreAuthorize("hasAuthority('attendance-device-assign-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long attendanceDeviceAssignId)
    {
        var data = attendanceDeviceAssignService.show(attendanceDeviceAssignId);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "3. Update attendance device assign",
            description = "Creating a new attendance device assign"
    )
    // @PreAuthorize("hasAuthority('attendance-device-assign-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long attendanceDeviceAssignId, @Valid @RequestBody AttendanceDeviceAssignRequest request)
    {
        var data = attendanceDeviceAssignService.update(attendanceDeviceAssignId, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete attendance device assign",
            description = "Creating a new attendance device assign"
    )
    // @PreAuthorize("hasAuthority('attendance-device-assign-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long attendanceDeviceAssignId)
    {
        attendanceDeviceAssignService.destroy(attendanceDeviceAssignId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
