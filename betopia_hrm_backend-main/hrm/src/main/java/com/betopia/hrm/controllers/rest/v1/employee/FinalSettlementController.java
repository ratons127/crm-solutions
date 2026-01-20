package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.FinalSettlementDTO;
import com.betopia.hrm.domain.employee.request.FinalSettlementRequest;
import com.betopia.hrm.services.employee.finalsettlement.FinalSettlementService;
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
@RequestMapping("/v1/settlement")
@Tag(
        name = "Employee Management -> Final Settlement",
        description = "APIs for configurable final settlement. Includes operations to create, read, update, and delete final settlement."
)
public class FinalSettlementController {

    private final FinalSettlementService finalSettlementService;

    public FinalSettlementController(FinalSettlementService finalSettlementService) {
        this.finalSettlementService = finalSettlementService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all  final settlement with pagination",
            description = "Retrieve a list of all  final settlement item with pagination."
    )
    public ResponseEntity<PaginationResponse<FinalSettlementDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<FinalSettlementDTO> response = finalSettlementService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all  final settlement without pagination",
            description = "Retrieve a list of all  final settlement without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<FinalSettlementDTO> data = finalSettlementService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(
            summary = "3. Store final settlement",
            description = "Creating a new final settlement"
    )
    public ResponseEntity<GlobalResponse> store(
            @RequestPart("data") @Valid FinalSettlementRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        FinalSettlementDTO separation = finalSettlementService.saveSettlement(request, files);
        return ResponseBuilder.created(separation, "final settlement created successfully");
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. show final settlement by id",
            description = "Show a new final settlement"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = finalSettlementService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update final settlement",
            description = "Update an existing final settlement with optional files")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @RequestPart("data") @Valid FinalSettlementRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        FinalSettlementDTO separation = finalSettlementService.update(id, request, files);
        return ResponseBuilder.ok(separation, "final settlement updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete final settlement",
            description = "Deleting a new final settlement"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        finalSettlementService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
