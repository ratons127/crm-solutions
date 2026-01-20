package com.betopia.hrm.domain.company.request;

import com.betopia.hrm.domain.company.enums.WeekendType;

import java.time.LocalDate;

public record CalendarHolidaysRequest(
        Long id,
        LocalDate holidayDate,
        Boolean isHoliday,
        String description,
        String colorCode,
        WeekendType weekendType

) {
}
