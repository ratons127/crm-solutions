package com.betopia.hrm.domain.company.request;

import jakarta.validation.constraints.NotNull;

public record CalenderAssignRequest(
        Long id,

        @NotNull(message = "calenderId cannot be null")
        Long calenderId,

        @NotNull(message = "companyId cannot be null")
        Long companyId,

        @NotNull(message = "businessUnitId cannot be null")
        Long businessUnitId,

        @NotNull(message = "workPlaceGroupId cannot be null")
        Long workPlaceGroupId,

        @NotNull(message = "workPlaceId cannot be null")
        Long workPlaceId,

        @NotNull(message = "departmentId cannot be null")
        Long departmentId,

        @NotNull(message = "teamId cannot be null")
        Long teamId,

        @NotNull(message = "description cannot be null")
        String description,

        @NotNull(message = "status cannot be null")
        Boolean status
) {
}
