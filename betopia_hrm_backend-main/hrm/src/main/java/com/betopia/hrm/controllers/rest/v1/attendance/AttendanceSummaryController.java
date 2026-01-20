package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.AttendanceSummaryRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceStatusDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceSummaryDTO;
import com.betopia.hrm.domain.dto.attendance.DeviceTypeDTO;
import com.betopia.hrm.services.attendance.attendancesummary.AttendanceSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/attendance-summary")
@Tag(
        name = "Attendance -> Attendance summary",
        description = "APIs for managing Attendance summary"
)
public class AttendanceSummaryController {

    private final AttendanceSummaryService attendanceSummaryService;

    @Autowired
    public AttendanceSummaryController(AttendanceSummaryService attendanceSummaryService) {
        this.attendanceSummaryService = attendanceSummaryService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all Attendance summary with pagination",
            description = "Retrieve a list of all Attendance summary with pagination."
    )
    // @PreAuthorize("hasAuthority('attendance-summary-list')")
    public ResponseEntity<PaginationResponse<AttendanceSummaryDTO>> index(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<AttendanceSummaryDTO> response = attendanceSummaryService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all Attendance summary without pagination",
            description = "Retrieve a list of all Attendance summary without pagination"
    )
    // @PreAuthorize("hasAuthority('attendance-summary-list')")
    public ResponseEntity<GlobalResponse> getAllAttendanceSummaries()
    {
        List<AttendanceSummaryDTO> data = attendanceSummaryService.getAllAttendanceSummaries();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Store Attendance Summary",
            description = "Creating a new Attendance Summary"
    )
    // @PreAuthorize("hasAuthority('attendance-summary-create')
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AttendanceSummaryRequest request)
    {
        var data = attendanceSummaryService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show Attendance Summary by id",
            description = "Creating a new Attendance Summary"
    )
    // @PreAuthorize("hasAuthority('attendance-summary-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long attendanceSummaryId)
    {
        var data = attendanceSummaryService.show(attendanceSummaryId);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "3. Update Attendance Summary",
            description = "Creating a new Attendance Summary"
    )
    // @PreAuthorize("hasAuthority('attendance-summary-edit')
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long attendanceSummaryId, @Valid @RequestBody AttendanceSummaryRequest request)
    {
        var data = attendanceSummaryService.update(attendanceSummaryId, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete Attendance Summary",
            description = "Creating a new Attendance Summary"
    )
    // @PreAuthorize("hasAuthority('attendance-summary-delete')
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long attendanceSummaryId)
    {
        attendanceSummaryService.destroy(attendanceSummaryId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @GetMapping("/show/{employeeId}")
    @Operation(
            summary = "3. Get attendance summary by employee id",
            description = "Getting an individual Attendance Summary by employee id with limit"
    )
    public List<DeviceTypeDTO> getAttendanceSummary(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer limit  // optional param
    ) {
        return attendanceSummaryService.getRecentAttendance(employeeId, limit);
    }

    @GetMapping("/attendanceStatus")
    @Operation(
            summary = "3. Get attendance summary by Login employee id",
            description = "Getting an individual employee entire Attendance Summary by employee id with limit"
    )
    public ResponseEntity<GlobalResponse> getAttendanceStatus() {

        List<AttendanceStatusDTO> attendanceStatus = attendanceSummaryService.getAttendanceStatus();

        return ResponseEntity.ok(GlobalResponse.success(attendanceStatus,"Successfully retrieve attendance summary",HttpStatus.OK.value()));
    }
}
