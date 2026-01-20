package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.EmployeeDocumentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EmployeeDocumentRequest(
        Long id,

        @NotNull(message = "Employee ID cannot be null")
        @Positive(message = "Employee ID must be positive")
        Long employeeId,

        @NotNull(message = "documentTypeId is required")
        Long documentTypeId,

        String filePath,
        LocalDate issueDate,
        LocalDate expiryDate,
        Boolean status,
        List<EmployeeDocumentVersionRequest> employeeDocumentVersions,
        LocalDateTime deletedAt,

        EmployeeDocumentStatus documentStatus
) {}