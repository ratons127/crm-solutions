package com.betopia.hrm.domain.admin.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record CountryRequest(
                @NotNull(message = "Country code cannot be null")
                String code,
                @NotNull(message = "Country name cannot be null")
                String name,
                @NotNull(message = "Country region cannot be null")
                String region
       ) {
}
