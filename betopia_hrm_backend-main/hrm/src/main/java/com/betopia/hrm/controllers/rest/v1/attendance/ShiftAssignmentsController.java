package com.betopia.hrm.controllers.rest.v1.attendance;


import com.betopia.hrm.domain.attendance.request.ShiftAssignmentsRequest;
import com.betopia.hrm.domain.attendance.request.ShowShiftAssignRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftAssignmentsDTO;
import com.betopia.hrm.domain.dto.attendance.ShowShiftAssignDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeShiftAssignDTO;
import com.betopia.hrm.domain.employee.request.EmployeeShiftAssignRequest;
import com.betopia.hrm.services.attendance.shiftassignment.ShiftAssignmentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/v1/shift-assignment")
@Tag(
        name = "Attendance -> Shift Assignment",
        description = "APIs for managing shift assignment"
)
public class ShiftAssignmentsController {

    private final ShiftAssignmentsService shiftAssignmentsService;

    public ShiftAssignmentsController(ShiftAssignmentsService shiftAssignmentsService){
        this.shiftAssignmentsService = shiftAssignmentsService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all shift assignment with pagination",
            description = "Retrieve a list of all shift assignment with pagination."
    )
    // @PreAuthorize("hasAuthority('shift-assignment-list')")
    public ResponseEntity<PaginationResponse<ShiftAssignmentsDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ShiftAssignmentsDTO> response = shiftAssignmentsService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all shift assignment without pagination",
            description = "Retrieve a list of all shift assignment pagination"
    )
    // @PreAuthorize("hasAuthority('shift-assignment-list')")
    public ResponseEntity<GlobalResponse> getAll()
    {
        List<ShiftAssignmentsDTO> shiftAssignmentsDTOS = shiftAssignmentsService.getAll();

        GlobalResponse response = GlobalResponse.success(
                shiftAssignmentsDTOS,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store shift assignment",
            description = "Creating a new shift assignment"
    )
    // @PreAuthorize("hasAuthority('shift-assignment-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ShiftAssignmentsRequest request)
    {
        var data = shiftAssignmentsService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show shift assignment by id",
            description = "Creating a new shift assignment"
    )
    // @PreAuthorize("hasAuthority('shift-assignment-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = shiftAssignmentsService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "3. Update shift assignment",
            description = "Creating a new shift assignment"
    )
    // @PreAuthorize("hasAuthority('shift-assignment-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ShiftAssignmentsRequest request)
    {
        var data = shiftAssignmentsService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "3. Delete shift assignment",
            description = "Creating a new shift assignment"
    )
    // @PreAuthorize("hasAuthority('shift-assignment-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id)
    {
        shiftAssignmentsService.destroy(id);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @PostMapping("/employee")
    @Operation(
            summary = "4. shift assignment to an employee",
            description = "Creating a new shift assignment for employee"
    )
    public ResponseEntity<GlobalResponse> assignShiftToEmployees(
            @Valid @RequestBody ShiftAssignmentsRequest request
    ) {
        List<ShiftAssignmentsDTO> assigned = shiftAssignmentsService.assignShiftToEmployees(request);

        GlobalResponse response = GlobalResponse.success(
                assigned,
                "Shift assigned successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "6. Get shift assign for showing",
            description = "Get the list of shift assigns"
    )
    @PostMapping("/get-shifts")
    public  ResponseEntity<GlobalResponse> getAllShiftAssignments() {

        List<ShowShiftAssignDTO> employees = shiftAssignmentsService.getAllShiftAssignments();

        GlobalResponse response = GlobalResponse.success(
                employees,
                "All shift assign successfully fetched",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

}
