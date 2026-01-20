package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.Departments;

public record ExitClearanceItemRequest(
        Long templateId,
        Departments department,
        String itemName,
        String itemDescription,
        Boolean isMandatory,
        String assigneeRole,
        Integer sequenceOrder,
        Boolean status
) {
}
