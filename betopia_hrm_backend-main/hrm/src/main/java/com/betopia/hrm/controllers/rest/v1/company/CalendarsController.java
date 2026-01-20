package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.company.entity.Calendars;
import com.betopia.hrm.domain.company.request.CalendarsRequest;
import com.betopia.hrm.services.company.calendars.CalendarsService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/calendars")
@Tag(
        name = "Company -> Calendars",
        description = "APIs for managing Calendars. Includes operations to create, read, update, and delete Calendars information."
)
public class CalendarsController {

    private final CalendarsService calendarsService;
    public CalendarsController(CalendarsService calendarsService) {
        this.calendarsService = calendarsService;
    }

    @GetMapping("/all")
    @Operation(summary = "1. Get all calendars", description = "Retrieve all calendars without pagination")
    public ResponseEntity<GlobalResponse> findAllLookupSetupDetails() {
        List<Calendars> calendars = calendarsService.getAllCalendars();
        return ResponseBuilder.ok(calendars, "All calendars fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "2. Get calendars by id", description = "Retrieve a single calendars by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        Calendars calendars = calendarsService.getCalendarsById(id);
        return ResponseBuilder.ok(calendars, "Calendars fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "3. Save/Store calendars", description = "Create/Save a new calendars")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CalendarsRequest request) {
        Calendars calendars = calendarsService.store(request);
        return ResponseBuilder.created(calendars, "Calendars created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "4. Update calendars", description = "Update an existing calendars")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CalendarsRequest request) {
        Calendars updatedCalendars = calendarsService.updateCalendars(id, request);
        return ResponseBuilder.ok(updatedCalendars, "Calendars updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "5. Delete calendars", description = "Remove a calendars from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        calendarsService.deleteCalendars(id);
        return ResponseBuilder.noContent("Calendars deleted successfully");
    }
}
