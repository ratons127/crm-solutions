package com.betopia.hrm.domain.employee.request;

public record ExitClearanceTemplateRequest(
        String templateName,
        String description,
        Boolean isDefault,
        Boolean status
) {
}
