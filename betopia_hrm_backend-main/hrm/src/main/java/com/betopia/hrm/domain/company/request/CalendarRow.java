package com.betopia.hrm.domain.company.request;

import java.time.LocalDate;

public record CalendarRow(
        LocalDate dt,
        Integer year,
        String monthName,
        String dayName,
        Boolean isWeekend
) {
}
