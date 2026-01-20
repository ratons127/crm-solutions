package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotNull;

public record LeaveGroupAssignRequest(
        Long id,

        @NotNull(message = "leaveTypeId cannot be null")
        Long leaveTypeId,

        Long leaveGroupId,

        @NotNull(message = "companyId cannot be null")
        Long companyId,

        Long businessUnitId,

        Long workPlaceGroupId,

        Long workPlaceId,

        Long departmentId,

        Long teamId,

        String description,

        @NotNull(message = "status cannot be null")
        Boolean status
) {
}
