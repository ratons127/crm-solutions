package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeClearanceChecklistDTO;
import com.betopia.hrm.domain.employee.request.EmployeeClearanceChecklistRequest;
import com.betopia.hrm.services.employee.employeeclearancechecklist.EmployeeClearanceChecklistService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/checklist")
@Tag(
        name = "Employee Management -> Exit Clearance Checklist",
        description = "APIs for configurable Exit Clearance checklist. Includes operations to create, read, update, and delete exit clearance checklist."
)
public class EmployeeClearanceChecklistController {

    private final EmployeeClearanceChecklistService employeeClearanceChecklistService;
    public EmployeeClearanceChecklistController(EmployeeClearanceChecklistService employeeClearanceChecklistService) {
        this.employeeClearanceChecklistService = employeeClearanceChecklistService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all  exit clearance checklist with pagination",
            description = "Retrieve a list of all  exit checklist item with pagination."
    )
    public ResponseEntity<PaginationResponse<EmployeeClearanceChecklistDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<EmployeeClearanceChecklistDTO> response = employeeClearanceChecklistService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "1. Get all  exit clearance checklist without pagination",
            description = "Retrieve a list of all  exit clearance checklist without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<EmployeeClearanceChecklistDTO> data = employeeClearanceChecklistService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(
            summary = "2. Store  exit clearance checklist",
            description = "Creating a new  exit clearance checklist"
    )
    public ResponseEntity<GlobalResponse> store(
            @RequestPart("data") @Valid EmployeeClearanceChecklistRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        EmployeeClearanceChecklistDTO separation = employeeClearanceChecklistService.saveChecklist(request, files);
        return ResponseBuilder.created(separation, "Employee clearance checklist created successfully");
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show  exit clearance checklist by id",
            description = "Show a new  exit clearance checklist"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = employeeClearanceChecklistService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "4. Update employee clearance checklist",
            description = "Update an existing employee clearance checklist with optional files")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @RequestPart("data") @Valid EmployeeClearanceChecklistRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        EmployeeClearanceChecklistDTO separation = employeeClearanceChecklistService.updateChecklist(id, request, files);
        return ResponseBuilder.ok(separation, "Employee clearance checklist updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "5. Delete exit clearance checklist",
            description = "Deleting a new exit clearance checklist"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        employeeClearanceChecklistService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
