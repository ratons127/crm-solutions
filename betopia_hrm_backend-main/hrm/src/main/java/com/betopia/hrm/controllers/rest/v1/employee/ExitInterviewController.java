package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitInterviewDTO;
import com.betopia.hrm.domain.employee.request.ExitInterviewRequest;
import com.betopia.hrm.services.employee.exitinterview.ExitInterviewService;
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
@RequestMapping("/v1/exit-interview")
@Tag(
        name = "Employee Management -> Exit Interview",
        description = "APIs for configurable exit interview. Includes operations to create, read, update, and delete exit interview."
)
public class ExitInterviewController {

    private final ExitInterviewService exitInterviewService;

    public ExitInterviewController(ExitInterviewService exitInterviewService){
        this.exitInterviewService = exitInterviewService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all exit interview with pagination",
            description = "Retrieve a list of all  exit interview item with pagination."
    )
    public ResponseEntity<PaginationResponse<ExitInterviewDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<ExitInterviewDTO> response = exitInterviewService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all exit interview without pagination",
            description = "Retrieve a list of all exit interview without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<ExitInterviewDTO> data = exitInterviewService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(summary = "4. Save/Store exit interview", description = "Create/Save a new exit interview")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ExitInterviewRequest request)
    {
        var data = exitInterviewService.store(request);
        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. show exit interview by id",
            description = "Show a new exit interview"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = exitInterviewService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update exit interview", description = "Update an existing exit interview")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ExitInterviewRequest request)
    {
        var data = exitInterviewService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete exit interview",
            description = "Deleting a exit interview"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        exitInterviewService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
