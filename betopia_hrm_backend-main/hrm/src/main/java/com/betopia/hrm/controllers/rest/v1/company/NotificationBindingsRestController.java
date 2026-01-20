package com.betopia.hrm.controllers.rest.v1.company;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationBindings;
import com.betopia.hrm.domain.company.entity.NotificationEvents;
import com.betopia.hrm.domain.company.request.NotificationBindingsRequest;
import com.betopia.hrm.services.company.notification.NotificationBindingsService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/notification-bindings")
@Tag(
        name = "Company -> Notification Bindings",
        description = "APIs for managing Notification Bindings. Includes operations to create, read, update, " +
                "and delete notification bindings information."
)
public class NotificationBindingsRestController {

    private final NotificationBindingsService notificationBindingsService;

    public NotificationBindingsRestController(NotificationBindingsService notificationBindingsService) {
        this.notificationBindingsService = notificationBindingsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all Notification bindings with pagination", description = "Retrieve a list of all Notification bindings with pagination.")
    public ResponseEntity<PaginationResponse<NotificationBindings>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NotificationBindings> paginationResponse = notificationBindingsService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "1. Get Notification Bindings", description = "Retrieve notification bindings without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<NotificationBindings> notificationBindings = notificationBindingsService.getAllNotificationBindings();
        return ResponseBuilder.ok(notificationBindings, "All notification bindings fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "2. Get Notification Bindings by id", description = "Retrieve a single Notification bindings by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        NotificationBindings notificationBindings = notificationBindingsService.getNotificationBindingsById(id);
        return ResponseBuilder.ok(notificationBindings, "Notification bindings fetched successfully");
    }


    @PostMapping("/save")
    @Operation(summary = "3. Save/Store Notification bindings", description = "Create/Save a new Notification bindings")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody NotificationBindingsRequest request) {
        NotificationBindings createdNotificationBindings = notificationBindingsService.store(request);
        return ResponseBuilder.created(createdNotificationBindings, "Notification bindings created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "4. Update Notification bindings", description = "Update an existing Notification bindings")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody NotificationBindingsRequest request) {
        NotificationBindings updatedNotificationBindings = notificationBindingsService.updateNotificationBindings(id, request);
        return ResponseBuilder.ok(updatedNotificationBindings, "Notification Providers bindings successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "5. Delete Notification bindings", description = "Remove a Notification binding from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        notificationBindingsService.deleteNotificationBindings(id);
        return ResponseBuilder.noContent("Notification bindings deleted successfully");
    }
}
