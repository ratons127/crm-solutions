package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.AssignmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActingAssignmentsRequest(
        Long id,
        Long employeeId,
        Long actingRoleId,
        Long fromDepartmentId,
        Long toDepartmentId,
        Long actingSupervisorId,
        AssignmentType assignmentType,
        LocalDate startDate,
        LocalDate endDate,
        String remarks,
        String approvalStatus,
        Long approvedById,
        LocalDateTime approvedAt,
        Boolean status,
        LocalDateTime deletedAt
) {
}
