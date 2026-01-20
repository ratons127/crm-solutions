package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveGroupAssignDTO;
import com.betopia.hrm.domain.leave.request.LeaveGroupAssignRequest;
import com.betopia.hrm.services.leaves.leaveGroupAssign.LeaveGroupAssignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/leave-group-assigns")
@Tag(
        name = "Leave Management -> Leave Group Assign",
        description = "Operations related to leave group assign"
)
public class LeaveGroupAssignController {

    private final LeaveGroupAssignService leaveGroupAssignService;

    public LeaveGroupAssignController(LeaveGroupAssignService leaveGroupAssignService) {
        this.leaveGroupAssignService = leaveGroupAssignService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave group assign with pagination", description = "Retrieve all leave group assign with pagination")
    @PreAuthorize("hasAuthority('leave-group-assign-list')")
    public ResponseEntity<PaginationResponse<LeaveGroupAssignDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveGroupAssignDTO> paginationResponse = leaveGroupAssignService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave group assign", description = "Retrieve all leave group assign without pagination")
//    @PreAuthorize("hasAuthority('leave-group-assign-list')")
    public ResponseEntity<GlobalResponse> getAllLeaveGroupAssigns(
            @RequestParam(required = false) Long leaveTypeId
    ) {
        var leaveGroupAssigns  = leaveGroupAssignService.getAllLeaveGroupAssigns(leaveTypeId);

        GlobalResponse response = GlobalResponse.success(
                leaveGroupAssigns,
                "All leave group assigns fetched successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "4. Save/Store leave group assign", description = "Create/Save a new leave group assign")
//    @PreAuthorize("hasAuthority('leave-group-assign-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveGroupAssignRequest request) {
        var savedLeaveGroupAssign = leaveGroupAssignService.store(request);

        GlobalResponse response = GlobalResponse.success(
                savedLeaveGroupAssign,
                "Leave group assign created successfully",
                HttpStatus.CREATED.value()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave group assign by id", description = "Retrieve a single leave group assign by its ID")
    @PreAuthorize("hasAuthority('leave-group-assign-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        var leaveGroupAssign = leaveGroupAssignService.show(id);

        GlobalResponse response = GlobalResponse.success(
                leaveGroupAssign,
                "Leave group assign fetched successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update leave group assign", description = "Update an existing leave group assign")
    @PreAuthorize("hasAuthority('leave-group-assign-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveGroupAssignRequest request) {
        var updatedLeaveGroupAssign = leaveGroupAssignService.update(id, request);
        GlobalResponse response = GlobalResponse.success(
                updatedLeaveGroupAssign,
                "Leave group assign updated successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete leave group assign", description = "Remove a leave group assign from the system")
    @PreAuthorize("hasAuthority('leave-group-assign-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveGroupAssignService.destroy(id);
        GlobalResponse response = GlobalResponse.success(
                null,
                "Leave group assign deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
