package com.betopia.hrm.services.company.calendarsholidays;

import com.betopia.hrm.domain.company.entity.CalendarHolidays;
import com.betopia.hrm.domain.company.request.CalendarHolidaysRequest;
import com.betopia.hrm.domain.company.request.UpdateIsHolidayRequest;

import java.time.LocalDate;
import java.util.List;

public interface CalendarHolidaysService {

    List<CalendarHolidays> getHolidaysForCurrentYear();

    List<CalendarHolidays> updateCalendars(List<CalendarHolidaysRequest> requests);

    List<CalendarHolidays> updateIsHoliday(List<UpdateIsHolidayRequest> request);

    void updateStatusOfHolidays();

    boolean isHolidayOrWeekend(LocalDate date);
}
