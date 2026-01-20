package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.AccessRevocationLogDTO;
import com.betopia.hrm.domain.employee.request.AccessRevocationLogRequest;
import com.betopia.hrm.services.employee.accessrevocationlog.AccessRevocationLogService;
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
@RequestMapping("/v1/access-log")
@Tag(
        name = "Employee Management -> Access Revocation Log",
        description = "APIs for configurable access revocation log. Includes operations to create, read, update, and delete access revocation log."
)
public class AccessRevocationLogController {

    private AccessRevocationLogService accessRevocationLogService;

    public AccessRevocationLogController(AccessRevocationLogService accessRevocationLogService){
        this.accessRevocationLogService =  accessRevocationLogService;
    }

    @GetMapping
    @Operation(summary = "1. Get all access revocation log with pagination", description = "Retrieve all access revocation log with pagination")
    public ResponseEntity<PaginationResponse<AccessRevocationLogDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<AccessRevocationLogDTO> paginationResponse = accessRevocationLogService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all access revocation log", description = "Retrieve all access revocation log without pagination")
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<AccessRevocationLogDTO> data = accessRevocationLogService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get access revocation log by id", description = "Retrieve a access revocation log by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = accessRevocationLogService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store access revocation log", description = "Create/Save a new access revocation log")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AccessRevocationLogRequest request)
    {
        var data = accessRevocationLogService.store(request);
        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update access revocation log", description = "Update an existing access revocation log")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AccessRevocationLogRequest request)
    {
        var data = accessRevocationLogService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete access revocation log", description = "Remove a access revocation log from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        accessRevocationLogService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
