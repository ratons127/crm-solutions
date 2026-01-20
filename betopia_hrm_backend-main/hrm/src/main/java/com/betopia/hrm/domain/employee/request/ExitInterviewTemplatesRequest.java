package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.ExitSeparationType;

import java.util.Map;

public record ExitInterviewTemplatesRequest(
        String templateName,
        ExitSeparationType separationType,
        Map<String, Object> questions,
        Boolean isDefault,
        Boolean status
) {
}
