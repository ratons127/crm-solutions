package com.betopia.hrm.domain.cashadvance.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CashAdvanceSlabConfigDetailsRequest(

         String serviceChargeType,

         @DecimalMin(value = "0.00", message = "Requested amount must be positive")
         @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
         BigDecimal serviceChargeAmount,


         @DecimalMin(value = "0.00", message = "Requested amount must be positive")
         @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
         BigDecimal fromAmount,


         @DecimalMin(value = "0.00", message = "Requested amount must be positive")
         @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
         BigDecimal toAmount
) {
}
