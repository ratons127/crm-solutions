package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.PromotionRequests;
import com.betopia.hrm.domain.employee.request.Promotion;
import com.betopia.hrm.services.employee.promotionrequests.PromotionRequestsService;
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
@RequestMapping("/v1/promotion-request")
@Tag(
        name = "Employee Management -> Promotion Request",
        description = "APIs for configurable promotion request. Includes operations to create, read, update, and delete promotion request."
)
public class PromotionRequestsController {

    private final PromotionRequestsService promotionRequestsService;

    public PromotionRequestsController(PromotionRequestsService promotionRequestsService) {
        this.promotionRequestsService = promotionRequestsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all promotion request with pagination", description = "Retrieve all promotion request with pagination")
    public ResponseEntity<PaginationResponse<PromotionRequests>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<PromotionRequests> paginationResponse = promotionRequestsService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all promotion request", description = "Retrieve all promotion request without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<PromotionRequests> promotionRequests = promotionRequestsService.getAll();
        return ResponseBuilder.ok(promotionRequests, "All promotion request fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get promotion request by id", description = "Retrieve a single promotion request by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        PromotionRequests promotionRequests = promotionRequestsService.show(id);
        return ResponseBuilder.ok(promotionRequests, "Promotion Request fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store promotion request", description = "Create/Save a new promotion request")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody Promotion request) {
        PromotionRequests promotionRequests = promotionRequestsService.store(request);
        return ResponseBuilder.created(promotionRequests, "Promotion Request created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update promotion request", description = "Update an existing promotion request")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody Promotion request) {
        PromotionRequests promotionRequests = promotionRequestsService.update(id, request);
        return ResponseBuilder.ok(promotionRequests, "Promotion Request updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete promotion request", description = "Remove a promotion request from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        promotionRequestsService.destroy(id);
        return ResponseBuilder.noContent("Promotion Request deleted successfully");
    }
}
