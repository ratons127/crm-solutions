package com.betopia.hrm.controllers.rest.v1.attendance;


import com.betopia.hrm.domain.attendance.request.AttendancePolicyRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendancePolicyDTO;
import com.betopia.hrm.services.attendance.attendancepolicy.AttendancePolicyService;
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
@RequestMapping("/v1/attendance-policy")
@Tag(
        name = "Attendance -> Attendance Policy",
        description = "APIs for managing attendance-policy"
)
public class AttendancePolicyController {

    private final AttendancePolicyService attendancePolicyService;

    public AttendancePolicyController(AttendancePolicyService attendancePolicyService){
        this.attendancePolicyService = attendancePolicyService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all attendance policy with pagination",
            description = "Retrieve a list of all attendance-policy with pagination."
    )
    // @PreAuthorize("hasAuthority('attendance-policy-list')")
    public ResponseEntity<PaginationResponse<AttendancePolicyDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<AttendancePolicyDTO> response = attendancePolicyService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all attendance policy without pagination",
            description = "Retrieve a list of all attendance policy pagination"
    )
    // @PreAuthorize("hasAuthority('attendance-policy-list')")
    public ResponseEntity<GlobalResponse> getAllAttendanceDeviceAssigns()
    {
        List<AttendancePolicyDTO> attendanceDeviceAssignDTOS = attendancePolicyService.getAll();

        GlobalResponse response = GlobalResponse.success(
                attendanceDeviceAssignDTOS,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store attendance policy",
            description = "Creating a new attendance policy"
    )
    // @PreAuthorize("hasAuthority('attendance-policy-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AttendancePolicyRequest request)
    {
        var data = attendancePolicyService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show attendance policy by id",
            description = "Creating a new attendance policy"
    )
    // @PreAuthorize("hasAuthority('attendance-policy-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = attendancePolicyService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "3. Update attendance policy",
            description = "Creating a new attendance policy"
    )
    // @PreAuthorize("hasAuthority('attendance-policy-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AttendancePolicyRequest request)
    {
        var data = attendancePolicyService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete attendance policy",
            description = "Deleting a new attendance policy"
    )
    // @PreAuthorize("hasAuthority('attendance-policy-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id)
    {
        attendancePolicyService.destroy(id);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

}
