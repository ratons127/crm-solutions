package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.CalendarAssign;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.request.CalenderAssignRequest;
import com.betopia.hrm.services.company.calendarAssign.CalendarAssignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/calendar-assigns")
@Tag(name = "Company -> calendar-assign", description = "Operations related to managing calendar-assign")
public class CalendarAssignController {

    private final CalendarAssignService calendarAssignService;

    public CalendarAssignController(CalendarAssignService calendarAssignService) {
        this.calendarAssignService = calendarAssignService;
    }

    @GetMapping
    @Operation(summary = "1. Get all calendar-assign with pagination", description = "Retrieve a list of all calendar-assign with pagination.")
    @PreAuthorize("hasAuthority('calendar-assign-list')")
    public ResponseEntity<PaginationResponse<CalendarAssign>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<CalendarAssign> paginationResponse = calendarAssignService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all calendar-assign without pagination", description = "Retrieve a list of all calendar-assign without pagination")
    @PreAuthorize("hasAuthority('calendar-assign-list')")
    public ResponseEntity<GlobalResponse> getAllCalendarAssigns()
    {
        var calendarAssigns = calendarAssignService.getAllCalendarAssigns();

        GlobalResponse response = GlobalResponse.success(
                calendarAssigns,
                "All calendar-assigns fetch successful",
                200
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store calendar-assign", description = "Creating a new calendar-assign")
    @PreAuthorize("hasAuthority('calendar-assign-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CalenderAssignRequest request)
    {
        var calendarAssign = calendarAssignService.store(request);

        GlobalResponse response = GlobalResponse.success(
                calendarAssign,
                "Calendar-assign created successfully",
                201
        );

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. calendar-assign get by id", description = "Return a single calendar-assign by id")
    @PreAuthorize("hasAuthority('calendar-assign-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long calendarAssignId)
    {
        var calendarAssign = calendarAssignService.show(calendarAssignId);

        GlobalResponse response = GlobalResponse.success(
                calendarAssign,
                "Calendar-assign fetch successful",
                200
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update calendar-assign", description = "Update an existing calendar-assign by id")
    @PreAuthorize("hasAuthority('calendar-assign-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long calendarAssignId, @Valid @RequestBody CalenderAssignRequest request) {
        var calendarAssign = calendarAssignService.update(calendarAssignId, request);
        GlobalResponse response = GlobalResponse.success(
                calendarAssign,
                "Calendar-assign updated successfully",
                200
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete calendar-assign", description = "Delete an existing calendar-assign by id")
    @PreAuthorize("hasAuthority('calendar-assign-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long calendarAssignId) {
        calendarAssignService.destroy(calendarAssignId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "Calendar-assign deleted successfully",
                200
        );
        return ResponseEntity.ok(response);

    }
}
