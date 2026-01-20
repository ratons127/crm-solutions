package com.betopia.hrm.services.company.calendars;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Calendars;
import com.betopia.hrm.domain.company.request.CalendarsRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CalendarsService {

    List<Calendars> getAllCalendars();

    Calendars getCalendarsById(Long id);

    Calendars store(CalendarsRequest request);

    Calendars updateCalendars(Long id, CalendarsRequest request);

    void deleteCalendars(Long id);
}
