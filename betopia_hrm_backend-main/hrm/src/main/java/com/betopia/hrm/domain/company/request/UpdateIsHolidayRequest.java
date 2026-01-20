package com.betopia.hrm.domain.company.request;

import java.time.LocalDate;

public record UpdateIsHolidayRequest(
        Long id,
        Boolean isHoliday,
        LocalDate holidayDate
) {
}
