package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeavePolicyDTO;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.request.LeavePolicyRequest;
import com.betopia.hrm.services.leaves.leavepolicy.LeavePolicyService;
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
@RequestMapping("/v1/leave-policies")
@Tag(
        name = "Leave Management -> LeavePolicy",
        description = "Operations related to leave polices"
)
public class LeavePolicyController {

    private final LeavePolicyService leavePolicyService;

    public LeavePolicyController(LeavePolicyService leavePolicyService) {
        this.leavePolicyService = leavePolicyService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave polices with pagination", description = "Retrieve all leave polices with pagination")
    public ResponseEntity<PaginationResponse<LeavePolicyDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeavePolicyDTO> paginationResponse = leavePolicyService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave polices", description = "Retrieve all leave polices without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypes() {
        List<LeavePolicyDTO> leavePolicies = leavePolicyService.getAllLeavePolicies();

        return ResponseBuilder.ok(leavePolicies, "All leave policies fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave policy by id", description = "Retrieve a single leave policy by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LeavePolicyDTO getId = leavePolicyService.getLeavePolicyById(id);
        return ResponseBuilder.ok(getId, "Leave policy fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store leave policy", description = "Create/Save a new leave policy")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeavePolicyRequest request) {
        LeavePolicyDTO created = leavePolicyService.store(request);
        return ResponseBuilder.created(created, "Leave policy created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave policy", description = "Update an existing leave policy")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeavePolicyRequest request) {
        LeavePolicyDTO updated = leavePolicyService.updateLeavePolicy(id, request);
        return ResponseBuilder.ok(updated, "Leave policy updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave policy", description = "Remove a leave policy from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leavePolicyService.deleteLeavePolicy(id);
        return ResponseBuilder.noContent("Leave policy deleted successfully");
    }
}
