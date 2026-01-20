package com.betopia.hrm.domain.users.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompanyRequest(
        @NotNull(message = "Company name cannot be null")
        @NotBlank(message = "Company name cannot be blank")
        @Size(min = 1, max = 255, message = "Company name must be between 1 and 255 characters")
        @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Company name can only contain letters, numbers, spaces, and hyphens")
        String name,
        Integer countryId,
        Integer divisionId,
        Integer districtId,
        Integer Integer,
        Integer thana,
        Integer postOffice,
        String shortName,
        @NotBlank(message = "Code cannot be blank")
        @NotNull(message = "Code cannot be null")
        String code,
        String phone,
        String email,
        String websiteUrl,
        String description,
        String address,
        String image,
        String imageUrl,
        Boolean status,
        String currency,
        String timeZone
) {
}
