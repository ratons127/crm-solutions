package com.betopia.hrm.domain.company.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BankBranchRequest(
        @NotNull(message = "Branch name cannot be null")
        @NotBlank(message = "Branch name cannot be blank")
        String branchName,

        @NotNull(message = "Bank ID cannot be null")
        @Positive(message = "Bank ID must be positive")
        Long bankId,


        Long locationId,
        String branchCode,
        String routingNo,
        String swiftCode,
        String email,
        String addressLine1,
        String addressLine2,
        String district,
        Boolean status

) {
}
