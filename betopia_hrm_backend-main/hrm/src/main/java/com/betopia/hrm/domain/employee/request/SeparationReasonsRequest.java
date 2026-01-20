package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.SeparationType;

public record SeparationReasonsRequest(
        SeparationType separationType,
        String reasonCode,
        String reasonText,
        Boolean isActive,
        Integer displayOrder,
        Boolean status
) {
}
