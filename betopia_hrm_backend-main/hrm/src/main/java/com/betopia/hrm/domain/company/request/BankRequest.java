package com.betopia.hrm.domain.company.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BankRequest(
        @NotNull(message = "Bank name cannot be null")
        @NotBlank(message = "Bank name cannot be blank")
        String name,

        String shortName,
        String bankCode,
        String swiftCode,
        Long countryId,

        String website,
        String supportEmail,
        Boolean status

) {
}
