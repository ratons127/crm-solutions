package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.DocumentType;
import com.betopia.hrm.domain.employee.request.DocumentTypeRequest;
import com.betopia.hrm.services.employee.DocumentTypeService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/document-type")
@Tag(
        name = "Employee -> Document type",
        description = "APIs for managing Document type. Includes operations to create, read, update, and delete Document type information."
)
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of Document Types",
            description = "Retrieves a paginated list of document Types from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch document Type records in a paginated format instead of retrieving all at once."
    )
    @PreAuthorize("hasAuthority('document-type-list')")
    public ResponseEntity<PaginationResponse<DocumentType>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<DocumentType> paginationResponse = documentTypeService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all document Types",
            description = "Retrieves a list of all document Types available in the system. "
                    + "This endpoint returns the complete document Types collection without pagination. "
                    + "Use it when you need to fetch all document Types at once."
    )
//    @PreAuthorize("hasAuthority('document-type-list')")
    public ResponseEntity<GlobalResponse> getAllDocumentTypes()
    {
        List<DocumentType> documentTypes = documentTypeService.getAllDocumentTypes();
        return ResponseBuilder.ok(documentTypes, "All document types fetch successful");

    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get document type by ID",
            description = "Retrieves the details of a specific document type using the provided ID. "
                    + "If the document type with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('document-type-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long documentTypeId)
    {
        DocumentType documentType = documentTypeService.show(documentTypeId);
        return ResponseBuilder.ok(documentType, "Document type fetch successful");
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new document type",
            description = "Creates a new document type in the system with the provided details. "
                    + "Required fields such as document type name, category, and description must be included in the request body. "
                    + "Returns the created document type information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('document-type-create')")
    public ResponseEntity<GlobalResponse> create(@Valid @RequestBody DocumentTypeRequest request)
    {
        DocumentType documentType = documentTypeService.create(request);
        return ResponseBuilder.ok(documentType, "Store successfully");
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update document type by ID",
            description = "Updates the details of an existing document typ using the provided ID. "
                    + "This endpoint allows modifying document typ information such as name, description, or other attributes. "
                    + "If the document typ with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('document-typ-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long documentTypId,
                                                 @Valid @RequestBody DocumentTypeRequest request)
    {
        DocumentType documentType = documentTypeService.update(documentTypId, request);
        return ResponseBuilder.ok(documentType, "Update successful");

    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete document type by ID",
            description = "Deletes a specific document type from the system using the provided ID. "
                    + "If the document type does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove document type records."
    )
//    @PreAuthorize("hasAuthority('document-type-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long documentTypeId)
    {
        documentTypeService.delete(documentTypeId);
        return ResponseBuilder.noContent("document type deleted successfully");
    }
}
