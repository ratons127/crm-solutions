package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.company.entity.CalendarHolidays;
import com.betopia.hrm.domain.company.request.CalendarHolidaysRequest;
import com.betopia.hrm.domain.company.request.UpdateIsHolidayRequest;
import com.betopia.hrm.services.company.calendarsholidays.CalendarHolidaysService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/calendar-holidays")
@Tag(
        name = "Company -> Calendar Holidays",
        description = "APIs for managing Calendars holidays. Includes operations to create, read, update, and delete Calendars holidays information."
)
public class CalendarHolidaysController {
    private final CalendarHolidaysService calendarHolidaysService;

    public CalendarHolidaysController(CalendarHolidaysService calendarHolidaysService) {
        this.calendarHolidaysService = calendarHolidaysService;
    }

    @Operation(
            summary = "Get current year holidays",
            description = "Fetches all holidays from calendar_holidays that belong to the calendar with the current year."
    )
    @GetMapping("/current-year")
    public ResponseEntity<GlobalResponse> getHolidaysForCurrentYear() {
        List<CalendarHolidays> holidays = calendarHolidaysService.getHolidaysForCurrentYear();
        return ResponseBuilder.ok(holidays, "Current year holidays fetched successfully");
    }

    @PutMapping("/update")
    @Operation(summary = "Update calendar holidays", description = "Update an existing calendar holiday")
    public ResponseEntity<GlobalResponse> update(@Valid @RequestBody List<CalendarHolidaysRequest> requests) {
         calendarHolidaysService.updateCalendars(requests);
        return ResponseBuilder.ok(null, "Holidays updated successfully");
    }

    @PatchMapping("/update-isholiday")
    @Operation(summary = "Update isHoliday field", description = "Update only the isHoliday field of a calendar holiday")
    public ResponseEntity<GlobalResponse> updateIsHoliday(
            @RequestBody List<UpdateIsHolidayRequest> request
    ) {
        calendarHolidaysService.updateIsHoliday(request);
        return ResponseBuilder.ok(null,"Holiday Date updated successfully");
    }
}
