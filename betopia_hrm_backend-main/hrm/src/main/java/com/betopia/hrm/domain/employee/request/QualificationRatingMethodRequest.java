package com.betopia.hrm.domain.employee.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QualificationRatingMethodRequest(
        @NotNull(message = "Code cannot be null")
        String code,
        @NotNull(message = "Name cannot be null")
        String methodName,

        List<QualificationRatingMethodRequestDetails> qualificationRatingMethodDetails ) {
}
