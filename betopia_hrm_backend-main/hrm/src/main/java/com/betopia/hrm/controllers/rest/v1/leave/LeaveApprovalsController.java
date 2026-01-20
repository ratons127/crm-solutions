package com.betopia.hrm.controllers.rest.v1.leave;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveApprovalsDTO;
import com.betopia.hrm.domain.leave.request.LeaveApprovalsRequest;
import com.betopia.hrm.domain.leave.request.StatusApproveRequest;
import com.betopia.hrm.services.leaves.leaveapproval.LeaveApprovalsService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/v1/leave-approvals")
@Tag(
        name = "Leave Management -> Leave Approval",
        description = "Operations related to leave approval"
)
public class LeaveApprovalsController {

    private final LeaveApprovalsService leaveApprovalsService;

    public LeaveApprovalsController(LeaveApprovalsService leaveApprovalsService) {
        this.leaveApprovalsService = leaveApprovalsService;
    }


    @GetMapping
    @Operation(summary = "1. Get all approval with pagination", description = "Retrieve all approvalg with pagination")
    public ResponseEntity<PaginationResponse<LeaveApprovalsDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String keyword
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveApprovalsDTO> paginationResponse = leaveApprovalsService.index(direction, page, perPage,keyword);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all approval", description = "Retrieve all approval without pagination")
    public ResponseEntity<GlobalResponse> getAll()
    {
        List<LeaveApprovalsDTO> data = leaveApprovalsService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get approval log by id", description = "Retrieve a approval log by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = leaveApprovalsService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store approval", description = "Create/Save a new approval")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveApprovalsRequest request)
    {
        var data = leaveApprovalsService.store(request);
        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update approval", description = "Update an existing approval")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveApprovalsRequest request)
    {
        var data = leaveApprovalsService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    /** Leave request approval api **/
    @PatchMapping("/status")
    @Operation(summary = "6. Update leave status", description = "Update leave status")
    public ResponseEntity<GlobalResponse> updateLeaveStatus(@Valid @RequestBody List<StatusApproveRequest> requests) {
        List<LeaveApprovalsDTO> updatedLeaveType = leaveApprovalsService.updateLeaveStatus(requests);
        return ResponseBuilder.ok(updatedLeaveType, "Leave status successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "7. Delete approval", description = "Remove approval from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        leaveApprovalsService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @GetMapping("/for-approvers")
    @Operation(summary = "8. Get leave approvals", description = "Get all leave approval list for the approver")
    public ResponseEntity<List<LeaveApprovalsDTO>> getMyApprovals() {
        List<LeaveApprovalsDTO> approvals = leaveApprovalsService.getApprovalsForSupervisor();
        return ResponseEntity.ok(approvals);
    }
}
