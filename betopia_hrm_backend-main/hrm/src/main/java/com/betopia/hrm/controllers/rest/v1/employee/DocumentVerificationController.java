package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.DocumentVerification;
import com.betopia.hrm.domain.employee.request.DocumentVerificationRequest;
import com.betopia.hrm.services.employee.DocumentVerificationService;
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
@RequestMapping("/v1/document-verification")
@Tag(
        name = "Employee -> Document verification",
        description = "APIs for managing Document verification. Includes operations to create, read, update, and delete designation information."
)
public class DocumentVerificationController {

    private final DocumentVerificationService documentVerificationService;

    public DocumentVerificationController(DocumentVerificationService documentVerificationService) {
        this.documentVerificationService = documentVerificationService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of Document verification",
            description = "Retrieves a paginated list of Document verifications from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch Document verification records in a paginated format instead of retrieving all at once."
    )
//    @PreAuthorize("hasAuthority('document-verification-list')")
    public ResponseEntity<PaginationResponse<DocumentVerification>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<DocumentVerification> paginationResponse = documentVerificationService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }


    @GetMapping("/all")
    @Operation(
            summary = "2. Get all document verifications",
            description = "Retrieves a list of all document verification available in the system. "
                    + "This endpoint returns the complete document verification collection without pagination. "
                    + "Use it when you need to fetch all document verifications at once."
    )
//    @PreAuthorize("hasAuthority('document-verification-list')")
    public ResponseEntity<GlobalResponse> getAllDocumentVerifications()
    {
        List<DocumentVerification> documentVerifications = documentVerificationService.getAllDocumentVerifications();
        GlobalResponse response = GlobalResponse.success(
                documentVerifications,
                "All document verification fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get document verification by ID",
            description = "Retrieves the details of a specific document verification using the provided ID. "
                    + "If the document verification with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('document-verification-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long documentVerificationId)
    {
        DocumentVerification documentVerification = documentVerificationService.show(documentVerificationId);
        GlobalResponse response = GlobalResponse.success(
                documentVerification,
                "Document Verification fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(
            summary = "3. Create a new document verification",
            description = "Creates a new document verification in the system with the provided details. "
                    + "Required fields such as document verification name, status, and description must be included in the request body. "
                    + "Returns the created document verification information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('document-verification-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody DocumentVerificationRequest request)
    {
        DocumentVerification documentVerification = documentVerificationService.store(request);
        GlobalResponse response = GlobalResponse.success(
                documentVerification,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update document verification by ID",
            description = "Updates the details of an existing Document verification using the provided ID. "
                    + "This endpoint allows modifying Document verification information such as name, description, or other attributes. "
                    + "If the Document verification with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('document-verification-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long documentVerificationId,
                                                 @Valid @RequestBody DocumentVerificationRequest request)
    {
        DocumentVerification documentVerification = documentVerificationService.update(documentVerificationId, request);
        GlobalResponse response = GlobalResponse.success(
                documentVerification,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete Document verification by ID",
            description = "Deletes a specific Document verification from the system using the provided ID. "
                    + "If the Document verification does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove Document verification records."
    )
//    @PreAuthorize("hasAuthority('document-verification-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long documentVerificationId)
    {
        documentVerificationService.destroy(documentVerificationId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "document Verification deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    //    @PreAuthorize("hasAuthority('document-verification-edit')")
    @PatchMapping("{id}")
    public ResponseEntity<GlobalResponse> updateStatus(
            @PathVariable("id") Long documentVerificationId,
            @RequestBody DocumentVerificationRequest request
    )  {
        DocumentVerification documentVerification = documentVerificationService.updateStatus(documentVerificationId, request);
        GlobalResponse response = GlobalResponse.success(
                documentVerification,
                "Status change and notification sent successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
