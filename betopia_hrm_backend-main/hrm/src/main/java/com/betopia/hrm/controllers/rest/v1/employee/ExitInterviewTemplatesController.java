package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitInterviewTemplatesDTO;
import com.betopia.hrm.domain.employee.request.ExitInterviewTemplatesRequest;
import com.betopia.hrm.services.employee.exitinterviewtemplates.ExitInterviewTemplateService;
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
@RequestMapping("/v1/exit-interview-template")
@Tag(
        name = "Employee Management -> Exit Interview Template",
        description = "APIs for configurable exit interview template. Includes operations to create, read, update, " +
                "and delete exit interview template."
)
public class ExitInterviewTemplatesController {

    private final ExitInterviewTemplateService exitInterviewTemplateService;

    public ExitInterviewTemplatesController(ExitInterviewTemplateService exitInterviewTemplateService) {
        this.exitInterviewTemplateService = exitInterviewTemplateService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all exit interview template with pagination",
            description = "Retrieve a list of all  exit interview item with pagination."
    )
    public ResponseEntity<PaginationResponse<ExitInterviewTemplatesDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ExitInterviewTemplatesDTO> response = exitInterviewTemplateService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all exit interview template without pagination",
            description = "Retrieve a list of all exit interview template without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ExitInterviewTemplatesDTO> data = exitInterviewTemplateService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(summary = "4. Save/Store exit interview template", description = "Create/Save a new exit interview template")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ExitInterviewTemplatesRequest request)
    {
        var data = exitInterviewTemplateService.store(request);
        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. Show exit interview template by id",
            description = "Show a new exit interview template"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = exitInterviewTemplateService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update exit interview template", description = "Update an existing exit interview template")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ExitInterviewTemplatesRequest request)
    {
        var data = exitInterviewTemplateService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete exit interview template",
            description = "Deleting a exit interview template"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        exitInterviewTemplateService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

}
