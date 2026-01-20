package com.betopia.hrm.domain.employee.request;

import java.time.LocalDate;

public record EmploymentStatusHistoryRequest(
        Long id,
        Long companyId,
        Long employeeId,
        Long statusId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        String reasonCode,
        String remarks
) {
}
