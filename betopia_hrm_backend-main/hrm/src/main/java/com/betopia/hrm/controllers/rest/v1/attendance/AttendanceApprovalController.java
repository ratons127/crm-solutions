package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.attendance.request.ApproveAttendanceStatusRequest;
import com.betopia.hrm.domain.attendance.request.AttendanceApprovalRequest;
import com.betopia.hrm.domain.attendance.request.AttendanceSummaryRequest;
import com.betopia.hrm.domain.attendance.request.ManualAttendanceRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceApprovalDTO;
import com.betopia.hrm.domain.dto.attendance.AttendancePolicyDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceSummaryDTO;
import com.betopia.hrm.domain.dto.attendance.ManualAttendanceDTO;
import com.betopia.hrm.domain.dto.leave.LeaveApprovalsDTO;
import com.betopia.hrm.domain.leave.request.StatusApproveRequest;
import com.betopia.hrm.services.attendance.attendanceapproval.AttendanceApprovalService;
import com.betopia.hrm.services.attendance.attendancesummary.AttendanceSummaryService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
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
@RequestMapping("/v1/attendance-approval")
@Tag(
        name = "Attendance -> Attendance approval",
        description = "APIs for managing Attendance approval"
)
public class AttendanceApprovalController {

    private AttendanceApprovalService attendanceApprovalService;

    public AttendanceApprovalController(AttendanceApprovalService attendanceApprovalService) {
        this.attendanceApprovalService = attendanceApprovalService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all attendance approval with pagination",
            description = "Retrieve a list of all  attendance approval with pagination."
    )
    // @PreAuthorize("hasAuthority('attendance-approval-list')")
    public ResponseEntity<PaginationResponse<AttendanceApprovalDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String keyword
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<AttendanceApprovalDTO> response = attendanceApprovalService.index(direction, page, perPage,keyword);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all manual attendance approval without pagination",
            description = "Retrieve a list of all manual attendance approval pagination"
    )
    // @PreAuthorize("hasAuthority('attendance-approval-list')")
    public ResponseEntity<GlobalResponse> getAll()
    {
        List<AttendanceApprovalDTO> approvals = attendanceApprovalService.getAllAttendanceApprovals();

        GlobalResponse response = GlobalResponse.success(
                approvals,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store manual attendance approval",
            description = "Creating a new manual attendance approval"
    )
    // @PreAuthorize("hasAuthority('attendance-approval-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AttendanceApprovalRequest request)
    {
        var data = attendanceApprovalService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show manual attendance approval by id",
            description = "Show a new manual attendance approval"
    )
    // @PreAuthorize("hasAuthority('attendance-approval-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = attendanceApprovalService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "3. Update manual attendance approval",
            description = "Creating a new manual attendance approval"
    )
    // @PreAuthorize("hasAuthority('attendance-approval-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AttendanceApprovalRequest request)
    {
        var data = attendanceApprovalService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete manual attendance approval",
            description = "Deleting a manual attendance approval"
    )
    // @PreAuthorize("hasAuthority('attendance-approval-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id)
    {
        attendanceApprovalService.destroy(id);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }


    /** Leave request approval api **/
    @PatchMapping("/status")
    @Operation(summary = "5. Update manual attendance status", description = "Update a manual attendance status")
    public ResponseEntity<GlobalResponse> updateAttendanceStatus(@Valid @RequestBody List<ApproveAttendanceStatusRequest> requests) {
        List<AttendanceApprovalDTO> updateAttendanceStatus = attendanceApprovalService.updateAttendanceStatus(requests);
        return ResponseBuilder.ok(updateAttendanceStatus, "Manual attendance status successfully");
    }

    @GetMapping("/for-approvers")
    @Operation(summary = "8. Get attendance approvals", description = "Get all attendance approval list for the approver")
    public ResponseEntity<List<AttendanceApprovalDTO>> getMyApprovals() {
        List<AttendanceApprovalDTO> approvals = attendanceApprovalService.getApprovalsForSupervisor();
        return ResponseEntity.ok(approvals);
    }
}
