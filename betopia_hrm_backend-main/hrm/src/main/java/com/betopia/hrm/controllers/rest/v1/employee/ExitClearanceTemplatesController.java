package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitClearanceTemplateDTO;
import com.betopia.hrm.domain.employee.request.ExitClearanceTemplateRequest;
import com.betopia.hrm.services.employee.exitclearancetemplate.ExitClearanceTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/exit-template")
@Tag(
        name = "Employee Management -> Exit Clearance Template",
        description = "APIs for configurable Exit Clearance Template. Includes operations to create, read, update, and delete exit clearance template."
)
public class ExitClearanceTemplatesController {

    private final ExitClearanceTemplateService exitClearanceTemplateService;

    public ExitClearanceTemplatesController(ExitClearanceTemplateService exitClearanceTemplateService) {
        this.exitClearanceTemplateService = exitClearanceTemplateService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all  exit clearance template with pagination",
            description = "Retrieve a list of all  exit clearance template with pagination."
    )
    public ResponseEntity<PaginationResponse<ExitClearanceTemplateDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ExitClearanceTemplateDTO> response = exitClearanceTemplateService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all  exit clearance template without pagination",
            description = "Retrieve a list of all  exit clearance template without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ExitClearanceTemplateDTO> data = exitClearanceTemplateService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store  exit clearance template",
            description = "Creating a new  exit clearance template"
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ExitClearanceTemplateRequest request)
    {
        var data = exitClearanceTemplateService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show  exit clearance template by id",
            description = "Show a new  exit clearance template"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = exitClearanceTemplateService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "3. Update  exit clearance template",
            description = "Updating a new  exit clearance template"
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ExitClearanceTemplateRequest request)
    {
        var data = exitClearanceTemplateService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete exit clearance templated",
            description = "Deleting a new exit clearance template"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        exitClearanceTemplateService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
