package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.SeparationDocumentType;

import java.time.LocalDateTime;

public record SeparationDocumentsRequest(
        Long separationId,
        SeparationDocumentType documentType,
        String documentName,
        String filePath,
        String mimeType,
        Boolean generatedBySystem,
        Long uploadedById,
        Boolean isSigned,
        LocalDateTime signedDate,
        Boolean isEmployeeAccessible
) {
}
