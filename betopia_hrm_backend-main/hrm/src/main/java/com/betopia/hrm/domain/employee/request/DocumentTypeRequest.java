package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DocumentTypeRequest(

        Long id,

        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "Category is required")
        CategoryType category,

        String description,
        boolean isRequired,
        boolean isTimeBound,
        int defaultValidityMonths,
        LocalDateTime deletedAt

) {

}