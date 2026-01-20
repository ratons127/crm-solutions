package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.ApprovalStatus;
import com.betopia.hrm.domain.employee.enums.RequestType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Transfer(
        Long id,
        Long employeeId,
        RequestType requestType,
        Long fromCompanyId,
        Long toCompanyId,
        Long fromWorkplaceId,
        Long toWorkplaceId,
        Long fromDepartmentId,
        Long toDepartmentId,
        Long fromDesignationId,
        Long toDesignationId,
        String reason,
        LocalDate effectiveDate,
        ApprovalStatus approvalStatus,
        Long approvedById,
        LocalDateTime approvedAt,
        Boolean status

) {
}
