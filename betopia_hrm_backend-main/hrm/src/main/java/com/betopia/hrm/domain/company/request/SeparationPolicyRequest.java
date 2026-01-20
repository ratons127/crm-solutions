package com.betopia.hrm.domain.company.request;


import com.betopia.hrm.domain.company.enums.SeparationPolicyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SeparationPolicyRequest(

        @NotNull(message = "Company Id cannot be null")
        Long companyId,
        Long workplaceId,

        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        String code,
        String description,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        SeparationPolicyStatus separationPolicyStatus,
        Long previousVersionId,
        Long approvedBy,
        Boolean status

) {
}
