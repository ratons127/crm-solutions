package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.AttendanceDeviceCategoryRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceCategoryDTO;
import com.betopia.hrm.services.attendance.attendanceCategory.AttendanceDeviceCategoryService;
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
@RequestMapping("/v1/attendance-device-category")
@Tag(
        name = "Attendance -> Attendance device category",
        description = "APIs for managing Attendance device categories"
)
public class AttendanceDeviceCategoryController {

    private final AttendanceDeviceCategoryService attendanceDeviceCategoryService;

    public AttendanceDeviceCategoryController(AttendanceDeviceCategoryService attendanceDeviceCategoryService) {
        this.attendanceDeviceCategoryService = attendanceDeviceCategoryService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all attendance device categories with pagination",
            description = "Retrieve a list of all attendance device categories with pagination."
    )
    // @PreAuthorize("hasAuthority('attendance-device-category-list')")
    public ResponseEntity<PaginationResponse<AttendanceDeviceCategoryDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<AttendanceDeviceCategoryDTO> response = attendanceDeviceCategoryService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all attendance device categories without pagination",
            description = "Retrieve a list of all shift attendance device categories pagination"
    )
    // @PreAuthorize("hasAuthority('attendance-device-category-list')")
    public ResponseEntity<GlobalResponse> getAllAttendanceDeviceCategories()
    {
        List<AttendanceDeviceCategoryDTO> attendanceDeviceCategoryDTOS = attendanceDeviceCategoryService.getAllAttendanceDeviceCategories();

        GlobalResponse response = GlobalResponse.success(
                attendanceDeviceCategoryDTOS,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store attendance device categories",
            description = "Creating a new attendance device categories"
    )
    // @PreAuthorize("hasAuthority('attendance-device-category-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AttendanceDeviceCategoryRequest request)
    {
        var data = attendanceDeviceCategoryService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show attendance device categories by id",
            description = "Creating a new attendance device categories"
    )
    // @PreAuthorize("hasAuthority('attendance-device-category-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long attendanceDeviceCategoryId)
    {
        var data = attendanceDeviceCategoryService.show(attendanceDeviceCategoryId);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "3. Update attendance device categories",
            description = "Creating a new attendance device categories"
    )
    // @PreAuthorize("hasAuthority('attendance-device-category-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long attendanceDeviceCategoryId, @Valid @RequestBody AttendanceDeviceCategoryRequest request)
    {
        var data = attendanceDeviceCategoryService.update(attendanceDeviceCategoryId, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete attendance device categories",
            description = "Creating a new attendance device categories"
    )
    // @PreAuthorize("hasAuthority('attendance-device-category-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long attendanceDeviceCategoryId)
    {
        attendanceDeviceCategoryService.destroy(attendanceDeviceCategoryId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
