package com.betopia.hrm.controllers.rest.v1.leave;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveTypeRules;
import com.betopia.hrm.domain.leave.request.LeaveTypeRulesRequest;
import com.betopia.hrm.services.leaves.leavetyperules.LeaveTypeRulesService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/v1/leave-type-rules")
@Tag(
        name = "Leave Management -> LeaveTypeRules",
        description = "Operations related to leave type rules"
)
public class LeaveTypeRulesController {

    private final LeaveTypeRulesService leaveTypeRulesService ;

    public LeaveTypeRulesController(LeaveTypeRulesService leaveTypeRulesService) {
        this.leaveTypeRulesService = leaveTypeRulesService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave type rules with pagination", description = "Retrieve all leave type rules with pagination")
    public ResponseEntity<PaginationResponse<LeaveTypeRules>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveTypeRules> paginationResponse = leaveTypeRulesService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave type rules", description = "Retrieve all leave type rules without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<LeaveTypeRules> leaveTypeRules = leaveTypeRulesService.getAllLeaveTypeRules();
        return ResponseBuilder.ok(leaveTypeRules, "All leave type rules fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave type rules by id", description = "Retrieve a single leave type rule by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LeaveTypeRules leaveTypeRules = leaveTypeRulesService.getLeaveTypeRulesById(id);
        return ResponseBuilder.ok(leaveTypeRules, "Leave type rules fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store leave type rules", description = "Create/Save a new leave type rules")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveTypeRulesRequest request) {
        LeaveTypeRules createdLeaveTypeRules = leaveTypeRulesService.store(request);
        return ResponseBuilder.created(createdLeaveTypeRules, "Leave type rules created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave type rules", description = "Update an existing leave type rule")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveTypeRulesRequest request) {
        LeaveTypeRules updatedLeaveTypeRules = leaveTypeRulesService.updateLeaveTypeRules(id, request);
        return ResponseBuilder.ok(updatedLeaveTypeRules, "Leave type rules updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave type rule", description = "Remove a leave type rule from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveTypeRulesService.deleteLeaveTypeRules(id);
        return ResponseBuilder.noContent("Leave type rules deleted successfully");
    }
}
