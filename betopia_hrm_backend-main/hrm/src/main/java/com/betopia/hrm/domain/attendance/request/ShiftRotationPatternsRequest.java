package com.betopia.hrm.domain.attendance.request;

import com.betopia.hrm.domain.attendance.entity.ShiftRotationPatternDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ShiftRotationPatternsRequest(
        Long id,

        // Shift Rotation Pattern fields
        @NotBlank(message = "Pattern name is required")
        @Size(max = 100, message = "Pattern name must be less than 100 characters")
        String patternName,

        Long companyId,

        Long businessUnitId,

        Long workplaceGroupId,

        Long workPlaceId,

        Long departmentId,

        Long teamId,

        String description,

        Long rotationDays,

        Boolean status,

        // Pattern Details - Multiple entries
        @Valid
        @NotNull(message = "Pattern details are required")
        @Size(min = 1, message = "At least one pattern detail is required")
        List<ShiftRotationPatternDetail> shiftRotationPatternDetails
) {

    public record ShiftRotationPatternDetail (
            Long id,
            @NotNull(message = "Day number is required")
            Long dayNumber,
            Long shiftId,
            Boolean isOffDay
    ){}
}
