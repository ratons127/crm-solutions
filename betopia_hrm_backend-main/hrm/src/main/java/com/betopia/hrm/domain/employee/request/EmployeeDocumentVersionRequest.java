package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;

public record EmployeeDocumentVersionRequest(
        Long id,
        Long employeeDocumentId,
        String filePath,
        Long userId,
        String remarks,
        Boolean isCurrent
) {
}
