package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationEvents;
import com.betopia.hrm.domain.company.entity.NotificationProviders;
import com.betopia.hrm.domain.company.request.NotificationEventsRequest;
import com.betopia.hrm.services.company.notification.NotificationEventsService;
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
@RequestMapping("/v1/notification-events")
@Tag(
        name = "Company -> Notification Events",
        description = "APIs for managing Notification Events. Includes operations to create, read, update, and delete notification events information."
)
public class NotificationEventsController {

    private final NotificationEventsService notificationEventsService;

    public NotificationEventsController(NotificationEventsService notificationEventsService) {
        this.notificationEventsService = notificationEventsService;
    }

    @GetMapping
    @Operation(summary = "1. Get all Notification Providers with pagination", description = "Retrieve a list of all Notification Providers with pagination.")
    public ResponseEntity<PaginationResponse<NotificationEvents>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NotificationEvents> paginationResponse = notificationEventsService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "1. Get Notification Events", description = "Retrieve notification Events without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypeRules() {
        List<NotificationEvents> notificationEvents = notificationEventsService.getAllNotificationEvents();
        return ResponseBuilder.ok(notificationEvents, "All Notification Events fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "2. Get Notification Events by id", description = "Retrieve a single Notification Events by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        NotificationEvents notificationEvents = notificationEventsService.getNotificationEventsById(id);
        return ResponseBuilder.ok(notificationEvents, "Notification Events fetched successfully");
    }


    @PostMapping("/save")
    @Operation(summary = "3. Save/Store Notification Events", description = "Create/Save a new Notification Events")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody NotificationEventsRequest request) {
        NotificationEvents createdNotificationEvents = notificationEventsService.store(request);
        return ResponseBuilder.created(createdNotificationEvents, "Notification Events created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "4. Update Notification Events", description = "Update an existing Notification Events")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody NotificationEventsRequest request) {
        NotificationEvents updatedNotificationEvents = notificationEventsService.updateNotificationEvents(id, request);
        return ResponseBuilder.ok(updatedNotificationEvents, "Notification Providers Events successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "5. Delete Notification Events", description = "Remove a Notification Events from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        notificationEventsService.deleteNotificationEvents(id);
        return ResponseBuilder.noContent("Notification Events deleted successfully");
    }
}
