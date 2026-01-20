package com.betopia.hrm.controllers.rest.v1.leave;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import com.betopia.hrm.domain.leave.request.LeaveGroupRequest;
import com.betopia.hrm.services.leaves.leavegroup.LeaveGroupService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/leave-groups")
@Tag(
        name = "Leave Management -> LeaveGroup",
        description = "Operations related to leave groups"
)
public class LeaveGroupControllrer {

    private final LeaveGroupService leaveGroupService;

    public LeaveGroupControllrer(LeaveGroupService leaveGroupService) {
        this.leaveGroupService = leaveGroupService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave groups with pagination", description = "Retrieve all leave groups with pagination")
    public ResponseEntity<PaginationResponse<LeaveGroup>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveGroup> paginationResponse = leaveGroupService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave groups", description = "Retrieve all leave groups without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveGroups() {
        List<LeaveGroup> leaveGroup = leaveGroupService.getAllLeaveGroups();
        return ResponseBuilder.ok(leaveGroup, "All leave groups fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave groups by id", description = "Retrieve a single leave group by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LeaveGroup leaveGroup = leaveGroupService.getLeaveGroupById(id);
        return ResponseBuilder.ok(leaveGroup, "Leave groups fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store leave groups", description = "Create/Save a new leave groups")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveGroupRequest request) {
        LeaveGroup createdLeaveGroup = leaveGroupService.store(request);
        return ResponseBuilder.created(createdLeaveGroup, "Leave groups created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave groups", description = "Update an existing leave groups")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveGroupRequest request) {
        LeaveGroup updatedLeaveGroup = leaveGroupService.updateLeaveGroup(id, request);
        return ResponseBuilder.ok(updatedLeaveGroup, "Leave groups updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave groups", description = "Remove a leave groups from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveGroupService.deleteLeaveGroup(id);
        return ResponseBuilder.noContent("Leave groups deleted successfully");
    }
}
