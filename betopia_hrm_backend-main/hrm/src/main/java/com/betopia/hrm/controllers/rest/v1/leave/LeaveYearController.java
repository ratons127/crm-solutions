package com.betopia.hrm.controllers.rest.v1.leave;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveYear;
import com.betopia.hrm.domain.leave.request.LeaveYearRequest;
import com.betopia.hrm.services.leaves.leaveyear.LeaveYearService;
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
@RequestMapping("/v1/leave-years")
@Tag(
        name = "Leave Management -> LeaveYear",
        description = "Operations related to leave yearss"
)
public class LeaveYearController {

    private final LeaveYearService leaveYearService;

    public LeaveYearController(LeaveYearService leaveYearService) {
        this.leaveYearService = leaveYearService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave years with pagination", description = "Retrieve all leave years with pagination")
    public ResponseEntity<PaginationResponse<LeaveYear>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveYear> paginationResponse = leaveYearService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave years", description = "Retrieve all leave years without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveGroups() {
        List<LeaveYear> leaveYear = leaveYearService.getAllLeaveYears();
        return ResponseBuilder.ok(leaveYear, "All leave years fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave years by id", description = "Retrieve a single leave years by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LeaveYear leaveYear = leaveYearService.getLeaveYearById(id);
        return ResponseBuilder.ok(leaveYear, "Leave years fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store leave years", description = "Create/Save a new leave years")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveYearRequest request) {
        LeaveYear createdLeaveGroup = leaveYearService.store(request);
        return ResponseBuilder.created(createdLeaveGroup, "Leave years created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave groups", description = "Update an existing leave years")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveYearRequest request) {
        LeaveYear updatedLeaveYear = leaveYearService.updateLeaveYear(id, request);
        return ResponseBuilder.ok(updatedLeaveYear, "Leave years updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave years", description = "Remove a leave years from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveYearService.deleteLeaveYear(id);
        return ResponseBuilder.noContent("Leave years deleted successfully");
    }
}
