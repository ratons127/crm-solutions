package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.DocumentExpiryAlert;
import com.betopia.hrm.domain.employee.request.DocumentExpiryAlertRequest;
import com.betopia.hrm.services.employee.DocumentExpiryAlertService;
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
@RequestMapping("/v1/document-expiry-alert")
@Tag(
        name = "Employee -> Document expiry alert",
        description = "APIs for managing Document expiry alert. Includes operations to create, read, update, and delete designation information."
)
public class DocumentExpiryAlertController {

    private final DocumentExpiryAlertService documentExpiryAlertService;

    public DocumentExpiryAlertController(DocumentExpiryAlertService documentExpiryAlertService) {
        this.documentExpiryAlertService = documentExpiryAlertService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of Document expiry alert",
            description = "Retrieves a paginated list of Document expiry alerts from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch Document expiry alert records in a paginated format instead of retrieving all at once."
    )
//    @PreAuthorize("hasAuthority('document-expiry-alert-list')")
    public ResponseEntity<PaginationResponse<DocumentExpiryAlert>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<DocumentExpiryAlert> paginationResponse = documentExpiryAlertService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }


    @GetMapping("/all")
    @Operation(
            summary = "2. Get all Document expiry alerts",
            description = "Retrieves a list of all Document expiry alert available in the system. "
                    + "This endpoint returns the complete Document expiry alert collection without pagination. "
                    + "Use it when you need to fetch all Document expiry alerts at once."
    )
//    @PreAuthorize("hasAuthority('document-expiry-alert-list')")
    public ResponseEntity<GlobalResponse> getAllDocumentExpiryAlerts()
    {
        List<DocumentExpiryAlert> documentExpiryAlerts = documentExpiryAlertService.getAllDocumentExpiryAlerts();
        GlobalResponse response = GlobalResponse.success(
                documentExpiryAlerts,
                "All Document expiry alert fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "4. Get Document expiry alert by ID",
            description = "Retrieves the details of a specific Document expiry alert using the provided ID. "
                    + "If the Document expiry alert with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('document-verification-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long documentExpiryAlertId)
    {
        DocumentExpiryAlert documentExpiryAlert = documentExpiryAlertService.show(documentExpiryAlertId);
        GlobalResponse response = GlobalResponse.success(
                documentExpiryAlert,
                "Document expiry alert fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(
            summary = "3. Create a new Document expiry alert",
            description = "Creates a new Document expiry alert in the system with the provided details. "
                    + "Required fields such as Document expiry alert employee document id , status, and sent to  must be included in the request body. "
                    + "Returns the created dDocument expiry alert information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('document-expiry-alert-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody DocumentExpiryAlertRequest request)
    {
        DocumentExpiryAlert documentExpiryAlert = documentExpiryAlertService.store(request);
        GlobalResponse response = GlobalResponse.success(
                documentExpiryAlert,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("{id}")
    @Operation(
            summary = "5. Update Document expiry alert by ID",
            description = "Updates the details of an existing Document expiry alert using the provided ID. "
                    + "This endpoint allows modifying Document expiry alert information such as name, description, or other attributes. "
                    + "If the Document expiry alert with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('document-expiry-alert-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long documentExpiryAlertId,
                                                 @Valid @RequestBody DocumentExpiryAlertRequest request)
    {
        DocumentExpiryAlert documentExpiryAlert = documentExpiryAlertService.update(documentExpiryAlertId, request);
        GlobalResponse response = GlobalResponse.success(
                documentExpiryAlert,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete Document Expiry Alert by ID",
            description = "Deletes a specific Document Expiry Alert from the system using the provided ID. "
                    + "If the Document Expiry Alert does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove Document verification records."
    )
//    @PreAuthorize("hasAuthority('document-expiry-alert-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long documentExpiryAlertId)
    {
        documentExpiryAlertService.destroy(documentExpiryAlertId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "document Verification deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
