package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.TransferRequests;
import com.betopia.hrm.domain.employee.request.Transfer;
import com.betopia.hrm.services.employee.transferrequest.TransferRequestService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/v1/transfer-request")
@Tag(
        name = "Employee Management -> Transfer Request",
        description = "APIs for configurable transfer request. Includes operations to create, read, update, and delete transfer request."
)
public class TransferRequestsController {

    private final TransferRequestService transferRequestService;

    public TransferRequestsController(TransferRequestService transferRequestService) {
        this.transferRequestService = transferRequestService;
    }

    @GetMapping
    @Operation(summary = "1. Get all transfer request with pagination", description = "Retrieve all transfer request with pagination")
    public ResponseEntity<PaginationResponse<TransferRequests>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<TransferRequests> paginationResponse = transferRequestService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all transfer request", description = "Retrieve all transfer request without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<TransferRequests> transfer = transferRequestService.getAll();
        return ResponseBuilder.ok(transfer, "All transfer request fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get transfer request by id", description = "Retrieve a single transfer request by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        TransferRequests transfer = transferRequestService.show(id);
        return ResponseBuilder.ok(transfer, "Transfer request fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store transfer request", description = "Create/Save a new transfer request")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody Transfer request) {
        TransferRequests transfer = transferRequestService.store(request);
        return ResponseBuilder.created(transfer, "Transfer request created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update transfer request", description = "Update an existing transfer request")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody Transfer request) {
        TransferRequests transfer = transferRequestService.update(id, request);
        return ResponseBuilder.ok(transfer, "Transfer request updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete transfer request", description = "Remove a transfer request from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        transferRequestService.destroy(id);
        return ResponseBuilder.noContent("Transfer request deleted successfully");
    }
}
