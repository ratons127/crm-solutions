package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.employee.enums.ClearanceStatus;
import com.betopia.hrm.domain.employee.enums.Departments;

import java.time.LocalDateTime;

public record EmployeeClearanceChecklistRequest(
        Long separationId,
        Long clearanceItemId,
        Departments department,
        Long assignedTo,
        ClearanceStatus clearanceStatus,
        String remarks,
        String evidencePath,
        LocalDateTime clearedDate,
        Long clearedBy,
        LocalDateTime slaDeadline,
        Boolean isOverdue
) {
}
