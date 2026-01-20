package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.HandoverChecklistDTO;
import com.betopia.hrm.domain.dto.employee.SeparationReasonsDTO;
import com.betopia.hrm.domain.employee.request.HandoverChecklistRequest;
import com.betopia.hrm.domain.employee.request.SeparationReasonsRequest;
import com.betopia.hrm.services.employee.handoverchecklist.HandoverChecklistService;
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
@RequestMapping("/v1/handover")
@Tag(
        name = "Employee Management -> Handover Checklist",
        description = "APIs for configurable handover checklist. Includes operations to create, read, update, and delete handover checklist."
)
public class HandoverChecklistController {

    private final HandoverChecklistService handoverChecklistService;

    public HandoverChecklistController(HandoverChecklistService handoverChecklistService) {
        this.handoverChecklistService = handoverChecklistService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all handover checklist with pagination",
            description = "Retrieve a list of all handover checklist with pagination."
    )
    public ResponseEntity<PaginationResponse<HandoverChecklistDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<HandoverChecklistDTO> response = handoverChecklistService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all handover checklist without pagination",
            description = "Retrieve a list of all handover checklist without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<HandoverChecklistDTO> data = handoverChecklistService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(summary = "4. Save/Store handover checklist", description = "Create/Save a new handover checklist")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody HandoverChecklistRequest request)
    {
        var data = handoverChecklistService.store(request);
        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. Show handover checklist by id",
            description = "Show a new handover checklist"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = handoverChecklistService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update handover checklist", description = "Update an existing handover checklist")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody HandoverChecklistRequest request)
    {
        var data = handoverChecklistService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete handover checklist",
            description = "Deleting a handover checklist"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        handoverChecklistService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
