package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeDocumentDTO;
import com.betopia.hrm.domain.employee.entity.EmployeeDocument;
import com.betopia.hrm.domain.employee.request.EmployeeDocumentRequest;
import com.betopia.hrm.services.employee.EmployeeDocumentService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/employee-documents")
@Tag(
        name = "Employee -> Employee document",
        description = "Operations related to employee documents"
)
public class EmployeeDocumentController {

    private final EmployeeDocumentService employeeDocumentService;

    public EmployeeDocumentController(EmployeeDocumentService employeeDocumentService) {
        this.employeeDocumentService = employeeDocumentService;
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('employee-document')")
    @Operation(summary = "1. Get all employee documents with pagination", description = "Retrieve all employee documents with pagination")
    public ResponseEntity<PaginationResponse<EmployeeDocument>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<EmployeeDocument> paginationResponse = employeeDocumentService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all employee documents", description = "Retrieve all lemployee documents without pagination")
//    @PreAuthorize("hasAuthority('employee-document-list')")
    public ResponseEntity<GlobalResponse> getAllEmployeeDocuments()
    {
        var employeeDocuments  = employeeDocumentService.getAllEmployeeDocuments();

        GlobalResponse response = GlobalResponse.success(
                employeeDocuments,
                "All employee documents fetched successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get employee document by id", description = "Retrieve a single employee document by its ID")
//    @PreAuthorize("hasAuthority('employee-document-edit')")
    public ResponseEntity<GlobalResponse> show(
            @PathVariable("id") Long id
    ) {
        EmployeeDocumentDTO getId = employeeDocumentService.show(id);
        return ResponseBuilder.ok(getId, "Employee Document fetch successfully");
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "4. Save/Store employee document", description = "Create/Save a new employee document")
    public ResponseEntity<GlobalResponse> store(
            @RequestPart("data") @Valid EmployeeDocumentRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        EmployeeDocumentDTO created = employeeDocumentService.store(request, file);
        return ResponseBuilder.created(created, "Employee document created successfully");
    }


    @PutMapping(
            value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "5. Update employee document",description = "Update an existing employee document and create a new version"
    )
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @RequestPart("data") @Valid EmployeeDocumentRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        EmployeeDocumentDTO updated = employeeDocumentService.update(id, request, file);
        return ResponseBuilder.ok(updated, "Employee document updated successfully and new version created");
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete employee document", description = "Remove a employee document from the system")
//    @PreAuthorize("hasAuthority('employee-document-delete')")
    public ResponseEntity<GlobalResponse> delete(
            @PathVariable("id") Long id
    ) {
        employeeDocumentService.delete(id);
        return ResponseBuilder.noContent("employee document deleted successfully");
    }
}
