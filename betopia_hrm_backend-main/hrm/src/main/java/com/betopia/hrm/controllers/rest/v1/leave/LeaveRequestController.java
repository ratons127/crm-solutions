package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveRequestDTO;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.request.LeaveRequestCreateRequest;
import com.betopia.hrm.services.leaves.leaverequest.LeaveRequestService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import com.betopia.hrm.webapp.util.SmsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/leave-requests")
@Tag(
        name = "Leave Management -> Leave Request",
        description = "Operations related to leave requests"
)
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave requests with pagination", description = "Retrieve all leave requests with pagination")
    public ResponseEntity<PaginationResponse<LeaveRequestDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String keyword
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveRequestDTO> paginationResponse = leaveRequestService.index(direction, page, perPage,userId,keyword);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave requests", description = "Retrieve all leave requests without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveRequest(
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.getAllLeaveRequests(userId);
        return ResponseBuilder.ok(leaveRequests, "All leave requests fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave request by id", description = "Retrieve a single leave request by its ID")
    public ResponseEntity<GlobalResponse> show(
            @PathVariable("id") Long id
    ) {
        LeaveRequest getId = leaveRequestService.getLeaveRequestById(id);
        return ResponseBuilder.ok(getId, "Leave request fetched successfully");
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "4. Save/Store leave request", description = "Create/Save a new leave request")
    public ResponseEntity<GlobalResponse> store(
            @RequestPart("data") @Valid LeaveRequestCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        LeaveRequestDTO created = leaveRequestService.store(request, files);
        return ResponseBuilder.created(created, "Leave request created successfully");
    }

    /**
     * @PutMapping("/update/{id}")
     * @Operation(summary = "5. Update leave request", description = "Update an existing leave request")
     * public ResponseEntity<GlobalResponse> update(
     * @PathVariable("id") Long id,
     * @Valid @RequestBody LeaveRequestCreateRequest request
     * ) {
     * LeaveRequest updated = leaveRequestService.updateLeaveRequest(id, request);
     * return ResponseBuilder.ok(updated, "Leave request updated successfully");
     * }
     **/

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave request", description = "Remove a leave request from the system")
    public ResponseEntity<GlobalResponse> delete(
            @PathVariable("id") Long id
    ) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseBuilder.noContent("Leave request deleted successfully");
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "7. Get leave request by employee id", description = "Retrieve leave request by employee id")
    public ResponseEntity<GlobalResponse> getLeaveRequestByEmployeeId(
            @PathVariable("employeeId") Long employeeId
    ) {
        List<LeaveRequest> leaveRequests = leaveRequestService.findByEmployeeId(employeeId);
        return ResponseBuilder.ok(leaveRequests, "Leave request fetched successfully");
    }

    @GetMapping("/send-sms")
    public SmsResponse sendLeaveSms(Long leaveRequestId) {
        return leaveRequestService.sendLeaveApprovalNotification(leaveRequestId);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave request",
            description = "Update leave request with optional files")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @RequestPart("data") @Valid LeaveRequestCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        LeaveRequestDTO separation = leaveRequestService.update(id, request, files);
        return ResponseBuilder.ok(separation, "leave request updated successfully");
    }
}
