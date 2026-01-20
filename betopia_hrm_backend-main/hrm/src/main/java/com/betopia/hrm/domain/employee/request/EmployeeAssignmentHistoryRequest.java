package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.ChangeType;

import java.util.Date;

public record EmployeeAssignmentHistoryRequest(
        Long id,
        Long employeeId,
        ChangeType changeType,
        Long fromDepartmentId,
        Long toDepartmentId,
        Long fromDesignationId,
        Long toDesignationId,
        Date effectiveDate,
        Long approvedById,
        Long referenceId,
        String remarks,
        Boolean status
) {
}
