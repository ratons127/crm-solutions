package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationDocumentsDTO;
import com.betopia.hrm.domain.employee.request.SeparationDocumentsRequest;
import com.betopia.hrm.services.employee.separationdocuments.SeparationDocumentsService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/separation-doc")
@Tag(
        name = "Employee Management -> Separation Documents",
        description = "APIs for configurable separation documents. Includes operations to create, read, update, and delete separation documents."
)
public class SeparationDocumentsController {

    private final SeparationDocumentsService separationDocumentsService;

    public SeparationDocumentsController(SeparationDocumentsService separationDocumentsService) {
        this.separationDocumentsService = separationDocumentsService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all separation documents with pagination",
            description = "Retrieve a list of all separation documents item with pagination."
    )
    public ResponseEntity<PaginationResponse<SeparationDocumentsDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<SeparationDocumentsDTO> response = separationDocumentsService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all separation documents without pagination",
            description = "Retrieve a list of all  final settlement without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<SeparationDocumentsDTO> data = separationDocumentsService.getAll();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/save")
    @Operation(
            summary = "3. Store separation documents",
            description = "Creating a new separation documents"
    )
    public ResponseEntity<GlobalResponse> store(
            @RequestPart("data") @Valid SeparationDocumentsRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        SeparationDocumentsDTO separation = separationDocumentsService.saveSeparation(request, files);
        return ResponseBuilder.created(separation, "separation documents created successfully");
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. show separation documents by id",
            description = "Show a new separation documents"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = separationDocumentsService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update separation documents",
            description = "Update an existing separation documents with optional files")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @RequestPart("data") @Valid SeparationDocumentsRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        SeparationDocumentsDTO separation = separationDocumentsService.update(id, request, files);
        return ResponseBuilder.ok(separation, "final settlement updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete separation documents",
            description = "Deleting a separation documents"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        separationDocumentsService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
