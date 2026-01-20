package com.betopia.hrm.domain.employee.request;

public record DetailDto( Long id,              // null => create; non-null => update
                         String grade,
                         Integer maximumMark,
                         Integer minimumMark,
                         Integer averageMark) {
}
