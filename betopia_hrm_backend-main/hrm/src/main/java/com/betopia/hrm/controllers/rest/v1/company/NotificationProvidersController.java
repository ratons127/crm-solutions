package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationProviders;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.request.NotificationProvidersRequest;
import com.betopia.hrm.services.company.notification.NotificationProvidersService;
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
@RequestMapping("/v1/notification-providers")
@Tag(
        name = "Company -> Notification Providers",
        description = "APIs for managing Notification Providers. Includes operations to create, read, update, and delete notification providers information."
)
public class NotificationProvidersController {

    private final NotificationProvidersService  notificationProvidersService;

    public NotificationProvidersController(NotificationProvidersService notificationProvidersService) {
        this.notificationProvidersService = notificationProvidersService;
    }

    @GetMapping
    @Operation(summary = "1. Get all Notification Providers with pagination", description = "Retrieve a list of all Notification Providers with pagination.")
    public ResponseEntity<PaginationResponse<NotificationProviders>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NotificationProviders> paginationResponse = notificationProvidersService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "1. Get Notification Providers", description = "Retrieve notification providers without pagination")
    public ResponseEntity<GlobalResponse> getAllNotificationProviders() {
        List<NotificationProviders> notificationProviders = notificationProvidersService.getAllNotificationProviders();
        return ResponseBuilder.ok(notificationProviders, "All Notification Providers fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "2. Get Notification Providers by id", description = "Retrieve a single Notification Providers by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        NotificationProviders notificationProviders = notificationProvidersService.getNotificationProvidersById(id);
        return ResponseBuilder.ok(notificationProviders, "Notification Providers fetched successfully");
    }


    @PostMapping("/save")
    @Operation(summary = "3. Save/Store Notification Providers", description = "Create/Save a new Notification Providers")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody NotificationProvidersRequest request) {
        NotificationProviders createdNotificationProviders = notificationProvidersService.store(request);
        return ResponseBuilder.created(createdNotificationProviders, "Notification Providers created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "4. Update Notification Providers", description = "Update an existing Notification Providers")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody NotificationProvidersRequest request) {
        NotificationProviders updatedNotificationProviders = notificationProvidersService.updateNotificationProviders(id, request);
        return ResponseBuilder.ok(updatedNotificationProviders, "Notification Providers updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "5. Delete Notification Providers", description = "Remove a Notification Providers from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        notificationProvidersService.deleteNotificationProviders(id);
        return ResponseBuilder.noContent("Notification Providers deleted successfully");
    }

}
