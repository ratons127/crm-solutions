package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveEligibilityRules;
import com.betopia.hrm.domain.leave.request.LeaveEligibilityRulesRequest;
import com.betopia.hrm.services.leaves.leaveeligibilityrules.LeaveEligibilityRulesService;
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
@RequestMapping("/v1/leave-eligibility")
@Tag(
        name = "Leave Management -> LeaveEligibilityRules",
        description = "Operations related to leave eligibility rules"
)
public class LeaveEligibilityRulesController {

    private final LeaveEligibilityRulesService leaveEligibilityRulesService ;

    public LeaveEligibilityRulesController(LeaveEligibilityRulesService leaveEligibilityRulesService) {
        this.leaveEligibilityRulesService = leaveEligibilityRulesService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave eligibility rules with pagination", description = "Retrieve all leave eligibility rules with pagination")
    public ResponseEntity<PaginationResponse<LeaveEligibilityRules>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveEligibilityRules> paginationResponse = leaveEligibilityRulesService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave eligibility rules", description = "Retrieve all leave eligibility rules without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveEligibilityRules() {
        List<LeaveEligibilityRules> leaveEligibilityRules = leaveEligibilityRulesService.getAllLeaveEligibilityRules();
        return ResponseBuilder.ok(leaveEligibilityRules, "All leave eligibility rules fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave type rules by id", description = "Retrieve a single leave type rule by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LeaveEligibilityRules leaveEligibilityRules = leaveEligibilityRulesService.getLeaveEligibilityRulesById(id);
        return ResponseBuilder.ok(leaveEligibilityRules, "Leave eligibility rules fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store leave eligibility rules", description = "Create/Save a new leave eligibility rules")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveEligibilityRulesRequest request) {
        LeaveEligibilityRules createdLeaveEligibilityRules = leaveEligibilityRulesService.store(request);
        return ResponseBuilder.created(createdLeaveEligibilityRules, "Leave type eligibility created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave eligibility rules", description = "Update an existing leave eligibility rule")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveEligibilityRulesRequest request) {
        LeaveEligibilityRules updatedLeaveEligibilityRules = leaveEligibilityRulesService.updateLeaveEligibilityRules(id, request);
        return ResponseBuilder.ok(updatedLeaveEligibilityRules, "Leave eligibility rule updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave eligibility rule", description = "Remove a leave eligibility rule from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveEligibilityRulesService.deleteLeaveEligibilityRules(id);
        return ResponseBuilder.noContent("Leave type rule eligibility successfully");
    }
}
