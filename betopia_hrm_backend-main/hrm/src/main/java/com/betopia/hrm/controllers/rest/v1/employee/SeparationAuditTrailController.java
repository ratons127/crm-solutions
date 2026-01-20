package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationAuditTrailDTO;
import com.betopia.hrm.domain.employee.request.SeparationAuditTrailRequest;
import com.betopia.hrm.services.employee.separationaudittrail.SeparationAuditTrailService;
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
@RequestMapping("/v1/separation-audit")
@Tag(
        name = "Employee Management -> Separation Audit",
        description = "APIs for configurable separation audit. Includes operations to create, read, update, " +
                "and delete separation audit."
)
public class SeparationAuditTrailController {

    private final SeparationAuditTrailService separationAuditTrailService;

    public SeparationAuditTrailController(SeparationAuditTrailService separationAuditTrailService){
        this.separationAuditTrailService = separationAuditTrailService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all separation audit with pagination",
            description = "Retrieve a list of all separation audit with pagination."
    )
    public ResponseEntity<PaginationResponse<SeparationAuditTrailDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<SeparationAuditTrailDTO> response = separationAuditTrailService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all separation audit without pagination",
            description = "Retrieve a list of all separation audit without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<SeparationAuditTrailDTO> data = separationAuditTrailService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(summary = "4. Save/Store separation audit", description = "Create/Save a new separation audit")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody SeparationAuditTrailRequest request)
    {
        var data = separationAuditTrailService.store(request);
        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. Show separation audit by id",
            description = "Show a new separation audit"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = separationAuditTrailService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update separation audit", description = "Update an existing separation audit")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SeparationAuditTrailRequest request)
    {
        var data = separationAuditTrailService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete separation audit",
            description = "Deleting a separation audit"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        separationAuditTrailService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
