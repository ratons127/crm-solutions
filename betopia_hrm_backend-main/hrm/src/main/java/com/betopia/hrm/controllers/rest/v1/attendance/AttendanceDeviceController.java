package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.AttendanceDeviceCategoryRequest;
import com.betopia.hrm.domain.attendance.request.AttendanceDeviceRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceCategoryDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceDTO;
import com.betopia.hrm.services.attendance.attendanceDevice.AttendanceDeviceService;
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
@RequestMapping("/v1/attendance-device")
@Tag(
        name = "Attendance -> Attendance device",
        description = "APIs for managing Attendance device"
)
public class AttendanceDeviceController {

    private final AttendanceDeviceService attendanceDeviceService;

    public AttendanceDeviceController(AttendanceDeviceService attendanceDeviceService) {
        this.attendanceDeviceService = attendanceDeviceService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all attendance device with pagination",
            description = "Retrieve a list of all attendance device with pagination."
    )
    // @PreAuthorize("hasAuthority('attendance-device-list')")
    public ResponseEntity<PaginationResponse<AttendanceDeviceDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<AttendanceDeviceDTO> response = attendanceDeviceService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all attendance device without pagination",
            description = "Retrieve a list of all shift attendance device pagination"
    )
    // @PreAuthorize("hasAuthority('attendance-device-list')")
    public ResponseEntity<GlobalResponse> getAllAttendanceDevices()
    {
        List<AttendanceDeviceDTO> attendanceDeviceDTOS = attendanceDeviceService.getAllAttendanceDevices();

        GlobalResponse response = GlobalResponse.success(
                attendanceDeviceDTOS,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store attendance device",
            description = "Creating a new attendance device"
    )
    // @PreAuthorize("hasAuthority('attendance-device-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AttendanceDeviceRequest request)
    {
        var data = attendanceDeviceService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show attendance device by id",
            description = "Creating a new attendance device"
    )
    // @PreAuthorize("hasAuthority('attendance-device-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long attendanceDeviceId)
    {
        var data = attendanceDeviceService.show(attendanceDeviceId);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "3. Update attendance device",
            description = "Creating a new attendance device"
    )
    // @PreAuthorize("hasAuthority('attendance-device-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long attendanceDeviceId, @Valid @RequestBody AttendanceDeviceRequest request)
    {
        var data = attendanceDeviceService.update(attendanceDeviceId, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete attendance device",
            description = "Creating a new attendance device"
    )
    // @PreAuthorize("hasAuthority('attendance-device-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long attendanceDeviceId)
    {
        attendanceDeviceService.destroy(attendanceDeviceId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
