package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.DocumentVerificationStatus;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;

public record DocumentVerificationRequest(

        Long id,
        @NotNull(message = "Document ID cannot be null")
        Long employeeDocumentId,
        @NotNull(message = "Document Version ID cannot be null")
        Long employeeDocumentVersionId,
        @NotNull(message = "user ID cannot be null")
        Long userId,

        LocalDateTime verifiedAt,
        String remarks,
        DocumentVerificationStatus status,
        LocalDateTime deletedAt

) {

}